package com.ycg.ksh.core.common.infrastructure.persistence;

import com.ycg.ksh.common.exception.ParameterException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;

/**
 * hibernate资源库
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/07 0007
 */
public abstract class HibernateRepository<T, K> {

    @Resource
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory getSessionFactory(){
        return Optional.ofNullable(sessionFactory).orElseThrow(()-> new ParameterException("No SessionFactory set"));
    }

    protected Session session(){
        return getSessionFactory().getCurrentSession();
    }

    protected Criteria applyCondition(Criteria criteria, Collection<Criterion> criterions){
        return criterions.stream().reduce(criteria, (a, n)->{ a.add(n); return a; }, (l, r)-> l);
    }

    public void save(T entity) {
        session().save(entity);
    }

    public void modify(T entity) {
        session().update(entity);
    }

}
