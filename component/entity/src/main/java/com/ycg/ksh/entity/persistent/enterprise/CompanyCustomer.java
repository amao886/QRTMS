package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_COMPANY_CUSTOMER`")
public class CompanyCustomer extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 客户名称
     */
    @Column(name = "`NAME`")
    private String name;

    /**
     * 所属企业编号
     */
    @Column(name = "`OWNER_KEY`")
    private Long ownerKey;

    /**
     * 对应的企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;

    /**
     * 创建人
     */
    @Column(name = "`USER_KEY`")
    private Integer userKey = 0;

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
     * 启用状态 0:禁用 1:启用
     */
    @Column(name = "`STATUS`")
    private Integer status;
    /**
     * 客户类型(1:发货方,2:收货方,3:承运方)
     */
    @Column(name = "`TYPE`")
    private Integer type;
    /**
     * 来源(1:新建,2:分享,3:指派)
     */
    @Column(name = "`SOURCE_TYPE`")
    private Integer sourceType;

    /**
     * 扫描手机号
     */
    @Column(name = "`SCAN_PHONE`")
    private String scanPhone;

    public String getScanPhone() {
        return scanPhone;
    }

    public void setScanPhone(String scanPhone) {
        this.scanPhone = scanPhone;
    }

    public CompanyCustomer() {
    }

    public CompanyCustomer(Long ownerKey, String name) {
        this.name = name;
        this.ownerKey = ownerKey;
    }

    public CompanyCustomer(Long ownerKey, Long companyKey, Integer userKey, Integer type, Integer sourceType) {
        this.ownerKey = ownerKey;
        this.companyKey = companyKey;
        this.userKey = userKey;
        this.type = type;
        this.sourceType = sourceType;
    }
    public CompanyCustomer(Long ownerKey, String name, Long companyKey, Integer userKey, Integer type, Integer sourceType) {
        this.ownerKey = ownerKey;
        this.name = name;
        this.companyKey = companyKey;
        this.userKey = userKey;
        this.type = type;
        this.sourceType = sourceType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public boolean registered(){
        return companyKey != null && companyKey > 0;
    }

    /**
     * 获取编号
     *
     * @return KEY - 编号
     */
    public Long getKey() {
        return key;
    }

    /**
     * 设置编号
     *
     * @param key 编号
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取客户名称
     *
     * @return NAME - 客户名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置客户名称
     *
     * @param name 客户名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取所属企业编号
     *
     * @return OWNER_KEY - 所属企业编号
     */
    public Long getOwnerKey() {
        return ownerKey;
    }

    /**
     * 设置所属企业编号
     *
     * @param ownerKey 所属企业编号
     */
    public void setOwnerKey(Long ownerKey) {
        this.ownerKey = ownerKey;
    }

    /**
     * 获取对应的企业编号
     *
     * @return COMPANY_KEY - 对应的企业编号
     */
    public Long getCompanyKey() {
        return companyKey;
    }

    /**
     * 设置对应的企业编号
     *
     * @param companyKey 对应的企业编号
     */
    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    /**
     * 获取创建人
     *
     * @return USER_KEY - 创建人
     */
    public Integer getUserKey() {
        return userKey;
    }

    /**
     * 设置创建人
     *
     * @param userKey 创建人
     */
    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
}