package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ImageStorage;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ImageStorageMapper extends Mapper<ImageStorage> {
	
	/**
	 * 批量插入
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 09:29:09
	 * @param collection
	 */
	void inserts(List<ImageStorage> collection);
}