package com.ycg.ksh.core.driver.infrastructure.persistence;

import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.driver.domain.model.DriverAwaitInfo;
import com.ycg.ksh.core.driver.domain.repos.DriverAwaitInfoRepository;
import com.ycg.ksh.core.util.Constants;
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
public class HibernateDriverAwaitInfoRepository extends HibernateRepository<DriverAwaitInfo, Long> implements DriverAwaitInfoRepository {

    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public DriverAwaitInfo findByKey(Long waitKey) {
        return session().get(DriverAwaitInfo.class, waitKey);
    }

    @Override
    public Collection<DriverAwaitInfo> listEffective(Long driverKey) {
        return Optional.ofNullable(
                session().createCriteria(DriverAwaitInfo.class)
                        .add(Restrictions.eq("status", Constants.AWAIT_STAUS_EFFECTIVE))
                        .add(Restrictions.eq("driverKey", driverKey))
                        .list()
        ).map(l -> (Collection<DriverAwaitInfo>) l).orElse(Collections.emptyList());
    }

    @Override
    public Page<DriverAwaitInfo> searchAwaitInfo(DriverAwaitInfoCondition condition, int num, int size) {
        Collection<Criterion> criterions = sigleTableCondition(condition);
        Long total = Optional.ofNullable(
                applyCondition(
                        session().createCriteria(DriverAwaitInfo.class)
                                .setProjection(Projections.rowCount())
                        , criterions
                ).uniqueResult()
        ).map(o-> (Long) o).orElse(0L);
        Collection<DriverAwaitInfo> collection = Collections.emptyList();
        if(total >0){
            collection = Optional.ofNullable(
                    applyCondition(session().createCriteria(DriverAwaitInfo.class), criterions)
                            .setFirstResult((num - 1) * size)
                            .setMaxResults(size)
                            .addOrder(Order.asc("status"))
                            .addOrder(Order.desc("releaseTime"))
                            .list()
            ).map(l-> ( (Collection<DriverAwaitInfo>) l)).orElse(Collections.emptyList());
        }
        return new Page<DriverAwaitInfo>(num, size, total, collection);
    }

    private Collection<Criterion> sigleTableCondition(DriverAwaitInfoCondition condition){
        Collection<Criterion> criterions = new ArrayList<Criterion>();
        if(condition.driverKey != null && condition.driverKey > 0){
            criterions.add(Restrictions.eq("driverKey", condition.driverKey));
        }
        if(StringUtils.isNotBlank(condition.start)){
            criterions.add(Restrictions.like("route.start", condition.start, MatchMode.ANYWHERE));
        }
        if(condition.status != null && condition.status > 0){
            criterions.add(Restrictions.eq("status", condition.status));
        }
        if(condition.carType != null && condition.carType > 0){
            criterions.add(Restrictions.eq("car.type", condition.carType));
        }
        if(condition.length != null && condition.length > 0){
            criterions.add(Restrictions.eq("car.length", condition.length));
        }
        if(condition.startMinTime != null && condition.startMaxTime != null){
            criterions.add(Restrictions.between("startTime", condition.startMinTime, condition.startMaxTime));
        }
        return criterions;
    }
}
