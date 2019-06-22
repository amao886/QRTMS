package com.ycg.ksh.entity.collecter;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */

import com.ycg.ksh.common.entity.BaseEntity;

import javax.xml.crypto.Data;

/**
 * 天-上下文
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public abstract class JdbcEntity extends BaseEntity {

    public abstract String ddl(String tableName);
}
