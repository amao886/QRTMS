package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.Goods;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.Map;
/**
 * 
 * TODO 货物明细持久类
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 14:48:26
 */
public interface GoodsMapper extends Mapper<Goods> {
	/**
	 * 
	 * TODO 批量插入货物信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 16:46:14
	 * @param goods
	 */
	void insertBatch(Collection<Goods> goods);
	
	/**
	 * 根据运单编号删除货物信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-26 13:24:06
	 * @param waybillId
	 */
	void deleteByWaybillId(Integer waybillId);
	
	/**
	 * 查询货物信息总和
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-04 11:57:49
	 * @param waybillId
	 * @return {quantity:数量, weight:重量, volume:体积}
	 */
	Map<String, Number> sumByWaybillID(Integer waybillId);
}