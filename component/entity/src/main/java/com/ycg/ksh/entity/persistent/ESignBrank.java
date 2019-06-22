package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "`T_ESIGN_BRANK`")
public class ESignBrank extends BaseEntity{
    /**
     * 省或直辖市
     */
    @Column(name = "`PROVINCE`")
    private String province;

    /**
     * 城市名称
     */
    @Column(name = "`CITY`")
    private String city;

    /**
     * 银行类型
     */
    @Column(name = "`BRANK_TYPE`")
    private String brankType;

    /**
     * 银行名称
     */
    @Column(name = "`BRANK_NAME`")
    private String brankName;

    /**
     * 联行号（大额行号）
     */
    @Column(name = "`BRANK_CODE`")
    private String brankCode;

    /**
     * 支行名称
     */
    @Column(name = "`BRANCH_NAME`")
    private String branchName;

    /**
     * 获取省或直辖市
     *
     * @return PROVINCE - 省或直辖市
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省或直辖市
     *
     * @param province 省或直辖市
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取城市名称
     *
     * @return CITY - 城市名称
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市名称
     *
     * @param city 城市名称
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取银行类型
     *
     * @return BRANK_TYPE - 银行类型
     */
    public String getBrankType() {
        return brankType;
    }

    /**
     * 设置银行类型
     *
     * @param brankType 银行类型
     */
    public void setBrankType(String brankType) {
        this.brankType = brankType;
    }

    /**
     * 获取银行名称
     *
     * @return BRANK_NAME - 银行名称
     */
    public String getBrankName() {
        return brankName;
    }

    /**
     * 设置银行名称
     *
     * @param brankName 银行名称
     */
    public void setBrankName(String brankName) {
        this.brankName = brankName;
    }

    /**
     * 获取联行号（大额行号）
     *
     * @return BRANK_CODE - 联行号（大额行号）
     */
    public String getBrankCode() {
        return brankCode;
    }

    /**
     * 设置联行号（大额行号）
     *
     * @param brankCode 联行号（大额行号）
     */
    public void setBrankCode(String brankCode) {
        this.brankCode = brankCode;
    }

    /**
     * 获取支行名称
     *
     * @return BRANCH_NAME - 支行名称
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * 设置支行名称
     *
     * @param branchName 支行名称
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}