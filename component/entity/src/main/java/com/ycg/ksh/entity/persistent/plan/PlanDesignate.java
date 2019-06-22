package com.ycg.ksh.entity.persistent.plan;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`T_PLAN_DESIGNATE`")
public class PlanDesignate extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 计划编号
     */
    @Column(name = "`PLAN_ID`")
    private Long planId;

    /**
     * 被指派的物流商企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;
    /**
     * 上游企业编号
     */
    @Column(name = "`LAST_COMPANY_KEY`")
    private Long lastCompanyKey;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    /**
     * 指派状态(0:未接单,1:已接单)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 分配状态(1:已分配,2:下级已分配)
     */
    @Column(name = "`ALLOCATE`")
    private Integer allocateFettle;


    @Transient
    private Boolean allocate = true;


    public PlanDesignate() { }


    public PlanDesignate(Long planId, Long lastCompanyKey, Long companyKey) {
        this.planId = planId;
        this.lastCompanyKey = lastCompanyKey;
        this.companyKey = companyKey;
    }

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取计划编号
     *
     * @return PLAN_ID - 计划编号
     */
    public Long getPlanId() {
        return planId;
    }

    /**
     * 设置计划编号
     *
     * @param planId 计划编号
     */
    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    /**
     * 获取被指派的物流商企业编号
     *
     * @return COMPANY_KEY - 被指派的物流商企业编号
     */
    public Long getCompanyKey() {
        return companyKey;
    }

    /**
     * 设置被指派的物流商企业编号
     *
     * @param companyKey 被指派的物流商企业编号
     */
    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    /**
     * 获取上游企业编号
     *
     * @return LAST_COMPANY_KEY - 上游企业编号
     */
    public Long getLastCompanyKey() {
        return lastCompanyKey;
    }

    /**
     * 设置上游企业编号
     *
     * @param lastCompanyKey 上游企业编号
     */
    public void setLastCompanyKey(Long lastCompanyKey) {
        this.lastCompanyKey = lastCompanyKey;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATE_TIME - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取指派状态(0:未接单,1:已接单)
     *
     * @return FETTLE - 指派状态(0:未接单,1:已接单)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置指派状态(0:未接单,1:已接单)
     *
     * @param fettle 指派状态(0:未接单,1:已接单)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Integer getAllocateFettle() {
        return allocateFettle;
    }

    public void setAllocateFettle(Integer allocateFettle) {
        this.allocateFettle = allocateFettle;
    }

    public Boolean getAllocate() {
        return allocate;
    }

    public void setAllocate(Boolean allocate) {
        this.allocate = allocate;
    }
}