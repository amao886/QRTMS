package com.ycg.ksh.collect.jdbc.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */

import com.ycg.ksh.collect.jdbc.CollectJdbcTemplate;
import com.ycg.ksh.entity.collecter.DaySummary;
import com.ycg.ksh.entity.collecter.GenerateBarcode;
import com.ycg.ksh.entity.collecter.SummaryOrderAssess;
import com.ycg.ksh.entity.collecter.report.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.metadata.HanaCallMetaDataProvider;

import javax.persistence.Column;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 汇总库JDBC操作
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */
public class CollectJdbcTemplateImpl extends AbstractJdbcTemplate implements CollectJdbcTemplate {

    private static final String TOTAL_SQL = "select count(*) from ( %s ) as s_total";

    @Override
    public DaySummary daySummary(String summaryNo) {
        return queryForObject("select summary_no, last_time, least_time, ddl_string, table_name from sys_day_summary where summary_no = '" + summaryNo + "'", new RowMapper<DaySummary>() {
            @Override
            public DaySummary mapRow(ResultSet resultSet, int i) throws SQLException {
                DaySummary summary = new DaySummary();
                summary.setSummaryNo(resultSet.getString("summary_no"));
                summary.setLastTime(ofNullable(resultSet.getTimestamp("last_time")));
                summary.setLeastTime(ofNullable(resultSet.getTimestamp("least_time")));
                summary.setDdlString(resultSet.getString("ddl_string"));
                summary.setTableName(resultSet.getString("table_name"));
                return summary;
            }
        });
    }

    @Override
    public void daySummary(DaySummary summary) {
        update("update sys_day_summary set last_time =?, least_time =? where summary_no =?", summary.getLastTime(), summary.getLeastTime(), summary.getSummaryNo());
    }


    @Override
    public Collection<String> filterExistOrderSummaryAssessNo(Collection<String> summaryNos) {
        return null;
    }

    private boolean existOrderSummaryAssess(String tableName, String summaryNo){
        return Optional.ofNullable(queryForObject("select count(summary_no) from "+ tableName +" where summary_no = ?",  Long.class, summaryNo)).orElse(0L) > 0;
    }

    @Override
    public void modifyOrderSummaryAssess(String tableName, Collection<SummaryOrderAssess> collection) {
        String sql = "insert into %s (summary_no, summary_time, company_key, partner_type, other_side_key, total_count, track_count, send_car_count, arrival_count, evaluate_count, complaint_count, delay_count, pickup_count, sign_count) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on duplicate key update summary_time=values(summary_time), company_key=values(company_key), partner_type=values(partner_type), other_side_key=values(other_side_key), total_count=values(total_count), track_count=values(track_count), send_car_count=values(send_car_count), arrival_count=values(arrival_count), evaluate_count=values(evaluate_count), complaint_count=values(complaint_count), delay_count=values(delay_count), pickup_count=values(pickup_count), sign_count=values(sign_count)";
        batchUpdate(String.format(sql, tableName), collection, 100, (preparedStatement, summaryAssess) -> {
            preparedStatement.setString(1, summaryAssess.getSummaryNo());
            preparedStatement.setDate(2, java.sql.Date.valueOf(summaryAssess.getSummaryDay()));
            preparedStatement.setLong(3, summaryAssess.getCompanyKey());
            preparedStatement.setInt(4, summaryAssess.getPartnerType());
            preparedStatement.setLong(5, summaryAssess.getOtherSideKey());
            preparedStatement.setLong(6, summaryAssess.getTotalCount());
            preparedStatement.setLong(7, summaryAssess.getTrackCount());
            preparedStatement.setLong(8, summaryAssess.getSendCarCount());
            preparedStatement.setLong(9, summaryAssess.getArrivalCount());
            preparedStatement.setLong(10, summaryAssess.getEvaluateCount());
            preparedStatement.setLong(11, summaryAssess.getComplaintCount());
            preparedStatement.setLong(12, summaryAssess.getDelayCount());
            preparedStatement.setLong(13, summaryAssess.getPickupCount());
            preparedStatement.setLong(14, summaryAssess.getSignCount());
        });
    }

    @Override
    public Report loadReport(int reportKey) {
        Report report = queryForObject("select  id, title, sql_text, table_name_suffix, suffix_query_key, category, need_sum from sys_report_config where id =?", (rs, i) -> {
            return new Report( rs.getInt("id"), rs.getString("title"), rs.getString("sql_text"), rs.getInt("table_name_suffix"), rs.getInt("suffix_query_key"), rs.getInt("category"), rs.getInt("need_sum"));
        }, reportKey);
        if (report != null) {
            report.setColumnItems(query("select id, report_id, column_name, column_key, column_type, column_format, sql_text, script_text, link_url, calculate from  sys_report_column where report_id = ?", (rs, i)->{
                    return new ColumnItem(rs.getInt("id"), rs.getInt("report_id"),  rs.getString("column_name"), rs.getString("column_key"), rs.getInt("column_type"), rs.getString("column_format"),  rs.getString("sql_text"),  rs.getString("script_text"), rs.getString("link_url"), rs.getBoolean("calculate"));
            }, reportKey));
            report.setQueryItems(query("select id, report_id, show_label , show_type, alias_key, required, sqlText, json_data, source_type from  sys_report_query where report_id = ?",  (rs, i)->{
                return new QueryItem(rs.getInt("id"), rs.getInt("report_id") , rs.getString("show_label"),  rs.getInt("show_type") , rs.getString("alias_key"),  rs.getString("sqlText"), rs.getBoolean("required"), rs.getString("json_data"),  rs.getInt("source_type"));
            }, reportKey));
        }
        return report;
    }

    @Override
    public ColumnValue[] selectReportColumnValue(String sqlString, Map<String, ColumnItem> columnMap) {
        return queryForObject(sqlString, (resultSet, i) -> {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            ColumnValue[] arrays = new ColumnValue[rsmd.getColumnCount()];
            for (int c = 1; c <= rsmd.getColumnCount(); c++){
                arrays[c - 1] = buildColumnValue(columnMap.get(rsmd.getColumnName(c)), resultSet);
            }
            return arrays;
        });
    }

    @Override
    public Collection<ColumnValue[]> selectReportColumnValues(String sqlString, Map<String, ColumnItem> columnMap) {
        return query(sqlString, (resultSet, i) -> {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            ColumnValue[] arrays = new ColumnValue[rsmd.getColumnCount()];
            for (int c = 1; c <= rsmd.getColumnCount(); c++){
                arrays[c - 1] = buildColumnValue(columnMap.get(rsmd.getColumnName(c)), resultSet);
            }
            return arrays;
        });
    }

    private ColumnValue buildColumnValue(ColumnItem columnItem,  ResultSet rs) throws SQLException {
        ColumnValue columnValue = new ColumnValue(columnItem.getColumnName(), columnItem.getColumnKey(), columnItem.getColumnType(), columnItem.isCalculate());
        if(columnItem.is(Reports.COLUMN_TYPE_INTEGER)){
            columnValue.setOriginalValue(rs.getLong(columnItem.getColumnKey()));
        }else  if(columnItem.is(Reports.COLUMN_TYPE_DECIMAL) || columnItem.is(Reports.COLUMN_TYPE_PERCENTAGE)){
            columnValue.setOriginalValue(rs.getDouble(columnItem.getColumnKey()));
        }else  if(columnItem.is(Reports.COLUMN_TYPE_DATE)){
            columnValue.setOriginalValue(ofNullable(rs.getTimestamp(columnItem.getColumnKey())));
        }else{
            columnValue.setOriginalValue(rs.getString(columnItem.getColumnKey()));
        }
        return columnValue;
    }

    @Override
    public Integer selectReportCount(String sqlString) {
        return queryForObject(String.format(TOTAL_SQL, sqlString), Integer.class);
    }


    @Override
    public GenerateBarcode getGenerateBarcode(Long day) {
        try {
            return queryForObject("select day_string, max_code, total_count from sys_generate_barcode where day_string = " + day , new RowMapper<GenerateBarcode>() {
                @Override
                public GenerateBarcode mapRow(ResultSet resultSet, int i) throws SQLException {
                    GenerateBarcode generate = new GenerateBarcode();
                    generate.setDayString(resultSet.getLong("day_string"));
                    generate.setMaxCode(resultSet.getLong("max_code"));
                    generate.setTotalCount(resultSet.getLong("total_count"));
                    return generate;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return new GenerateBarcode(day, 0, 0);
        }
    }

    @Override
    public boolean modifyGenerateBarcode(GenerateBarcode newObject, Long maxCode) {
        if(maxCode == null || maxCode <= 0){
            return  update("insert into sys_generate_barcode ( day_string, max_code, total_count) value (?, ?, ?)",  newObject.getDayString(), newObject.getMaxCode(), newObject.getTotalCount()) > 0;
        }else{
           return  update("update sys_generate_barcode set  max_code=?, total_count=? where day_string=? and max_code=?",  newObject.getMaxCode(), newObject.getTotalCount(), newObject.getDayString(), maxCode) > 0;
        }
    }
}
