package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;

public class AddressSearch extends BaseEntity {

	private static final long serialVersionUID = 140791888070455677L;
	private String companyName; //收货客户
	private String fullAddress; //收货地址
	private String contacts; //联系人
    private String contactNumber; //手机号
	private Integer groupId; //项目组
    private Integer userId; //操作人编号
    private Integer type; //地址类型 1：收货地址，2：发货地址

	private Long companyId;	//所属公司
	private Long customerId; //客户编号
	private Integer customerType;	//客户类型(1:货主,2:收货客户，3:承运方)


	private Collection<Long> customerKeys; //客户编号


	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

    /**
     * getter method for userId
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * setter method for userId
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public Collection<Long> getCustomerKeys() {
		return customerKeys;
	}

	public void setCustomerKeys(Collection<Long> customerKeys) {
		this.customerKeys = customerKeys;
	}
}

