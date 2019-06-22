/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:43:33
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.ImageStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 图片存储接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:43:33
 */
public interface ImageStorageService {

    final Logger logger = LoggerFactory.getLogger(DriverService.class);
	
    /**
     * 查询图片存储数量
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 17:45:34
     * @param imageType
     * @param associateKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Integer count(Integer imageType, Serializable associateKey) throws ParameterException, BusinessException;
    /**
	 * 查询图片信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:48:30
	 * @param imageType
	 * @param associateKey
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	Collection<ImageStorage> list(Integer imageType, Serializable associateKey) throws ParameterException, BusinessException;
	
	/**
	 * 存储图片
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:50:39
	 * @param imageType
	 * @param associateKey
	 * @param path
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void save(Integer imageType, Serializable associateKey, String path) throws ParameterException, BusinessException;
	
	/**
	 * 批量存储图片
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:50:53
	 * @param imageType
	 * @param associateKey
	 * @param paths
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void save(Integer imageType, Serializable associateKey, Collection<String> paths) throws ParameterException, BusinessException;
	
	/**
	 * 批量存储图片
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:50:56
	 * @param collection
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void save(List<ImageStorage> collection) throws ParameterException, BusinessException;
	
	/**
	 * 删除图片
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 08:50:58
	 * @param imageType
	 * @param associateKey
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void delete(Integer imageType, Serializable associateKey) throws ParameterException, BusinessException;
}
