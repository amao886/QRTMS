package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ProjectGroupPermission;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * 项目组权限持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:23:28
 */
public interface ProjectGroupPermissionMapper extends Mapper<ProjectGroupPermission> {
    
    /**
     * 根据项目组和用户查询权限
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 12:36:28
     * @param groupKey
     * @param userKey
     * @return
     */
    Collection<ProjectGroupPermission> selectByGroupUser(Integer groupKey, Integer userKey);
    

    /**
     * 根据角色查询项目组权限信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:47:35
     * @param rKey
     * @return
     */
    Collection<ProjectGroupPermission> selectByRoleKey(Integer rKey);
}