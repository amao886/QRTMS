package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;

/**
 * 纸质回单状态修改
 *
 * @author wangke
 * @create 2018-03-05 15:18
 **/
public class MergeReceiptStatus extends BaseEntity {

    private Integer userId;

    private Integer receiptStatus;

    private Collection<Integer> waybills;

    private String batchNumber;

    private String deliveryNumber;

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Collection<Integer> getWaybills() {
        return waybills;
    }

    public void setWaybills(Collection<Integer> waybills) {
        this.waybills = waybills;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
