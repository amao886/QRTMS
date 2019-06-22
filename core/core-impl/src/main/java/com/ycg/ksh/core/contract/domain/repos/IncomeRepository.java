package com.ycg.ksh.core.contract.domain.repos;

import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.contract.application.dto.IncomeRecordDto;
import com.ycg.ksh.core.contract.domain.model.Contract;
import com.ycg.ksh.core.contract.domain.model.IncomeRecord;

/**
 * 收支资源库
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public interface IncomeRepository extends DomainRepository<IncomeRecord, Long> {

    /**
     * 根据送货单号查询
     *
     * @param orderKey
     * @return
     */
    boolean existIncome(Long orderKey);

}
