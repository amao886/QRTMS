package com.ycg.ksh.entity.collecter;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 条码生成
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */
public class GenerateBarcode extends BaseEntity {

    private long dayString;
    private long maxCode;
    private long totalCount;

    public GenerateBarcode() {
    }

    public GenerateBarcode(long dayString, long maxCode, long totalCount) {
        this.dayString = dayString;
        this.maxCode = maxCode;
        this.totalCount = totalCount;
    }

    public long getDayString() {
        return dayString;
    }

    public void setDayString(long dayString) {
        this.dayString = dayString;
    }

    public long getMaxCode() {
        return maxCode;
    }

    public void setMaxCode(long maxCode) {
        this.maxCode = maxCode;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
