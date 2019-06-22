package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`t_complaint`")
public class Complaint extends BaseEntity {

    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 投诉人ID
     */
    @Column(name = "`COMPLAINANT`")
    private String complainant;

    /**
     * 投诉人电话
     */
    @Column(name = "`COMPLAINANT_NUMBER`")
    private String complainantNumber;

    /**
     * 时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 投诉内容
     */
    @Column(name = "`COMPLAINANT_CONTENT`")
    private String complainantContent;

    /**
     * 订单编号
     */
    @Column(name = "`ORDER_ID`")
    private Long orderId;

    /**
     * 发货方ID
     */
    @Column(name = "`SHIPPER_ID`")
    private Long shipperId;

    /**
     * 收货方ID
     */
    @Column(name = "`RECEIVE_ID`")
    private Long receiveId;

    /**
     * 承运商ID
     */
    @Column(name = "`CONVEY_ID`")
    private Long conveyId;

    /**
     * 收货方
     */
    @Column(name = "`RECEIVE_NAME`")
    private String receiveName;

    /**
     * 发货方
     */
    @Column(name = "`SHIPPER_NAME`")
    private String shipperName;

    /**
     * 承运商
     */
    @Column(name = "`CONVEY_NAME`")
    private String conveyName;

    public Complaint() {
    }

    public Complaint(String complainantContent) {
        this.complainantContent = complainantContent;
    }

    public Complaint(Long orderId) {
        this.orderId = orderId;
    }

    public Long getConveyId() {
        return conveyId;
    }

    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getConveyName() {
        return conveyName;
    }

    public void setConveyName(String conveyName) {
        this.conveyName = conveyName;
    }

    public Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * @return KEY
     */
    public Long getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取投诉人
     *
     * @return COMPLAINANT - 投诉人
     */
    public String getComplainant() {
        return complainant;
    }

    /**
     * 设置投诉人
     *
     * @param complainant 投诉人
     */
    public void setComplainant(String complainant) {
        this.complainant = complainant;
    }

    /**
     * 获取投诉人电话
     *
     * @return COMPLAINANT_NUMBER - 投诉人电话
     */
    public String getComplainantNumber() {
        return complainantNumber;
    }

    /**
     * 设置投诉人电话
     *
     * @param complainantNumber 投诉人电话
     */
    public void setComplainantNumber(String complainantNumber) {
        this.complainantNumber = complainantNumber;
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取投诉内容
     *
     * @return COMPLAINANT_CONTENT - 投诉内容
     */
    public String getComplainantContent() {
        return complainantContent;
    }

    /**
     * 设置投诉内容
     *
     * @param complainantContent 投诉内容
     */
    public void setComplainantContent(String complainantContent) {
        this.complainantContent = complainantContent;
    }
}