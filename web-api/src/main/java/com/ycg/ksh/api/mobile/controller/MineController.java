package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.AssociateUser;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.MergeProjectGroup;
import com.ycg.ksh.entity.service.MergeProjectGroupMember;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.ProjectGroupService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * 我的资源模块功能
 * @Author yangc
 * */
@Controller("mobile.mine.controller")
@RequestMapping("/mobile/mine")
public class MineController extends BaseController {

    @Resource
    ProjectGroupService groupService;

    @Resource
    SmsService smsService;

    @Resource
    CompanyService companyService;

    /**
     * 获取当前登录用户的拥有设置信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/commonly")
    @ResponseBody
    public JsonResult listCommonly(HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        UserCommonly commonly = userService.loadUserCommonlyByUserKey(loadUserKey(request));
        if(commonly != null){
            jsonResult.put("commonlies", commonly.commonlies(false));
        }
        return jsonResult;
    }

    /**
     * 获取当前登录用户的拥有设置信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/commonly/modify")
    @ResponseBody
    public JsonResult modifyCommonly(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        String codeString = object.get("code");
        Assert.notNull(codeString, "至少选择一个功能");
        User user = RequestUitl.getUserInfo(request);
        userService.modifyCommonlyByUserKey(user.getId(), StringUtils.integerCollection(codeString));
        return JsonResult.SUCCESS;
    }

    // 发送短信
    @RequestMapping(value = "/smsCode/{phone}")
    @ResponseBody
    public JsonResult smsCode(@PathVariable String phone, HttpServletRequest request) throws Exception {
        logger.info("-----------smsCode------------phone: {} ", phone);
        if (StringUtils.isBlank(phone)) {
            throw new ParameterException(phone, "手机号不能为空");
        }
        smsService.sendSmsCode(phone);
        return new JsonResult(true, "验证码发送成功");
    }

    /**
     * 绑定手机号
     * <p>
     * @param object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bindphone")
    @ResponseBody
    public JsonResult bindPhone(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 用户注册和绑定手机号特殊处理
        logger.debug("bindphone -> {}", object);
        String phone = object.get("phone"), name = object.get("name"), code = object.get("code");

        validateSmsCode(phone, code);

        AuthorizeUser user = userService.bindMobile(loadUserKey(request), phone, name, object.getInteger("identity"));
        if (user != null) {
            RequestUitl.modifyUserInfo(request, user);
        }
        clearSmsCode(phone);
        return new JsonResult(true, "手机号绑定成功");
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
    @RequestMapping(value = "/user/info")
    @ResponseBody
    public JsonResult resMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        AuthorizeUser userInfo = RequestUitl.getUserInfo(request);
        if (userInfo.getCompanyName() == null) {
            Company company = companyService.getCompanyByUserKey(userInfo.getId());
            if(company!= null)
                userInfo.setCompanyName(company.getCompanyName());
        }
        if (userInfo.getFettle() == null) {
            UserLegalize legalize = userService.getUserLegalize(userInfo.getId());
            if(legalize!= null)
                userInfo.setFettle(legalize.getFettle());
            else
                userInfo.setFettle(0);
        }
        jsonResult.put("user", userInfo);
        RequestUitl.modifyUserInfo(request,userInfo);
        return jsonResult;
    }

    /**
     * 编辑用户昵称
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-11 14:49:50
     * @param user
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/editName")
    @ResponseBody
    public JsonResult editName(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("----edit------ {}", user);
        AuthorizeUser authorize = RequestUitl.getUserInfo(request);
        user.setId(authorize.getId());
        Assert.notNull(user.getUname(), "昵称不能为空");
        user.setUname(UserUtil.encodeName(user.getUname()));
        authorize = userService.update(user);
        if (authorize != null) {
            RequestUitl.modifyUserInfo(request, authorize);
        }
        return new JsonResult(true, "昵称修改成功");
    }

    /**
     * 编辑用户手机号
     * <p>
     * @param object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/editPhone")
    @ResponseBody
    public JsonResult editPhone(@RequestBody RequestObject object, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("editPhone -> {}", object);
        String phone = object.get("phone"), code = object.get("code");

        if (StringUtils.isBlank(phone)) {
            throw new ParameterException(phone, "手机号不能为空");
        }

        validateSmsCode(phone, code);

        AuthorizeUser authorize = RequestUitl.getUserInfo(request);

        User user = new User();
        user.setId(authorize.getId());
        user.setMobilephone(phone);
        authorize = userService.update(user);
        if (authorize != null) {
            RequestUitl.modifyUserInfo(request, authorize);
        }
        clearSmsCode(phone);
        return new JsonResult(true, "手机号修改成功");
    }

    @RequestMapping(value = "/group/list/contactNumber")
    @ResponseBody
    public JsonResult listGroupByContactNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        if(StringUtils.isNotBlank(user.getMobilephone())) {
            jsonResult.put("groups", groupService.listByContactNumber(user.getMobilephone()));
        }else {
            jsonResult.put("groups", Collections.emptyList());
        }
        return jsonResult;
    }


    /**
     * 获取当前登陆用户的项目组信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-11 14:49:50
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/group/list")
    @ResponseBody
    public JsonResult listGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        List<ProjectGroup> groups = groupService.listByUserKey(user.getId());
        if(CollectionUtils.isEmpty(groups)) {
            jsonResult.put("groups", Collections.emptyList());
        }else {
            jsonResult.put("groups", groups);
        }
        return jsonResult;
    }

    @RequestMapping(value = "/group/list/detail")
    @ResponseBody
    public JsonResult listDetailGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        User user = RequestUitl.getUserInfo(request);
        List<MergeProjectGroup> groups = groupService.listMergeByUserKey(user.getId());
        if(CollectionUtils.isEmpty(groups)) {
            jsonResult.put("groups", Collections.emptyList());
        }else {
            jsonResult.put("groups", groups);
        }
        return jsonResult;
    }

    /**
     * 查看指定项目详细信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-11 14:49:50
     * @param groupId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/group/detail/{groupId}")
    @ResponseBody
    public JsonResult detailGroup(@PathVariable Integer groupId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Assert.notNull(groupId, "groupId不能为空");
        User u = RequestUitl.getUserInfo(request);
        JsonResult jsonResult = new JsonResult();
        MergeProjectGroup projectGroup = groupService.getDetailByGroupKey(groupId);
        if(projectGroup != null) {
            jsonResult.put("results", projectGroup);
        }
        jsonResult.put("userid", u.getId());
        return jsonResult;
    }

    // 删除资源组成员
    @RequestMapping(value = "/delete/groupUser/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deleteGroupUser(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("deleteGroupUser -> {}", id);
        Assert.notNull(id, Constant.PARAMS_ERROR);
        User u = RequestUitl.getUserInfo(request);
        groupService.deleteMember(u.getId(), id);
        return JsonResult.SUCCESS;
    }


    //我的资源组添加
    @RequestMapping(value = "/addGroup", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addResGroup(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        logger.info("addResGroup -> {}", object);
        User u = RequestUitl.getUserInfo(request);
        ProjectGroup projectGroup = object.toJavaBean(ProjectGroup.class);
        projectGroup.setUserid(u.getId());
        Assert.notBlank(projectGroup.getGroupName(), "项目组名称不能为空");
        groupService.save(projectGroup);
        return JsonResult.SUCCESS;
    }

    // 编辑资源组名称
    @RequestMapping(value = "/edit/groupName", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult editResGroupName(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        User u = RequestUitl.getUserInfo(request);
        ProjectGroup projectGroup = new ProjectGroup();
        projectGroup.setId(object.getInteger("id"));
        projectGroup.setGroupName(object.get("groupName"));
        Assert.notBlank(projectGroup.getGroupName(),"群组名称不能为空");
        Assert.notBlank(projectGroup.getId(),"群组Id不能为空");
        groupService.update(u.getId(), projectGroup);
        return JsonResult.SUCCESS;
    }

    /**
     * 获取所有角色及对应权限列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/group/queryGrantRole")
    @ResponseBody
    public JsonResult queryGroupUserRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取所有角色以及角色对应的权限信息
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", groupService.listMergeProjectGroupRoles());
        return jsonResult;
    }

    //查看组员详细信息和对应的角色
    @RequestMapping(value = "/userRole/detail/{userId}/{groupId}")
    @ResponseBody
    public JsonResult groupUserDetail(@PathVariable Integer userId, @PathVariable Integer groupId,HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Assert.notNull(userId, "userid不能为空");
        Assert.notNull(groupId, "groupid不能为空");
        JsonResult jsonResult = new JsonResult();
        MergeProjectGroupMember member = groupService.getMergeGroupMember(groupId, userId);
        if(member == null) {
            jsonResult.modify(false, "成员不存在");
        }else {
            AssociateUser associateUser = member.getUser();
            if(associateUser != null) {
                Map<String, Object> entity = associateUser.toMap();
                ProjectGroupRole groupRole = member.getRolePermission();
                if(groupRole != null) {
                    entity.put("roleName", groupRole.getRolename());
                    entity.put("roleCode", groupRole.getRolecode());
                    entity.put("guserid", member.getId());
                }
                jsonResult.put("results", entity);
            }
        }
        return jsonResult;
    }

    /**
     * 资源组的用户设置角色及权限
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add/userRole")
    @ResponseBody
    public JsonResult addUserRole(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        UserRole4Group userRole = object.toJavaBean(UserRole4Group.class);
        Assert.notNull(userRole.getUserid(), "userid不能为空");
        Assert.notNull(userRole.getGroupid(), "groupid不能为空");
        Assert.notNull(userRole.getRoleid(), "roleid不能为空");
        User u = RequestUitl.getUserInfo(request);
        groupService.modifyProjectGroupRole(u.getId(), userRole);
        return JsonResult.SUCCESS;
    }

    /**
     * 组长变更
     * @param groupId 组id
     * @param userId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/group/change/{groupId}/{userId}")
    @ResponseBody
    public JsonResult updateGroupChange(@PathVariable Integer groupId, @PathVariable Integer userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Assert.notNull(groupId, "groupId不能为空");
        Assert.notNull(userId, "userId不能为空");
        User u = RequestUitl.getUserInfo(request);
        //InterfaceHepler.requestObject("{\"id\":"+groupId+",\"cuserid\":"+u.getId()+",\"userid\":"+userId+"}", InterfaceHepler.URL_RESOURCE_GROUP_UPDATE);
        groupService.modifyProjectGroupLeader(u.getId(), groupId, userId);
        return JsonResult.SUCCESS;
    }
}
