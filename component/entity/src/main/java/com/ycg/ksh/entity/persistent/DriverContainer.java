package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`driver_container_tab`")
public class DriverContainer extends BaseEntity {

	private static final long serialVersionUID = 957073880975933382L;

	@Id
	@Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	/**
     * 二维码
     */
	@Column(name = "`barcode`")
    private String barcode;

    /**
     * 装车人
     */
    @Column(name = "`load_id`")
    private Integer loadId;

    /**
     * 装车时间
     */
    @Column(name = "`load_time`")
    private Date loadTime;

    /**
     * 卸货人
     */
    @Column(name = "`unload_id`")
    private Integer unloadId;

    /**
     * 卸货时间
     */
    @Column(name = "`unload_time`")
    private Date unloadTime;

    /**
     * 送货单号
     */
    @Column(name = "`delivery_number`")
    private String deliveryNumber;

    /**
     * 收货客户名称/公司名称
     */
    @Column(name = "`receiver_name`")
    private String receiverName;
    /**
     * 收货联系人
     */
    @Column(name = "`contact_name`")
    private String contactName;

    /**
     * 收货联系人电话
     */
    @Column(name = "`contact_number`")
    private String contactNumber;

    /**
     * 收货地址
     */
    @Column(name = "`receive_address`")
    private String receiveAddress;

    /**
     * 经度
     */
    @Column(name = "`longitude`")
    private String longitude;

    /**
     * 纬度
     */
    @Column(name = "`latitude`")
    private String latitude;
    /**
     * 10:未绑定,20:已经绑定
     */
    @Column(name = "`bind_status`")
    private Integer bindStatus;
    /**
     * 0:未卸车,1:卸车
     */
    @Column(name = "`unload`")
    private Boolean unload;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
     * 获取二维码
     *
     * @return barcode - 二维码
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 设置二维码
     *
     * @param barcode 二维码
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 获取装车人
     *
     * @return load_id - 装车人
     */
    public Integer getLoadId() {
        return loadId;
    }

    /**
     * 设置装车人
     *
     * @param loadId 装车人
     */
    public void setLoadId(Integer loadId) {
        this.loadId = loadId;
    }

    /**
     * 获取装车时间
     *
     * @return load_time - 装车时间
     */
    public Date getLoadTime() {
        return loadTime;
    }

    /**
     * 设置装车时间
     *
     * @param loadTime 装车时间
     */
    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    /**
     * 获取卸货人
     *
     * @return unload_id - 卸货人
     */
    public Integer getUnloadId() {
        return unloadId;
    }

    /**
     * 设置卸货人
     *
     * @param unloadId 卸货人
     */
    public void setUnloadId(Integer unloadId) {
        this.unloadId = unloadId;
    }

    /**
     * 获取卸货时间
     *
     * @return unload_time - 卸货时间
     */
    public Date getUnloadTime() {
        return unloadTime;
    }

    /**
     * 设置卸货时间
     *
     * @param unloadTime 卸货时间
     */
    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    /**
     * 获取送货单号
     *
     * @return delivery_number - 送货单号
     */
    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    /**
     * 设置送货单号
     *
     * @param deliveryNumber 送货单号
     */
    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    /**
     * 获取收货联系人
     *
     * @return contact_name - 收货联系人
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置收货联系人
     *
     * @param contactName 收货联系人
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取收货联系人电话
     *
     * @return contact_number - 收货联系人电话
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * 设置收货联系人电话
     *
     * @param contactNumber 收货联系人电话
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * 获取收货地址
     *
     * @return receive_address - 收货地址
     */
    public String getReceiveAddress() {
        return receiveAddress;
    }

    /**
     * 设置收货地址
     *
     * @param receiveAddress 收货地址
     */
    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
    public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
     * 获取10:未绑定,20:已经绑定
     *
     * @return bind_status - 10:未绑定,20:已经绑定
     */
    public Integer getBindStatus() {
        return bindStatus;
    }

    /**
     * 设置10:未绑定,20:已经绑定
     *
     * @param bindStatus 10:未绑定,20:已经绑定
     */
    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }

	public Boolean getUnload() {
		return unload;
	}

	public void setUnload(Boolean unload) {
		this.unload = unload;
	}

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}