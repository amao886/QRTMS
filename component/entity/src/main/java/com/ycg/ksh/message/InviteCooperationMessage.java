package com.ycg.ksh.message;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/05 0005
 */

import java.io.Serializable;

/**
 * 邀请司机合作的消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/05 0005
 */
public class InviteCooperationMessage implements Serializable {

    private Long inviteKey;
    private Long companyKey;
    private Integer operatorKey;
    private String  driverName;
    private String driverPhone;

    public InviteCooperationMessage() {
    }

    public InviteCooperationMessage(Long inviteKey, Long companyKey, Integer operatorKey, String driverName, String driverPhone) {
        this.inviteKey = inviteKey;
        this.companyKey = companyKey;
        this.operatorKey = operatorKey;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public Long getInviteKey() {
        return inviteKey;
    }

    public void setInviteKey(Long inviteKey) {
        this.inviteKey = inviteKey;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
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

    public Integer getOperatorKey() {
        return operatorKey;
    }

    public void setOperatorKey(Integer operatorKey) {
        this.operatorKey = operatorKey;
    }
}
