package com.ycg.ksh.entity.collecter.report;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;

/**
 * 报表
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public class Report extends BaseEntity {

        /**
         * 报表编号
         */
        private int id;
        /**
         * 报表标题
         */
        private String title;
        /**
         * 报表sql文本
         */
        private String sqlText;
        /**
         * 表名后缀(0:无，11:按月，待定)
         */
        private int tableNameSuffix;
        /**
         * 表名后缀参数,表名后缀依据改参数的值进行变化
         */
        private int suffixQueryKey;
        /**
         * 报表类别(1:列表,2:统计,3:待定)
         */
        private int category;
        /**
         * 是否需要合计(0:不需要,1:需要)
         */
        private int needSum;
        /**
         * 列信息
         */
        private Collection<ColumnItem> columnItems;
        /**
         * 查询项
         */
        private Collection<QueryItem> queryItems;

        public Report() {
        }

        public Report(int id, String title, String sqlText, int tableNameSuffix, int suffixQueryKey, int category, int needSum) {
                this.id = id;
                this.title = title;
                this.sqlText = sqlText;
                this.tableNameSuffix = tableNameSuffix;
                this.suffixQueryKey = suffixQueryKey;
                this.category = category;
                this.needSum = needSum;
        }

        public boolean suffix(int suffix) {
                return tableNameSuffix == suffix;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getSqlText() {
                return sqlText;
        }

        public void setSqlText(String sqlText) {
                this.sqlText = sqlText;
        }

        public int getTableNameSuffix() {
                return tableNameSuffix;
        }

        public void setTableNameSuffix(int tableNameSuffix) {
                this.tableNameSuffix = tableNameSuffix;
        }

        public int getCategory() {
                return category;
        }

        public void setCategory(int category) {
                this.category = category;
        }

        public int getNeedSum() {
                return needSum;
        }

        public void setNeedSum(int needSum) {
                this.needSum = needSum;
        }

        public Collection<ColumnItem> getColumnItems() {
                return columnItems;
        }

        public void setColumnItems(Collection<ColumnItem> columnItems) {
                this.columnItems = columnItems;
        }

        public Collection<QueryItem> getQueryItems() {
                return queryItems;
        }

        public void setQueryItems(Collection<QueryItem> queryItems) {
                this.queryItems = queryItems;
        }

        public int getSuffixQueryKey() {
                return suffixQueryKey;
        }

        public void setSuffixQueryKey(int suffixQueryKey) {
                this.suffixQueryKey = suffixQueryKey;
        }
}
