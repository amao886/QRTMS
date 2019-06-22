package com.ycg.ksh.core.driver.infrastructure.persistence;

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.common.infrastructure.persistence.HibernateRepository;
import com.ycg.ksh.core.driver.domain.model.InviteAsk;
import com.ycg.ksh.core.driver.domain.repos.InviteAskRepository;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 司机资源库Hibernate实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
@Repository
public class HibernateInviteAskRepository extends HibernateRepository<InviteAsk, Long>  implements InviteAskRepository {

    @Override
    public Long nextIdentify() {
        return Globallys.nextKey();
    }

    @Override
    public InviteAsk findByKey(Long inviteKey) {
        return session().get(InviteAsk.class, inviteKey);
    }

    @Override
    public boolean validateInviteAsk(Long companyKey, String phone) {
        return Optional.ofNullable(session().createCriteria(InviteAsk.class).add(Restrictions.eq("companyKey", companyKey)).add(Restrictions.eq("driverPhone", phone)).setProjection(Projections.rowCount()).uniqueResult()).map(o-> (Long) o).orElse(0L) > 0;
    }
}
