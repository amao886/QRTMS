package com.ycg.ksh.entity.collecter.report;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Optional;

/**
 * 报表列
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public class ColumnValue extends BaseEntity {
    /**
     * 列名
     */
    private String name;
    /**
     * 列名
     */
    private String key;
    /**
     * 列类型
     */
    private int type;
    /**
     * 原始数值
     */
    private Object originalValue;
    /**
     * 公式
     */
    private String scriptText;
    /**
     * 执行公式之后的数值
     */
    private Object scriptValue;
    /**
     * 格式化字符串
     */
    private String format;
    /**
     * 格式化后的数值
     */
    private String formatValue;
    /**
     * 超链接地址
     */
    private String link;

    /**
     * 是否为计算的值(1:是,0:不是)
     */
    private boolean calculate;

    public ColumnValue() {
    }

    public ColumnValue(String name, String key, int type, boolean calculate) {
        this.name = name;
        this.key = key;
        this.type = type;
        this.calculate = calculate;
    }

    public boolean is(int type){
        return this.type == type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Object originalValue) {
        this.originalValue = originalValue;
    }

    public String getScriptText() {
        return scriptText;
    }

    public void setScriptText(String scriptText) {
        this.scriptText = scriptText;
    }

    public Object getScriptValue() {
        return scriptValue;
    }

    public void setScriptValue(Object scriptValue) {
        this.scriptValue = scriptValue;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFormatValue() {
        return formatValue;
    }

    public void setFormatValue(String formatValue) {
        this.formatValue = formatValue;
    }

    public boolean isCalculate() {
        return calculate;
    }

    public void setCalculate(boolean calculate) {
        this.calculate = calculate;
    }
}
