package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.MergeBehaviorTotal;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.persistence.BehaviorDifferentiationMapper;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.api.BusinessStatisticsService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 用户行为数据业务类
 *
 * @author wangke
 * @create 2018-03-16 8:46
 **/
@Service("ksh.core.service.businessStatisticsService")
public class BusinessStatisticsServiceImpl implements BusinessStatisticsService {


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    BehaviorDifferentiationMapper behaviorDifferentiationMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public CustomPage<MergeBehaviorTotal> behaviorTotalPage(MergeBehaviorTotal mergeBehaviorTotal, PageScope scope) {
        if (scope == null) {
            scope = PageScope.DEFAULT;
        }
        Page<MergeBehaviorTotal> page = behaviorDifferentiationMapper.behaviorTotalPage(mergeBehaviorTotal, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        for (MergeBehaviorTotal total : page) {
            List<Map> totalList = behaviorDifferentiationMapper.queryTotalbyDate(total.getUriKey());
            total.setGroupCount(totalList);
        }
        return new CustomPage<MergeBehaviorTotal>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public CustomPage<MergeBehaviorTotal> queryUserbehaviorPage(MergeBehaviorTotal mergeBehaviorTotal, PageScope scope) {
        if (scope == null) {
            scope = PageScope.DEFAULT;
        }
        //如果不是全部则组装数据
        if (mergeBehaviorTotal.getFalg() != null && mergeBehaviorTotal.getFalg() < 999) {
            if (1 == mergeBehaviorTotal.getFalg()) {
                mergeBehaviorTotal.setStartTime(DateUtils.getMinusTime(1, formatter));
                mergeBehaviorTotal.setEndTime(DateUtils.getMinusTime(1, formatter));
            } else if (2 == mergeBehaviorTotal.getFalg()) {
                mergeBehaviorTotal.setStartTime(DateUtils.getMinusTime(7, formatter));
                mergeBehaviorTotal.setEndTime(DateUtils.getDateTime());
            } else if (3 == mergeBehaviorTotal.getFalg()) {
                mergeBehaviorTotal.setStartTime(DateUtils.getMinusTime(30, formatter));
                mergeBehaviorTotal.setEndTime(DateUtils.getMinusTime(1, formatter));
            }
        }

        //根据用户手机查询当前用户ID
        if (null != mergeBehaviorTotal.getLikeString() && !"".equals(mergeBehaviorTotal.getLikeString())) {
            User user = userMapper.loadUserByMobile(mergeBehaviorTotal.getLikeString());
            if (null != user) {
                mergeBehaviorTotal.setLikeString(String.valueOf(user.getId()));
            }
        }

        Page<MergeBehaviorTotal> page = behaviorDifferentiationMapper.queryUserbehavior(mergeBehaviorTotal, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<MergeBehaviorTotal>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }
}
