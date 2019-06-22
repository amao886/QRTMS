/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 15:10:55
 */
package com.ycg.ksh.api.enterprise.controller;

import com.google.common.collect.Lists;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.service.api.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 企业司机控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 15:10:55
 */
@Controller("enterprise.companyDriver.controller")
@RequestMapping("/enterprise/driver")
public class DriverController extends BaseController{

    @Resource
    private DriverService driverService;

    /**
     * 根据条码查询订单列表
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search")
    @ResponseBody
    public JsonResult search(@RequestBody(required = false) RequestObject requestObject, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (requestObject == null) {
            requestObject = new RequestObject();
        }
        OrderSearch search = requestObject.toJavaBean(OrderSearch.class);
        search.setUserKey(loadUserKey(request));
        Integer timeType = requestObject.getInteger("timeType");
        if(timeType != null){
            search.setTime(timeType);
        }else{
            search.setFirstTime(DateUtils.minOfDay(requestObject.getDate("firstTime")));
            search.setSecondTime(DateUtils.maxOfDay(requestObject.getDate("secondTime")));
        }
        search.setFettles(convertOrderFettles(requestObject.getInteger("fettle")));//0:全部,1:运输中,4:已到货
        Integer signFettle = requestObject.getInteger("signFettle");
        if (signFettle != null && signFettle > 0) {
            search.setSignFettles(Lists.newArrayList(signFettle));//0:全部,1:正常签收,2:异常签收
        }
        PageScope scope = new PageScope(requestObject.getInteger("num"), requestObject.getInteger("size"));
        jsonResult.put("page", driverService.pageOrderDeliver(search, scope));
        return jsonResult;
    }
}
