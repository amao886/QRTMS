package com.ycg.ksh.core.contract.application;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.contract.application.dto.*;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 合同计价应用服务
 */
public interface ContractApplicationService {

    final Logger logger = LoggerFactory.getLogger(ContractApplicationService.class);

    /**
     * 查看合同明细
     *
     * @param contractKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    ContractDetailDto getContract(Long contractKey) throws ParameterException, BusinessException;

    /**
     * 新增合同
     *
     * @param company       当前用户所在公司
     * @param contractDto   合同信息
     * @param valuationDtos 计价配置信息
     * @throws ParameterException
     * @throws BusinessException
     */
    void save(CompanyConcise company, Integer userKey, ContractDto contractDto, Collection<ValuationDto> valuationDtos) throws ParameterException, BusinessException;

    /**
     * 更新合同
     *
     * @param company       当前用户所在公司
     * @param contractDto   合同信息
     * @param valuationDtos 计价配置信息
     * @throws ParameterException
     * @throws BusinessException
     */
    void modify(CompanyConcise company, Integer userKey, ContractDto contractDto, Collection<ValuationDto> valuationDtos) throws ParameterException, BusinessException;

    /**
     * 合同审核
     *
     * @param userKey     审核人编号
     * @param contractKey 合同编号
     * @param verifyDto   审核信息
     * @throws ParameterException
     * @throws BusinessException
     */
    void verify(Integer userKey, Long contractKey, ContractVerifyDto verifyDto) throws ParameterException, BusinessException;

    /**
     * 合同计价列表查询
     *
     * @param companyConcise 当前用户所在公司
     * @param contractType   合同类型
     * @param likeString     合同编号/客户名称
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Page<ContractDto> searchContract(CompanyConcise companyConcise, Integer contractType, String likeString, PageScope scope) throws ParameterException, BusinessException;


    /**
     * 批量计算单笔应收
     *
     * @param dtos
     * @throws ParameterException
     * @throws BusinessException
     */
    void singleReceivable(Integer userKey, CompanyConcise companyConcise, Collection<IncomeRecordDto> dtos) throws ParameterException, BusinessException;


    /**
     * 查询计算应收详情
     *
     * @param orderKeys
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<IncomeRecordDto> receivableDetails(Collection<Long> orderKeys, CompanyConcise companyConcise, Integer userKey) throws ParameterException, BusinessException;

    /**
     * 批量导入物料
     *
     * @param contractKey
     * @param fileEntity
     * @throws ParameterException
     * @throws BusinessException
     */
    void batchImportCommodity(Long contractKey, FileEntity fileEntity) throws ParameterException, BusinessException;


    /**
     * 查询物料列表
     *
     * @param contractKey 合同ID
     * @param likeString  物料编号/物料名称
     * @param scope       分页参数
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Page<CommodityConfigDto> commoditySearch(Long contractKey, String likeString, PageScope scope) throws ParameterException, BusinessException;


    /**
     * 收支列表查询
     *
     * @param likeString
     * @param statuts
     * @param deliveryDateStart
     * @param deliveryDateEnd
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Page<IncomeRecordDto> incomeSearch(String likeString, Integer statuts, LocalDateTime deliveryDateStart,
                                       LocalDateTime deliveryDateEnd, PageScope pageScope, Long companKey) throws ParameterException, BusinessException;
}
