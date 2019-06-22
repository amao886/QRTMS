package com.ycg.ksh.collect.jdbc;

import com.ycg.ksh.entity.collecter.GenerateBarcode;
import com.ycg.ksh.entity.collecter.DaySummary;
import com.ycg.ksh.entity.collecter.SummaryOrderAssess;
import com.ycg.ksh.entity.collecter.report.ColumnItem;
import com.ycg.ksh.entity.collecter.report.ColumnValue;
import com.ycg.ksh.entity.collecter.report.Report;

import java.util.Collection;
import java.util.Map;

/**
 * 收集汇总库JDBC
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public interface CollectJdbcTemplate extends JdbcTemplateService {

    /**
     * 查询每日统计记录
     * @param summaryNo
     *
     * @return
     */
    DaySummary daySummary(String summaryNo);

    /**
     * 更新每日统计记录
     * @param summary
     *
     * @return
     */
    void daySummary(DaySummary summary);

    /**
     *
     * 过滤已经存在的订单考核统计编号
     * @param summaryNos
     *
     * @return
     */
    Collection<String> filterExistOrderSummaryAssessNo(Collection<String> summaryNos);


    /**
     * 批量修改订单考核统计数据
     * @param collection
     *
     * @return
     */
    void modifyOrderSummaryAssess(String tableName, Collection<SummaryOrderAssess> collection);

    /**
     * 加载报表配置数据
     * @param reportKey  报表编号
     *
     * @return
     */
    Report loadReport(int reportKey);

    /**
     * 查询报表合计
     * @param sqlString
     * @param columnMap
     *
     * @return
     */
    ColumnValue[] selectReportColumnValue(String sqlString, Map<String, ColumnItem> columnMap);

    /**
     * 查询报表数据
     * @param sqlString
     * @param columnMap
     *
     * @return
     */
    Collection<ColumnValue[]> selectReportColumnValues(String sqlString, Map<String, ColumnItem> columnMap);

    /**
     *  查询报表数量
     * @param sqlString
     *
     * @return
     */
    Integer selectReportCount(String sqlString);

    /**
     *  查询条码生成数据
     * @param day  日期
     *
     * @return
     */
    GenerateBarcode getGenerateBarcode(Long day);

    /**
     *  更新条码生成数据
     * @param newObject
     * @param maxCode
     * @return
     */
    boolean modifyGenerateBarcode(GenerateBarcode newObject,  Long maxCode);

}
