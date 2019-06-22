package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.MapGroup;
import com.ycg.ksh.entity.persistent.MapGroupMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * 地图组
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:38:10
 */
public interface MapGroupMapper extends Mapper<MapGroup> {
	
	/**
	 * 加入组
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:38:23
	 * @param member
	 */
	void insertMember(MapGroupMember member);
	
	/**
	 * 查询组员信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:38:38
	 * @param groupId
	 * @param userId
	 */
	MapGroupMember getMember(Integer groupId, Integer userId);
	
	/**
	 * 进组的数量
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 23:26:52
	 * @param userId
	 * @return
	 */
	int countGroup(Integer userId);
	
	/**
	 * 差寻所有组信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 23:36:09
	 * @param userKey
	 * @return
	 */
	Collection<MapGroup> listByUserKey(Integer userKey);
}