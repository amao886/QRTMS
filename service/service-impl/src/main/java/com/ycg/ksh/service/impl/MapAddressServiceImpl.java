/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:38:08
 */
package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.MapAddress;
import com.ycg.ksh.service.persistence.MapAddressMapper;
import com.ycg.ksh.service.persistence.MapGroupMapper;
import com.ycg.ksh.service.api.MapAddressService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;

/**
 * 地图地址业务实现类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:38:08
 */
@Service("ksh.core.service.mapAddressService")
public class MapAddressServiceImpl implements MapAddressService {

	protected final Logger logger = LoggerFactory.getLogger(MapAddressService.class);
	
	@Resource
	MapAddressMapper addressMapper;
	@Resource
	MapGroupMapper mapGroupMapper;
	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#save(MapAddress)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:38:08
	 * @param address  实体信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public MapAddress save(MapAddress address) throws ParameterException, BusinessException {
		Assert.notNull(address, "实体对象不能为空");
		Assert.notBlank(address.getAddress(), "地址不能为空");
		try {
			if(null == address.getAddressType() || address.getAddressType() <= 0) {
				address.setAddressType(Constant.ADDRESS_TYPE_COMMON);
			}
			address.setModifyTime(new Date());
			address.setStatus(1);
			addressMapper.insert(address);
			return address;
		} catch (Exception e) {
			logger.error("save -> address:{}", address, e);
			throw BusinessException.dbException("保存异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#update(MapAddress)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 12:23:42
	 * @param address 要更新的地址信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public MapAddress update(MapAddress address) throws ParameterException, BusinessException {
		Assert.notNull(address, "实体对象不能为空");
		Assert.notBlank(address.getId(), "修改对象编号不能为空");
		try {
			MapAddress exister = addressMapper.selectByPrimaryKey(address.getId());
			if(exister == null) {
				throw new BusinessException("要更新的对象不存在!");
			}
			if(StringUtils.isNotBlank(address.getAddress())) {
				exister.setAddress(address.getAddress());
				exister.setLatitude(address.getLatitude());
				exister.setLongitude(address.getLongitude());
			}
			if(null == address.getAddressType() || address.getAddressType() <= 0) {
				address.setAddressType(Constant.ADDRESS_TYPE_COMMON);
			}
			if(address.getPackageCount() != null) {
				exister.setPackageCount(address.getPackageCount());
			}
			exister.setRemark(address.getRemark());
			//exister.setModifyTime(new Date());
			addressMapper.updateByPrimaryKey(exister);
			return exister;
		} catch (Exception e) {
			logger.error("update -> address:{}", address, e);
			throw BusinessException.dbException("数据持久化异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#modifyPackageCount(java.lang.Integer, java.lang.Integer)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 15:35:47
	 * @param id
	 * @param count
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	@Override
	public void modifyPackageCount(Integer id, Integer count) throws ParameterException, BusinessException {
		Assert.notBlank(id, "地址编号不能为空");
		try {
			addressMapper.packageCount(id, count);
		} catch (Exception e) {
			logger.error("modifyPackageCount -> id:{}, count:{}", id, count, e);
			throw BusinessException.dbException("修改包裹数量异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#save(java.util.Collection)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:38:08
	 * @param addresss 要保存的地址信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public void save(Collection<MapAddress> collection) throws ParameterException, BusinessException {
		Assert.notEmpty(collection, "至少输入一条要保存的数据");
		try {
			Date date = new Date();
			collection.forEach(address -> {
				if(null == address.getAddressType() || address.getAddressType() <= 0) {
					address.setAddressType(Constant.ADDRESS_TYPE_COMMON);
				}
				address.setModifyTime(date);
    			address.setStatus(1);
	    	});
			addressMapper.inserts(collection);
		} catch (Exception e) {
			logger.error("save -> collection:{}", collection, e);
			throw BusinessException.dbException("批量保存异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#delete(java.util.Collection)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 14:38:08
	 * @param keys  要删除的地址编号集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public void delete(Collection<Integer> keys) throws ParameterException, BusinessException {
		Assert.notEmpty(keys, "至少选择一条要删除的ID");
		try {
			addressMapper.deletes(keys);
		} catch (Exception e) {
			logger.error("delete -> keys:{}", keys, e);
			throw BusinessException.dbException("批量删除异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#listByUserKey(java.lang.String, java.lang.Integer)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 17:17:33
	 * @param search  地址查询字符串
	 * @param search  地址查询字符串
	 * @param userKey  用户编号
	 * @return 如果用户加组了, 返回的是所有组地址库中满足条件的地址集合；如果没加组，返回的是用户自己地址库中满足条件的地址集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public Collection<MapAddress> listByUserKey(Integer userKey, Integer addressType, String search) throws ParameterException, BusinessException {
		try {
			if(null == addressType || addressType <= 0) {
				addressType = Constant.ADDRESS_TYPE_COMMON;
			}
			if(addressType == Constant.ADDRESS_TYPE_COMMON && mapGroupMapper.countGroup(userKey) > 0) {
				return addressMapper.listBySomething(userKey, search);
			}else {
				return addressMapper.listByUserKey(userKey, addressType, search);
			}
		} catch (Exception e) {
			logger.error("listByUserKey -> userKey:{} addressType:{} search:{}", userKey, addressType, search, e);
			throw BusinessException.dbException("数据查询异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#listByKeys(java.lang.Integer, java.util.Collection)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 15:41:30
	 * @param userKey  用户编号
	 * @param keys  要查询的地址编号集合
	 * @return  满足条件的地址集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public Collection<MapAddress> listByKeys(Integer userKey, Collection<Integer> keys) throws ParameterException, BusinessException {
		try {
			Example example = new Example(MapAddress.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("userId", userKey);
			criteria.andNotEqualTo("status", 0);
			if(CollectionUtils.isNotEmpty(keys)) {
				criteria.andIn("id", keys);
			}
			example.orderBy("address");
			return addressMapper.selectByExample(example);
		} catch (Exception e) {
			logger.error("listByKeys -> userKey:{} keys:{}", userKey, keys, e);
			throw BusinessException.dbException("数据查询异常");
		}
	}

	/**
	 * @see com.ycg.ksh.service.api.MapAddressService#saveAddressByCopy(java.lang.Integer, java.util.Collection)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-13 10:07:22
	 * @param userKey  用户编号
	 * @param collection  要复制的地址编号集合
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public void copyAddress(Integer userKey, Collection<Integer> collection) throws ParameterException, BusinessException {
		Assert.notEmpty(collection, "至少有一条要复制的ID");
		try {
			addressMapper.saveAddressByCopy(userKey, collection);
		} catch (Exception e) {
			logger.error("copyAddress -> userKey:{} collection:{}", userKey, collection, e);
			throw BusinessException.dbException("数据操作异常");
		}
	}
}
