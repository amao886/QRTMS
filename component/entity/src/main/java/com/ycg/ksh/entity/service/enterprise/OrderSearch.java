package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.DateUtils;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * 订单查询
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */
public class OrderSearch extends BaseEntity {

    private Integer userKey;//当前用户编号

    private String likeString;// 模糊查询字符串（订单号，配送单号）

    private Long shipperKey;//发货方
    private Long receiveKey;//收货方
    private Long conveyKey;//承运商

    private Long companyKey;//查询人所属公司编号
    private PartnerType partnerType;//合作类型(1:发货方,2:收货方,3:承运方)
    private boolean manage;//是否是管理员


    private Collection<Long> companyKeys;//收货方或者承运方编号

    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间


    private Integer[] flags;//要加载的订单数据

    private Collection<Integer> fettles;// 任务单状态
    private Collection<Integer> signFettles;//签署状态
    private Collection<Integer> receiptFettles;// 电子回单状态

    private Boolean uploadReceipt;// 是否上传回单
    private boolean invalid;//是否查询的是作废订单
    private boolean ereceipt;//是否查询的是电子回单
    private boolean all;

    private Integer bindStatus;//绑定状态 0：未绑码 ，1：已绑码

    private Integer pickupWarning; //提货预警  1：正常提货 2：延迟提货
    private Integer delayWarning;//延迟预警1：正常运输 2：可能延迟 3：已延迟

    public Integer getPartnerCode() {
        if (partnerType != null) {
            return partnerType.getCode();
        }
        return null;
    }

    public void setTime(Integer timeType) {
        if (timeType != null && timeType >= 0) {
            setTime(new Date(), 0 - timeType);
        }
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

    public Integer getPickupWarning() {
        return pickupWarning;
    }

    public void setPickupWarning(Integer pickupWarning) {
        this.pickupWarning = pickupWarning;
    }

    public Integer getDelayWarning() {
        return delayWarning;
    }

    public void setDelayWarning(Integer delayWarning) {
        this.delayWarning = delayWarning;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
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

    public Collection<Long> getCompanyKeys() {
        return companyKeys;
    }

    public void setCompanyKeys(Collection<Long> companyKeys) {
        this.companyKeys = companyKeys;
    }

    public Collection<Integer> getFettles() {
        return fettles;
    }

    public void setFettles(Collection<Integer> fettles) {
        this.fettles = fettles;
    }

    public Collection<Integer> getSignFettles() {
        return signFettles;
    }

    public void setSignFettles(Collection<Integer> signFettles) {
        this.signFettles = signFettles;
    }

    public Collection<Integer> getReceiptFettles() {
        return receiptFettles;
    }

    public void setReceiptFettles(Collection<Integer> receiptFettles) {
        this.receiptFettles = receiptFettles;
    }

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public Long getShipperKey() {
        return shipperKey;
    }

    public void setShipperKey(Long shipperKey) {
        this.shipperKey = shipperKey;
    }

    public Long getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(Long receiveKey) {
        this.receiveKey = receiveKey;
    }

    public Long getConveyKey() {
        return conveyKey;
    }

    public void setConveyKey(Long conveyKey) {
        this.conveyKey = conveyKey;
    }

    public boolean isEreceipt() {
        return ereceipt;
    }

    public void setEreceipt(boolean ereceipt) {
        this.ereceipt = ereceipt;
    }

    /**
     * getter method for bindStatus
     *
     * @return the bindStatus
     */
    public Integer getBindStatus() {
        return bindStatus;
    }

    /**
     * setter method for bindStatus
     *
     * @param bindStatus the bindStatus to set
     */
    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }

    public Integer[] getFlags() {
        return flags;
    }

    public void setFlags(Integer[] flags) {
        this.flags = flags;
    }

    public Boolean getUploadReceipt() {
        return uploadReceipt;
    }

    public void setUploadReceipt(Boolean uploadReceipt) {
        this.uploadReceipt = uploadReceipt;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
    }
}
