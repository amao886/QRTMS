package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.common.constant.Constants;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.service.util.P;
import com.ycg.ksh.service.util.Transform;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.CustomDataType;
import com.ycg.ksh.constant.OrderEventType;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;
import com.ycg.ksh.entity.persistent.plan.PlanDesignate;
import com.ycg.ksh.entity.persistent.plan.PlanOrder;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.*;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.entity.service.plan.PlanSearch;
import com.ycg.ksh.entity.service.plan.PlanTemplate;
import com.ycg.ksh.service.persistence.enterprise.OrderExtraMapper;
import com.ycg.ksh.service.persistence.plan.PlanCommodityMapper;
import com.ycg.ksh.service.persistence.plan.PlanDesignateMapper;
import com.ycg.ksh.service.persistence.plan.PlanOrderMapper;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.observer.PlanOrderObserverAdapter;
import com.ycg.ksh.service.observer.TemplateObserverAdapter;
import com.ycg.ksh.service.support.assist.ObjectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发货计划
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */
@Service("ksh.core.service.planOrderService")
public class PlanOrderServiceImpl implements PlanOrderService, TemplateObserverAdapter {


    @Resource
    private MessageQueueService queueService;
    @Resource
    private PlanOrderMapper planOrderMapper;
    @Resource
    private PlanCommodityMapper commodityMapper;
    @Resource
    private PlanDesignateMapper designateMapper;
    @Resource
    private OrderExtraMapper orderExtraMapper;
    @Resource
    private CompanyService companyService;
    @Resource
    private CustomerService customerService;
    @Resource
    private OrderService orderService;
    @Resource
    private TemplateService templateService;
    @Resource
    CustomDataService customDataService;
    @Autowired(required = false)
    Collection<PlanOrderObserverAdapter> observers;


    /**
     * 指定模板导入数据时触发的事件
     *
     * @param context   模板上下文
     * @param propertys 解析后的数据
     * @throws MessageException 处理解析后的数据异常时,所有异常必须转换成 MessageException 异常抛出，否则可能会引起事务回滚
     */
    @Override
    public void importSomething(TemplateContext context, Collection<PropertyDescribe> propertys) throws MessageException {
        if (context.is(Constants.TEMPLATE_CATEGORY_PLAN) && CollectionUtils.isNotEmpty(propertys)) {
            try {
                Company company = companyService.getCompanyByKey(context.getCompanyKey());//当前操作人的企业信息
                //订单数据
                PlanTemplate template = ObjectUtils.reduceObject(PlanTemplate.class, propertys);
                if (template != null) {
                    Assert.notBlank(template.getPlanNo(), "计划单号不能为空");
                    template.setUniqueKey(context.getUniqueKey());
                    PlanOrder order = assemblyOrderByTemplate(context.getOperatorKey(), context.getPartnerType(), company, template, true);
                    OrderExtra orderExtra = ObjectUtils.reduceObject(OrderExtra.class, propertys);//额外的数据
                    Collection<PlanCommodity> commodities = ObjectUtils.collectObjects(PlanCommodity.class, propertys);//明细数据
                    if (planOrderMapper.insert(order) > 0) {
                        //保存额外信息
                        saveExtra(orderExtra, template, company);
                        //保存货物明细
                        saveCommodities(context.getOperatorKey(), order.getId(), commodities, order.getCreateTime());
                    }
                }
            } catch (MessageException e) {
                throw e;
            } catch (BusinessException | ParameterException e) {
                throw new MessageException(e.getFriendlyMessage(), e);
            } catch (Exception e) {
                logger.error("发货计划数据保存异常 --> context:{} propertys:{}", context, propertys, e);
                throw new MessageException("发货计划数据保存异常", e);
            }
        }
    }

    @Override
    public Collection<PlanCommodity> listCommodity(Integer uKey, Long planKey) throws ParameterException, BusinessException {
        Assert.notBlank(planKey, "计划编号不能为空");
        Example example = new Example(PlanCommodity.class);
        example.createCriteria().andEqualTo("planId", planKey);
        return commodityMapper.selectByExample(example);
    }

    private void saveCommodities(Integer uKey, Long oKey, Collection<PlanCommodity> commodities, Date createTime) {
        Collection<PlanCommodity> collection = commodities.stream().filter(c -> !P.isEmptyCommodity(c)).collect(Collectors.toList());
        Assert.notEmpty(collection, "货物信息不能为空");
        Collection<Long> commodityKeys = commodityMapper.listKeyByPlanKey(oKey);
        for (PlanCommodity commodity : collection) {
            commodity.setPlanId(oKey);
            commodity.setUpdateTime(createTime);
            if (commodity.getId() != null && commodity.getId() > 0) {
                commodityMapper.updateByPrimaryKeySelective(commodity);
                commodityKeys.remove(commodity.getId());
            } else {
                commodity.setId(Globallys.nextKey());
                commodity.setCreateUserId(uKey);
                commodity.setCreateTime(commodity.getUpdateTime());
                commodityMapper.insertSelective(commodity);
            }
        }
        if (CollectionUtils.isNotEmpty(commodityKeys)) {
            commodityMapper.deleteByIdentities(commodityKeys);
        }
    }


    private boolean validateExistByPlanNo(Long pid, Long companyKey, Long shipperKey, String planNo) {
        if (StringUtils.isNotBlank(planNo)) {
            Example example = new Example(PlanOrder.class);
            Example.Criteria criteria = example.createCriteria().andNotEqualTo("id", pid).andEqualTo("planNo", planNo);
            if (shipperKey == null || shipperKey <= 0) {
                criteria.andEqualTo("companyKey", companyKey);
            } else {
                criteria.andEqualTo("shipperId", shipperKey);
            }
            return planOrderMapper.selectCountByExample(example) > 0;
        }
        return false;
    }

    private PlanOrder assemblyOrderByTemplate(Integer uKey, PartnerType partnerType, Company company, PlanTemplate template, Boolean insert) throws MessageException {
        if (StringUtils.isBlank(template.getReceiverName())) {
            throw new MessageException("收货联系人不能为空");
        }
        if (StringUtils.isBlank(template.getReceiveAddress())) {
            throw new MessageException("收货地址不能为空");
        }
        if (CollectionUtils.isNotEmpty(observers)) {
            for (PlanOrderObserverAdapter observer : observers) {
                observer.initializeTemplate(uKey, partnerType, company.getId(), template, insert);
            }
        }
        Date cdate = new Date();
        PlanOrder order = new PlanOrder();
        order.setId(template.getUniqueKey());
        order.setCompanyKey(company.getId());
        order.setReceiveId(Optional.ofNullable(template.getReceive()).map(CustomerConcise::getId).orElseThrow(() -> new MessageException("收货客户信息不能为空")));
        if (partnerType.shipper()) {
            order.setShipperId(0L);
        } else {
            order.setShipperId(Optional.ofNullable(template.getShipper()).map(CustomerConcise::getId).orElseThrow(() -> new MessageException("发货客户信息不能为空")));
        }
        order.setShipperId(Optional.ofNullable(order.getShipperId()).orElse(0L));
        order.setPlanNo(template.getPlanNo());
        if (validateExistByPlanNo(order.getId(), company.getId(), order.getShipperId(), order.getPlanNo())) {
            throw new MessageException("对应的计划单号[" + template.getPlanNo() + "]已经存在");
        }
        order.setOrderKey(0L);
        order.setOrderNo(template.getOrderNo());
        order.setReceiverName(template.getReceiverName());
        order.setReceiverContact(template.getReceiverContact());
        order.setReceiveAddress(template.getReceiveAddress());
        order.setRemark(template.getRemark());
        order.setUpdateTime(cdate);
        order.setDeliveryTime(template.getDeliveryTime());
        order.setArrivalTime(template.getArrivalTime());
        order.setCollectTime(template.getCollectTime());
        order.setTransportRoute(template.getTransportRoute());
        order.setUserKey(uKey);

        if (insert) {
            order.setCreateTime(cdate);
            order.setUserKey(uKey);
            order.setCarStatus(CoreConstants.CAR_STATUS_NO);
        }
        return order;
    }

    /**
     * 联合计划详情
     *
     * @param uKey
     * @param planKey
     * @param flags
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public PlanAlliance alliance(Integer uKey, Long planKey, Integer... flags) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notBlank(planKey, "发货计划编号不能为空");
        flags = Optional.ofNullable(flags).orElse(P.DETAILS);
        return alliance(uKey, planOrderMapper.selectByPrimaryKey(planKey), flags);
    }

    public PlanAlliance alliance(Integer uKey, PlanOrder order, Integer... flags) throws BusinessException {
        try {
            Company company = companyService.getCompanyByUserKey(uKey);
            if (company == null) {
                return null;
            }
            Long lastCompanyKey = company.getId();
            PlanAlliance alliance = PlanAlliance.buildAlliance(order);
            alliance.setAllocate(company.getId() - alliance.getCompanyKey() != 0);//不是创建的就是指派的
            if (CollectionUtils.isNotEmpty(observers) && ArrayUtils.isNotEmpty(flags)) {
                Arrays.sort(flags);
                //加载明细数据
                if (Arrays.binarySearch(flags, P.commodities) >= 0) {
                    alliance.setCommodities(listCommodity(uKey, alliance.getId()));
                }
                //记载额外数据
                if (Arrays.binarySearch(flags, P.extradata) >= 0) {
                    alliance.setExtra(orderExtraMapper.selectByType(Constants.ORDER_EXTRA_PLAN, alliance.getId()));
                }
                //加载指派数据
                if (Arrays.binarySearch(flags, P.designate) >= 0) {
                    PlanDesignate designate = alliance.getDesignate();
                    if (designate == null) {
                        designate = designateMapper.getByLastCompanyKey(lastCompanyKey, alliance.getId());
                    }
                    if (designate != null) {
                        alliance.setTarget(companyService.getCompanyConciseByKey(designate.getCompanyKey()));
                    }
                    alliance.setDesignate(designate);
                }
                if (alliance.isAllocate() && Arrays.binarySearch(flags, P.accept) >= 0) {
                    PlanDesignate accept = designateMapper.getByCompanyKey(company.getId(), alliance.getId());
                    if (accept != null) {
                        alliance.setSource(companyService.getCompanyConciseByKey(accept.getLastCompanyKey()));
                    }
                    alliance.setAccept(accept);
                }
                for (PlanOrderObserverAdapter observerAdapter : observers) {
                    observerAdapter.allianceOrder(uKey, alliance, flags);
                }
            }
            return alliance;
        } catch (Exception e) {
            throw new BusinessException("获取订单信息异常", e);
        }
    }

    @Override
    public void generate(Integer uKey, Long planKey, OrderTemplate template, OrderExtra orderExtra, Collection<OrderCommodity> commodities, Collection<CustomData> customDatas, PartnerType partner) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notBlank(planKey, "发货计划编号不能为空");
        try {
            PlanOrder order = planOrderMapper.selectByPrimaryKey(planKey);
            if(order == null){
                throw  new BusinessException("指定的计划编号未找到");
            }
            template.setUniqueKey(Globallys.nextKey());
            if (CollectionUtils.isNotEmpty(customDatas)) {
                template.setCustomDatas(customDatas.stream().peek(c -> c.setKey(0L)).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(commodities)) {
                template.setCommodities(commodities.stream().peek(c -> c.setId(0L)).collect(Collectors.toList()));
            }
            orderExtra.setKey(0L);
            if(order.getCollectTime() != null){
                template.setCollectTime(order.getCollectTime());
            }
            orderService.saveOrder(uKey, partner, template, OrderEventType.CREATE_GENERATE);

            planOrderMapper.updateByPrimaryKeySelective(new PlanOrder(planKey, template.getUniqueKey()));

        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("生成发货单异常", e);
        }
    }

    /**
     * 生成发货单
     *
     * @param uKey     操作人编号
     * @param planKeys 发货计划编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void generates(Integer uKey, PartnerType partner, Collection<Long> planKeys, Long templateKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notEmpty(planKeys, "至少选择一项要生成发货单的发货计划");
        //Assert.notBlank(templateKey, "发货模板编号不能为空");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            //过滤掉已经生成发货单的发货计划 或者没有分配物流商的，并加载其他数据
            Collection<PlanOrder> planOrderCollection = planOrderMapper.selectByIdentities(planKeys);
            Collection<PlanAlliance> collection = planOrderCollection.stream().filter(p -> (p.getOrderKey() == null || p.getOrderKey() <= 0) && (p.getCompanyKey() - company.getId() == 0)).map(p -> alliance(uKey, p, P.DETAILS)).filter(p -> p.getConvey() != null).collect(Collectors.toList());
            Assert.notEmpty(collection, "至少选择一项未生成发货单的发货计划");
            Collection<PlanOrder> updateOrders = new ArrayList<PlanOrder>(collection.size());
            for (PlanAlliance planAlliance : collection) {
                OrderTemplate template = Transform.orderTemplateform(planAlliance);
                template.setOrderExtra(Transform.orderExtraform(planAlliance.getExtra()));
                template.setCommodities(Optional.ofNullable(planAlliance.getCommodities()).map(cc -> {
                    return cc.stream().map(Transform::orderCommodityform).collect(Collectors.toList());
                }).orElse(Collections.emptyList()));
                template.setCustomDatas(Optional.ofNullable(planAlliance.getCustomDatas()).map(cc -> {
                    return cc.stream().map(Transform::customDataform).collect(Collectors.toList());
                }).orElse(Collections.emptyList()));
                template.setUniqueKey(Globallys.nextKey());
                updateOrders.add(new PlanOrder(planAlliance.getId(), template.getUniqueKey()));
                orderService.saveOrder(uKey, partner, template, OrderEventType.CREATE_GENERATE);
            }
            planOrderMapper.updates(updateOrders);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("批量生成发货单异常", e);
        }
    }

    @Override
    public CustomPage<PlanAlliance> pagePlanOrder(PartnerType partner, PlanSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notBlank(search.getuKey(), "用户编号不能为空");
        Company company = companyService.assertCompanyByUserKey(search.getuKey());
        try {
            search.setCompanyId(company.getId());
            search.setPartner(partner.getCode());
            Page<PlanOrder> planOrders = null;
            if (search.getLastCompnayKey() == null || search.getLastCompnayKey() < 0) {
                //search.setOrdinateState(null);
                search.ordinateState(false);
                planOrders = planOrderMapper.listByShipper(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
            } else {
                search.ordinateState(true);
                search.setLastCompnayKey(Math.max(search.getLastCompnayKey(), 0L));
                planOrders = planOrderMapper.listByConveyer(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
            }
            Collection<PlanAlliance> pageAlliance = new ArrayList<PlanAlliance>(planOrders.size());
            for (PlanOrder orders : planOrders) {
                pageAlliance.add(alliance(search.getuKey(), PlanAlliance.buildAlliance(orders), P.LISTS));
            }
            return new CustomPage<PlanAlliance>(planOrders.getPageNum(), planOrders.getPageSize(), planOrders.getTotal(), pageAlliance);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("查询发货计划异常", e);
        }
    }

    @Override
    public Collection<CompanyConcise> pullParameter(Long companyKey) throws ParameterException, BusinessException {
        return designateMapper.listSource(companyKey);
    }

    private boolean validate(PlanOrder order, Long companyKey, Long customerKey, boolean exception) {
        Collection<Long> designates = designateMapper.listDesignates(order.getId());
        if (CollectionUtils.isNotEmpty(designates) && designates.stream().anyMatch(i -> i - customerKey == 0)) {
            if (exception) {
                throw new BusinessException("该计划已经分配过该物流商");
            } else {
                return false;
            }
        }
        if (order.getCompanyKey() - companyKey != 0) {
            //判断当前企业是否已经被接
            PlanDesignate planDesignate = designateMapper.getByCompanyKey(companyKey, order.getId());
            if (Optional.ofNullable(planDesignate).map(PlanDesignate::getFettle).orElse(0) < 1) {
                if (exception) {
                    throw new BusinessException("先接单才能分配下级物流商");
                } else {
                    return false;
                }
            }
        } else {
            PlanDesignate designate = designateMapper.getByLastCompanyKey(companyKey, order.getId());
            if (designate != null) {
                if (exception) {
                    throw new BusinessException("该计划已经分配过物流商");
                } else {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public int designate(Integer userKey, Collection<Long> planKeys, Long customerKey, String conveyerName, String conveyerContact) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "用户编号不能为空");
        Assert.notEmpty(planKeys, "请至少选择一项需要分配的发货计划");
        Assert.notNull(customerKey, "请选择物流商");
        //查询当前用户所属公司
        Company company = companyService.assertCompanyByUserKey(userKey);
        if (customerKey == 0L) {
            customerKey = company.getId();
        } else {
            CustomerConcise customer = customerService.loadCustomerConcise(customerKey);
            if (customer == null) {
                throw new ParameterException(customerKey, "未找到选择的物流商信息");
            }
            if (!customer.isEnterprise()) {
                throw new BusinessException("该物流商未注册合同物流管理平台平台");
            }
            customerKey = customer.getCompanyKey();
            queueService.sendCoreMessage(new MediaMessage(Globallys.UUID(), new CompanyCustomer(customerKey, company.getCompanyName(), company.getId(), userKey, CoreConstants.COMPANYCUSTOMER_TYPE_SHIPPER, CoreConstants.COMPANYCUSTOMER_SOURCE_ZHIPAI)));
        }
        Collection<PlanDesignate> insertCommit = new ArrayList<PlanDesignate>();
        int identification = 0;
        boolean exception = planKeys.size() == 1;
        for (Iterator<Long> iterator = planKeys.iterator(); iterator.hasNext(); ) {
            PlanOrder order = planOrderMapper.selectByPrimaryKey(iterator.next());
            if (validate(order, company.getId(), customerKey, exception)) {
                insertCommit.add(initPlanDesignate(order.getId(), company.getId(), customerKey));
                identification++;
            } else {
                iterator.remove();
            }
        }
        if (CollectionUtils.isNotEmpty(planKeys)) {
            if (CollectionUtils.isNotEmpty(insertCommit)) {
                designateMapper.inserts(insertCommit);

                Example example = new Example(PlanDesignate.class);
                example.createCriteria().andEqualTo("companyKey", company.getId()).andIn("planId", planKeys);

                PlanDesignate designate = new PlanDesignate();
                designate.setAllocateFettle(CoreConstants.PLAN_ALLOCATE_STATUS_ALREADY);
                designate.setUpdateTime(new Date());

                designateMapper.updateByExampleSelective(designate, example);
            }
            modifyOrderExtra(planKeys, new OrderExtra(conveyerName, conveyerContact));
        }
        return identification;
    }

    private void modifyOrderExtra(Collection<Long> planKeys, OrderExtra extra) {
        if (!extra.isEmpty()) {
            Example example = new Example(OrderExtra.class);
            example.createCriteria().andEqualTo("dataType", Constants.ORDER_EXTRA_PLAN).andIn("dataKey", planKeys);
            Collection<OrderExtra> collection = orderExtraMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(collection)) {
                for (OrderExtra orderExtra : collection) {
                    orderExtra.modify(extra);

                    if (StringUtils.isNotBlank(extra.getDistributeAddress()) && StringUtils.isBlank(extra.getOriginStation()) && StringUtils.isBlank(orderExtra.getOriginStation())) {
                        orderExtra.setOriginStation(RegionUtils.analyze(extra.getDistributeAddress(), 3));
                    }

                    planKeys.remove(orderExtra.getDataKey());
                }
                orderExtraMapper.updates(collection);
            }
            if (CollectionUtils.isNotEmpty(planKeys)) {
                Collection<OrderExtra> inserts = new ArrayList<OrderExtra>(planKeys.size());
                for (Long planKey : planKeys) {
                    OrderExtra orderExtra = new OrderExtra();
                    orderExtra.setKey(Globallys.nextKey());
                    orderExtra.setDataType(Constants.ORDER_EXTRA_PLAN);
                    orderExtra.setDataKey(planKey);
                    orderExtra.setDriverName(extra.getDriverName());
                    orderExtra.setDriverContact(extra.getDriverContact());
                    orderExtra.setCareNo(extra.getCareNo());
                    if (StringUtils.isNotBlank(extra.getDistributeAddress()) && StringUtils.isBlank(extra.getOriginStation())) {
                        orderExtra.setOriginStation(RegionUtils.analyze(extra.getDistributeAddress(), 3));
                    }
                    inserts.add(orderExtra);
                }
                orderExtraMapper.inserts(inserts);
            }
        }
    }

    /**
     * 初始化计划分配
     *
     * @param planId
     * @param lastCompanyKey
     * @param companyKey
     * @return
     */
    public PlanDesignate initPlanDesignate(Long planId, Long lastCompanyKey, Long companyKey) {
        PlanDesignate planDesignate = new PlanDesignate();
        planDesignate.setId(Globallys.nextKey());
        planDesignate.setPlanId(planId);
        planDesignate.setCompanyKey(companyKey);
        planDesignate.setLastCompanyKey(lastCompanyKey);
        planDesignate.setUpdateTime(new Date());
        planDesignate.setFettle(CoreConstants.PLAN_ACCEPT_STATUS_NOT);
        planDesignate.setAllocateFettle(CoreConstants.PLAN_ALLOCATE_STATUS_NOT);
        return planDesignate;
    }

    private boolean allowReceive(Long planKey, Long companyKey, boolean exception) {
        PlanDesignate designate = designateMapper.getByCompanyKey(companyKey, planKey);
        if (designate == null) {
            if (exception) {
                throw new BusinessException("该计划没有分配给当前企业,不能接单");
            }
            return false;
        }
        if (designate.getFettle() != CoreConstants.PLAN_ACCEPT_STATUS_NOT) {
            if (exception) {
                throw new BusinessException("该计划已经接单不能重复接单");
            }
            return false;
        }
        return true;
    }

    public void receive(Integer uKey, Collection<Long> planKeys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notEmpty(planKeys, "请至少选择一项需要接单的发货计划");
        Company company = companyService.assertCompanyByUserKey(uKey);

        boolean exception = planKeys.size() == 1;

        planKeys.removeIf(aLong -> !allowReceive(aLong, company.getId(), exception));
        if (CollectionUtils.isNotEmpty(planKeys)) {
            PlanDesignate designate = new PlanDesignate();
            designate.setFettle(CoreConstants.PLAN_ACCEPT_STATUS_ALREADY);
            Example example = new Example(PlanDesignate.class);
            example.createCriteria().andEqualTo("companyKey", company.getId()).andIn("planId", planKeys);
            Collection<PlanDesignate> designates = designateMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(designates)) {
                designateMapper.updateByExampleSelective(designate, example);
                Collection<Long> lastKeys = designates.stream().map(PlanDesignate::getLastCompanyKey).collect(Collectors.toList());
                if (lastKeys.size() > 0) {
                    example.clear();
                    example.createCriteria().andIn("companyKey", lastKeys).andIn("planId", planKeys);
                    designate.setFettle(CoreConstants.PLAN_ACCEPT_STATUS_SUB);
                    designateMapper.updateByExampleSelective(designate, example);
                }
            }
        }
    }

    @Override
    public void modifyPlanOrder(Integer uKey, PlanTemplate planTemplate, PartnerType partnerType, OrderExtra orderExtra, Collection<PlanCommodity> commodities,
                                Collection<CustomData> customDatas) throws MessageException, BusinessException, ParameterException {
        Assert.notNull(planTemplate, "发货信息不能为空");
        Assert.notEmpty(commodities, "货物信息不能为空");
        Assert.notBlank(uKey, "操作人编号不能为空");
        Company company = companyService.assertCompanyByUserKey(uKey);
        try {
            if (designateMapper.getByLastCompanyKey(company.getId(), planTemplate.getUniqueKey()) != null) {
                throw new BusinessException("已分配物流商的发货计划不能修改");
            }
            PlanOrder planOrder = assemblyOrderByTemplate(uKey, partnerType, company, planTemplate, false);
            if (uKey - planOrder.getUserKey() != 0) {
                throw new BusinessException("当前用户不是创建人，不能编辑");
            }
            if (null != planOrder.getOrderKey() && planOrder.getOrderKey() > 0) {
                throw new BusinessException("已生成发货单的发货计划无法编辑");
            }
            planOrder.setUpdateTime(new Date());
            planOrderMapper.updateByPrimaryKeySelective(planOrder);
            saveCommodities(uKey, planTemplate.getUniqueKey(), commodities, new Date());
            if (CollectionUtils.isNotEmpty(customDatas)) {
                customDataService.save(CustomDataType.PLAN, planOrder.getId(), customDatas);
            }
            saveExtra(orderExtra, planTemplate, company);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("发货计划数据编辑异常 template:{} ,orderExtra:{},commodities:{},customDatas:{}",
                    planTemplate, orderExtra, commodities, customDatas, e);
            throw new MessageException("发货计划数据编辑异常", e);
        }

    }

    @Override
    public void midifyCarStatus(Collection<Long> orders, OrderExtra orderExtra) throws ParameterException, BusinessException {
        Assert.notEmpty(orders, "请至少选择一条需要派车的计划");
        Assert.notNull(orderExtra, "派车信息不能为空");
        try {
            Collection<PlanOrder> collection = Optional.ofNullable(planOrderMapper.selectByIdentities(orders)).map(ps -> {
                return ps.stream().filter(p -> p.getCarStatus() != CoreConstants.CAR_STATUS_CARRY).collect(Collectors.toList());
            }).orElse(Collections.emptyList());
            if (CollectionUtils.isNotEmpty(collection)) {
                Collection<Long> planKeys = collection.stream().map(PlanOrder::getId).collect(Collectors.toList());
                Example example = new Example(PlanOrder.class);
                example.createCriteria().andIn("id", planKeys);
                planOrderMapper.updateByExampleSelective(new PlanOrder(CoreConstants.CAR_STATUS_ALREADY, new Date()), example);
                modifyOrderExtra(planKeys, orderExtra);
                Collection<Long> orderKeys = collection.stream().filter(p -> p.getOrderKey() > 0).map(PlanOrder::getOrderKey).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(orderKeys)) {
                    orderService.modifyOrderExtra(orderKeys, orderExtra);
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("计划单派车异常: orders:{} orderExtra:{}", orders, orderExtra);
            throw BusinessException.dbException("计划单派车异常");
        }

    }

    public void saveExtra(OrderExtra orderExtra, PlanTemplate template, Company company) {
        if (orderExtra != null && !orderExtra.isEmpty()) {
            OrderExtra exister = orderExtraMapper.selectByType(Constants.ORDER_EXTRA_PLAN, template.getUniqueKey());
            if (exister != null) {
                if (StringUtils.isNotBlank(template.getReceiveAddress()) && StringUtils.isBlank(exister.getArrivalStation())) {
                    orderExtra.setArrivalStation(RegionUtils.analyze(template.getReceiveAddress(), 3));
                }
                if (StringUtils.isNotBlank(orderExtra.getDistributeAddress()) && StringUtils.isBlank(exister.getOriginStation())) {
                    orderExtra.setOriginStation(RegionUtils.analyze(orderExtra.getDistributeAddress(), 3));
                }
                orderExtra.setKey(exister.getKey());
                orderExtraMapper.updateByPrimaryKey(orderExtra);
            } else {
                if (StringUtils.isNotBlank(template.getReceiveAddress()) && StringUtils.isBlank(orderExtra.getArrivalStation())) {
                    orderExtra.setArrivalStation(RegionUtils.analyze(template.getReceiveAddress(), 3));
                }
                if (StringUtils.isNotBlank(orderExtra.getDistributeAddress()) && StringUtils.isBlank(orderExtra.getOriginStation())) {
                    orderExtra.setOriginStation(RegionUtils.analyze(orderExtra.getDistributeAddress(), 3));
                }
                orderExtra.setKey(Globallys.nextKey());
                orderExtra.setDataKey(template.getUniqueKey());
                orderExtra.setDataType(Constants.ORDER_EXTRA_PLAN);
                orderExtraMapper.insert(orderExtra);
            }
        }
        if (template.getConveyKey() != null && template.getConveyKey() > 0) {
            CustomerConcise customer = customerService.loadCustomerConcise(template.getConveyKey());
            if (customer == null) {
                throw new ParameterException(template.getConveyKey(), "未找到选择的物流商信息");
            }
            if (!customer.isEnterprise()) {
                throw new BusinessException("该物流商未注册合同物流管理平台平台");
            }
            designateMapper.insert(initPlanDesignate(template.getUniqueKey(), company.getId(), customer.getCompanyKey()));
        }

    }

    @Override
    public Collection<TemplateDescribe> fillTemplateByPlanKey(Integer uKey, Long planKey, Long templateKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notBlank(planKey, "发货计划编号不能为空");
        Assert.notBlank(templateKey, "发货模板编号不能为空");
        PlanAlliance alliance = Optional.ofNullable(alliance(uKey, planKey, P.DETAILS)).orElseThrow(() -> new ParameterException(planKey, "没有找到指定的发货计划信息"));
        Collection<TemplateDescribe> describes = templateService.listTemplateDescribe(templateKey);

        return Transform.transform(describes, alliance);
    }

    @Override
    public void delete(Integer uKey, Long planKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户编号不能为空");
        Assert.notBlank(planKey, "计划单号不能为空");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            PlanOrder order = planOrderMapper.selectByPrimaryKey(planKey);
            if (order == null) {
                throw new ParameterException("该计划已经删除");
            }
            if (order.getCompanyKey() - company.getId() != 0) {
                throw new BusinessException("只能是我方创建的才可以删除");
            }
            if (order.getOrderKey() != null && order.getOrderKey() > 0) {
                throw new BusinessException("该计划已经生成发货单不能删除");
            }
            PlanDesignate designate = designateMapper.getByLastCompanyKey(company.getId(), planKey);
            if (designate != null) {
                throw new BusinessException("该计划已经分配物流商不能删除");
            }


            planOrderMapper.deleteByPrimaryKey(planKey);
            orderExtraMapper.delete(new OrderExtra(Constants.ORDER_EXTRA_PLAN, planKey));
            commodityMapper.delete(new PlanCommodity(planKey));
            customDataService.delete(CustomDataType.PLAN, planKey);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.info("发货计划删除->: {}, : {}", uKey, planKey);
            throw new BusinessException("发货计划删除异常", e);
        }

    }
}
