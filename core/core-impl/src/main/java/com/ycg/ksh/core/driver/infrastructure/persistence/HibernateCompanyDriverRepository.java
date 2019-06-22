package com.ycg.ksh.core.driver.infrastructure.persistence;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.driver.domain.model.CompanyDriver;
import com.ycg.ksh.core.driver.domain.model.CompanyDriverCar;
import com.ycg.ksh.core.driver.domain.repos.CompanyDriverRepository;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 司机资源库Hibernate实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
@Repository
public class HibernateCompanyDriverRepository extends HibernateRepository<CompanyDriver, Long> implements CompanyDriverRepository {

    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public CompanyDriver findByKey(Long driverKey) {
        return session().get(CompanyDriver.class, driverKey);
    }

    @Override
    public CompanyDriver findByDriverKey(Long companyKey, Long driverKey) {
        return (CompanyDriver) Optional.ofNullable(
                session().createCriteria(CompanyDriver.class)
                        .add(Restrictions.eq("compaynKey", companyKey))
                        .add(Restrictions.eq("driverKey", driverKey))
                        .uniqueResult()
        ).orElse(null);
    }

    @Override
    public boolean existByPhnoe(String phnoe, Long driverKey) {
        return Optional.ofNullable(
                session().createCriteria(CompanyDriver.class)
                        .add(Restrictions.eq("phone", phnoe))
                        .add(Restrictions.ne("identify", driverKey))
                        .setProjection(Projections.rowCount())
                        .uniqueResult()
        ).map(o-> (Long) o).orElse(0L) > 0;
    }

    @Override
    public Collection<CompanyDriverCar> listCarByLicense(Collection<String> licenses) {
        return Optional.ofNullable(
                session().createCriteria(CompanyDriverCar.class)
                        .add(Restrictions.in("license", licenses))
                        .list()
        ).map(l -> (Collection<CompanyDriverCar>) l).orElse(Collections.emptyList());
    }

    @Override
    public Page<CompanyDriver> searchCompanyDriver(CompanyDriverCondition condition, int num, int size) {
        Collection<Criterion> criterions = sigleTableCondition(condition);
        Long total = Optional.ofNullable(
                applyCondition(
                        session().createCriteria(CompanyDriver.class)
                                .setProjection(Projections.rowCount())
                        , criterions
                ).uniqueResult()
        ).map(o-> (Long) o).orElse(0L);

        Collection<CompanyDriver> collection = Collections.emptyList();
        if(total >0){
            collection = Optional.ofNullable(
                    applyCondition(session().createCriteria(CompanyDriver.class), criterions)
                            .setFirstResult((num - 1) * size)
                            .setMaxResults(size)
                            .addOrder(Order.asc("status"))
                            .addOrder(Order.desc("relationTime"))
                            .list()
            ).map(l-> ( (Collection<CompanyDriver>) l)).orElse(Collections.emptyList());
        }
        return new Page<CompanyDriver>(num, size, total, collection);
    }

    private Collection<Criterion> sigleTableCondition(CompanyDriverCondition condition){
        Collection<Criterion> criterions = new ArrayList<Criterion>();
        if(StringUtils.isNotBlank(condition.likeString)){
            criterions.add(Restrictions.or(Restrictions.like("phone", condition.likeString, MatchMode.ANYWHERE), Restrictions.like("name", condition.likeString, MatchMode.ANYWHERE)));
        }
        if(condition.status != null && condition.status > 0){
            criterions.add(Restrictions.eq("status", condition.status));
        }
        return criterions;
    }
}
