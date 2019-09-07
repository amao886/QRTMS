/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 15:10:55
 */
package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.DriverTrack;
import com.ycg.ksh.entity.persistent.TransitionException;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AssociateUser;
import com.ycg.ksh.entity.service.DriverContainerSearch;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * 司机控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 15:10:55
 */
@Controller("mobile.driver.controller")
@RequestMapping("/mobile/driver")
public class DriverController extends BaseController{

    @Resource
    private DriverService driverService;
    @Resource
    private BarCodeService barCodeService;
    //@Resource
    //private WaybillTrackService trackService;
    @Resource
    private ReceiptService receiptService;
    @Resource
    private ExceptionService exceptionService;
    @Resource
    private ActivityService activityService;

    //根据条码查询装车详情
    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResult detail(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (body == null) {
            body = new RequestObject();
        }
        String code = body.get("barcode");
        Assert.notBlank(code, "二维码号不能为空");
        User user = RequestUitl.getUserInfo(request);
        jsonResult.put("result", driverService.getMergeByBarcode(body.get("barcode")));
        jsonResult.put("user", new AssociateUser(user));
        return jsonResult;
    }

    //司机装车
    @RequestMapping(value = "/scan/load")
    @ResponseBody
    public JsonResult scanLoad(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (body == null) {
            body = new RequestObject();
        }
        String code = body.get("barcode");
        Assert.notBlank(code, "二维码号不能为空");
        User u = RequestUitl.getUserInfo(request);
        String dcode = driverService.load(u.getId(), code);
        if(StringUtils.isNotBlank(dcode)) {
        	jsonResult.put("barcode", dcode);
        }else {
        	jsonResult.put("barcode", "");
        }
        return jsonResult;
    }
    
    //司机卸货
    @RequestMapping(value = "/scan/unload")
    @ResponseBody
    public JsonResult scanunLoad(@RequestBody(required = false) RequestObject body,HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        if (body == null) {
            body = new RequestObject();
        }
        String code = body.get("barcode");
        Assert.notBlank(code, "二维码号不能为空");
        jsonResult.put("barcode", driverService.unload(u.getId(), code));
        return jsonResult;
    }
    
    //查询装车信息
    @RequestMapping(value = "/search")
    @ResponseBody
    public JsonResult search(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (body == null) {
            body = new RequestObject();
        }
        User u = RequestUitl.getUserInfo(request);
        DriverContainerSearch search = body.toJavaBean(DriverContainerSearch.class);
        search.setUserId(u.getId());
        if(search.getUnload() == null) {
        	search.setUnload(false);
        }
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        jsonResult.put("page", driverService.pageByDriver(search, new PageScope(pageNum, pageSize)));
        //最后一次轨迹上报信息
        jsonResult.put("track", driverService.lastDriverTrack(u.getId()));
        jsonResult.put("loadCount", driverService.loadCount(u.getId()));
        return jsonResult;
    }
    
    /**
     * 批量定位
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/loaction")
    @ResponseBody
    public JsonResult loaction(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        if (body == null) {
            body = new RequestObject();
        }
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        DriverTrack transitionTrack = body.toJavaBean(DriverTrack.class);
        transitionTrack.setUserId(u.getId());
        driverService.saveLoaction(u.getId(), body.get("barcode"), transitionTrack);

        /*
        LotteryNote lotteryNote = activityService.lotteryValidate(u.getId(), CoreConstants.LOTTERY_TYPE_DRIVER_CONTAINER, null);
        if(lotteryNote != null){
            jsonResult.put("lotteryKey", lotteryNote.getNoteId());//抽奖资格ID
        }
        */
        return jsonResult;
    }
    
    //保存送货单图片
    @RequestMapping(value = "/image/delivery")
    @ResponseBody
    public JsonResult saveDeliveryImage(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("saveDeliveryImage -> {}", body);
    	if (body == null) {
            body = new RequestObject();
        }
        User u = RequestUitl.getUserInfo(request);
        String barcode = body.get("barcode");
        Assert.notBlank(barcode, "条码号不能为空");
        String wxServerKeys = body.get("serverIds");
        Assert.notBlank(wxServerKeys, "图片信息不能为空");
        Collection<String> collection = new ArrayList<String>();
        StringTokenizer token = new StringTokenizer(wxServerKeys, ",");
        while (token.hasMoreElements()) {
        	String serverId = token.nextToken();
        	if(StringUtils.isNotBlank(serverId)) {
        		collection.add(serverId);
        	}
        }
        driverService.saveDeliveryImage(u.getId(), barcode, collection, true);
        return JsonResult.SUCCESS;
    }

    //保存送货单图片
    @RequestMapping(value = "/image/delivery/app")
    @ResponseBody
    public JsonResult saveDeliveryImageByApp(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        String barcode = body.get("barcode");
        Assert.notBlank(barcode, "条码号不能为空");
        User user = RequestUitl.getUserInfo(request);
        Collection<String> collection = FileUploadHelper.saveImage(request, SystemUtils.directoryUpload(), Directory.UPLOAD_DELIVERY.getDir());
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException("请上传一个送货单文件!!!");
        }
        driverService.saveDeliveryImage(user.getId(), barcode, collection, false);
        return JsonResult.SUCCESS;
    }

    
    //保存回单图片
    @RequestMapping(value = "/image/receipt")
    @ResponseBody
    public JsonResult saveReceipt(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
    	logger.info("saveReceipt -> {}", body);
    	if (body == null) {
            body = new RequestObject();
        }
        User u = RequestUitl.getUserInfo(request);
        String barcode = body.get("barcode");
        Assert.notBlank(barcode, "条码号不能为空");
        String wxServerKeys = body.get("serverIds");
        Assert.notBlank(wxServerKeys, "图片信息不能为空");
        Collection<String> collection = new ArrayList<String>();
        StringTokenizer token = new StringTokenizer(wxServerKeys, ",");
        while (token.hasMoreElements()) {
        	String serverId = token.nextToken();
        	if(StringUtils.isNotBlank(serverId)) {
        		collection.add(serverId);
        	}
        }
        receiptService.transitionReceipt(u.getId(), barcode, collection, true);
        return JsonResult.SUCCESS;
    }
    //保存回单图片
    @RequestMapping(value = "/image/receipt/app")
    @ResponseBody
    public JsonResult saveReceiptByApp(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        String barcode = body.get("barcode");
        Assert.notBlank(barcode, "条码号不能为空");
        User user = RequestUitl.getUserInfo(request);
        Collection<String> collection = FileUploadHelper.saveImage(request, SystemUtils.directoryUpload(), Directory.UPLOAD_RECEIPT.getDir());
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException("请上传一个送货单文件!!!");
        }
        receiptService.transitionReceipt(user.getId(), barcode, collection, false);
        return JsonResult.SUCCESS;
    }

    //异常上报
    @RequestMapping(value = "/exception")
    @ResponseBody
    public JsonResult exception(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("exception -> {}", body);
        if (body == null) {
            body = new RequestObject();
        }
        User u = RequestUitl.getUserInfo(request);
        TransitionException exception = new TransitionException();
        exception.setBarcode(body.get("barcode"));
        exception.setContent(body.get("context"));
        Assert.notBlank(exception.getBarcode(), "条码号不能为空");
        Collection<String> collection = new ArrayList<String>();
        StringTokenizer token = new StringTokenizer(body.get("serverIds"), ",");
        while (token.hasMoreElements()) {
            String serverId = token.nextToken();
            if(StringUtils.isNotBlank(serverId)) {
                collection.add(serverId);
            }
        }
        exception.setUserId(u.getId());
        exceptionService.saveException(exception, collection, true);
        return JsonResult.SUCCESS;
    }

    //异常上报
    @RequestMapping(value = "/exception/app")
    @ResponseBody
    public JsonResult exceptionByApp(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User u = RequestUitl.getUserInfo(request);
        TransitionException exception = new TransitionException();
        exception.setBarcode(body.get("barcode"));
        exception.setContent(body.get("context"));
        Assert.notBlank(exception.getBarcode(), "条码号不能为空");
        exception.setUserId(u.getId());
        Collection<String> collection = FileUploadHelper.saveImage(request, SystemUtils.directoryUpload(), Directory.UPLOAD_EXCEPTION.getDir());
        exceptionService.saveException(exception, collection, false);
        return JsonResult.SUCCESS;
    }

}
