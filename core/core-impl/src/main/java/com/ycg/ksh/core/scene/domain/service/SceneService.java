package com.ycg.ksh.core.scene.domain.service;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.core.scene.domain.model.ArrivalsCar;
import com.ycg.ksh.core.scene.domain.model.DeliveryCar;

/**
 * 现场领域服务
 *
 * @Author: wangke
 * @Date: 2018/12/12
 */
public interface SceneService {

    /**
     * 到车登记
     *
     * @param arrivalsCar
     * @Author: wangke
     * @Date: 2018/12/12
     */
    void registration(Long orderKey, ArrivalsCar arrivalsCar, DeliveryCar deliveryCar) throws ParameterException,BusinessException;
}
