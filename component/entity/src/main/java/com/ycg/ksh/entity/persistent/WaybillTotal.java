package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 每日统计详情实体类
 * <p>
 */
@Table(name = "`waybill_total_tab`")
public class WaybillTotal extends BaseEntity {

    private static final long serialVersionUID = 2339405104116308998L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 发货日期
     */
    @Column(name = "`bindTime`")
    private Date bindTime;

    /**
     * 关联ID(组ID，用户ID)
     */
    @Column(name = "`relation_id`")
    private Integer relationId;

    /**
     * 发货任务数
     */
    @Column(name = "`allCount`")
    private Date allCount;

    /**
     * 应到任务数
     */
    @Column(name = "`toCount`")
    private Integer toCount;

    /**
     * 已送达任务数
     */
    @Column(name = "`sendCount`")
    private Integer sendCount;

    /**
     * 按时送达任务数
     */
    @Column(name = "`timeCount`")
    private Integer timeCount;

    /**
     * 按时送达率
     */
    @Column(name = "`sendRate`")
    private Integer sendRate;

    /**
     * 0：个人   1：项目组
     */
    @Column(name = "`ascription`")
    private Integer ascription;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Date getAllCount() {
        return allCount;
    }

    public void setAllCount(Date allCount) {
        this.allCount = allCount;
    }

    public Integer getToCount() {
        return toCount;
    }

    public void setToCount(Integer toCount) {
        this.toCount = toCount;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(Integer timeCount) {
        this.timeCount = timeCount;
    }

    public Integer getSendRate() {
        return sendRate;
    }

    public void setSendRate(Integer sendRate) {
        this.sendRate = sendRate;
    }

    public Integer getAscription() {
        return ascription;
    }

    public void setAscription(Integer ascription) {
        this.ascription = ascription;
    }
}