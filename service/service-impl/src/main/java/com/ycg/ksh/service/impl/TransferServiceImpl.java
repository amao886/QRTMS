package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/3
 */

import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.enterprise.OrderShare;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.ShareOrderSearch;
import com.ycg.ksh.service.persistence.enterprise.OrderShareMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.OrderService;
import com.ycg.ksh.service.api.TransferService;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.util.O;
import com.ycg.ksh.service.observer.TransferObserverAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 分享、转包等业务逻辑
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/3
 */
@Service("ksh.core.service.transferService")
public class TransferServiceImpl implements TransferService, OrderObserverAdapter {


    @Resource
    CompanyService companyService;
    @Resource
    CustomerService customerService;

    @Resource
    OrderService orderService;
    @Resource
    OrderShareMapper orderShareMapper;



    @Autowired(required = false)
    Collection<TransferObserverAdapter> observers;

    /**
     * 联合订单数据
     *
     * @param uKey
     * @param alliance
     * @param flags
     * @throws BusinessException
     */
    @Override
    public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
        if (Arrays.binarySearch(flags, O.share) >= 0) {
            Company company = companyService.getCompanyByUserKey(uKey);
            if(company != null){
                OrderShare share = orderShareMapper.selectOne(new OrderShare(alliance.getId(), company.getId()));
                if(share != null){
                    alliance.setShareName(share.getShareCompanyName());
                }
            }
        }
    }

    /**
     * 分享订单
     *
     * @param uKey         操作人
     * @param orderKeys    要分享的订单编号
     * @param customerKeys 分享的对象(CompanyCustomer.key)
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑处理异常
     */
    @Override
    public void shareOrder(Integer uKey, Collection<Long> orderKeys, Collection<Long> customerKeys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notEmpty(orderKeys, "至少选择一个订单");
        Assert.notEmpty(customerKeys, "至少选择一个分享的对象");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            //将分享目标编号换成企业信息
            Collection<Company> receives = customerKeys.stream().distinct().map(k -> companyService.getCompanyByKey(k)).filter(c -> c != null).collect(Collectors.toList());
            Assert.notEmpty(receives, "没有满足条件的分享对象");

            //将分享订单编号换成订单信息
            Collection<Order> orders = orderKeys.stream().distinct().map(k -> orderService.getOrderByKey(k)).filter(o -> o != null).collect(Collectors.toList());
            Assert.notEmpty(orders, "没有满足条件的分享订单信息");

            Collection<OrderShare> shares = new ArrayList<OrderShare>(receives.size() * orders.size());
            OrderShare orderShare = new OrderShare();
            for (Company receive : receives) {
                for (Order order : orders) {
                    //已经分享的不需要再分享
                    if (orderShareMapper.selectCount(orderShare.modify(company.getId(), receive.getId(), order.getId())) > 0) {
                        continue;
                    }
                    shares.add(new OrderShare(Globallys.nextKey(), company.getId(), company.getCompanyName(), uKey, order.getId(), receive.getId(), receive.getCompanyName()));
                }
            }
            if (CollectionUtils.isEmpty(shares)) {
                throw new ParameterException("选择订单和分享对象已经分享过");
            }
            orderShareMapper.inserts(shares);
            //触发导入事件
            if (CollectionUtils.isNotEmpty(observers)) {
                for (TransferObserverAdapter observer : observers) {
                    observer.onShareOrders(company, receives, orders);
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("订单分享失败, uKey:{} orderKeys:{} customerKeys:{}", uKey, orderKeys, customerKeys);
            throw BusinessException.dbException("订单分享失败");
        }
    }

    /**
     * 分享查询
     *
     * @param shareSearch 查询条件
     * @param scope       分页信息
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑处理异常
     */
    @Override
    public CustomPage<OrderAlliance> pageOrderByShare(ShareOrderSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notNull(search, "查询条件不能为空");
        Assert.notBlank(search.getUserKey(), "操作用户编号不能为空");
        Company company = companyService.assertCompanyByUserKey(search.getUserKey());
        try {
            search.setCompanyKey(company.getId());
            search.setLikeString(StringUtils.trim(search.getLikeString()));
            Page<OrderAlliance> page = orderShareMapper.selectOrderBySomething(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
            return new CustomPage<OrderAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), orderService.alliance(search.getUserKey(), page, search.getFlags()));
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("查询分享订单信息异常", e);
        }
    }

    /**
     * 查询目标企业信息
     *
     * @param uKey       操作用户
     * @param type       查询类型(1:我分享的-发货方,2:我分享的-分享至企业,3:分享给我的-发货方,4:分享给我的-数据来源企业)
     * @param likeString
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<CompanyConcise> listTargets(Integer uKey, Integer type, String likeString) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户编号不能为空");
        Assert.notIn(type, new Integer[]{CoreConstants.SHARE_TARGET_FS, CoreConstants.SHARE_TARGET_FT, CoreConstants.SHARE_TARGET_RS, CoreConstants.SHARE_TARGET_RT}, "查询类型有误");
        likeString = StringUtils.trim(likeString);
        Company company = companyService.assertCompanyByUserKey(uKey);
        if (CoreConstants.SHARE_TARGET_FT == type || CoreConstants.SHARE_TARGET_RT == type) {
            return orderShareMapper.selectMySharingCompanyConcise(type, company.getId(), likeString);
        } else {
            return orderShareMapper.selectShipperCompanyConcise(type, company.getId(), likeString);
        }
    }
}
