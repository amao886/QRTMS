package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`smsinfo_tab`")
public class SmsInfo extends BaseEntity {

	private static final long serialVersionUID = 8741696303976102856L;

	/**
     * 主键Id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 发送人
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 发送时间
     */
    @Column(name = "`sendtime`")
    private Date sendtime;

    /**
     * 接收的手机号
     */
    @Column(name = "`mobile_phone`")
    private String mobilePhone;
    /**
     * 备注
     */
    @Column(name = "`context`")
    private String context;
    /**
     * getter method for id
     * @return the id
     */
    public Integer getId() {
        return id;
    }
    /**
     * setter method for id
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * getter method for userid
     * @return the userid
     */
    public Integer getUserid() {
        return userid;
    }
    /**
     * setter method for userid
     * @param userid the userid to set
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
    /**
     * getter method for sendtime
     * @return the sendtime
     */
    public Date getSendtime() {
        return sendtime;
    }
    /**
     * setter method for sendtime
     * @param sendtime the sendtime to set
     */
    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }
    /**
     * getter method for mobilePhone
     * @return the mobilePhone
     */
    public String getMobilePhone() {
        return mobilePhone;
    }
    /**
     * setter method for mobilePhone
     * @param mobilePhone the mobilePhone to set
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    /**
     * getter method for context
     * @return the context
     */
    public String getContext() {
        return context;
    }
    /**
     * setter method for context
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }
}