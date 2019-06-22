package com.ycg.ksh.collect.schedule;

import com.ycg.ksh.collect.jdbc.CollectJdbcTemplate;
import com.ycg.ksh.collect.observer.DaySummaryBeginObserverAdapter;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.collecter.DaySummary;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 每天开始的定时任务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public class DaySummaryBeginSchedule extends QuartzJobBean {

    protected final Logger logger = LoggerFactory.getLogger(DaySummaryBeginSchedule.class);

    @Resource
    CollectJdbcTemplate collectJdbcTemplate;

    @Autowired(required = false)
    Collection<DaySummaryBeginObserverAdapter> observers;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime localDateTime = LocalDateTime.now();
        logger.info("当前日期 {}", localDateTime.format(Globallys.DF_L_YMRHMS));
        if(CollectionUtils.isNotEmpty(observers)){
            for (DaySummaryBeginObserverAdapter observer : observers) {
                try{
                    Collection<DaySummary> summaries = Stream.of(observer.loadSupportSummary()).map(no -> collectJdbcTemplate.daySummary(no)).filter(Objects::nonNull).peek(d->d.setLastTime(localDateTime)).collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(summaries)){
                        continue;
                    }
                    for (DaySummary summary : summaries) {
                        try{

                            summary.setLeastTime(observer.daySummaryBegin(summary));
                            summary.setLastTime(localDateTime);

                            collectJdbcTemplate.daySummary(summary);
                        }catch (Exception e){
                            logger.error("每日任务: {} 执行异常", summary.getSummaryNo(), e);
                        }
                    }
                }catch (Exception e){
                    logger.error("业务处理异常 {} ", observer, e);
                }
            }
        }
    }
}


