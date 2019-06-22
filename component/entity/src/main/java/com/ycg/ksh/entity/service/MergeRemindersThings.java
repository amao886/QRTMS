package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.RemindersThings;

/**
 * 催办事项整合类
 *
 * @author wangke
 * @create 2018-02-28 13:32
 **/
public class MergeRemindersThings extends RemindersThings {

    private Integer waybillKey;//任务单编号
    private String barcode;
    private String conveyanceNumber;//运单号
    private String deliveryNumber;//送货单号
    private String loaction;//最新位置
    private Integer groupKey;//项目组
    private Boolean haveChild;//是否有子运单

    private Integer ownerKey;//承运人
    private AssociateUser owner;

    private boolean forward;

    public Integer getWaybillKey() {
        return waybillKey;
    }

    public void setWaybillKey(Integer waybillKey) {
        this.waybillKey = waybillKey;
    }

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction;
    }

    public Integer getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(Integer ownerKey) {
        this.ownerKey = ownerKey;
    }

    public AssociateUser getOwner() {
        return owner;
    }

    public void setOwner(AssociateUser owner) {
        this.owner = owner;
    }

    public String getConveyanceNumber() {
        return conveyanceNumber;
    }

    public void setConveyanceNumber(String conveyanceNumber) {
        this.conveyanceNumber = conveyanceNumber;
    }

    public Integer getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(Integer groupKey) {
        this.groupKey = groupKey;
    }

    public Boolean getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
