package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.AppVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AppVersionService {
	
	final Logger logger = LoggerFactory.getLogger(AppVersionService.class);
	/**
	 * 
	 * TODO 添加版本信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-20 10:26:34
	 * @param appVersion
	 */
	void saveAppVersion(AppVersion appVersion) throws BusinessException, ParameterException;
	/**
	 * 
	 * TODO 查询最新版本信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-20 11:01:30
	 * @return
	 * @throws BusinessException
	 * @throws ParameterException
	 */
	AppVersion queryLastEntity() throws BusinessException, ParameterException;
}
