package com.ycg.ksh.service.persistence;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.EmployeeSeal;

import java.util.Collection;

/**
 * 个人印章持久类
 */
public interface EmployeeSealMapper extends CustomMapper<EmployeeSeal> {

    /**
     * 根据个人ID和公司ID 查询公章ID
     *
     * @param employeeId
     * @param companyId
     * @return
     */
    Collection<Long> getEmployeeSealId(Integer employeeId, Long companyId);
}
