package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ProjectGroupRole;
import tk.mybatis.mapper.common.Mapper;

/**
 * 项目组角色持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:24:35
 */
public interface ProjectGroupRoleMapper extends Mapper<ProjectGroupRole> {
    
    /**
     * 查询用户的项目组角色
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:50:42
     * @param groupKey
     * @param userKey
     * @return
     */
    ProjectGroupRole getRoleByUserKey(Integer groupKey, Integer userKey);
    /**
     * 
     * TODO 根据角色编号查询角色
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-04 17:01:45
     * @param roleCode
     * @return
     */
    Integer getRoleByRoleCode(String roleCode);
    
}