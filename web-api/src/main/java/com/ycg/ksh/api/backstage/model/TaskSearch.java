package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.RequestEntity;

import java.util.Date;

/**
 * 任务查询条件实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-05 09:59:26
 */
public class TaskSearch extends RequestEntity {

	private static final long serialVersionUID = -6603956477349723775L;

	private Integer groupId;
	private String searchData;
	private Integer status;//任务状态
	private Boolean delay;//是否延迟
	private Date createtime;//发货时间
	
	
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getSearchData() {
		return searchData;
	}
	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean getDelay() {
		return delay;
	}
	public void setDelay(Boolean delay) {
		this.delay = delay;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
}
