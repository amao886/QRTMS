package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.AppVersion;
import com.ycg.ksh.service.persistence.AppVersionMapper;
import com.ycg.ksh.service.api.AppVersionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("ksh.core.service.appVersionService")
public class AppVersionServiceImpl implements AppVersionService {
	
	@Resource
	AppVersionMapper appVersionMapper;
	
	@Override
	public void saveAppVersion(AppVersion appVersion) throws BusinessException, ParameterException {
		Assert.notNull(appVersion, "版本信息部不能为空");
		Assert.notBlank(appVersion.getUrl(), "版本地址链接不能为空");
		try {
			appVersionMapper.insertSelective(appVersion);
		} catch (Exception e) {
			logger.error("saveAppVersion appVersion:{},e:{}",appVersion,e);
			throw BusinessException.dbException("添加版本信息异常");
		}
	}

	@Override
	public AppVersion queryLastEntity() throws BusinessException, ParameterException {
		try {
			return appVersionMapper.queryLastEntity();
		} catch (Exception e) {
			logger.error("queryLastEntity erro:{}",e);
			throw BusinessException.dbException("查询版本信息异常");
		}
	}

}
