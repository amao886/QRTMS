package com.ycg.ksh.service.persistence.plan;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;

import java.util.Collection;

public interface PlanCommodityMapper extends CustomMapper<PlanCommodity> {

    Collection<Long> listKeyByPlanKey(Long planKey);
}