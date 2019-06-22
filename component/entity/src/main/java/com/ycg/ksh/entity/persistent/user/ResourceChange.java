package com.ycg.ksh.entity.persistent.user;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_RESOURCE_CHANGE`")
public class ResourceChange extends BaseEntity {
    /**
     * 变更流水号
     */
    @Id
    @Column(name = "`CHANGE_ID`")
    private Long changeId;

    /**
     * 用户编号
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 资源类型(1:红包, 2:积分)
     */
    @Column(name = "`RES_TYPE`")
    private Integer resType;

    /**
     * 变更原因(1:抽奖活动,2:提现)
     */
    @Column(name = "`CHANGE_CAUSE`")
    private Integer changeCause;

    /**
     * 变更数值(正数为新增,负数便是减少)
     */
    @Column(name = "`CHANGE_VALUE`")
    private Long changeValue;

    /**
     * 变更时间
     */
    @Column(name = "`CHANGE_TIME`")
    private Date changeTime;

    
    public ResourceChange(){
    	super();
    }
    
    public ResourceChange(Long changeId,Integer userId,Integer resType,Integer changeCause,Long changeValue,Date changeTime){
    	super();
    	this.changeId = changeId;
    	this.userId = userId;
    	this.resType = resType;
    	this.changeCause = changeCause;
    	this.changeValue = changeValue;
    	this.changeTime = changeTime;
    }
    
    /**
     * 获取变更流水号
     *
     * @return CHANGE_ID - 变更流水号
     */
    public Long getChangeId() {
        return changeId;
    }

    /**
     * 设置变更流水号
     *
     * @param changeId 变更流水号
     */
    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }

    /**
     * 获取用户编号
     *
     * @return USER_ID - 用户编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户编号
     *
     * @param userId 用户编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取资源类型(1:红包, 2:积分)
     *
     * @return RES_TYPE - 资源类型(1:红包, 2:积分)
     */
    public Integer getResType() {
        return resType;
    }

    /**
     * 设置资源类型(1:红包, 2:积分)
     *
     * @param resType 资源类型(1:红包, 2:积分)
     */
    public void setResType(Integer resType) {
        this.resType = resType;
    }

    /**
     * 获取变更原因(1:抽奖活动,2:提现)
     *
     * @return CHANGE_CAUSE - 变更原因(1:抽奖活动,2:提现)
     */
    public Integer getChangeCause() {
        return changeCause;
    }

    /**
     * 设置变更原因(1:抽奖活动,2:提现)
     *
     * @param changeCause 变更原因(1:抽奖活动,2:提现)
     */
    public void setChangeCause(Integer changeCause) {
        this.changeCause = changeCause;
    }

    /**
     * 获取变更数值(正数为新增,负数便是减少)
     *
     * @return CHANGE_VALUE - 变更数值(正数为新增,负数便是减少)
     */
    public Long getChangeValue() {
        return changeValue;
    }

    /**
     * 设置变更数值(正数为新增,负数便是减少)
     *
     * @param changeValue 变更数值(正数为新增,负数便是减少)
     */
    public void setChangeValue(Long changeValue) {
        this.changeValue = changeValue;
    }

    /**
     * 获取变更时间
     *
     * @return CHANGE_TIME - 变更时间
     */
    public Date getChangeTime() {
        return changeTime;
    }

    /**
     * 设置变更时间
     *
     * @param changeTime 变更时间
     */
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}