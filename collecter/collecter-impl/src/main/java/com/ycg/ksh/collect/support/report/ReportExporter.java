package com.ycg.ksh.collect.support.report;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/15 0015
 */

import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.entity.collecter.report.ColumnItem;
import com.ycg.ksh.entity.collecter.report.ColumnValue;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 报表导出
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/15 0015
 */
public class ReportExporter {

    private static final int SIZE = 10000;

    private String reportName;//报表名称
    private Collection<ColumnItem> columns;//列

    private ExcelWriter writer;
    public ReportExporter(String reportName, Collection<ColumnItem> columns) {
        this.reportName = reportName;
        this.columns = columns;
    }
    public  ReportExporter createWriter(File file) throws BusinessException {
        try {
            this.writer = new ExcelWriter(file);
            return this;
        } catch (Exception e){
            throw new BusinessException("report "+ reportName +" create writer "+ file +" exception",  e);
        }
    }

    public  void  build() throws BusinessException {
        try {
            writer.write();
        } catch (Exception e){
            throw new BusinessException("report "+ reportName +" build file  exception",  e);
        }finally {
            try {
                writer.close();
            } catch (Exception e) {  }
        }
    }

    public ReportExporter title() {
        writer.title(reportName, columns.size());
        writer.row(columns.stream().map(ColumnItem::getColumnName).collect(Collectors.toList()));
        return this;
    }

    public ReportExporter total(ColumnValue[] totals) {
        if (ArrayUtils.isNotEmpty(totals)){
            writer.row(Stream.of(totals).map(ColumnValue::getFormatValue).collect(Collectors.toList()));
        }
        return this;
    }

    public ReportExporter  values(ColumnValue[] totals, List<ColumnValue[]> values) throws BusinessException {
        try {
            int pages = values.size() % SIZE == 0 ?  values.size() / SIZE :  values.size() / SIZE + 1;
            for (int i = 1; i <= pages; i++) {
                int s = (i-1) * SIZE, e = Math.min(s +  SIZE, values.size());
                writer.createSheet(reportName + "("+ s +"-"+ e +")");
                title();//标题和列
                if (i==1 && ArrayUtils.isNotEmpty(totals)){
                    total(totals);//合计
                }
                for (int p = s; p < e ; p++) {
                    writer.row(Stream.of(values.get(i)).map(ColumnValue::getFormatValue).collect(Collectors.toList()));
                }
            }
        } catch (Exception e){
            throw new BusinessException("report "+ reportName +" export exception", e);
        }
        return this;
    }

    public ReportExporter  values(ColumnValue[] totals, Collection<ColumnValue[]> collection, int s, int e) throws BusinessException {
        try {
            writer.createSheet(reportName + "("+ s +"-"+ e +")");
            title();//标题和列
            if (s <= 0 && ArrayUtils.isNotEmpty(totals)){
                total(totals);//合计
            }
            for (ColumnValue[] columnValues : collection) {
                writer.row(Stream.of(columnValues).map(ColumnValue::getFormatValue).collect(Collectors.toList()));
            }
        } catch (Exception ex){
            throw new BusinessException("report "+ reportName +" export exception", ex);
        }
        return this;
    }

    public ReportExporter  values(Collection<ColumnValue[]> collection, int s, int e) throws BusinessException {
        try {
            writer.createSheet(reportName + "("+ s +"-"+ e +")");
            title();//标题和列
            for (ColumnValue[] columnValues : collection) {
                writer.row(Stream.of(columnValues).map(ColumnValue::getFormatValue).collect(Collectors.toList()));
            }
        } catch (Exception ex){
            throw new BusinessException("report "+ reportName +" export exception", ex);
        }
        return this;
    }

}
