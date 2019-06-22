package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_MESSAGE`")
public class Message extends BaseEntity {
    /**
     * 消息编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 消息头
     */
    @Column(name = "`MSG_TITLE`")
    private String msgTitle;

    /**
     * 消息内容
     */
    @Column(name = "`MSG_CONTEXT`")
    private String msgContext;

    /**
     * 消息类型(1:系统消息)
     */
    @Column(name = "`MSG_TYPE`")
    private Integer msgType;

    /**
     * 接收者用户编号
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 发送时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 附件
     */
    @Column(name = "`ADJUNCT`")
    private String adjunct;

    /**
     * 获取消息编号
     *
     * @return ID - 消息编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置消息编号
     *
     * @param id 消息编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取消息头
     *
     * @return MSG_TITLE - 消息头
     */
    public String getMsgTitle() {
        return msgTitle;
    }

    /**
     * 设置消息头
     *
     * @param msgTitle 消息头
     */
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    /**
     * 获取消息内容
     *
     * @return MSG_CONTEXT - 消息内容
     */
    public String getMsgContext() {
        return msgContext;
    }

    /**
     * 设置消息内容
     *
     * @param msgContext 消息内容
     */
    public void setMsgContext(String msgContext) {
        this.msgContext = msgContext;
    }

    /**
     * 获取消息类型(1:系统消息)
     *
     * @return MSG_TYPE - 消息类型(1:系统消息)
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * 设置消息类型(1:系统消息)
     *
     * @param msgType 消息类型(1:系统消息)
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    /**
     * 获取接收者用户编号
     *
     * @return USER_ID - 接收者用户编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置接收者用户编号
     *
     * @param userId 接收者用户编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取发送时间
     *
     * @return CREATE_TIME - 发送时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置发送时间
     *
     * @param createTime 发送时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取附件
     *
     * @return ADJUNCT - 附件
     */
    public String getAdjunct() {
        return adjunct;
    }

    /**
     * 设置附件
     *
     * @param adjunct 附件
     */
    public void setAdjunct(String adjunct) {
        this.adjunct = adjunct;
    }
}