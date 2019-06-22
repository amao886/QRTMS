package com.ycg.ksh.collect.jdbc.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */

import com.ycg.ksh.collect.jdbc.JdbcTemplateService;
import com.ycg.ksh.common.exception.BusinessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * jdbc抽象
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public abstract class AbstractJdbcTemplate extends JdbcTemplate implements JdbcTemplateService {

    private static final String[] types = {"TABLE"};

    protected LocalDateTime ofNullable(Timestamp timestamp){
        return Optional.ofNullable(timestamp).map(Timestamp::toLocalDateTime).orElse(null);
    }

    protected LocalDateTime ofNullable(Timestamp timestamp, Timestamp defaultValue){
        return Optional.ofNullable(timestamp).map(Timestamp::toLocalDateTime).orElse(ofNullable(defaultValue));
    }

    protected Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public boolean existTableName(String tableName) throws BusinessException {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            rs = databaseMetaData.getTables(null, null, tableName, types);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            throw new BusinessException("exist table exception", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void createTable(String ddlString) throws BusinessException {
        execute(ddlString);
    }
}
