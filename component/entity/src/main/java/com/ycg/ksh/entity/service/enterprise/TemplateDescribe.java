package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.TemplateDataType;
import com.ycg.ksh.entity.persistent.enterprise.TemplateProperty;

/**
 * 摹本字段描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public class TemplateDescribe extends BaseEntity {

    private static final String CUSTOM_CLASS_NAME = "CustomData";

    private Long key;
    private String name;//字段名称
    private String description;//字段描述
    private String className;//字段所属类
    private String dataKey;//字段属性名
    private Integer dataType;//字段数据类型(1:字符串,2:整数,3:小数,4:日期)
    private Integer type;//字段类型(1:必填,2:选填)


    private Long detailKey;
    private Object dataValue;

    private boolean required;//是否必填
    private Integer positionIndex;//字段位置
    private Boolean isUnique;//字段是否参与唯一性
    private boolean custom = false;

    public TemplateDescribe() {
    }

    public TemplateDescribe(TemplateProperty property) {
        this();
        setKey(property.getKey());
        setName(property.getName());
        setDescription(property.getDescription());
        setClassName(property.getClassName());
        setDataKey(property.getDataKey());
        setDataType(property.getDataType());
        setType(property.getType());
    }

    public TemplateDescribe(TemplateProperty property, Long key, Integer positionIndex, Boolean isUnique, Boolean required) {
        this(property);
        this.detailKey = key;
        this.positionIndex = positionIndex;
        this.isUnique = isUnique;
        this.required = required;
    }

    /**
     * 自定义字段
     * @param customName
     * @param description
     * @param positionIndex
     * @param isUnique
     * @param required
     */
    public TemplateDescribe(Long key, String customName, String description, Integer positionIndex, Boolean isUnique, Boolean required){
        this.custom = true;
        this.positionIndex = positionIndex;
        this.isUnique = isUnique;
        this.detailKey = key;
        this.required = required;
        setDataType(TemplateDataType.STRING.ordinal());
        setName(customName);
        setDescription(description);
        setType(CoreConstants.TEMPLATE_PTYPE_CUSTOM);
        setClassName(CUSTOM_CLASS_NAME);
        setDataKey(customName);
    }

    public TemplateDescribe(TemplateDescribe describe, Object dataValue) {
        this.key = describe.getKey();
        this.name = describe.getName();
        this.description = describe.getDescription();
        this.className = describe.getClassName();
        this.dataKey = describe.getDataKey();
        this.dataType = describe.getDataType();
        this.type = describe.getType();
        this.positionIndex = describe.getPositionIndex();
        this.isUnique = describe.getUnique();
        this.required = describe.isRequired();
        this.custom = describe.isCustom();
        this.detailKey = describe.getDetailKey();
        this.dataValue = dataValue;
    }

    public Integer getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(Integer positionIndex) {
        this.positionIndex = positionIndex;
    }

    public Boolean getUnique() {
        return isUnique;
    }

    public void setUnique(Boolean unique) {
        isUnique = unique;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public Long getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(Long detailKey) {
        this.detailKey = detailKey;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }


    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
