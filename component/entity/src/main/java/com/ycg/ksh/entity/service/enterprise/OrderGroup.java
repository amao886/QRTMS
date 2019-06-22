package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/15
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/15
 */
public class OrderGroup extends BaseEntity {

    private long shipperKey;//发货方
    private long receiveKey;//收货方
    private long conveyKey;//承运商

    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间


    private Integer fettle;// 任务单状态
    private Integer receiptFettle;// 回单状态

    private String likeString;

    public long getShipperKey() {
        return shipperKey;
    }

    public void setShipperKey(long shipperKey) {
        this.shipperKey = shipperKey;
    }

    public long getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(long receiveKey) {
        this.receiveKey = receiveKey;
    }

    public long getConveyKey() {
        return conveyKey;
    }

    public void setConveyKey(long conveyKey) {
        this.conveyKey = conveyKey;
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

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Integer getReceiptFettle() {
        return receiptFettle;
    }

    public void setReceiptFettle(Integer receiptFettle) {
        this.receiptFettle = receiptFettle;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }
}
