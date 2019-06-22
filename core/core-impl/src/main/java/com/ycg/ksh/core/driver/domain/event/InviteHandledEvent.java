package com.ycg.ksh.core.driver.domain.event;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */

import com.ycg.ksh.common.event.DomainEvent;

import java.io.Serializable;

/**
 * 司机注册领域事件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class InviteHandledEvent extends DomainEvent {

    private Long inviteKey;//邀请编号
    private Long companyKey;//企业编号,发起邀请的企业编号
    private Long driverKey;//司机编号,处理邀请的司机
    private Boolean result;//处理结果(true:同意, false:不同意)
    

    public InviteHandledEvent(Long driverKey) {
        this.driverKey = driverKey;
    }

    public InviteHandledEvent(Serializable eventId, Long companyKey, Long driverKey, Long inviteKey, Boolean result) {
        super(eventId);
        this.companyKey = companyKey;
        this.driverKey = driverKey;
        this.inviteKey = inviteKey;
        this.result = result;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public void setDriverKey(Long driverKey) {
        this.driverKey = driverKey;
    }

    public Long getInviteKey() {
        return inviteKey;
    }

    public void setInviteKey(Long inviteKey) {
        this.inviteKey = inviteKey;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
