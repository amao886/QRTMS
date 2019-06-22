package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.CompanyEmployee;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface CompanyEmployeeMapper extends Mapper<CompanyEmployee> {

    /**
     * 第一个管理员
     * @param companyKey
     *
     * @return
     */
    CompanyEmployee firstAdminCompany(Long companyKey);
    Collection<CompanyEmployee> listAdminCompany(Long companyKey);
}