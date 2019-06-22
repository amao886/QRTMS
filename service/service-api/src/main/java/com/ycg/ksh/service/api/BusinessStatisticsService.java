package com.ycg.ksh.service.api;

import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.service.MergeBehaviorTotal;
import com.ycg.ksh.entity.service.PageScope;

/**
 * 用户行为逻辑接口
 *
 * @author wangke
 * @date 2018/3/16 8:47
 */
public interface BusinessStatisticsService {

    /**
     * 查询首页统计列表
     *
     * @param mergeBehaviorTotal
     * @param scope
     * @return
     * @author wangke
     * @date 2018/3/16 8:51
     */
    CustomPage<MergeBehaviorTotal> behaviorTotalPage(MergeBehaviorTotal mergeBehaviorTotal, PageScope scope);

    /**
     * 查询用户行为列表
     *
     * @param mergeBehaviorTotal
     * @param scope
     * @return
     * @author wangke
     * @date 2018/3/16 15:20
     */
    CustomPage<MergeBehaviorTotal> queryUserbehaviorPage(MergeBehaviorTotal mergeBehaviorTotal, PageScope scope);
}
