package com.ycg.ksh.service.persistence;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.CompanyBankVerify;
import com.ycg.ksh.entity.service.CompanyBank;
import tk.mybatis.mapper.common.Mapper;

public interface CompanyBankVerifyMapper extends Mapper<CompanyBankVerify> {
	
	CompanyBank queryCompanyBank(Integer userKey) throws ParameterException,BusinessException;

	CompanyBankVerify queryByCompanyKey(Long companyKey) throws ParameterException,BusinessException;
}