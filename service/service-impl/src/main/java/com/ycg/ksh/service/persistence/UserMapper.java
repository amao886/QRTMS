package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.User;
import tk.mybatis.mapper.common.Mapper;

/**
 * 用户持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:27:53
 */
public interface UserMapper extends Mapper<User> {
	
    /**
     * 根据mobile获取用户信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-06 10:52:32
     * @param mobile
     * @return
     */
    User loadUserByMobile(String mobile);
	/**
	 * 根据openId获取用户信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:41:32
	 * @param openId
	 * @return
	 */
	User loadUserByOpenId(String openId);
	/**
	 * 根据unionId获取用户信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:25:55
	 * @param unionId
	 * @return
	 */
	User loadUserByUnionId(String unionId);
	

	/**
	 * 根据openid更新关注状态
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-16 16:51:21
	 * @param openId  微信用户标识
	 * @param subscribe 关注状态(1:关注，0:未关注)
	 */
	void modifySubscribe(String openId, String subscribe);
	
	/**
	 * 查询手机号绑定数量
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-06 13:53:14
	 * @param mobile 手机号
	 * @return
	 */
	Integer countByMobile(String mobile);
}