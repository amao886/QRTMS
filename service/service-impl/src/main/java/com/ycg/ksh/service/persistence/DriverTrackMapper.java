package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.DriverTrack;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface DriverTrackMapper extends Mapper<DriverTrack> {
	
	/**
	 * 最后一次位置上报信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 16:56:20
	 * @param uKey
	 * @return
	 */
	DriverTrack selectLast(Integer uKey);
	
	/**
	 * 根据二维码号查询轨迹信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-13 16:17:24
	 * @param barcode
	 * @return
	 */
	Collection<DriverTrack> selectByBarcode(String barcode);
}