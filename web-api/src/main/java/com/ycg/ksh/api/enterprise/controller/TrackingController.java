package com.ycg.ksh.api.enterprise.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description: 跟踪管理控制器
 * @author: wangke
 * @create: 2018-10-10 10:49
 **/
@Controller("enterprise.tracking.controller")
@RequestMapping("/enterprise/tracking")
public class TrackingController extends BaseController {

    @Resource
    protected OrderService orderService;

    @Resource
    protected CompanyService companyService;

    /**
     * 跟踪管理入口
     *
     * @param viewkey
     * @param model
     * @param request
     * @throws Exception
     * @Author: wangke
     * @Date: 2018/10/10
     */
    @RequestMapping(value = "/search/{viewkey}")
    public String search(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/tracking/" + viewkey;
    }


    /**
     * 拓展通用接口
     *
     * @param viewkey
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/operate/{viewkey}")
    public String operateView(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/tracking/" + viewkey;
    }


    /**
     * 跟踪管理列表查询
     *
     * @param requestObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/search/list")
    @ResponseBody
    public JsonResult trackingList(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        AuthorizeUser user = loadUser(request);
        OrderSearch search = new OrderSearch();
        search.setLikeString(requestObject.get("likeString"));
        search.setPickupWarning(requestObject.getInteger("pickupWarning"));
        search.setDelayWarning(requestObject.getInteger("delayWarning"));
        search.setFirstTime(requestObject.getDate("firstTime"));
        search.setSecondTime(requestObject.getDate("secondTime"));
        if (user.getIdentityKey() - 2 == 0) {
            search.setPartnerType(PartnerType.SHIPPER);
        } else if (user.getIdentityKey() - 3 == 0) {
            search.setPartnerType(PartnerType.CONVEY);
        } else if (user.getIdentityKey() - 4 == 0) {
            search.setPartnerType(PartnerType.RECEIVE);
        }
        JsonResult result = new JsonResult();
        PageScope scope = new PageScope(requestObject.getInteger("num"), requestObject.getInteger("size"));
        result.put("page", orderService.pageOrder(user.getId(), search, scope));
        return result;
    }

    /**
     * 订单详情
     *
     * @param orderKey
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail/{orderKey}")
    @ResponseBody
    public JsonResult trackingDetail(@PathVariable Long orderKey, HttpServletRequest request) {
        Assert.notBlank(orderKey, "订单编号不能为空");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", orderService.getOrderAlliance(loadUserKey(request), orderKey, true));
        return JsonResult.SUCCESS;
    }

    /**
     * 跟踪设置
     *
     * @param requestObject
     * @return
     */
    @RequestMapping(value = "/setting")
    @ResponseBody
    public JsonResult trackingSetting(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        String frequency = requestObject.get("frequency");
        Assert.notBlank(frequency, "设置次数不能为空");
        companyService.trackingSetting(loadUserKey(request), frequency);
        return JsonResult.SUCCESS;
    }

}
