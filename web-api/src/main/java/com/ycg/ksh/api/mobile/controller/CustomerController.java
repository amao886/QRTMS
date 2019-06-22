package com.ycg.ksh.api.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AddressSearch;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.ProjectGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//客戶信息ConTROLLER

@Controller("mobile.customer.controller")
@RequestMapping("/mobile/customer")
public class CustomerController extends BaseController {

    @Resource
    ProjectGroupService projectGroupService;

    @Resource
    CustomerService customerService;

    //查询客户列表跳转
    @RequestMapping(value = "/list")
    public String customerList(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bindCode = request.getParameter("bindCode");
        String companyName = request.getParameter("companyName");
        if (StringUtils.isNotBlank(companyName)) {
            //数据搜索回显
            model.addAttribute("companyName", StringUtils.trimToEmpty(companyName));
        }
        model.addAttribute("bindCode", bindCode);
        return "chooseCustom";
    }

    //查询客户列表(微信分页)
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult pageCustomer(@RequestBody(required = false) RequestObject object, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.info("page customer -> {}", object);
        if(object == null){
            object = new RequestObject();
        }
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        AddressSearch search = new AddressSearch();
        Integer groupid = object.getInteger("groupid");
        if(groupid != null){
            jsonResult.put("group", projectGroupService.getByGroupKey(groupid));
            search.setGroupId(groupid);
        }
        search.setUserId(user.getId());
        search.setType(object.getInteger("type"));
        search.setCompanyName(StringUtils.trimToEmpty(object.get("c_companyName")));
        Integer pageSize = object.getInteger("pageSize");
        Integer pageNum = object.getInteger("pageNum");
        CustomPage<Customer> page = customerService.pageQueryCustomer(search, new PageScope(pageNum, pageSize));
        jsonResult.put("results", page.getCollection());
        return jsonResult;
    }

    /**
     * 查询选中的客户信息
     *
     * @param customerId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/choose/{customerId}")
    @ResponseBody
    public JsonResult chooseCustom(@PathVariable Integer customerId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        JsonResult jsonResult = new JsonResult();
        getcustomeryId(customerId, jsonResult);
        return jsonResult;
    }

    /**
     * 客户添加接口
     *
     * @param object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addCustom(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.info("add customer -> {}", object);
        Integer belongType = object.getInteger("belongType");
        User user = RequestUitl.getUserInfo(request);
        Customer customer = object.toJavaBean(Customer.class);
        customer.setUserid(user.getId());
        customerService.save(belongType, customer);
        return JsonResult.SUCCESS;
    }

    /**
     * 通过customerid获取用户详细信息
     *
     * @param customerId
     * @param request
     * @param response
     * @return JsonResult
     * @throws Exception
     */
    @RequestMapping(value = "/queryById/{customerId}")
    @ResponseBody
    public JsonResult queryById(@PathVariable int customerId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        JsonResult jsonResult = new JsonResult();
        getcustomeryId(customerId, jsonResult);
        return jsonResult;
    }

    //修改客户
    @RequestMapping(value = "/update")
    @ResponseBody
    public JsonResult editCustom(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger.info("edit customer -> {}", object);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        Customer customer = object.toJavaBean(Customer.class);
        customer.setUserid(user.getId());
        customerService.update(customer);
        return new JsonResult();
    }

    //内部方法，通过id获取用户详细信息
    private void getcustomeryId(int id, JsonResult jsonResult)
            throws Exception {
        logger.info("-----------findById------------" + id);
        logger.info("---传输数据：---" + id);
        Customer customer = customerService.queryByKey(id);
        if (null != customer) {
            jsonResult.put("customer", customer);
        } else {
            jsonResult.modify(false, Constant.QUERY_FAIL);
        }
    }

    /**
     * 根据组ID查询组信息
     *
     * @param groupId
     * @param jsonResult
     * @throws Exception
     */
    private void queryGroupById(Integer groupId, JsonResult jsonResult)
            throws Exception {
        logger.info("-----------update------------" + groupId);
        Assert.notBlank(groupId, Constant.PARAMS_ERROR);
        JSONObject json = new JSONObject();
        json.put("id", groupId);
        logger.info("---传输数据：---" + json);
        ProjectGroup group = projectGroupService.getByGroupKey(groupId);
        jsonResult.put("group", group);
    }
}
