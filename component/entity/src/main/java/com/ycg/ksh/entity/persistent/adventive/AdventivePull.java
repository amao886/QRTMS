package com.ycg.ksh.entity.persistent.adventive;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "`T_ADVENTIVE_PUSH`")
public class AdventivePull extends BaseEntity {

    @Id
    @Column(name="`ID`")
    private Long id;
    /**
     * 推送对象
     */
    @Column(name = "`ADVENTIVE_ID`")
    private Long adventiveKey;

    /**
     * 要推送的配置
     */
    @Column(name = "`INCLUDES`")
    private String includes;

    /**
     * 不推送的配置
     */
    @Column(name = "`EXCLUDES`")
    private String excludes;
    /**
     * 关注的数据
     */
    @Column(name = "`DATA_TYPES`")
    private String dataTypes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdventiveKey() {
        return adventiveKey;
    }

    public void setAdventiveKey(Long adventiveKey) {
        this.adventiveKey = adventiveKey;
    }

    /**
     * 获取要推送的配置
     *
     * @return INCLUDES - 要推送的配置
     */
    public String getIncludes() {
        return includes;
    }

    /**
     * 设置要推送的配置
     *
     * @param includes 要推送的配置
     */
    public void setIncludes(String includes) {
        this.includes = includes;
    }

    /**
     * 获取不推送的配置
     *
     * @return EXCLUDES - 不推送的配置
     */
    public String getExcludes() {
        return excludes;
    }

    /**
     * 设置不推送的配置
     *
     * @param excludes 不推送的配置
     */
    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(String dataTypes) {
        this.dataTypes = dataTypes;
    }
}