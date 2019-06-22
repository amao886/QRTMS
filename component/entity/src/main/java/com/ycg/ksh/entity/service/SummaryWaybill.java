package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 任务单数据汇总
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */
public class SummaryWaybill extends BaseEntity {

    private Integer groupKey;
    private String groupName;
    private Long cancelCount;
    private Long unbindCount;
    private Long boundCount;
    private Long ingCount;
    private Long arriveCount;
    private Long receiveCount;
    private Long delayCount;

    public Integer getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(Integer groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Long cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Long getUnbindCount() {
        return unbindCount;
    }

    public void setUnbindCount(Long unbindCount) {
        this.unbindCount = unbindCount;
    }

    public Long getBoundCount() {
        return boundCount;
    }

    public void setBoundCount(Long boundCount) {
        this.boundCount = boundCount;
    }

    public Long getIngCount() {
        return ingCount;
    }

    public void setIngCount(Long ingCount) {
        this.ingCount = ingCount;
    }

    public Long getArriveCount() {
        return arriveCount;
    }

    public void setArriveCount(Long arriveCount) {
        this.arriveCount = arriveCount;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Long getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(Long delayCount) {
        this.delayCount = delayCount;
    }
}
