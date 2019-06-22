package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.RequestEntity;

/**        
 * 名称：    BillTotal    
 * 描述：    任务单日统计详情
 * 创建人：zcl    
 * 创建时间：2017年11月17日 下午1:59:03
 * @version        
 */
public class BillTotalInfo extends RequestEntity{
	
	private static final long serialVersionUID = 5778670408435192486L;
    private Integer bindstatus;  //任务状态
    private String barcode;//任务单号
    private String companyName;//客户名称
    private String deliveryNumber;//送货单号
    private String bindtime; //绑定时间
    private Integer groupid;//组id
	public Integer getBindstatus() {
		return bindstatus;
	}
	public void setBindstatus(Integer bindstatus) {
		this.bindstatus = bindstatus;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public String getBindtime() {
		return bindtime;
	}
	public void setBindtime(String bindtime) {
		this.bindtime = bindtime;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
}
