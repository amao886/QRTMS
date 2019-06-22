package com.ycg.ksh.service.api;

import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.SummaryWaybill;
import com.ycg.ksh.entity.service.WaybillSerach;

import java.util.Collection;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */
public interface SummaryService {

    /**
     * 任务单数据汇总
     * @param serach
     * @return
     */
    public Collection<SummaryWaybill> summaryWaybill(WaybillSerach serach);

    /**
     * 统计运单
     * @param search
     * @return
     */
    public SummaryWaybill summaryConveyance(ConveyanceSearch search);
}
