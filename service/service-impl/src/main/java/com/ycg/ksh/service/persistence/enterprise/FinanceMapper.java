package com.ycg.ksh.service.persistence.enterprise;

import com.ycg.ksh.entity.persistent.enterprise.Finance;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;

public interface FinanceMapper extends Mapper<Finance> {

    /**
     * 查询财务数据
     * @param objectType
     * @param objectKey
     * @return
     */
    Finance getFinanceByTypeId(Integer objectType, Serializable objectKey);
}