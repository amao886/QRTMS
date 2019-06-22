package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * 零担计价配置
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class ValuationDto extends BaseEntity {

    private Integer goodsNature; // 货物性质: 0：不限 1：重货 2：轻货
    private Integer goodsCalculMode; // 计价方式: 0：重量 1：体积 2：数量
    private Integer goodsUnit;//货物单位: 0：吨 1：千克/公斤 2：方 3：件
    private LocalDate useStartDate; // 开始使用日期
    private LocalDate useEndDate; // 结束使用日期

    private IntervalDto[] intervals;//区间配置
    private List<IntervalPriceDto> freightList;//单价数据数组

    public ValuationDto() {
    }

    public ValuationDto(Integer goodsNature, Integer goodsCalculMode, Integer goodsUnit, LocalDate useStartDate, LocalDate useEndDate, IntervalDto[] intervals, List<IntervalPriceDto> freightList) {
        this.goodsNature = goodsNature;
        this.goodsCalculMode = goodsCalculMode;
        this.goodsUnit = goodsUnit;
        this.useStartDate = useStartDate;
        this.useEndDate = useEndDate;
        this.intervals = intervals;
        this.freightList = freightList;
    }

    public Integer getGoodsNature() {
        return goodsNature;
    }

    public void setGoodsNature(Integer goodsNature) {
        this.goodsNature = goodsNature;
    }

    public Integer getGoodsCalculMode() {
        return goodsCalculMode;
    }

    public void setGoodsCalculMode(Integer goodsCalculMode) {
        this.goodsCalculMode = goodsCalculMode;
    }

    public Integer getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(Integer goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public LocalDate getUseStartDate() {
        return useStartDate;
    }

    public void setUseStartDate(LocalDate useStartDate) {
        this.useStartDate = useStartDate;
    }

    public LocalDate getUseEndDate() {
        return useEndDate;
    }

    public void setUseEndDate(LocalDate useEndDate) {
        this.useEndDate = useEndDate;
    }

    public IntervalDto[] getIntervals() {
        return intervals;
    }

    public void setIntervals(IntervalDto[] intervals) {
        this.intervals = intervals;
    }

    public List<IntervalPriceDto> getFreightList() {
        return freightList;
    }

    public void setFreightList(List<IntervalPriceDto> freightList) {
        this.freightList = freightList;
    }
}
