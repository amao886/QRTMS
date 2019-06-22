package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.CompanBankVerifyFettle;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`T_COMPANY_BANK_VERIFY`")
public class CompanyBankVerify extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_ID`")
    private Long companyId;

    /**
     * 对公账户户名（一般来说即企业名称）
     */
    @Column(name = "`NAME`")
    private String name;

    /**
     * 企业对公银行账号
     */
    @Column(name = "`CARD_NO`")
    private String cardNo;

    /**
     * 企业银行账号开户行支行全称
     */
    @Column(name = "`SUB_BRANCH`")
    private String subBranch;

    /**
     * 企业银行账号开户行名称
     */
    @Column(name = "`BANK`")
    private String bank;

    /**
     * 企业银行账号开户行所在省份
     */
    @Column(name = "`PROVICE`")
    private String provice;

    /**
     * 企业银行账号开户行所在城市
     */
    @Column(name = "`CITY`")
    private String city;

    /**
     * 企业用户对公账户所在的开户行的大额行号
     */
    @Column(name = "`PRCPTCD`")
    private String prcptcd;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_ID`")
    private Integer createId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 状态(1:打款中,2:校验中,3:校验未通过,4:校验通过)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 更新时间
     */
    @Column(name = "`MODIFY_TIME`")
    private Date modifyTime;

    /**
     * 校验金额
     */
    @Column(name = "`CHECK_AMOUNT`")
    private Double checkAmount;

    /**
     * 打款失败原因
     */
    @Column(name = "`ERROR_MSG`")
    private String errorMsg;

    public void modify(String bank, String prcptcd, String subBranch, String provice, String city) {
        setBank(bank);
        setPrcptcd(prcptcd);
        setSubBranch(subBranch);
        setProvice(provice);
        setCity(city);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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
     * 获取企业编号
     *
     * @return COMPANY_ID - 企业编号
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置企业编号
     *
     * @param companyId 企业编号
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取对公账户户名（一般来说即企业名称）
     *
     * @return NAME - 对公账户户名（一般来说即企业名称）
     */
    public String getName() {
        return name;
    }

    /**
     * 设置对公账户户名（一般来说即企业名称）
     *
     * @param name 对公账户户名（一般来说即企业名称）
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取企业对公银行账号
     *
     * @return CARD_NO - 企业对公银行账号
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置企业对公银行账号
     *
     * @param cardNo 企业对公银行账号
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 获取企业银行账号开户行支行全称
     *
     * @return SUB_BRANCH - 企业银行账号开户行支行全称
     */
    public String getSubBranch() {
        return subBranch;
    }

    /**
     * 设置企业银行账号开户行支行全称
     *
     * @param subBranch 企业银行账号开户行支行全称
     */
    public void setSubBranch(String subBranch) {
        this.subBranch = subBranch;
    }

    /**
     * 获取企业银行账号开户行名称
     *
     * @return BANK - 企业银行账号开户行名称
     */
    public String getBank() {
        return bank;
    }

    /**
     * 设置企业银行账号开户行名称
     *
     * @param bank 企业银行账号开户行名称
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取企业银行账号开户行所在省份
     *
     * @return PROVICE - 企业银行账号开户行所在省份
     */
    public String getProvice() {
        return provice;
    }

    /**
     * 设置企业银行账号开户行所在省份
     *
     * @param provice 企业银行账号开户行所在省份
     */
    public void setProvice(String provice) {
        this.provice = provice;
    }

    /**
     * 获取企业银行账号开户行所在城市
     *
     * @return CITY - 企业银行账号开户行所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置企业银行账号开户行所在城市
     *
     * @param city 企业银行账号开户行所在城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取企业用户对公账户所在的开户行的大额行号
     *
     * @return PRCPTCD - 企业用户对公账户所在的开户行的大额行号
     */
    public String getPrcptcd() {
        return prcptcd;
    }

    /**
     * 设置企业用户对公账户所在的开户行的大额行号
     *
     * @param prcptcd 企业用户对公账户所在的开户行的大额行号
     */
    public void setPrcptcd(String prcptcd) {
        this.prcptcd = prcptcd;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_ID - 创建人
     */
    public Integer getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(Integer createId) {
        this.createId = createId;
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
     * 获取状态(1:打款中,2:校验中,3:校验未通过,4:校验通过)
     *
     * @return FETTLE - 状态(1:打款中,2:校验中,3:校验未通过,4:校验通过)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(1:打款中,2:校验未通过,3:校验通过)
     *
     * @param fettle 状态(1:打款中,2:校验中,3:校验未通过,4:校验通过)
     */
    public void setFettle(Integer fettle) {
        if (fettle != CompanBankVerifyFettle.VFAILED.getCode()) {
            this.setErrorMsg("");
        }
        this.fettle = fettle;
    }

    /**
     * 获取更新时间
     *
     * @return MODIFY_TIME - 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取校验金额
     *
     * @return CHECK_AMOUNT - 校验金额
     */
    public Double getCheckAmount() {
        return checkAmount;
    }

    /**
     * 设置校验金额
     *
     * @param checkAmount 校验金额
     */
    public void setCheckAmount(Double checkAmount) {
        this.checkAmount = checkAmount;
    }
}