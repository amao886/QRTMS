package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.AppVersion;
import tk.mybatis.mapper.common.Mapper;

public interface AppVersionMapper extends Mapper<AppVersion> {
	/**
	 * 查询最新版本信息
	 * TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-20 14:40:26
	 * @return
	 */
	AppVersion queryLastEntity();
}