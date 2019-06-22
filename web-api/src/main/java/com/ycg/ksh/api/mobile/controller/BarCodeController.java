package com.ycg.ksh.api.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.WaybillContext;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.api.WaybillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller("mobile.barcode.controller")
@RequestMapping("/mobile/barCode")
public class BarCodeController extends BaseController {

    @Resource
    private WaybillService waybillService;
    @Resource
    private BarCodeService codeService;

    /**
     * 查询条码状态
     * <p>
     *
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-19 14:28:06
     */
    @RequestMapping(value = "/validate/code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult validate(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("validate code -> {}", object);
        String barcode = object.get("barcode");
        Assert.notBlank(barcode, "二维码信息不能为空");
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", barCodeService.validate(user.getId(), barcode));
        return jsonResult;
    }

    // 扫码完成以后需要先去录单页面(此处的页面申请路径是录单页面weixin/toAddBarCode)
    @RequestMapping(value = "/toAdd")
    public String toAddBarCode(HttpServletRequest request, Model model) {
        String barCode = request.getParameter("bindCode");
        if (StringUtils.isNotEmpty(barCode)) {
            request.setAttribute("bindCode", barCode);
        }
        return "barCodeAfter";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addBarCode(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("bind waybill -> {}", object);
        Assert.notNull(object, Constant.PARSEERROR);
        User user = RequestUitl.getUserInfo(request);
        Waybill waybill = object.toJavaBean(Waybill.class);

        Assert.notBlank(waybill.getBarcode(), "条码号不能为空");

        Integer receiverKey = object.getInteger("customerId"), shipperKey = object.getInteger("sendCustomerId");
        Assert.notBlank(receiverKey, "请选择收货信息");
        Assert.notBlank(shipperKey, "请选择发货信息");
        WaybillContext context = WaybillContext.buildContext(user, waybill);
        context.setUpdate(waybill);

        context.setSendType(Constant.CUSTOMER_SELECT_BYID);
        context.setReceiveType(Constant.CUSTOMER_SELECT_BYID);
        //客户类型(1：收货客户 ，2：发货客户)
        context.setCustomers(new Customer[]{new Customer(Constant.CUSTOMER_TYPE_RECEIVER , receiverKey), new Customer(Constant.CUSTOMER_TYPE_SEND , shipperKey)});
        //绑定时的轨迹信息
        WaybillTrack waybillTrack = new WaybillTrack();
        if (StringUtils.isNotBlank(waybill.getLatitude())) {
            waybillTrack.setLatitude(new Double(waybill.getLatitude()));
        }
        if (StringUtils.isNotBlank(waybill.getLongitude())) {
            waybillTrack.setLongitude(new Double(waybill.getLongitude()));
        }
        waybillTrack.setLocations(waybill.getAddress());
        context.setWaybillTrack(waybillTrack);
        //要求到货信息
        context.setArriveDay(object.getInteger("arriveDay"));
        context.setArriveHour(object.getInteger("arriveHour"));
        context.setCommodities(JSON.parseArray(object.get("commodities"), Goods.class));
        waybillService.saveBind(context);
        return JsonResult.SUCCESS;
    }

    /**
     * TODO 任务单和条码绑定（此逻辑用于绑定导入的任务单和微信端我要发货）
     * <p>
     *
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-19 14:28:06
     */
    @RequestMapping(value = "/bind/waybill", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult bindCodeToWaybill(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("select bind waybill -> {}", object);
        Assert.notNull(object, "运单信息不能为空");
        User user = RequestUitl.getUserInfo(request);
        WaybillContext context = WaybillContext.buildContext(user);
        context.setUpdate(object.toJavaBean(Waybill.class));
        context.setArriveDay(object.getInteger("arriveday"));
        context.setArriveHour(object.getInteger("arrivehour"));
        context.setAddress(object.get("locations"));
        if(StringUtils.isNotBlank(context.getAddress())){
            //绑定时的轨迹信息
            WaybillTrack waybillTrack = new WaybillTrack();
            if (StringUtils.isNotBlank(context.getLatitude())) {
                waybillTrack.setLatitude(new Double(context.getLatitude()));
            }
            if (StringUtils.isNotBlank(context.getLongitude())) {
                waybillTrack.setLongitude(new Double(context.getLongitude()));
            }
            waybillTrack.setLocations(context.getAddress());
            context.setWaybillTrack(waybillTrack);
        }
        waybillService.saveSelectBind(context);
        return JsonResult.SUCCESS;
    }


    /**
     * 验证条码状态
     * <p>
     *
     * @param barcode
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-21 19:01:07
     */
    @RequestMapping("/verifyAjax/{barcode}")
    @ResponseBody
    public JsonResult barcodeVerify(@PathVariable String barcode, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        Assert.hasLength(barcode, Constant.PARAMS_ERROR);
        BarcodeContext context = barCodeService.validate(user.getId(), barcode);
        if(context instanceof GroupCodeContext) {
            GroupCodeContext codeContext = (GroupCodeContext) context;
            jsonResult.put("code", codeContext.getStatus().getCode());
        }else{
            jsonResult.modify(false, "企业二维码");
        }
        return jsonResult;
    }

    /**
     * 根据条码获取条码信息，先从缓存中取，如果缓存没有就从数据库中取
     * <p>
     *
     * @param barcode
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-21 18:53:18
     */
    @RequestMapping(value = "/getBindCache/{barcode}")
    @ResponseBody
    public JsonResult getBindCache(@PathVariable String barcode, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        Assert.hasLength(barcode, Constant.PARAMS_ERROR);
        logger.debug("getBindCache===================> {}", barcode);
        Waybill waybill = waybillService.getWaybillByCode(barcode);
        if (null != waybill) {
            JSONObject resultBack = new JSONObject();

            resultBack.put("bindstatus", waybill.getWaybillStatus());
            resultBack.put("barcode", waybill.getBarcode());
            resultBack.put("companyName", waybill.getReceiverName());
            resultBack.put("contacts", waybill.getContactName());
            resultBack.put("contactNumber", waybill.getContactPhone());
            resultBack.put("tel", waybill.getReceiverTel());
            resultBack.put("fullAddress", waybill.getReceiveAddress());
            resultBack.put("deliveryNumber", waybill.getDeliveryNumber());
            resultBack.put("orderSummary", waybill.getOrderSummary());
            resultBack.put("waybillId", waybill.getId());

            resultBack.put("subscribe", subscribe(user));

            jsonResult.put("results", resultBack);
        } else {
            jsonResult.modify(false, "未绑定!!!");
        }
        return jsonResult;
    }


    // 我的资源申请
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult resApply(@RequestBody RequestObject rObject, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("res apply -> {}", rObject);
        User user = RequestUitl.getUserInfo(request);
        codeService.applyRes(user.getId(), rObject.toJavaBean(ApplyRes.class));
        return JsonResult.SUCCESS;
    }

    // 查询条码数
    @RequestMapping(value = "/queryTotalCount", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult totalCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", barCodeService.queryTotalCount(user.getId()));
        return jsonResult;
    }
}
