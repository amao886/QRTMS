package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 功能描述: 企业配置
 *
 * @Auther: wangke
 * @Date: 2018/9/17 13:46
 * @Description:
 */
@Table(name = "`T_COMPANY_CONFIG`")
public class CompanyConfig extends BaseEntity {

    /**
     * 主键
     */
    @Id
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;

    /**
     * 配置编号
     */
    @Id
    @Column(name = "`CONFIG_KEY`")
    private String configKey;

    /**
     * 配置值
     */
    @Column(name = "`CONFIG_VALUE`")
    private String configValue;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    public CompanyConfig() {

    }

    public CompanyConfig(Long companyKey, String configKey, String configValue, Date updateTime) {
        this.companyKey = companyKey;
        this.configKey = configKey;
        this.configValue = configValue;
        this.updateTime = updateTime;
    }

    public CompanyConfig(Long companyKey, String configKey) {
        this.companyKey = companyKey;
        this.configKey = configKey;
    }

    public CompanyConfig(Long companyKey) {
        this.companyKey = companyKey;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
