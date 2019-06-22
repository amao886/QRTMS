package com.ycg.ksh.core.scene.domain.model;

import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.common.domain.Model;

/**
 * 派车信息值对象
 *
 * @author: wangke
 * @create: 2018-12-11 14:15
 **/

public class DeliveryCar extends Model {


    private String deliveryNo; //配送单号

    private String driverName; //司机名称

    private String driverContact; //司机电话

    private String license; //车牌号

    public DeliveryCar(String deliveryNo, String driverName, String driverContact, String license) {
        this.setDeliveryNo(deliveryNo);
        this.setDriverName(driverName);
        this.setDriverContact(driverContact);
        this.setLicense(license);
    }

    public DeliveryCar() {
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    private void setDeliveryNo(String deliveryNo) {
        Assert.notBlank(deliveryNo, "送货单号不能为空");
        this.deliveryNo = deliveryNo;
    }

    public String getDriverName() {
        return driverName;
    }

    private void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    private void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getLicense() {
        return license;
    }

    private void setLicense(String license) {
        this.license = license;
    }
}
