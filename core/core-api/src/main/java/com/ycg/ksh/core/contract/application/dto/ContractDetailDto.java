package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * 合同
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class ContractDetailDto extends ContractDto {

    private List<ValuationDto> list;
    private List<ContractVerifyDto> auditResultList;//审核信息

    public ContractDto contractDto(){
        ContractDto contractDto = new ContractDto();
        contractDto.base(id, contractStartDate, contractEndDate, contractType, contractNo);
        contractDto.customer(customerCode, customerName, industryType, contactName, telephoneNumber,  mobileNumber, customerProvince, customerCity, customerDistrict, customerAddress, customerFullAddress) ;
        return contractDto;
    }


    public List<ValuationDto> getList() {
        return list;
    }

    public void setList(List<ValuationDto> list) {
        this.list = list;
    }

    public List<ContractVerifyDto> getAuditResultList() {
        return auditResultList;
    }

    public void setAuditResultList(List<ContractVerifyDto> auditResultList) {
        this.auditResultList = auditResultList;
    }
}
