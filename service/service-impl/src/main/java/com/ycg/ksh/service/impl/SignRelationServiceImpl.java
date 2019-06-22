package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.CustomExample;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.SignedRelation;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.SignRecordSearch;
import com.ycg.ksh.entity.service.SignRelationRes;
import com.ycg.ksh.service.persistence.CompanyMapper;
import com.ycg.ksh.service.persistence.SignedRelationMapper;
import com.ycg.ksh.service.api.SignRelationService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service("ksh.core.service.signRelationService")
public class SignRelationServiceImpl implements SignRelationService {
	
	@Resource
	private SignedRelationMapper signRelationMapper;
	@Resource
	private CompanyMapper companyMapper;
	
	/**
	 * 新增签署方
	 * @see com.ycg.ksh.service.api.SignRelationService#saveSignRelation(SignedRelation)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 14:06:24
	 */
	@Override
	public void saveSignRelation(SignedRelation signedRelation) throws ParameterException, BusinessException {
		Assert.notBlank(signedRelation.getSignCompanyName(),"发货方不能为空");
		Assert.notNull(signedRelation.getStarttime(), "签署开始时间");
		Assert.notNull(signedRelation.getEndtime(), "签署结束时间");
		try {
			//根据当前用户编号查询发货方
			Company company = companyMapper.getCompanyByUserKey(signedRelation.getCreateId());
			if(company == null) throw new BusinessException("该用户没有关联企业信息");
			signedRelation.setCompanyName(company.getCompanyName());
			Assert.notNull(signedRelation.getCompanyName(),"签署方不能为空");
			signRelationMapper.insertSelective(signedRelation);
		}catch (ParameterException | BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("saveSignRelation====> signedRelation:{},error:{}",signedRelation, e);
			throw BusinessException.dbException("新增签署方异常");
		}
	}
	/**
	 * 修改签署方
	 * @see com.ycg.ksh.service.api.SignRelationService#updateSignRelation(SignedRelation)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 14:06:46
	 */
	@Override
	public void updateSignRelation(SignedRelation signedRelation) throws ParameterException, BusinessException {
		Assert.notBlank(signedRelation.getId(), "签署方关系编号不能为空");
		try {
			signRelationMapper.updateByPrimaryKeySelective(signedRelation);
		} catch (Exception e) {
			logger.error("updateSignRelation====> signedRelation:{},error:{}",signedRelation, e);
			throw BusinessException.dbException("修改签署方异常");
		}
	}
	/**
	 * 删除签署方
	 * @see com.ycg.ksh.service.api.SignRelationService#deleteSignRelation(java.lang.Long)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 16:29:27
	 */
	@Override
	public void deleteSignRelation(Long key) throws ParameterException, BusinessException {
		Assert.notBlank(key, "签署方关系编号不能为空");
		try {
			signRelationMapper.deleteByPrimaryKey(key);
		} catch (Exception e) {
			logger.error("deleteSignRelation====> key:{},error:{}",key, e);
			throw BusinessException.dbException("删除签署方异常");
		}
	}
	@Override
	public CustomPage<SignRelationRes> listSignRecord(SignRecordSearch search, PageScope pageScope) throws BusinessException,ParameterException {
		CustomExample example = createSignedRelationExample(search);
		RowBounds rowBounds = new RowBounds(pageScope.getPageNum(), pageScope.getPageSize());
		Page<SignedRelation> page = signRelationMapper.selectPageByExample(example, rowBounds);
		Collection<SignRelationRes> signRelationRes = null;
		try {
			signRelationRes = reverseSignRelation(page);
		} catch (Exception e) {
			throw new BusinessException("数据类型转换错误");
		}
		return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), signRelationRes);
	}
	public Collection<SignRelationRes> reverseSignRelation(Collection<SignedRelation> collection) throws Exception {
		Collection<SignRelationRes> result = Lists.newArrayList();
		for (SignedRelation relation : collection) {
			SignRelationRes res = new SignRelationRes();
			BeanUtils.build().copyProperties(res,relation);
			res.setCompanyId(companyMapper.getCompanyByName(res.getSignCompanyName()).getId());
			if (DateUtils.getDateBeforeDay(new Date(), 1).compareTo(res.getEndtime()) == 1) {   //过期了
				res.setStatus(false);
			} else{
				res.setStatus(true);
			}
			result.add(res);
		}
		return result;
	}

	public CustomExample createSignedRelationExample(SignRecordSearch search) {
		CustomExample example = new CustomExample(SignedRelation.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(search.getStartBeginTime())) {
			criteria.andGreaterThanOrEqualTo("starttime", search.getStartBeginTime());
		}
		if (StringUtils.isNotBlank(search.getStartEndTime())) {
			criteria.andLessThanOrEqualTo("starttime", search.getStartEndTime());
		}
		if (StringUtils.isNotBlank(search.getEndStartTime())) {
			criteria.andGreaterThanOrEqualTo("endtime", search.getEndStartTime());
		}
		if (StringUtils.isNotBlank(search.getEndEndTime())) {
			criteria.andLessThanOrEqualTo("endtime", search.getEndEndTime());
		}

		criteria.andLike("companyName", String.valueOf(search.getLikeString()));

//        if (search.getLikeString().matches("[0-9]+")) { //暂时不处理公司编号的情况
//        }

		switch (Optional.ofNullable(search.getStatus()).orElse(- 1)) {
			case 1: //未到期
				criteria.orGreaterThan("endtime", DateUtils.getDateBeforeDay(new Date(), 1));
				break;
			case 2://已到期
				criteria.andLessThan("endtime", new Date());
				break;
			default:
				//其他状态??nothing
		}
		return example;
	}
}
