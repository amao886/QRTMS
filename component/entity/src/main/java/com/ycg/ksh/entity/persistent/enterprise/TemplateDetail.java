package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_TEMPLATE_DETAIL`")
public class TemplateDetail extends BaseEntity {
    /**
     * 模板编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 模板编号
     */
    @Column(name = "`TEMPLATE_KEY`")
    private Long templateKey;

    /**
     * 字段编号(必填和选填字段才有值,自定义字段没有)
     */
    @Column(name = "`PROPERTY_KEY`")
    private Long propertyKey;

    /**
     * 字段位置
     */
    @Column(name = "`POSITION_INDEX`")
    private Integer positionIndex;

    /**
     * 字段是否参与唯一性
     */
    @Column(name = "`IS_UNIQUE`")
    private Boolean isUnique;

    /**
     * 是否必填
     */
    @Column(name = "`REQUIRED`")
    private boolean required;
    /**
     * 自定义字段的名称
     */
    @Column(name = "`CUSTOM_NAME`")
    private String customName;
    /**
     * 描述
     */
    @Column(name = "`DESCRIPTION`")
    private String description;

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
     * 获取模板编号
     *
     * @return TEMPLATE_KEY - 模板编号
     */
    public Long getTemplateKey() {
        return templateKey;
    }

    /**
     * 设置模板编号
     *
     * @param templateKey 模板编号
     */
    public void setTemplateKey(Long templateKey) {
        this.templateKey = templateKey;
    }

    /**
     * 获取字段编号
     *
     * @return PROPERTY_KEY - 字段编号
     */
    public Long getPropertyKey() {
        return propertyKey;
    }

    /**
     * 设置字段编号
     *
     * @param propertyKey 字段编号
     */
    public void setPropertyKey(Long propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * 获取字段位置
     *
     * @return POSITION_INDEX - 字段位置
     */
    public Integer getPositionIndex() {
        return positionIndex;
    }

    /**
     * 设置字段位置
     *
     * @param positionIndex 字段位置
     */
    public void setPositionIndex(Integer positionIndex) {
        this.positionIndex = positionIndex;
    }

    /**
     * 获取字段是否参与唯一性
     *
     * @return IS_UNIQUE - 字段是否参与唯一性
     */
    public Boolean getIsUnique() {
        return isUnique;
    }

    /**
     * 设置字段是否参与唯一性
     *
     * @param isUnique 字段是否参与唯一性
     */
    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    /**
     * 获取描述
     *
     * @return DESCRIPTION - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}