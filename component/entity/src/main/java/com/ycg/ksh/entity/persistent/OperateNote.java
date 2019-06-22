package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_OPERATE_NOTE`")
public class OperateNote extends BaseEntity {
    /**
     * 日志编号
     */
    @Id
    @Column(name = "`ID`")
    protected Long id;

    /**
     * 关联类型
     */
    @Column(name = "`HOST_TYPE`")
    protected Integer hostType;
    /**
     * 关联主键编号
     */
    @Column(name = "`HOST_ID`")
    protected String hostId;

    /**
     * 日志类型
     */
    @Column(name = "`LOG_TYPE`")
    protected Integer logType;

    /**
     * 日志内容
     */
    @Column(name = "`LOG_CONTEXT`")
    protected String logContext;

    /**
     * 附件编号
     */
    @Column(name = "`ADJUNCT_KEY`")
    protected String adjunctKey;

    /**
     * 记录人
     */
    @Column(name = "`USER_KEY`")
    protected Integer userKey;
    /**
     * 记录时间
     */
    @Column(name = "`CREATE_TIME`")
    protected Date createTime;

    public OperateNote() {

    }

    public OperateNote(Integer hostType, String hostId) {
        this.hostType = hostType;
        this.hostId = hostId;
    }

    /**
     * 获取日志编号
     *
     * @return ID - 日志编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置日志编号
     *
     * @param id 日志编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取关联主键编号
     *
     * @return HOST_ID - 关联主键编号
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * 设置关联主键编号
     *
     * @param hostId 关联主键编号
     */
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    /**
     * 获取日志类型
     *
     * @return LOG_TYPE - 日志类型
     */
    public Integer getLogType() {
        return logType;
    }

    /**
     * 设置日志类型
     *
     * @param logType 日志类型
     */
    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    /**
     * 获取日志内容
     *
     * @return LOG_CONTEXT - 日志内容
     */
    public String getLogContext() {
        return logContext;
    }

    /**
     * 设置日志内容
     *
     * @param logContext 日志内容
     */
    public void setLogContext(String logContext) {
        this.logContext = logContext;
    }

    /**
     * 获取附件编号
     *
     * @return ADJUNCT_KEY - 附件编号
     */
    public String getAdjunctKey() {
        return adjunctKey;
    }

    /**
     * 设置附件编号
     *
     * @param adjunctKey 附件编号
     */
    public void setAdjunctKey(String adjunctKey) {
        this.adjunctKey = adjunctKey;
    }

    /**
     * 获取记录时间
     *
     * @return CREATE_TIME - 记录时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置记录时间
     *
     * @param createTime 记录时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getHostType() {
        return hostType;
    }

    public void setHostType(Integer hostType) {
        this.hostType = hostType;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }
}