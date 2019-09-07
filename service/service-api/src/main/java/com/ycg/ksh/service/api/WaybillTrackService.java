/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:49:09
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.WaybillTrack;
import com.ycg.ksh.entity.service.MergeTrack;

import java.util.List;

/**
 * 运单轨迹业务逻辑接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:49:09
 */
public interface WaybillTrackService {

	/**
	 * 位置上报
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 16:24:30
	 * @param userId 上报人编号
	 * @param waybillTrack 轨迹信息
	 * @throws ParameterException 参数异常
	 * @throws BusinessException  逻辑异常
	 */
	void saveLoactionReport(Integer userId, WaybillTrack waybillTrack) throws ParameterException, BusinessException;


	/**
	 * 上报位置
	 * @param userId
	 * @param barcode
	 * @param waybillTrack
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void saveLoactionReport(Integer userId, String barcode, WaybillTrack waybillTrack) throws ParameterException, BusinessException;
	/**
	 * 保存运单轨迹信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:50:59
	 * @param waybillTrack 轨迹信息
	 * @return
	 * @throws ParameterException 参数异常
	 * @throws BusinessException  逻辑异常
	 */
	WaybillTrack save(WaybillTrack waybillTrack) throws ParameterException, BusinessException;
	
	/**
	 * 根据运单编号查询运单轨迹信息（扫描位置）
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:46:19
	 * @param waybillId  运单编号
	 * @return
	 * @throws ParameterException 参数异常
	 * @throws BusinessException  逻辑异常
	 */
	List<MergeTrack> listByWaybillId(Integer waybillId) throws ParameterException, BusinessException;
}
