package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.BaseEntity;

public class ReceiptModel extends BaseEntity {

	private static final long serialVersionUID = -6815575654880453967L;

	private Integer status;//1通过0不通过
	private Integer wayBillId;//任务单Id
	private Integer addressId;//图片id
	private String remark;//审核备注
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getWayBillId() {
		return wayBillId;
	}
	public void setWayBillId(Integer wayBillId) {
		this.wayBillId = wayBillId;
	}
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
