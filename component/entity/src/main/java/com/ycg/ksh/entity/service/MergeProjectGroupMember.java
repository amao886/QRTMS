/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 12:55:19
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ProjectGroupMember;
import com.ycg.ksh.entity.persistent.ProjectGroupRole;
import org.apache.commons.beanutils.BeanUtils;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 12:55:19
 */
public class MergeProjectGroupMember extends ProjectGroupMember {

	private static final long serialVersionUID = 3111669778258501477L;

	private AssociateUser user;
	
	private ProjectGroupRole rolePermission;
	
	public MergeProjectGroupMember() {
		super();
	}
	public MergeProjectGroupMember(ProjectGroupMember projectGroupMember) throws Exception {
		super();
		BeanUtils.copyProperties(this, projectGroupMember);
	}
	/**
	 * getter method for user
	 * @return the user
	 */
	public AssociateUser getUser() {
		return user;
	}
	/**
	 * setter method for user
	 * @param user the user to set
	 */
	public void setUser(AssociateUser user) {
		this.user = user;
	}
	/**
	 * getter method for rolePermission
	 * @return the rolePermission
	 */
	public ProjectGroupRole getRolePermission() {
		return rolePermission;
	}
	/**
	 * setter method for rolePermission
	 * @param rolePermission the rolePermission to set
	 */
	public void setRolePermission(ProjectGroupRole rolePermission) {
		this.rolePermission = rolePermission;
	}
	
}
