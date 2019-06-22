package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.entity.RequestSerial;
import com.ycg.ksh.entity.persistent.SysRequestSerial;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

/**
 * 用户请求行为数据持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:27:33
 */
public interface SysRequestSerialMapper extends Mapper<SysRequestSerial> {
	/**
	 * 
	 * TODO 分页查询
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-28 15:21:40
	 * @param serial
	 * @param rowBounds
	 * @return
	 */
	Page<SysRequestSerial> queryPage(RequestSerial serial,RowBounds rowBounds);
}