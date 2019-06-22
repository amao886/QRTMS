package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

public class OrderConcise extends BaseEntity {

	private static final long serialVersionUID = 7040204139682643726L;

	private CompanyConcise shipper;//发货方
	private CompanyConcise receive;//收货方
	private CompanyConcise convey;//承运方


	private long shipperKey;//发货方
	private long receiveKey;//收货方
	private long conveyKey;//承运商
	private Date createTime;//创建时间

	public OrderConcise() {
	}

	public OrderConcise(long shipperKey, long receiveKey, long conveyKey, Date createTime) {
		this.shipperKey = shipperKey;
		this.receiveKey = receiveKey;
		this.conveyKey = conveyKey;
		this.createTime = createTime;
	}

	public CompanyConcise getShipper() {
		return shipper;
	}

	public void setShipper(CompanyConcise shipper) {
		this.shipper = shipper;
	}

	public CompanyConcise getReceive() {
		return receive;
	}

	public void setReceive(CompanyConcise receive) {
		this.receive = receive;
	}

	public CompanyConcise getConvey() {
		return convey;
	}

	public void setConvey(CompanyConcise convey) {
		this.convey = convey;
	}

	public long getShipperKey() {
		return shipperKey;
	}

	public void setShipperKey(long shipperKey) {
		this.shipperKey = shipperKey;
	}

	public long getReceiveKey() {
		return receiveKey;
	}

	public void setReceiveKey(long receiveKey) {
		this.receiveKey = receiveKey;
	}

	public long getConveyKey() {
		return conveyKey;
	}

	public void setConveyKey(long conveyKey) {
		this.conveyKey = conveyKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
