package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/2
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * 催办查询
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/2
 */
public class ReminderSearch extends BaseEntity {

    private String likeString;// 模糊查询字符串（运单号，送货单号，收货客户，收货地址，任务摘要）
    private Integer userKey;// 用户
    private Integer groupKey;// 组
    private Integer sendKey;// 发起人
    private Integer msgType;// 类型
    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间
    private Integer[] fettles;// 待办状态

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public Integer getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(Integer groupKey) {
        this.groupKey = groupKey;
    }

    public Integer getSendKey() {
        return sendKey;
    }

    public void setSendKey(Integer sendKey) {
        this.sendKey = sendKey;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
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

    public Integer[] getFettles() {
        return fettles;
    }

    public void setFettles(Integer[] fettles) {
        this.fettles = fettles;
    }
}
