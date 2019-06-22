package com.ycg.ksh.entity.collecter.report;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 查询参数项
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public class QueryItem extends BaseEntity {

    /**
     * 报表编号
     */
    private int id;
    /**
     * 报表编号
     */
    private int reportId;
    /**
     * 展示标签
     */
    private String showLabel;
    /**
     * 展示类型(1:输入框,2:单选框,3:复选框,4:下拉列表,5:日期,6:时间段)
     */
    private int showType;
    /**
     * 别名key
     */
    private String aliasKey;
    /**
     * sql文本
     */
    private String sqlText;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 静态json数据
     */
    private String jsonData;
    /**
     * 数据来源(0:不需要,1:静态json数据)
     */
    private int sourceType;


    private Object queryValue;

    public QueryItem() {
    }

    public QueryItem(int id, int reportId, String showLabel, int showType, String aliasKey, String sqlText, boolean required, String jsonData, int sourceType) {
        this.id = id;
        this.reportId = reportId;
        this.showLabel = showLabel;
        this.showType = showType;
        this.aliasKey = aliasKey;
        this.sqlText = sqlText;
        this.required = required;
        this.jsonData = jsonData;
        this.sourceType = sourceType;
    }

    public boolean is(int showType){
        return  this.showType == showType;
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

    public String getShowLabel() {
        return showLabel;
    }

    public void setShowLabel(String showLabel) {
        this.showLabel = showLabel;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getAliasKey() {
        return aliasKey;
    }

    public void setAliasKey(String aliasKey) {
        this.aliasKey = aliasKey;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public Object getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(Object queryValue) {
        this.queryValue = queryValue;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }
}
