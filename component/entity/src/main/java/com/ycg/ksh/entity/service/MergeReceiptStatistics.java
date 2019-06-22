package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 回单统计整合类
 *
 * @author wangke
 * @create 2018-03-08 10:05
 **/
public class MergeReceiptStatistics extends BaseEntity {

    private static final long serialVersionUID = 5629560429671430339L;

    private Integer selectYear;//选择的年份（不映射数据库）
    private Integer selectMonth;//选择的月份（不映射数据库）
    private Integer userid; //用户ID
    private Integer groupid; //组ID

    private Integer countGroupid;//总数(不映射到数据库)
    private Integer unRecycleCount;//未回收(不映射到数据库)
    private String unRecyclePercent; //未回收百分比(不映射到数据库)
    private Integer retiredSupplierCount;  //已退承运商(不映射到数据库)
    private String retiredSupplierPercent;//已退承运商百分比(不映射到数据库)
    private Integer recycleCount; //已回收(不映射到数据库)
    private String recycleCountPercent;//已回收百分比(不映射到数据库)
    private Integer retiredClientsCount;//已退客户(不映射到数据库)
    private String retiredClientsPercent;//已退客户百分比(不映射到数据库)
    private Integer dueIn; //代收(不映射到数据库)
    private String dueInPercent;//代收百分比(不映射到数据库)


    private Integer endCustomercount;//已送客户(不映射到数据库)
    private String endCustomerPercent;//已送客户百分比(不映射到数据库)
    private String groupName;//(不映射到数据库)
    private Integer store; //在库(不映射到数据库)
    private String storePercent; //在库百分比(不映射到数据库)
    private boolean all; // 是否查询全部

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public Integer getSelectYear() {
        return selectYear;
    }

    public void setSelectYear(Integer selectYear) {
        this.selectYear = selectYear;
    }

    public Integer getSelectMonth() {
        return selectMonth;
    }

    public void setSelectMonth(Integer selectMonth) {
        this.selectMonth = selectMonth;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getCountGroupid() {
        return countGroupid;
    }

    public void setCountGroupid(Integer countGroupid) {
        this.countGroupid = countGroupid;
    }

    public Integer getUnRecycleCount() {
        return unRecycleCount;
    }

    public void setUnRecycleCount(Integer unRecycleCount) {
        this.unRecycleCount = unRecycleCount;
    }

    public String getUnRecyclePercent() {
        return unRecyclePercent;
    }

    public void setUnRecyclePercent(String unRecyclePercent) {
        this.unRecyclePercent = unRecyclePercent;
    }

    public Integer getRetiredSupplierCount() {
        return retiredSupplierCount;
    }

    public void setRetiredSupplierCount(Integer retiredSupplierCount) {
        this.retiredSupplierCount = retiredSupplierCount;
    }

    public String getRetiredSupplierPercent() {
        return retiredSupplierPercent;
    }

    public void setRetiredSupplierPercent(String retiredSupplierPercent) {
        this.retiredSupplierPercent = retiredSupplierPercent;
    }

    public Integer getRecycleCount() {
        return recycleCount;
    }

    public void setRecycleCount(Integer recycleCount) {
        this.recycleCount = recycleCount;
    }

    public String getRecycleCountPercent() {
        return recycleCountPercent;
    }

    public void setRecycleCountPercent(String recycleCountPercent) {
        this.recycleCountPercent = recycleCountPercent;
    }

    public Integer getRetiredClientsCount() {
        return retiredClientsCount;
    }

    public void setRetiredClientsCount(Integer retiredClientsCount) {
        this.retiredClientsCount = retiredClientsCount;
    }

    public String getRetiredClientsPercent() {
        return retiredClientsPercent;
    }

    public void setRetiredClientsPercent(String retiredClientsPercent) {
        this.retiredClientsPercent = retiredClientsPercent;
    }

    public Integer getDueIn() {
        return dueIn;
    }

    public void setDueIn(Integer dueIn) {
        this.dueIn = dueIn;
    }

    public String getDueInPercent() {
        return dueInPercent;
    }

    public void setDueInPercent(String dueInPercent) {
        this.dueInPercent = dueInPercent;
    }

    public Integer getEndCustomercount() {
        return endCustomercount;
    }

    public void setEndCustomercount(Integer endCustomercount) {
        this.endCustomercount = endCustomercount;
    }

    public String getEndCustomerPercent() {
        return endCustomerPercent;
    }

    public void setEndCustomerPercent(String endCustomerPercent) {
        this.endCustomerPercent = endCustomerPercent;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getStorePercent() {
        return storePercent;
    }

    public void setStorePercent(String storePercent) {
        this.storePercent = storePercent;
    }
}
