package com.ycg.ksh.entity.service.plan;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;

import java.util.Date;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */
public class PlanTemplate extends BaseEntity {

    private Long uniqueKey;//唯一编号
    private String orderNo;//订单好
    private String planNo;//计划单号
    private Long shipperKey;
    private Long receiveKey;
    private Long conveyKey;
    private String shipperName;//发货方名称
    private String receiveName;//收货方客户
    private String conveyName;//物流商名称
    private String receiverName;//收货人
    private String receiverContact;//联系方式
    private String receiveAddress;//收货地址
    private String remark;
    private Date deliveryTime;//发货日期
    private Date arrivalTime;//要求到货时间
    private Date collectTime;//要求提货时间
    private String transportRoute;//运输路线

    private CustomerConcise shipper;
    private CustomerConcise receive;
    private CustomerConcise convey;

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

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
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

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getTransportRoute() {
        return transportRoute;
    }

    public void setTransportRoute(String transportRoute) {
        this.transportRoute = transportRoute;
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
}
