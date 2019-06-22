package com.ycg.ksh.api.backstage.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.api.DriverService;
import com.ycg.ksh.service.api.WaybillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 绑定运单控制层
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 9:15 2018/1/10
 * @Modified By:
 */
@Controller("backstage.driver.controller")
@RequestMapping("/backstage/driver")
public class DriverController extends BaseController {

    @Resource
    DriverService driverService;

    @Resource
    WaybillService waybillService;

    @Resource
    BarCodeService barCodeService;

    /**
     * 跳转到任务绑定页面
     *
     * @Author：wangke
     * @description：
     * @Date：15:25 2018/1/10
     */
    @RequestMapping(value = "/search")
    public String search(Model model, HttpServletRequest request) throws Exception {
        return "/backstage/waybill/bind_waybill";
    }

    @RequestMapping(value = "/list/wait/code")
    @ResponseBody
    public JsonResult listWaitCode(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        DriverContainerSearch driverCearch = body.toJavaBean(DriverContainerSearch.class);
        driverCearch.setUserId(u.getId());
        driverCearch.setBindStatus(WaybillFettle.UNBIND.getCode());
        driverCearch.setScope(0);
        Integer pageNum = body.getInteger("num");
        JsonResult result = new JsonResult();
        result.put("page", driverService.pageBySomething(driverCearch, new PageScope(pageNum, 5)));
        result.put("baseImg", SystemUtils.imagepath());
        return result;
    }


    /**
     * 选中查询上报信息
     *
     * @Author：wangke
     * @description：
     * @Date：17:16 2018/1/10
     */
    @RequestMapping(value = "/wait/code/detail")
    @ResponseBody
    public JsonResult waitCodeDetail(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        Barcode barcode = barCodeService.getBarcode(body.get("barcode"));
        WaybillSerach serach = new WaybillSerach();
        serach.setWaybillFettles(new Integer[]{Constant.WAYBILL_STATUS_WAIT});//待绑定的
        serach.setGroupId(barcode.getGroupid());
        serach.setUserId(u.getId());
        jsonResult.put("info", driverService.getMergeByBarcode(barcode.getBarcode()));
        WaybillContext context = WaybillContext.buildContext(u, serach);
        context.setPageScope(new PageScope(1, 5));
        jsonResult.put("waybills", waybillService.pageWaybill(context));
        return jsonResult;
    }

    @RequestMapping(value = "/list/unbind/waybill")
    @ResponseBody
    public JsonResult listUnbindWaybill(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        Barcode barcode = barCodeService.getBarcode(body.get("barcode"));
        WaybillSerach serach = new WaybillSerach();
        serach.setWaybillFettles(new Integer[]{Constant.WAYBILL_STATUS_WAIT});//待绑定的
        serach.setGroupId(barcode.getGroupid());
        serach.setUserId(u.getId());
        serach.setLikeString(body.get("likeString"));
        WaybillContext context = WaybillContext.buildContext(u, serach);
        context.setPageScope(new PageScope(body.getInteger("num"), 5));
        jsonResult.put("page", waybillService.pageWaybill(context));
        return jsonResult;
    }


    /**
     * 选中查询运单和货物信息
     *
     * @Author：wangke
     * @description：
     * @Date：17:18 2018/1/10
     */
    @RequestMapping(value = "searchWaybillInfo")
    @ResponseBody
    public JsonResult searchWaybillInfo(Integer waybillId, HttpServletRequest request) {
        Assert.notBlank(waybillId, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        WaybillAssociate associate = WaybillAssociate.associateEmpty();
        associate.setAssociateCommodity(true);
        associate.setAssociateProjectGroup(true);
        jsonResult.put("waybill", waybillService.getWaybillById(user.getId(), waybillId, associate));
        return jsonResult;
    }

    /**
     * 绑定运单
     *
     * @Author：wangke
     * @description：
     * @Date：8:29 2018/1/11
     */
    @RequestMapping(value = "bind/barcode")
    @ResponseBody
    public JsonResult bindBarcode(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        WaybillContext context = WaybillContext.buildContext(user);
        context.setUpdate(object.toJavaBean(Waybill.class));
        context.setArriveDay(object.getInteger("arriveday"));
        context.setArriveHour(object.getInteger("arrivehour"));
        waybillService.saveSelectBind(context);
        return JsonResult.SUCCESS;
    }
}
