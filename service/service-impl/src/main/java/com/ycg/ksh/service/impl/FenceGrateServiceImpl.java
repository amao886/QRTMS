package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.FenceGrate;
import com.ycg.ksh.service.persistence.FenceGrateMapper;
import com.ycg.ksh.service.api.FenceGrateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("ksh.core.service.fenceGrateService")
public class FenceGrateServiceImpl implements FenceGrateService{

	@Resource
	private FenceGrateMapper fenceGrateMapper;
	
	@Override
	public Integer save(FenceGrate fenceGrate) throws ParameterException, BusinessException {
		Assert.notNull(fenceGrate, "电子围栏信息为空");
		try {
			return fenceGrateMapper.insertSelective(fenceGrate);
		} catch (Exception e) {
			logger.debug("save  fenceGrate:{}",fenceGrate,e);
			//BusinessException.dbException("新增电子围栏信息异常");
			throw new BusinessException("新增电子围栏信息异常");
		}
	}

	@Override
	public Integer update(FenceGrate fenceGrate) throws ParameterException, BusinessException {
		Assert.notBlank(fenceGrate.getId(), "电子围栏主键为空");
		try {
			return fenceGrateMapper.updateByPrimaryKeySelective(fenceGrate);
		} catch (Exception e) {
			logger.debug("update  fenceGrate:{}", fenceGrate, e);
			BusinessException.dbException("更新电子围栏信息异常");
		}
		return null;
	}

	@Override
	public Double queryByCustomerId(Integer customerId) throws ParameterException, BusinessException {
		try {
			return fenceGrateMapper.getRadiusByCustomerKey(customerId);
		} catch (Exception e) {
			logger.debug("queryByCustomerId  customerId:{}", customerId, e);
			throw new BusinessException("查询围栏信息异常");
		}
	}
}
