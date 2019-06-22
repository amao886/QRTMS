package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_USER_LEGALIZE`")
public class UserLegalize extends BaseEntity {
    /**
     * 用户编号
     */
    @Id
    @Column(name = "`ID`")
    private Integer id;

    /**
     * 真实姓名
     */
    @Column(name = "`NAME`")
    private String name;

    /**
     * 手机号
     */
    @Column(name = "`MOBILE_PHONE`")
    private String mobilePhone;

    /**
     * 身份证号码
     */
    @Column(name = "`ID_CARD_NO`")
    private String idCardNo;

    /**
     * 认证时间
     */
    @Column(name = "`LEGALIZE_TIME`")
    private Date legalizeTime;

    /**
     * 0:没有认证,1:已经认证
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 银行账号
     */
    @Column(name = "`BRANK_CARD_NO`")
    private String brankCardNo;

    /**
     * 获取用户编号
     *
     * @return ID - 用户编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户编号
     *
     * @param id 用户编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取真实姓名
     *
     * @return NAME - 真实姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置真实姓名
     *
     * @param name 真实姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取手机号
     *
     * @return MOBILE_PHONE - 手机号
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 设置手机号
     *
     * @param mobilePhone 手机号
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * 获取身份证号码
     *
     * @return ID_CARD_NO - 身份证号码
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * 设置身份证号码
     *
     * @param idCardNo 身份证号码
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    /**
     * 获取认证时间
     *
     * @return LEGALIZE_TIME - 认证时间
     */
    public Date getLegalizeTime() {
        return legalizeTime;
    }

    /**
     * 设置认证时间
     *
     * @param legalizeTime 认证时间
     */
    public void setLegalizeTime(Date legalizeTime) {
        this.legalizeTime = legalizeTime;
    }

    /**
     * 获取0:没有认证,1:已经认证
     *
     * @return FETTLE - 0:没有认证,1:已经认证
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置0:没有认证,1:已经认证
     *
     * @param fettle 0:没有认证,1:已经认证
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    /**
     * 获取银行账号
     *
     * @return BRANK_CARD_NO - 银行账号
     */
    public String getBrankCardNo() {
        return brankCardNo;
    }

    /**
     * 设置银行账号
     *
     * @param brankCardNo 银行账号
     */
    public void setBrankCardNo(String brankCardNo) {
        this.brankCardNo = brankCardNo;
    }
}