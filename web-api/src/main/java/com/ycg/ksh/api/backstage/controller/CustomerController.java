package com.ycg.ksh.api.backstage.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AddressSearch;
import com.ycg.ksh.entity.service.MergeCustomer;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.ProjectGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 客户
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 * 2017-09-17 14:29:18
 */
@Controller("backstage.customer.controller")
@RequestMapping("/backstage/customer")
public class CustomerController extends BaseController {


    @Resource
    private CustomerService customerService;
    @Resource
    private ProjectGroupService projectGroupService;

    // 关联客户信息
    @RequestMapping(value = "/search/ajax")
    @ResponseBody
    public JsonResult searchAjax(@RequestBody RequestObject params, HttpServletRequest request) throws Exception {
        Assert.notNull(params, Constant.PARAMS_ERROR);
        if (logger.isDebugEnabled()) {
            logger.info("-----------searchAjax------------{}", params);
        }
        AddressSearch search = params.toJavaBean(AddressSearch.class);
        JsonResult jsonResult = new JsonResult();
        if (logger.isDebugEnabled()) {
            logger.info("-----------searchAjax------------>search:{}", search);
        }
        jsonResult.put("customers", customerService.queryCustomer(search));
        return jsonResult;
    }

    @RequestMapping(value = "/select")
    @ResponseBody
    public JsonResult select(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("select --> {}", requestObject);
        JsonResult jsonResult = new JsonResult();
        User u = RequestUitl.getUserInfo(request);
        AddressSearch search = requestObject.toJavaBean(AddressSearch.class);
        search.setUserId(u.getId());
        if (search.getType() == null || search.getType() != 2) {
            search.setType(1);//地址类型，1：收货地址，2：发货地址
        }
        Integer pageSize = requestObject.getInteger("size");
        Integer pageNum = requestObject.getInteger("num");
        jsonResult.put("result", customerService.pageQueryByHotspot(search, new PageScope(pageNum, pageSize)));
        return jsonResult;
    }

    /**
     * TODO 客户信息查询
     * <p>
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 14:50:51
     */
    @RequestMapping(value = "/search")
    public String search(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        logger.info("-----------customerSearch------------body:{}", body);
        User u = RequestUitl.getUserInfo(request);
        model.addAttribute("groups", projectGroupService.listByUserKey(u.getId()));
        AddressSearch search = body.toJavaBean(AddressSearch.class);
        search.setUserId(u.getId());
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        if (StringUtils.isBlank(body.get("groupId"))) {
            search.setGroupId(null);
        }
        if (logger.isDebugEnabled()) {
            logger.info("传输数据============>:{}", search);
        }
        CustomPage<MergeCustomer> customPage = customerService.pageQueryMergeCustomer(search, new PageScope(pageNum, pageSize));
        model.addAttribute("page", customPage);
        if (logger.isDebugEnabled()) {
            logger.info("结果==========>:result:{}", customPage);
        }
        model.addAttribute("search", body);
        return "/backstage/customer/manage";
    }

    @RequestMapping(value = "/findById/{id}")
    @ResponseBody
    public JsonResult findById(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("-----------findById------------{}", id);
        }
        JsonResult jsonResult = new JsonResult();
        if (logger.isDebugEnabled()) {
            logger.info("======resultInfo==========>:" + customerService.queryByKey(id));
        }
        jsonResult.put("customer", customerService.queryByKey(id));
        return jsonResult;
    }

    // 修改客户
    @RequestMapping(value = "/edit")
    @ResponseBody
    public JsonResult editCustomer(@RequestBody RequestObject params, Model model, HttpServletRequest request)
            throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("-----------------editCustomer----------------->:{}", params);
        }
        Assert.notNull(params, "客户信息不能为空");
        Customer customer = params.toJavaBean(Customer.class);
        customerService.update(customer);
        return JsonResult.SUCCESS;
    }

    // 新增客户
    @RequestMapping(value = "/save")
    @ResponseBody
    public JsonResult saveCustomer(@RequestBody RequestObject params, HttpServletRequest request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("-----------------saveCustomer----------------->{}", params);
        }
        Assert.notEmpty(params, Constant.PARAMS_ERROR);
        User u = RequestUitl.getUserInfo(request);
        params.put("userid", u.getId());
        Integer belongType = params.getInteger("belongType");
        Customer customer = params.toJavaBean(Customer.class);
        customerService.save(belongType, customer);
        return JsonResult.SUCCESS;
    }

    /**
     * 客户对应的电子围栏开关状态修改（此功能已取消）
     *
     * @param customer
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateGrateSatus")
    @ResponseBody
    public JsonResult updateGrateSatus(@RequestBody RequestObject params, HttpServletRequest request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("-----------------updateGrateSatus----------------->{}", params);
        }
        Assert.notNull(params, "客户信息不能为空");
        JsonResult result = new JsonResult();
        Integer customerid = params.getInteger("id");
        Integer status = params.getInteger("grateSatus");
        status = customerService.updateGrate(customerid, status);
        result.put("status", status);
        return result;
    }

    /**
     * TODO 常用地址删除
     * <p>
     *
     * @param params
     * @param request
     * @return
     * @throws Exception
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-22 19:39:53
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public JsonResult delete(@RequestBody RequestObject params, HttpServletRequest request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("-----------------delete-----customerId:{}------------>{}", params);
        }
        Customer customer = new Customer();
        customer.setId(params.getInteger("customerId"));
        customer.setGroupId(params.getInteger("groupId"));
        customerService.delete(customer);
        return JsonResult.SUCCESS;
    }
}
