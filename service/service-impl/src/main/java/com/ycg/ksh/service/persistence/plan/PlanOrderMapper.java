package com.ycg.ksh.service.persistence.plan;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.plan.PlanOrder;
import com.ycg.ksh.entity.service.plan.PlanSearch;
import org.apache.ibatis.session.RowBounds;

public interface PlanOrderMapper extends CustomMapper<PlanOrder> {

    /**
     * 功能描述: 货主分页查询发货计划
     *
     * @param search
     * @param rowBounds
     * @return
     * @auther: wangke
     * @date: 2018/9/13 14:35
     */
    Page<PlanOrder> listByShipper(PlanSearch search, RowBounds rowBounds);

    /**
     * 物流商分页查询发货计划
     * @param search
     * @param rowBounds
     *
     * @return
     */
    Page<PlanOrder> listByConveyer(PlanSearch search, RowBounds rowBounds);

}