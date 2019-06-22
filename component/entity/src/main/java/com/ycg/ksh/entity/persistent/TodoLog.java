package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 待办事项操作日志
 */
@Table(name = "`todo_log`")
public class TodoLog extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 待办消息主键
     */
    @Column(name = "`todo_key`")
    private Integer todoKey;

    /**
     * 待办日志类型
     */
    @Column(name = "`log_type`")
    private Integer logType;

    /**
     * 时间
     */
    @Column(name = "`log_time`")
    private Date logTime;

    /**
     * 操作人编号
     */
    @Column(name = "`user_key`")
    private Integer userKey;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取待办消息主键
     *
     * @return todo_key - 待办消息主键
     */
    public Integer getTodoKey() {
        return todoKey;
    }

    /**
     * 设置待办消息主键
     *
     * @param todoKey 待办消息主键
     */
    public void setTodoKey(Integer todoKey) {
        this.todoKey = todoKey;
    }

    /**
     * 获取待办日志类型
     *
     * @return log_type - 待办日志类型
     */
    public Integer getLogType() {
        return logType;
    }

    /**
     * 设置待办日志类型
     *
     * @param logType 待办日志类型
     */
    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    /**
     * 获取时间
     *
     * @return log_time - 时间
     */
    public Date getLogTime() {
        return logTime;
    }

    /**
     * 设置时间
     *
     * @param logTime 时间
     */
    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    /**
     * 获取操作人编号
     *
     * @return user_key - 操作人编号
     */
    public Integer getUserKey() {
        return userKey;
    }

    /**
     * 设置操作人编号
     *
     * @param userKey 操作人编号
     */
    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }
}