package com.ycg.ksh.entity.persistent.user;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_MONEY_EXTRACT`")
public class MoneyExtract extends BaseEntity {
    /**
     * 流水号
     */
    @Id
    @Column(name = "`EXTRACT_ID`")
    private String extractId;

    /**
     * 用户编号
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 提取数值(单位分)
     */
    @Column(name = "`EXTRACT_VALUE`")
    private Long extractValue;

    /**
     * 处理类型(0:申请，1:申请拒绝，2:申请通过)
     */
    @Column(name = "`HANDLE_TYPE`")
    private Integer handleType;

    /**
     * 申请时间
     */
    @Column(name = "`APPLY_TIME`")
    private Date applyTime;

    /**
     * 处理时间
     */
    @Column(name = "`HANDLE_TIME`")
    private Date handleTime;

    public MoneyExtract() {

    }

    public MoneyExtract(Integer userKey, String extractId, Long moneyVal) {
        this.extractId = extractId;
        this.userId = userKey;
        this.extractValue = moneyVal;
        this.applyTime = new Date();
    }

    /**
     * 获取流水号
     *
     * @return EXTRACT_ID - 流水号
     */
    public String getExtractId() {
        return extractId;
    }

    /**
     * 设置流水号
     *
     * @param extractId 流水号
     */
    public void setExtractId(String extractId) {
        this.extractId = extractId;
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
     * 获取提取数值
     *
     * @return EXTRACT_VALUE - 提取数值
     */
    public Long getExtractValue() {
        return extractValue;
    }

    /**
     * 设置提取数值
     *
     * @param extractValue 提取数值
     */
    public void setExtractValue(Long extractValue) {
        this.extractValue = extractValue;
    }

    /**
     * 获取处理类型(0:申请，1:申请拒绝，2:申请通过)
     *
     * @return HANDLE_TYPE - 处理类型(0:申请，1:申请拒绝，2:申请通过)
     */
    public Integer getHandleType() {
        return handleType;
    }

    /**
     * 设置处理类型(0:申请，1:申请拒绝，2:申请通过)
     *
     * @param handleType 处理类型(0:申请，1:申请拒绝，2:申请通过)
     */
    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    /**
     * 获取申请时间
     *
     * @return APPLY_TIME - 申请时间
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * 设置申请时间
     *
     * @param applyTime 申请时间
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取处理时间
     *
     * @return HANDLE_TIME - 处理时间
     */
    public Date getHandleTime() {
        return handleTime;
    }

    /**
     * 设置处理时间
     *
     * @param handleTime 处理时间
     */
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
}