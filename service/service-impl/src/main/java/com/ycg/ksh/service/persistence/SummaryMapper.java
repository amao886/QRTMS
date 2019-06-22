package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.SummaryWaybill;
import com.ycg.ksh.entity.service.WaybillSerach;

import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */
public interface SummaryMapper extends Mapper<SummaryWaybill>{

    /**
     * 任务单数据汇总
     * @param serach
     * @return
     */
    public Collection<SummaryWaybill> summaryWaybill(WaybillSerach serach);


    public SummaryWaybill summaryConveyance(ConveyanceSearch serach);
}
