package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.Goods;

import java.util.Collection;

/**
 * 
 * TODO 货物明细业务接口
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:02:23
 */
public interface GoodsService {
	/**
	 * 
	 * TODO 货物明细单条添加
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:19:19
	 * @param goods 货物明细
	 * @return 货物明细
	 * @throws BusinessException 业务异常
	 * @throws ParameterException 参数异常
	 */
	void save(Goods goods)throws BusinessException, ParameterException;
	/**
	 * 
	 * TODO 货物明细批量添加
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:19:55
	 * @param goods 货物明细
	 * @throws BusinessException 业务异常
	 * @throws ParameterException 参数异常
	 */
	void saveBatch(Collection<Goods> goods) throws BusinessException, ParameterException;
	/**
	 * 
	 * TODO 货物明细删除
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:20:11
	 * @param id
	 * @throws BusinessException 业务异常
	 * @throws ParameterException 参数异常
	 */
	void delete(Integer id) throws BusinessException, ParameterException;
	/**
	 * 
	 * TODO 货物明细修改
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:20:33
	 * @param goods 货物明细
	 * @throws BusinessException 业务异常
	 * @throws ParameterException 参数异常
	 */
	void update(Goods goods) throws BusinessException, ParameterException;
	/**
	 * 
	 * TODO 货物明细查询
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:22:20
	 * @param id 主键Id
	 * @return 对应货物对象
	 * @throws BusinessException 业务异常
	 * @throws ParameterException 参数异常
	 */
	Goods queryById(Integer id)throws BusinessException, ParameterException;
	/**
	 * 
	 * TODO 根据任务单主键查询货物明细
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:20:55
	 * @param waybillId 任务单主键
	 * @return 货物明细集合
	 * @throws BusinessException 业务异常
	 * @throws ParameterException 参数异常
	 */
	Collection<Goods> queryByWaybillId(Integer waybillId) throws BusinessException, ParameterException;
}
