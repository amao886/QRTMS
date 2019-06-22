package com.ycg.ksh.core.scene.domain.model;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 现场管理实体
 *
 * @author: wangke
 * @create: 2018-12-12 13:09
 **/

public class VehicleRegistration extends Model {

    private Long orderKey; //订单编号

    private ArrivalsCar arrivalsCar;

    private DeliveryCar deliveryCar;

    private LocalDateTime arrivalTime; //到达时间

    public VehicleRegistration() {
    }

    public void save(Long orderKey, ArrivalsCar arrivalsCar, DeliveryCar deliveryCar) {
        setOrderKey(orderKey);
        setArrivalsCar(arrivalsCar);
        setDeliveryCar(deliveryCar);
        setArrivalTime(LocalDateTime.now());
        Registrys.vehicleRegistrationRepository().save(this);
    }

    public Long getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Long orderKey) {
        if (orderKey != null && orderKey > 0) {
            this.orderKey = orderKey;
        } else {
            throw new BusinessException("订单信息不存在");
        }
    }

    public ArrivalsCar getArrivalsCar() {
        return arrivalsCar;
    }

    public void setArrivalsCar(ArrivalsCar arrivalsCar) {
        this.arrivalsCar = arrivalsCar;
    }

    public DeliveryCar getDeliveryCar() {
        return deliveryCar;
    }

    public void setDeliveryCar(DeliveryCar deliveryCar) {
        this.deliveryCar = deliveryCar;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = Optional.ofNullable(arrivalTime).orElse(LocalDateTime.now());
    }
}
