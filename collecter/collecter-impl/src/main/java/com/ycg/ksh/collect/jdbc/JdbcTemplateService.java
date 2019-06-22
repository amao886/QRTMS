package com.ycg.ksh.collect.jdbc;

import com.ycg.ksh.common.exception.BusinessException;

/**
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */
public interface JdbcTemplateService {

    public boolean existTableName(String tableName) throws BusinessException;
    public void createTable(String ddlString) throws BusinessException;
}
