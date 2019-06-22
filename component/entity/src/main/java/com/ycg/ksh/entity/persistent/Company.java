package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_COMPANY`")
public class Company extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 企业名称
     */
    @Column(name = "`COMPANY_NAME`")
    private String companyName;

    /**
     * 状态(1:注册未认证,2:已认证,3:禁用)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 电子签收开通状态(0:未开通,1:已开通)
     */
    @Column(name = "`SIGN_FETTLE`")
    private Integer signFettle;

    /**
     * 创建人
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    /**
     * 组织机构代码,与信用代码二者必填一个
     */
    @Column(name = "`CODE_ORG`")
    private String codeOrg;

    /**
     * 社会统一信用代码,与组织机构代码二者必填一个
     */
    @Column(name = "`CODE_USC`")
    private String codeUsc;

    /**
     * 法人姓名
     */
    @Column(name = "`LEGAL_NAME`")
    private String legalName;

    /**
     * 法人身份证号码
     */
    @Column(name = "`LEGAL_ID_NO`")
    private String legalIdNo;
    /**
     * 企业名称修改次数
     */
    @Column(name = "`RENAME_COUNT`")
    private Integer renameCount;

    /**
     * 企业地址
     */
    @Column(name = "`ADDRESS`")
    private String address;

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
     * 获取企业名称
     *
     * @return COMPANY_NAME - 企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置企业名称
     *
     * @param companyName 企业名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取状态(1:注册未认证,2:已认证,3:禁用)
     *
     * @return FETTLE - 状态(1:注册未认证,2:已认证,3:禁用)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(1:注册未认证,2:已认证,3:禁用)
     *
     * @param fettle 状态(1:注册未认证,2:已认证,3:禁用)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    /**
     * 获取电子签收开通状态(0:未开通,1:已开通)
     *
     * @return SIGN_FETTLE - 电子签收开通状态(0:未开通,1:已开通)
     */
    public Integer getSignFettle() {
        return signFettle;
    }

    /**
     * 设置电子签收开通状态(0:未开通,1:已开通)
     *
     * @param signFettle 电子签收开通状态(0:未开通,1:已开通)
     */
    public void setSignFettle(Integer signFettle) {
        this.signFettle = signFettle;
    }

    /**
     * 获取创建人
     *
     * @return USER_ID - 创建人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置创建人
     *
     * @param userId 创建人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
     * 获取组织机构代码,与信用代码二者必填一个
     *
     * @return CODE_ORG - 组织机构代码,与信用代码二者必填一个
     */
    public String getCodeOrg() {
        return codeOrg;
    }

    /**
     * 设置组织机构代码,与信用代码二者必填一个
     *
     * @param codeOrg 组织机构代码,与信用代码二者必填一个
     */
    public void setCodeOrg(String codeOrg) {
        this.codeOrg = codeOrg;
    }

    /**
     * 获取社会统一信用代码,与组织机构代码二者必填一个
     *
     * @return CODE_USC - 社会统一信用代码,与组织机构代码二者必填一个
     */
    public String getCodeUsc() {
        return codeUsc;
    }

    /**
     * 设置社会统一信用代码,与组织机构代码二者必填一个
     *
     * @param codeUsc 社会统一信用代码,与组织机构代码二者必填一个
     */
    public void setCodeUsc(String codeUsc) {
        this.codeUsc = codeUsc;
    }

    /**
     * 获取法人姓名
     *
     * @return LEGAL_NAME - 法人姓名
     */
    public String getLegalName() {
        return legalName;
    }

    /**
     * 设置法人姓名
     *
     * @param legalName 法人姓名
     */
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    /**
     * 获取法人身份证号码
     *
     * @return LEGAL_ID_NO - 法人身份证号码
     */
    public String getLegalIdNo() {
        return legalIdNo;
    }

    /**
     * 设置法人身份证号码
     *
     * @param legalIdNo 法人身份证号码
     */
    public void setLegalIdNo(String legalIdNo) {
        this.legalIdNo = legalIdNo;
    }

    public Integer getRenameCount() {
        return renameCount;
    }

    public void setRenameCount(Integer renameCount) {
        this.renameCount = renameCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}