package com.ycg.ksh.core.scene.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 派车信息传输对象
 *
 * @author: wangke
 * @create: 2018-12-11 14:15
 **/

public class DeliveryCarDto extends BaseEntity {

    private String deliveryNo; //配送单号

    private String driverName; //司机名称

    private String driverContact; //司机电话

    private String license; //车牌号

    public DeliveryCarDto(String deliveryNo, String driverName, String driverContact, String license) {
        this.deliveryNo = deliveryNo;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.license = license;
    }

    public DeliveryCarDto() {
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
