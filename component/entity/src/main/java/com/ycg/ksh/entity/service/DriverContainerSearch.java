/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 10:08:42
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 10:08:42
 */
public class DriverContainerSearch extends BaseEntity {

	private static final long serialVersionUID = 620999643559437351L;

	private String likeString;//条码号/送货单号
	
	private Integer scope = 0;//查询范围(0:所有，1:项目组，2:个人)
	
	private Integer userId;//查询的用户ID
	
    private Date loadFirstTime;//装车开始时间
    private Date loadSencodTime;//装车结束时间
    private Date unloadFirstTime;//卸货开始时间
    private Date unloadSencodTime;//卸货结束时间
    private Integer bindStatus;
    private Boolean unload;
    
    
	public String getLikeString() {
		return likeString;
	}
	public void setLikeString(String likeString) {
		this.likeString = likeString;
	}
	public Date getLoadFirstTime() {
		return loadFirstTime;
	}
	public void setLoadFirstTime(Date loadFirstTime) {
		this.loadFirstTime = loadFirstTime;
	}
	public Date getLoadSencodTime() {
		return loadSencodTime;
	}
	public void setLoadSencodTime(Date loadSencodTime) {
		this.loadSencodTime = loadSencodTime;
	}
	public Date getUnloadFirstTime() {
		return unloadFirstTime;
	}
	public void setUnloadFirstTime(Date unloadFirstTime) {
		this.unloadFirstTime = unloadFirstTime;
	}
	public Date getUnloadSencodTime() {
		return unloadSencodTime;
	}
	public void setUnloadSencodTime(Date unloadSencodTime) {
		this.unloadSencodTime = unloadSencodTime;
	}
	public Integer getBindStatus() {
		return bindStatus;
	}
	public void setBindStatus(Integer bindStatus) {
		this.bindStatus = bindStatus;
	}
	public Boolean getUnload() {
		return unload;
	}
	public void setUnload(Boolean unload) {
		this.unload = unload;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
