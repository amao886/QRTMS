/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 09:12:37
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;
import java.util.Date;

/**
 * 条码查询
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 09:12:37
 */
public class BarcodeSearch extends BaseEntity {

	private static final long serialVersionUID = -7394582249069064782L;


    private Integer resourceId;
    private String barcode;
    private Date firstTime;
    private Date secondTime;
    private Integer[] bindStatus;
    private String codeBatch;
    private Integer userId;
    private Integer groupId;
    private String startNum;//开始条码
	private String endNum;//结束条码
	private Collection<Long> companyIds;//企业编号集合
	private Long companyId;
    
	public BarcodeSearch() {
		super();
	}
	public BarcodeSearch(Integer resourceId, Integer...bindStatus) {
		super();
		this.resourceId = resourceId;
		this.bindStatus = bindStatus;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Date getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}
	public Date getSecondTime() {
		return secondTime;
	}
	public void setSecondTime(Date secondTime) {
		this.secondTime = secondTime;
	}
	public Integer[] getBindStatus() {
		return bindStatus;
	}
	public void setBindStatus(Integer[] bindStatus) {
		this.bindStatus = bindStatus;
	}
	public String getCodeBatch() {
		return codeBatch;
	}
	public void setCodeBatch(String codeBatch) {
		this.codeBatch = codeBatch;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getStartNum() {
		return startNum;
	}
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	public Collection<Long> getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(Collection<Long> companyIds) {
		this.companyIds = companyIds;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
