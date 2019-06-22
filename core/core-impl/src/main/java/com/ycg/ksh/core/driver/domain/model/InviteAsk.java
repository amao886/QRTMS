package com.ycg.ksh.core.driver.domain.model;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.common.event.DomainEventPublisher;
import com.ycg.ksh.core.driver.domain.event.InviteCreatedEvent;
import com.ycg.ksh.core.driver.domain.event.InviteHandledEvent;
import com.ycg.ksh.core.util.Constants;

import java.time.LocalDateTime;

/**
 * 邀请
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class InviteAsk extends Model {

    private Long inviteKey;//邀请编号
    private Long companyKey;//发出邀请的企业编号
    private Integer inviteUserKey;    //邀请人id
    private String inviteUserName;//邀请人姓名
    private String driverName;//邀请人姓名
    private String driverPhone;//邀请人姓名
    private Long driverKey;//邀请人姓名

    private LocalDateTime inviteTime; // 邀请时间-------发出邀请的时间
    private LocalDateTime handleTime; // //处理时间-------司机同意或者拒绝的时间
    private Integer handleType; //邀请结果---待处理、同意、拒绝


    /**
     * 发起邀请
     * @param companyKey  企业编号
     * @param inviteUserKey 发起邀请的用户编号
     * @param inviteUserName 发起邀请的用户名称
     * @param driverName 邀请的司机名称
     * @param driverPhone 邀请的司机手机号
     *
     * @throws BusinessException
     */
    public void invite(Long companyKey, Integer inviteUserKey, String inviteUserName, String driverName, String driverPhone) throws BusinessException{
        //判断手机号有没有邀请过
        if(Registrys.inviteAskRepository().validateInviteAsk(companyKey, driverPhone)){
                throw new BusinessException("已经邀请过["+ driverPhone +"]");
        }
        setInviteKey(Registrys.inviteAskRepository().nextIdentify());//调用资源库生成编号
        setCompanyKey(companyKey);
        setInviteUserKey(inviteUserKey);
        setInviteUserName(inviteUserName);
        setDriverName(driverName);
        setDriverPhone(driverPhone);
        setInviteTime(LocalDateTime.now());
        setHandleType(Constants.INVITE_RESULT_WAIT);
        //持久化
        Registrys.inviteAskRepository().save(this);
        //发送领域事件
        DomainEventPublisher.instance().publish(new InviteCreatedEvent(Globallys.UUID(), inviteKey, companyKey, inviteUserKey, driverName, driverPhone));
    }

    public void handle(Long driverKey, Boolean result) throws BusinessException{
        //判断是否已经处理过
        if(Constants.INVITE_RESULT_AGREE == handleType){
            throw new BusinessException("已经同意过,并且已经是合作司机了");
        }
        if(Constants.INVITE_RESULT_REFUSE == handleType){
            throw new BusinessException("已经拒绝过该邀请了");
        }
        setDriverKey(driverKey);
        setHandleTime(LocalDateTime.now());
        setHandleType(result ? Constants.INVITE_RESULT_AGREE : Constants.INVITE_RESULT_REFUSE);
        //持久化
        Registrys.inviteAskRepository().modify(this);
        //发送领域事件
        DomainEventPublisher.instance().publish(new InviteHandledEvent(Globallys.UUID(), companyKey, driverKey, inviteKey, result));
    }

    protected void setInviteKey(Long inviteKey) {
        this.inviteKey = inviteKey;
    }

    protected void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    protected void setInviteUserKey(Integer inviteUserKey) {
        this.inviteUserKey = inviteUserKey;
    }

    protected void setInviteUserName(String inviteUserName) {
        this.inviteUserName = inviteUserName;
    }

    protected void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    protected void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    protected void setDriverKey(Long driverKey) {
        this.driverKey = driverKey;
    }

    protected void setInviteTime(LocalDateTime inviteTime) {
        this.inviteTime = inviteTime;
    }

    protected void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    protected void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }


    public Long getInviteKey() {
        return inviteKey;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public Integer getInviteUserKey() {
        return inviteUserKey;
    }

    public String getInviteUserName() {
        return inviteUserName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public LocalDateTime getInviteTime() {
        return inviteTime;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public Integer getHandleType() {
        return handleType;
    }
}
