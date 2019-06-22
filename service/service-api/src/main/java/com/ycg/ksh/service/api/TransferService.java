package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.ShareOrderSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 分享、转包等业务逻辑
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/3
 */
public interface TransferService {

    final Logger logger = LoggerFactory.getLogger(TransferService.class);

    /**
     * 分享订单
     * @param uKey 操作人
     * @param orderKeys  要分享的订单编号
     * @param customerKeys  分享的对象(CompanyCustomer.key)
     * @throws ParameterException  参数异常
     * @throws BusinessException   业务逻辑处理异常
     */
    void shareOrder(Integer uKey, Collection<Long> orderKeys, Collection<Long> customerKeys) throws ParameterException, BusinessException;


    /**
     * 分享查询
     * @param shareSearch  查询条件
     * @param scope  分页信息
     * @return
     * @throws ParameterException  参数异常
     * @throws BusinessException   业务逻辑处理异常
     */
    CustomPage<OrderAlliance> pageOrderByShare(ShareOrderSearch shareSearch, PageScope scope) throws ParameterException, BusinessException;


    /**
     * 查询目标企业信息
     * @param uKey 操作用户
     * @param type 查询类型(1:我分享的-发货方,2:我分享的-分享至企业,3:分享给我的-发货方,4:分享给我的-数据来源企业)
     * @param likeString
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CompanyConcise> listTargets(Integer uKey, Integer type, String likeString) throws ParameterException, BusinessException;

}
