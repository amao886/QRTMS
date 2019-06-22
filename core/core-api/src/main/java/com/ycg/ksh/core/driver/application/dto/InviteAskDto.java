package com.ycg.ksh.core.driver.application.dto;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 邀请数据传输对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class InviteAskDto extends BaseEntity {

    private Long companyKey;//发出邀请的企业编号
    private Integer inviteUserKey;    //邀请人id
    private String inviteUserName;//邀请人姓名
    private String driverName;//邀请人姓名
    private String driverPhone;//邀请人姓名


    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public Integer getInviteUserKey() {
        return inviteUserKey;
    }

    public void setInviteUserKey(Integer inviteUserKey) {
        this.inviteUserKey = inviteUserKey;
    }

    public String getInviteUserName() {
        return inviteUserName;
    }

    public void setInviteUserName(String inviteUserName) {
        this.inviteUserName = inviteUserName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
}
