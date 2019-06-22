package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.PaperyReceipt;
import com.ycg.ksh.entity.service.TimeCycle;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 纸质回单持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:47
 */
public interface PaperyReceiptMapper extends Mapper<PaperyReceipt> {

    /**
     * 查询库存回单信息
     * <p>
     *
     * @param receipt   查询条件
     * @param sendCycle 操作时间
     * @param bindCycle 绑定时间段(发货日期时间段)
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 16:49:18
     */
    List<PaperyReceipt> listInventoryReceipt(PaperyReceipt receipt, TimeCycle sendCycle, TimeCycle bindCycle);

    /**
     * 查询送交客户的回单
     * <p>
     *
     * @param receipt   查询条件
     * @param sendCycle 操作时间
     * @param bindCycle 绑定时间段(发货日期时间段)
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 16:49:14
     */
    List<PaperyReceipt> listPaperyReceipt(PaperyReceipt receipt, TimeCycle sendCycle, TimeCycle bindCycle);


    /**
     * 查询需要导出的回单
     *
     * @param receipt
     * @param bindCycle
     * @return
     * @author wangke
     * @date 2018/3/30 10:19
     */
    List<PaperyReceipt> exportReceiptImages(PaperyReceipt receipt, TimeCycle bindCycle);
}