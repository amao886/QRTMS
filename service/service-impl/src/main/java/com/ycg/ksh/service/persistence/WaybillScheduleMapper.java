package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.WaybillSchedule;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

public interface WaybillScheduleMapper extends Mapper<WaybillSchedule> {
	/**
     * 批量保存
     * @Author：wangke
     * @description：
     * @Date：8:51 2017/11/24
     */
	void saveBatchSchedule(List<WaybillSchedule> list);

    /**
     * 列表查询
     *
     * @Author：wangke
     * @description：
     * @Date：8:51 2017/11/24
     */
    Collection<WaybillSchedule> queryWaybillScheduleList(WaybillSchedule waybillSchedule);

    /**
     * 分页查询
     *
     * @Author：wangke
     * @description：
     * @Date：8:52 2017/11/24
     */
    Page<WaybillSchedule> queryWaybillScheduleList(WaybillSchedule waybillSchedule, RowBounds bounds);
}