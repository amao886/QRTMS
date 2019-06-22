package com.ycg.ksh.common.extend.jdbc;

import com.ycg.ksh.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/10 0010
 */
public class QueryBuilder {

    private StringBuilder select;
    private Map<String, Object> conditions;

    private QueryBuilder() {
        select = new StringBuilder();
        conditions = new HashMap<String, Object>();
    }

    public static QueryBuilder create(){
        return new QueryBuilder();
    }

    public  QueryBuilder column(String...columns) {
        this.select.append("select ");
        for (String column : columns) {
            this.select.append(column).append(",");
        }
        this.select.deleteCharAt(this.select.length() - 1);
        return this;
    }

    public  QueryBuilder from(String tableName) {
        this.select.append(" from ").append(tableName);
        return this;
    }

    public QueryBuilder from(String tableName, String alias) {
        this.select.append(" from ").append(tableName);
        if (StringUtils.isNotBlank(alias)) {
            this.select.append(" as ").append(alias);
        }
        return this;
    }

    public  QueryBuilder join(String category, String tableName, String alias) {
        this.select.append(" ").append(category).append(" ").append(tableName).append(" as ").append(alias);
        return this;
    }

}
