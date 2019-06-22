package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ProjectGroupMember;
import tk.mybatis.mapper.common.Mapper;

/**
 * 项目组成员持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:23:14
 */
public interface ProjectGroupMemberMapper extends Mapper<ProjectGroupMember> {
	
    
    /**
     * 根据项目组和用户编号查询项目组成员信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 12:29:35
     * @param groupId
     * @param userId
     * @return
     */
    ProjectGroupMember get(Integer groupId, Integer userId);
    
	/**
	 * 是否是组员
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 11:19:17
	 * @param groupId
	 * @param userId
	 * @return
	 */
	Integer isMember(Integer groupId, Integer userId);
}