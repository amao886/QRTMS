package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.service.persistence.GoodsMapper;
import com.ycg.ksh.service.api.GoodsService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
/**
 *
 * TODO 货物明细业务实现类
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 15:32:34
 */
@Service("ksh.core.service.goodsService")
public class GoodsServiceImpl implements GoodsService {

	@Resource
	GoodsMapper goodsMapper;

	@Override
	public void save(Goods goods) throws BusinessException, ParameterException {
		goodsMapper.insertSelective(goods);
	}

	@Override
	public void saveBatch(Collection<Goods> goodsList) throws BusinessException, ParameterException {
		for (Goods goods : goodsList) {
			goods.setCreateTime(new Date());
			goods.setUpdateTime(new Date());
		}
		goodsMapper.insertBatch(goodsList);
	}

	@Override
	public void delete(Integer id) throws BusinessException, ParameterException {
		goodsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void update(Goods goods) throws BusinessException, ParameterException {
		goodsMapper.updateByPrimaryKeySelective(goods);
	}

	@Override
	public Goods queryById(Integer id) throws BusinessException, ParameterException {
		return goodsMapper.selectByPrimaryKey(id);
	}

	@Override
	public Collection<Goods> queryByWaybillId(Integer waybillId) throws BusinessException, ParameterException {
		   Example example = new Example(Goods.class);
	       Criteria criteria = example.createCriteria();
	       criteria.andEqualTo("waybillid", waybillId);
		return goodsMapper.selectByExample(example);
	}

}
