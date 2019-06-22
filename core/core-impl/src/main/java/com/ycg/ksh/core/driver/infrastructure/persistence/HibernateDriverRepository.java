package com.ycg.ksh.core.driver.infrastructure.persistence;

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.driver.domain.model.Driver;
import com.ycg.ksh.core.driver.domain.model.DriverAwaitInfo;
import com.ycg.ksh.core.driver.domain.model.DriverCar;
import com.ycg.ksh.core.driver.domain.repos.DriverRepository;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 司机资源库Hibernate实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
@Repository
public class HibernateDriverRepository extends HibernateRepository<Driver, Long> implements DriverRepository {

    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public Driver findByKey(Long driverKey) {
        return session().get(Driver.class, driverKey);
    }

    @Override
    public boolean existByDriverKey(Long driverKey) {
        return Optional.ofNullable(
                session().createCriteria(Driver.class)
                        .add(Restrictions.eq("identify", driverKey))
                        .setProjection(Projections.rowCount())
                        .uniqueResult()
        ).map(o-> (Long) o).orElse(0L) > 0;
    }

    @Override
    public boolean existByPhnoe(String phnoe, Long driverKey) {
        return Optional.ofNullable(
                session().createCriteria(Driver.class)
                        .add(Restrictions.eq("phone", phnoe))
                        .add(Restrictions.ne("identify", driverKey))
                        .setProjection(Projections.rowCount())
                        .uniqueResult()
        ).map(o-> (Long) o).orElse(0L) > 0;
    }

    @Override
    public Collection<DriverCar> listCarByLicense(Collection<String> licenses) {
        return Optional.ofNullable(
                session().createCriteria(DriverCar.class)
                        .add(Restrictions.in("license", licenses))
                        .list()
        ).map(l -> (Collection<DriverCar>) l).orElse(Collections.emptyList());
    }
}
