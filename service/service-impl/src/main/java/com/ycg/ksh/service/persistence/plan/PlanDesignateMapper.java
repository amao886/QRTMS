package com.ycg.ksh.service.persistence.plan;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.plan.PlanDesignate;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;

import java.util.Collection;

public interface PlanDesignateMapper extends CustomMapper<PlanDesignate> {

    Collection<CompanyConcise> listSource(Long companyId);

    Collection<Long> getLastCompanyId(Long companyId);

    PlanDesignate getByLastCompanyKey(Long lastCompanyKey, Long planKey);

    PlanDesignate getByCompanyKey(Long companyKey, Long planKey);

    Collection<Long> listDesignates(Long planKey);
}