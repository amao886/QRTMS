/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 10:31:27
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 任务单查询条件
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 * 2017-11-21 10:31:27
 */
public class WaybillSerach extends BaseEntity {

    private static final long serialVersionUID = 5250351405349360812L;
    private String likeString;// 模糊查询字符串（运单号，送货单号，收货客户，收货地址，任务摘要）
    private Integer userId;// 用户
    private Integer groupId;// 组
    private Integer delay;// 是否延迟(0:所有,1:延迟,2:没延迟)
    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间
    private Integer[] waybillFettles;// 任务单状态
    private Integer[] receiptFettles;// 回单状态
    private String mobilphone;// 联系电话
    private Integer waitFettle;//回单待处理类型(1:未上传,2:已上传-全部未审核,3:已上传-部分未审核,4:已审核-有不合格,5:已上传-有未审核的)
    private int TimeSearch; //按时间区间搜索
    private String startStation; //发站
    private String endStation;//到站
    private Integer paperyReceiptStatus;//纸质回单状态
    private Date deliveryTimeStart;//发货开始时间
    private Date deliveryTimeEnd;//发货结束时间

    private boolean all;

    public Date getDeliveryTimeStart() {
        return deliveryTimeStart;
    }

    public void setDeliveryTimeStart(Date deliveryTimeStart) {
        this.deliveryTimeStart = deliveryTimeStart;
    }

    public Date getDeliveryTimeEnd() {
        return deliveryTimeEnd;
    }

    public void setDeliveryTimeEnd(Date deliveryTimeEnd) {
        this.deliveryTimeEnd = deliveryTimeEnd;
    }

    public Integer getPaperyReceiptStatus() {
        return paperyReceiptStatus;
    }

    public void setPaperyReceiptStatus(Integer paperyReceiptStatus) {
        this.paperyReceiptStatus = paperyReceiptStatus;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public void setTime(Date date, int d) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        setSecondTime(DateUtils.maxOfDay(cal));
        if (d != 0) {
            cal.add(Calendar.DAY_OF_YEAR, d);
        }
        setFirstTime(DateUtils.minOfDay(cal));
    }

    public void setTime(Date firstTime, Date secondTime, int d) {
        Calendar cal = Calendar.getInstance();
        if (secondTime != null) {
            cal.setTime(secondTime);
        }
        setSecondTime(DateUtils.maxOfDay(cal));

        if (firstTime != null) {
            cal.setTime(firstTime);
        } else if (d != 0) {
            cal.add(Calendar.DAY_OF_YEAR, d);
        }
        setFirstTime(DateUtils.minOfDay(cal));
    }

    public int getTimeSearch() {
        return TimeSearch;
    }

    public void setTimeSearch(int timeSearch) {
        TimeSearch = timeSearch;
    }

    /**
     * getter method for likeString
     *
     * @return the likeString
     */
    public String getLikeString() {
        return likeString;
    }

    /**
     * setter method for likeString
     *
     * @param likeString the likeString to set
     */
    public void setLikeString(String likeString) {
        this.likeString = StringUtils.trimToEmpty(likeString);
    }

    /**
     * getter method for userId
     *
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * setter method for userId
     *
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * getter method for groupId
     *
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * setter method for groupId
     *
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * getter method for delay
     *
     * @return the delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * setter method for delay
     *
     * @param delay the delay to set
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * getter method for firstTime
     *
     * @return the firstTime
     */
    public Date getFirstTime() {
        return firstTime;
    }

    /**
     * setter method for firstTime
     *
     * @param firstTime the firstTime to set
     */
    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    /**
     * getter method for secondTime
     *
     * @return the secondTime
     */
    public Date getSecondTime() {
        return secondTime;
    }

    /**
     * setter method for secondTime
     *
     * @param secondTime the secondTime to set
     */
    public void setSecondTime(Date secondTime) {
        this.secondTime = secondTime;
    }

    /**
     * getter method for waybillFettles
     *
     * @return the waybillFettles
     */
    public Integer[] getWaybillFettles() {
        return waybillFettles;
    }

    /**
     * setter method for waybillFettles
     *
     * @param waybillFettles the waybillFettles to set
     */
    public void setWaybillFettles(Integer[] waybillFettles) {
        this.waybillFettles = waybillFettles;
    }

    /**
     * getter method for receiptFettles
     *
     * @return the receiptFettles
     */
    public Integer[] getReceiptFettles() {
        return receiptFettles;
    }

    /**
     * setter method for receiptFettles
     *
     * @param receiptFettles the receiptFettles to set
     */
    public void setReceiptFettles(Integer[] receiptFettles) {
        this.receiptFettles = receiptFettles;
    }

    /**
     * getter method for mobilphone
     *
     * @return the mobilphone
     */
    public String getMobilphone() {
        return mobilphone;
    }

    /**
     * setter method for mobilphone
     *
     * @param mobilphone the mobilphone to set
     */
    public void setMobilphone(String mobilphone) {
        this.mobilphone = mobilphone;
    }

    /**
     * getter method for waitFettle
     *
     * @return the waitFettle
     */
    public Integer getWaitFettle() {
        return waitFettle;
    }

    /**
     * setter method for waitFettle
     *
     * @param waitFettle the waitFettle to set
     */
    public void setWaitFettle(Integer waitFettle) {
        this.waitFettle = waitFettle;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
