package com.ycg.ksh.api.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.backstage.model.ReceiptModel;
import com.ycg.ksh.api.common.socket.CustomWebSocketHandler;
import com.ycg.ksh.api.common.socket.SocketConstant;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.adapter.wechat.JsApiTicket;
import com.ycg.ksh.entity.common.constant.PermissionCode;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.service.api.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 回单审核
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 * 2017-09-13 10:28:24
 */
@Controller("backstage.receipt.controller")
@RequestMapping("/backstage/receipt")
public class ReceiptController extends BaseController {

    @Resource
    protected CacheManager cacheManger;
    @Resource
    private WaybillService waybillService;
    @Resource
    private ProjectGroupService projectGroupService;
    @Resource
    private ReceiptService receiptService;
    @Resource
    CustomWebSocketHandler socketHandler;
    @Resource
    private WeChatApiService apiService;

    @Resource
    private BarCodeService barCodeService;

    /**
     * 回单审核status 1合格0不合格 Integer[] 图片id remark 不合格备注
     */
    @RequestMapping(value = "verify")
    @ResponseBody
    public JsonResult approval(@RequestBody(required = false) ReceiptModel reModel, HttpServletRequest request)
            throws Exception {
        Assert.notNull(reModel, "图片审核参数不能为空");
        User u = RequestUitl.getUserInfo(request);
        // 判断用户是否有回单审核的权限
        if (!permissionService.validateByWaybillID(reModel.getWayBillId(), u.getId(), PermissionCode.RECEIPT_VERIFY)) {
            JsonResult jsonResult = new JsonResult();
            jsonResult.modify(false, "没有回单审核的权限");
            return jsonResult;
        }
        ImageInfo imageInfo = new ImageInfo(u.getId(), reModel.getAddressId(), reModel.getStatus(), reModel.getRemark());
        receiptService.modifyVerify(reModel.getWayBillId(), imageInfo);
        return JsonResult.SUCCESS;
    }

    /**
     * 回单管理(审核分页列表)
     *
     * @param search
     * @param request
     * @return
     */
    @RequestMapping(value = "search")
    public String queryListPage(WaybillSerach search, Model model, HttpServletRequest request, PageScope pageScope)
            throws Exception {
        User user = RequestUitl.getUserInfo(request);
        search.setUserId(user.getId());
        search.setReceiptFettles(new Integer[]{Constant.RECEIPT_VERIFY_STATUS_WAIT, Constant.RECEIPT_VERIFY_STATUS_ING});//待审核,审核中
        search.setWaybillFettles(new Integer[]{Constant.WAYBILL_STATUS_BIND, Constant.WAYBILL_STATUS_ING, Constant.WAYBILL_STATUS_ARRIVE});
        search.setAll(true);
        CustomPage<MergeWaybill> page = waybillService.pageMergeWaybill(WaybillContext.buildContext(user, search, pageScope));
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        return "/backstage/billbook/control";
    }

    /**
     * （回单管理）回单详情
     *
     * @param waybillId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "queryInfo/{waybillId}")
    @ResponseBody
    public JsonResult queryReceiptInfo(@PathVariable Integer waybillId, HttpServletRequest request) throws Exception {
        Assert.notNull(waybillId, "参数不能为空");
        JsonResult jsonResult = new JsonResult();
        Waybill waybill = waybillService.getWaybillById(waybillId);
        List<MergeReceipt> list = receiptService.listByWaybillId(waybillId);
        jsonResult.put("path", SystemUtils.imagepath());
        jsonResult.put("waybill", waybill);
        jsonResult.put("results", list);
        return jsonResult;
    }

    @RequestMapping(value = "scan/enter")
    public String scanEnter(HttpServletRequest request, Model model) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        model.addAttribute("url", RequestUitl.getServerPath(request) + "/backstage/receipt/special/scan/open/" + user.getId());
        return "/backstage/receipt/scan_pc";
    }

    /**
     * 复选框选中是否送交客户
     *
     * @param requestObject
     * @author wangke
     * @date 2018/3/22 15:02
     */
    @RequestMapping(value = "checkScanFlag")
    @ResponseBody
    public void checkScanFlag(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        User user = RequestUitl.getUserInfo(request);
        if (requestObject.getBoolean("flag") == true) {
            cacheManger.set(user.getId() + "flag", 2);
        } else {
            cacheManger.set(user.getId() + "flag", 1);
        }
    }

    @RequestMapping(value = "special/scan/open/{userId}/{status}")
    public String scanOpen(@PathVariable Integer userId, @PathVariable Integer status, HttpServletRequest request,
                           Model model) throws Exception {
        AuthorizeUser user = userService.get(userId);
        if (user == null) {
            throw new BusinessException("用户信息异常");
        }
        RequestUitl.modifyUserInfo(request, user);
        String targetUrl = SystemUtils.buildUrl(RequestUitl.getServerPath(request), request.getServletPath());
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            targetUrl = targetUrl + "?" + queryString;
        }
        JsApiTicket apiTicket = apiService.swapJsApiTicket(targetUrl);
        if (apiTicket != null) {
            model.addAttribute("appId", apiTicket.getAppId());
            model.addAttribute("nonceStr", apiTicket.getNonceStr());
            model.addAttribute("timestamp", apiTicket.getTimestamp());
            model.addAttribute("signature", apiTicket.getSignature());
            model.addAttribute("userId", userId);
            model.addAttribute("status", status);
            // 批次号放入缓存，设置用户ID 为key
            cacheManger.set(user.getId(), receiptService.getBatchNumber());
            socketHandler.sendTextMessage(SocketConstant.CLIENT_TYPE_PC, userId, SocketConstant.MSG_TYPE_SCAN_OPEN, status);
        } else {
            throw new BusinessException("微信授权异常");
        }
        logger.info("--------------------------------------------------------------------------");
        return "/backstage/receipt/scan_mobile";
    }

    @RequestMapping(value = "scan/scan")
    @ResponseBody
    public JsonResult scan(@RequestBody RequestObject object, HttpServletRequest request, Model model)
            throws Exception {
        PaperyReceipt paperyReceipt = new PaperyReceipt();
        paperyReceipt.setBarcode(object.get("code"));
        paperyReceipt.setReceiptStatus(object.getInteger("status"));
        paperyReceipt.setUserId(object.getInteger("userId"));
        logger.debug("扫描回单 用户ID:{} 回单状态:{} 运单编号:{}", paperyReceipt.getUserId(), paperyReceipt.getReceiptStatus(), paperyReceipt.getBarcode());
        Barcode barcode = barCodeService.getBarcode(paperyReceipt.getBarcode());
        JsonResult jsonResult = new JsonResult();
        try {
            // 从缓存读取批次号
            object.put("batchNumber", cacheManger.get(paperyReceipt.getUserId()));
            object.put("code", barcode.getBarcode());
            paperyReceipt.setBatchNumber(cacheManger.get(paperyReceipt.getUserId()).toString());
            paperyReceipt.setBarcode(barcode.getBarcode());
            int flag = 1;
            if (paperyReceipt.getReceiptStatus() == 1) {
                flag = Integer.valueOf(cacheManger.get(paperyReceipt.getUserId() + "flag").toString());
            }
            MergeWayBillReceipt mergeReceipt = receiptService.singleScan(paperyReceipt, flag);
            mergeReceipt.setPath(SystemUtils.imagepath());
            mergeReceipt.setSendCustomerFlag(Integer.valueOf(cacheManger.get(paperyReceipt.getUserId() + "flag").toString()));
            socketHandler.sendTextMessage(SocketConstant.CLIENT_TYPE_PC, paperyReceipt.getUserId(), SocketConstant.MSG_TYPE_SCAN, mergeReceipt);
        } catch (BusinessException be) {
            logger.error("scan -> {}", object, be);
            jsonResult.modify(false, be.getFriendlyMessage());
            try {
                JSONObject errorObject = new JSONObject();
                errorObject.put("code", barcode.getBarcode());
                errorObject.put("exception", be.getFriendlyMessage());
                socketHandler.sendTextMessage(SocketConstant.CLIENT_TYPE_PC, paperyReceipt.getUserId(), SocketConstant.MSG_TYPE_EXCEPTION, errorObject);
            } catch (Exception e1) {
            } // 忽略异常
            logger.warn("条码扫描异常 {}, {}", object, be.getMessage());
        } catch (Exception e) {
            jsonResult.modify(false, "处理异常");
            logger.error("条码扫描异常 {}", object, e);
        }
        return jsonResult;
    }


    /**
     * 统计分析
     * <p>
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-07 10:07:44
     */
    //TODO 要修改的
    @RequestMapping(value = "analyze")
    public String analyze(MergeReceiptStatistics mergeReceiptStatistics, HttpServletRequest request, Model model) {
        try {
            RequestObject body = new RequestObject(request.getParameterMap());
            User u = RequestUitl.getUserInfo(request);
            ProjectGroup group = new ProjectGroup();
            group.setUserid(u.getId());
            if (null == mergeReceiptStatistics.getSelectYear()) {
                mergeReceiptStatistics.setSelectYear(DateUtils.getYear(new Date()));
            }
            if (null == mergeReceiptStatistics.getSelectMonth()) {
                mergeReceiptStatistics.setSelectMonth(DateUtils.getMonth(new Date(), -1));
            }
            if (null == mergeReceiptStatistics.getGroupid() || mergeReceiptStatistics.getGroupid() == -1) {
                mergeReceiptStatistics.setAll(true);
            } else {
                mergeReceiptStatistics.setAll(false);
            }
            mergeReceiptStatistics.setUserid(u.getId());

            Integer pageSize = body.getInteger("size");
            Integer pageNum = body.getInteger("num");
            CustomPage<MergeReceiptStatistics> page = receiptService.receiptAnalysis(mergeReceiptStatistics, new PageScope(pageSize, pageNum));
            model.addAttribute("page", page);
            model.addAttribute("groups", projectGroupService.listByUserKey(u.getId()));
            model.addAttribute("mergeReceiptStatistics", mergeReceiptStatistics);
            Integer year = DateUtils.getYear(new Date());
            model.addAttribute("year", new Integer[]{year, year - 1, year - 2});
        } catch (Exception exception) {
            logger.error(" --- 回单状态扫描统计 ---", exception);
        }
        model.addAttribute("group", new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});

        return "/backstage/receipt/analyze";
    }


    /**
     * 历史记录查询
     * <p>
     *
     * @param object  参数
     * @param request
     * @param model
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-07 10:07:32
     * @author chenxm
     */
    @RequestMapping(value = "history")
    public String history(@RequestParam(required = false) HashMap<String, Object> object, HttpServletRequest request,
                          Model model) throws Exception {
        // 获取当前用户
        User u = RequestUitl.getUserInfo(request);
        object.put("userId", u.getId());

        if (object.get("groupId") == null) {
            object.put("all", true);
        } else if (Integer.valueOf(object.get("groupId").toString()) == -1) {
            object.put("all", true);
        } else {
            object.put("all", false);
        }

        Integer pageSize = object.containsKey("pageSize") && StringUtils.isNotBlank(object.get("pageSize").toString())
                ? Integer.valueOf("" + object.get("pageSize")) : null;
        Integer pageNum = object.containsKey("pageNum") && StringUtils.isNotBlank(object.get("pageNum").toString())
                ? Integer.valueOf("" + object.get("pageNum")) : null;

        CustomPage<WaybillReceiptView> page = receiptService.historyRecordList(object,
                new PageScope(pageNum, pageSize));
        // 查询用户关联的资源组
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
        model.addAttribute("groups", groupList);
        model.addAttribute("data", page);
        model.addAttribute("param", object);
        return "/backstage/receipt/history";
    }


    /**
     * 回单列表
     *
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/3 17:09
     */
    @RequestMapping(value = "queryReceiptList")
    public String queryReceiptList(Model model, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        RequestObject body = new RequestObject(request.getParameterMap());
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
        if (groupList != null) {
            model.addAttribute("groups", groupList);
        }
        Integer pageSize = body.containsKey("pageSize") && StringUtils.isNotBlank(body.get("pageSize").toString())
                ? Integer.valueOf("" + body.get("pageSize")) : null;
        Integer pageNum = body.containsKey("pageNum") && StringUtils.isNotBlank(body.get("pageNum").toString())
                ? Integer.valueOf("" + body.get("pageNum")) : null;
        WaybillSerach search = new WaybillSerach();
        search.setFirstTime(body.getDate("firstTime"));
        search.setSecondTime(body.getDate("secondTime"));
        Calendar calendar = Calendar.getInstance();
        if(search.getSecondTime() != null){
            search.setSecondTime(DateUtils.maxOfDay(search.getSecondTime()));
        }
        search.setWaitFettle(body.getInteger("waitFettle"));
        search.setPaperyReceiptStatus(body.getInteger("paperyReceiptStatus"));
        search.setLikeString(body.get("likeString"));
        search.setUserId(u.getId());
        if (!body.containsKey("groupId") || body.getInteger("groupId") == -1) {
            search.setAll(true);
        } else {
            search.setAll(false);
            search.setGroupId(body.getInteger("groupId"));
        }
        CustomPage<MergeReceiptResult> page = receiptService.queryReceiptList(search, new PageScope(pageNum, pageSize));
        model.addAttribute("page", page);
        model.addAttribute("search", body);
        return "/backstage/receipt/manage";
    }

    /**
     * 查询纸质回单和电子回单操作记录
     *
     * @param waybillid
     * @param request
     * @return
     * @author wangke
     * @date 2018/4/9 09:32
     */
    @RequestMapping(value = "queryOperationRecord/{waybillid}")
    public String queryOperationRecord(@PathVariable String waybillid, Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer pageSize = body.containsKey("pageSize") && StringUtils.isNotBlank(body.get("pageSize").toString())
                ? Integer.valueOf("" + body.get("pageSize")) : null;
        Integer pageNum = body.containsKey("pageNum") && StringUtils.isNotBlank(body.get("pageNum").toString())
                ? Integer.valueOf("" + body.get("pageNum")) : null;
        MergeOperationRecord record = new MergeOperationRecord();
        record.setFirstTime(body.getDate("firstTime"));
        record.setSecondTime(body.getDate("secondTime"));
        record.setReceiptStatus(body.getInteger("receiptStatus"));
        record.setElectronicStatus(body.getInteger("electronicStatus"));
        record.setSearchType(body.getInteger("searchType"));
        if ("search".equals(waybillid)) {
            record.setWaybillid(body.getInteger("waybillid"));
        } else {
            record.setWaybillid(Integer.valueOf(waybillid));
        }
        if (null == record.getSearchType()) {
            record.setSearchType(1);
        }
        CustomPage<MergeOperationRecord> page = receiptService.queryOperationRecord(record, new PageScope(pageNum, pageSize));
        model.addAttribute("seach", body);
        model.addAttribute("page", page);
        model.addAttribute("waybillid", record.getWaybillid());
        return "/backstage/receipt/oprationrecord";
    }

    /**
     * 库存回单查询
     * <p>
     *
     * @param object  参数
     * @param request
     * @param model
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-07 10:07:20
     */
    @RequestMapping(value = "inventory")
    public String inventory(@RequestParam(required = false) HashMap<String, Object> object, HttpServletRequest request,
                            Model model) throws Exception {
        Integer userId = RequestUitl.getUserInfo(request).getId();
        if (object == null || object.isEmpty()) {
            object.put("receiptStatus", "");
        }
        object.put("type", 1);
        object.put("userId", userId);
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(userId);
        if (groupList != null) {
            model.addAttribute("groups", groupList);
        }
        Integer pageSize = object.containsKey("pageSize") && StringUtils.isNotBlank(object.get("pageSize").toString())
                ? Integer.valueOf("" + object.get("pageSize")) : null;

        Integer pageNum = object.containsKey("pageNum") && StringUtils.isNotBlank(object.get("pageNum").toString())
                ? Integer.valueOf("" + object.get("pageNum")) : null;

        CustomPage<WaybillReceiptView> page = receiptService.queryReceiptPage(object, new PageScope(pageNum, pageSize));
        model.addAttribute("search", object);
        model.addAttribute("results", page);
        return "/backstage/receipt/inventory";
    }


    /**
     * 批量回单入库
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/23 14:01
     */
    @RequestMapping(value = "receiptStorage")
    @ResponseBody
    public JsonResult receiptStorage(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        MergeReceiptStatus mergeReceipt = object.toJavaBean(MergeReceiptStatus.class);
        mergeReceipt.setUserId(user.getId());
        mergeReceipt.setBatchNumber(receiptService.getBatchNumber());
        Integer flag[] = receiptService.receiptStorageList(mergeReceipt);
        JsonResult jsonResult = new JsonResult();
        //根据 数组 list.size数和执行的个数给出提示
        if (flag[1] == 0) {
            jsonResult.put("flag", 1);
        } else if (flag[0] - flag[1] != flag[0] && flag[0] - flag[1] != 0) {
            jsonResult.put("flag", 2);
        }
        return jsonResult;
    }

    /**
     * 下载回单图片并导出部分回单信息
     *
     * @param requestObject
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/24 15:43
     */
    @RequestMapping(value = "downloadReceipt")
    @ResponseBody
    public JsonResult downloadReceipt(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        WaybillSerach search = new WaybillSerach();
        search.setUserId(u.getId());
        search.setGroupId(requestObject.getInteger("groupId"));
        // 发货时间段
        search.setDeliveryTimeStart(requestObject.getDate("firstTime"));
        search.setDeliveryTimeEnd(requestObject.getDate("secondTime"));
        if(search.getDeliveryTimeEnd() != null){
            search.setDeliveryTimeEnd(DateUtils.maxOfDay(search.getDeliveryTimeEnd()));
        }
        ExcelWriter easyExcel = null;
        String base_upload_path = SystemUtils.directoryUpload();
        String user_temp_path = SystemUtils.directoryTemp(String.valueOf(System.nanoTime()));
        int count = 0, flag = 1;
        easyExcel = EasyExcelBuilder.createWriteExcel(FileUtils.file(user_temp_path, FileUtils.appendSuffix("审核不合格以及未审核的任务单信息", FileUtils.XLSX_SUFFIX)));
        easyExcel.createSheet("回单信息");
        easyExcel.columnWidth(30, 25, 30, 30, 10, 10, 10);
        easyExcel.header("送货单号", "任务单号", "发货日期", "回单总数", "已审合格", "已审不合格", "未审");
        try{
            Collection<Waybill> waybills = waybillService.listWaybill(search);
            if (null != waybills) {
                for (Waybill waybill : waybills) {
                    List<MergeReceipt> mergeReceipts = receiptService.listByWaybillId(waybill.getId());
                    if (null != mergeReceipts) {
                        File waybillFile = FileUtils.directory(FileUtils.path(user_temp_path, "回单图片"));
                        int imageCount = 1;
                        for (MergeReceipt mergeReceipt : mergeReceipts) {
                            if (null != mergeReceipt.getImages()) {
                                if (waybill.getWaybillStatus() == 40) {
                                    for (ImageInfo imageInfo : mergeReceipt.getImages()) {
                                        if (imageInfo.getVerifyStatus() != null && imageInfo.getVerifyStatus() == 1) {
                                            String file_path = FileUtils.path(base_upload_path, imageInfo.getPath());
                                            File srcFile = FileUtils.file(file_path, false);
                                            if (!srcFile.exists()) {
                                                logger.warn("回单图片 {} 不存在!", srcFile.getPath());
                                                continue;
                                            }
                                            String fineName = imageCount + "_" + waybill.getDeliveryNumber();
                                            File imgFile = FileUtils.heavyName(waybillFile, FileUtils.appendSuffix(fineName, FileUtils.suffix(srcFile.getName())));
                                            FileUtils.copyFile(srcFile, imgFile);
                                            count++;
                                            imageCount++;
                                        } else {
                                            easyExcel.row(waybill.getDeliveryNumber(), waybill.getBarcode(), waybill.getCreatetime(), waybill.getReceiptCount()
                                                    , waybill.getReceiptVerifyCount() - waybill.getReceiptUnqualifyCount(), waybill.getReceiptUnqualifyCount(), waybill.getReceiptCount() - waybill.getReceiptVerifyCount());
                                            flag++;
                                            count++;
                                        }
                                    }
                                } else {
                                    easyExcel.row(waybill.getDeliveryNumber(), waybill.getBarcode(), waybill.getCreatetime(), waybill.getReceiptCount()
                                            , waybill.getReceiptVerifyCount() - waybill.getReceiptUnqualifyCount(), waybill.getReceiptUnqualifyCount(), waybill.getReceiptCount() - waybill.getReceiptVerifyCount());
                                    flag++;
                                    count++;
                                }
                            }
                        }
                    }
                }
                if (flag > 1) {
                    easyExcel.write();
                }
                if (count > 0) {
                    File zipFile = FileUtils.zip(user_temp_path, SystemUtils.directoryDownload());
                    jsonResult.put("url", FileUtils.buildDownload(zipFile.getPath(), "回单文件.zip", false));
                    jsonResult.put("count", count);
                    jsonResult.put("size", FileUtils.size(zipFile.length(), FileUtils.ONE_MB));
                    logger.info("回单下载 -> {}", zipFile);
                } else {
                    jsonResult.modify(false, "无可下载的回单信息");
                }
            } else {
                jsonResult.modify(false, "无可下载的回单信息");
            }
        }catch (BusinessException | ParameterException be){
            throw be;
        }catch (Exception e){
            logger.error("回单下载异常 {}", requestObject , e);
        }finally {
            if(easyExcel != null){
                easyExcel.close();
            }
            FileUtils.deleteDirectory(user_temp_path);
        }
        return jsonResult;
    }


    /**
     * 送交客户回单查询
     * <p>
     *
     * @param object  参数
     * @param request
     * @param model
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
     * 2017-11-07 10:07:08
     */
    @RequestMapping(value = "sendcustomer")
    public String sendcustomer(@RequestParam(required = false) HashMap<String, Object> object,
                               HttpServletRequest request, Model model) throws Exception {
        Integer userId = RequestUitl.getUserInfo(request).getId();
        object.put("userId", userId);
        object.put("type", 2);
        object.put("receiptStatus", 2);
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(userId);
        if (groupList != null) {
            model.addAttribute("groups", groupList);
        }
        Integer pageSize = object.containsKey("pageSize") && StringUtils.isNotBlank(object.get("pageSize").toString())
                ? Integer.valueOf("" + object.get("pageSize")) : null;
        Integer pageNum = object.containsKey("pageNum") && StringUtils.isNotBlank(object.get("pageNum").toString())
                ? Integer.valueOf("" + object.get("pageNum")) : null;
        CustomPage<WaybillReceiptView> page = receiptService.queryReceiptPage(object, new PageScope(pageNum, pageSize));
        model.addAttribute("search", object);
        model.addAttribute("results", page);
        return "/backstage/receipt/sendcustomer";
    }

    /**
     * 批量修改回单状态
     *
     * @param object
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "modifyReceiptStatus")
    @ResponseBody
    public JsonResult modifyReceiptStatus(@RequestBody RequestObject object, HttpServletRequest request, Model model)
            throws Exception {
        User user = RequestUitl.getUserInfo(request);
        object.put("userId", user.getId());
        MergeReceiptStatus mergeReceipt = new MergeReceiptStatus();
        mergeReceipt.setReceiptStatus(object.getInteger("receiptStatus"));
        mergeReceipt.setUserId(user.getId());
        mergeReceipt.setWaybills(JSON.parseArray(object.get("waybills"), Integer.class));
        Integer falg = receiptService.modifyReceiptStatus(mergeReceipt);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("falg", falg);
        return jsonResult;
    }

    /**
     * 库存回单下载、送交客户下载
     *
     * @param object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/papery/export")
    @ResponseBody
    public JsonResult export(@RequestBody RequestObject object, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        logger.info("-----------papery export------------{}", object);
        User u = RequestUitl.getUserInfo(request);
        // 送交客户时间段
        TimeCycle sendCycle = new TimeCycle(object.getDate("serviceStartTime"), object.getDate("serviceEndTime"));
        PaperyReceipt reModel = new PaperyReceipt();
        reModel.setUserId(u.getId());
        reModel.setBatchNumber(object.get("batchNumber"));
        reModel.setReceiptStatus(object.getInteger("receiptStatus"));
        reModel.setDeliveryNumber(object.get("waybillDeliveryNumber"));
        reModel.setGroupid(object.getInteger("groupid"));
        Integer type = object.getInteger("type");// 导出类型1库存导出2已送达客户
        // 发货时间段
        TimeCycle deliverCycle = new TimeCycle(object.getDate("deliverStartTime"), object.getDate("deliverEndTime"));
        FileEntity fileEntity = receiptService.exportPaperyReceipt(reModel, sendCycle, deliverCycle, type);
        JsonResult jsonResult = new JsonResult();
        if (null != fileEntity && StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());

            String fileName = FileUtils.appendSuffix((1 == type) ? "库存回单" : "送交客户回单", fileEntity.getSuffix());

            jsonResult.put("fileName", fileName);
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), fileName));
        } else {
            jsonResult.modify(false, "没有找到相应数据文件");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/wait/detail")
    public String waitDetail(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        logger.info("-----------findById------------{}", body);
        Integer waybillId = body.getInteger("wkey");
        Assert.notNull(waybillId, "运单编号不能为空");
        User user = RequestUitl.getUserInfo(request);
        MergeWaybill waybill = waybillService.getWaybillById(user.getId(), waybillId, WaybillAssociate.associateDetail());
        if (waybill != null) {
            model.addAttribute("waybill", waybill);
            model.addAttribute("imagePath", imagepath());// 图片服务器的路径
        }
        return "/backstage/receipt/waitdetail";
    }

    @RequestMapping(value = "/wait/search")
    public String waitSearch(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User u = RequestUitl.getUserInfo(request);
        WaybillSerach serach = new WaybillSerach();
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
        if (groupList != null) {
            model.addAttribute("groups", groupList);
        }
        serach.setGroupId(body.getInteger("groupid"));
        serach.setUserId(u.getId());
        serach.setLikeString(body.get("likeString"));
        serach.setWaybillFettles(new Integer[]{Constant.WAYBILL_STATUS_BIND, Constant.WAYBILL_STATUS_ING, Constant.WAYBILL_STATUS_ARRIVE});
        serach.setWaitFettle(body.getInteger("waitFettle"));
        if (serach.getWaitFettle() == null) {
            serach.setWaitFettle(0);
        }
        Boolean delay = body.getBoolean("delay");
        if (delay != null) {
            serach.setDelay(delay ? 1 : 2);
        } else {
            serach.setDelay(0);
        }
        serach.setTime(body.getDate("startTime"), body.getDate("endTime"), -30);

        body.put("startTime", DateUtils.formatDate(serach.getFirstTime(), "yyyy-MM-dd"));
        body.put("endTime", DateUtils.formatDate(serach.getSecondTime(), "yyyy-MM-dd"));

        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");

        WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
        context.setAssociate(WaybillAssociate.associateProjectGroup());
        model.addAttribute("page", waybillService.pageMergeWaybill(context));
        model.addAttribute("search", body);
        return "/backstage/receipt/waithandle";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult upload(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer waybillKey = body.getInteger("waybillKey");
        Assert.notBlank(waybillKey, "请选择任务单后再上传回单");
        User user = RequestUitl.getUserInfo(request);
        Collection<String> collection = FileUploadHelper.saveImage(request, SystemUtils.directoryUpload(), Directory.UPLOAD_RECEIPT.getDir());
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException("请上传一个回单文件!!!");
        }
        receiptService.saveReceipt(user, waybillKey, collection, false);
        return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/batchupload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult batchUpload(HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult(true, "上传成功");
        HashMap<Integer, String> collection = saveReceipt(request, SystemUtils.directoryUpload(), Directory.UPLOAD_RECEIPT.getDir());
        if (collection.isEmpty()) {
            throw new BusinessException("请上传一个回单文件!!!");
        }
        receiptService.saveReceipt(user,collection);
        jsonResult.put("imgUrl",SystemUtils.staticPathPrefix());
        jsonResult.put("imagPaths", collection.values());
        return jsonResult;
    }
    
    
    private  HashMap<Integer, String> saveReceipt(HttpServletRequest request, String baseDic, String subDic) throws Exception {
        HashMap<Integer, String> map = new HashMap<Integer,String>();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String suffix = FileUtils.suffix(file.getOriginalFilename());
                    if (StringUtils.isBlank(suffix)) {
                        suffix = "jpeg";
                    }
                    if (!FileUtils.isImage(suffix)) {
                        throw new BusinessException("必须是图片文件,请重新选择文件!!!");
                    }
                    String fileName = FileUtils.name(file.getOriginalFilename());
                   
                    Waybill waybill = waybillService.getWaybillByCode(fileName);
                    if(waybill == null) continue;
                    String filePath = FileUtils.appendSuffix(StringUtils.UUID(), suffix);
                    if(StringUtils.isNotBlank(subDic)){
                        filePath = FileUtils.path(subDic, filePath);
                    }
                    file.transferTo(FileUtils.file(FileUtils.path(baseDic, filePath), true));
                    map.put(waybill.getId(), "/" + filePath);
                    logger.info("saveReceipt=========>waybillId:{},filePath:{}",waybill.getId(),filePath);
                }
            }
        }
        return map;
    }
}
