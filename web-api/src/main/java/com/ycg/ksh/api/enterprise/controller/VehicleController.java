package com.ycg.ksh.api.enterprise.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.enterprise.Vehicle;
import com.ycg.ksh.entity.persistent.enterprise.VehicleDesignate;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.VehicleSearch;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 车辆管理
 */
@Controller("enterprise.vehicle.controller")
@RequestMapping("/enterprise/vehicle")
public class VehicleController extends BaseController {

    @Resource
    VehicleService vehicleService;

    @Resource
    CompanyService companyService;

    /**
     * 要车管理入口
     *
     * @param viewKey
     * @param model
     * @param request
     * @throws Exception
     * @Author: wangke
     */
    @RequestMapping(value = "/search/{viewKey}")
    public String search(@PathVariable String viewKey, Model model, HttpServletRequest request) {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/vehicle/" + viewKey;
    }

    /**
     * 要车录入
     *
     * @return
     */
    @RequestMapping(value = "/load/save")
    public String loadSave(HttpServletRequest request) {
        return "/enterprise/vehicle/detail";
    }

    /**
     * 分页查询要车列表
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     * @Author: wangke
     * @Date: 2018/10/22
     */
    @RequestMapping(value = "/listNeedCar")
    @ResponseBody
    public JsonResult listNeedCar(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        VehicleSearch vehicleSearch = requestObject.toJavaBean(VehicleSearch::new);
        PageScope scope = new PageScope(requestObject.getInteger("num"), requestObject.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", vehicleService.listByNeedCar(loadUserKey(request), vehicleSearch, scope));
        return jsonResult;
    }

    /**
     * 分页查询派車列表
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     * @Author: wangke
     * @Date: 2018/10/22
     */
    @RequestMapping(value = "/listSendCar")
    @ResponseBody
    public JsonResult listSendCar(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        VehicleSearch vehicleSearch = requestObject.toJavaBean(VehicleSearch::new);
        PageScope scope = new PageScope(requestObject.getInteger("num"), requestObject.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        //查询上级企业
        jsonResult.put("lastCompanys", vehicleService.listSource(companyService.assertCompanyByUserKey(loadUserKey(request)).getId()));
        jsonResult.put("page", vehicleService.listBySendCar(loadUserKey(request), vehicleSearch, scope));
        return jsonResult;
    }

    /**
     * 车辆录入
     *
     * @param requestObject
     * @param request
     * @return
     * @Author: wangke
     * @Date: 2018/10/19
     */
    @RequestMapping(value = "/car/save")
    @ResponseBody
    public JsonResult saveVehicle(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        String vehicleStr = requestObject.get("vehicle");
        Vehicle vehicle = Globallys.toJavaObject(vehicleStr, Vehicle.class);
        vehicle.setCreateUser(loadUserKey(request));
        String designateStr = requestObject.get("designate");
        VehicleDesignate designate = Globallys.toJavaObject(designateStr, VehicleDesignate.class);
        vehicleService.saveVehicle(vehicle, designate);
        return JsonResult.SUCCESS;
    }

    /**
     * 要车
     *
     * @param requestObject
     * @param request
     * @return
     * @Author: wangke
     * @Date: 2018/10/19
     */
    @RequestMapping(value = "/car/want")
    @ResponseBody
    public JsonResult wantCar(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        String designateStr = requestObject.get("designate");
        VehicleDesignate designate = Globallys.toJavaObject(designateStr, VehicleDesignate.class);
        Assert.notBlank(designate.getvId(), "要车单编号不能为空");
        Assert.notBlank(designate.getConveyId(), "请选择物流商");
        vehicleService.wantCar(loadUserKey(request), designate);
        return JsonResult.SUCCESS;
    }


    /**
     * 确认派车
     *
     * @param key
     * @param request
     * @return
     */
    @RequestMapping(value = "/car/send/confirm/{key}")
    @ResponseBody
    public JsonResult confirmCar(@PathVariable Long key, HttpServletRequest request) {
        vehicleService.confirmCar(key);
        return JsonResult.SUCCESS;
    }

    /**
     * 车辆删除
     *
     * @param key
     * @param request
     * @return
     * @Author: wangke
     * @Date: 2018/10/19
     */
    @RequestMapping(value = "/car/delete/{key}")
    @ResponseBody
    public JsonResult delVehicle(@PathVariable Long key, HttpServletRequest request) throws Exception {
        Assert.notBlank(key, "编号不能为空");
        vehicleService.deleteVehicle(key);
        return JsonResult.SUCCESS;
    }


    /**
     * 派车调整
     *
     * @param requestObject
     * @param request
     * @return
     * @Author: wangke
     * @Date: 2018/10/22
     */
    @RequestMapping(value = "/car/send/modify")
    @ResponseBody
    public JsonResult modifyVehicle(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        String vehicleStr = requestObject.get("vehicle");
        Vehicle vehicle = Globallys.toJavaObject(vehicleStr, Vehicle.class);
        Assert.notNull(vehicle, Constant.PARAMS_ERROR);
        Assert.notBlank(vehicle.getCarNo(), "车牌号不能为空");
        Assert.notBlank(vehicle.getDriverName(), "司机姓名不能为空");
        Assert.notBlank(vehicle.getDriverNumber(), "司机电话不能为空");
        Collection<Long> vids = StringUtils.longCollection(requestObject.get("vids"));
        Assert.notEmpty(vids, "请至少选择一项要车单");
        vehicleService.modifyVehicle(vehicle, vids);
        return JsonResult.SUCCESS;
    }

    /**
     * 要车单详情
     *
     * @param key
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/car/detail/{key}")
    @ResponseBody
    public JsonResult vehicleDetail(@PathVariable Long key, HttpServletRequest request) throws Exception {
        Assert.notBlank(key, "要车单编号不能为空");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", vehicleService.queryVehicle(key));
        return jsonResult;
    }
}
