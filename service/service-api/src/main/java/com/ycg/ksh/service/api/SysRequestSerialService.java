/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:02:22
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.RequestSerial;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.SysRequestSerial;
import com.ycg.ksh.entity.service.PageScope;

/**
 * 用户请求数据记录接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:02:22
 */
public interface SysRequestSerialService {

	/**
	 * 保存用户请求数据
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:03:31
	 * @param sysRequestSerial 请求数据
	 * @throws ParameterException 参数异常
	 * @throws BusinessException  逻辑异常
	 */
	void save(SysRequestSerial sysRequestSerial) throws ParameterException, BusinessException;
	/**
	 * 
	 * TODO 分页查询
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-28 15:18:30
	 * @param serial 查询条件
	 * @param pageScope 分页条件
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	CustomPage<SysRequestSerial> queryPageList(RequestSerial serial,PageScope pageScope) throws ParameterException, BusinessException;
}
