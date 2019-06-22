package com.ycg.ksh.api.enterprise.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.constant.OperateType;
import com.ycg.ksh.constant.OrderEventType;
import com.ycg.ksh.constant.ReceiptFettleType;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.LocationTrack;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.OrderSignature;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import com.ycg.ksh.entity.service.enterprise.ShareOrderSearch;
import com.ycg.ksh.entity.service.support.OperateNoteAlliance;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.util.O;
import com.ycg.ksh.service.util.Transform;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单相关控制器
 * <p>
 */
@Controller("enterprise.order.controller")
@RequestMapping("/enterprise/order")
public class OrderController extends BaseController {

    @Resource
    protected CompanyService companyService;
    @Resource
    protected MessageService messageService;
    @Resource
    protected OrderService orderService;
    @Resource
    protected ReceiptService receiptService;
    @Resource
    protected FileService fileService;
    @Resource
    protected SupportService supportService;
    @Resource
    protected LocationTrackService locationTrackService;
    @Resource
    protected ActivityService activityService;
    @Resource
    protected TransferService transferService;

    /**
     * 订单管理菜单入口
     *
     * @return
     */
    @RequestMapping(value = "/manage/{viewkey}")
    public String manage(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/order/" + viewkey;
    }

    /**
     * 订单录入入口
     *
     * @return
     */
    @RequestMapping(value = "/{viewkey}/entrance")
    public String entrance(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/order/" + viewkey + "/entrance";
    }

    /**
     * 运输管理菜单入口
     *
     * @return
     */
    @RequestMapping(value = "/traffic/{viewkey}")
    public String traffic(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        if ("invalid".equalsIgnoreCase(viewkey)) {
            return "/enterprise/order/shipper/invalid";
        } else {
            String filePath = SystemUtils.fileRootPath(Directory.TEMPLATE.sub("order_template.xls"));
            model.addAttribute("template", FileUtils.buildDownload(filePath, "订单模板.xlsx", false));
        }
        return "/enterprise/order/" + viewkey + "/manage";
    }

    /**
     * 电子回单管理菜单入口
     *
     * @return
     */
    @RequestMapping(value = "/receipt/{viewkey}")
    public String ereceipt(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/order/" + viewkey + "/ereceipt";
    }

    /**
     * 订单分享菜单入口
     *
     * @return
     */
    @RequestMapping(value = "/share/{viewkey}")
    public String share(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        model.addAttribute("share", true);
        if ("detail".equalsIgnoreCase(viewkey)) {
            return "/enterprise/order/detail";
        }
        return "/enterprise/order/" + viewkey + "/share";
    }

    /**
     * 扫码绑定
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/code/bind")
    @ResponseBody
    public JsonResult bindCode(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("bind code -> {}", object);
        String qrcode = object.get("code");
        Assert.notBlank(qrcode, "二维码号码不能为空");
        Long orderKey = object.getLong("orderKey");
        Assert.notBlank(orderKey, "要绑码的订单编号不能为空");
        orderService.bindCode(loadUserKey(request), orderKey, qrcode);
        return JsonResult.SUCCESS;
    }

    /**
     * 上报位置
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/loaction")
    @ResponseBody
    public JsonResult loaction(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("loaction -> {}", object);
        Long orderKey = object.getLong("orderKey");
        Assert.notBlank(orderKey, "要绑码的订单编号不能为空");
        LocationTrack locationTrack = object.toJavaBean(LocationTrack.class);

        Assert.notNull(locationTrack, "位置信息不能为空");
//        Assert.notBlank(locationTrack.getHostId(), "关联编号不能为空");
        Assert.notNull(locationTrack.getLocation(), "位置地址不能为空");
        Assert.notNull(locationTrack.getLatitude(), "位置纬度不能为空");
        Assert.notNull(locationTrack.getLongitude(), "位置经度不能为空");

        locationTrack.setHostId(String.valueOf(orderKey));

        Integer ukey = loadUserKey(request);

        JsonResult jsonResult = new JsonResult();

        locationTrackService.saveTrack(ukey, LocationType.ORDER, locationTrack);

        /*LotteryNote lotteryNote = activityService.lotteryValidate(ukey, CoreConstants.LOTTERY_TYPE_SCAN_CODE, orderKey);
        if(lotteryNote != null){
            jsonResult.put("lotteryKey", lotteryNote.getNoteId());//抽奖资格ID
        }*/
        return jsonResult;
    }


    /**
     * 作废回单列表查询
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/receipt/invalid/search")
    @ResponseBody
    public JsonResult invalid(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        OrderSearch search = body.toJavaBean(OrderSearch.class);
        search.setTime(body.getInteger("timeType"));
        //search.setReceiptFettles(StringUtils.integerCollection(body.get("receiptFettle")));
        //search.setFettles(StringUtils.integerCollection(body.get("fettle")));
        search.setPartnerType(PartnerType.SHIPPER);//以货主的角色查询
        search.setInvalid(true);//查询作废的
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("shipper manage -> {}", body);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrder(loadUserKey(request), search, scope));
        return result;
    }

    /**
     * 发货回单列表查询
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/5/25 14:03
     */
    @RequestMapping(value = "/receipt/shipper/search")
    @ResponseBody
    public JsonResult queryShipperReceipt(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        return receiptListResult(request, body, PartnerType.SHIPPER);
    }


    /**
     * 回单列表查询通用方法
     *
     * @param request
     * @param body
     * @param partnerType
     * @return
     * @throws ReflectiveOperationException
     */
    public JsonResult receiptListResult(HttpServletRequest request, RequestObject body, PartnerType partnerType) throws Exception {
        OrderSearch search = body.toJavaBean(OrderSearch.class);
        search.setTime(body.getInteger("timeType"));
        search.setPartnerType(partnerType);
        search.setFettles(convertOrderFettles(body.getInteger("fettle")));//0:全部,1:运输中,4:已到货
        Integer signFettle = body.getInteger("signFettle");
        if (signFettle != null && signFettle > 0) {
            search.setSignFettles(Lists.newArrayList(signFettle));//0:全部,1:正常签收,2:异常签收
        }
        Integer receiptFettle = Optional.ofNullable(body.getInteger("receiptFettle")).orElse(0);//0:全部,1:物流商未签,2:物流商已签,3:收货客户未签,4:收货客户已签,5:三方已签
        Integer flag = Optional.ofNullable(body.getInteger("flag")).orElse(0);//0:全部,1:未生成\未签署,2:已生成\已签署
        Collection<Integer> collection = receiptFettles(partnerType, flag, receiptFettle);
        if (CollectionUtils.isNotEmpty(collection)) {
            search.setReceiptFettles(collection);
        }
        search.setEreceipt(true);

        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrder(loadUserKey(request), search, scope));
        return result;
    }

    /**
     * 承运商回单列表查询
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/5/25 14:03
     */
    @RequestMapping(value = "/receipt/convey/search")
    @ResponseBody
    public JsonResult queryConveyReceipt(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        return receiptListResult(request, body, PartnerType.CONVEY);
    }

    /**
     * 查询选中的回单列表
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/6/27 9:28
     */
    @RequestMapping(value = "/receipt/electoral/search")
    @ResponseBody
    public JsonResult queryEleReceiptList(@RequestBody RequestObject body, HttpServletRequest request) {
        Assert.notNull(body, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        Collection<Long> orderKeys = StringUtils.longCollection(body.get("orderKeys"));
        jsonResult.put("page", orderService.electoralOrderList(loadUserKey(request), orderKeys));
        return jsonResult;
    }

    /**
     * 查询当前用户印章和授权的印章
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/userCertified/search")
    @ResponseBody
    public JsonResult queryUserCertified(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("reslut", userService.getUserCertified(loadUserKey(request)));
        jsonResult.put("pathPrefix", SystemUtils.staticPathPrefix());
        return jsonResult;
    }

    /**
     * 收货方回单列表查询
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/5/25 14:03
     */
    @RequestMapping(value = "/receipt/receirve/search")
    @ResponseBody
    public JsonResult queryReceiveReceipt(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        return receiptListResult(request, body, PartnerType.RECEIVE);
    }


    /**
     * 发货方列表查询
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shipper/manage/search")
    @ResponseBody
    public JsonResult searchShipperManage(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        OrderSearch search = body.toJavaBean(OrderSearch.class);
        search.setTime(body.getInteger("timeType"));
        Integer fettle = body.getInteger("fettle");
        if (null != fettle) {
            search.setFettles(convertOrderFettles(body.getInteger("fettle")));
        }
        search.setSignFettles(StringUtils.integerCollection(body.get("signFettle")));
        //查询条件为某时间
        Date searchDate = body.getDate("searchDate");
        if (null != searchDate) {
            search.setTime(null);
            search.setFirstTime(DateUtils.minOfDay(searchDate));
            search.setSecondTime(DateUtils.maxOfDay(searchDate));
        }
        search.setPartnerType(PartnerType.SHIPPER);//以货主的角色查询
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("shipper manage -> {}", body);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrder(loadUserKey(request), search, scope));
        return result;
    }

    /**
     * 承运商列表查询
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/covey/manage/search")
    @ResponseBody
    public JsonResult searchConveyManage(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        OrderSearch search = body.toJavaBean(OrderSearch.class);
        search.setTime(body.getInteger("timeType"));
        Integer fettle = body.getInteger("fettle");
        if (null != fettle) {
            search.setFettles(convertOrderFettles(body.getInteger("fettle")));
        }
        search.setSignFettles(StringUtils.integerCollection(body.get("signFettle")));
        //查询条件为某时间
        Date searchDate = body.getDate("searchDate");
        if (null != searchDate) {
            search.setTime(null);
            search.setFirstTime(DateUtils.minOfDay(searchDate));
            search.setSecondTime(DateUtils.maxOfDay(searchDate));
        }
        search.setPartnerType(PartnerType.CONVEY);//以货主的角色查询
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("covey manage -> {}", body);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrder(loadUserKey(request), search, scope));
        return result;
    }

    /**
     * 收货方列表查询
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/receive/manage/search")
    @ResponseBody
    public JsonResult searchReceiveManage(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        OrderSearch search = body.toJavaBean(OrderSearch.class);
        search.setTime(body.getInteger("timeType"));
        Integer fettle = body.getInteger("fettle");
        if (null != fettle) {
            search.setFettles(convertOrderFettles(body.getInteger("fettle")));
        }
        search.setSignFettles(StringUtils.integerCollection(body.get("signFettle")));
        //查询条件为某时间
        Date searchDate = body.getDate("searchDate");
        if (null != searchDate) {
            search.setTime(null);
            search.setFirstTime(DateUtils.minOfDay(searchDate));
            search.setSecondTime(DateUtils.maxOfDay(searchDate));
        }
        search.setPartnerType(PartnerType.RECEIVE);//以货主的角色查询
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("receive manage -> {}", body);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrder(loadUserKey(request), search, scope));
        return result;
    }


    /**
     * 查询未绑码的订单
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shipper/bind/search")
    @ResponseBody
    public JsonResult shipperBindSearch(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        OrderSearch search = new OrderSearch();
        search.setLikeString(body.get("likeString"));
        //search.setFettle(new Integer[]{OrderFettleType.DEFAULT.getCode()});//未绑码的
        //search.setPartnerType(PartnerType.SHIPPER);//以货主的角色查询
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("shipper bind search -> {}", search);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageBindOrder(loadUserKey(request), search, scope));
        return result;
    }

    /**
     * 发货商删除订单
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shipper/manage/delete")
    @ResponseBody
    public JsonResult deleteShipperManage(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.debug("delete order ->{}", body);
        List<Long> ids = JSONArray.parseArray(body.get("ids"), Long.class);
        try {
            orderService.deleteOrders(RequestUitl.getUserInfo(request).getId(), PartnerType.SHIPPER, ids);
        } catch (BusinessException | ParameterException e) {
            return new JsonResult(false, e.getFriendlyMessage());
        }
        return JsonResult.SUCCESS;
    }

    /**
     * 承运商订单删除
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("/conveyer/manage/delete")
    @ResponseBody
    public JsonResult delConveyer(@RequestBody(required = false) RequestObject body, HttpServletRequest request) {
        logger.debug("conveyer/manage/delete ->{}", body);
        List<Long> ids = JSONArray.parseArray(body.get("ids"), Long.class);
        try {
            orderService.deleteOrders(loadUserKey(request), PartnerType.CONVEY, ids);
        } catch (BusinessException | ParameterException e) {
            return new JsonResult(false, e.getFriendlyMessage());
        }
        return JsonResult.SUCCESS;
    }

    /**
     * @Title: 撤销电子回单
     * @author wyj
     * @date 2018/5/14 15:16
     */
    @RequestMapping(value = "/shipper/manage/cancle")
    @ResponseBody
    public JsonResult cancleShipperManage(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.debug("cancle order ->{}", body);
        User user = RequestUitl.getUserInfo(request);
        List<Long> ids = JSONArray.parseArray(body.get("ids"), Long.class);
        try {
            orderService.invalid(user.getId(), ids);
        } catch (BusinessException | ParameterException e) {
            return new JsonResult(false, e.getFriendlyMessage());
        }
        return JsonResult.SUCCESS;
    }

    /**
     * 查询已作废的订单详情和物料集合
     *
     * @param orderKey
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/17 15:36
     */
    @RequestMapping(value = "/detail/cancle/{orderKey}")
    @ResponseBody
    public JsonResult getCancleOrderDetailById(@PathVariable Long orderKey, HttpServletRequest request) throws Exception {
        Assert.notBlank(orderKey, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        OrderAlliance alliance = orderService.getOrderAlliance(user.getId(), orderKey, false);
        if (alliance != null) {
            jsonResult.put("reslut", alliance);
            //订单的操作记录
            jsonResult.put("operates", completeOperateNote(supportService.listOperateNoteByType(OperateType.ORDER, alliance.getId())));
        }
        jsonResult.put("pathPrefix", SystemUtils.staticPathPrefix());
        return jsonResult;
    }

    @RequestMapping(value = "/shipper/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipperImportExcel(HttpServletRequest request) throws Exception {
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        RequestObject requestObject = new RequestObject(request.getParameterMap());
        return importByFile(file, loadUserKey(request), requestObject.getLong("templateKey"), PartnerType.SHIPPER);
    }

    private JsonResult importByFile(File file, Integer uKey, Long templateKey, PartnerType partnerType) throws IOException {
        Assert.notNull(file, "请上传一个文件!!!");
        Assert.notBlank(templateKey, "请选择一个导入模板");
        FileEntity fileEntity = new FileEntity(file);
        String path = fileEntity.getPath();
        try {
            logger.info("开始处理文件 -> {}", fileEntity);
            fileEntity = fileService.saveByTemplateFile(uKey, partnerType, templateKey, fileEntity);
            logger.info("文件处理完成 -> {}", fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (fileEntity != null && fileEntity.getFailureCount() != null && fileEntity.getFailureCount() > 0) {
            jsonResult.modify(false, "存在异常数据</br>成功导入" + fileEntity.getSuccessCount() + "条,失败" + fileEntity.getFailureCount() + "条</br>请下载处理后上传导入");
            fileEntity.setUrl(FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix(fileEntity.getAliasName(), fileEntity.getSuffix())));
            jsonResult.put("result", fileEntity);
        } else {
            jsonResult.message("操作成功</br>成功导入" + fileEntity.getSuccessCount() + "条");
        }
        return jsonResult;
    }


    @RequestMapping(value = "/coveyer/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult coveyerImportExcel(HttpServletRequest request) throws Exception {
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        RequestObject requestObject = new RequestObject(request.getParameterMap());
        return importByFile(file, loadUserKey(request), requestObject.getLong("templateKey"), PartnerType.CONVEY);
    }


    /**
     * 根据二维码号查询其绑定的订单信息
     *
     * @param qrcode
     * @param request
     * @return
     * @throws Exception
     * @author wangke
     */
    @RequestMapping(value = "/detail/code/{qrcode}")
    @ResponseBody
    public JsonResult getDetailByCode(@PathVariable String qrcode, HttpServletRequest request) throws Exception {
        Assert.notBlank(qrcode, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        OrderAlliance alliance = orderService.getAllianceByCode(loadUserKey(request), qrcode);
        if (alliance != null) {
            jsonResult.put("reslut", alliance);
            //订单的操作记录
            jsonResult.put("operates", completeOperateNote(supportService.listOperateNoteByType(OperateType.ORDER, alliance.getId())));
        }
        jsonResult.put("pathPrefix", SystemUtils.staticPathPrefix());
        return jsonResult;
    }

    /**
     * 查询订单详情和物料集合
     *
     * @param orderKey
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/17 15:36
     */
    @RequestMapping(value = "/orderDetail/{orderKey}")
    @ResponseBody
    public JsonResult getOrderDetailById(@PathVariable Long orderKey, HttpServletRequest request) throws Exception {
        Assert.notBlank(orderKey, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        OrderAlliance alliance = orderService.getOrderAlliance(user.getId(), orderKey, true);
        if (alliance != null) {
            jsonResult.put("reslut", alliance);
            //订单的操作记录
            jsonResult.put("operates", completeOperateNote(supportService.listOperateNoteByType(OperateType.ORDER, alliance.getId())));
        }
        jsonResult.put("pathPrefix", SystemUtils.staticPathPrefix());
        jsonResult.put("imagePath", imagepath());
        return jsonResult;
    }

    /**
     * 查询订单详情和物料集合
     *
     * @param orderKey
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/17 15:36
     */
    @RequestMapping(value = "/share/detail/{orderKey}")
    @ResponseBody
    public JsonResult detailShare(@PathVariable Long orderKey, HttpServletRequest request) throws Exception {
        Assert.notBlank(orderKey, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        OrderAlliance alliance = orderService.alliance(user.getId(), orderKey, O.SHARE_DETAILS);
        if (alliance != null) {
            jsonResult.put("reslut", alliance);
            //订单的操作记录
            jsonResult.put("operates", completeOperateNote(supportService.listOperateNoteByType(OperateType.ORDER, alliance.getId())));
        }
        jsonResult.put("pathPrefix", SystemUtils.staticPathPrefix());
        jsonResult.put("imagePath", imagepath());
        return jsonResult;
    }

    private Collection<OperateNoteAlliance> completeOperateNote(Collection<OperateNoteAlliance> alliances) {
        if (CollectionUtils.isNotEmpty(alliances)) {
            return alliances.stream().filter(a -> OrderEventType.convert(a.getLogType()).isShow()).peek(p -> {
                if (StringUtils.isBlank(p.getArea())) {
                    OrderEventType eventType = OrderEventType.convert(p.getLogType());
                    if (eventType == OrderEventType.RECEIVE || eventType == OrderEventType.LOCATE || eventType == OrderEventType.UPLOADRECEIPT) {
                        int index = p.getLogContext().indexOf(", ");
                        if (index >= 0) {
                            p.setLogContext(p.getLogContext().substring(index + 2));
                        }
                        p.setArea(RegionUtils.analyze(p.getLogContext(), 2));
                    }
                }
            }).collect(Collectors.toList());
        }
        return alliances;
    }

    /**
     * 保存订单(非模板)
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */

    /*@RequestMapping(value = "/shipper/save/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipperSaveOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("save order -> {}", requestObject);
        modifyOrder(PartnerType.SHIPPER, loadUserKey(request), requestObject, true);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/coveyer/save/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult coveyerSaveOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("save order -> {}", requestObject);
        modifyOrder(PartnerType.CONVEY, loadUserKey(request), requestObject, true);
        return JsonResult.SUCCESS;
    }*/
    private void modifyOrder(PartnerType partnerType, Integer userKey, RequestObject requestObject) {
        String orderString = requestObject.get("order");
        Assert.notBlank(orderString, "订单信息不能为空");
        OrderTemplate orderTemplate = Globallys.toJavaObject(orderString, OrderTemplate.class);
        String commodityString = requestObject.get("commodities");
        Assert.notBlank(commodityString, "货物信息不能为空");
        orderTemplate.setCommodities(Globallys.toJavaObjects(commodityString, OrderCommodity.class));
        String extraString = requestObject.get("extra");
        if (StringUtils.isNotBlank(extraString)) {
            orderTemplate.setOrderExtra(Globallys.toJavaObject(extraString, OrderExtra.class));
        }
        String customDataString = requestObject.get("customDatas");
        if (StringUtils.isNotBlank(customDataString)) {
            orderTemplate.setCustomDatas(Globallys.toJavaObjects(customDataString, CustomData.class));
        }
        orderService.modifyOrder(userKey, partnerType, orderTemplate);
    }

    /**
     * 转换订单信息
     *
     * @param requestObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/convert/order")
    @ResponseBody
    public JsonResult convertOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Long orderKey = requestObject.getLong("orderKey");
        Assert.notBlank(orderKey, "订单编号不能为空");

        OrderAlliance alliance = orderService.alliance(loadUserKey(request), orderKey, O.DETAILS);

        jsonResult.put("orderKey", orderKey);
        jsonResult.put("results", Transform.transform(alliance));

        return jsonResult;
    }

    /**
     * 修改订单
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult editOrder(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("edit order -> {}", body);
        modifyOrder(PartnerType.SHIPPER, loadUserKey(request), body);
        return JsonResult.SUCCESS;
    }

    /**
     * 修改订单(非模板)
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shipper/edit/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipperEditOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("save order -> {}", requestObject);
        modifyOrder(PartnerType.SHIPPER, loadUserKey(request), requestObject);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/coveyer/edit/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult coveyerEditOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("save order -> {}", requestObject);
        modifyOrder(PartnerType.CONVEY, loadUserKey(request), requestObject);
        return JsonResult.SUCCESS;
    }


    /**
     * 生成回单 单个
     *
     * @param orderKey
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/build/{orderKey}")
    @ResponseBody
    public JsonResult build(@PathVariable Long orderKey, HttpServletRequest request) throws Exception {
        logger.info("build -> {}", orderKey);
        User user = loadUser(request);
        orderService.buildReceipt(user.getId(), orderKey);
        return JsonResult.SUCCESS;
    }

    /**
     * 生成回单 批量
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/build")
    @ResponseBody
    public JsonResult builds(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("builds -> {}", object);
        String keyString = object.get("orderKeys");
        Assert.notBlank(keyString, "至少选择一项数据");
        User user = loadUser(request);
        int count = orderService.buildReceipt(user.getId(), StringUtils.longCollection(keyString));
        if (count > 0) {
            return new JsonResult(false, "有[" + count + "]条数据电子回单生成异常");
        }
        return JsonResult.SUCCESS;
    }


    /**
     * 订单签署
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/18 14:22
     */
    @RequestMapping(value = "/Sign")
    @ResponseBody
    public JsonResult orderSign(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("orderSign {}", body);
        Assert.notNull(body, Constant.PARAMS_ERROR);
        User user = loadUser(request);
        OrderSignature orderSignature = body.toJavaBean(OrderSignature.class);
        String exceptionContent = null;
        Integer flag = body.getInteger("exception");
        if (flag != null && flag == 1) {
            exceptionContent = body.get("content");
            Assert.notBlank(exceptionContent, "异常内容不能为空");
        }
        //@TODO 需要接收用户输入的验证码，非空校验
        String code = body.get("code");
        Assert.notBlank(code, "验证码不能为空");
        orderSignature.setUserId(user.getId());
        receiptService.signature(code, orderSignature, exceptionContent);
        return JsonResult.SUCCESS;
    }


    /**
     * @Title: 到货
     * @author wyj
     * @date 2018/6/12 18:32
     */
    @RequestMapping(value = "/arrivel")
    @ResponseBody
    public JsonResult orderArrivel(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("orderSign {}", body);
        Assert.notNull(body, Constant.PARAMS_ERROR);
        List<Long> orderIds = JSONArray.parseArray(body.get("orderIds"), Long.class);
        orderService.modifyOrderArrivelStatus(loadUserKey(request), orderIds);
        return JsonResult.SUCCESS;
    }

    /**
     * @Title: 微信端-单个到货 *需要校验是否是当前公司
     * @author wyj
     * @date 2018/6/12 18:32
     */
    @RequestMapping(value = "/arrivel/{orderId}")
    @ResponseBody
    public JsonResult orderArrivel(@PathVariable Long orderId, HttpServletRequest request) throws Exception {
        logger.info("order arrive - one {}", orderId);
        orderService.modifyOneOrderArrivelStatus(loadUserKey(request), orderId);
        return JsonResult.SUCCESS;
    }

    /**
     * 下载电子回单PDF文件
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/20 11:41
     */
    @RequestMapping(value = "down/order")
    @ResponseBody
    public JsonResult downOrderList(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("down receipt - > {}", body);
        Collection<Long> orderKeys = StringUtils.longCollection(body.get("orderIds"));
        Assert.notEmpty(orderKeys, "订单ID为空");
        JsonResult jsonResult = new JsonResult();
        FileEntity fileEntity = receiptService.exportReceipt(loadUserKey(request), orderKeys);
        logger.info("fileEntity -> {}", fileEntity);
        if (fileEntity.getCount() > 0) {
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), "电子回单文件.zip", false));
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
        } else {
            jsonResult.modify(false, "无可下载的电子回单信息");
        }
        return jsonResult;
    }

    /**
     * 分组查询
     */
    private JsonResult conciseSearch(HttpServletRequest request, RequestObject body, PartnerType partnerType) throws Exception {
        OrderSearch search = new OrderSearch();
        search.setPartnerType(partnerType);
        search.setLikeString(body.get("likeString"));
        search.setTime(body.getInteger("timeType"));
        search.setFirstTime(DateUtils.minOfDay(body.getDate("firstTime")));
        search.setSecondTime(DateUtils.maxOfDay(body.getDate("secondTime")));
        Integer signFettle = body.getInteger("signFettle");
        if (signFettle != null && signFettle > 0) {
            search.setSignFettles(Lists.newArrayList(signFettle));//0:全部,1:正常签收,2:异常签收
        }
        //回单状态(0:全部,1:物流商未签,2:物流商已签,3:收货客户未签,4:收货客户已签,5：三方已签)
        Integer receiptFettle = Optional.ofNullable(body.getInteger("signFettle")).orElse(0);
        Collection<Integer> collection = receiptFettles(partnerType, 0, receiptFettle);
        if (CollectionUtils.isNotEmpty(collection)) {
            search.setReceiptFettles(collection);
        }

        search.setEreceipt(true);

        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("concise search -> {} {} {}", body, partnerType, scope);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrderConcise(loadUserKey(request), search, scope));
        return result;
    }

    //分组查询-发货方
    @RequestMapping(value = "/shipper/concise/search")
    @ResponseBody
    public JsonResult shipperConcise(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        return conciseSearch(request, body, PartnerType.SHIPPER);
    }

    //分组查询-收货方
    @RequestMapping(value = "/receive/concise/search")
    @ResponseBody
    public JsonResult receiveConcise(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        return conciseSearch(request, body, PartnerType.RECEIVE);
    }

    //分组查询-承运方
    @RequestMapping(value = "/convey/concise/search")
    @ResponseBody
    public JsonResult conveyConcise(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        return conciseSearch(request, body, PartnerType.CONVEY);
    }

    /**
     * 订单状态，回单状态 转换查询数组
     *
     * @param partner
     * @param flag          0:全部,1:未生成\未签署,2:已生成\已签署
     * @param receiptFettle 0:全部,1:物流商未签,2:物流商已签,3:收货客户未签,4:收货客户已签,5:三方已签
     * @return
     */
    public Collection<Integer> receiptFettles(PartnerType partner, Integer flag, Integer receiptFettle) {
        if (flag <= 0 && receiptFettle <= 0) {
            return null;
        }
        return Arrays.stream(ReceiptFettleType.values()).filter(f -> {
            if (partner.shipper()) {//发货方
                if (flag == 1) {
                    return f == ReceiptFettleType.DEFAULT;
                }//未生成电子回单
                if (flag == 2) {
                    return f != ReceiptFettleType.DEFAULT;
                }//已生成电子回单
            } else if (partner.receive()) {//收货方
                if (flag == 1) {
                    return f != ReceiptFettleType.SSIGN && f != ReceiptFettleType.YSIGN;
                }//未签署
                if (flag == 2) {
                    return f == ReceiptFettleType.SSIGN || f == ReceiptFettleType.YSIGN;
                }//已签署
            } else if (partner.convey()) {//承运方
                if (flag == 1) {
                    return f != ReceiptFettleType.CSIGN && f != ReceiptFettleType.YSIGN;
                }//未签署
                if (flag == 2) {
                    return f == ReceiptFettleType.CSIGN || f == ReceiptFettleType.YSIGN;
                }//已签署
            }
            return flag == 0;
        }).filter(f -> {
            if (receiptFettle == 1) {//物流商未签
                return f != ReceiptFettleType.CSIGN && f != ReceiptFettleType.YSIGN;
            } else if (receiptFettle == 2) {//物流商已签
                return f == ReceiptFettleType.CSIGN || f == ReceiptFettleType.YSIGN;
            } else if (receiptFettle == 3) {//收货客户未签
                return f != ReceiptFettleType.SSIGN && f != ReceiptFettleType.YSIGN;
            } else if (receiptFettle == 4) {//收货客户已签
                return f == ReceiptFettleType.SSIGN || f == ReceiptFettleType.YSIGN;
            } else if (receiptFettle == 5) {//三方已签
                return f == ReceiptFettleType.YSIGN;
            }
            return receiptFettle == 0;
        }).map(ReceiptFettleType::getCode).collect(Collectors.toList());
    }

    private JsonResult conciseDay(HttpServletRequest request, RequestObject body, PartnerType partnerType) throws Exception {
        OrderSearch search = new OrderSearch();
        Date dayTime = body.getDate("dayTime");
        Assert.notNull(dayTime, "按天查询,指定日期不能为空");
        search.setTime(body.getDate("dayTime"), 0);//dayTime 	要查询的日期
        search.setPartnerType(partnerType);
        search.setLikeString(body.get("likeString"));//二维码\订单编号\送货单号\收货客户\收货地址等
        search.setShipperKey(body.getLong("shipperKey"));// 发货方编号(收货方和承运方查询时必填)
        search.setReceiveKey(body.getLong("receiveKey"));// 收货方编号(发货方和承运方查询时必填)
        search.setConveyKey(body.getLong("conveyKey"));//承运方编号(发货方和收货方查询时必填)
        Integer signFettle = body.getInteger("signFettle");
        if (signFettle != null && signFettle > 0) {
            search.setSignFettles(Lists.newArrayList(signFettle));//0:全部,1:正常签收,2:异常签收
        }
        //标记(0:全部,1:未生成\未签署,2:已生成\已签署)
        Integer flag = Optional.ofNullable(body.getInteger("flag")).orElse(0);
        //回单状态(0:全部,1:物流商未签,2:物流商已签,3:收货客户未签,4:收货客户已签,5：三方已签)

        Integer receiptFettle = Optional.ofNullable(body.getInteger("fettle")).orElse(0);
        Collection<Integer> collection = receiptFettles(partnerType, flag, receiptFettle);
        if (CollectionUtils.isNotEmpty(collection)) {
            search.setReceiptFettles(collection);
        }
        search.setEreceipt(true);

        //PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("concise day -> {}", search);
        JsonResult result = new JsonResult();
        result.put("results", orderService.listOrder(loadUserKey(request), search));
        return result;
    }

    //按天查询订单简要信息-发货方
    @RequestMapping(value = "/shipper/concise/day")
    @ResponseBody
    public JsonResult shipperDay(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        body.stringIsNull("receiveKey", "发货方按天查询, 收货方编号不能为空");
        body.stringIsNull("conveyKey", "发货方按天查询, 承运方编号不能为空");
        return conciseDay(request, body, PartnerType.SHIPPER);
    }

    //按天查询订单简要信息-收货方
    @RequestMapping(value = "/receive/concise/day")
    @ResponseBody
    public JsonResult receiveDay(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        body.stringIsNull("conveyKey", "收货方按天查询, 承运方编号不能为空");
        body.stringIsNull("shipperKey", "收货方按天查询, 发货方编号不能为空");
        return conciseDay(request, body, PartnerType.RECEIVE);
    }

    //按天查询订单简要信息-承运方
    @RequestMapping(value = "/convey/concise/day")
    @ResponseBody
    public JsonResult conveyDay(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        body.stringIsNull("receiveKey", "承运方按天查询, 收货方编号不能为空");
        body.stringIsNull("shipperKey", "承运方按天查询, 发货方编号不能为空");
        return conciseDay(request, body, PartnerType.CONVEY);
    }


    @RequestMapping(value = "/commodity/{orderKey}")
    @ResponseBody
    public JsonResult commodity(@PathVariable Long orderKey, HttpServletRequest request) throws Exception {
        Assert.notBlank(orderKey, "订单编号不能为空");
        User user = loadUser(request);
        logger.info("commodity() -> orderKey {}", orderKey);
        JsonResult result = new JsonResult();
        Collection<OrderCommodity> commodities = orderService.listCommodity(user.getId(), orderKey);
        int quantity = 0, boxCount = 0;
        double volume = 0, weight = 0;
        if (CollectionUtils.isNotEmpty(commodities)) {
            for (OrderCommodity commodity : commodities) {
                if (commodity.getBoxCount() != null) {
                    boxCount = boxCount + commodity.getBoxCount();
                }
                if (commodity.getQuantity() != null) {
                    quantity = quantity + commodity.getQuantity();
                }
                if (commodity.getVolume() != null) {
                    volume = volume + commodity.getVolume();
                }
                if (commodity.getWeight() != null) {
                    weight = weight + commodity.getWeight();
                }
            }
        }
        result.put("quantity", quantity);
        result.put("boxCount", boxCount);
        result.put("volume", volume);
        result.put("weight", weight);
        result.put("commodities", commodities);
        return result;
    }


    @RequestMapping("/share/order")
    @ResponseBody
    public JsonResult shareOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("share order -> {}", requestObject);
        Collection<Long> orderKeys = StringUtils.longCollection(requestObject.get("orderKeys"));
        Assert.notEmpty(orderKeys, "请选择要分享的订单信息");
        Collection<Long> customerKeys = StringUtils.longCollection(requestObject.get("customerKeys"));
        Assert.notEmpty(orderKeys, "请选择要分享的企业信息");
        transferService.shareOrder(loadUserKey(request), orderKeys, customerKeys);
        return new JsonResult(true, "已成功将" + orderKeys.size() + "条订单信息分享到" + customerKeys.size() + "个对象");
    }


    @RequestMapping("/share/targets")
    @ResponseBody
    public JsonResult shareTargets(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("share targets -> {}", requestObject);
        String likeString = requestObject.get("likeString");
        Integer type = requestObject.getInteger("type");

        JsonResult result = new JsonResult();
        result.put("targets", transferService.listTargets(loadUserKey(request), type, likeString));
        return result;
    }

    @RequestMapping("/share/search")
    @ResponseBody
    public JsonResult shareSearch(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("share search -> {}", requestObject);

        ShareOrderSearch search = new ShareOrderSearch();
        search.setLikeString(requestObject.get("likeString"));
        search.setTime(requestObject.getInteger("timeType"));
        //search.setFirstTime(DateUtils.minOfDay(requestObject.getDate("firstTime")));
        //search.setSecondTime(DateUtils.maxOfDay(requestObject.getDate("secondTime")));
        Integer uploadReceipt = requestObject.getInteger("uploadReceipt");
        if (uploadReceipt != null) {
            search.setUploadReceipt(uploadReceipt == 1);
        }
        search.setTargetKey(requestObject.getLong("targetKey"));
        search.setUserKey(loadUserKey(request));
        search.setFrom(requestObject.getBoolean("flag"));
        PageScope scope = new PageScope(requestObject.getInteger("num"), 8);
        JsonResult result = new JsonResult();
        result.put("page", transferService.pageOrderByShare(search, scope));
        return result;
    }

    /**
     * 修改配送单号
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("/update/deliveryNo")
    @ResponseBody
    public JsonResult modifyDeliveryNo(@RequestBody RequestObject body, HttpServletRequest request) {
        Long orderKey = body.getLong("orderKey");
        Assert.notBlank(orderKey, "订单编号不能为空");
        String deliveryNo = body.get("deliveryNo");
        Assert.notBlank(deliveryNo, "配送单号不能为空");
        orderService.updateDeliveryNo(orderKey, deliveryNo);
        return JsonResult.SUCCESS;
    }

    /**
     * 开发请不要使用.给余维测试使用.用于批量绑单推送数据给tms使用
     */
    @RequestMapping("/special/batch/bind/test")
    @ResponseBody
    public JsonResult batchBindForTest(@RequestBody RequestObject body, HttpServletRequest request, HttpServletResponse response) {
        logger.info("body: ->{}", body);
        List<Long> orderKeys = JSONArray.parseArray(body.get("orderKeys"), Long.class);
        List<String> qrcodes = JSONArray.parseArray(body.get("qrcodes"), String.class);
        orderService.bindCodeForTest(56, orderKeys, qrcodes);
        return JsonResult.SUCCESS;

    }

    /**
     * 评价
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping("/evaluation")
    @ResponseBody
    public JsonResult evaluation(@RequestBody RequestObject body, HttpServletRequest request) {
        Long orderKey = body.getLong("orderKey");
        Assert.notBlank(orderKey, "订单编号不能为空");
        Integer evaluation = body.getInteger("evaluation");
        Assert.notNull(evaluation, "请至少选择一项");
        orderService.modifyEvaluation(orderKey, evaluation);
        return JsonResult.SUCCESS;
    }

    /**
     * 异常收货
     *
     * @return
     */
    @RequestMapping("/modify/exception")
    @ResponseBody
    public JsonResult modifyException(@RequestBody RequestObject body, HttpServletRequest request) {
        Collection<Long> orderKeys = StringUtils.longCollection(body.get("orderKeys"));
        Assert.notEmpty(orderKeys, "订单编号不能为空");
        String content = body.get("content");
        Assert.notBlank(content, "异常描述不能为空");
        Integer type = body.getInteger("type");
        Assert.notBlank(type, "异常类型不能为空");
        orderService.modifyException(orderKeys, content, loadUserKey(request), type);
        return JsonResult.SUCCESS;
    }
}
