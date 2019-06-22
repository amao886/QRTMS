package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.WaybillReceiptView;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

/**
 * 回单扫描视图
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 13:53 2017/11/27
 * @Modified By:
 */
public interface WaybillReceiptViewMapper extends Mapper<WaybillReceiptView> {


    /**
     * 分页查询当前用户的纸质回单
     *
     * @param map
     * @return
     */
    Page<WaybillReceiptView> queryListPage(Map<String, Object> map, RowBounds bounds);

    /**
     * 历史回单信息分页
     *
     * @Author：wangke
     * @description：
     * @Date：14:59 2017/11/28
     */
    Page<WaybillReceiptView> historyRecordList(Map<String, Object> map, RowBounds bounds);

}
