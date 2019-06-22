package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

public class SignRecordSearch extends BaseEntity {

    private String likeString;  //企业名称/编号

    private String startBeginTime;//约定使用电子签收开始时间

    private String startEndTime;

    private String endStartTime;  //约定使用电子签收结束时间

    private String endEndTime;

    private Integer status;     //到期状态 :1 未到期 2 已到期

    private Integer userId;     //当前查询员工的公司编号

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public String getStartBeginTime() {
        return startBeginTime;
    }

    public void setStartBeginTime(String startBeginTime) {
        this.startBeginTime = startBeginTime;
    }

    public String getStartEndTime() {
        return startEndTime;
    }

    public void setStartEndTime(String startEndTime) {
        this.startEndTime = startEndTime;
    }

    public String getEndStartTime() {
        return endStartTime;
    }

    public void setEndStartTime(String endStartTime) {
        this.endStartTime = endStartTime;
    }

    public String getEndEndTime() {
        return endEndTime;
    }

    public void setEndEndTime(String endEndTime) {
        this.endEndTime = endEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
