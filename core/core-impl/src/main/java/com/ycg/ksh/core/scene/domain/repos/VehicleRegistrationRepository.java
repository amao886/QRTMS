package com.ycg.ksh.core.scene.domain.repos;

import com.ycg.ksh.core.common.domain.DomainRepository;
import com.ycg.ksh.core.scene.domain.model.VehicleRegistration;

/**
 * 派车资源库
 *
 * @Author: wangke
 * @Date: 2018/12/12
 */
public interface VehicleRegistrationRepository extends DomainRepository<VehicleRegistration, Long>  {

    /**
     * 查询详情
     *
     * @param orderKey
     * @return
     */
    VehicleRegistration getVehicleRegistration(Long orderKey);

}
