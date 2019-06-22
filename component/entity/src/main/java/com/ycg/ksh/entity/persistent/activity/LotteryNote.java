package com.ycg.ksh.entity.persistent.activity;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`T_LOTTERY_NOTE`")
public class LotteryNote extends BaseEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "`NOTE_ID`")
    private String noteId;

    /**
     * 用户编号
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 抽奖事件(装车上报, 扫码上报...)
     */
    @Column(name = "`LOTTERY_TYPE`")
    private Integer lotteryType;

    /**
     * 其他编号
     */
    @Column(name = "`OBJECT_KEY`")
    private String objectKey;

    /**
     * 奖励类型(1:红包,2:积分,3:其他)
     */
    @Column(name = "`AWARD_TYPE`")
    private Integer awardType;

    /**
     * 奖励数值
     */
    @Column(name = "`AWARD_VALUE`")
    private Long awardValue;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 更新时间时间
     */
    @Column(name = "`MODIFY_TIME`")
    private Date modifyTime;

    public LotteryNote() {
    }


    public LotteryNote(String noteId, Integer awardType, Date modifyTime, Long awardValue) {
        this.noteId = noteId;
        this.awardType = awardType;
        this.modifyTime = modifyTime;
        this.awardValue = awardValue;
    }

    public LotteryNote(String noteId, Integer userId, Integer lotteryType, Serializable objectKey, Date createTime) {
        this.noteId = noteId;
        this.userId = userId;
        this.lotteryType = lotteryType;
        this.objectKey = String.valueOf(objectKey);
        this.createTime = createTime;
    }

    /**
     * 获取流水号
     *
     * @return NOTE_ID - 流水号
     */
    public String getNoteId() {
        return noteId;
    }

    /**
     * 设置流水号
     *
     * @param noteId 流水号
     */
    public void setNoteId(String noteId) {
        this.noteId = noteId;
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
     * 获取抽奖事件(装车上报, 扫码上报...)
     *
     * @return LOTTERY_TYPE - 抽奖事件(装车上报, 扫码上报...)
     */
    public Integer getLotteryType() {
        return lotteryType;
    }

    /**
     * 设置抽奖事件(装车上报, 扫码上报...)
     *
     * @param lotteryType 抽奖事件(装车上报, 扫码上报...)
     */
    public void setLotteryType(Integer lotteryType) {
        this.lotteryType = lotteryType;
    }

    /**
     * 获取其他编号
     *
     * @return OBJECT_KEY - 其他编号
     */
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * 设置其他编号
     *
     * @param objectKey 其他编号
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * 获取奖励类型(1:红包,2:积分,3:其他)
     *
     * @return AWARD_TYPE - 奖励类型(1:红包,2:积分,3:其他)
     */
    public Integer getAwardType() {
        return awardType;
    }

    /**
     * 设置奖励类型(1:红包,2:积分,3:其他)
     *
     * @param awardType 奖励类型(1:红包,2:积分,3:其他)
     */
    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    /**
     * 获取奖励数值
     *
     * @return AWARD_VALUE - 奖励数值
     */
    public Long getAwardValue() {
        return awardValue;
    }

    /**
     * 设置奖励数值
     *
     * @param awardValue 奖励数值
     */
    public void setAwardValue(Long awardValue) {
        this.awardValue = awardValue;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间时间
     *
     * @return MODIFY_TIME - 更新时间时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间时间
     *
     * @param modifyTime 更新时间时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}