package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ProjectGroupPermission;
import com.ycg.ksh.entity.persistent.RolePermission;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

public class RoleAndPermission extends RolePermission{

	private static final long serialVersionUID = 5581449474524206493L;
	
	private String roleName; //角色名称
	
	private String roleCode; //角色编码
	
	private Collection<ProjectGroupPermission> projectGroupPermissions; //权限集合
	
	public RoleAndPermission() {
		super();
	}

	public RoleAndPermission(RolePermission rolePermission) throws Exception {
		super();
		BeanUtils.copyProperties(this, rolePermission);
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Collection<ProjectGroupPermission> getProjectGroupPermissions() {
		return projectGroupPermissions;
	}

	public void setProjectGroupPermissions(Collection<ProjectGroupPermission> projectGroupPermissions) {
		this.projectGroupPermissions = projectGroupPermissions;
	}
	
}
