package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CustomDataType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Optional;

@Table(name = "`T_CUSTOM_DATA`")
public class CustomData extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 自定义数据类型(1:订单...)
     */
    @Column(name = "`DATA_TYPE`")
    private Integer dataType;

    /**
     * 数据编号,与数据类型相对应
     */
    @Column(name = "`DATA_KEY`")
    private String dataKey;

    /**
     * 自定义数据名称
     */
    @Column(name = "`CUSTOM_NAME`")
    private String customName;

    /**
     * 自定义数据值
     */
    @Column(name = "`CUSTOM_VALUE`")
    private String customValue;

    public CustomData() {
    }

    public CustomData(Integer dataType, String dataKey) {
        this.dataType = dataType;
        this.dataKey = dataKey;
    }

    public CustomData(Long key, CustomDataType dataType, Serializable dataKey, String customName, Object customValue) {
        this.key = key;
        this.dataType = dataType.getCode();
        this.dataKey = String.valueOf(dataKey);
        this.customName = customName;
        this.customValue = Optional.ofNullable(customValue).orElse(StringUtils.EMPTY).toString();
    }

    public CustomData(String customName, String customValue) {
        this.customName = customName;
        this.customValue = customValue;
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
     * 获取自定义数据类型(1:订单...)
     *
     * @return DATA_TYPE - 自定义数据类型(1:订单...)
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * 设置自定义数据类型(1:订单...)
     *
     * @param dataType 自定义数据类型(1:订单...)
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    /**
     * 获取数据编号,与数据类型相对应
     *
     * @return DATA_KEY - 数据编号,与数据类型相对应
     */
    public String getDataKey() {
        return dataKey;
    }

    /**
     * 设置数据编号,与数据类型相对应
     *
     * @param dataKey 数据编号,与数据类型相对应
     */
    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    /**
     * 获取自定义数据名称
     *
     * @return CUSTOM_NAME - 自定义数据名称
     */
    public String getCustomName() {
        return customName;
    }

    /**
     * 设置自定义数据名称
     *
     * @param customName 自定义数据名称
     */
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * 获取自定义数据值
     *
     * @return CUSTOM_VALUE - 自定义数据值
     */
    public String getCustomValue() {
        if(customValue == null || StringUtils.equalsIgnoreCase(customValue, StringUtils.EMPTY_NULL)){
            return StringUtils.EMPTY;
        }
        return customValue;
    }

    /**
     * 设置自定义数据值
     *
     * @param customValue 自定义数据值
     */
    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }
}