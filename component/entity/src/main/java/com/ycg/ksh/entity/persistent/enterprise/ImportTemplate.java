package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.TemplateMergeType;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`T_IMPORT_TEMPLATE`")
public class ImportTemplate extends BaseEntity {
    /**
     * 模板编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 企业编号
     */
    @Column(name = "`COMPANY_KEY`")
    private Long companyKey;

    /**
     * 模板名称
     */
    @Column(name = "`NAME`")
    private String name;

    /**
     * 状态(1:默认,2:常用)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 创建人
     */
    @Column(name = "`USER_KEY`")
    private Integer userKey;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 最后的更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    /**
     * 开始解析的行数
     */
    @Column(name = "`START_ROW`")
    private Integer startRow;
    /**
     * 开始解析的列数
     */
    @Column(name = "`START_COLUMN`")
    private Integer startColumn;
    /**
     * 空白列
     */
    @Column(name = "`EMPTY_COLUMN`")
    private String emptyColumn;
    /**
     * 合并的方式(0:不合并,1:按送货单号合并,2:按收货信息合并)
     */
    @Column(name = "`MERGE_TYPE`")
    private Integer mergeType;

    /**
     * 类别(1:发货模板,2:计划模板)
     */
    @Column(name = "`CATEGORY`")
    private Integer category;

    public ImportTemplate() {

    }

    public ImportTemplate(Integer fettle) {
        this.fettle = fettle;
    }

    public ImportTemplate(String name, Long companyKey, Integer userKey, TemplateMergeType mergeType, String emptyColumn) {
        this.name = name;
        this.key = Globallys.nextKey();
        this.companyKey = companyKey;
        this.userKey = userKey;
        this.createTime = new Date();
        this.fettle = CoreConstants.TEMPLATE_FETTLE_DEFAULT;
        this.updateTime = this.getCreateTime();
        this.startRow = 2;
        this.startColumn = 0;
        this.mergeType = mergeType.getCode();
        this.emptyColumn = emptyColumn;
    }

    /**
     * 获取模板编号
     *
     * @return KEY - 模板编号
     */
    public Long getKey() {
        return key;
    }

    /**
     * 设置模板编号
     *
     * @param key 模板编号
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取企业编号
     *
     * @return COMPANY_KEY - 企业编号
     */
    public Long getCompanyKey() {
        return companyKey;
    }

    /**
     * 设置企业编号
     *
     * @param companyKey 企业编号
     */
    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    /**
     * 获取模板名称
     *
     * @return NAME - 模板名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板名称
     *
     * @param name 模板名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取状态(1:默认,2:常用)
     *
     * @return FETTLE - 状态(1:默认,2:常用)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(1:默认,2:常用)
     *
     * @param fettle 状态(1:默认,2:常用)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
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
     * 获取最后的更新时间
     *
     * @return UPDATE_TIME - 最后的更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后的更新时间
     *
     * @param updateTime 最后的更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(Integer startColumn) {
        this.startColumn = startColumn;
    }

    public String getEmptyColumn() {
        return emptyColumn;
    }

    public void setEmptyColumn(String emptyColumn) {
        this.emptyColumn = emptyColumn;
    }

    public Integer getMergeType() {
        return mergeType;
    }

    public void setMergeType(Integer mergeType) {
        this.mergeType = mergeType;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}