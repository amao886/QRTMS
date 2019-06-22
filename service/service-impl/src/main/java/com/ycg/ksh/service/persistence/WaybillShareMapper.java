package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.WaybillShare;
import tk.mybatis.mapper.common.Mapper;

/**
 * 运单分享持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:30:33
 */
public interface WaybillShareMapper extends Mapper<WaybillShare> {
	
	WaybillShare selectByUserId(Integer waybillId, Integer userId);
}