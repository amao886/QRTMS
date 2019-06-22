package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.CompanyTemplate;
import tk.mybatis.mapper.common.Mapper;

public interface CompanyTemplateMapper extends Mapper<CompanyTemplate> {

    CompanyTemplate getByCompanyKey(Long companyKey);
}