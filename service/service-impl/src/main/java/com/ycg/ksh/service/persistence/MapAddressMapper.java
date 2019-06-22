package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.MapAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * 地图地址持久层
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:25:04
 */
public interface MapAddressMapper extends Mapper<MapAddress> {
	
	
	/**
	 * 修改包裹数量
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 15:36:34
	 * @param id
	 * @param count
	 */
	void packageCount(Integer id, Integer count);
	/**
	 * 批量新增地图地址信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:12:46
	 * @param addresss 地图地址集合
	 */
	void inserts(Collection<MapAddress> addresss);
	
	/**
	 * 批量删除地图地址信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:13:12
	 * @param addresss  地图地址编号集合
	 */
	void deletes(Collection<Integer> addresss);
	
	/**
	 * 根据地址模糊查询同一个组内的地图地址信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:13:31
	 * @param userKey  用户编号
	 * @param search   地址查询字符串
	 * @return
	 */
	Collection<MapAddress> listBySomething(Integer userKey, String search);
	
	/**
	 * 根据地址模糊查询个人的地图地址信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 14:59:29
	 * @param userKey   用户编号
	 * @param addressType  地址类型
	 * @param search   地址查询字符串
	 * @return
	 */
	Collection<MapAddress> listByUserKey(Integer userKey, Integer addressType, String search);
	
	/**
	 * 复制地址信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:15:17
	 * @param userKey   用户的编号
	 * @param collection  要复制到地址编号集合
	 */
	void saveAddressByCopy(Integer userKey, Collection<Integer> collection);
}