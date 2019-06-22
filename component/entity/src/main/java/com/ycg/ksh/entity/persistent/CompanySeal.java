package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_COMPANY_SEAL`")
public class CompanySeal extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_ID`")
    private Long companyId;

    /**
     * 状态(1:可用,2:禁用)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

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
     * 印章数据,图片BASE64数据
     */
    @Column(name = "`SEAL_DATA`")
    private String sealData;

    /**
     * 公司印章简称
     */
    @Column(name = "`COMPANY_NAME`")
    private String companyName;

    /**
     * 印章样式
     */
    @Column(name = "`SEAL_TYPE`")
    private String sealType;

    /**
     * 印章横向文案
     */
    @Column(name = "`COPYWRITING`")
    private String copyWriting;

    /**
     * 印章防伪条码
     */
    @Column(name = "`SECURITY_CODE`")
    private String securityCode;

    public CompanySeal() {
    }

    public CompanySeal(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSealType() {
        return sealType;
    }

    public void setSealType(String sealType) {
        this.sealType = sealType;
    }

    public String getCopyWriting() {
        return copyWriting;
    }

    public void setCopyWriting(String copyWriting) {
        this.copyWriting = copyWriting;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
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
     * 获取状态(1:可用,2:禁用)
     *
     * @return FETTLE - 状态(1:可用,2:禁用)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(1:可用,2:禁用)
     *
     * @param fettle 状态(1:可用,2:禁用)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
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
     * 获取印章数据,图片BASE64数据
     *
     * @return SEAL_DATA - 印章数据,图片BASE64数据
     */
    public String getSealData() {
        return sealData;
    }

    /**
     * 设置印章数据,图片BASE64数据
     *
     * @param sealData 印章数据,图片BASE64数据
     */
    public void setSealData(String sealData) {
        this.sealData = sealData;
    }
}