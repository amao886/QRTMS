package com.ycg.ksh.core.scene.infrastructure.persistence;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

/**
 * 现场管理查询整合
 *
 * @author: wangke
 * @create: 2018-12-13 11:13
 **/

public class VehicleRegistrationCondition extends BaseEntity {

    private String likeString; //模糊疲匹配 送货单号/物流商/收货客户
    private Long shipperKey;//货主编号

    private Collection<Long> companyKeys;//物流商和收货客户模糊查询的编号

    private Integer status; //到车状态

    private Integer arrivalType;//到车备注状态

    private LocalDateTime minCollectTime;
    private LocalDateTime maxCollectTime;

    public static VehicleRegistrationCondition build(Long shipperKey, String likeString, Integer status, Integer arrivalType, LocalDate localDate, Collection<Long> companyKeys) {

        VehicleRegistrationCondition condition = new VehicleRegistrationCondition();

        condition.shipperKey = shipperKey;
        condition.likeString = likeString;
        condition.status = status;
        condition.arrivalType = arrivalType;
        condition.companyKeys = companyKeys;


        if(localDate != null){
            LocalDateTime localDateTime =  localDate.atTime(0,0,0);
            condition.minCollectTime = localDateTime.with(LocalTime.MIN);
            condition.maxCollectTime = localDateTime.with(LocalTime.MAX);
        }
        return condition;
    }

    public Long getShipperKey() {
        return shipperKey;
    }

    public void setShipperKey(Long shipperKey) {
        this.shipperKey = shipperKey;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Collection<Long> getCompanyKeys() {
        return companyKeys;
    }

    public void setCompanyKeys(Collection<Long> companyKeys) {
        this.companyKeys = companyKeys;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getArrivalType() {
        return arrivalType;
    }

    public void setArrivalType(Integer arrivalType) {
        this.arrivalType = arrivalType;
    }

    public LocalDateTime getMinCollectTime() {
        return minCollectTime;
    }

    public void setMinCollectTime(LocalDateTime minCollectTime) {
        this.minCollectTime = minCollectTime;
    }

    public LocalDateTime getMaxCollectTime() {
        return maxCollectTime;
    }

    public void setMaxCollectTime(LocalDateTime maxCollectTime) {
        this.maxCollectTime = maxCollectTime;
    }
}
