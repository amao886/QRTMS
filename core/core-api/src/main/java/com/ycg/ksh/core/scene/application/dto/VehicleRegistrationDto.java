package com.ycg.ksh.core.scene.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 现场管理实体
 *
 * @author: wangke
 * @create: 2018-12-12 13:09
 **/

public class VehicleRegistrationDto extends BaseEntity {

    private Long orderKey; //订单编号

    private ArrivalsCarDto arrivalsCarDto;

    private DeliveryCarDto deliveryCarDto;

    private LocalDateTime arrivalTime; //到达时间

    public VehicleRegistrationDto() {
    }

    public VehicleRegistrationDto(Long orderKey) {
        this.orderKey = orderKey;
    }

    public Long getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }


    public ArrivalsCarDto getArrivalsCarDto() {
        return arrivalsCarDto;
    }

    public void setArrivalsCarDto(ArrivalsCarDto arrivalsCarDto) {
        this.arrivalsCarDto = arrivalsCarDto;
    }

    public DeliveryCarDto getDeliveryCarDto() {
        return deliveryCarDto;
    }

    public void setDeliveryCarDto(DeliveryCarDto deliveryCarDto) {
        this.deliveryCarDto = deliveryCarDto;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = Optional.ofNullable(arrivalTime).orElse(LocalDateTime.now());
    }
}
