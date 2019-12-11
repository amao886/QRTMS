package com.ycg.ksh.api.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.backstage.model.WayBillCondition;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.InterfaceHepler;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.service.api.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 任务单、回单、异常、轨迹
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-13 10:28:24
 */
@Controller("backstage.trace.controller")
@RequestMapping("/backstage/trace")
public class WaybillController extends BaseController {

    @Resource
    private WaybillService waybillService;
    @Resource
    private BarCodeService barcodeService;
    @Resource
    private CustomerService customerService;
    @Resource
    private ProjectGroupService projectGroupService;
    @Resource
    private ReceiptService receiptService;
    @Resource
    private WaybillTrackService trackService;
    @Resource
    private ExceptionService exceptionService;
    @Resource
    private SmsService smsService;
    @Resource
    private FileService fileService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 我要发货
     * <p>
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/send/goods")
    public String sendGoods(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User u = RequestUitl.getUserInfo(request);
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
        if (CollectionUtils.isNotEmpty(groupList)) {
            model.addAttribute("groups", groupList);
            Integer groupId = body.getInteger("groupid");
            if (null != groupId) {
                RequestUitl.modifyGroupKey(request, groupId);
            }else{
                body.put("groupid", RequestUitl.getGroupKey(request, 0));
            }
        }
        model.addAttribute("search", body);
        return "/backstage/waybill/send_goods";
    }

    /**
     * 我要发货-数据保存
     * <p>
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/send/goods/save")
    @ResponseBody
    public JsonResult saveSendGoods(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, Constant.PARAMS_ERROR);
        object.stringIsNull("commodities", Constant.GOODS_PARAMS_ERROR);
        object.stringIsNull("sender", "发货信息不能为空");
        object.stringIsNull("receiver", "收货信息不能为空");
        User user = RequestUitl.getUserInfo(request);
        object.put("userid", user.getId());

        WaybillContext context = WaybillContext.buildContext(user);
        context.setUpdate(object.toJavaBean(Waybill.class));
        //(1:收货客户,2:发货客户)
        boolean saveSend = object.getBoolean("saveSend");
        context.setSendType(saveSend ? Constant.CUSTOMER_CLIENT_SAVE : Constant.CUSTOMER_CLIENT_NONE);
        Customer sender = JSON.parseObject(object.get("sender"), Customer.class);
        boolean saveReceive = object.getBoolean("saveReceive");
        context.setReceiveType(saveReceive ? Constant.CUSTOMER_CLIENT_SAVE : Constant.CUSTOMER_CLIENT_NONE);
        Customer receiver = JSON.parseObject(object.get("receiver"), Customer.class);
        context.setCustomers(new Customer[]{sender, receiver});
        context.setArriveDay(object.getInteger("arriveDay"));
        context.setArriveHour(object.getInteger("arriveHour"));
        context.setCommodities(JSON.parseArray(object.get("commodities"), Goods.class));
        waybillService.save(context);
        return JsonResult.SUCCESS;
    }


    /**
     * 查询任务单
     * <p>
     *
     * @param search
     * @param model
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-13 10:28:43
     */
    @RequestMapping(value = "/search")
    public String search(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User u = RequestUitl.getUserInfo(request);
        List<ProjectGroup> projectGroups = projectGroupService.listByUserKey(u.getId());
        Integer groupId = body.getInteger("groupid");
        if (null != groupId) {
            RequestUitl.modifyGroupKey(request, groupId);
        }else{
            body.put("groupid", RequestUitl.getGroupKey(request, 0));
        }
        WaybillSerach serach = new WaybillSerach();
        serach.setGroupId(body.getInteger("groupid"));
        serach.setUserId(u.getId());
        serach.setLikeString(body.get("likeString"));
        serach.setWaybillFettles(StringUtils.integerArray(body.get("waybillFettles")));
        serach.setReceiptFettles(StringUtils.integerArray(body.get("receiptFettles")));
        serach.setStartStation(body.get("startStation"));
        serach.setEndStation(body.get("endStation"));
        Integer timeType = body.getInteger("TimeSearch");
        if (timeType == null) {
            timeType = 0;
        }
        if (timeType > 0) {
            serach.setTime(new Date(), 0 - timeType);
        }
        body.put("TimeSearch", timeType);
        Boolean delay = body.getBoolean("delay");
        if (delay != null) {
            serach.setDelay(delay ? 1 : 2);
        } else {
            serach.setDelay(0);
        }
        logger.info("我的任务查询 -> {}", serach);
        Integer pageSize = body.getInteger("size");
        if (pageSize == null || pageSize <= 0) {
            pageSize = 15;
        }
        Integer pageNum = body.getInteger("num");
        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
        context.setAssociate(WaybillAssociate.associate(true, false, false, false, false, true));
        model.addAttribute("groups", projectGroups);
        //判断是否是货主类型的用户
        if(u.getUserType() == 1) {
        	context.getSearch().setAll(true);
        	model.addAttribute("page", waybillService.pageMergeWaybill(context));
        }else{
        	context.getSearch().setAll(false);
        	model.addAttribute("page", waybillService.pageMergeWaybill(context));
        }
        
        
        String filePath = SystemUtils.fileRootPath(Directory.TEMPLATE.sub("waybill_template.xlsx"));
        model.addAttribute("template", FileUtils.buildDownload(filePath, "任务单模板.xlsx", false));//要修改
        model.addAttribute("search", body);
        model.addAttribute("userType", u.getUserType());
        return "/backstage/waybill/manage";
    }
    
    /**
     * 	导出任务单数据
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/export/waybill")
    @ResponseBody
    public JsonResult outBoundExport(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        JSONObject requestParam = new JSONObject();
        requestParam.putAll(object);
        requestParam.put("userId", loadUserKey(request));
        //Collection<Long> waybillIds = StringUtils.longCollection(object.get("waybillIds"));
        //object.put("waybillIds", waybillIds);
        
        logger.info("waybillIds==========>:{}",requestParam);
        FileEntity fileEntity = waybillService.listExportWaybill(requestParam);
        JsonResult jsonResult = new JsonResult();
        if (null != fileEntity && StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            String fileName = FileUtils.appendSuffix("任务单", fileEntity.getSuffix());
            jsonResult.put("fileName", fileName);
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), fileName));
            logger.info("url==========>:{}",FileUtils.buildDownload(fileEntity.getPath(), fileName));
        } else {
            jsonResult.modify(false, "没有找到相应数据文件");
        }
        return jsonResult;
    }
    

    @RequestMapping(value = "/follow/into")
    public String followInto(Model model, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        WayBillCondition condition = new WayBillCondition();
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
        if (CollectionUtils.isNotEmpty(groupList)) {
            model.addAttribute("groups", groupList);
            if (null != condition.getGroupid()) {
                RequestUitl.modifyGroupKey(request, condition.getGroupid());
            }else{
                condition.setGroupid(RequestUitl.getGroupKey(request, 0));
            }
            if (condition != null) {
                LocalDate localDate = LocalDate.now();
                if (condition.getBindStartTime() == null) {
                    condition.setBindStartTime(localDate.minusDays(3).format(formatter));
                }
                if (condition.getBindEndTime() == null) {
                    condition.setBindEndTime(localDate.format(formatter));
                }
            }
        }
        model.addAttribute("condition", condition);
        return "/backstage/waybill/follow_ajax";
    }

    @RequestMapping(value = "/followAjax")
    @ResponseBody
    public JsonResult followAjax(@RequestBody(required = false) RequestObject object, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        //处理默认值
        WaybillSerach serach = new WaybillSerach();
        serach.setUserId(u.getId());
        serach.setGroupId(object.getInteger("groupid"));
        serach.setLikeString(object.get("likeString"));
        Boolean delay = object.getBoolean("delay");
        if (delay != null) {
            serach.setDelay(delay ? 1 : 2);
        }
        serach.setTime(object.getDate("bindStartTime"), object.getDate("bindEndTime"), -3);
        serach.setReceiptFettles(StringUtils.integerArray(object.get("receiptFettles")));
        serach.setWaybillFettles(StringUtils.integerArray(object.get("waybillFettles")));
        RequestUitl.modifyGroupKey(request, serach.getGroupId());
        WaybillContext context = WaybillContext.buildContext(u, serach);
        WaybillAssociate associate = WaybillAssociate.associateConveyance();
        associate.setAssociateTrack(true);
        context.setAssociate(associate);
        jsonResult.put("waybills", waybillService.listMergeWaybill(context));
        return jsonResult;
    }

    /**
     * TODO 任务单编辑
     * <p>
     *
     * @param waybill
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-11-27 16:38:23
     */
    @RequestMapping(value = "/editWaybill")
    @ResponseBody
    public JsonResult editWaybill(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("editWaybill -> {}", object);
        Assert.notNull(object, "编辑信息不能为空");
        User u = RequestUitl.getUserInfo(request);
        object.put("userid", u.getId());
        WaybillContext context = WaybillContext.buildContext(u);
        context.setUpdate(object.toJavaBean(Waybill.class));
        context.setReceiveType(Constant.CUSTOMER_CLIENT_NONE);
        context.setSendType(Constant.CUSTOMER_CLIENT_NONE);
        waybillService.update(context);
        return JsonResult.SUCCESS;
    }

    // 发送短信通知
    @RequestMapping(value = "/sendsms")
    @ResponseBody
    public JsonResult sendsms(@RequestBody Integer[] waybills, HttpServletRequest request) throws Exception {
        logger.info("-----------sendsms------------{}", Arrays.toString(waybills));
        for (Integer waybillId : waybills) {
            Waybill waybill = waybillService.getWaybillById(waybillId);
            if (waybill != null) {
                if (waybill.getWaybillStatus() < Constant.WAYBILL_STATUS_BIND) {
                    throw new BusinessException("选择了还未绑定的任务单");
                }
                if (StringUtils.isBlank(waybill.getContactPhone())) {
                    throw new BusinessException("有联系人手机号未空的选项");
                }
                String barcode = waybill.getBarcode();
                String address = waybill.getAddress();
                String contactNumber = waybill.getContactPhone();
                String memContent = "尊敬的客户您好！您的货物单号" + barcode + "已到达" + address + ",如有问题请联系13370216559,祝你工作顺利,生活愉快！";
                if (contactNumber != null) {
                    smsService.sendmsg(contactNumber, memContent);
                }
            }
        }
        return JsonResult.SUCCESS;
    }

    // 发送短信通知
    @RequestMapping(value = "/sendsms/{id}")
    @ResponseBody
    public JsonResult sendsms(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        logger.info("-----------sendsms------------{}", id);
        /*User u = RequestUitl.getUserInfo(request);
        for (Integer waybillId : waybills) {
			Waybill waybill = new Waybill();
			waybill.setId(waybillId);
			waybill.setUserid(u.getId());
			String json = JSONObject.toJSONString(waybill);
			logger.info("---传输数据：---" + json);
			JSONObject entity = InterfaceHepler.requestObject(json, InterfaceHepler.URL_WAYBILL_BY_ID);
			if(entity != null) {
				String barcode = entity.getJSONObject("barcode").getString("barcode");
				String address =  entity.getString("address");
				String contactNumber = entity.getJSONObject("customer").getString("contactNumber");
				String memContent = "尊敬的客户您好！您的货物单号" + barcode + "已到达" + address + ",如有问题请联系13370216559,祝你工作顺利,生活愉快！";
				if(contactNumber != null) {
					SmsUtil.send(contactNumber, memContent);
				}
			}
		}
		*/
        return JsonResult.SUCCESS;
    }

    // 任务单图片批量下载
    @RequestMapping(value = "/dowload/receipts")
    @ResponseBody
    public JsonResult dowloadReceipts(@RequestBody Integer[] waybills, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("-----------dowloadReceipts------------{}", Arrays.toString(waybills));
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        JSONObject object = new JSONObject();
        object.put("userid", u.getId());
        object.put("waybills", waybills);
        String json = object.toJSONString();
        logger.info("---传输数据：---" + json);
        JSONArray entitys = InterfaceHepler.requestArray(json, InterfaceHepler.URL_WAYBILL_SEARCH_BYIDS);
        String base_upload_path = SystemUtils.directoryUpload();
        logger.info("回单存储根目录：{}", base_upload_path);
        String user_temp_path = SystemUtils.directoryTemp(String.valueOf(u.getId()));
        logger.info("当前用户下载回单临时目录：{}", user_temp_path);
        int count = 0;
        for (int i = 0; i < entitys.size(); i++) {
            JSONObject waybill = entitys.getJSONObject(i);
            JSONArray receipts = waybill.getJSONArray("receipts");
            if (CollectionUtils.isEmpty(receipts)) {
                continue;
            }
            JSONObject barcode = waybill.getJSONObject("barcode");
            String subDirectory = waybill.getString("deliveryNumber");
            if (StringUtils.isEmpty(subDirectory)) {
                subDirectory = barcode.getString("barcode");//条码号
            } else {
                subDirectory += "_" + barcode.getString("barcode");//条码号
            }
            File waybillFile = FileUtils.directory(FileUtils.path(user_temp_path, subDirectory));
            for (int j = 0; j < receipts.size(); j++) {
                JSONObject receipt = receipts.getJSONObject(j);
                JSONArray addressCollection = receipt.getJSONArray("list");
                if (CollectionUtils.isEmpty(addressCollection)) {
                    continue;
                }
                for (int k = 0; k < addressCollection.size(); k++) {
                    JSONObject address = addressCollection.getJSONObject(k);
                    String file_path = FileUtils.path(base_upload_path, address.getString("path"));
                    logger.info("回单图片：{}", file_path);
                    File srcFile = FileUtils.file(file_path, false);
                    try {
                        if (!srcFile.exists()) {
                            logger.warn("回单图片 {} 不存在!", srcFile.getPath());
                            continue;
                        }
                        Date createtime = address.getDate("createtime");
                        File destFile = null;
                        if (createtime != null) {
                            String fineName = DateFormatUtils.format(createtime, "yyyyMMddHHmmss");
                            destFile = FileUtils.heavyName(waybillFile, FileUtils.appendSuffix(fineName, FileUtils.suffix(srcFile.getName())));
                            FileUtils.copyFile(srcFile, destFile);
                        } else {
                            destFile = FileUtils.copyFileToDirectory(srcFile, waybillFile, false);
                        }
                        count++;
                        logger.debug("copy from {} to {}", srcFile, destFile);
                    } catch (Exception e) {
                        logger.error("回单图片 {} 处理异常", srcFile.getPath(), e);
                    }
                }
            }
        }
        if (count > 0) {
            File zipFile = FileUtils.zip(user_temp_path, SystemUtils.directoryDownload());
            String url = FileUtils.path(downpath(), zipFile.getName());
            jsonResult.put("url", url);
            jsonResult.put("count", count);
            jsonResult.put("length", FileUtils.size(zipFile.length(), FileUtils.ONE_MB));
            FileUtils.deleteDirectory(new File(user_temp_path));
        } else {
            jsonResult = new JsonResult(false, "没有找到回单文件");
        }
        return jsonResult;
    }

    /**
     * 确认到货
     *
     * @param 任务单id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "confirm/receive")
    @ResponseBody
    public JsonResult confirm(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("confirm receive -> {}", object);
        User user = RequestUitl.getUserInfo(request);
        Collection<Integer> waybillKeys = StringUtils.integerCollection(object.get("waybillIds"));
        Assert.notEmpty(waybillKeys, "至少选择一条任务单数据");
        waybillService.confirmReceive(user.getId(), waybillKeys, Constant.CONFIRM_DELIVERY_WAY_BACK);
        return JsonResult.SUCCESS;
    }

    /**
     * 已送达
     *
     * @param 任务单id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "confirm/arrive")
    @ResponseBody
    public JsonResult arrive(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("confirm arrive -> {}", object);
        User user = RequestUitl.getUserInfo(request);
        Collection<Integer> waybillKeys = StringUtils.integerCollection(object.get("waybillIds"));
        Assert.notEmpty(waybillKeys, "至少选择一条任务单数据");
        waybillService.confirmArrive(user.getId(), waybillKeys, Constant.CONFIRM_DELIVERY_WAY_BACK);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public JsonResult delete(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("delete -> {}", object.get("waybillIds"));
        Collection<Integer> deleteWaybillKeys = StringUtils.integerCollection(object.get("waybillIds"));
        Assert.notEmpty(deleteWaybillKeys, "至少选择一项要删除的数据");
        User user = RequestUitl.getUserInfo(request);
        waybillService.delete(user.getId(), deleteWaybillKeys);
        return JsonResult.SUCCESS;
    }

    /**
     * 根据运单编号查看运单详情
     * <p>
     *
     * @param waybillid 运单编号
     * @param model
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:59:48
     */
    @RequestMapping(value = "findById/{waybillid}")
    public String findById(@PathVariable Integer waybillid, Model model, HttpServletRequest request) throws Exception {
        logger.info("-----------findById------------{}", waybillid);
        User user = RequestUitl.getUserInfo(request);
        MergeWaybill waybill = waybillService.getWaybillById(user.getId(), waybillid, WaybillAssociate.associateDetail());
        if (waybill != null) {
            model.addAttribute("waybill", waybill);
            model.addAttribute("imagePath", imagepath());// 图片服务器的路径
        }
        return "/backstage/waybill/waybill_detail";
    }

    @RequestMapping(value = "detail/{waybillid}")
    @ResponseBody
    public JsonResult detailById(@PathVariable Integer waybillid, HttpServletRequest request) throws Exception {
        logger.info("-----------detailById------------{}", waybillid);
        User user = RequestUitl.getUserInfo(request);
        JsonResult result = new JsonResult();
        result.put("waybill", waybillService.getWaybillById(user.getId(), waybillid, WaybillAssociate.associateDetail()));
        return result;
    }

    @RequestMapping(value = "showmap/{waybillid}")
    public String showmap(@PathVariable Integer waybillid, Model model, HttpServletRequest request) throws Exception {
        logger.info("-----------showmap------------{}", waybillid);
        User user = RequestUitl.getUserInfo(request);
        MergeWaybill waybill = waybillService.getWaybillById(user.getId(), waybillid, WaybillAssociate.associateTrack());
        if (waybill != null) {
            model.addAttribute("waybill", waybill);
        }
        return "/backstage/waybill/showmap";
    }

    /**
     * 任务单轨迹上报位置
     *
     * @param track
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadPosition", method = RequestMethod.POST)
    public @ResponseBody
    JsonResult uploadPosition(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.debug("轨迹上报 -> {}", object);
        Assert.notNull(object, Constant.PARAMS_ERROR);

        User user = RequestUitl.getUserInfo(request);
        trackService.saveLoactionReport(user.getId(), object.toJavaBean(WaybillTrack.class));

        return JsonResult.SUCCESS;
    }

    /**
     * TODO 跳转新增界面
     * <p>
     *
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-11 16:07:35
     */
    @RequestMapping(value = "/addview")
    public String addView(HttpServletRequest request, Model model) throws Exception {
        logger.debug("addview===========>");
        User user = RequestUitl.getUserInfo(request);
        //查询组信息
        List<ProjectGroup> list = projectGroupService.listByUserKey(user.getId());
        model.addAttribute("list", list);
        return "/backstage/waybill/addtask";
    }

    /**
     * TODO 跳转编辑界面
     * <p>
     *
     * @param waybillid
     * @param model
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-11 16:17:01
     */
    @RequestMapping(value = "/eidtview/{waybillid}")
    public String eidtview(@PathVariable Integer waybillid, Model model, HttpServletRequest request) throws Exception {
        logger.debug("addview===========>{}", waybillid);
        //查询组信息
        User user = RequestUitl.getUserInfo(request);
        model.addAttribute("groups", projectGroupService.listByUserKey(user.getId()));
        model.addAttribute("waybill", waybillService.getWaybillById(user.getId(), waybillid, WaybillAssociate.associateCommodity()));
        return "/backstage/waybill/send_goods_edit";
    }

    /**
     * 根据groupID查询收货客户信息
     *
     * @throws Exception
     * @Author：wangke
     * @description：
     * @Date：10:54 2017/12/15
     */
    @RequestMapping(value = "/customerByGroupId")
    @ResponseBody
    public JsonResult queryCustomerListByGroupId(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, "查询信息不能为空");
        JsonResult jsonResult = new JsonResult();
        AddressSearch search = object.toJavaBean(AddressSearch.class);
        jsonResult.put("customers", customerService.queryCustomer(search));
        return jsonResult;
    }


    /**
     * 修改运单
     *
     * @Author：wangke
     * @description：
     * @Date：19:43 2017/12/16
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public JsonResult updateTaskList(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, Constant.PARAMS_ERROR);
        object.stringIsNull("commodities", Constant.GOODS_PARAMS_ERROR);
        object.stringIsNull("sender", "发货信息不能为空");
        object.stringIsNull("receiver", "收货信息不能为空");
        User user = RequestUitl.getUserInfo(request);
        object.put("userid", user.getId());

        WaybillContext context = WaybillContext.buildContext(user);
        context.setUpdate(object.toJavaBean(Waybill.class));
        //(1:收货客户,2:发货客户)
        context.setSendType(Constant.CUSTOMER_CLIENT_NONE);
        Customer sender = JSON.parseObject(object.get("sender"), Customer.class);
        context.setReceiveType(Constant.CUSTOMER_CLIENT_NONE);
        Customer receiver = JSON.parseObject(object.get("receiver"), Customer.class);
        context.setCustomers(new Customer[]{sender, receiver});
        context.setArriveDay(object.getInteger("arriveDay"));
        context.setArriveHour(object.getInteger("arriveHour"));
        context.setCommodities(JSON.parseArray(object.get("commodities"), Goods.class));
        context.setDeleteCommodityKeys(StringUtils.integerCollection(object.get("deleteCommodityKeys")));
        waybillService.update(context);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult waybillImport(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer gKey = body.getInteger("groupid"), sendKey = body.getInteger("senderKey");
        //Assert.notNull(gKey, "请选择项目组!!!");
        if(gKey != null && 17 == gKey){//金发项目组
            Assert.notNull(sendKey, "请选择发货方信息");
        }
        User user = RequestUitl.getUserInfo(request);
        FileEntity fileEntity = null;
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        if(file != null){
            fileEntity = new FileEntity();
            fileEntity.setSuffix(FileUtils.suffix(file.getName()));
            fileEntity.setDirectory(SystemUtils.directoryTemp());
            fileEntity.setFileName(file.getName());
            fileEntity.setSize(file.length());
        }
        if (fileEntity == null) {
            throw new BusinessException("请上传一个文件!!!");
        }
        String path = fileEntity.getPath();
        try {
            fileEntity = fileService.saveByFile(user.getId(), gKey, sendKey, fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.modify(false, "存在异常数据</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条</br>请下载处理后上传导入");
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            jsonResult.put("fileName", fileEntity.getFileName());
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix("导入异常数据", fileEntity.getSuffix())));
        } else {
            jsonResult.message("操作成功</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条");
            if (gKey != null && gKey > 0) {
                jsonResult.put("url", "/backstage/trace/search?groupid=" + gKey + "&waybillFettles=" + Constant.WAYBILL_STATUS_WAIT);
            } else {
                jsonResult.put("url", "/backstage/trace/search?groupid=0&waybillFettles=" + Constant.WAYBILL_STATUS_WAIT);
            }
        }
        return jsonResult;
    }
    
    @RequestMapping(value = "/batchBind", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult batchBind(@RequestBody RequestObject object,HttpServletRequest request) throws Exception {
    	JsonResult jsonResult = new JsonResult("200", true, "绑定成功");
    	JSONObject params = new JSONObject();
    	params.putAll(object);
    	Integer gKey = object.getInteger("groupId");
    	Assert.notBlank(gKey, "项目组编号不能为空");
    	waybillService.batchBind(loadUserKey(request),waybillService.listunBindWaybill(params), gKey);
    	return jsonResult;
    }

    /**
     * 开关电子围栏
     *
     * @Author：wangke
     * @description：
     * @Date：13:58 2017/12/19
     */
    @RequestMapping(value = "/updateFenceStatus")
    @ResponseBody
    public JsonResult updateFenceStatus(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Integer waybillId = object.getInteger("id");
        Assert.notNull(waybillId, "运单编号不能为空");
        Integer status = object.getInteger("fenceStatus");
        status = waybillService.updateFenceStatus(waybillId, status);
        jsonResult.put("status", status);
        return jsonResult;
    }


    /**
     * 查询未绑定的任务单
     * <p>
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-18 09:35:50
     */
    @RequestMapping(value = "/list/unbound/waybill")
    @ResponseBody
    public JsonResult listUnBoundWaybil(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("listUnBoundWaybil -> {} ", body);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }

        WaybillSerach serach = new WaybillSerach();
        String code = body.get("barcode");
        if (StringUtils.isNotBlank(code)) {
            Barcode barcode = barcodeService.getBarcode(code);
            if (barcode != null) {
                serach.setGroupId(barcode.getId());
            }
        }
        serach.setUserId(u.getId());
        if (body.containsKey("likeString")) {
            serach.setLikeString(body.get("likeString"));
        }
        Integer pageNum = body.getInteger("num");
        serach.setWaybillFettles(new Integer[]{Constant.WAYBILL_STATUS_WAIT});//待绑定的
        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, 5));
        context.setAssociate(WaybillAssociate.associateProjectGroup());
        jsonResult.put("page", waybillService.pageMergeWaybill(context));
        return jsonResult;
    }

    /**
     * 上报位置
     */
    @RequestMapping(value = "/report/loaction", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult reportLoaction(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.debug("reportLoaction -> {}", object);
        JsonResult jsonResult = new JsonResult();
        Assert.notNull(object, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        WaybillTrack waybillTrack = new WaybillTrack();
        String barcode = object.get("barcode");
        Assert.notBlank(barcode, "任务单号不能为空");
        waybillTrack.setLocations(object.get("area") + object.get("street"));
        Assert.notBlank(waybillTrack.getLocations(), "上报地址不能为空");
        waybillTrack.setUserid(object.getInteger("driverKey"));
        waybillTrack.setLatitude(object.getDouble("lat"));
        waybillTrack.setLongitude(object.getDouble("lng"));
        trackService.saveLoactionReport(user.getId(), barcode, waybillTrack);
        return jsonResult;
    }

    /**
     * 打印页
     */
    @RequestMapping(value = "/print/page")
    public String toPrintList(Model model, HttpServletRequest request) throws Exception {
        RequestObject object = new RequestObject(request.getParameterMap());
        logger.debug("print page -> {}", object);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        Integer gKey = object.getInteger("groupId");
        Assert.notBlank(gKey, "项目组编号不能为空");
        String waybillKeyString = object.get("waybillKeys");
        Assert.notBlank(waybillKeyString, "请选择要打印的任务单");
        model.addAttribute("waybillKeys", waybillKeyString);
        model.addAttribute("groupId", gKey);
        model.addAttribute("ctime", com.ycg.ksh.common.util.DateFormatUtils.format(new Date()));
        return "/backstage/waybill/waybill_print";
    }

    /**
     * 打印数据
     */
    @RequestMapping(value = "/print/waybills", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult printWaybills(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.debug("print waybills -> {}", object);
        JsonResult jsonResult = new JsonResult();
        Assert.notNull(object, Constant.PARAMS_ERROR);
        String waybillKeyString = object.get("waybillKeys");
        Integer gKey = object.getInteger("groupId");
        Assert.notBlank(waybillKeyString, "请选择要打印的任务单");
        User user = RequestUitl.getUserInfo(request);
        Collection<Integer> collection = StringUtils.integerCollection(waybillKeyString);
        jsonResult.put("waybills", waybillService.listPrint(user.getId(), collection, 5, gKey));
        return jsonResult;
    }
    /**
     * 	批量下载已绑定任务单
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downBind")
    @ResponseBody
    public JsonResult downBind(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("downBind params -> {}", object);
        User u = RequestUitl.getUserInfo(request);
        object.put("userId", u.getId());
    	FileEntity fileEntity = waybillService.buildPDF(object);
    	JsonResult jsonResult = new JsonResult();
        if (null != fileEntity && StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            jsonResult.put("fileName", fileEntity.getFileName());
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix("已绑定二维码PDF文件", fileEntity.getSuffix()), true));
        } else {
            jsonResult.modify(false, "文件下载异常");
        }
        return jsonResult;
    }
    /**
     * 修改要求到货时间
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value ="/changeArrivaltime")
    @ResponseBody
    public JsonResult changeArrivaltime(@RequestBody RequestObject object,HttpServletRequest request) throws Exception{
        JsonResult jsonResult = new JsonResult(true,"修改成功");
        waybillService.batchUpdateArrivaltime(object);
        return jsonResult;
    }
}
