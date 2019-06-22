package com.ycg.ksh.collect.support.report;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.collecter.report.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 报表解析
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/2 0002
 */
public class ReportParser {

        private static Pattern dateRegx = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

        private Report report;

        private boolean sum = false;

        private String columnSql;
        private String whereSql;
        private Collection<String> suffixs;
        private Collection<String> selects;

        private Map<String, ColumnItem> columns;
        private Map<String, QueryItem> parameters;

        private  QueryItem suffixQueryItem;

        public ReportParser(Report report) {
                this.report = report;
                suffixs = new ArrayList<String>();
                selects = new ArrayList<String>();
                columns = new HashMap<String, ColumnItem>();
                parameters = new HashMap<String, QueryItem>();
        }

        public String reportName(){
                return report.getTitle();
        }

        public boolean needSum(){
                return report.getNeedSum() > 0;
        }

        public boolean dynamicTableName() {
                return report.getTableNameSuffix() > 0;
        }

        public ReportParser analyzeParameters(Map<String, String> parameters) {
                this.parameters.clear();
                if (CollectionUtils.isNotEmpty(report.getQueryItems())) {
                        StringBuilder builder = new StringBuilder(" 1=1");
                        for (QueryItem queryItem : report.getQueryItems()) {
                                String pvalue = parameters.get(queryItem.getAliasKey());
                                if(StringUtils.isNotBlank(pvalue)){
                                        if (StringUtils.isBlank(pvalue) && queryItem.isRequired()) {
                                                throw new ParameterException(queryItem.getAliasKey(), pvalue, "参数" + queryItem.getShowLabel() + "不能为空");
                                        }
                                        String sqlText = parameter(queryItem, pvalue);
                                        if (StringUtils.isNotBlank(sqlText)) {
                                                builder.append(" and ").append(sqlText);
                                        }
                                        this.parameters.put(queryItem.getAliasKey(), queryItem);
                                }
                                if(report.getSuffixQueryKey() > 0 && queryItem.getId() - report.getSuffixQueryKey() == 0){
                                        suffixQueryItem = queryItem;
                                }
                        }
                        whereSql = builder.toString();
                }
                return this;
        }

        public ReportParser analyzeTableNames() {
                suffixs.clear();
                if (dynamicTableName()) {
                        if (suffixQueryItem == null || suffixQueryItem.getQueryValue() == null) {
                                throw new BusinessException("动态表名,后缀参数不能为空");
                        }
                        if (report.suffix(Reports.TABLE_NAME_SUFFIX_MONTH)) {
                                if (suffixQueryItem.is(Reports.QUERY_SHOW_TYPE_DATEAREA)) {
                                        LocalDate[] lds = (LocalDate[]) suffixQueryItem.getQueryValue();
                                        for (LocalDate l = lds[0], h = lds[1]; l.getYear() + l.getMonthValue() <= h.getYear() + h.getMonthValue(); l = l.plusMonths(1L)) {
                                                suffixs.add(l.format(Globallys.DF_YM));
                                        }
                                } else if (suffixQueryItem.is(Reports.QUERY_SHOW_TYPE_DATE)) {
                                        suffixs.add(((LocalDate) suffixQueryItem.getQueryValue()).format(Globallys.DF_YM));
                                } else {
                                        throw new BusinessException("表为按月分配时,表名后缀参数只能是(日期/日期区间)");
                                }
                        }

                        if (report.suffix(Reports.TABLE_NAME_SUFFIX_COMPANY)) {
                                if(suffixQueryItem.getQueryValue() == null){
                                        throw new BusinessException("表为按公司分配时,表名后缀参数不能为空");
                                }
                                suffixs.add(String.valueOf(suffixQueryItem.getQueryValue()));
                        }
                }
                return this;
        }

        public ReportParser sum(boolean sum){
                this.sum = sum;
                return this;
        }

        public ReportParser analyzeColumn() {
                columns.clear();
                if (CollectionUtils.isNotEmpty(report.getColumnItems())) {
                        StringBuilder builder = new StringBuilder();
                        for (ColumnItem columnItem : report.getColumnItems()) {
                                if(sum && columnItem.isCalculate()){
                                        builder.append("sum(");
                                }
                                builder.append(StringUtils.isNotBlank(columnItem.getSqlText()) ? columnItem.getSqlText() : columnItem.getColumnKey());
                                if(sum && columnItem.isCalculate()){
                                        builder.append(")");
                                }
                                builder.append(" as ").append(columnItem.getColumnKey()).append(",");
                                columns.put(columnItem.getColumnKey(), columnItem);
                        }
                        columnSql = builder.deleteCharAt(builder.length() - 1).toString();
                } else {
                        throw new BusinessException("至少需要一列");
                }
                return this;
        }

        public ReportParser analyzeSelect() {
                selects.clear();
                if (dynamicTableName()) {
                        if (CollectionUtils.isNotEmpty(suffixs)) {
                                for (String suffix : suffixs) {
                                        selects.add(String.format(report.getSqlText(), columnSql , suffix, whereSql));
                                }
                        } else {
                                throw new BusinessException("表名为动态时,表名后缀至少有一个");
                        }
                } else {
                        selects.add(String.format(report.getSqlText(), columnSql , whereSql));
                }
                return this;
        }

        public ReportParser analyzeSelect(int s, int e) {
                selects.clear();
                String limit = String.format(" limit %d, %d", s, e);
                if (dynamicTableName()) {
                        if (CollectionUtils.isNotEmpty(suffixs)) {
                                for (String suffix : suffixs) {
                                        selects.add(String.format(report.getSqlText(), columnSql , suffix, whereSql) + limit);
                                }
                        } else {
                                throw new BusinessException("表名为动态时,表名后缀至少有一个");
                        }
                } else {
                        selects.add(String.format(report.getSqlText(), columnSql , whereSql) + limit);
                }
                return this;
        }


        private String parameter(QueryItem queryItem, String object) {
                String sqlText;
                if (queryItem.is(Reports.QUERY_SHOW_TYPE_DATEAREA)) {
                        LocalDate[] ldts = Optional.ofNullable(object).map(this::localDates).orElse(this.localDates());
                        queryItem.setQueryValue(ldts);
                        sqlText = String.format(queryItem.getSqlText(), ldts[0].format(Globallys.DF_L_YMD), ldts[1].format(Globallys.DF_L_YMD));
                } else if (queryItem.is(Reports.QUERY_SHOW_TYPE_DATE)) {
                        LocalDate ldt = Optional.ofNullable(object).map(this::localDate).orElse(LocalDate.now());
                        queryItem.setQueryValue(ldt);
                        sqlText = String.format(queryItem.getSqlText(), ldt.format(Globallys.DF_L_YMD));
                } else {
                        queryItem.setQueryValue(object);
                        sqlText = Optional.ofNullable(object).map(o -> String.format(queryItem.getSqlText(), o)).orElse(StringUtils.EMPTY);
                }
                return sqlText;
        }

        public String replace(String source, Collection<ColumnValue> values){
                for (ColumnValue value : values) {
                        source = source.replace("{"+ value.getKey() +"}", String.valueOf(value.getOriginalValue()));
                }
                return source;
        }

        public Map<String, ColumnValue> toMap(ColumnValue[] cc){
                return Arrays.stream(cc).collect(Collectors.toMap(ColumnValue::getKey, c->c));
        }
        public Collection<ColumnValue> toCollection(ColumnValue[] cc){
                return Arrays.stream(cc).collect(Collectors.toList());
        }
        public Collection<ColumnValue> reduceCollection(Collection<ColumnValue[]> cc){
                Optional<ColumnValue[]> optional = cc.stream().reduce(this::calculate);
                if(optional.isPresent()){
                        return toCollection(optional.get());
                }
                return Collections.emptyList();
        }

        public ColumnValue[] reduceArray(Collection<ColumnValue[]> cc){
                return cc.stream().reduce(this::calculate).orElse(null);
        }

        public Collection<ColumnValue> collection(Collection<ColumnValue[]> columnValues){
                if(CollectionUtils.isNotEmpty(columnValues)){
                        Collection<ColumnValue>  values = reduceCollection(columnValues);
                        for (ColumnValue columnValue : values) {
                                ColumnItem ci = columns(columnValue.getKey());
                                columnValue.setLink(ci.getLinkUrl());
                                columnValue.setFormat(ci.getColumnFormat());
                                if(StringUtils.isNotBlank(ci.getScriptText())){
                                        columnValue.setScriptText(replace(ci.getScriptText(), values));
                                }
                        }
                        return values.stream().peek(this::execScript).peek(v->link(v, values)).peek(this::format).collect(Collectors.toList());
                }
                return Collections.emptyList();
        }

        public ColumnValue[] calculate(ColumnValue[] cc, ColumnValue[] vv){
                for (int i = 0; i < cc.length; i++) {
                        cc[i] = calculate(cc[i], vv[i]);
                }
                return cc;
        }

        public ColumnValue calculate(ColumnValue c, ColumnValue v){
                if(c.isCalculate()){
                        if(c.is(Reports.COLUMN_TYPE_DECIMAL) || c.is(Reports.COLUMN_TYPE_PERCENTAGE)){
                                c.setOriginalValue( (double) Optional.ofNullable(c.getOriginalValue()).orElse(0) +  (double) Optional.ofNullable(v.getOriginalValue()).orElse(0));
                        }else if(c.is(Reports.COLUMN_TYPE_INTEGER)){
                                c.setOriginalValue( (long) Optional.ofNullable(c.getOriginalValue()).orElse(0) +  (long) Optional.ofNullable(v.getOriginalValue()).orElse(0));
                        }else{
                                c.setOriginalValue(Optional.ofNullable(c.getOriginalValue()).orElse(v.getOriginalValue()));
                        }
                }
                return c;
        }

        public void  execScript(ColumnValue columnValue){
                try{
                        if(StringUtils.isNotBlank(columnValue.getScriptText())){
                                Object value = scriptEngine.eval(columnValue.getScriptText());
                                if(value != null){
                                        if("NaN".equalsIgnoreCase(value.toString())){
                                                columnValue.setScriptValue(0);
                                        }else{
                                                columnValue.setScriptValue(value);
                                        }
                                }
                        }
                }catch (ScriptException se){
                        se.printStackTrace();
                }finally {
                        if(columnValue.getScriptValue() == null){
                                columnValue.setScriptValue(columnValue.getOriginalValue());
                        }
                }
        }

        public void  format(ColumnValue cv){
                if(StringUtils.isNotBlank(cv.getFormat())){
                        if(cv.is(Reports.COLUMN_TYPE_INTEGER) || cv.is(Reports.COLUMN_TYPE_DECIMAL) || cv.is(Reports.COLUMN_TYPE_PERCENTAGE)){
                                cv.setFormatValue(new DecimalFormat(cv.getFormat()).format(cv.getScriptValue()));
                        }
                        if(cv.is(Reports.COLUMN_TYPE_DATE)){
                                cv.setFormatValue(((LocalDateTime) cv.getScriptValue()).format(DateTimeFormatter.ofPattern(cv.getFormat())));
                        }
                        if(cv.is(Reports.COLUMN_TYPE_STRING)){
                                cv.setFormatValue(String.format(cv.getFormat(), cv.getScriptValue()));
                        }
                }else{
                        cv.setFormatValue(String.valueOf(cv.getScriptValue()));
                }
        }

        public <T> T findOne(Collection<T> collection,  Predicate<T> handler){
                return collection.stream().filter(handler).findAny().orElse(null);
        }

        public void  link(ColumnValue cv, Collection<ColumnValue> values){
                if(StringUtils.isNotBlank(cv.getLink())){
                        cv.setLink(replace(cv.getLink(), values));
                }
        }

        public LocalDate localDate(String source){
                Matcher matcher = dateRegx.matcher(source);
                if(matcher.find()){
                        return LocalDate.parse(matcher.group(), Globallys.DF_L_YMD);
                }
                return null;
        }
        public LocalDate[] localDates(){
                LocalDate now = LocalDate.now();
                return new LocalDate[]{now, now};
        }
        public LocalDate[] localDates(String source){
                LocalDate[] localDates = new LocalDate[2];
                Matcher matcher = dateRegx.matcher(source);
                if(matcher.find()){
                        localDates[0] = LocalDate.parse(matcher.group(), Globallys.DF_L_YMD);
                }
                if(matcher.find()){
                        localDates[1] = LocalDate.parse(matcher.group(), Globallys.DF_L_YMD);
                }
                return localDates;
        }


        public Map<String, ColumnItem> columns() {
                return columns;
        }
        public ColumnItem columns(String columnKey) {
                if(columns != null && !columns.isEmpty()){
                        return columns.get(columnKey);
                }
                return null;
        }
        public Map<String, QueryItem> parameters() {
                return parameters;
        }
        public QueryItem parameters(String queryKey) {
                if(parameters != null && !parameters.isEmpty()){
                        return parameters.get(queryKey);
                }
                return null;
        }

        public Collection<String> getSelects() {
                return selects;
        }
}
