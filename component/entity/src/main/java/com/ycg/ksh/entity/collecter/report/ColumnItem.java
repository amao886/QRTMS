package com.ycg.ksh.entity.collecter.report;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 报表列
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public class ColumnItem  extends BaseEntity {

    /**
     * 报表编号
     */
    private int id;
    /**
     * 报表编号
     */
    private int reportId;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列code
     */
    private String columnKey;
    /**
     * 数据类型(1:字符串,2:整数,3:小数,4:百分比,5:日期)
     */
    private int columnType;

    /**
     * 是否为计算的值(1:是,0:不是)
     */
    private boolean calculate;
    /**
     * 格式化字符串
     */
    private String columnFormat;
    /**
     * sql文本
     */
    private String sqlText;
    /**
     * script文本
     */
    private String scriptText;
    /**
     * 超链接地址
     */
    private String linkUrl;

    public ColumnItem() {
    }

    public ColumnItem(int id, int reportId, String columnName, String columnKey, int columnType, String columnFormat, String sqlText, String scriptText, String linkUrl, boolean calculate ) {
        this.id = id;
        this.reportId = reportId;
        this.columnName = columnName;
        this.columnKey = columnKey;
        this.columnType = columnType;
        this.columnFormat = columnFormat;
        this.sqlText = sqlText;
        this.scriptText = scriptText;
        this.linkUrl = linkUrl;
        this.calculate = calculate;
    }

    public boolean is(int columnType){
        return this.columnType == columnType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getColumnFormat() {
        return columnFormat;
    }

    public void setColumnFormat(String columnFormat) {
        this.columnFormat = columnFormat;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public String getScriptText() {
        return scriptText;
    }

    public void setScriptText(String scriptText) {
        this.scriptText = scriptText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public boolean isCalculate() {
        return calculate;
    }

    public void setCalculate(boolean calculate) {
        this.calculate = calculate;
    }
}
