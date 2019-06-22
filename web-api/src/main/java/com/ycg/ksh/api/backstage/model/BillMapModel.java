/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-08 17:27:41
 */
package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 运单地图展示模型实体类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-08 17:27:41
 */
public class BillMapModel extends BaseEntity {

	private static final long serialVersionUID = -7735613439228830407L;


    private String billNumber;
    private String deliverymanPhone;
    private String destination;
    private Integer billStatus;
	private Double latitude;//经度
	private Double longitude;//纬度
	
	
	public BillMapModel() {
		super();
	}
	public BillMapModel(String billNumber, String deliverymanPhone, String destination, Integer billStatus,
			Double latitude, Double longitude) {
		super();
		this.billNumber = billNumber;
		this.deliverymanPhone = deliverymanPhone;
		this.destination = destination;
		this.billStatus = billStatus;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	/**
	 * getter method for billNumber
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}
	/**
	 * setter method for billNumber
	 * @param billNumber the billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	/**
	 * getter method for deliverymanPhone
	 * @return the deliverymanPhone
	 */
	public String getDeliverymanPhone() {
		return deliverymanPhone;
	}
	/**
	 * setter method for deliverymanPhone
	 * @param deliverymanPhone the deliverymanPhone to set
	 */
	public void setDeliverymanPhone(String deliverymanPhone) {
		this.deliverymanPhone = deliverymanPhone;
	}
	/**
	 * getter method for destination
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * setter method for destination
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	/**
	 * getter method for billStatus
	 * @return the billStatus
	 */
	public Integer getBillStatus() {
		return billStatus;
	}
	/**
	 * setter method for billStatus
	 * @param billStatus the billStatus to set
	 */
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	/**
	 * getter method for latitude
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}
	/**
	 * setter method for latitude
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	/**
	 * getter method for longitude
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}
	/**
	 * setter method for longitude
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
