package com.ycg.ksh.core.scene.domain.service;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.scene.domain.model.ArrivalsCar;
import com.ycg.ksh.core.scene.domain.model.DeliveryCar;
import com.ycg.ksh.core.scene.domain.model.VehicleRegistration;
import org.springframework.stereotype.Service;

/**
 * 现场领域服务实现
 *
 * @author: wangke
 * @create: 2018-12-12 13:24
 **/
@Service
public class SceneServiceImpl implements SceneService {


    @Override
    public void registration(Long orderKey, ArrivalsCar arrivalsCar, DeliveryCar deliveryCar) throws ParameterException,BusinessException {
        new VehicleRegistration().save(orderKey, arrivalsCar, deliveryCar);
    }
}
