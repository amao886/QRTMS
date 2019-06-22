package com.ycg.ksh.entity.collecter;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.encrypt.MD5;

import java.time.LocalDate;

/**
 * 订单考核统计数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public class SummaryOrderAssess extends BaseEntity {

    private String summaryNo;//统计编号
    private LocalDate summaryDay;//统计日期(天)
    private long companyKey;//企业编号
    private int partnerType;//企业类型(1:发货方,2:收货方,3:承运方)
    private long otherSideKey;//对方编号

    private long totalCount;//订单总数
    private long trackCount;//跟踪及时数
    private long sendCarCount;//派车及时数
    private long arrivalCount;//到货及时数
    private long evaluateCount;//客户好评数
    private long complaintCount;//投诉次数
    private long receiptCount;//有上传回单数
    private long delayCount;//延迟数
    private long pickupCount;//准时提货数
    private long signCount;//正常签收数

    private String summaryMonth;//统计月份

    public SummaryOrderAssess() {
    }

    public SummaryOrderAssess(String summaryNo, LocalDate summaryDay, long companyKey, int partnerType, long otherSideKey) {
        this.summaryNo = summaryNo;
        this.summaryDay = summaryDay;
        this.companyKey = companyKey;
        this.partnerType = partnerType;
        this.otherSideKey = otherSideKey;
        this.summaryMonth = summaryDay.format(Globallys.DF_YM);
    }

    public SummaryOrderAssess(LocalDate summaryDay, long companyKey, int partnerType, long otherSideKey) {
        this(summaryNo(summaryDay, companyKey, partnerType, otherSideKey), summaryDay, companyKey, partnerType, otherSideKey);
    }

    public static String summaryNo(LocalDate summaryDay, long companyKey, int partnerType, long otherSideKey){
        return MD5.encrypt(summaryDay.format(Globallys.DF_YMD) + companyKey + partnerType + otherSideKey);
    }

    public String getSummaryNo() {
        return summaryNo;
    }

    public void setSummaryNo(String summaryNo) {
        this.summaryNo = summaryNo;
    }

    public LocalDate getSummaryDay() {
        return summaryDay;
    }

    public void setSummaryDay(LocalDate summaryDay) {
        this.summaryDay = summaryDay;
    }

    public long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(long companyKey) {
        this.companyKey = companyKey;
    }

    public int getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(int partnerType) {
        this.partnerType = partnerType;
    }

    public long getOtherSideKey() {
        return otherSideKey;
    }

    public void setOtherSideKey(long otherSideKey) {
        this.otherSideKey = otherSideKey;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(long trackCount) {
        this.trackCount = trackCount;
    }

    public long getSendCarCount() {
        return sendCarCount;
    }

    public void setSendCarCount(long sendCarCount) {
        this.sendCarCount = sendCarCount;
    }

    public long getArrivalCount() {
        return arrivalCount;
    }

    public void setArrivalCount(long arrivalCount) {
        this.arrivalCount = arrivalCount;
    }

    public long getEvaluateCount() {
        return evaluateCount;
    }

    public void setEvaluateCount(long evaluateCount) {
        this.evaluateCount = evaluateCount;
    }

    public long getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(long complaintCount) {
        this.complaintCount = complaintCount;
    }

    public String getSummaryMonth() {
        return summaryMonth;
    }

    public void setSummaryMonth(String summaryMonth) {
        this.summaryMonth = summaryMonth;
    }

    public long getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(long receiptCount) {
        this.receiptCount = receiptCount;
    }

    public long getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(long delayCount) {
        this.delayCount = delayCount;
    }

    public long getPickupCount() {
        return pickupCount;
    }

    public void setPickupCount(long pickupCount) {
        this.pickupCount = pickupCount;
    }

    public long getSignCount() {
        return signCount;
    }

    public void setSignCount(long signCount) {
        this.signCount = signCount;
    }

}
