package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 任务单查询条件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-18 16:42:51
 */
public class WayBillCondition extends BaseEntity {


    /**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-18 16:42:00
	 */
	private static final long serialVersionUID = 910959891833460416L;
	
	private String barcode;// 条码
    private Integer bindstatus;//条码绑定状态
	protected String deliveryNumber;// 送货单号
    private Integer groupid;// 对应组
    private String bindStartTime;// 绑定开始时间
    private String bindEndTime; //绑定结束时间
    private String searchData;//模糊搜索条件
    
    
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getBindstatus() {
		return bindstatus;
	}
	public void setBindstatus(Integer bindstatus) {
		this.bindstatus = bindstatus;
	}
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getBindStartTime() {
		return bindStartTime;
	}
	public void setBindStartTime(String bindStartTime) {
		this.bindStartTime = bindStartTime;
	}
	public String getBindEndTime() {
		return bindEndTime;
	}
	public void setBindEndTime(String bindEndTime) {
		this.bindEndTime = bindEndTime;
	}
	public String getSearchData() {
		return searchData;
	}
	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}

}
