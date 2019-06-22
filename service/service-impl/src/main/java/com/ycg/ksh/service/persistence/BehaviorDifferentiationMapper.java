package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.BehaviorDifferentiation;
import com.ycg.ksh.entity.service.MergeBehaviorTotal;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wangke
 * @date 2018/3/15 16:01
 */
public interface BehaviorDifferentiationMapper extends Mapper<BehaviorDifferentiation> {

    /**
     * 查询首页统计列表
     *
     * @param mergeBehaviorTotal
     * @param bounds
     * @return
     * @author wangke
     * @date 2018/3/16 8:51
     */
    Page<MergeBehaviorTotal> behaviorTotalPage(MergeBehaviorTotal mergeBehaviorTotal, RowBounds bounds);

    /**
     * 根据uriKey  日期分组查询
     *
     * @param uriKey
     * @return
     * @author wangke
     * @date 2018/3/16 10:03
     */
    List<Map> queryTotalbyDate(String uriKey);


    /**
     * 查询用户行为列表
     *
     * @param mergeBehaviorTotal
     * @param bounds
     * @return
     * @author wangke
     * @date 2018/3/16 15:19
     */
    Page<MergeBehaviorTotal> queryUserbehavior(MergeBehaviorTotal mergeBehaviorTotal, RowBounds bounds);
}
