package com.ycg.ksh.collect.observer;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.entity.collecter.DaySummary;

import java.time.LocalDateTime;

/**
 * 每天开始的观察者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public interface DaySummaryBeginObserverAdapter {

    String[] loadSupportSummary();

    LocalDateTime daySummaryBegin(DaySummary daySummary) throws BusinessException;

}
