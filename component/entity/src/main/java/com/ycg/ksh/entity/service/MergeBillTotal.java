package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.WaybillTotalView;

/**
 * 日统计查询类
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 13:52 2017/12/27
 * @Modified By:
 */
public class MergeBillTotal extends WaybillTotalView {

    private static final long serialVersionUID = 6228414298964820921L;
    private String startTime;//开始日期
    private String endTime;//结束日期
    private String year;//年
    private String month;//月
    private Integer flag;//统计标记0全部 ,1昨天，2最近7天，3最近三十天
    private String sort;//排序规则

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSort() {
        if (StringUtils.isEmpty(sort)) {
            sort = "asc";
        }
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
