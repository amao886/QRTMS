package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.WaybillReceiptStatus;
import tk.mybatis.mapper.common.Mapper;

/**
 * 运单纸质回单状态记录持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:29:55
 */
public interface WaybillReceiptStatusMapper extends Mapper<WaybillReceiptStatus> {

    /***
     * 增加回单扫描信息
     * @param waybillReceiptStatus
     * @return
     */
    void insertWaybillReceiptStatus(WaybillReceiptStatus waybillReceiptStatus);

}