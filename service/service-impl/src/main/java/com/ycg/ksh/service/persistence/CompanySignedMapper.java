package com.ycg.ksh.service.persistence;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.CompanySigned;
import org.apache.ibatis.annotations.Param;

public interface CompanySignedMapper extends CustomMapper<CompanySigned> {

    void updateCompanySignNum(@Param("id") Long id, @Param("presentNum") Integer presentNum);
}
