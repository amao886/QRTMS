package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.BaseEntity;

public class BindPhone extends BaseEntity {

	private static final long serialVersionUID = -6815575654880453967L;

	private String phone;
	private String code;
	/**
	 * 创建一个新的 BindPhone实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-04 11:04:10
	 */
	private BindPhone() {
		super();
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
