package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.enterprise.Complaint;
import com.ycg.ksh.entity.service.ConciseUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.ComplaintAlliance;
import com.ycg.ksh.entity.service.enterprise.ComplaintSearch;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.persistence.OrderMapper;
import com.ycg.ksh.service.persistence.enterprise.ComplaintMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.ComplaintService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 投诉管理实现类
 *
 * @author: wangke
 * @create: 2018-10-30 16:07
 **/
@Service("ksh.core.service.complaintService")
public class ComplaintServiceImpl implements ComplaintService , OrderObserverAdapter {

    @Resource
    OrderMapper orderMapper;

    @Resource
    UserService userService;

    @Resource
    ComplaintMapper complaintMapper;

    @Resource
    CompanyService companyService;

    @Override
    public void modifyComplaint(Integer userKey, String content, Collection<Long> orderKeys) throws ParameterException, BusinessException {
        Assert.notEmpty(orderKeys, "至少选择一项需要投诉的订单");
        Assert.notBlank(content, "投诉内容不能为空");
        Assert.notBlank(userKey, "用户编号不能为空");
        try {
            Collection<Order> modifyCommit = new ArrayList<Order>();
            Collection<Complaint> saveCommit = new ArrayList<Complaint>();
            if (CollectionUtils.isNotEmpty(orderKeys)) {
                orderKeys.forEach(o -> {
                    Complaint complaint = new Complaint(content);
                    if (assertComplaintByOrderKey(o)) {
                        Order order = orderMapper.selectByPrimaryKey(o);
                        if (null == order) {
                            throw new BusinessException("订单不能存在");
                        }
                        ConciseUser conciseUser = userService.getConciseUser(userKey);
                        if (null == conciseUser) {
                            throw new BusinessException("用户信息查询异常");
                        }
                        saveCommit.add(initComplaint(complaint, conciseUser, order));
                        order.setIsComplaint(true);
                        modifyCommit.add(order);
                    }
                });
            }
            if (CollectionUtils.isNotEmpty(saveCommit)) {
                complaintMapper.inserts(saveCommit);
            }
            if (CollectionUtils.isNotEmpty(modifyCommit)) {
                orderMapper.updates(modifyCommit);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("订单投诉异常 userKey:{} orderKeys:{} content:{}", userKey, orderKeys, content);
            throw BusinessException.dbException("订单投诉异常");
        }
    }

    public boolean assertComplaintByOrderKey(Long orderKey) {
        Complaint complaint = new Complaint(orderKey);
        complaint = complaintMapper.selectOne(complaint);
        if (null != complaint && complaint.getKey() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public CustomPage<ComplaintAlliance> pageComplaint(ComplaintSearch search, PageScope pageScope) throws ParameterException, BusinessException {
        Company company = null;
        Company ownCompany = companyService.assertCompanyByUserKey(search.getUesrKey());
        if (StringUtils.isNotBlank(search.getLikeString())) {
            company = companyService.getCompanyByName(search.getLikeString());
        }
        Page<ComplaintAlliance> page = null;
        if (search.getPartnerType().shipper()) {
            search.setShipperId(ownCompany.getId());
            if (null != company) {
                search.setReceiveId(company.getId());
            }
            page = complaintMapper.complaintsShipper(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        } else if (search.getPartnerType().receive()) {
            search.setReceiveId(ownCompany.getId());
            if (null != company) {
                search.setShipperId(company.getId());
            }
            page = complaintMapper.complaintReceive(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        } else {
            search.setConveyId(ownCompany.getId());
            if (null != company) {
                search.setReceiveId(company.getId());
            }
            page = complaintMapper.complaintsConvey(search, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        }
        return new CustomPage<ComplaintAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    public Complaint initComplaint(Complaint complaint, ConciseUser conciseUser, Order order) {
        complaint.setKey(Globallys.nextKey());
        complaint.setComplainant(conciseUser.getUnamezn());
        complaint.setComplainantNumber(conciseUser.getMobilephone());
        complaint.setOrderId(order.getId());
        complaint.setCreateTime(new Date());
        complaint.setShipperId(order.getShipperId());
        complaint.setReceiveId(order.getReceiveId());
        complaint.setConveyId(order.getConveyId());
        complaint.setShipperName(companyService.getCompanyByKey(order.getShipperId()).getCompanyName());
        complaint.setReceiveName(companyService.getCompanyByKey(order.getReceiveId()).getCompanyName());
        complaint.setConveyName(companyService.getCompanyByKey(order.getConveyId()).getCompanyName());
        return complaint;
    }

    @Override
    public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
        if (Arrays.binarySearch(flags, O.complaint) >= 0) {
            alliance.setComplaint(complaintMapper.selectOne(new Complaint(alliance.getId())));
            alliance.setIsComplaint(Optional.ofNullable(alliance.getComplaint()).isPresent());
        }
    }
}
