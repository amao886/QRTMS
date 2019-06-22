package com.ycg.ksh.core.driver.domain.event;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */

import com.ycg.ksh.common.event.DomainEvent;

import java.io.Serializable;

/**
 * 邀请创建的事件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class InviteCreatedEvent extends DomainEvent {

    private Long inviteKey;//邀请编号
    private Long companyKey;//企业编号,发起邀请的企业编号
    private Integer operatorKey;//操作人编号
    private String driverName;//司机名称
    private String driverPhone;//司机手机号

    public InviteCreatedEvent() {
        super();
    }

    public InviteCreatedEvent(Serializable eventId, Long inviteKey, Long companyKey, Integer operatorKey, String driverName, String driverPhone) {
        super(eventId);
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
