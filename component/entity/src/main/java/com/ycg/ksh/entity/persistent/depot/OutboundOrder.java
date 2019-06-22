package com.ycg.ksh.entity.persistent.depot;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`T_OUTBOUND_ORDER`")
public class OutboundOrder extends BaseEntity {

    @Id
    @Column(name = "`KEY`")
    private Long key;
    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;

    /**
     * 收货人ID
     */
    @Column(name = "`RECEIVE_ID`")
    private Long receiveId;

    /**
     * 发货人ID
     */
    @Column(name = "`SHIPPER_ID`")
    private Long shipperId;

    /**
     * 收货人名称
     */
    @Column(name = "`RECEIVE_NAME`")
    private String receiveName;

    /**
     * 发货人名称
     */
    @Column(name = "`SHIPPER_NAME`")
    private String shipperName;

    /**
     * 送货单号
     */
    @Column(name = "`DELIVERY_NO`")
    private String deliveryNo;

    /**
     * 发货日期
     */
    @Column(name = "`DELIVERY_TIME`")
    private Date deliveryTime;

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

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    /**
     * 获取收货人ID
     *
     * @return RECEIVE_ID - 收货人ID
     */
    public Long getReceiveId() {
        return receiveId;
    }

    /**
     * 设置收货人ID
     *
     * @param receiveId 收货人ID
     */
    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    /**
     * 获取发货人ID
     *
     * @return SHIPPER_ID - 发货人ID
     */
    public Long getShipperId() {
        return shipperId;
    }

    /**
     * 设置发货人ID
     *
     * @param shipperId 发货人ID
     */
    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    /**
     * 获取收货人名称
     *
     * @return RECEIVE_NAME - 收货人名称
     */
    public String getReceiveName() {
        return receiveName;
    }

    /**
     * 设置收货人名称
     *
     * @param receiveName 收货人名称
     */
    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    /**
     * 获取发货人名称
     *
     * @return SHIPPER_NAME - 发货人名称
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     * 设置发货人名称
     *
     * @param shipperName 发货人名称
     */
    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    /**
     * 获取送货单号
     *
     * @return DELIVERY_NO - 送货单号
     */
    public String getDeliveryNo() {
        return deliveryNo;
    }

    /**
     * 设置送货单号
     *
     * @param deliveryNo 送货单号
     */
    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    /**
     * 获取发货日期
     *
     * @return DELIVERY_TIME - 发货日期
     */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * 设置发货日期
     *
     * @param deliveryTime 发货日期
     */
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}