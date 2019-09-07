package com.ycg.ksh.api.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateFormatUtils;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.service.api.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 任务单的控制层
 *
 * @author 29556
 */

@Controller("mobile.waybill.controller")
@RequestMapping("/mobile/wayBill")
public class WayBillController extends BaseController {

    @Resource
    private WaybillService waybillService;
    @Resource
    private BarCodeService barCodeService;
    @Resource
    private ReceiptService receiptService;
    @Resource
    private WaybillTrackService trackService;
    @Resource
    private ExceptionService exceptionService;

    @Resource
    private GoodsService goodsService;

    //查询每日任务详情列表
    @RequestMapping(value = "/detail/search")
    @ResponseBody
    public JsonResult searchDetail(@RequestBody(required = false) RequestObject body, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (body == null) {
            body = new RequestObject();
        }
        User u = RequestUitl.getUserInfo(request);

        WaybillSerach serach = new WaybillSerach();
        serach.setLikeString(body.get("customerName"));
        serach.setWaybillFettles(StringUtils.integerArray(body.get("bindstatus")));

        //设置时间
        serach.setTime(body.getDate("bindtime"), 0);

        Integer groupId = body.getInteger("groupid");
        serach.setGroupId(groupId);
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
        //context.setAssociate(WaybillAssociate.associateProjectGroup());
        jsonResult.put("page", waybillService.pageDailyWaybill(context));
        return jsonResult;
    }

    @RequestMapping(value = "/myTask/search")
    @ResponseBody
    public JsonResult searchTask(@RequestBody RequestObject object,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("searchTask -> {}", object);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        WaybillSerach serach = new WaybillSerach();
        serach.setUserId(u.getId());
        serach.setLikeString(object.get("likeString"));
        serach.setGroupId(object.getInteger("groupId"));
        serach.setWaybillFettles(StringUtils.integerArray(object.get("waybillStatus")));
        Boolean delay = object.getBoolean("delay");
        if (delay != null) {
            serach.setDelay(delay ? 1 : 2);
        } else {
            serach.setDelay(0);
        }
        //设置时间
        Integer simpleTime = object.getInteger("simpleTime");
        if (simpleTime != null) {
            if (simpleTime < 0) {
                serach.setFirstTime(DateUtils.minOfDay(object.getDate("firstTime")));
                serach.setSecondTime(DateUtils.maxOfDay(object.getDate("secondTime")));
            } else {
                serach.setTime(new Date(), 0 - simpleTime);
            }
        }
        Integer pageSize = object.getInteger("size");
        Integer pageNum = object.getInteger("num");
        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
        context.setAssociate(WaybillAssociate.associateProjectGroup());
        CustomPage<MergeWaybill> page = waybillService.queryMyTaskPage(context);
        if (CollectionUtils.isNotEmpty(page.getCollection())) {
            page.setAttachment(mergeByCreatetime(page.getCollection()));
            page.setCollection(null);
        } else {
            page.setCurrSize(0);
        }
        jsonResult.put("page", page);
        return jsonResult;
    }

    private Map<String, List<MergeWaybill>> mergeByCreatetime(Collection<MergeWaybill> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            Map<String, List<MergeWaybill>> sameDateMap = new TreeMap<String, List<MergeWaybill>>(new Comparator<String>() {
                public int compare(String obj1, String obj2) {
                    return obj2.compareTo(obj1);// 降序排序
                }
            });
            String dateKey;
            for (MergeWaybill mergeWaybill : collection) {
                dateKey = DateFormatUtils.format(mergeWaybill.getCreatetime(), "yyyy-MM-dd");
                List<MergeWaybill> items = sameDateMap.get(dateKey);
                if (items == null) {
                    items = new ArrayList<MergeWaybill>();
                }
                items.add(mergeWaybill);
                sameDateMap.put(dateKey, items);
            }
            return sameDateMap;
        }
        return Collections.emptyMap();
    }
    
    /**
     * 手机端查看任务单详情 update
     * @param waybillId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/myTask/deatil/{waybillId}")
    @ResponseBody
    public JsonResult deatilById(@PathVariable Integer waybillId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String shareId = request.getParameter("shareId");
        logger.info("============waybill---findbyid============> waybillId {} shareId {}", waybillId, shareId);
        Assert.notNull(waybillId, "任务单ID不能为空");
        JsonResult result = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (StringUtils.isNotBlank(shareId)) {
            waybillService.insertShare(u.getId(), Integer.parseInt(shareId), waybillId);
        }
        MergeWaybill waybill = waybillService.getWaybillById(u.getId(), waybillId, WaybillAssociate.associateDetail());
        if (waybill != null) {
            result.put("waybill", waybill);
            result.put("imagePath", SystemUtils.imagepath());// 图片服务器的路径
        }
        return result;
    }

    //项目组任务查询
    @RequestMapping(value = "three/days")
    @ResponseBody
    public JsonResult threeDays(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("three days -> {} ", body);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }
        WaybillSerach serach = new WaybillSerach();
        serach.setAll(true);
        serach.setUserId(u.getId());
        serach.setTime(new Date(), -3);//近三天
        if (serach.getWaybillFettles() == null || serach.getWaybillFettles().length <= 0) {
            serach.setWaybillFettles(new Integer[]{WaybillFettle.BOUND.getCode(), WaybillFettle.ING.getCode(), WaybillFettle.ARRIVE.getCode(), WaybillFettle.RECEIVE.getCode()});
        }
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        jsonResult.put("page", waybillService.pageWaybillSimple(u.getId(), serach, new PageScope(pageNum, pageSize)));
        return jsonResult;
    }

    //项目组任务查询
    @RequestMapping(value = "group/search")
    @ResponseBody
    public JsonResult searchGroup(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("========waybill====searchGroup=====> {} ", body);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }
        WaybillSerach serach = new WaybillSerach();
        serach.setGroupId(body.getInteger("groupId"));
        serach.setAll(serach.getGroupId() == null);
        serach.setUserId(u.getId());
        serach.setLikeString(body.get("likeString"));
        serach.setWaybillFettles(StringUtils.integerArray(body.get("fettles")));
        serach.setReceiptFettles(StringUtils.integerArray(body.get("rptFettles")));
        Boolean delay = body.getBoolean("delay");
        if (delay != null) {
            serach.setDelay(delay ? 1 : 2);
        } else {
            serach.setDelay(0);
        }
        //设置时间
        Integer simpleTime = body.getInteger("simpleTime");
        if (simpleTime != null) {
            if (simpleTime < 0) {
                serach.setFirstTime(DateUtils.minOfDay(body.getDate("firstTime")));
                serach.setSecondTime(DateUtils.maxOfDay(body.getDate("secondTime")));
            } else {
                serach.setTime(new Date(), 0 - simpleTime);
            }
        }
        if (serach.getWaybillFettles() == null || serach.getWaybillFettles().length <= 0) {
            serach.setWaybillFettles(new Integer[]{WaybillFettle.BOUND.getCode(), WaybillFettle.ING.getCode(), WaybillFettle.ARRIVE.getCode(), WaybillFettle.RECEIVE.getCode()});
        }
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
        context.setAssociate(WaybillAssociate.associateConveyance());
        CustomPage<MergeWaybill> page = waybillService.pageMergeWaybill(context);
        if (CollectionUtils.isNotEmpty(page.getCollection())) {
            page.setAttachment(mergeByCreatetime(page.getCollection()));
            page.setCollection(null);
        } else {
            page.setCurrSize(0);
        }
        jsonResult.put("page", page);
        jsonResult.put("userid", u.getId());
        return jsonResult;
    }

    @RequestMapping(value = "/edit/{waybillId}")
    @ResponseBody
    public JsonResult edit(@PathVariable Integer waybillId, HttpServletRequest request) throws Exception {
        logger.info("edit -> waybillId {}", waybillId);
        Assert.notNull(waybillId, "任务单ID不能为空");
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        Waybill waybill = waybillService.getWaybillById(waybillId);
        if (null != waybill) {
            if (waybill.getUserid() - u.getId() != 0) {
                jsonResult.modify(false, "你没有权限编辑该任务单!!!");
            } else {
                jsonResult.put("waybill", waybill);
            }
        } else {
            jsonResult.modify(false, "该运单数据不存在!!!");
        }
        return jsonResult;
    }

    /**
     * 微信回单和异常图片上传
     *
     * @param object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "uploadWx")
    @ResponseBody
    public JsonResult uploadWx(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //保存数据
        User u = RequestUitl.getUserInfo(request);
        String serverIdString = object.get("serverId");//图片上传微信返回路径
        Integer wayBillId = object.getInteger("wayBillId");// 任务单id
        String content = object.get("content");//异常上报描述
        Integer flag = object.getInteger("flag");// 1回单上传,0异常上报
        Collection<String> collection = new ArrayList<String>();
        StringTokenizer token = new StringTokenizer(serverIdString, ",");
        while (token.hasMoreElements()) {
            String serverId = token.nextToken();
            if (StringUtils.isNotBlank(serverId)) {
                collection.add(serverId);
            }
        }
        //将异常图片路径、时间以及相关id信息存储到数据库
        if (1 == flag) {//回单上报
            if (CollectionUtils.isNotEmpty(collection)) {
                receiptService.saveReceipt(u, wayBillId, collection, true);
            } else {
                throw new ParameterException("至少上传一张图片");
            }
        } else {//异常上报
            WaybillException exception = new WaybillException();
            exception.setContent(content);
            exception.setUserid(u.getId());
            exception.setUname(u.getUname());
            exception.setWaybillid(wayBillId);
            exceptionService.saveWaybillException(exception, collection, true);
        }

        return JsonResult.SUCCESS;
    }

    /**
     * 上传回单
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/6/14 15:45
     */
    @RequestMapping(value = "uploadReceipt")
    @ResponseBody
    public JsonResult uploadReceipt(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        String serverIdString = object.get("serverId");//图片上传微信返回路径

        PaperReceipt receipt = object.toJavaBean(PaperReceipt.class);

        receipt.setUserId(loadUserKey(request));

        Collection<String> collection = new ArrayList<String>();
        StringTokenizer token = new StringTokenizer(serverIdString, ",");
        while (token.hasMoreElements()) {
            String serverId = token.nextToken();
            if (StringUtils.isNotBlank(serverId)) {
                collection.add(serverId);
            }
        }
        if (CollectionUtils.isNotEmpty(collection)) {
            receiptService.savePaperReceipt(receipt, collection, true);
        } else {
            throw new ParameterException("至少上传一张图片");
        }
        return JsonResult.SUCCESS;
    }

    /**
     * 选择任务单详细数据或APP详情查询
     *
     * @param request
     * @throws Exception
     * @return
     */
    @RequestMapping(value = "deatil/getWayBill")
    @ResponseBody
    public JsonResult getWayBill(HttpServletRequest request) throws Exception {
        RequestObject object = new RequestObject(request.getParameterMap());
        logger.debug("getWayBill -> {}", object);
        JsonResult result = new JsonResult();
        MergeWaybill waybill = null;
        Integer waybillID = object.getInteger("id");//任务单id
        String code = object.get("barcode");//任务条码号
        String isDetail = object.get("isDetail");//判断是否查询位置、回单、异常等信息。
        if (waybillID != null && waybillID > 0) {
            waybill = waybillService.getWaybillById(waybillID, StringUtils.isNotBlank(isDetail) ? WaybillAssociate.associateDetail() : WaybillAssociate.associateEmpty());
        } else if (StringUtils.isNotBlank(code)) {
            Barcode barcode = barCodeService.validate(code);
            if (barcode != null) {
                waybill = waybillService.getWaybillByCode(barcode.getBarcode(), StringUtils.isNotBlank(isDetail) ? WaybillAssociate.associateDetail() : WaybillAssociate.associateEmpty());
            } else {
                throw new ParameterException(code, "无效的条码");
            }
        }
        if (waybill == null) {
            throw new ParameterException("指定任务单信息不存在");
        }
        if (StringUtils.isNotBlank(isDetail)) {
            result.put("imagePath", SystemUtils.imagepath());// 图片服务器的路径
            result.put("waybill", waybill);
        } else {
            result.putAll(waybill.toMap());
        }
        return result;
    }

    @RequestMapping(value = "listMyReceive")
    @ResponseBody
    public JsonResult listMyReceive(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        if (StringUtils.isNotBlank(user.getMobilephone())) {
            JsonResult jsonResult = new JsonResult();
            if (body == null) {
                body = new RequestObject();
            }
            User u = RequestUitl.getUserInfo(request);

            WaybillSerach serach = new WaybillSerach();
            serach.setMobilphone(user.getMobilephone());
            serach.setLikeString(body.get("searchData"));
            serach.setWaybillFettles(StringUtils.integerArray(body.get("bindstatus")));
            Boolean delay = body.getBoolean("delay");
            if (delay != null) {
                serach.setDelay(delay ? 1 : 2);
            } else {
                serach.setDelay(0);
            }
            //设置时间
            serach.setTime(body.getDate("bindtime"), -30);

            Integer pageSize = body.getInteger("pageSize");
            Integer pageNum = body.getInteger("pageNum");
            WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
            context.setAssociate(WaybillAssociate.associateProjectGroup());
            CustomPage<MergeWaybill> page = waybillService.pageMergeWaybill(context);
            if (CollectionUtils.isNotEmpty(page.getCollection())) {
                page.setAttachment(mergeByCreatetime(page.getCollection()));
                page.setCollection(null);
            } else {
                page.setCurrSize(0);
            }
            jsonResult.put("page", page);
            return jsonResult;
        }
        return JsonResult.SUCCESS;
    }

    /**
     * 我要发货
     *
     * @Author：wangke
     * @description：
     * @Date：15:52 2017/12/15
     */
    @RequestMapping(value = "/saveWaybill")
    @ResponseBody
    public JsonResult saveWaybill(@RequestBody RequestObject object, HttpServletRequest request) {
        logger.info("saveWaybill {} ", object);
        Assert.notNull(object, "运单信息为空");
        User user = RequestUitl.getUserInfo(request);
        WaybillContext waybillContext = WaybillContext.buildContext(user);
        waybillContext.setUpdate(new Waybill());
        waybillContext.setDeliveryNumber(object.get("deliveryNumber"));
        waybillContext.setGroupid(object.getInteger("groupid"));
        waybillContext.setSendType(Constant.CUSTOMER_SELECT_BYID);
        waybillContext.setReceiveType(Constant.CUSTOMER_SELECT_BYID);
        waybillContext.setCustomers(new Customer[]{
                new Customer(Constant.CUSTOMER_TYPE_SEND, object.getInteger("sendCustomerId")),
                new Customer(Constant.CUSTOMER_TYPE_RECEIVER, object.getInteger("customerId"))
        });
        waybillContext.setArriveDay(object.getInteger("arrivalDay"));
        waybillContext.setArriveHour(object.getInteger("arrivalHour"));
        waybillContext.setOrderSummary(object.get("orderSummary"));
        waybillContext.setUserKey(user.getId());
        //waybillContext.addCommodity(new Goods(object.getInteger("goodsQuantity"), object.getDouble("goodsWeight"), object.getDouble("goodsVolume")));
        waybillContext.setCommodities(JSON.parseArray(object.get("goods"), Goods.class));
        logger.debug("==================waybillContext=====================:" + waybillContext);
        waybillService.saveWcWaybill(waybillContext);
        return JsonResult.SUCCESS;
    }

    /**
     * 编辑任务单
     *
     * @Author：wangke
     * @description：
     * @Date：15:52 2017/12/15
     */
    @RequestMapping(value = "/updateWaybill")
    @ResponseBody
    public JsonResult updateWaybill(@RequestBody RequestObject object, HttpServletRequest request) {
        logger.info("updateWaybill -> {}", object);
        Assert.notNull(object, "运单信息为空");
        User user = RequestUitl.getUserInfo(request);
        WaybillContext waybillContext = WaybillContext.buildContext(user);
        waybillContext.setUpdate(new Waybill(object.getInteger("id")));
        waybillContext.setDeliveryNumber(object.get("deliveryNumber"));
        waybillContext.setGroupid(object.getInteger("groupid"));
        waybillContext.setSendType(Constant.CUSTOMER_SELECT_BYID);
        waybillContext.setReceiveType(Constant.CUSTOMER_SELECT_BYID);
        waybillContext.setCustomers(new Customer[]{
                new Customer(Constant.CUSTOMER_TYPE_SEND, object.getInteger("sendCustomerId")),
                new Customer(Constant.CUSTOMER_TYPE_RECEIVER, object.getInteger("customerId"))
        });
        waybillContext.setArriveDay(object.getInteger("arrivalDay"));
        waybillContext.setArriveHour(object.getInteger("arrivalHour"));
        waybillContext.setOrderSummary(object.get("orderSummary"));
        waybillContext.setUserKey(user.getId());
        //waybillContext.addCommodity(new Goods(object.getInteger("goodsQuantity"), object.getDouble("goodsWeight"), object.getDouble("goodsVolume")));

        waybillService.updateWcWaybill(waybillContext);
        return JsonResult.SUCCESS;
    }

    /**
     * TODO 查询未绑定的任务单
     * <p>
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-18 09:35:50
     */
    @RequestMapping(value = "/query/unbound/waybill")
    @ResponseBody
    public JsonResult queryUnBoundWaybil(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("========waybill====queryUnBoundWaybil=====> {} ", body);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }
        WaybillSerach serach = new WaybillSerach();
        if (body.containsKey("groupId")) {
            serach.setGroupId(body.getInteger("groupId"));
        }
        serach.setUserId(u.getId());
        if (body.containsKey("likeString")) {
            serach.setLikeString(body.get("likeString"));
        }

        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");

        serach.setWaybillFettles(new Integer[]{Constant.WAYBILL_STATUS_WAIT});//待绑定的
        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
        context.setAssociate(WaybillAssociate.associateProjectGroup());
        jsonResult.put("page", waybillService.pageMergeWaybill(context));
        jsonResult.put("userid", u.getId());
        return jsonResult;
    }

    /**
     * TODO 微信端任务单绑定是根据主键绑定信息
     * <p>
     *
     * @param waybillId
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-19 11:07:20
     */
    @RequestMapping("/queryById/{waybillId}")
    @ResponseBody
    public JsonResult queryById(@PathVariable Integer waybillId, HttpServletRequest request) {
        logger.debug("queryById=============>waybillId {}", waybillId);

        Assert.notBlank(waybillId, "任务单编号不能为空");

        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", waybillService.getWaybillById(waybillId, WaybillAssociate.associateEmpty()));
        return jsonResult;
    }

    /**
     * 查询所有已绑定的运单，时间降序排序
     * <p>
     *
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 18:26:23
     */
    @RequestMapping(value = "/list/bind")
    @ResponseBody
    public JsonResult listbind(HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", waybillService.listBindWaybill(user.getId()));
        return jsonResult;
    }

    /***
     * 查询货物list
     * @author wangke
     * @date 2018/4/11 10:18
     * @param waybillId
     * @param request
     * @return
     */
    @RequestMapping(value = "/listGoods/{waybillId}")
    @ResponseBody
    public JsonResult listGoods(@PathVariable Integer waybillId, HttpServletRequest request) {
        Assert.notBlank(waybillId, "任务单编号不能为空");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("goods", goodsService.queryByWaybillId(waybillId));
        return jsonResult;
    }

    /**
     * 修改货物信息
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/11 10:33
     */
    @RequestMapping(value = "updateGoods")
    @ResponseBody
    public JsonResult updateGoods(@RequestBody RequestObject object, HttpServletRequest request) {
        User user = RequestUitl.getUserInfo(request);
        WaybillContext waybillContext = WaybillContext.buildContext(user);
        waybillContext.setUpdate(new Waybill(object.getInteger("waybillId")));
        waybillContext.setCommodities(JSON.parseArray(object.get("commodities"), Goods.class));
        /*waybillContext.setDeleteCommodityKeys(JSON.parseArray(object.get("deleteCommodityKeys"), Integer.class));*/
        waybillService.updateGoods(waybillContext);
        return JsonResult.SUCCESS;
    }

    /**
     * 刪除货物信息
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/12 16:37
     */
    @RequestMapping(value = "deleteGoods")
    @ResponseBody
    public JsonResult deleteGoods(@RequestBody RequestObject object, HttpServletRequest request) {
        Assert.notNull(object.get("deleteCommodityKeys"), Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        WaybillContext waybillContext = WaybillContext.buildContext(user);
        waybillContext.setUpdate(new Waybill(object.getInteger("waybillId")));
        waybillContext.setDeleteCommodityKeys(JSON.parseArray(object.get("deleteCommodityKeys"), Integer.class));
        waybillService.deleteGoods(waybillContext);
        return JsonResult.SUCCESS;
    }

}
