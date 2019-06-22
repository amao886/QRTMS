package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 区间
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
public class IntervalDto extends BaseEntity {

    private Integer startNum;
    private Integer endNum;

    public IntervalDto() {
    }

    public IntervalDto(Integer startNum, Integer endNum) {
        this.startNum = startNum;
        this.endNum = endNum;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }
}
