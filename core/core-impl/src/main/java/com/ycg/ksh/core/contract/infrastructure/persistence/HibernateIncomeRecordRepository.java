package com.ycg.ksh.core.contract.infrastructure.persistence;

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.contract.domain.model.IncomeRecord;
import com.ycg.ksh.core.contract.domain.repos.IncomeRepository;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 收支聚合
 *
 * @author: wangke
 * @create: 2019-01-07 08:57
 **/
@Repository
public class HibernateIncomeRecordRepository extends HibernateRepository<IncomeRecord, Long> implements IncomeRepository {

    @Override
    public boolean existIncome(Long orderKey) {
        return Optional.ofNullable(
                session().createCriteria(IncomeRecord.class)
                        .add(Restrictions.eq("orderKey", orderKey))
                        .setProjection(Projections.rowCount())
                        .uniqueResult()
        ).map(o -> (Long) o).orElse(0L) > 0;
    }

    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public IncomeRecord findByKey(Long orderKey) {
        return session().get(IncomeRecord.class, orderKey);
    }
}
