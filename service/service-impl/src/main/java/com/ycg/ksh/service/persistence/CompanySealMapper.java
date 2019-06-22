package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.CompanySeal;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface CompanySealMapper extends Mapper<CompanySeal> {

    /**
     * 查询最新添加的企业印章
     * @param companyKey
     * @return
     */
    CompanySeal getCompanySealByCompanyKey(Long companyKey);

    /**
     * 查询员工授权的企业印章数据
     * @param companyKey
     * @param employeeKey
     * @param likeString
     * @return
     */
    Collection<CompanySeal> listCompanySeals(Long companyKey, Integer employeeKey, String likeString);

    /**
     * 分页查询印章数据
     * @param companySeal
     * @param rowBounds
     * @return
     */
    Page<CompanySeal> pageCompanySeal(CompanySeal companySeal, RowBounds rowBounds);
}