package com.ycg.ksh.api.admin.controller;

import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.MenuType;
import com.ycg.ksh.core.driver.application.DriverApplicationService;
import com.ycg.ksh.core.driver.application.dto.DriverDto;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.user.UserResource;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.EmployeeAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.service.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Driver;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

/**
 * 总后台用户控制器
 *
 * @author wangke
 * @create 2018-03-12 10:29
 **/
@Controller("admin.user.controller")
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    @Resource
    ManagingUsersService managingUsersService;

    @Resource
    UserService userService;
    @Resource
    PermissionService permissionService;

    @Resource
    WeChatApiService apiService;
    @Resource
    OrderService orderService;
    @Resource
    DriverService driverService;
    @Resource
    CompanyService companyService;
    @Resource
    DriverApplicationService driverApplicationService;
    /**
     * 获取用户的所有权限
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/authoritys")
    @ResponseBody
    public JsonResult listAuthority(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        AuthorizeUser user = RequestUitl.getUserInfo(request);
        UserCommonly commonly = userService.loadUserCommonlyByUserKey(user.getId());
        if(commonly != null && !commonly.isDriver()){
            logger.info("获取用户权限, 当前身份为 {}", commonly.getIdentityKey());
            user.setAuthorityKeys(permissionService.loadAuthorityCodes(user.getId(), MenuType.NORMAL));
            RequestUitl.modifyUserInfo(request, user);
            jsonResult.put("authoritys", user.getAuthorityKeys());
        }else{
            jsonResult.put("authoritys", Collections.emptyList());
        }
        return jsonResult;
    }
    /**
     * 获取当前登录用户的拥有设置信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/commonly")
    @ResponseBody
    public JsonResult listCommonly(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        AuthorizeUser user = RequestUitl.getUserInfo(request);
        UserCommonly commonly = userService.loadUserCommonlyByUserKey(user.getId());
        if(commonly != null){
            Collection<String> authorityCodes = permissionService.loadAuthorityCodes(user.getId(), MenuType.NORMAL);
            Collection<String> commonlies = commonly.commonlies(true);
            //非司机需要根据分配的权限过滤
            if(!commonly.isDriver()){
                commonlies.removeIf(c -> !authorityCodes.contains(c));
            }
            jsonResult.put("commonlies", commonlies);
            jsonResult.put("identity", commonly.getIdentityKey());
            OrderSearch search = new OrderSearch();
            search.setUserKey(user.getId());
            search.setTime(new Date(), -3);
            if(commonly.isDriver()){//司机
                jsonResult.put("orders", driverService.listOrderDeliver(search));
            }else{
                if(CoreConstants.USER_CATEGORY_SHIPPER - commonly.getIdentityKey() == 0){
                    search.setPartnerType(PartnerType.SHIPPER);
                }

                if(CoreConstants.USER_CATEGORY_RECEIVE - commonly.getIdentityKey() == 0){
                    search.setPartnerType(PartnerType.RECEIVE);
                }

                if(CoreConstants.USER_CATEGORY_CONVEY - commonly.getIdentityKey() == 0){
                    search.setPartnerType(PartnerType.CONVEY);
                }
                search.setAll(false);
                jsonResult.put("orders", orderService.listOrderAlliance(user.getId(), search));
            }
        }
        return jsonResult;
    }

    /**
     * 获取当前登录用户的拥有设置信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/commonly/modify")
    @ResponseBody
    public JsonResult modifyCommonly(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        String codeString = object.get("code");
        Assert.notNull(codeString, "至少选择一个功能");
        User user = RequestUitl.getUserInfo(request);
        userService.modifyCommonlyKeys(user.getId(), StringUtils.integerCollection(codeString));
        return JsonResult.SUCCESS;
    }

    /**
     * 获取当前登录用户的拥有设置信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/identity/change")
    @ResponseBody
    public JsonResult changeIdentity(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Integer identityKey = object.getInteger("identityKey");
        Assert.notBlank(identityKey, "请选择要切换的身份");
        RequestUitl.modifyUserInfo(request, userService.modifyIdentity(loadUserKey(request), identityKey));
        return JsonResult.SUCCESS;
    }

    /**
     * 获取当前登录用户的基本信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-11 14:49:50
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/info")
    @ResponseBody
    public JsonResult resMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        AuthorizeUser userInfo = RequestUitl.getUserInfo(request);
        jsonResult.put("id", userInfo.getId());
        jsonResult.put("mobilephone", userInfo.getMobilephone());
        jsonResult.put("unamezn", userInfo.getUnamezn());
        jsonResult.put("headImg", userInfo.getHeadImg());
        Company company = companyService.getCompanyByUserKey(userInfo.getId());
        if(company!= null){
            //查询企业员工数量
            Collection<EmployeeAlliance> companyEmployees = companyService.queryEmployee(userInfo.getId(), company.getId(), null);
            jsonResult.put("employeeCount", companyEmployees.size());// @TODO 员工数量
            userInfo.setCompanyName(company.getCompanyName());
            jsonResult.put("companyId", company.getId());
            jsonResult.put("companyName", company.getCompanyName());
            jsonResult.put("allowModify", company.getRenameCount() == null || company.getRenameCount() <= 0);//(true/false)是否可以修改企业名称
        }else{
            jsonResult.put("companyId", StringUtils.EMPTY);
            jsonResult.put("companyName", StringUtils.EMPTY);
            jsonResult.put("allowModify", false);//(true/false)是否可以修改企业名称
        }
        CompanyEmployee employee = companyService.getCompanyEmployee(userInfo.getId());
        if (employee != null) {
            userInfo.setEmployee(employee);
            jsonResult.put("employeeName", employee.getEmployeeName());
            jsonResult.put("employeeType", employee.getEmployeeType());
            jsonResult.put("allowAdd", employee.getEmployeeType() - 1 == 0 ? true : false);// @TODO 是否可以添加员工
        } else {
            jsonResult.put("employeeType", StringUtils.EMPTY);
            jsonResult.put("allowAdd", false);
        }
        UserLegalize legalize = userService.getUserLegalize(userInfo.getId());
        if(legalize!= null){
            userInfo.setFettle(legalize.getFettle());//认证状态(0:没有认证,1:已经认证)
        }else{
            userInfo.setFettle(0);
        }
        jsonResult.put("fettle", userInfo.getFettle());
        UserCommonly commonly = userService.loadUserCommonlyByUserKey(userInfo.getId());
        if(commonly != null){
            jsonResult.put("identity", commonly.getIdentityKey());//用户身份标识(1:司机,2:非司机)
        }
        UserResource resource = userService.loadResource(userInfo.getId());
        if(resource != null){
            jsonResult.put("money", resource.getMoney());//钱
            jsonResult.put("integral", resource.getIntegral());//积分
        }else{
            jsonResult.put("money", 0);//钱
            jsonResult.put("integral", 0);//积分
        }
        jsonResult.put("registerDriver", Optional.ofNullable(driverApplicationService.getDriver(new Long(userInfo.getId()))).map(d-> Boolean.TRUE).orElse(Boolean.FALSE));
        RequestUitl.modifyUserInfo(request,userInfo);
        return jsonResult;
    }

    /**
     * 用户管理
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/12 14:24r
     */
    @RequestMapping(value = "userManagement")
    public String userManagement(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        User user = RequestUitl.getUserInfo(request);
        PageScope pageScope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        model.addAttribute("page", managingUsersService.queryUsersList(body.toJavaBean(ManagingUsers.class), pageScope));
        model.addAttribute("search", body);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.listSysRoles());
        return "/admin/user/userManagement";
    }


    /**
     * 添加用户
     *
     * @param requestObject
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/20 14:51
     */
    @RequestMapping(value = "saveManagingUsers")
    @ResponseBody
    public JsonResult saveManagingUsers(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        User user = loadUser(request);
        managingUsersService.saveUser(user.getId(), requestObject.toJavaBean(ManagingUsers.class));
        return JsonResult.SUCCESS;
    }

    /**
     * 删除用户
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     * @author wangke
     * @date 2018/3/20 17:04
     */
    @RequestMapping(value = "delManagingUsers/{id}")
    @ResponseBody
    public JsonResult delManagingUsers(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        Assert.notBlank(id, "用户ID为空");
        User user = loadUser(request);
        managingUsersService.deleteUser(user.getId(), id);
        return JsonResult.SUCCESS;
    }

    /**
     * 修改用户信息
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     * @author wangke
     * @date 2018/3/20 14:51
     */
    @RequestMapping(value = "updateUserInfo")
    @ResponseBody
    public JsonResult updateUserInfo(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        User user = loadUser(request);
        managingUsersService.updateUserInfo(user.getId(), requestObject.toJavaBean(ManagingUsers.class));
        return JsonResult.SUCCESS;
    }


    /**
     * 会员列表
     * @param model
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/20 13:45
     */
    @RequestMapping(value = "/loadMembershipList")
    public String loadMembershipList(Model model, HttpServletRequest request) {
        return "/admin/dataanalysis/members_List";
    }
}
