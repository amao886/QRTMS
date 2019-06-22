package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.SignedRelation;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.SignRecordSearch;
import com.ycg.ksh.entity.service.SignRelationRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface SignRelationService {

	final Logger logger = LoggerFactory.getLogger(SignRelationService.class);
	/**
	 * 
	 * TODO 添加签署方
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 14:03:46
	 * @param signedRelation
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public void saveSignRelation(SignedRelation signedRelation) throws ParameterException, BusinessException; 
	
	/**
	 * 
	 * TODO 修改签署方
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 14:04:23
	 * @param signedRelation
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public void updateSignRelation(SignedRelation signedRelation) throws ParameterException,BusinessException;
	/**
	 * 
	 * TODO 删除签署方
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 16:26:56
	 * @param key
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public void deleteSignRelation(Long key)throws ParameterException,BusinessException;
	/**
	 * TODO 签署查询
	 * @author wyj
	 * @date 2018/6/14 13:58
	 */
	CustomPage<SignRelationRes> listSignRecord(SignRecordSearch search, PageScope pageScope) throws BusinessException,ParameterException;


}
