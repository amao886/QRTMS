package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.WaybillTotalView;
import com.ycg.ksh.entity.service.MergeBillTotal;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

/**
 * 日统计mapper
 *
 * @Author：wangke
 * @description：
 * @Date：13:58 2017/12/27
 */
public interface WayBillTotalViewMapper extends Mapper<WaybillTotalView> {

    /**
     * 任务单日月分页查询统计
     *
     * @Author：wangke
     * @description：
     * @Date：14:03 2017/12/27
     */
    Page<WaybillTotalView> listPage(MergeBillTotal mergeBillTotal, RowBounds bounds);
}