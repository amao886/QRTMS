package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_TEMPLATE_PROPERTY`")
public class TemplateProperty extends BaseEntity {
    /**
     * 字段编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 字段名称
     */
    @Column(name = "`NAME`")
    private String name;

    /**
     * 字段描述
     */
    @Column(name = "`DESCRIPTION`")
    private String description;

    /**
     * 字段所属类
     */
    @Column(name = "`CLASS_NAME`")
    private String className;

    /**
     * 字段属性名
     */
    @Column(name = "`DATA_KEY`")
    private String dataKey;

    /**
     * 字段数据类型(1:字符串,2:整数,3:小数,4:日期)
     */
    @Column(name = "`DATA_TYPE`")
    private Integer dataType;

    /**
     * 字段类型(1:必填,2:选填)
     */
    @Column(name = "`TYPE`")
    private Integer type;

    /**
     * 类别(1:发货模板,2:计划模板)
     */
    @Column(name = "`CATEGORY`")
    private Integer category;


    public TemplateProperty() {
    }

    public TemplateProperty(Integer category) {
        this.category = category;
    }

    /**
     * 获取字段编号
     *
     * @return KEY - 字段编号
     */
    public Long getKey() {
        return key;
    }

    /**
     * 设置字段编号
     *
     * @param key 字段编号
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取字段名称
     *
     * @return NAME - 字段名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置字段名称
     *
     * @param name 字段名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取字段描述
     *
     * @return DESCRIPTION - 字段描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置字段描述
     *
     * @param description 字段描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取字段所属类
     *
     * @return CLASS_NAME - 字段所属类
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置字段所属类
     *
     * @param className 字段所属类
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取字段属性名
     *
     * @return DATA_KEY - 字段属性名
     */
    public String getDataKey() {
        return dataKey;
    }

    /**
     * 设置字段属性名
     *
     * @param dataKey 字段属性名
     */
    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    /**
     * 获取字段数据类型(1:字符串,2:整数,3:小数,4:日期)
     *
     * @return DATA_TYPE - 字段数据类型(1:字符串,2:整数,3:小数,4:日期)
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * 设置字段数据类型(1:字符串,2:整数,3:小数,4:日期)
     *
     * @param dataType 字段数据类型(1:字符串,2:整数,3:小数,4:日期)
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * 获取字段类型(1:必填,2:选填)
     *
     * @return TYPE - 字段类型(1:必填,2:选填)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置字段类型(1:必填,2:选填)
     *
     * @param type 字段类型(1:必填,2:选填)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}