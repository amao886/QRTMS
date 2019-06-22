package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.Waybill;

import java.util.Date;

/**
 * 回单列表组合类
 *
 * @author wangke
 * @create 2018-04-03 16:44
 **/
public class MergeReceiptResult extends  Waybill {
    private static final long serialVersionUID = -7640066110032633440L;

    private String groupName;

    private String receiptStatusName;

    private Date firstTime;

    private Date secondTime;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getReceiptStatusName() {
        return receiptStatusName;
    }

    public void setReceiptStatusName(String receiptStatusName) {
        this.receiptStatusName = receiptStatusName;
    }
}
