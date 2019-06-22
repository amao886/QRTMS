package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDate;

/**
 * 合同
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class ContractDto extends BaseEntity {


    protected Long id;//合同编号
    protected LocalDate contractStartDate;//合同开始日期
    protected LocalDate contractEndDate;//合同结束日期
    //contractType 合同类型: 1：客户合同 2：供应商合同
    protected Integer contractType;//合同类型: 1：客户合同 2：供应商合同
    //contractNo 合同编号
    protected String contractNo;//合同编号--------------不能为空
    //customerCode 客户编码
    protected Long customerCode;//合同对方编码--------------合同对方编码,不能为空
    // customerName 客户名称
    protected String customerName;//合同对方名称--------------合同对方名称,不能为空
    //industryType 行业类别
    protected Integer industryType;//行业类别--------------合同对方行业类别，供应商合同没有

    protected Integer verifyStatus;

    protected String contactName;
    protected String telephoneNumber;
    protected String mobileNumber;
    protected String customerProvince;
    protected String customerCity;
    protected String customerDistrict;
    protected String customerAddress;
    protected String customerFullAddress;

    public ContractDto() {
    }

    public void base(Long contractKey, LocalDate contractStartDate, LocalDate contractEndDate, Integer contractType, String contractNo) {
        this.id = contractKey;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.contractType = contractType;
        this.contractNo = contractNo;
    }

    public void customer(Long customerCode, String customerName, Integer industryType, String contactName, String telephoneNumber, String mobileNumber, String customerProvince, String customerCity, String customerDistrict, String customerAddress, String customerFullAddress) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.industryType = industryType;
        this.contactName = contactName;
        this.telephoneNumber = telephoneNumber;
        this.mobileNumber = mobileNumber;
        this.customerProvince = customerProvince;
        this.customerCity = customerCity;
        this.customerDistrict = customerDistrict;
        this.customerAddress = customerAddress;
        this.customerFullAddress = customerFullAddress;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Long customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getIndustryType() {
        return industryType;
    }

    public void setIndustryType(Integer industryType) {
        this.industryType = industryType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustomerProvince() {
        return customerProvince;
    }

    public void setCustomerProvince(String customerProvince) {
        this.customerProvince = customerProvince;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerDistrict() {
        return customerDistrict;
    }

    public void setCustomerDistrict(String customerDistrict) {
        this.customerDistrict = customerDistrict;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerFullAddress() {
        return customerFullAddress;
    }

    public void setCustomerFullAddress(String customerFullAddress) {
        this.customerFullAddress = customerFullAddress;
    }
}
