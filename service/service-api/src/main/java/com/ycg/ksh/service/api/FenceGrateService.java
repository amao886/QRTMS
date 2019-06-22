package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.FenceGrate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-27 11:01:34
 */
public interface FenceGrateService {
	final Logger logger = LoggerFactory.getLogger(FenceGrateService.class);
	/**
	 * 
	 * TODO 新增电子围栏信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-27 11:06:08
	 * @param fenceGrate 电子围栏信息
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	Integer save(FenceGrate fenceGrate)throws ParameterException, BusinessException;
	/**
	 * 
	 * TODO 更新电子围栏信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-27 11:06:33
	 * @param fenceGrate 电子围栏信息
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	Integer update(FenceGrate fenceGrate)throws ParameterException, BusinessException;
	
	/**
	 * 
	 * TODO 获取电子围栏信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-27 11:08:56
	 * @param id
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	Double queryByCustomerId(Integer customerId)throws ParameterException, BusinessException;
}
