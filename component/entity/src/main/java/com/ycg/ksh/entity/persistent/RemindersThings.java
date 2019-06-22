package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 日常工作实体类
 *
 * @author wangke
 * @create 2018-02-27 9:14
 **/
@Table(name = "`reminders_things_tab`")
public class RemindersThings extends BaseEntity {

    private static final long serialVersionUID = 1347778282689958866L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 运单主键
     */
    @Column(name = "`conveyance_id`")
    private Long conveyanceId;

    /**
     * 消息类型（1：位置消息、2：回单消息）
     */
    @Column(name = "`msg_type`")
    private Integer msgType;

    /**
     * 处理状态（0：未读、1：已读、3：已处理、4：已完成、）
     */
    @Column(name = "`processing_status`")
    private Integer processingStatus;

    /**
     * 催办人（当前用户）
     */
    @Column(name = "`sendkey`")
    private Integer sendkey;

    /**
     * 父消息ID
     */
    @Column(name = "`parent_news_id`")
    private Integer parentNewsId;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 状态更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 消息备注
     */
    @Column(name = "`msg_remark`")
    private String msgRemark;

    public RemindersThings() {}

    public RemindersThings(Integer id, Integer processingStatus) {
        this.id = id;
        this.processingStatus = processingStatus;
    }

    public Integer getParentNewsId() {
        return parentNewsId;
    }

    public void setParentNewsId(Integer parentNewsId) {
        this.parentNewsId = parentNewsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getConveyanceId() {
        return conveyanceId;
    }

    public void setConveyanceId(Long conveyanceId) {
        this.conveyanceId = conveyanceId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(Integer processingStatus) {
        this.processingStatus = processingStatus;
    }

    public Integer getSendkey() {
        return sendkey;
    }

    public void setSendkey(Integer sendkey) {
        this.sendkey = sendkey;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getMsgRemark() {
        return msgRemark;
    }

    public void setMsgRemark(String msgRemark) {
        this.msgRemark = msgRemark;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
