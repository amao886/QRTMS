package com.ycg.ksh.service.api;

import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.WaybillTotalView;
import com.ycg.ksh.entity.service.MergeBillTotal;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务单统计接口
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 10:25 2017/12/27
 * @Modified By:
 */
public interface WayBillTotalService {
    final Logger logger = LoggerFactory.getLogger(WayBillTotalService.class);

    /**
     * 任务单日月分页查询统计
     *
     * @param mergeBillTotal 查询条件
     * @return
     */
    CustomPage<WaybillTotalView> queryTotalPage(MergeBillTotal mergeBillTotal, PageScope pageScope);

}
