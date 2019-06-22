package com.ycg.ksh.core.scene.infrastructure.persistence;

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.scene.domain.model.VehicleRegistration;
import com.ycg.ksh.core.scene.domain.repos.VehicleRegistrationRepository;
import org.springframework.stereotype.Repository;

/**
 * 派车资源库实现
 *
 * @author: wangke
 * @create: 2018-12-12 13:22
 **/
@Repository
public class HibernateVehicleRegistrationRepository extends HibernateRepository<VehicleRegistration, Long> implements VehicleRegistrationRepository {

    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public VehicleRegistration findByKey(Long key) {
        return session().get(VehicleRegistration.class, key);
    }

    @Override
    public void save(VehicleRegistration vehicleRegistration) {
        session().save(vehicleRegistration);
    }

    @Override
    public VehicleRegistration getVehicleRegistration(Long orderKey) {
        return session().get(VehicleRegistration.class, orderKey);
    }

}
