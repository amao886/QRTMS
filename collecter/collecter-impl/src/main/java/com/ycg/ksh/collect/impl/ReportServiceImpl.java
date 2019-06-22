package com.ycg.ksh.collect.impl;

import com.ycg.ksh.collect.api.ReportService;
import com.ycg.ksh.collect.jdbc.CollectJdbcTemplate;
import com.ycg.ksh.collect.support.report.ReportExporter;
import com.ycg.ksh.collect.support.report.ReportParser;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.entity.collecter.ReportPage;
import com.ycg.ksh.entity.collecter.report.ColumnValue;
import com.ycg.ksh.entity.collecter.report.Report;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报表业务
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
@Service("ksh.collect.reportService")
public class ReportServiceImpl implements ReportService {

    @Resource
    CollectJdbcTemplate collectJdbcTemplate;

    private Report loadReport(int reportKey) throws ParameterException, BusinessException {
        Report report = collectJdbcTemplate.loadReport(reportKey);
        if (report == null) {
            throw new ParameterException("未能找到指定的报表信息");
        }
        return report;
    }

    private Collection<ColumnValue[]> reduceReport(ReportParser parser, boolean sum) {
        parser.sum(sum).analyzeColumn().analyzeSelect();
        Collection<String> selects = parser.getSelects();
        if (CollectionUtils.isNotEmpty(selects)) {
            return selects.stream().map(s -> collectJdbcTemplate.selectReportColumnValue(s, parser.columns())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<ColumnValue> reduceReport(int reportKey, Map<String, String> parameters) throws ParameterException, BusinessException {
        ReportParser parser = new ReportParser(loadReport(reportKey)).analyzeParameters(parameters).analyzeTableNames();
        return parser.collection(reduceReport(parser, false));
    }

    @Override
    public Collection<ColumnValue[]> listReport(int reportKey, Map<String, String> parameters) throws ParameterException, BusinessException {
        ReportParser parser = new ReportParser(loadReport(reportKey)).analyzeParameters(parameters).analyzeTableNames();
        Collection<ColumnValue[]> results = parser.needSum() ? reduceReport(parser, true) : new ArrayList<ColumnValue[]>();
        Collection<String> selects = parser.sum(false).analyzeColumn().analyzeSelect().getSelects();
        if (CollectionUtils.isNotEmpty(selects)) {
            for (String select : selects) {
                Collection<ColumnValue[]> collection = collectJdbcTemplate.selectReportColumnValues(select, parser.columns());
                if (CollectionUtils.isNotEmpty(collection)) {
                    results.addAll(collection);
                }
            }
        }
        return results;
    }

    private int count(Collection<String> selects){
        if (CollectionUtils.isNotEmpty(selects)) {
            return selects.stream().map(s -> collectJdbcTemplate.selectReportCount(s)).reduce((s, c)-> s+c).orElse(0);
        }
        return 0;
    }

    @Override
    public ReportPage<ColumnValue[]> pageReport(int reportKey, int num, int size, Map<String, String> parameters) throws ParameterException, BusinessException {
        num = Math.max(num, 1);
        size = Math.max(size, 1);
        ReportParser parser = new ReportParser(loadReport(reportKey));
        if(parser.dynamicTableName()){
            throw new BusinessException("分页查询目前只支持静态表名,请检查报表配置");
        }
        ReportPage<ColumnValue[]> page = new ReportPage<ColumnValue[]>(num, size);
        parser.analyzeParameters(parameters).analyzeTableNames();
        Collection<String> selects = parser.sum(false).analyzeColumn().analyzeSelect().getSelects();
        page.setTotal(count(selects));
        if(page.getTotal() > 0){
            Collection<ColumnValue[]> results = parser.needSum() ? reduceReport(parser, true) : new ArrayList<ColumnValue[]>();
            if (CollectionUtils.isNotEmpty(selects)) {
                selects = parser.sum(false).analyzeColumn().analyzeSelect((num - 1) * size, size).getSelects();
                for (String select : selects) {
                    Collection<ColumnValue[]> collection = collectJdbcTemplate.selectReportColumnValues(select, parser.columns());
                    if (CollectionUtils.isNotEmpty(collection)) {
                        results.addAll(collection);
                    }
                }
            }
            page.setResults(results);
        }
        return page.refresh();
    }

    @Override
    public FileEntity exportReport(int reportKey, Map<String, String> parameters) throws ParameterException, BusinessException {
        FileEntity fileEntity = new FileEntity(SystemUtils.directoryDownload(),  Directory.DOWN_REPORT,  reportKey +"-"+ System.nanoTime(), FileUtils.XLSX_SUFFIX);
        ReportParser parser = new ReportParser(loadReport(reportKey));
        if(parser.dynamicTableName()){
            throw new BusinessException("分页查询目前只支持静态表名,请检查报表配置");
        }
        parser.analyzeParameters(parameters).analyzeTableNames();
        int size = 5000, total = count(parser.sum(false).analyzeColumn().analyzeSelect().getSelects());//总数
        if(total <= 0){
            throw  new BusinessException("未查询到满足条件的数据");
        }
        File file = new File(fileEntity.getPath());
        ReportExporter exporter = new ReportExporter(parser.reportName(), parser.columns().values()).createWriter(file);
        ColumnValue[] reduces = parser.reduceArray(reduceReport(parser, true));//合计
        int pages = total % size == 0 ?  total / size :  total / size + 1;
        for (int i = 0; i < pages; i++) {
            Collection<String> selects = parser.sum(false).analyzeColumn().analyzeSelect((i - 1) * size, size).getSelects();
            if (CollectionUtils.isNotEmpty(selects)) {
                Collection<ColumnValue[]> collection = new ArrayList<ColumnValue[]>();
                for (String select : selects) {
                    collection.addAll(Optional.ofNullable(collectJdbcTemplate.selectReportColumnValues(select, parser.columns())).orElse(Collections.emptyList())) ;
                }
                exporter.values(reduces, collection, (i-1) * size, Math.min(i  * size, total));
            }
        }
        exporter.build();
        fileEntity.setSize(file.length());
        fileEntity.setCount(1);
        fileEntity.setAliasName(parser.reportName());
        return fileEntity;
    }
}
