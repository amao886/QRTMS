package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.MapAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 地图地址业务逻辑接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:37:40
 */
public interface MapAddressService {

	final Logger logger = LoggerFactory.getLogger(MapAddressService.class);

	/**
	 * 复制地址
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-13 10:05:37
	 * @param userKey  用户编号
	 * @param collection  要复制的地址编号集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	void copyAddress(Integer userId, Collection<Integer> collection) throws ParameterException, BusinessException;
	/**
	 * 保存单个对象
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:34:54
	 * @param address  实体信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	MapAddress save(MapAddress address) throws ParameterException, BusinessException;

	/**
	 * 修改单个对象
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 12:21:51
	 * @param address 要更新的地址信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	MapAddress update(MapAddress address) throws ParameterException, BusinessException;
	
	/**
	 * 更新地址上的包裹数量
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 15:34:41
	 * @param id  地址编号
	 * @param count  包裹数量
	 * @return 
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void modifyPackageCount(Integer id, Integer count) throws ParameterException, BusinessException;
	/**
	 * 批量保存对象
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:34:51
	 * @param addresss 要保存的地址信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	void save(Collection<MapAddress> addresss) throws ParameterException, BusinessException;

	/**
	 * 批量删除
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:34:47
	 * @param keys  要删除的地址编号集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	void delete(Collection<Integer> keys) throws ParameterException, BusinessException;
	

	/**
	 * 根据用户ID查询地图地址信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 17:16:57
	 * @param search  地址查询字符串
	 * @param addressType 地址类型
	 * @param userKey  用户编号
	 * @return 如果用户加组了, 返回的是所有组地址库中满足条件的地址集合；如果没加组，返回的是用户自己地址库中满足条件的地址集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	Collection<MapAddress> listByUserKey(Integer userKey, Integer addressType, String search) throws ParameterException, BusinessException;
	
	/**
	 * 查询路径信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 15:37:38
	 * @param userKey  用户编号
	 * @param keys  要查询的地址编号集合
	 * @return  满足条件的地址集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	Collection<MapAddress> listByKeys(Integer userKey, Collection<Integer> keys) throws ParameterException, BusinessException;
}
