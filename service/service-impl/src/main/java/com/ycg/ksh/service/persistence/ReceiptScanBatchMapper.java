package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ReceiptScanBatch;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 支持回单扫码批次持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:26:56
 */
public interface ReceiptScanBatchMapper extends Mapper<ReceiptScanBatch> {
    /**
     * 添加批次号
     *
     * @param batch
     * @return String DOM对象
     * @Exception 异常对象
     */
    int insertMapper(ReceiptScanBatch batch);


    /**
     * 查询批次号条数
     *
     * @param map
     * @return
     */
    List<ReceiptScanBatch> queryListGroup(Map<String, Object> map);


    /***
     * 根据运单ID和操作状态查询详情
     * @param receiptScanBatch
     * @return
     */
    ReceiptScanBatch queryReceiptScanBatch(ReceiptScanBatch receiptScanBatch);
}