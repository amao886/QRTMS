package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.WaybillDriverScan;
import tk.mybatis.mapper.common.Mapper;

/**
 * 运单司机扫描持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:28:58
 */
public interface WaybillDriverScanMapper extends Mapper<WaybillDriverScan> {
	
	/**
	 * 查询
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 11:10:58
	 * @param waybillId
	 * @param userId
	 * @return
	 */
	WaybillDriverScan selectByUserId(Integer waybillId, Integer userId);
}