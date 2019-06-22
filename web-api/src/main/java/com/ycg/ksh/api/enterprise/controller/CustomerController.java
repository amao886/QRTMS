package com.ycg.ksh.api.enterprise.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.entity.common.constant.HotspotType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.AddressSearch;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CustomerAlliance;
import com.ycg.ksh.entity.service.enterprise.CustomerSearch;
import com.ycg.ksh.service.api.AccessoryService;
import com.ycg.ksh.service.api.CustomerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 企业客户
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
@Controller("enterprise.customer.controller")
@RequestMapping("/enterprise/customer")
public class CustomerController extends BaseController {

    private static final String SELECTS_CACHE_KEY = "CUSTOMER_SELECTS_CACHE_KEY#";
    private static final String QR_CODE_EXPIRED_PREFIX = "QR_CODE_DOWNLOAD_";
    @Resource
    private CustomerService customerService;
    @Resource
    private AccessoryService accessoryService;

    /**
     * 客户管理菜单入口
     *
     * @return
     */
    @RequestMapping(value = "/view/{viewkey}")
    public String manage(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/customer/" + viewkey;
    }

    /**
     * 选择客户
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selects")
    @ResponseBody
    public JsonResult selects(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Collection<Long> customerKeys = StringUtils.longCollection(requestObject.get("selects"));
        if (CollectionUtils.isNotEmpty(customerKeys)) {
            Integer userKey = loadUserKey(request);
            accessoryService.modifyHotspot(userKey, HotspotType.COMPANY_CUSTOMER, customerKeys);
            cacheManger.set(SELECTS_CACHE_KEY + userKey, customerKeys);
        }
        return JsonResult.SUCCESS;
    }

    /**
     * 根据客户名称模糊查询客户信息(主要用于页面选择)
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public JsonResult list(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        //根据客户名称模糊查询客户信息
        CustomerSearch search = new CustomerSearch();
        search.setUserKey(loadUserKey(request));
        search.setLikeName(requestObject.get("likeName"));
        search.setType(requestObject.getInteger("type"));
        search.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);//查询启用的客户关系
        search.setUserHost(requestObject.getBoolean("userHost"));//查询时使用用户热点(用户选择的次数越多,排序越靠前)
        Integer reg = requestObject.getInteger("reg");
        if (reg != null) {
            search.setReg(reg == 1 ? true : false);
        }
        Collection<CustomerAlliance> customers = customerService.searchCustomerBySomething(search);
        if (customers != null && !customers.isEmpty()) {
            Collection<Long> collection = cacheManger.get(SELECTS_CACHE_KEY + search.getUserKey());
            if (collection != null && !collection.isEmpty()) {
                customers = customers.parallelStream().map(c -> {
                    if (collection.contains(c.getKey())) {
                        c.setCommonly(true);
                    }
                    return c;
                }).sorted((x, y) -> x.isCommonly() ? -1 : y.isCommonly() ? 1 : 0).collect(Collectors.toList());
            }
        }
        jsonResult.put("customers", customers);
        return jsonResult;
    }

    @RequestMapping(value = "/manage")
    @ResponseBody
    public JsonResult manage(@RequestBody(required = false) RequestObject requestObject, HttpServletRequest request) throws Exception {
        if (requestObject == null) {
            requestObject = new RequestObject();
        }
        JsonResult jsonResult = new JsonResult();
        CustomerSearch search = new CustomerSearch();
        search.setUserKey(loadUserKey(request));
        search.setLikeName(requestObject.get("likeName"));
        search.setType(requestObject.getInteger("type"));
        search.setStatus(CoreConstants.COMPANYCUSTOMER_STATUS_NORMAL);//查询启用的客户关系
        //search.setUserHost(true);//查询时使用用户热点(用户选择的次数越多,排序越靠前)

        Integer reg = requestObject.getInteger("reg");
        if (reg != null) {
            search.setReg(reg == 1 ? true : false);
        }
        Integer pageNum = requestObject.getInteger("num");
        Integer pageSize = requestObject.getInteger("size");
        jsonResult.put("page", customerService.pageCustomers(search, new PageScope(pageNum, pageSize)));
        return jsonResult;
    }

    /**
     * 根据员工编号查询员工有权限的客户编号
     *
     * @param employeeId 要查询的员工编号
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/employee/authority/{employeeId}")
    @ResponseBody
    public JsonResult employeeAuthority(@PathVariable Integer employeeId, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("authoritys", customerService.listCustomerAuthoritys(employeeId));
        return jsonResult;
    }

    /**
     * 根据员工编号查询员工有权限的客户编号
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/employee/authority/modify")
    @ResponseBody
    public JsonResult modifyEemployeeAuthority(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Integer employeeKey = requestObject.getInteger("employeeKey");
        Assert.notBlank(employeeKey, "员工编号不能为空");
        Collection<Long> customerKeys = StringUtils.longCollection(requestObject.get("customerKeys"));
        Assert.notEmpty(customerKeys, "至少选择一项客户数据");
        customerService.modifyCustomerAuthoritys(employeeKey, customerKeys);
        return JsonResult.SUCCESS;
    }

    /**
     * 根据客户编号查询客户详情
     *
     * @param key     要查询的客户编号
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/{key}")
    @ResponseBody
    public JsonResult detail(@PathVariable Long key, HttpServletRequest request) throws Exception {
        logger.info("detail company customer -> {}", key);
        JsonResult jsonResult = new JsonResult();
        AuthorizeUser user = RequestUitl.getUserInfo(request);
        jsonResult.put("customer", customerService.allianceCompanyCustomer(user.getId(), key));
        if (user != null) {
            jsonResult.put("companyName", user.getCompanyName());
        }
        return jsonResult;
    }

    /**
     * 添加客户
     *
     * @param requestObject 要添加的客户信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public JsonResult add(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("add company customer -> {}", requestObject);
        User user = RequestUitl.getUserInfo(request);
        customerService.saveCustomer(user.getId(), requestObject.get("customerName"), requestObject.getInteger("customerType")
        ,requestObject.get("scanPhone"));
        return JsonResult.SUCCESS;
    }

    /**
     * 编辑客户
     *
     * @param requestObject 要编辑的客户信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public JsonResult edit(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("edit company customer -> {}", requestObject);
        User user = RequestUitl.getUserInfo(request);
        customerService.editCustomer(user.getId(), requestObject.getLong("customerKey"), requestObject.get("customerName"), requestObject.getInteger("customerType"));
        return JsonResult.SUCCESS;
    }

    /**
     * 删除客户
     *
     * @param requestObject 要删除的客户信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public JsonResult delete(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("add company customer -> {}", requestObject);
        User user = RequestUitl.getUserInfo(request);

        Collection<Long> customerKeys = StringUtils.longCollection(requestObject.get("customerKey"));
        Assert.notEmpty(customerKeys, "至少选择一项要删除的数据");
        customerService.deleteCustomer(user.getId(), customerKeys);
        return JsonResult.SUCCESS;
    }

    /**
     * 取消客户管理
     *
     * @param requestObject 要取消的客户信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancle")
    @ResponseBody
    public JsonResult cancle(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("add company customer -> {}", requestObject);
        User user = RequestUitl.getUserInfo(request);
        customerService.cancleRelation(user.getId(), requestObject.getLong("customerKey"));
        return JsonResult.SUCCESS;
    }

    /**
     * 下载关联二维码
     *
     * @param customerKey 客户编号
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/download/{customerKey}")
    @ResponseBody
    public JsonResult downloadImage(@PathVariable Long customerKey, HttpServletRequest request) throws Exception {
        logger.info("download customerKey -> {}", customerKey);
        JsonResult jsonResult = new JsonResult();
        String url = RequestUitl.getServerPath(request) + "/mobile/wechat/company/associate/%d/%d";
        FileEntity fileEntity = customerService.buildQrcode(loadUserKey(request), customerKey, url);
        if (fileEntity != null) {
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix(fileEntity.getAliasName(), fileEntity.getSuffix()), true));
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            cacheManger.set(QR_CODE_EXPIRED_PREFIX + customerKey, customerKey, 1L, TimeUnit.HOURS);
        } else {
            jsonResult.modify(false, "二维码生成异常");
        }
        return jsonResult;
    }

    /**
     * 分页查询客户地址信息
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/address/search")
    @ResponseBody
    public JsonResult addressSearch(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        //根据条件查询客户地址信息
        AddressSearch search = requestObject.toJavaBean(AddressSearch.class);
        search.setUserId(loadUserKey(request));
        search.setCompanyId(Optional.ofNullable(loadUser(request))
                .map(user -> user.getEmployee())
                .map(employee -> employee.getCompanyId())
                .orElse(0l));
        PageScope pageScope = new PageScope(Optional.ofNullable(requestObject.getInteger("pageNum")).orElse(1),
                Optional.ofNullable(requestObject.getInteger("pageSize")).orElse(10));
        jsonResult.put("page", customerService.pageCompanyCustomer(search, pageScope));
        return jsonResult;
    }

    /**
     * 查询客户地址信息
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/address/list")
    @ResponseBody
    public JsonResult addressList(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        //根据条件查询客户地址信息
        AddressSearch search = requestObject.toJavaBean(AddressSearch.class);

        AuthorizeUser user = loadUser(request);

        search.setCompanyId(Optional.ofNullable(user)
                .map(u -> u.getEmployee())
                .map(employee -> employee.getCompanyId())
                .orElse(0l));
        search.setUserId(user.getId());
        jsonResult.put("addresss", customerService.searchAddressBySomething(search));
        return jsonResult;
    }

    /**
     * 删除客户地址信息
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/address/delete")
    @ResponseBody
    public JsonResult deleteSearch(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        customerService.deleteAddress(loadUserKey(request), StringUtils.integerCollection(requestObject.get("ids")));
        return jsonResult;
    }

    /**
     * 地址保存
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/address/save")
    @ResponseBody
    public JsonResult saveAddress(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        logger.info("save address -> {}", requestObject);
        //新增客户地址
        Customer customer = requestObject.toJavaBean(Customer.class);
        AuthorizeUser authorizeUser = loadUser(request);
        //customer.setCompanyId(Optional.ofNullable(loadUser(request)).map(AuthorizeUser::getEmployee).map(CompanyEmployee::getCompanyId).orElse(0L));
        customerService.saveAddress(authorizeUser.getId(), customer);
        return JsonResult.SUCCESS;
    }

    /**
     * 编辑客户地址
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/address/edit")
    @ResponseBody
    public JsonResult editAddress(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        //编辑客户地址
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        logger.info("save address -> {}", requestObject);
        Customer customer = requestObject.toJavaBean(Customer.class);
        customerService.editAddress(loadUserKey(request), customer);
        return JsonResult.SUCCESS;
    }

    /**
     * 查询地址详情
     *
     * @param key
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "address/detail/{key}")
    @ResponseBody
    public JsonResult searchAddressDetail(@PathVariable Integer key, HttpServletRequest request) throws Exception {
        Assert.notNull(key, Constant.PARAMS_ERROR);
        JsonResult result = new JsonResult();
        result.put("customer", customerService.queryByKey(key));
        return result;
    }

    /**
     * 查询地址详情
     *
     * @param key
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "address/last/{key}")
    @ResponseBody
    public JsonResult lastByCustomerKey(@PathVariable Long key, HttpServletRequest request) throws Exception {
        Assert.notNull(key, Constant.PARAMS_ERROR);
        JsonResult result = new JsonResult();
        result.put("address", customerService.lastAddressByCustomerKey(key));
        return result;
    }

    /**
     * 客户关系关联企业
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "associate/company")
    @ResponseBody
    public JsonResult associateCompany(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Long customerKey = requestObject.getLong("customerKey");
        Assert.notBlank(customerKey, "客户关系编号不能为空");
        Long companyKey = requestObject.getLong("companyKey");
        Assert.notBlank(companyKey, "企业编号不能为空");
        customerService.associateCompany(customerKey, companyKey);
        return JsonResult.SUCCESS;
    }
}
