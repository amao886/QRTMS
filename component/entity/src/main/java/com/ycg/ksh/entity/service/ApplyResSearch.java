package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.RequestEntity;

public class ApplyResSearch extends RequestEntity {

	private static final long serialVersionUID = 7447466271176043159L;
	
	private Integer userId; //管理员用户Id为999的
	
	private String groupId; //项目组主键
	
	private String contacts; //联系人

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
}
