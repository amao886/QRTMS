package com.ycg.ksh.core.scene.search.dto;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.core.common.domain.CustomerIdentify;

import java.time.LocalDateTime;

/**
 * 现场车辆确认运单信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/13 0013
 */
public class VehicleConfirmDto extends BaseEntity {

    private Long orderKey;//运单编号
    private String deliveryNo;//送货单号
    private CustomerIdentify shipper;//货主
    private CustomerIdentify receive;//收货客户
    private CustomerIdentify convey;//承运商
    private LocalDateTime collectTime;//要求提货时间
    private String driverName; //司机名称
    private String driverContact; //司机电话
    private String license; //车牌号

    private Integer vehicleStatus;//到车状态
    private LocalDateTime arrivalTime;//到车时间
    private Integer confirmStatus;//到车确认状态
    private String confirmDriverName; //到车确认司机名称
    private String confirmDriverContact; //到车确认司机电话
    private String confirmLicense; //到车确认车牌号

    public VehicleConfirmDto() {
    }

    public Long getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public CustomerIdentify getShipper() {
        return shipper;
    }

    public void setShipper(CustomerIdentify shipper) {
        this.shipper = shipper;
    }

    public CustomerIdentify getReceive() {
        return receive;
    }

    public void setReceive(CustomerIdentify receive) {
        this.receive = receive;
    }

    public CustomerIdentify getConvey() {
        return convey;
    }

    public void setConvey(CustomerIdentify convey) {
        this.convey = convey;
    }

    public LocalDateTime getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
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

    public Integer getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(Integer vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getConfirmDriverName() {
        return confirmDriverName;
    }

    public void setConfirmDriverName(String confirmDriverName) {
        this.confirmDriverName = confirmDriverName;
    }

    public String getConfirmDriverContact() {
        return confirmDriverContact;
    }

    public void setConfirmDriverContact(String confirmDriverContact) {
        this.confirmDriverContact = confirmDriverContact;
    }

    public String getConfirmLicense() {
        return confirmLicense;
    }

    public void setConfirmLicense(String confirmLicense) {
        this.confirmLicense = confirmLicense;
    }
}
