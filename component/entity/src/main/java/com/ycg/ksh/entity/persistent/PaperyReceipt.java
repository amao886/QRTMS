package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 纸质回单实体类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:32:16
 */
@Table(name = "waybill_receipt_view")
public class PaperyReceipt extends BaseEntity{
	
	private static final long serialVersionUID = 4918466819712555813L;

	/**
     * 运单主键
     */
    @Column(name = "`waybill_id`")
    private Integer waybillId;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 送货单号
     */
    @Column(name = "`delivery_number`")
    private String deliveryNumber;

    /**
     * 绑定时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 要求到货时间
     */
    @Column(name = "`arrivaltime`")
    private Date arrivaltime;

    /**
     * 实际达到时间
     */
    @Column(name = "`actual_arrival_time`")
    private Date actualArrivalTime;

    /**
     * 回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    @Column(name = "`receipt_status`")
    private Integer receiptStatus;

    /**
     * group主键
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 数量
     */
    @Column(name = "`number`")
    private Integer number;

    /**
     * 重量
     */
    @Column(name = "`weight`")
    private Double weight;

    /**
     * 条码编号
     */
    @Column(name = "`barcode`")
    private String barcode;

    /**
     * 组名称
     */
    @Column(name = "`group_name`")
    private String groupName;

    /**
     * 收货客户名称（公司名称）
     */
    @Column(name = "`company_name`")
    private String companyName;

    /**
     * 联系人
     */
    @Column(name = "`contacts`")
    private String contacts;

    /**
     * 联系电话
     */
    @Column(name = "`contact_number`")
    private String contactNumber;

    /**
     * 完整地址（省+市+区+详细地址）
     */
    @Column(name = "`fullAddress`")
    private String fulladdress;

    /**
     * 批次号
     */
    @Column(name = "`batch_number`")
    private String batchNumber;
    
    /**
     * 地址（省+市+区）
     */
    @Column(name = "`address`")
    private String address;

    /**
     * 体积
     */
    @Column(name = "`volume`")
    private Double volume;


    /**
     * 当前用户id
     */
    @Transient
    private Integer userId;
    
    /**
     * 获取运单主键
     *
     * @return waybill_id - 运单主键
     */
    public Integer getWaybillId() {
        return waybillId;
    }

    /**
     * 设置运单主键
     *
     * @param waybillId 运单主键
     */
    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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
     * 获取绑定时间
     *
     * @return createtime - 绑定时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置绑定时间
     *
     * @param createtime 绑定时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取要求到货时间
     *
     * @return arrivaltime - 要求到货时间
     */
    public Date getArrivaltime() {
        return arrivaltime;
    }

    /**
     * 设置要求到货时间
     *
     * @param arrivaltime 要求到货时间
     */
    public void setArrivaltime(Date arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    /**
     * 获取实际达到时间
     *
     * @return actual_arrival_time - 实际达到时间
     */
    public Date getActualArrivalTime() {
        return actualArrivalTime;
    }

    /**
     * 设置实际达到时间
     *
     * @param actualArrivalTime 实际达到时间
     */
    public void setActualArrivalTime(Date actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    /**
     * 获取回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     *
     * @return receipt_status - 回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    /**
     * 设置回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     *
     * @param receiptStatus 回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    /**
     * 获取group主键
     *
     * @return groupid - group主键
     */
    public Integer getGroupid() {
        return groupid;
    }

    /**
     * 设置group主键
     *
     * @param groupid group主键
     */
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    /**
     * 获取重量
     *
     * @return weight - 重量
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * 设置重量
     *
     * @param weight 重量
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 获取条码编号
     *
     * @return barcode - 条码编号
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 设置条码编号
     *
     * @param barcode 条码编号
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 获取组名称
     *
     * @return group_name - 组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置组名称
     *
     * @param groupName 组名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取收货客户名称（公司名称）
     *
     * @return company_name - 收货客户名称（公司名称）
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置收货客户名称（公司名称）
     *
     * @param companyName 收货客户名称（公司名称）
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取联系人
     *
     * @return contacts - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * 获取联系电话
     *
     * @return contact_number - 联系电话
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * 设置联系电话
     *
     * @param contactNumber 联系电话
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * 获取完整地址（省+市+区+详细地址）
     *
     * @return fullAddress - 完整地址（省+市+区+详细地址）
     */
    public String getFulladdress() {
        return fulladdress;
    }

    /**
     * 设置完整地址（省+市+区+详细地址）
     *
     * @param fulladdress 完整地址（省+市+区+详细地址）
     */
    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    /**
     * 获取批次号
     *
     * @return batch_number - 批次号
     */
    public String getBatchNumber() {
        return batchNumber;
    }

    /**
     * 设置批次号
     *
     * @param batchNumber 批次号
     */
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }
}