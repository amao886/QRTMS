package com.ycg.ksh.api.enterprise.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.constant.CompanyConfigType;
import com.ycg.ksh.constant.CompanyFettle;
import com.ycg.ksh.constant.LegalizeFettle;
import com.ycg.ksh.constant.MenuType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.enterprise.CompanyConfig;
import com.ycg.ksh.entity.service.AuthorityMenu;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.enterprise.CustomerSearch;
import com.ycg.ksh.entity.service.enterprise.EmployeeAlliance;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.util.AuthorityUtil;
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
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 公司企业相关控制器
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */
@Controller("enterprise.company.controller")
@RequestMapping("/enterprise/company")
public class CompanyController extends BaseController {

    @Resource
    protected CompanyService companyService;
    @Resource
    protected CustomerService customerService;
    @Resource
    protected UserService userService;
    @Resource
    protected SupportService supportService;
    @Resource
    protected PermissionService permissionService;

    @RequestMapping(value = "/view/{viewkey}")
    public String manage(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        User user = loadUser(request);
        Company company = companyService.getCompanyByUserKey(user.getId());
        if (company != null) {
            CompanyBankVerify companyBankVerify = companyService.queryByCompanyKey(user.getId(), company.getId());
            if (companyBankVerify != null) {
                model.addAttribute("companyBankVerify", companyBankVerify);
            }
            model.addAttribute("company", company);
        }
        model.addAttribute("user", user);
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        if ("legalize".equalsIgnoreCase(viewkey)) {
            model.addAttribute("legalize", userService.getUserLegalize(user.getId()));
        }
        return "/enterprise/company/" + viewkey;
    }

    /**
     * 跳转开通授权
     *
     * @param personalId
     * @return
     * @author wangke
     * @date 2018/6/27 17:37
     */
    @RequestMapping(value = "/authorize/{personalId}")
    @ResponseBody
    public JsonResult authorized(@PathVariable Integer personalId, Model model, HttpServletRequest request) {
        RequestObject body = new RequestObject(request.getParameterMap());
        JsonResult jsonResult = new JsonResult();
        Company company = companyService.assertCompanyByUserKey(personalId);
        jsonResult.put("legalize", userService.getUserLegalize(personalId));
        jsonResult.put("company", company);
        jsonResult.put("companySeals", companyService.listCompanySeal(company.getId(), loadUserKey(request), null));

        Collection<CompanySeal> authorizeSeals = companyService.getCompanySeal(personalId);
        if (CollectionUtils.isNotEmpty(authorizeSeals)) {
            //已经授权了的公章
            jsonResult.put("selects", authorizeSeals.parallelStream().map(s -> s.getId()).collect(Collectors.toList()));
        }
        jsonResult.put("imagePath", SystemUtils.staticPathPrefix());
        return jsonResult;
    }

    /**
     * 企业认证
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/verify/manage")
    @ResponseBody
    public JsonResult companyVerify(HttpServletRequest request) throws Exception {
        AuthorizeUser user = loadUser(request);
        JsonResult jsonResult = new JsonResult();
        Integer fettle = 0;
        Company company = companyService.assertCompanyByUserKey(user.getId());
        UserLegalize legalize = userService.getUserLegalize(user.getId());
        if (legalize == null || !LegalizeFettle.convert(legalize.getFettle()).isCreate()) {
            fettle = 1;
            jsonResult.put("legalize", legalize);
        } else {
            CompanyFettle companyFettle = CompanyFettle.convert(company.getFettle());
            if (companyFettle.isRegistered()) {
                fettle = 2;
            }
            if (companyFettle.isBase()) {
                jsonResult.put("bankVerify", companyService.queryByCompanyKey(user.getId(), company.getId()));
                jsonResult.put("brankData", supportService.loadBrankData());
                fettle = 3;
            }
        }
        jsonResult.put("company", company);
        jsonResult.put("fettle", fettle);
        return jsonResult;
    }


    /**
     * 员工管理 - 员工管理列表查询
     *
     * @param request
     * @return
     * @author wangke
     * @date 2018/5/4 15:01
     */
    @RequestMapping(value = "/employee/list")
    @ResponseBody
    public JsonResult employeeList(@RequestBody(required = false) RequestObject body, HttpServletRequest request) throws Exception {
        if (body == null) {
            body = RequestObject.EMPTY;
        }
        AuthorizeUser user = loadUser(request);
        JsonResult jsonResult = new JsonResult();
        Company company = companyService.getCompanyByUserKey(user.getId());
        if (null != company) {
            jsonResult.put("company", company);
            jsonResult.put("employees", companyService.queryEmployee(user.getId(), company.getId(), body.get("likeString")));
        }
        return jsonResult;
    }

    /**
     * 员工管理 - 员工管理列表查询
     *
     * @param request
     * @return
     * @author wangke
     * @date 2018/5/4 15:01
     */
    @RequestMapping(value = "/authority/list")
    @ResponseBody
    public JsonResult authorityList(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("authorities", permissionService.loadAuthoritys(loadUserKey(request), MenuType.NORMAL));
        return jsonResult;
    }

    /**
     * 员工管理 - 修改员工权限
     *
     * @param requestObject
     * @param request
     * @return
     * @author wangke
     */
    @RequestMapping(value = "/employee/authority/modify")
    @ResponseBody
    public JsonResult modifyEemployeeAuthority(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        User u = loadUser(request);
        Integer employeeId = requestObject.getInteger("employeeId");
        Assert.notBlank(employeeId, "员工编号不能为空");
        Collection<Integer> authorities = StringUtils.integerCollection(requestObject.get("authorityKeys"));
        companyService.modifyEmployeeAuthoritys(u.getId(), employeeId, authorities);
        return JsonResult.SUCCESS;
    }

    /**
     * 员工管理 - 员工管理列表查询
     *
     * @param request
     * @return
     * @author wangke
     * @date 2018/5/4 15:01
     */
    @RequestMapping(value = "/employee/manage")
    @ResponseBody
    public JsonResult employeeManage(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        AuthorizeUser user = loadUser(request);
        JsonResult jsonResult = new JsonResult();
        Company company = companyService.getCompanyByUserKey(user.getId());
        if (null != company) {
            Collection<EmployeeAlliance> companyEmployees = companyService.queryEmployee(user.getId(), company.getId(), body.get("likeString"));
            if (CollectionUtils.isNotEmpty(companyEmployees)) {
                jsonResult.put("employees", companyEmployees);
                jsonResult.put("count", companyEmployees.size());
            }
            jsonResult.put("company", company);

            if (body.getBoolean("seals")) {
                jsonResult.put("seals", companyService.listCompanySeal(company.getId(), user.getId(), null));
            }
            if (body.getBoolean("customers")) {
                jsonResult.put("customers", customerService.searchCustomerBySomething(new CustomerSearch(user.getId(), company.getId())));
            }
            if (body.getBoolean("authoritys")) {
                jsonResult.put("authorities", permissionService.loadAuthoritys(loadUserKey(request), MenuType.NORMAL));
            }
        }
        return jsonResult;
    }

    public EmployeeAlliance firstEmployeeAlliance(Collection<EmployeeAlliance> companyEmployees, User user) {
        EmployeeAlliance employee = null;
        for (Iterator<EmployeeAlliance> iterator = companyEmployees.iterator(); iterator.hasNext(); ) {
            if (employee == null) {
                employee = iterator.next();
                break;
            }
        }
        return employee;
    }

    /**
     * 员工管理 - 员工详情查询
     *
     * @param employeeId
     * @return
     * @author wangke
     * @date 2018/5/4 15:01
     */
    @RequestMapping(value = "/employee/detail/{employeeId}")
    @ResponseBody
    public JsonResult employeeDetail(@PathVariable Integer employeeId, @RequestBody(required = false) RequestObject object, HttpServletRequest request) {
        Assert.notBlank(employeeId, "员工ID为空");
        JsonResult jsonResult = new JsonResult();

        AuthorizeUser user = RequestUitl.getUserInfo(request);

        Assert.notNull(user, "登陆超时");

        EmployeeAlliance alliance = companyService.getEmployeeAlliance(employeeId);
        if (user.getId() - alliance.getEmployeeId() != 0){
            alliance.setIdCardNo(UserUtil.encodeIdNo(alliance.getIdCardNo()));
            alliance.setMobilephone(UserUtil.encodeMobile(alliance.getMobilephone()));
            alliance.setBrankCardNo(UserUtil.encodeMobile(alliance.getBrankCardNo()));
        }
        Collection<AuthorityMenu> authorities = permissionService.listAuthoritys(alliance.getCompanyId(), alliance.getEmployeeId(), user.getIdentityKey());
        if (CollectionUtils.isNotEmpty(authorities)) {
            //Collection<Integer> authoritys = AuthorityUtil.reduceAuthority(authorities, AuthorityMenu::getId);
            jsonResult.put("authoritys", AuthorityUtil.reduceAuthority(authorities, AuthorityMenu::getId));
        }
        jsonResult.put("seals", companyService.listCompanySealKeys(alliance.getCompanyId(), alliance.getEmployeeId()));
        jsonResult.put("customers", customerService.listCustomerAuthoritys(alliance.getEmployeeId()));
        if (RequestUitl.isMobileDevice(request)) {
            jsonResult.put("baseAuthoritys", permissionService.loadAuthoritys(loadUserKey(request), MenuType.NORMAL));
        }
        jsonResult.put("employee", alliance);
        return jsonResult;
    }

    /**
     * 员工管理 - 员工印章详情查询
     *
     * @param employeeId
     * @return
     * @author wangke
     * @date 2018/5/4 15:01
     */
    @RequestMapping(value = "/employee/seal/list/{employeeId}")
    @ResponseBody
    public JsonResult employeeSealList(@PathVariable Integer employeeId, HttpServletRequest request) {
        Assert.notBlank(employeeId, "员工ID为空");
        //AuthorizeUser user = loadUser(request);
        JsonResult jsonResult = new JsonResult();
        //CompanyEmployeeAlliance alliance = companyService.getEmployeeAlliance(employeeId);
        jsonResult.put("imagePath", SystemUtils.staticPathPrefix());
        jsonResult.put("seals", userService.listPersonalSeal(employeeId));
        return jsonResult;
    }

    /**
     * 员工管理 - 员工详情查询
     *
     * @param employeeId
     * @return
     * @author wangke
     * @date 2018/5/4 15:01
     */
    @RequestMapping(value = "/employee/seal/{employeeId}")
    @ResponseBody
    public JsonResult employeeSeal(@PathVariable Integer employeeId, HttpServletRequest request) {
        Assert.notBlank(employeeId, "员工ID为空");
        //AuthorizeUser user = loadUser(request);
        JsonResult jsonResult = new JsonResult();
        //CompanyEmployeeAlliance alliance = companyService.getEmployeeAlliance(employeeId);
        jsonResult.put("imagePath", SystemUtils.staticPathPrefix());
        jsonResult.put("employee", companyService.getCompanyEmployee(employeeId));
        jsonResult.put("user", userService.getConciseUser(employeeId));
        jsonResult.put("legalize", userService.getUserLegalize(employeeId));
        jsonResult.put("seals", userService.listPersonalSeal(employeeId));
        return jsonResult;
    }

    /**
     * 员工管理 - 员工信息修改
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/6/5 16:36
     */
    @RequestMapping(value = "/employee/edit")
    @ResponseBody
    public JsonResult editEmployee(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        Assert.notNull(body, Constant.PARAMS_ERROR);
        User u = loadUser(request);
        CompanyEmployee employee = body.toJavaBean(CompanyEmployee.class);
        Assert.notBlank(employee.getEmployeeId(), "员工编号不能为空");
        Collection<Integer> authorities = StringUtils.integerCollection(body.get("authorityKeys"));
        Collection<Long> customerKeys = StringUtils.longCollection(body.get("customerKeys"));
        Collection<Long> sealKeys = StringUtils.longCollection(body.get("sealKeys"));
        companyService.modifyEmployee(u.getId(), employee, authorities, customerKeys, sealKeys);
        return JsonResult.SUCCESS;
    }


    /**
     * 员工管理 - 添加企业人员
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     */
    @RequestMapping(value = "/employee/add")
    @ResponseBody
    public JsonResult addPersonnel(@RequestBody RequestObject body, HttpServletRequest request) {
        User user = loadUser(request);
        String employeeName = body.get("employeeName");
        Assert.notBlank(employeeName, "员工姓名不能为空");
        String mobile = body.get("mobile");
        Assert.notBlank(mobile, "手机号不能为空");
        Collection<Integer> authorities = StringUtils.integerCollection(body.get("authoritys"));
        //Assert.notEmpty(authorities, "至少保留一条权限菜单");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("employeeKey", companyService.addPersonnel(user.getId(), employeeName, mobile, authorities));
        return jsonResult;
    }


    /**
     * 企业管理-删除企业人员
     *
     * @param employeeId
     * @param request
     * @return
     * @author wangke
     */
    @RequestMapping(value = "/employee/delete/{employeeId}")
    @ResponseBody
    public JsonResult delPersonnel(@PathVariable Integer employeeId, HttpServletRequest request) {
        companyService.deletePersonnel(loadUserKey(request), employeeId);
        return JsonResult.SUCCESS;
    }


    /**
     * 企业个人 - 签章授权
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     * @author wangke
     */
    @RequestMapping(value = "/signature/auth")
    @ResponseBody
    public JsonResult signatureAuth(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        Assert.notNull(body, Constant.PARAMS_ERROR);
        Collection<Long> seals = StringUtils.longCollection(body.get("seals"));
        Assert.notEmpty(seals, "至少选中一个印章");
        Integer employeeKey = body.getInteger("employeeKey");
        Assert.notBlank(employeeKey, "员工编号不能为空");
        companyService.signatureAuth(loadUserKey(request), employeeKey, seals);
        return JsonResult.SUCCESS;
    }

    /**
     * 取消授权
     *
     * @param body
     * @return
     * @author wangke
     * @date 2018/5/7 16:01
     */
    @RequestMapping(value = "/signature/unauth")
    @ResponseBody
    public JsonResult uSignatureAuth(@RequestBody RequestObject body, HttpServletRequest request) {
        Assert.notNull(body, Constant.PARAMS_ERROR);
        Integer employeeKey = body.getInteger("employeeKey");
        Assert.notBlank(employeeKey, "员工编号不能为空");
        Collection<Long> seals = StringUtils.longCollection(body.get("seals"));
        Assert.notEmpty(seals, "至少选中一个要取消的印章");
        companyService.unsignatureAuth(loadUserKey(request), employeeKey, seals);
        return JsonResult.SUCCESS;
    }

    /**
     * 个人实名认证
     *
     * @param body
     * @param request
     * @return
     * @throws ReflectiveOperationException
     * @author wangke
     */
    @RequestMapping(value = "personalAuth")
    @ResponseBody
    public JsonResult personalAuth(@RequestBody RequestObject body, HttpServletRequest request) throws ReflectiveOperationException {
        Assert.notNull(body, "认证信息为空");
        Assert.notBlank(body.get("code"), "验证码为空");
        UserLegalize userLegalize = body.toJavaBean(UserLegalize.class);
        Assert.notBlank(userLegalize.getMobilePhone(), "手机号不能为空");
        Assert.notBlank(userLegalize.getBrankCardNo(), "银行卡号不能为空");
        String code = body.get("code");

        validateSmsCode(userLegalize.getMobilePhone(), code);

        String name = body.get("employeeName");
        if (StringUtils.isBlank(userLegalize.getName()) && StringUtils.isNotBlank(name)) {
            userLegalize.setName(name);
        }
        userLegalize.setId(loadUserKey(request));
        userService.bindPersonalAuth(userLegalize);
        clearSmsCode(userLegalize.getMobilePhone());
        return JsonResult.SUCCESS;
    }

    /**
     * 个人认证信息校验
     * TODO Add description
     * <p>
     *
     * @param body
     * @param request
     * @return
     * @throws ReflectiveOperationException
     */
    @RequestMapping(value = "userLegalize/check")
    @ResponseBody
    public JsonResult userLegalizeCheck(@RequestBody RequestObject body, HttpServletRequest request) throws ReflectiveOperationException {
        Assert.notNull(body, "认证信息为空");
        Assert.notBlank(body.get("code"), "验证码为空");
        UserLegalize userLegalize = body.toJavaBean(UserLegalize.class);
        Assert.notBlank(userLegalize.getId(), "用户编号不能为空");
        if (userService.getUserLegalize(userLegalize.getId()) == null) {
            throw new ParameterException("认证信息不存在");
        }
        Assert.notBlank(userLegalize.getMobilePhone(), "手机号不能为空");
        String code = body.get("code");

        validateSmsCode(userLegalize.getMobilePhone(), code);


        clearSmsCode(userLegalize.getMobilePhone());
        return JsonResult.SUCCESS;
    }

    /**
     * 个人签章样式选择
     *
     * @param body
     * @param request
     * @return
     * @author wangke
     * @date 2018/6/19 13:57
     */
    @RequestMapping(value = "/userLegalize/sealType")
    @ResponseBody
    public JsonResult userSealType(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        User user = loadUser(request);
        PersonalSeal personalSeal = body.toJavaBean(PersonalSeal.class);
        Assert.notBlank(personalSeal.getSealStyle(), "印章样式不能为空");
        personalSeal.setUserId(user.getId());
        userService.bindSealType(personalSeal);
        return JsonResult.SUCCESS;
    }


    /**
     * 添加公司
     *
     * @param request
     * @return
     * @throws Exception
     * @author wangke
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public JsonResult addCompany(@RequestBody RequestObject params, HttpServletRequest request) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("-----------------addCompany----------------->{}", params);
        }
        Assert.notEmpty(params, Constant.PARAMS_ERROR);
        User user = RequestUitl.getUserInfo(request);
        params.put("userId", user.getId());
        Company company = params.toJavaBean(Company.class);
        //company.setLicensePath(Directory.LICENSE.sub(file.getName()));
        companyService.save(user.getId(), company, params.getLong("customerKey"));
        return JsonResult.SUCCESS;
    }

    /**
     * TODO 根据用户编号获取所属公司信息
     * <p>
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getCompanyByUserKey")
    @ResponseBody
    public JsonResult getCompanyByUserKey(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = loadUser(request);
        Company company = companyService.getCompanyByUserKey(user.getId());
        jsonResult.put("results", company);
        return jsonResult;
    }

    /**
     * TODO 企业对公银行信息添加
     * <p>
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add/company/bank")
    @ResponseBody
    public JsonResult addCompanyBank(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, Constant.PARAMS_ERROR);
        if (logger.isDebugEnabled()) {
            logger.debug("addCompanyBank()===>: param: {}", object);
        }
        CompanyBankVerify companyBankVerify = object.toJavaBean(CompanyBankVerify.class);
        companyService.saveCompanyBankVerify(loadUserKey(request), companyBankVerify);
        return JsonResult.SUCCESS;
    }

    /**
     * TODO 企业信息
     * <p>
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query/companyBank")
    @ResponseBody
    public JsonResult queryCompanyBank(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = loadUser(request);
        jsonResult.put("results", companyService.queryCompanyBank(user.getId()));
        return jsonResult;
    }

    /**
     * TODO 打款金额校验接口
     * <p>
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check/amount")
    @ResponseBody
    public JsonResult checkAmount(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, Constant.PARAMS_ERROR);
        if (logger.isDebugEnabled()) {
            logger.debug("checkAmount======>param:{}", object);
        }
        JsonResult jsonResult = new JsonResult();
        String errorMessage = companyService.verifyCash(loadUserKey(request), object.getDouble("checkAmount"));
        if (StringUtils.isNotBlank(errorMessage)) {
            jsonResult.modify(false, errorMessage + ", 需要重新发起打款认证");
        }
        return jsonResult;
    }

    /**
     * TODO 根据公司编号查询对公银行校验信息
     * <p>
     *
     * @param companyKey
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query/companyKey/{companyKey}")
    @ResponseBody
    public JsonResult queryByCompanyKey(@PathVariable Long companyKey, HttpServletRequest request) throws Exception {
        User user = loadUser(request);
        if (logger.isDebugEnabled()) {
            logger.debug("queryByCompanyKey======>companyKey:{}", companyKey);
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", companyService.queryByCompanyKey(user.getId(), companyKey));
        return jsonResult;
    }


    /**
     * 保存企业签章
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/6/12 9:46
     */
    @RequestMapping(value = "/save/seal")
    @ResponseBody
    public JsonResult saveSeal(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Assert.notNull(object, Constant.PARAMS_ERROR);
        User user = loadUser(request);
        CompanySeal companySeal = object.toJavaBean(CompanySeal.class);
        companySeal.setUserId(user.getId());
        companyService.saveCompanySeal(companySeal);
        return JsonResult.SUCCESS;
    }

    /**
     * 公司印章删除
     *
     * @return
     * @author wangke
     * @date 2018/6/12 16:06
     */
    @RequestMapping(value = "/del/seal/{sealKey}")
    @ResponseBody
    public JsonResult deleteSeal(@PathVariable Long sealKey, HttpServletRequest request) {
        Assert.notBlank(sealKey, "要删除的印章编号不能为空");
        companyService.deleteCompanySeal(loadUserKey(request), sealKey);
        return JsonResult.SUCCESS;
    }

    /**
     * 查询企业签章
     *
     * @param object
     * @return
     * @author wangke
     * @date 2018/6/12 9:46
     */
    @RequestMapping(value = "/company/seals")
    @ResponseBody
    public JsonResult listCompanySeal(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = loadUser(request);
        Company company = companyService.getCompanyByUserKey(user.getId());
        if (null == company) {
            throw new BusinessException("当前用户尚未加入公司");
        }
        jsonResult.put("company", company);
        jsonResult.put("companySeals", companyService.listCompanySeal(company.getId(), user.getId(), object.get("companyName")));
        jsonResult.put("imagePath", SystemUtils.staticPathPrefix());
        return jsonResult;
    }

    /**
     * 查询个人签章
     *
     * @param object
     * @return
     * @author wangke
     * @date 2018/6/12 9:46
     */
    @RequestMapping(value = "/personal/seals")
    @ResponseBody
    public JsonResult listPersonalSeal(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = loadUser(request);
        Company company = companyService.assertCompanyByUserKey(user.getId());
        jsonResult.put("company", company);
        jsonResult.put("employees", companyService.queryEmployee(user.getId(), company.getId(), object.get("employeeName")));
        jsonResult.put("imagePath", SystemUtils.staticPathPrefix());
        return jsonResult;
    }

    /**
     * 修改企业名称
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modify/name")
    @ResponseBody
    public JsonResult modifyCompanyName(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        companyService.modifyName(loadUserKey(request), object.get("companyName"));
        return JsonResult.SUCCESS;
    }

    /**
     * 功能描述: 更新企业配置
     *
     * @param requestObject
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/17 13:38
     */
    @RequestMapping(value = "/modify/config")
    @ResponseBody
    public JsonResult modifyConfig(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        String configString = requestObject.get("configs");
        if(StringUtils.isNotBlank(configString)){
            companyService.modifyConfig(loadUserKey(request), Globallys.toJavaObjects(configString, CompanyConfig.class));
        }
        return JsonResult.SUCCESS;
    }
    /**
     * 功能描述: 发货通知配置
     *
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/17 13:38
     */
    @RequestMapping(value = "/list/config")
    @ResponseBody
    public JsonResult listConfig(HttpServletRequest request) {
        JsonResult result = new JsonResult();
        Collection<CompanyConfig> companyConfigs = companyService.listConfig(loadUserKey(request));
        if(companyConfigs != null && !companyConfigs.isEmpty()){
            result.put("configs", companyConfigs.stream().collect(Collectors.toMap(CompanyConfig::getConfigKey, CompanyConfig::getConfigValue)));
        }else{
            result.put("configs", Stream.of(CompanyConfigType.values()).collect(Collectors.toMap(CompanyConfigType::getCode, CompanyConfigType::getDefaultValue)));
        }
        return result;
    }
}
