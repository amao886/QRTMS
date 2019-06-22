package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * 操作记录整合类
 *
 * @author wangke
 * @create 2018-04-09 13:34
 **/
public class MergeOperationRecord extends BaseEntity{

    private static final long serialVersionUID = -8550324582113572608L;

    //回单类型
    private String receiptType;

    //回单动作
    private String receiptAction;

    //操作时间
    private Date  operationTime;

    //操作人
    private String operationName;

    private Integer userid;

    private Integer waybillid;

    private Date firstTime;

    private Date secondTime;

    //纸质回单动作ID
    private Integer receiptStatus;

    //电子回单动作ID
    private Integer electronicStatus;

    //查询类型 1,纸质回单  2 电子回单
    private Integer searchType;

    public Integer getElectronicStatus() {
        return electronicStatus;
    }

    public void setElectronicStatus(Integer electronicStatus) {
        this.electronicStatus = electronicStatus;
    }

    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public Integer getSearchType() {
        return searchType;
    }

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

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Integer getWaybillid() {
        return waybillid;
    }

    public void setWaybillid(Integer waybillid) {
        this.waybillid = waybillid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptAction() {
        return receiptAction;
    }

    public void setReceiptAction(String receiptAction) {
        this.receiptAction = receiptAction;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
