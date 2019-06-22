package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;

import java.util.Collection;
import java.util.Date;

/**
 * 简单订单信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */
public class OrderTemplate extends BaseEntity {


    private CustomerConcise shipper;
    private CustomerConcise receive;
    private CustomerConcise convey;

    private Long uniqueKey;
    private String orderNo;
    private String deliveryNo;
    private Date deliveryTime;
    private Date receiveTime;
    private Date collectTime;
    private Long shipperKey;
    private Long receiveKey;
    private Long conveyKey;
    private String shipperName;
    private String receiveName;
    private String conveyName;
    private String receiverName;
    private String receiverContact;
    private String receiveAddress;
    private String remark;
    private Date arrivalTime;


    private OrderExtra orderExtra;
    private Collection<OrderCommodity> commodities;
    private Collection<CustomData> customDatas;

    public Long getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(Long uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = StringUtils.trim(shipperName);
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = StringUtils.trim(receiveName);
    }

    public String getConveyName() {
        return conveyName;
    }

    public void setConveyName(String conveyName) {
        this.conveyName = StringUtils.trim(conveyName);
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Long getShipperKey() {
        return shipperKey;
    }

    public void setShipperKey(Long shipperKey) {
        this.shipperKey = shipperKey;
    }

    public Long getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(Long receiveKey) {
        this.receiveKey = receiveKey;
    }

    public Long getConveyKey() {
        return conveyKey;
    }

    public void setConveyKey(Long conveyKey) {
        this.conveyKey = conveyKey;
    }

    public CustomerConcise getShipper() {
        return shipper;
    }

    public void setShipper(CustomerConcise shipper) {
        this.shipper = shipper;
    }

    public CustomerConcise getReceive() {
        return receive;
    }

    public void setReceive(CustomerConcise receive) {
        this.receive = receive;
    }

    public CustomerConcise getConvey() {
        return convey;
    }

    public void setConvey(CustomerConcise convey) {
        this.convey = convey;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public OrderExtra getOrderExtra() {
        return orderExtra;
    }

    public void setOrderExtra(OrderExtra orderExtra) {
        this.orderExtra = orderExtra;
    }

    public Collection<OrderCommodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(Collection<OrderCommodity> commodities) {
        this.commodities = commodities;
    }

    public Collection<CustomData> getCustomDatas() {
        return customDatas;
    }

    public void setCustomDatas(Collection<CustomData> customDatas) {
        this.customDatas = customDatas;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
