package com.ycg.ksh.api.Scene.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.scene.application.SceneApplicationService;
import com.ycg.ksh.core.scene.application.dto.DeliveryCarDto;
import com.ycg.ksh.core.scene.application.dto.VehicleRegistrationDto;
import com.ycg.ksh.core.util.Constants;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.OrderService;
import com.ycg.ksh.service.util.O;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

/**
 * 现场管理控制层
 *
 * @author: wangke
 * @create: 2018-12-13 15:30
 **/
@Controller("enterprise.scene.controller")
@RequestMapping("/scene")
public class SceneController extends BaseController {

    @Resource
    OrderService orderService;

    @Resource
    SceneApplicationService applicationService;


    /**
     * 根据送货单号查询派车信息
     *
     * @param deliveryNo
     * @return
     */
    @RequestMapping(value = "/queryDeliveryDetails/{deliveryNo}")
    @ResponseBody
    public JsonResult queryDeliveryDetails(@PathVariable String deliveryNo, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Order o = orderService.getOrderBySD(loadCompanyKey(request), deliveryNo);
        if (null != o && o.getId() > 0) {
            VehicleRegistrationDto dto = Optional.ofNullable(applicationService.getVehicleRegistration(o.getId())).orElse(new VehicleRegistrationDto(o.getId()));
            if(dto.getDeliveryCarDto() == null){
                OrderExtra extra = orderService.getOrderExtraByOrderKey(o.getId());
                if(extra != null){
                    dto.setDeliveryCarDto(new DeliveryCarDto(o.getDeliveryNo(), extra.getDriverName(), extra.getDriverContact(), extra.getCareNo()));
                }else{
                    dto.setDeliveryCarDto(new DeliveryCarDto(o.getDeliveryNo(), null, null, null));
                }
            }
            jsonResult.put("result", dto);
        } else {
            jsonResult.modify(false, "根据送货单号["+ deliveryNo +"]未找到送货单信息");
        }
        return jsonResult;
    }

    /**
     * 到车登记
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/vehicle/save")
    @ResponseBody
    public JsonResult save(@RequestBody VehicleRegistrationDto dto, HttpServletRequest request) {
        Assert.notBlank(dto.getOrderKey(), "订单编号不能为空");
        Assert.notNull(dto.getArrivalsCarDto(), "到车信息不能为空");
        Assert.notBlank(dto.getArrivalsCarDto().getArrivalType(), "请选择到达类型");
        applicationService.save(dto);
        return JsonResult.SUCCESS;
    }

    /**
     * 查询登记车辆详情
     *
     * @param orderKey
     * @return
     */
    @RequestMapping(value = "/query/registration/{orderKey}")
    @ResponseBody
    public JsonResult getRegistrationDetail(@PathVariable Long orderKey) {
        return new JsonResult().add("result", applicationService.getVehicleRegistration(orderKey));
    }

    /**
     * 分页查询现场列表
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping(value = "/search/list")
    @ResponseBody
    public JsonResult searchPage(@RequestBody RequestObject body, HttpServletRequest request) {
        Integer status = body.getInteger("status");
        String likeString = body.get("likeString");
        LocalDate pickTime = body.getLocalDate("pickTime");
        Integer arrivalType = body.getInteger("type");
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", applicationService.searchPage(loadCompanyKey(request), status, likeString, arrivalType, pickTime, scope));
        return jsonResult;
    }
}
