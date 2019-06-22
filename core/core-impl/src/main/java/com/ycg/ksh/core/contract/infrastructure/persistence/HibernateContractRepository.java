package com.ycg.ksh.core.contract.infrastructure.persistence;

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.contract.domain.model.Contract;
import com.ycg.ksh.core.contract.domain.repos.ContractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 合同资源库
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
@Repository
public class HibernateContractRepository extends HibernateRepository<Contract, Long> implements ContractRepository {
    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public Contract findByKey(Long key) {
        return session().get(Contract.class, key);
    }

    @Override
    public long countByContractNo(String contractNo, Long exkey) {
        Criteria criteria = session().createCriteria(Contract.class);
        if (exkey != null && exkey > 0) {
            criteria.add(Restrictions.eq("contractNo", contractNo));
          //  criteria.add(Restrictions.ne("contractNo", contractNo));
        }
        criteria.setProjection(Projections.rowCount());
        System.out.println(criteria.uniqueResult());
        return Optional.ofNullable(criteria.uniqueResult()).map(s -> (Long) s).orElse(0L);
    }

    @Override
    public Contract getContract(Long companyKey, Long customerKey, LocalDateTime validityPeriod) {
        Criteria criteria = session().createCriteria(Contract.class);
        criteria.add(Restrictions.eq("companyKey", companyKey)).add(Restrictions.eq("shipperId", customerKey)).add( Restrictions.le("period.firstTime", validityPeriod)).add(Restrictions.ge("period.secondTime", validityPeriod));
        return (Contract) criteria.uniqueResult();
    }

}
