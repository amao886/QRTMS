package com.ycg.ksh.core.contract.domain.repos;

import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.contract.domain.model.Contract;
import com.ycg.ksh.core.contract.search.dto.ContractDtoJdbc;
import com.ycg.ksh.core.scene.domain.model.VehicleRegistration;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同资源库
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public interface ContractRepository extends DomainRepository<Contract, Long> {

    long countByContractNo(String contractNo, Long exkey);

    /**
     * 查询合同详情
     *
     * @param companyKey
     * @param customerKey
     * @param validityPeriod
     * @return
     */
    Contract getContract(Long companyKey, Long customerKey, LocalDateTime validityPeriod);


}
