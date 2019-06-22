package com.ycg.ksh.entity.adapter.wechat;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 模板数据值
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */
public class TemplateDataValue extends BaseEntity {

    private Object value;//值
    private String color;//颜色

    public TemplateDataValue() {
    }

    public TemplateDataValue(Object value, String color) {
        this.value = value;
        this.color = color;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
