package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.FenceGrate;
import tk.mybatis.mapper.common.Mapper;

public interface FenceGrateMapper extends Mapper<FenceGrate> {
	
	/**
	 * 根据客户编号查询电子围栏半径
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-28 17:18:38
	 * @param customerKey
	 * @return
	 */
	Double getRadiusByCustomerKey(Integer customerKey);
}