package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import java.math.BigDecimal;

/**
 * 日统计视图实体
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 13:54 2017/12/27
 * @Modified By:
 */
public class WaybillTotalView extends BaseEntity {
    private static final long serialVersionUID = 8039353636552972355L;

    //发货任务总数
    private Integer allCount;

    //应到任务数
    private Integer toCount;

    //发货日期
    private String createTime;

    //已送达任务数
    private Integer sendCount;

    //按时送达任务数
    private Integer timeCount;

    //用户组id
    private Integer groupid;

    //用户名称
    private String companyName;

    //准时到达率
    private String sendRate;

    public String getSendRate() {
        if (toCount == null || toCount == 0 || timeCount == null || timeCount == 0) {
            sendRate = "0%";
        } else {
            sendRate = new BigDecimal(timeCount).multiply(new BigDecimal(100)).divide(new BigDecimal(toCount), 2).toPlainString() + "%";
        }
        return sendRate;
    }

    public void setSendRate(String sendRate) {
        this.sendRate = sendRate;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getToCount() {
        return toCount;
    }

    public void setToCount(Integer toCount) {
        this.toCount = toCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(Integer timeCount) {
        this.timeCount = timeCount;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
