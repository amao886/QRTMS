package com.ycg.ksh.collect.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.collecter.ReportPage;
import com.ycg.ksh.entity.collecter.report.ColumnValue;

import java.util.Collection;
import java.util.Map;

/**
 * 报表service
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public interface ReportService {

    Collection<ColumnValue> reduceReport(int reportKey, Map<String, String> parameters) throws ParameterException, BusinessException;

    Collection<ColumnValue[]> listReport(int reportKey, Map<String, String> parameters) throws ParameterException, BusinessException;

    ReportPage<ColumnValue[]> pageReport(int reportKey, int num, int size, Map<String, String> parameters) throws ParameterException, BusinessException;

    FileEntity exportReport(int reportKey, Map<String, String> parameters) throws ParameterException, BusinessException;
}
