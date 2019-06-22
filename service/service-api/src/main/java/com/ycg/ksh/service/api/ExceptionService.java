/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 13:56:59
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.TransitionException;
import com.ycg.ksh.entity.persistent.WaybillException;
import com.ycg.ksh.entity.service.ExceptionSearch;
import com.ycg.ksh.entity.service.MergeExceptionRepor;
import com.ycg.ksh.entity.service.MergeWaybillException;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * 异常业务逻辑接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 13:56:59
 */
public interface ExceptionService {

	final Logger logger = LoggerFactory.getLogger(ExceptionService.class);

	/**
	 * 批量保存临时异常
	 * <p>
	 * @param exception
	 * @param paths
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void saveException(TransitionException exception, Collection<String> paths, boolean fromWx) throws ParameterException, BusinessException;
	/**
	 * 保存运单异常信息
	 * <p>
	 * @param exception  异常信息
	 * @param images 异常图片路径信息
	 * @param fromWx     是否要从微信服务器下载
	 * @throws ParameterException 参数异常
	 * @throws BusinessException  逻辑异常
	 */
	void saveWaybillException(WaybillException exception, Collection<String> images, boolean fromWx) throws ParameterException, BusinessException;
	
	/**
	 * 根据运单编号查询异常信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 08:39:04
	 * @param waybillId
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	List<MergeWaybillException> listByWaybillId(Integer waybillId) throws ParameterException, BusinessException;

	/**
	 * 分页查询异常信息列表
	 *
	 * @param search
	 * @return
	 */
	CustomPage<MergeExceptionRepor> pageConveyanceException(Integer ukey, ExceptionSearch search, PageScope scope) throws ParameterException, BusinessException;

}
