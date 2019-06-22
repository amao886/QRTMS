package com.ycg.ksh.entity.collecter;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * 每日统计记录信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */
public class DaySummary extends BaseEntity {

    private String summaryNo;
    private LocalDateTime lastTime;//上一次统计结束时间
    private LocalDateTime leastTime;//上一次统计最小时间
    private String ddlString;//建表语句
    private String tableName;//表名


    public String tableName(String suffix){
        return String.format(tableName, suffix);
    }

    public String ddl(String tableName){
        return String.format(ddlString, tableName);
    }


    public String getSummaryNo() {
        return summaryNo;
    }

    public void setSummaryNo(String summaryNo) {
        this.summaryNo = summaryNo;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        if(lastTime != null){
            this.lastTime = lastTime;
        }
    }

    public LocalDateTime getLeastTime() {
        return leastTime;
    }

    public void setLeastTime(LocalDateTime leastTime) {
        if(leastTime != null){
            this.leastTime = leastTime;
        }
    }

    public String getDdlString() {
        return ddlString;
    }

    public void setDdlString(String ddlString) {
        this.ddlString = ddlString;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
