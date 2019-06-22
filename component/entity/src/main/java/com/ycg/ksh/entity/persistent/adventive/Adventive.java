package com.ycg.ksh.entity.persistent.adventive;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ADVENTIVE`")
public class Adventive extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ACCESS_KEY`")
    private Long accessKey;

    /**
     * 公钥
     */
    @Column(name = "`PUBLIC_KEY`")
    private String publicKey;

    /**
     * 私钥
     */
    @Column(name = "`PRIVATE_KEY`")
    private String privateKey;

    /**
     * IP白名单
     */
    @Column(name = "`WHITE_LIST`")
    private String whiteList;

    /**
     * 推送对象代码
     */
    @Column(name = "`HIS_CODE`")
    private String hisCode;

    /**
     * 对应的企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;

    /**
     * 推送频率(分钟/次)
     */
    @Column(name = "`FREQUENCY`")
    private Integer frequency;

    /**
     * 频率类型(1:正常,2:递增)
     */
    @Column(name = "`FREQUENCY_TYPE`")
    private Integer frequencyType;

    /**
     * 同一条记录最大推送次数
     */
    @Column(name = "`MAX_TIMES`")
    private Integer maxTimes;

    /**
     * 最后一次推送的时间
     */
    @Column(name = "`LAST_TIME`")
    private Date lastTime;

    /**
     * 状态(1:可用,2:不可用)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 数据推送URL
     */
    @Column(name = "`DELIVERY_URL`")
    private String deliveryUrl;

    public Adventive() {
    }

    public Adventive(Integer fettle) {
        this.fettle = fettle;
    }

    /**
     * 获取编号
     *
     * @return ACCESS_KEY - 编号
     */
    public Long getAccessKey() {
        return accessKey;
    }

    /**
     * 设置编号
     *
     * @param accessKey 编号
     */
    public void setAccessKey(Long accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * 获取公钥
     *
     * @return PUBLIC_KEY - 公钥
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 设置公钥
     *
     * @param publicKey 公钥
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 获取私钥
     *
     * @return PRIVATE_KEY - 私钥
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * 设置私钥
     *
     * @param privateKey 私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * 获取IP白名单
     *
     * @return WHITE_LIST - IP白名单
     */
    public String getWhiteList() {
        return whiteList;
    }

    /**
     * 设置IP白名单
     *
     * @param whiteList IP白名单
     */
    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    /**
     * 获取推送对象代码
     *
     * @return HIS_CODE - 推送对象代码
     */
    public String getHisCode() {
        return hisCode;
    }

    /**
     * 设置推送对象代码
     *
     * @param hisCode 推送对象代码
     */
    public void setHisCode(String hisCode) {
        this.hisCode = hisCode;
    }

    /**
     * 获取推送频率(分钟/次)
     *
     * @return FREQUENCY - 推送频率(分钟/次)
     */
    public Integer getFrequency() {
        return frequency;
    }

    /**
     * 设置推送频率(分钟/次)
     *
     * @param frequency 推送频率(分钟/次)
     */
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    /**
     * 获取频率类型(1:正常,2:递增)
     *
     * @return FREQUENCY_TYPE - 频率类型(1:正常,2:递增)
     */
    public Integer getFrequencyType() {
        return frequencyType;
    }

    /**
     * 设置频率类型(1:正常,2:递增)
     *
     * @param frequencyType 频率类型(1:正常,2:递增)
     */
    public void setFrequencyType(Integer frequencyType) {
        this.frequencyType = frequencyType;
    }

    /**
     * 获取同一条记录最大推送次数
     *
     * @return MAX_TIMES - 同一条记录最大推送次数
     */
    public Integer getMaxTimes() {
        return maxTimes;
    }

    /**
     * 设置同一条记录最大推送次数
     *
     * @param maxTimes 同一条记录最大推送次数
     */
    public void setMaxTimes(Integer maxTimes) {
        this.maxTimes = maxTimes;
    }

    /**
     * 获取最后一次推送的时间
     *
     * @return LAST_TIME - 最后一次推送的时间
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * 设置最后一次推送的时间
     *
     * @param lastTime 最后一次推送的时间
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 获取状态(1:可用,2:不可用)
     *
     * @return FETTLE - 状态(1:可用,2:不可用)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(1:可用,2:不可用)
     *
     * @param fettle 状态(1:可用,2:不可用)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public String getDeliveryUrl() {
        return deliveryUrl;
    }

    public void setDeliveryUrl(String deliveryUrl) {
        this.deliveryUrl = deliveryUrl;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }
}