package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 收支
 *
 * @author: wangke
 * @create: 2019-01-04 16:18
 **/

public class IncomeRecordDto extends BaseEntity {

    private Long systemId; //订单编号

    //发货时间
    private LocalDateTime deliveryDate;

    //发货人姓名
    private String deliveryCustomer;

    //收货客户
    private String receiveCustomer;

    //送货单号
    private String deliveryNo;

    //总重量
    private double totalWeight;

    //总体积
    private double totalVolume;

    //总数量
    private Integer totalQuantity;

    //运输应收
    private double transportReceivable;

    //其他应收
    private double otherTotalReceivable;

    //应收合计
    private double totalReceivable;

    //确认状态 0 待确认， 1 已确认
    private Integer confirmState;

    //备注
    private String otherReceivableRemark;

    //创建时间
    private LocalDateTime createTime;

    public IncomeRecordDto() {
    }

    public IncomeRecordDto(Long systemId, String deliveryNo, LocalDateTime deliveryDate) {
        this.systemId = systemId;
        this.deliveryDate = deliveryDate;
        this.deliveryNo = deliveryNo;
    }

    public IncomeRecordDto(Long systemId, double transportReceivable, double otherTotalReceivable, String otherReceivableRemark) {
        this.systemId = systemId;
        this.transportReceivable = transportReceivable;
        this.otherTotalReceivable = otherTotalReceivable;
        this.otherReceivableRemark = otherReceivableRemark;
    }

    public void append(int count, double unitWeight, double unitVolume) {
        if (this.totalQuantity == null) {
            this.totalQuantity = 0;
        }
        this.totalQuantity = this.totalQuantity + count;
        this.totalWeight = this.totalWeight + count * unitWeight;
        this.totalVolume = this.totalVolume + count * unitVolume;
    }

    public void append(double transportReceivable) {
        this.transportReceivable = this.transportReceivable + transportReceivable;
    }


    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryCustomer() {
        return deliveryCustomer;
    }

    public void setDeliveryCustomer(String deliveryCustomer) {
        this.deliveryCustomer = deliveryCustomer;
    }

    public String getReceiveCustomer() {
        return receiveCustomer;
    }

    public void setReceiveCustomer(String receiveCustomer) {
        this.receiveCustomer = receiveCustomer;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalReceivable() {
        return totalReceivable;
    }

    public void setTotalReceivable(double totalReceivable) {
        this.totalReceivable = totalReceivable;
    }

    public Integer getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(Integer confirmState) {
        this.confirmState = confirmState;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public double getTransportReceivable() {
        return transportReceivable;
    }

    public void setTransportReceivable(double transportReceivable) {
        this.transportReceivable = transportReceivable;
    }

    public double getOtherTotalReceivable() {
        return otherTotalReceivable;
    }

    public void setOtherTotalReceivable(double otherTotalReceivable) {
        this.otherTotalReceivable = otherTotalReceivable;
    }

    public String getOtherReceivableRemark() {
        return otherReceivableRemark;
    }

    public void setOtherReceivableRemark(String otherReceivableRemark) {
        this.otherReceivableRemark = otherReceivableRemark;
    }
}
