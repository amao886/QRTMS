package com.ycg.ksh.core.scene.application.transform;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.domain.CustomerIdentify;
import com.ycg.ksh.core.scene.application.dto.ArrivalsCarDto;
import com.ycg.ksh.core.scene.application.dto.DeliveryCarDto;
import com.ycg.ksh.core.scene.application.dto.SceneConfirmDto;
import com.ycg.ksh.core.scene.application.dto.VehicleRegistrationDto;
import com.ycg.ksh.core.scene.domain.model.ArrivalsCar;
import com.ycg.ksh.core.scene.domain.model.DeliveryCar;
import com.ycg.ksh.core.scene.domain.model.VehicleRegistration;
import com.ycg.ksh.core.scene.search.dto.VehicleConfirmDto;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 转换类
 *
 * @author: wangke
 * @create: 2018-12-13 15:53
 **/

public class RegistrationDtoTransformer {


    private static final ThreadLocal<Map<Long, String>> cache = ThreadLocal.withInitial(HashMap::new);

    public ArrivalsCar transformArrivalsCar(ArrivalsCarDto dto) {
        return Optional.ofNullable(dto).map(d -> new ArrivalsCar(d.getInDriverName(), d.getInDriverContact(),
                d.getInLicense(), d.getArrivalType())).orElse(null);
    }

    public DeliveryCar transformDeliveryCar(DeliveryCarDto dto) {
        return Optional.ofNullable(dto).map(d -> new DeliveryCar(d.getDeliveryNo(), d.getDriverName(), d.getDriverContact(), d.getLicense())).orElse(null);
    }

    public ArrivalsCarDto transformArrivalsCarDto(ArrivalsCar arrivalsCar) {
        return Optional.ofNullable(arrivalsCar).map(d -> new ArrivalsCarDto(d.getInDriverName(), d.getInDriverContact(),
                d.getInLicense(), d.getArrivalType())).orElse(null);
    }

    public DeliveryCarDto transformDeliveryDto(DeliveryCar deliveryCar) {
        return Optional.ofNullable(deliveryCar).map(d -> new DeliveryCarDto(d.getDeliveryNo(), d.getDriverName(),
                d.getDriverContact(), d.getLicense())).orElse(null);
    }

    public VehicleRegistrationDto VehicleRegistrationFromDto(VehicleRegistration vehicleRegistration) {
        VehicleRegistrationDto dto = new VehicleRegistrationDto();
        dto.setOrderKey(vehicleRegistration.getOrderKey());
        dto.setArrivalsCarDto(transformArrivalsCarDto(vehicleRegistration.getArrivalsCar()));
        dto.setDeliveryCarDto(transformDeliveryDto(vehicleRegistration.getDeliveryCar()));
        return dto;
    }

    public Collection<SceneConfirmDto> transformVehicleConfirmDto(Collection<VehicleConfirmDto> dtos, Function<CustomerIdentify, String> function) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            return dtos.stream().map(v -> {
                SceneConfirmDto scd = new SceneConfirmDto();
                try {
                    PropertyUtils.copyProperties(scd, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                scd.setShipperName(customerName(v.getShipper(), function));
                scd.setReceiveName(customerName(v.getReceive(), function));
                scd.setConveyName(customerName(v.getConvey(), function));

                return scd;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private String customerName(CustomerIdentify identify, Function<CustomerIdentify, String> function) {
        String name = cache.get().getOrDefault(identify.getIdentify(), function.apply(identify));
        if (StringUtils.isNotBlank(name)) {
            cache.get().put(identify.getIdentify(), name);
        }
        return name;
    }


}
