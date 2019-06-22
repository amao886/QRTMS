/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-03 15:28:22
 */
package com.ycg.ksh.api.admin.controller;

import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.ProjectGroupPermission;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.MergeAuthorityMenu;
import com.ycg.ksh.entity.service.RoleAndPermission;
import com.ycg.ksh.service.api.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限控制层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-03 15:28:22
 */
@Controller("admin.permission.controller")
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {
    
	@Resource
	private PermissionService permissionService;
	
    @RequestMapping(value = "/search")
    public String search(Model model, HttpServletRequest request) throws Exception {
        //RequestObject body = new RequestObject(request.getParameterMap());
        //User u = RequestUitl.getUserInfo(request);
    	//model.addAttribute("search", body);
        Collection<RoleAndPermission> list = permissionService.queryRoleAndPermission();
        model.addAttribute("list", list);
        return "/admin/permission/manage";
    }
   
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult permissionList(){
    	JsonResult result = new JsonResult();
    	Collection<ProjectGroupPermission> list = permissionService.queryAllPermission();	
    	result.put("result",list);
    	return result;
    }

    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult roleAdd(@RequestBody RoleAndPermission params, HttpServletRequest request) throws Exception{
    	Assert.notNull(params, "添加角色信息不能为空");
    	if (logger.isDebugEnabled()) {
			logger.debug("roleAdd====>  params:{}",params);
		}
    	permissionService.saveRole(params);	
    	return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/rolePermission/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult rolePermissionUpdate(@RequestBody RoleAndPermission params, HttpServletRequest request) throws Exception{
    	Assert.notNull(params, "修改角色信息不能为空");
    	if (logger.isDebugEnabled()) {
    		logger.debug("rolePermissionUpdate====>  params:{}",params);
    	}
    	permissionService.updateRolePermission(params);	
    	return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/role/del/{rolePermissionId}/{roleId}")
    @ResponseBody
    public JsonResult roleDel(@PathVariable Integer rolePermissionId, @PathVariable Integer roleId, HttpServletRequest request) throws Exception{
    	if (logger.isDebugEnabled()) {
    		logger.debug("roleDel====>  rolePermissionId:{}, roleId:{}", rolePermissionId, roleId);
    	}
    	permissionService.deleteRole(rolePermissionId, roleId);	
    	return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/rolePermission/{rolePermissionKey}")
    @ResponseBody
    public JsonResult getRolePermission(@PathVariable Integer rolePermissionKey, HttpServletRequest request) throws Exception{
    	JsonResult jsonResult = new JsonResult();
    	RoleAndPermission roleAndPermission = permissionService.queryByRolePermissionKey(rolePermissionKey);
    	Collection<ProjectGroupPermission> list = permissionService.queryAllPermission();
    	if (logger.isDebugEnabled()) {
    		logger.debug("getPermission====>  rolePermissionKey:{}, roleAndPermission:{}", rolePermissionKey , roleAndPermission);
    	}
    	Map<Integer,String> map =new  HashMap<Integer,String>(roleAndPermission.getProjectGroupPermissions().size());
    	for (ProjectGroupPermission projectGroupPermission : roleAndPermission.getProjectGroupPermissions()) {
    		map.put(projectGroupPermission.getId(), projectGroupPermission.getPermissioncode());
    	}
    	jsonResult.put("permissions", list);
    	jsonResult.put("result", roleAndPermission);
    	jsonResult.put("checkPermission", map);
    	return jsonResult;
    }
    
    @RequestMapping(value = "/goto/permission")
    public String gotoPermission(Model model, HttpServletRequest request) throws Exception {
    	Collection<ProjectGroupPermission> list = permissionService.queryAllPermission();
    	model.addAttribute("list", list);
    	return "/admin/permission/permission";
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult permissionAdd(@RequestBody RequestObject params, HttpServletRequest request) throws Exception{
    	if (logger.isDebugEnabled()) {
			logger.debug("permissionAdd====>  params:{}", params);
		}
    	ProjectGroupPermission groupPermission = params.toJavaBean(ProjectGroupPermission.class);
    	permissionService.savePermission(groupPermission);	
    	return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/delete/{permissionId}")
    @ResponseBody
    public JsonResult delPermission(@PathVariable Integer permissionId, HttpServletRequest request) throws Exception{
    	if (logger.isDebugEnabled()) {
    		logger.debug("roleDel====>  permissionId:{}", permissionId);
    	}
    	permissionService.deletePermission(permissionId);	
    	return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult permissionUpdate(@RequestBody RequestObject params, HttpServletRequest request) throws Exception{
    	if (logger.isDebugEnabled()) {
			logger.debug("permissionUpdate====>  params:{}", params);
		}
    	ProjectGroupPermission groupPermission = params.toJavaBean(ProjectGroupPermission.class);
    	permissionService.updatePermission(groupPermission);	
    	return JsonResult.SUCCESS;
    }
    
    @RequestMapping(value = "/info/{perssionId}")
    @ResponseBody
    public JsonResult getPermission(@PathVariable Integer perssionId, HttpServletRequest request) throws Exception{
    	JsonResult jsonResult = new JsonResult();
    	ProjectGroupPermission groupPermission = permissionService.queryBykey(perssionId);
    	if (logger.isDebugEnabled()) {
    		logger.debug("getPermission====>  perssionId:{}, permission:{}", perssionId , groupPermission);
    	}
    	jsonResult.put("permission", groupPermission);
    	return jsonResult;
    }
    /**
     * 微信端查询企业下个人菜单权限
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-29 16:51:00
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/weChat/getAuthMenu")
    @ResponseBody
    public JsonResult getgetAuthMenu(HttpServletRequest request) throws Exception{
    	JsonResult jsonResult = new JsonResult();
    	User user = loadUser(request);
    	Collection<MergeAuthorityMenu> collection = permissionService.queryAuthorityMenuByUkey(user.getId());
    	jsonResult.put("results", collection);
    	return jsonResult;
    }
    
    
}
