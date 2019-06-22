package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */

import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.SummaryWaybill;
import com.ycg.ksh.entity.service.WaybillSerach;
import com.ycg.ksh.service.persistence.SummaryMapper;
import com.ycg.ksh.service.api.SummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 数据汇总
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/21
 */
@Service("ksh.core.service.summaryService")
public class SummaryServiceImpl implements SummaryService {

    @Resource
    SummaryMapper summaryMapper;


    /**
     * 任务单数据汇总
     *
     * @param serach
     * @return
     */
    @Override
    public Collection<SummaryWaybill> summaryWaybill(WaybillSerach serach) {
        if(serach == null){
            serach = new WaybillSerach();
        }
        return summaryMapper.summaryWaybill(serach);
    }

    /**
     * 统计运单
     *
     * @param search
     * @return
     */
    @Override
    public SummaryWaybill summaryConveyance(ConveyanceSearch search) {
        if(search == null){
            search = new ConveyanceSearch();
        }
        return summaryMapper.summaryConveyance(search);
    }
}
