package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.util.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * 合同有效期
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class ValidityPeriod extends Model {

    protected LocalDateTime firstTime;//有效期的开始时间
    protected LocalDateTime secondTime;//有效期的结束时间

    public ValidityPeriod() {
    }

    public ValidityPeriod(LocalDateTime firstTime, LocalDateTime secondTime) {
        this.setFirstTime(firstTime);
        this.setSecondTime(secondTime);
        if (firstTime.isAfter(secondTime)) {
            throw new ParameterException("有效期的开始时间不能在结束时间之后");
        }
    }

    public ValidityPeriod(LocalDate firstTime, LocalDate secondTime) {
        this(firstTime.atTime(LocalTime.MIN), secondTime.atTime(LocalTime.MAX));
    }

    /**
     * 判断给定的时间是否在有效期内
     *
     * @param localDateTime 给定的时间
     * @return true:在有效期内，false:不在有效期内
     */
    public boolean isEffective(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "要判断有效期的时间不能为空");
        return localDateTime.isAfter(firstTime) && localDateTime.isBefore(secondTime);
    }

    public ValidityPeriod modify(LocalDateTime firstTime, LocalDateTime secondTime) {
        return new ValidityPeriod(firstTime, secondTime);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidityPeriod)) return false;
        ValidityPeriod that = (ValidityPeriod) o;
        return Objects.equals(firstTime, that.firstTime) && Objects.equals(secondTime, that.secondTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstTime, secondTime);
    }

    @Override
    public String toString() {
        return firstTime.format(Constants.YMDHMS) + "," + secondTime.format(Constants.YMDHMS);
    }

    public LocalDateTime getFirstTime() {
        return firstTime;
    }

    public LocalDateTime getSecondTime() {
        return secondTime;
    }

    private void setFirstTime(LocalDateTime firstTime) {
        Assert.notNull(firstTime, "有效期的开始时间不能为空");
        this.firstTime = firstTime;
    }

    private void setSecondTime(LocalDateTime secondTime) {
        Assert.notNull(firstTime, "有效期的结束时间不能为空");
        this.secondTime = secondTime;
    }
}
