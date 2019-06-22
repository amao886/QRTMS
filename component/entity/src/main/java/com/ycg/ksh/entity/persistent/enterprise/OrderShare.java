package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ORDER_SHARE`")
public class OrderShare extends BaseEntity {
    /**
     * 分享主键
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 发起分享的企业编号
     */
    @Column(name = "`SHARE_COMPANY_KEY`")
    private Long shareCompanyKey;

    /**
     * 发起分享的企业名称
     */
    @Column(name = "`SHARE_COMPANY_NAME`")
    private String shareCompanyName;

    /**
     * 发起分享的用户编号
     */
    @Column(name = "`SHARE_USER_KEY`")
    private Integer shareUserKey;

    /**
     * 分享的订单编号
     */
    @Column(name = "`SHARE_ORDER_KEY`")
    private Long shareOrderKey;

    /**
     * 接收企业编号
     */
    @Column(name = "`RECEIVE_COMPANY_KEY`")
    private Long receiveCompanyKey;
    /**
     * 接收企业名称
     */
    @Column(name = "`RECEIVE_COMPANY_NAME`")
    private String receiveCompanyName;

    /**
     * 创建时间
     */
    @Column(name = "`SHARE_TIME`")
    private Date shareTime;

    public OrderShare() {

    }

    public OrderShare(Long receiveCompanyKey, Long shareOrderKey) {
        this.shareOrderKey = shareOrderKey;
        this.receiveCompanyKey = receiveCompanyKey;
    }

    public OrderShare(Long id, Long companyKey, String companyName, Integer userKey, Long orderKey, Long receiveCompanyKey, String receiveCompanyName) {
        this.id = id;
        this.shareCompanyKey = companyKey;
        this.shareCompanyName = companyName;
        this.shareUserKey = userKey;
        this.shareOrderKey = orderKey;
        this.receiveCompanyKey = receiveCompanyKey;
        this.receiveCompanyName = receiveCompanyName;
        this.shareTime = new Date();
    }

    public OrderShare modify(Long shareKey, Long reciveKey, Long orderKey){
        this.shareCompanyKey = shareKey;
        this.receiveCompanyKey = reciveKey;
        this.shareOrderKey = orderKey;
        return this;
    }

    /**
     * 获取分享主键
     *
     * @return ID - 分享主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置分享主键
     *
     * @param id 分享主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取发起分享的企业编号
     *
     * @return SHARE_COMPANY_KEY - 发起分享的企业编号
     */
    public Long getShareCompanyKey() {
        return shareCompanyKey;
    }

    /**
     * 设置发起分享的企业编号
     *
     * @param shareCompanyKey 发起分享的企业编号
     */
    public void setShareCompanyKey(Long shareCompanyKey) {
        this.shareCompanyKey = shareCompanyKey;
    }

    /**
     * 获取发起分享的企业名称
     *
     * @return SHARE_COMPANY_NAME - 发起分享的企业名称
     */
    public String getShareCompanyName() {
        return shareCompanyName;
    }

    /**
     * 设置发起分享的企业名称
     *
     * @param shareCompanyName 发起分享的企业名称
     */
    public void setShareCompanyName(String shareCompanyName) {
        this.shareCompanyName = shareCompanyName;
    }

    /**
     * 获取发起分享的用户编号
     *
     * @return SHARE_USER_KEY - 发起分享的用户编号
     */
    public Integer getShareUserKey() {
        return shareUserKey;
    }

    /**
     * 设置发起分享的用户编号
     *
     * @param shareUserKey 发起分享的用户编号
     */
    public void setShareUserKey(Integer shareUserKey) {
        this.shareUserKey = shareUserKey;
    }

    /**
     * 获取分享的订单编号
     *
     * @return SHARE_ORDER_KEY - 分享的订单编号
     */
    public Long getShareOrderKey() {
        return shareOrderKey;
    }

    /**
     * 设置分享的订单编号
     *
     * @param shareOrderKey 分享的订单编号
     */
    public void setShareOrderKey(Long shareOrderKey) {
        this.shareOrderKey = shareOrderKey;
    }

    /**
     * 获取接收企业编号
     *
     * @return RECEIVE_COMPANY_KEY - 接收企业编号
     */
    public Long getReceiveCompanyKey() {
        return receiveCompanyKey;
    }

    /**
     * 设置接收企业编号
     *
     * @param receiveCompanyKey 接收企业编号
     */
    public void setReceiveCompanyKey(Long receiveCompanyKey) {
        this.receiveCompanyKey = receiveCompanyKey;
    }

    /**
     * 获取接收企业名称
     *
     * @return RECEIVE_COMPANY_NAME - 接收企业名称
     */
    public String getReceiveCompanyName() {
        return receiveCompanyName;
    }

    /**
     * 设置接收企业名称
     *
     * @param receiveCompanyName 接收企业名称
     */
    public void setReceiveCompanyName(String receiveCompanyName) {
        this.receiveCompanyName = receiveCompanyName;
    }

    /**
     * 获取创建时间
     *
     * @return SHARE_TIME - 创建时间
     */
    public Date getShareTime() {
        return shareTime;
    }

    /**
     * 设置创建时间
     *
     * @param shareTime 创建时间
     */
    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }
}