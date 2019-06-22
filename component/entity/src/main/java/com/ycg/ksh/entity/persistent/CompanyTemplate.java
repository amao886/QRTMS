package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`T_COMPANY_TEMPLATE`")
public class CompanyTemplate extends BaseEntity{
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
     * 模板样式,BASE64数据
     */
    @Column(name = "`CSS_STRING`")
    private String cssString;

    /**
     * 模板HTML,BASE64数据
     */
    @Column(name = "`HTML_STRING`")
    private String htmlString;

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
     * 获取模板样式,BASE64数据
     *
     * @return CSS_STRING - 模板样式,BASE64数据
     */
    public String getCssString() {
        return cssString;
    }

    /**
     * 设置模板样式,BASE64数据
     *
     * @param cssString 模板样式,BASE64数据
     */
    public void setCssString(String cssString) {
        this.cssString = cssString;
    }

    /**
     * 获取模板HTML,BASE64数据
     *
     * @return HTML_STRING - 模板HTML,BASE64数据
     */
    public String getHtmlString() {
        return htmlString;
    }

    /**
     * 设置模板HTML,BASE64数据
     *
     * @param htmlString 模板HTML,BASE64数据
     */
    public void setHtmlString(String htmlString) {
        this.htmlString = htmlString;
    }
}