package com.ycg.ksh.entity.collecter.report;
/**
 *  报表工具
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public abstract class Reports {

    //数据类型(1:字符串,2:整数,3:小数,4:百分比,5:日期)
    public static final int COLUMN_TYPE_STRING = 1;
    public static final int COLUMN_TYPE_INTEGER = 2;
    public static final int COLUMN_TYPE_DECIMAL = 3;
    public static final int COLUMN_TYPE_PERCENTAGE = 4;
    public static final int COLUMN_TYPE_DATE = 5;

    //table_name_suffix 表名后缀(0:无，11:按月，待定)
    public static final int TABLE_NAME_SUFFIX_NO = 0;
    public static final int TABLE_NAME_SUFFIX_MONTH = 11;
    public static final int TABLE_NAME_SUFFIX_COMPANY = 21;

    //category  报表类别(1:列表,2:统计,3:待定)
    public static final int REPORT_CATEGORY_LIST = 1;
    public static final int REPORT_CATEGORY_REDUCE = 2;

    //show_type 展示类型(1:输入框,2:单选框,3:复选框,4:下拉列表,5:日期,6:时间段)
    public static final int QUERY_SHOW_TYPE_INPUT = 1;
    public static final int QUERY_SHOW_TYPE_RADIO = 2;
    public static final int QUERY_SHOW_TYPE_CHECKBOX = 3;
    public static final int QUERY_SHOW_TYPE_SELECT = 4;
    public static final int QUERY_SHOW_TYPE_DATE = 5;
    public static final int QUERY_SHOW_TYPE_DATEAREA = 6;

    //data_source  数据来源(0:不需要,1:静态json数据)
    public static final int QUERY_SOURCE_TYPE_NO =0;
    public static final int QUERY_SOURCE_TYPE_STATIC = 1;

}
