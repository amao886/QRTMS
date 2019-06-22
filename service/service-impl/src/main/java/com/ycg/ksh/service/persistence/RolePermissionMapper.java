package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.RolePermission;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface RolePermissionMapper extends Mapper<RolePermission> {
	/**
	 * 
	 * TODO 查询所有角色
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-04 11:45:49
	 * @return
	 */
	Collection<RolePermission> queryRolePermission();
	/**
	 * 
	 * TODO 根据角色主键删除对应的权限
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-05 17:49:27
	 * @param roleId
	 */
	int deleteByRoleId(Integer roleId);
}