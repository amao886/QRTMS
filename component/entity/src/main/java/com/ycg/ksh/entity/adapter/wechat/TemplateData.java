package com.ycg.ksh.entity.adapter.wechat;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */

/**
 * 微信模板数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */
public class TemplateData {

    private String key;//键
    private Object value;//值
    private String color;//颜色

    public TemplateData(String key, Object value, String color) {
        this.key = key;
        this.value = value;
        this.color = color;
    }

    public TemplateData(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
