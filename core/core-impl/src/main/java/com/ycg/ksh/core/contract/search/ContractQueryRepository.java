package com.ycg.ksh.core.contract.search;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.core.contract.search.dto.CommodityConfigDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.ContractDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.IncomeRecordDtoJdbc;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同计价查询
 */
public interface ContractQueryRepository {

    final Logger logger = LoggerFactory.getLogger(ContractQueryRepository.class);

    /**
     * 查询合同计价列表
     *
     * @param companyConcise
     * @param contractType
     * @param likeString
     * @return
     */
    Page<ContractDtoJdbc> pricingSearch(CompanyConcise companyConcise, Integer contractType, String likeString, int num, int size);


    /**
     * 根据合同编号查询详情
     *
     * @param contractNo
     * @param exkey
     * @return
     */
    long countByContractNo(String contractNo, Long exkey);

    /**
     * 查询物料集合
     *
     * @param contractKey
     * @param likeString
     * @param num
     * @param size
     * @return
     */
    Page<CommodityConfigDtoJdbc> commoditySearch(Long contractKey, String likeString, int num, int size);


    /**
     * 收支列表查询
     *
     * @param likeString
     * @param statuts
     * @param deliveryDateStart
     * @param deliveryDateEnd
     * @param companKey
     * @param num
     * @param size
     * @return
     */
    Page<IncomeRecordDtoJdbc> incomeRecordSearch(String likeString, Integer statuts, LocalDateTime deliveryDateStart,
                                                 LocalDateTime deliveryDateEnd, Long companKey, int num, int size);

}
