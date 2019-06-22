package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/23
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 任务单简洁
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/23
 */
public class WaybillSimple extends BaseEntity {

    private Integer id;//编号
    private String deliveryNumber;//送货单号
    private String barcode;//任务单号
    private String receiveName;//收货客户
    private String receiveAddress;//收货地址
    private Integer fettle;//状态
    private boolean allowEdit;//是否可以编辑
    private Integer groupId;//项目组

    public WaybillSimple() {}

    public WaybillSimple(Integer id, Integer groupId, String deliveryNumber, String barcode, Integer fettle) {
        this.id = id;
        this.groupId = groupId;
        this.deliveryNumber = deliveryNumber;
        this.barcode = barcode;
        this.fettle = fettle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }
}
