package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/5
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * 异常查询
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/5
 */
public class ExceptionSearch extends BaseEntity {

    private Date firstTime;//开始时间
    private Date secondTime;//结束时间
    private Integer assignKey;//承运人
    private Integer assignName;//承运人名称
    private Integer sendKey;

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(Date secondTime) {
        this.secondTime = secondTime;
    }

    public Integer getAssignKey() {
        return assignKey;
    }

    public void setAssignKey(Integer assignKey) {
        this.assignKey = assignKey;
    }

    public Integer getAssignName() {
        return assignName;
    }

    public void setAssignName(Integer assignName) {
        this.assignName = assignName;
    }

    public Integer getSendKey() {
        return sendKey;
    }

    public void setSendKey(Integer sendKey) {
        this.sendKey = sendKey;
    }
}
