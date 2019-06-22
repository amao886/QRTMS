package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/3
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 分享订单查询
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/3
 */
public class ShareOrderSearch extends BaseEntity {

    private boolean from = true;// true:查询我分享的; false:查询分享给我的

    private String likeString;// 模糊查询字符串(系统单号/送货单号)

    private Integer userKey;//当前用户编号
    private Long companyKey;//当前用户所属公司编号

    private Long targetKey;//目标企业编号(from=true时为分享的目标企业，from=false时为发起分享的企业编号)

    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间

    private Boolean uploadReceipt;// 是否上传回单

    private Integer[] flags;//要加载的订单数据


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



    public boolean isFrom() {
        return from;
    }

    public void setFrom(boolean from) {
        this.from = from;
    }

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

    public Long getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(Long targetKey) {
        this.targetKey = targetKey;
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

    public Boolean getUploadReceipt() {
        return uploadReceipt;
    }

    public void setUploadReceipt(Boolean uploadReceipt) {
        this.uploadReceipt = uploadReceipt;
    }

    public Integer[] getFlags() {
        return flags;
    }

    public void setFlags(Integer[] flags) {
        this.flags = flags;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }
}
