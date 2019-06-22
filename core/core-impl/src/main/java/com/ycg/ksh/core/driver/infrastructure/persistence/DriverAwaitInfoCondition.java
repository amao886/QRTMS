package com.ycg.ksh.core.driver.infrastructure.persistence;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.core.util.Constants;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 等货信息查询条件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class DriverAwaitInfoCondition extends BaseEntity {


    protected Long companyKey;//企业编号
    protected Long driverKey;//司机标识

    protected Integer status;//状态-------有效、失效...

    protected Integer carType;//车型
    protected Float length; //车长

    protected String start; //路线起点

    protected LocalDateTime startMinTime;//等货开始时间
    protected LocalDateTime startMaxTime;//等货开始时间


    public static DriverAwaitInfoCondition build(Long companyKey, Integer carType, Float length, String start) {

        DriverAwaitInfoCondition condition = new DriverAwaitInfoCondition();

        condition.companyKey = companyKey;
        condition.start = start;
        condition.carType = carType;
        condition.length = length;
        condition.status = Constants.AWAIT_STAUS_EFFECTIVE;
        return condition;
    }

    public static DriverAwaitInfoCondition build(Long driverKey, String start, LocalDateTime startTime) {
        DriverAwaitInfoCondition condition = new DriverAwaitInfoCondition();

        condition.driverKey = driverKey;
        condition.start = start;

        if(startTime != null){
            condition.startMinTime = startTime.with(LocalTime.MIN);
            condition.startMaxTime = startTime.with(LocalTime.MAX);
        }

        return condition;
    }
}
