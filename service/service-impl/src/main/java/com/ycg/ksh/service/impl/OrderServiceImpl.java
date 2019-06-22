package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.mybatis.CustomExample;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.constant.*;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.entity.common.constant.Constants;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.enterprise.AbstractOrder;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.CompanyCodeContext;
import com.ycg.ksh.entity.service.enterprise.*;
import com.ycg.ksh.entity.service.esign.ReceiptSignature;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveFettle;
import com.ycg.ksh.service.observer.*;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.persistence.enterprise.OrderExtraMapper;
import com.ycg.ksh.service.support.assist.ObjectUtils;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单业务逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
@Service("ksh.core.service.orderService")
public class OrderServiceImpl implements OrderService, BarcodeObserverAdapter, LocationObserverAdapter, ReceiptObserverAdapter, DriverContainerObserverAdapter, AdventiveObserverAdapter, TemplateObserverAdapter, CustomeObserverAdapter, RabbitMessageListener {
    @Resource
    CacheManager cacheManager;
    @Resource
    CompanyService companyService;
    @Resource
    CustomerService customerService;
    @Resource
    UserService userService;
    @Resource
    BarCodeService barCodeService;
    @Resource
    ReceiptService receiptService;
    @Resource
    LocationTrackService locationTrackService;
    @Resource
    CustomDataService customDataService;

    @Resource
    OrderMapper orderMapper;
    @Resource
    OrderExtraMapper orderExtraMapper;
    @Resource
    OrderCommodityMapper commodityMapper;
    @Resource
    OrderExceptionMapper exceptionMapper;
    @Resource
    InvalidOrderMapper invalidMapper;
    @Resource
    AutoMapService autoMapService;
    @Resource
    OperateNoteMapper operateNoteMapper;

    @Autowired(required = false)
    Collection<OrderObserverAdapter> observers;

    /*private void synchronize(Collection<Order> orders, boolean create){
        if(create){
            queueService.synchronize(Globallys.nextKey(), orders);
        }else{
            queueService.synchronize(Globallys.nextKey(), orders);
        }
    }

    private void synchronize(Long orderKey, boolean create){
        OrderAlliance alliance = alliance(0, orderKey, O.DETAILS);
        if(create){
            queueService.synchronize(Globallys.nextKey(), alliance);
        }else{
            queueService.synchronize(Globallys.nextKey(), alliance);
        }
    }*/

    private AbstractOrder getOrderByKey(boolean available, Long orderKey) {
        if (!available) {
            return invalidMapper.selectByPrimaryKey(orderKey);
        }
        return getOrderByKey(orderKey);
    }

    @Override
    public Order getOrderByKey(Long orderKey) {
        return orderMapper.selectByPrimaryKey(orderKey);
    }

    @Override
    public Order getOrderByCode(String bindCode) {
        Barcode barcode = barCodeService.getBarcode(bindCode);
        if (barcode != null) {
            return orderMapper.selectByCode(barcode.getBarcode());
        } else {
            throw new ParameterException("无效的二维码");
        }
    }

    private void modifyNoticeObserver(OrderEventType eventType, Integer uKey, Order order) {
        if (CollectionUtils.isNotEmpty(observers)) {
            for (OrderObserverAdapter observerAdapter : observers) {
                observerAdapter.modifyOrder(uKey, order, eventType);
            }
        }
    }

    private void modifyNoticeObserver(OrderEventType eventType, Integer uKey, Collection<Order> collection) {
        if (CollectionUtils.isNotEmpty(observers)) {
            for (OrderObserverAdapter observerAdapter : observers) {
                for (Order order : collection) {
                    observerAdapter.modifyOrder(uKey, order, eventType);
                }
            }
        }
    }

    @Override
    public void notifyBarcodeValidate(Barcode barcode, BarcodeContext context) {
        if (context != null && context instanceof CompanyCodeContext) {
            try {
                context.setNeedBingImage(false);
                CompanyCodeContext codeContext = (CompanyCodeContext) context;
                if (context.fettle().bind()) {//已经绑定的二维码
                    OrderAlliance orderAlliance = getAllianceByCode(context.getUserKey(), barcode.getBarcode());
                    if (orderAlliance != null) {
                        codeContext.setOrderKey(orderAlliance.getId());
                        codeContext.setFettleType(OrderFettleType.convert(orderAlliance.getFettle()));
                        //isUserType 为空表示当前用户不是订单的三方，即为司机角色
                        if (orderAlliance.getIsUserType() != null && orderAlliance.getIsUserType() > 0) {
                            codeContext.setRoleType(OrderRoleType.convert(orderAlliance.getIsUserType()));
                        }
                    }
                } else {//没有绑定的二维码
                    Example example = new Example(Order.class);
                    example.createCriteria().andEqualTo("shipperId", codeContext.getCompanyKey()).andEqualTo("fettle", OrderFettleType.DEFAULT.getCode());
                    example.orderBy("createTime").desc();
                    codeContext.setWaitBindCount(orderMapper.selectCountByExample(example));
                }
            } catch (BusinessException | ParameterException e) {
                throw e;
            } catch (Exception e) {
                logger.error("notify barcode validate --> barcode:{} context:{}", barcode, context, e);
                throw BusinessException.dbException("二维码校验异常");
            }
        }
    }

    @Override
    public void notifyLocationReport(LocationEvent locationEvent, LocationType locationType, LocationTrack locationTrack) throws BusinessException {
        if (locationType != null && locationType.isOrder()) {
            try {
                Order order = getOrderByKey(Long.parseLong(locationTrack.getHostId()));
                if (order != null) {
                    if (locationEvent.sync()) {//异步处理
                        delayedWarning(locationTrack, order);
                    } else {
                        order.setLocation(locationTrack.getLocation());
                        order.setUpdateTime(locationTrack.getCreateTime());
                        orderMapper.updateByPrimaryKeySelective(order);
                        modifyNoticeObserver(OrderEventType.LOCATE, locationTrack.getUserId(), order);
                    }
                }
            } catch (BusinessException | ParameterException e) {
                throw e;
            } catch (Exception e) {
                logger.error("notify location report --> locationType:{} locationTrack:{}", locationType, locationTrack, e);
                throw new BusinessException("更新订单最新位置异常", e);
            }
        }
    }

    /**
     * 延迟预警
     *
     * @param locationTrack
     * @param order
     */
    private void delayedWarning(LocationTrack locationTrack, Order order) {
        if (order.getArrivalTime() != null && order.getDeliveryTime() != null) {
            OrderExtra orderExtra = orderExtraMapper.selectByType(Constants.ORDER_EXTRA_SHIP, order.getId());
            if (orderExtra != null && StringUtils.isNotBlank(orderExtra.getDistributeAddress())) {
                //发货时间
                Long deliveryTime = order.getDeliveryTime().getTime();
                //发货地点
                AutoMapLocation shipLocation = autoMapService.coordinate(orderExtra.getDistributeAddress());

                //定位的时间
                Long currentTime = Optional.ofNullable(locationTrack.getCreateTime()).map(Date::getTime).orElse(System.currentTimeMillis());
                //起点到当前所在地直线距离
                Double currentDistance = autoMapService.distance(shipLocation.getLongitude(), shipLocation.getLatitude(), locationTrack.getLongitude(), locationTrack.getLatitude());
                //如果运输距离或者运输时间 小于等于0，则不需要计算
                if (currentDistance <= 0 || currentTime - deliveryTime <= 0) {
                    return;
                }
                //收货时间
                Long arrivalTime = order.getArrivalTime().getTime();
                //收货地点
                AutoMapLocation receiptLocation = autoMapService.coordinate(order.getReceiveAddress());
                //起点到终点直线距离
                Double sendDistance = autoMapService.distance(shipLocation.getLongitude(), shipLocation.getLatitude(), receiptLocation.getLongitude(), receiptLocation.getLatitude());

                //已经运输的平均速度(千米/每分)
                Double speed = currentDistance / TimeUnit.MILLISECONDS.toMinutes(currentTime - deliveryTime);
                //在要求到货时间之前跑完全程所需最小速度(千米/每分)
                Double minspeed = sendDistance / TimeUnit.MILLISECONDS.toMinutes(arrivalTime - deliveryTime);
                logger.info("延迟预警-> 订单编号:{} 当前平均速度:{} km/m, 所需最小速度:{} km/m", order.getId(), speed, minspeed);
                if (speed >= minspeed) {//如果当前速度 大于等于 所需最小速度 则可以正常送达
                    order.setDelayWarning(CoreConstants.DELAY_WARNING_NORMAL);
                } else {//如果当前速度 小于 所需最小速度 则可能延迟
                    order.setDelayWarning(CoreConstants.DELAY_WARNING_PROBABLE);
                }
            }
            orderMapper.updateByPrimaryKeySelective(order);
        }
    }

    /**
     * 绑定二维码
     *
     * @param uKey     当前用户编号
     * @param orderKey 订单编号
     * @param qrcode
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void bindCode(Integer uKey, Long orderKey, String qrcode) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notBlank(orderKey, "要绑码的订单编号不能为空");
        Assert.notBlank(qrcode, "二维码不能为空");
        Barcode barcode = barCodeService.loadBarcode(qrcode);
        if (barcode == null) {
            throw new ParameterException("二维码[" + barcode + "]不存在");
        }
        if (!OrderFettleType.convert(barcode.getBindstatus()).isDefault()) {
            throw new ParameterException("二维码[" + barcode + "]已经绑定过");
        }
        Order order = getOrderByKey(orderKey);
        if (order == null) {
            throw new ParameterException(orderKey, "要绑码的订单信息不存在");
        }
        if (StringUtils.isBlank(order.getDeliveryNo())) {
            throw new ParameterException(order.getDeliveryNo(), "绑单时送货单号不能为空");
        }
        if (StringUtils.isNotEmpty(order.getBindCode())) {
            throw new ParameterException("订单已经绑定过,不能重复绑定!");
        }
        order.setBindCode(qrcode);
//        order.setFettle(OrderFettleType.DISUSE.getCode());
        order.setUpdateTime(new Date());
        if (order.getDeliveryTime() == null) {
            order.setDeliveryTime(order.getUpdateTime());
        }
        if (OrderFettleType.convert(order.getFettle()).isDefault()) {
            order.setFettle(OrderFettleType.ING.getCode());
        }
        orderMapper.updateByPrimaryKeySelective(order);
        modifyNoticeObserver(OrderEventType.BINDCODE, uKey, order);
    }


    private boolean validateExistByDeliveryNo(Long orderKey, Long companyKey, String deliveryNo) {
        if (StringUtils.isNotBlank(deliveryNo)) {
            Example example = new Example(Order.class);
            example.createCriteria().andNotEqualTo("id", orderKey).andEqualTo("shipperId", companyKey).andEqualTo("deliveryNo", deliveryNo);
            return orderMapper.selectCountByExample(example) > 0;
        }
        return false;
    }

    private Order assemblyOrderByTemplate(Integer uKey, PartnerType partnerType, Company company, OrderTemplate template, boolean insert) throws MessageException {
        if (StringUtils.isBlank(template.getReceiverName())) {
            throw new MessageException("收货联系人不能为空");
        }
        if (StringUtils.isBlank(template.getReceiveAddress())) {
            throw new MessageException("收货地址不能为空");
        }
        if (CollectionUtils.isNotEmpty(observers)) {
            for (OrderObserverAdapter observer : observers) {
                observer.initializeOrder(uKey, partnerType, company.getId(), template, insert);
            }
        }
        Date cdate = new Date();
        Order order = null;
        if (insert) {
            order = new Order(company.getId(), template.getUniqueKey());
            int clientType = 0;
            CustomerConcise shipper = template.getShipper(), receive = template.getReceive(), convey = template.getConvey();
            //处理发货方
            if (shipper != null) {
                order.setShipperId(shipper.isEnterprise() ? shipper.getCompanyKey() : shipper.getId());
                clientType = PartnerType.SHIPPER.clientType(clientType, shipper.isEnterprise());
            } else {
                order.setShipperId(company.getId());
                clientType = PartnerType.SHIPPER.clientType(clientType, true);
            }
            //处理收货方
            if (receive != null) {
                order.setReceiveId(receive.isEnterprise() ? receive.getCompanyKey() : receive.getId());
                clientType = PartnerType.RECEIVE.clientType(clientType, receive.isEnterprise());
            } else {
                order.setReceiveId(company.getId());
                clientType = PartnerType.RECEIVE.clientType(clientType, true);
            }
            //处理承运方
            if (convey != null) {
                order.setConveyId(convey.isEnterprise() ? convey.getCompanyKey() : convey.getId());
                clientType = PartnerType.CONVEY.clientType(clientType, convey.isEnterprise());
            } else {
                order.setConveyId(company.getId());
                clientType = PartnerType.CONVEY.clientType(clientType, true);
            }
            order.setClientType(clientType);
            order.setCreateTime(cdate);
//            if (order.getDeliveryTime() == null) {
//                order.setDeliveryTime(cdate);
//            }
            order.setCreateUserId(uKey);
            order.setFettle(OrderFettleType.ING.getCode());
        } else {
            order = getOrderByKey(template.getUniqueKey());
        }
        if (validateExistByDeliveryNo(order.getId(), order.getShipperId(), template.getDeliveryNo())) {
            throw new MessageException("对应的送货单号[" + template.getDeliveryNo() + "]已经存在");
        }
        order.setInsertType(partnerType.getCode());
        order.setOrderNo(template.getOrderNo());
        order.setDeliveryNo(Optional.ofNullable(template.getDeliveryNo()).orElse(StringUtils.EMPTY));
        order.setReceiverName(template.getReceiverName());
        order.setReceiverContact(template.getReceiverContact());
        order.setReceiveAddress(template.getReceiveAddress());
        order.setRemark(template.getRemark());
        order.setArrivalTime(template.getArrivalTime());
        order.setCollectTime(template.getCollectTime());
//        order.setDeliveryTime(Optional.ofNullable(template.getDeliveryTime()).orElse(cdate));
        order.setUpdateTime(cdate);
        return order;
    }

    private void saveCommodities(Integer uKey, Long oKey, Collection<OrderCommodity> commodities, Date createTime) {
        Collection<Long> commodityKeys = commodityMapper.listKeyByOrderKey(oKey);
        for (OrderCommodity commodity : commodities) {
            if (O.isEmptyCommodity(commodity)) {
                continue;
            }
            commodity.setOrderId(oKey);
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
            commodityMapper.deleteByKeys(commodityKeys);
        }
    }

    /**
     * 去除重复数据
     *
     * @param context
     * @param propertyDescribes
     * @throws MessageException
     */
    @Override
    public void distinctSomething(TemplateContext context, Collection<PropertyDescribe> propertyDescribes) throws MessageException {
        Optional<PropertyDescribe> optional = propertyDescribes.stream().filter(pd -> StringUtils.equalsIgnoreCase(OrderTemplate.class.getName(), pd.getClassName())).findAny();
        if (optional.isPresent()) {
            PropertyDescribe describe = optional.get();
            Example example = new Example(Order.class);
            Example.Criteria criteria = example.createCriteria();
            PartnerType type = context.getPartnerType();
            criteria.andEqualTo(type.shipper() ? "shipperId" : type.receive() ? "receiveId" : "conveyId", context.getCompanyKey());
            for (Map.Entry<String, Object> entry : describe.getProperties().entrySet()) {
                criteria.andEqualTo(entry.getKey(), entry.getValue());
            }
            if (orderMapper.selectCountByExample(example) > 0) {
                throw new MessageException("订单数据已经存在");
            }
        }
    }

    @Override
    public void importSomething(TemplateContext context, Collection<PropertyDescribe> propertys) throws MessageException {
        if (context.is(Constants.TEMPLATE_CATEGORY_SHIP) && CollectionUtils.isNotEmpty(propertys)) {
            try {
                Company company = companyService.getCompanyByKey(context.getCompanyKey());//当前操作人的企业信息
                //订单数据
                OrderTemplate orderTemplate = ObjectUtils.reduceObject(OrderTemplate.class, propertys);
                if (orderTemplate != null) {
                    orderTemplate.setUniqueKey(context.getUniqueKey());
                    OrderExtra orderExtra = ObjectUtils.reduceObject(OrderExtra.class, propertys);//额外的数据
                    Collection<OrderCommodity> commodities = ObjectUtils.collectObjects(OrderCommodity.class, propertys);//明细数据
                    saveOrder(context.getOperatorKey(), company, context.getPartnerType(), orderTemplate, orderExtra, commodities, OrderEventType.convert(context.getEventKey()));
                }
            } catch (MessageException e) {
                throw e;
            } catch (BusinessException | ParameterException e) {
                throw new MessageException(e.getFriendlyMessage(), e);
            } catch (Exception e) {
                logger.error("订单数据保存异常 --> context:{} propertys:{}", context, propertys, e);
                throw new MessageException("订单数据保存异常", e);
            }
        }
    }

    /**
     * 订单查询
     *
     * @param uKey   操作人编号
     * @param search 查询参数
     * @param scope  分页数据
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<OrderAlliance> pageOrder(Integer uKey, OrderSearch search, PageScope scope) throws ParameterException, BusinessException {
        logger.debug("page order --> uKey:{} search:{} scope:{}", uKey, search, scope);
        if (!search.isAll()) {
            Assert.notBlank(uKey, "操作人不能为空");
            CompanyEmployee employee = companyService.getCompanyEmployee(uKey);
            if (employee != null) {
                search.setCompanyKey(employee.getCompanyId());
                search.setManage(EmployeeType.convert(employee.getEmployeeType()).isManage());
            } else {
                return new CustomPage<OrderAlliance>(scope.getPageNum(), scope.getPageSize(), 0, Collections.emptyList());
            }
        }
        search.setShipperKey(customerKey(search.getShipperKey()));
        search.setReceiveKey(customerKey(search.getReceiveKey()));
        search.setConveyKey(customerKey(search.getConveyKey()));
        search.setUserKey(uKey);
        try {
            Integer[] flags = Optional.ofNullable(search.getFlags()).orElseGet(() -> O.LISTS);
            RowBounds rowBounds = new RowBounds(scope.getPageNum(), scope.getPageSize());
            if (search.isInvalid()) {//查询作废的订单数据
                Page<ObsoleteOrder> page = invalidMapper.selectPageByExample(buildExample(search, ObsoleteOrder.class), rowBounds);
                return new CustomPage<OrderAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), alliance(uKey, page, flags));
            } else if ((search.isEreceipt() || CollectionUtils.isNotEmpty(search.getReceiptFettles())) && !search.isAll()) {//查询电子回单
                Page<Order> page = orderMapper.listBySomething(search, rowBounds);
                return new CustomPage<OrderAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), alliance(uKey, page, flags));
            } else {
                Page<Order> page = orderMapper.selectPageByExample(buildExample(search, Order.class), rowBounds);
                return new CustomPage<OrderAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), alliance(uKey, page, flags));
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("page order --> uKey:{} search:{} scope:{}", uKey, search, scope, e);
            throw BusinessException.dbException("查询订单信息异常");
        }
    }

    @Override
    public Collection<OrderAlliance> electoralOrderList(Integer uKey, Collection<Long> orderKeys) throws ParameterException, BusinessException {
        Assert.notEmpty(orderKeys, "至少选择一个订单信息");
        Collection<OrderAlliance> collection = new ArrayList<OrderAlliance>(orderKeys.size());
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            for (Long orderKey : orderKeys) {
                //判断当前签署角色是否已经签署，只需要没有签署的
                if (receiptService.validateSing(orderKey, company.getId())) {
                    continue;
                }
                OrderAlliance alliance = alliance(uKey, getOrderByKey(orderKey), O.LISTS);
                //未生成电子回单的不要
                if (ReceiptFettleType.convert(alliance.getReceiptFettle()).isDefault()) {
                    continue;
                }
                collection.add(alliance);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("查询选中的回单列表异常 uKey:{} orderKeys:{}", uKey, orderKeys);
            throw BusinessException.dbException("查询选中的回单列表异常");
        }
        return collection;
    }

    /**
     * 订单详情查询
     *
     * @param userKey 操作人编号
     * @param qrcode  二维码
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     */
    @Override
    public OrderAlliance getAllianceByCode(Integer userKey, String qrcode) throws ParameterException, BusinessException {
        //Assert.notBlank(userKey, "操作人编号不能为空");
        Assert.notBlank(qrcode, "二维码不能为空");
        try {
            Order order = getOrderByCode(qrcode);
            if (null == order) {
                throw new BusinessException("订单详情查询异常");
            }
            OrderAlliance alliance = alliance(userKey, order, O.DETAILS);
            if (null == alliance) {
                throw new BusinessException("订单详情查询异常");
            }
            return alliance;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("get alliance by code --> userKey:{} qrcode:{}", userKey, qrcode, e);
            throw BusinessException.dbException("获取订单信息异常");
        }
    }

    @Override
    public OrderAlliance getOrderAlliance(Integer userKey, Long orderKey, boolean available) throws ParameterException, BusinessException {
        Assert.notBlank(orderKey, "订单ID为空");
        try {
            AbstractOrder order = getOrderByKey(available, orderKey);
            if (null == order) {
                throw new BusinessException("订单详情查询异常");
            }
            OrderAlliance alliance = alliance(userKey, order, O.DETAILS);
            if (null == alliance) {
                throw new BusinessException("订单详情查询异常");
            }
            return alliance;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("query order detail --> userKey:{} orderKey:{}", userKey, orderKey, e);
            throw BusinessException.dbException("获取订单信息异常");
        }
    }

    @Override
    public Collection<Order> listByOrderNo(PartnerType partner, Long companyKey, String orderNo) throws BusinessException {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (partner.convey()) {
            criteria.andEqualTo("conveyId", companyKey);
        } else if (partner.receive()) {
            criteria.andEqualTo("receiveId", companyKey);
        } else {
            criteria.andEqualTo("shipperId", companyKey);
        }
        criteria.andEqualTo("orderNo", orderNo);
        return orderMapper.selectByExample(example);
    }

    @Override
    public Collection<Order> listByDeliveryNo(PartnerType partner, Long companyKey, Collection<String> deliveryNos) throws BusinessException {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if (partner.convey()) {
            criteria.andEqualTo("conveyId", companyKey);
        } else if (partner.receive()) {
            criteria.andEqualTo("receiveId", companyKey);
        } else {
            criteria.andEqualTo("shipperId", companyKey);
        }
        criteria.andIn("deliveryNo", deliveryNos);
        return orderMapper.selectByExample(example);
    }

    public Collection<OrderAlliance> alliance(Integer uKey, Collection<? extends AbstractOrder> results, Integer... flags) throws
            BusinessException {
        Collection<OrderAlliance> collection = new ArrayList<OrderAlliance>(results.size());
        for (AbstractOrder order : results) {
            collection.add(alliance(uKey, order, flags));
        }
        return collection;
    }

    public OrderAlliance alliance(Integer uKey, AbstractOrder order, Integer... flags) throws BusinessException {
        try {
            OrderAlliance alliance = OrderAlliance.buildAlliance(order);
            if (CollectionUtils.isNotEmpty(observers) && ArrayUtils.isNotEmpty(flags)) {
                Arrays.sort(flags);
                if (Arrays.binarySearch(flags, O.commodities) >= 0) {
                    alliance.setCommodities(listCommodity(uKey, alliance.getId()));
                }
                if (Arrays.binarySearch(flags, O.expetion) >= 0) {
                    alliance.setException(exceptionMapper.selectByPrimaryKey(alliance.getId()));
                }
                if (Arrays.binarySearch(flags, O.extradata) >= 0) {
                    alliance.setExtra(orderExtraMapper.selectByType(Constants.ORDER_EXTRA_SHIP, alliance.getId()));
                }
                if (Arrays.binarySearch(flags, O.operatenote) >= 0) {
                    alliance.setOperateNote(operateNoteMapper.queryByHostId(alliance.getId()));
                }
                for (OrderObserverAdapter observerAdapter : observers) {
                    observerAdapter.allianceOrder(uKey, alliance, flags);
                }
            }
            return alliance;
        } catch (Exception e) {
            throw new BusinessException("获取订单信息异常", e);
        }
    }


    /**
     * 按需加载订单详情
     *
     * @param userKey
     * @param orderKey
     * @param flags
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public OrderAlliance alliance(Integer userKey, Long orderKey, Integer... flags) throws ParameterException, BusinessException {
        Assert.notBlank(orderKey, "订单ID为空");
        try {
            AbstractOrder order = getOrderByKey(true, orderKey);
            if (null == order) {
                throw new BusinessException("未能找到该订单信息");
            }
            OrderAlliance alliance = alliance(userKey, order, flags);
            if (null == alliance) {
                throw new BusinessException("订单详情查询异常");
            }
            return alliance;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("alliance --> userKey:{} orderKey:{} flags:{}", userKey, orderKey, flags, e);
            throw BusinessException.dbException("获取订单信息异常");
        }
    }


    /**
     * 生成电子回单
     *
     * @param userKey
     * @param orderKey
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void buildReceipt(Integer userKey, Long orderKey) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "操作人不能为空");
        Assert.notBlank(orderKey, "订单编号不能为空");
        try {
            OrderAlliance alliance = getOrderAlliance(userKey, orderKey, true);
            if (alliance == null) {
                throw new BusinessException("订单信息获取异常");
            }
            FileEntity fileEntity = receiptService.buildReceipt(userKey, alliance);
            if (fileEntity != null) {
                //生成电子回单事件
                modifyNoticeObserver(OrderEventType.ERECEIPT, userKey, alliance);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("build receipt --> userKey:{} orderKey:{}", userKey, orderKey, e);
            throw BusinessException.dbException("生成电子回单异常");
        }
    }

    /**
     * 批量生成电子回单
     *
     * @param userKey
     * @param orderKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public int buildReceipt(Integer userKey, Collection<Long> orderKeys) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "操作人不能为空");
        Assert.notEmpty(orderKeys, "至少选择一项数据");
        int failure = 0;
        Company company = companyService.assertCompanyByUserKey(userKey);
        for (Long orderKey : orderKeys) {
            try {
                buildReceipt(userKey, orderKey);
            } catch (Exception e) {
                logger.error("生成电子回单异常 {}", orderKey, e);
                failure++;
            }
        }
        return failure;
    }

    private boolean saveOrder(Integer uKey, Company company, PartnerType partner, OrderTemplate template, OrderExtra orderExtra, Collection<OrderCommodity> commodities, OrderEventType eventType) throws ParameterException, BusinessException, MessageException {
        Order order = assemblyOrderByTemplate(uKey, partner, company, template, true);
        if (orderMapper.insert(order) > 0) {
            orderExtra = Optional.ofNullable(orderExtra).orElse(new OrderExtra());
            orderExtra.setDistributeAddress(Optional.ofNullable(orderExtra.getDistributeAddress()).orElse(company.getAddress()));

            if (StringUtils.isNotBlank(template.getReceiveAddress()) && StringUtils.isBlank(orderExtra.getArrivalStation())) {
                orderExtra.setArrivalStation(RegionUtils.analyze(template.getReceiveAddress(), 3));
            }

            modifyOrderExtra(Lists.newArrayList(order.getId()), orderExtra);

            //保存货物明细
            saveCommodities(uKey, order.getId(), commodities, order.getCreateTime());
            //订单创建事件
            modifyNoticeObserver(eventType, uKey, order);
            return true;
        }
        return false;
    }

    @Override
    public void saveOrder(Integer uKey, PartnerType partner, OrderTemplate template, OrderEventType eventType) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notNull(template, "订单信息不能为空");
        if (CollectionUtils.isNotEmpty(template.getCommodities())) {
            template.getCommodities().removeIf(c -> !c.validate());
        }
        Assert.notEmpty(template.getCommodities(), "至少输入一条有效的货物信息");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            if (template.getUniqueKey() == null || template.getUniqueKey() <= 0) {
                template.setUniqueKey(Globallys.nextKey());
            }
            if (saveOrder(uKey, company, partner, template, template.getOrderExtra(), template.getCommodities(), eventType)) {
                //保存自定义信息
                customDataService.save(CustomDataType.ORDER, template.getUniqueKey(), template.getCustomDatas());
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.info("saveOrder->uKey: {}, template: {}, orderExtra: {}, commodities: {}, customDatas: {}, partner: {}", uKey, template, partner);
            throw new BusinessException("处理订单信息异常", e);
        }
    }

    @Override
    public void modifyOrder(Integer uKey, PartnerType partner, OrderTemplate template) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notNull(template, "订单信息不能为空");
        Assert.notBlank(template.getUniqueKey(), "发货单系统编号信息不能为空");
        if (CollectionUtils.isNotEmpty(template.getCommodities())) {
            template.getCommodities().removeIf(c -> !c.validate());
        }
        Assert.notEmpty(template.getCommodities(), "至少输入一条有效的货物信息");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            Order order = assemblyOrderByTemplate(uKey, partner, company, template, false);
            if (orderMapper.updateByPrimaryKeySelective(order) > 0) {
                OrderExtra orderExtra = template.getOrderExtra();
                if (StringUtils.isNotBlank(template.getReceiveAddress()) && StringUtils.isBlank(orderExtra.getArrivalStation())) {
                    orderExtra.setArrivalStation(RegionUtils.analyze(template.getReceiveAddress(), 3));
                }
                modifyOrderExtra(Lists.newArrayList(order.getId()), orderExtra);

                //保存货物明细
                saveCommodities(uKey, order.getId(), template.getCommodities(), order.getCreateTime());
                //保存自定义信息
                customDataService.save(CustomDataType.ORDER, template.getUniqueKey(), template.getCustomDatas());
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.info("modifyOrder->: uKey: {}, template: {}, orderExtra: {}, commodities: {}, customDatas: {}, partner: {}", uKey, template, partner);
            throw new BusinessException("修改订单信息异常", e);
        }
    }

    @Override
    public void modifyOrderExtra(Collection<Long> orderKeys, OrderExtra extra) throws ParameterException, BusinessException {
        if (CollectionUtils.isNotEmpty(orderKeys) && !extra.isEmpty()) {
            Example example = new Example(OrderExtra.class);
            example.createCriteria().andEqualTo("dataType", Constants.ORDER_EXTRA_SHIP).andIn("dataKey", orderKeys);
            Collection<OrderExtra> collection = orderExtraMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(collection)) {
                for (OrderExtra orderExtra : collection) {
                    orderExtra.modify(extra);

                    if (StringUtils.isNotBlank(extra.getDistributeAddress()) && StringUtils.isBlank(extra.getOriginStation()) && StringUtils.isBlank(orderExtra.getOriginStation())) {
                        orderExtra.setOriginStation(RegionUtils.analyze(extra.getDistributeAddress(), 3));
                    }

                    orderExtra.setKey(orderExtra.getKey());
                    orderKeys.remove(orderExtra.getDataKey());
                }
                orderExtraMapper.updates(collection);
            }
            if (CollectionUtils.isNotEmpty(orderKeys)) {
                Collection<OrderExtra> inserts = new ArrayList<OrderExtra>(orderKeys.size());
                for (Long planKey : orderKeys) {
                    extra.setKey(Globallys.nextKey());
                    extra.setDataType(Constants.ORDER_EXTRA_SHIP);
                    extra.setDataKey(planKey);

                    if (StringUtils.isNotBlank(extra.getDistributeAddress()) && StringUtils.isBlank(extra.getOriginStation())) {
                        extra.setOriginStation(RegionUtils.analyze(extra.getDistributeAddress(), 3));
                    }

                    inserts.add(extra);
                }
                orderExtraMapper.inserts(inserts);
            }
        }
    }

    @Override
    public void modifyOrderExtra(Collection<OrderExtra> orderExtra) throws ParameterException, BusinessException {
        if (CollectionUtils.isNotEmpty(orderExtra)) {
            orderExtraMapper.updates(orderExtra);
        }
    }

    /**
     * 订单删除
     *
     * @param uKey      操作人用户编号
     * @param orderKeys 要作废的订单编号
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public void deleteOrders(Integer uKey, PartnerType partnerType, List<Long> orderKeys) throws BusinessException, ParameterException {
        Assert.notEmpty(orderKeys, "请选择一条要删除的订单编号");
        Example example = new Example(Order.class);
        Company company = companyService.assertCompanyByUserKey(uKey);
        if (partnerType.shipper()) {
            example.createCriteria().andIn("id", orderKeys)
                    .andEqualTo("shipperId", company.getId())
                    .andNotIn("fettle", Lists.newArrayList(2, 4));//到货订单不可以进行删除
        } else if (partnerType.convey()) {
            example.createCriteria().andIn("id", orderKeys)
                    .andEqualTo("conveyId", company.getId())
                    .andNotIn("fettle", Lists.newArrayList(2, 4))
                    .andEqualTo("insertType", CoreConstants.COMPANYCUSTOMER_TYPE_CONVEY);
        }
        List<Order> orders = orderMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(orders)) {
            for (Order order : orders) {
                try {
                    if (receiptService.getDigitizedReceiptByKey(order.getId()) == null) {
                        orderMapper.deleteByPrimaryKey(order.getId());
                    }
                } catch (Exception e) {
                    logger.error("订单删除异常 {} {}", uKey, orderKeys);
                }
            }
            //订单作废事件
            modifyNoticeObserver(OrderEventType.DELETE, uKey, orders);
        } else if (partnerType.convey()) {
            throw new BusinessException("发货方分配的订单不可删除");
        }
    }

    /**
     * 订单作废
     *
     * @param uKey      操作人用户编号
     * @param orderKeys 要作废的订单编号
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public void invalid(Integer uKey, Collection<Long> orderKeys) throws BusinessException, ParameterException {
        Assert.notEmpty(orderKeys, "请选择一条要作废的订单编号");
        try {
            Collection<Order> orders = orderMapper.selectByIdentities(orderKeys);
            if (CollectionUtils.isNotEmpty(orders)) {
                Date ctime = new Date();
                //将数据插入order_invalid表中
                invalidMapper.inserts(orders.stream().map(o -> new ObsoleteOrder(o, uKey, ctime)).collect(Collectors.toList()));
                //删除订单数据
                orderMapper.deleteByIdentities(orderKeys);
                //订单作废事件
                modifyNoticeObserver(OrderEventType.DISUSE, uKey, orders);
            }
        } catch (Exception e) {
            throw new BusinessException("类型转换错误");
        }
    }

    private Long customerKey(Long customerKey) {
        if (customerKey != null && customerKey > 0) {
            CustomerConcise concise = customerService.loadCustomerConcise(customerKey);
            if (concise != null) {
                return concise.isEnterprise() ? concise.getCompanyKey() : concise.getId();
            }
        }
        return null;
    }

    @Override
    public CustomPage<OrderConcise> pageOrderConcise(Integer uKey, OrderSearch search, PageScope scope) throws
            ParameterException, BusinessException {
        logger.debug("pageOrderSig() --> uKey:{} search:{} scope:{}", uKey, search, scope);
        Assert.notBlank(uKey, "操作人不能为空");
        CompanyEmployee employee = companyService.getCompanyEmployee(uKey);
        if (employee != null) {
            search.setCompanyKey(employee.getCompanyId());
            search.setManage(EmployeeType.convert(employee.getEmployeeType()).isManage());
        } else {
            return new CustomPage<OrderConcise>(scope.getPageNum(), scope.getPageSize(), 0, Collections.emptyList());
        }
        search.setShipperKey(customerKey(search.getShipperKey()));
        search.setReceiveKey(customerKey(search.getReceiveKey()));
        search.setConveyKey(customerKey(search.getConveyKey()));

        search.setUserKey(uKey);
        CustomExample example = buildExample(new CustomExample(Order.class), search);
        example.setGroupColumns(example.selectColumn("shipperId"), example.selectColumn("conveyId"), example.selectColumn("receiveId"), example.dateFormatColumn("createTime", "%Y-%m-%d"));
        example.orderBy("createTime").desc();
        try {
            Page<Order> page = orderMapper.selectGroupByExample(example, new RowBounds(scope.getPageNum(), scope.getPageSize()));
            Collection<OrderConcise> collection = new ArrayList<OrderConcise>(page.size());
            for (Order order : page) {
                OrderConcise concise = new OrderConcise(order.getShipperId(), order.getReceiveId(), order.getConveyId(), order.getCreateTime());
                concise.setShipper(companyService.getCompanyConciseByKey(concise.getShipperKey()));
                concise.setReceive(companyService.getCompanyConciseByKey(concise.getReceiveKey()));
                concise.setConvey(companyService.getCompanyConciseByKey(concise.getConveyKey()));

                collection.add(concise);
            }
            return new CustomPage<OrderConcise>(page.getPageNum(), page.getPageSize(), page.getTotal(), collection);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("pageOrderSig() --> uKey:{} search:{} scope:{}", uKey, search, scope, e);
            throw BusinessException.dbException("查询订单信息异常");
        }
    }

    /**
     * 查询订单信息
     *
     * @param uKey
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<Order> listOrder(Integer uKey, OrderSearch search) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        CompanyEmployee employee = companyService.getCompanyEmployee(uKey);
        if (employee != null) {
            search.setCompanyKey(employee.getCompanyId());
            search.setManage(EmployeeType.convert(employee.getEmployeeType()).isManage());

            search.setShipperKey(customerKey(search.getShipperKey()));
            search.setReceiveKey(customerKey(search.getReceiveKey()));
            search.setConveyKey(customerKey(search.getConveyKey()));
            if (search.isEreceipt() || CollectionUtils.isNotEmpty(search.getReceiptFettles())) {//查询电子回单
                return orderMapper.listBySomething(search);
            } else {
                return orderMapper.selectByExample(buildExample(search, Order.class));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<OrderAlliance> listOrderAlliance(Integer uKey, OrderSearch orderSearch) throws ParameterException, BusinessException {
        return alliance(uKey, listOrder(uKey, orderSearch), O.DEFAULT_LISTS);
    }

    @Override
    public CustomPage<OrderAlliance> pageBindOrder(Integer uKey, OrderSearch search, PageScope scope) {
        logger.debug("page order --> uKey:{} search:{} scope:{}", uKey, search, scope);
        Assert.notBlank(uKey, "操作人不能为空");
        try {
            //search.setPartnerType(PartnerType.SHIPPER);
            CompanyEmployee employee = companyService.getCompanyEmployee(uKey);
            if (employee != null) {
                search.setCompanyKey(employee.getCompanyId());
                search.setManage(EmployeeType.convert(employee.getEmployeeType()).isManage());
            } else {
                return new CustomPage<OrderAlliance>(scope.getPageNum(), scope.getPageSize(), 0, Collections.emptyList());
            }
            if (StringUtils.isNotBlank(search.getLikeString())) {
                Company company = companyService.getCompanyByName(search.getLikeString());
                if (null != company && company.getId() > 0) {
                    search.setReceiveKey(company.getId());
                }
            }

            search.setShipperKey(customerKey(search.getShipperKey()));
            search.setReceiveKey(customerKey(search.getReceiveKey()));
            search.setConveyKey(customerKey(search.getConveyKey()));

            search.setUserKey(uKey);
            RowBounds rowBounds = new RowBounds(scope.getPageNum(), scope.getPageSize());
            CustomExample example = buildExample(new CustomExample(Order.class), search);
            example.and().andIsNull("bindCode");
            example.orderBy("createTime").desc();
            Page<Order> page = orderMapper.selectPageByExample(example, rowBounds);
            return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), alliance(uKey, page, O.enterprise));
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("page order --> uKey:{} search:{} scope:{}", uKey, search, scope, e);
            throw BusinessException.dbException("查询订单信息异常");
        }
    }

    private Order receiveOrder(Order order, Date cdate) {
        order.setUpdateTime(cdate);
        order.setSignFettle(SignFettle.INTACT.getCode());
        if (!OrderFettleType.convert(order.getSignFettle()).isComplete()) {
            if (order.getArrivalTime() != null) {
                if (order.getArrivalTime().getTime() - cdate.getTime() >= 0) {
                    order.setDelayWarning(CoreConstants.DELAY_WARNING_NORMAL);
                } else {
                    order.setDelayWarning(CoreConstants.DELAY_WARNING_ALREADY);
                }
            }
            if (order.getArrivedTime() == null) {
                order.setArrivedTime(cdate);
            }
            order.setReceiveTime(cdate);
            order.setFettle(OrderFettleType.COMPLETE.getCode());
        }
        return order;
    }

    @Override
    public void modifyOrderArrivelStatus(Integer uKey, List<Long> orderIds) throws ParameterException, BusinessException {
        Assert.notEmpty(orderIds, "订单编号不能为空");
        Example example = new Example(Order.class);
        example.createCriteria().andIn("id", orderIds).andNotEqualTo("fettle", OrderFettleType.COMPLETE.getCode());
        Collection<Order> collection = orderMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(collection)) {
            Date cdate = new Date();
            collection = collection.stream().peek(o -> receiveOrder(o, cdate)).collect(Collectors.toList());

            orderMapper.updates(collection);
            //订单确认收货事件
            modifyNoticeObserver(OrderEventType.RECEIVE, uKey, collection);
        }
    }

    @Override
    public void modifyOneOrderArrivelStatus(Integer userId, Long orderId) throws ParameterException, BusinessException {
        Assert.notBlank(orderId, "订单编号不能为空");
        Order order = getOrderByKey(orderId);
        if (order == null) {
            throw new BusinessException("订单信息查询异常");
        }
        Company company = companyService.assertCompanyByUserKey(userId);
        /*
        if (order.getReceiveId() - currCompany.getId() != 0) {
            throw new ParameterException("只有收货方才可以进行到货操作");
        }
        */
        order = receiveOrder(order, new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        //订单确认收货事件
        modifyNoticeObserver(OrderEventType.RECEIVE, userId, order);
    }

    private CustomExample buildExample(OrderSearch search, Class<?> clazz) {
        CustomExample example = buildExample(new CustomExample(clazz), search);
        example.orderBy("createTime").desc().orderBy("deliveryTime").desc();
        return example;
    }

    private CustomExample buildExample(CustomExample example, OrderSearch search) {
        CustomExample.Criteria criteria = example.createCriteria();
        PartnerType partnerType;
        if (!search.isAll()) {
            if (search.getPartnerType() != null) {
                partnerType = search.getPartnerType();
                if (partnerType.shipper()) {
                    //criteria.andEqualTo("shipperId", search.getCompanyKey());
                    search.setShipperKey(search.getCompanyKey());
                } else if (partnerType.receive()) {
                    //criteria.andEqualTo("receiveId", search.getCompanyKey());
                    search.setReceiveKey(search.getCompanyKey());
                } else if (partnerType.convey()) {
                    //criteria.andEqualTo("conveyId", search.getCompanyKey());
                    search.setConveyKey(search.getCompanyKey());
                }
            } else {
                example.and().orEqualTo("receiveId", search.getCompanyKey()).orEqualTo("conveyId", search.getCompanyKey()).orEqualTo("shipperId", search.getCompanyKey());
            }
            criteria.andEqualTo("receiveId", search.getReceiveKey());
            criteria.andEqualTo("conveyId", search.getConveyKey());
        }
        if (search.getShipperKey() != null && search.getShipperKey() > 0) {
            criteria.andEqualTo("shipperId", search.getShipperKey());
        }
        criteria.andBetween("deliveryTime", search.getFirstTime(), search.getSecondTime());

        if (CollectionUtils.isNotEmpty(search.getFettles())) {
            criteria.andIn("fettle", search.getFettles());
        }
        if (CollectionUtils.isNotEmpty(search.getSignFettles())) {
            criteria.andIn("signFettle", search.getSignFettles());
        }
        //条码是否绑定
        if (search.getBindStatus() != null) {
            if (search.getBindStatus() == 0) {
                criteria.andIsNull("bindCode");
            } else if (search.getBindStatus() == 1) {
                criteria.andIsNotNull("bindCode");
            }
        }
        //是否上传回单
        if (search.getUploadReceipt() != null) {
            if (search.getUploadReceipt()) {
                criteria.andGreaterThan("isReceipt", 0);
            } else {
                criteria.andLessThanOrEqualTo("isReceipt", 0);
            }
        }
        if (search.isEreceipt()) {
            criteria.andEqualTo("clientType", 0);
        }
        search.setLikeString(StringUtils.trim(search.getLikeString()));
        if (StringUtils.isNotBlank(search.getLikeString())) {
            CustomExample.Criteria A = example.createCriteria();
            A.orLike("id", search.getLikeString()).orLike("bindCode", search.getLikeString()).orLike("orderNo", search.getLikeString()).orLike("deliveryNo", search.getLikeString()).orLike("receiverName", search.getLikeString()).orLike("receiveAddress", search.getLikeString());
            //if (!search.isAll()) {
            //    search.setCompanyKeys(customerService.listKeyLikeName(search.getUserKey(), search.getLikeString()));
            // }
            // example.and(conversionExample(A, search));
        }
        //if (!search.isManage() && !search.isAll()) {
        //   search.setCompanyKeys(customerService.listKeyLikeName(search.getUserKey(), StringUtils.EMPTY));
        //    conversionExample(example.and(), search);
        // }
        return example;
    }


    public CustomExample.Criteria conversionExample(CustomExample.Criteria criteria, OrderSearch search) {
        if (CollectionUtils.isNotEmpty(search.getCompanyKeys())) {
            if (search.getShipperKey() == null || search.getShipperKey() <= 0) {
                criteria.orIn("shipperId", search.getCompanyKeys());
            }
            if (search.getConveyKey() == null || search.getConveyKey() <= 0) {
                criteria.orIn("conveyId", search.getCompanyKeys());
            }
            if (search.getReceiveKey() == null || search.getReceiveKey() <= 0) {
                criteria.orIn("receiveId", search.getCompanyKeys());
            }
        }
        return criteria;
    }

    @Override
    public Collection<OrderCommodity> listCommodity(Integer uKey, Long orderKey) throws ParameterException, BusinessException {
        Assert.notBlank(orderKey, "订单编号不能为空");
        Example example = new Example(OrderCommodity.class);
        example.createCriteria().andEqualTo("orderId", orderKey);
        return commodityMapper.selectByExample(example);
    }


    /**
     * 通知回单上传
     *
     * @param receipt 回单信息
     * @throws BusinessException
     */
    @Override
    public void notifyUploadReceipt(PaperReceipt receipt) throws BusinessException {
        try {
            if (receipt.getReceiptCount() != null && receipt.getReceiptCount() > 0) {
                Order order = getOrderByKey(receipt.getOrderKey());
                if (null == order) {
                    throw new BusinessException("该订单不存在");
                }
                order.setIsReceipt(Optional.ofNullable(order.getIsReceipt()).orElse(0) + receipt.getReceiptCount());
                OrderFettleType fettleType = OrderFettleType.convert(order.getFettle());
                if (!fettleType.isArrived() && !fettleType.isComplete()) {
                    //order = receiveOrder(receipt.getUserId(), order, receipt.getCreateTime());
                    order.setArrivedTime(receipt.getCreateTime());
                    order.setFettle(OrderFettleType.ARRIVED.getCode());

                    if (StringUtils.isNotBlank(receipt.getLocation())) {
                        locationTrackService.saveTrack(receipt.getUserId(), LocationType.ORDER, new LocationTrack(order.getId(), receipt.getLongitude(), receipt.getLatitude(), receipt.getLocation()));
                    }
                    orderMapper.updateByPrimaryKeySelective(order);
                    //上传回单日志
                    modifyNoticeObserver(OrderEventType.UPLOADRECEIPT, receipt.getUserId(), order);
                    //加到货日志
                    modifyNoticeObserver(OrderEventType.ARRIVED, receipt.getUserId(), order);
                }

            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("更新订单回单上传状态异常 {}", receipt, e);
        }
    }

    /**
     * 通知回单签署
     *
     * @param signature 签署信息
     * @throws BusinessException
     */
    @Override
    public void notifySignReceipt(ReceiptEventType eventType, ReceiptSignature signature, String exceptionContent) throws BusinessException {
        try {
            if (eventType.isValidate()) {//签署准备
                if (signature.getOrderId() == null || signature.getOrderId() <= 0) {
                    throw new ParameterException("订单信息异常");
                }
                signature.setOrder(Optional.of(getOrderByKey(signature.getOrderId())).orElseThrow(() -> new BusinessException("获取订单信息异常")));
            }
            if (eventType.isSign()) {//签署操作
                Order order = signature.getOrder();
                OrderRoleType roleType = signature.getRoleType();
                order.setUpdateTime(new Date());
                if (roleType.isReceive()) {
                    order = receiveOrder(order, signature.getCreateTime());
                    if (StringUtils.isNotBlank(exceptionContent)) {//异常签收
                        OrderException exception = new OrderException(exceptionContent, signature.getUserId());
                        exception.setOrderNo(signature.getOrderId());
                        exception.setCreateTime(signature.getCreateTime());
                        exception.setUpdateTime(signature.getCreateTime());
                        exceptionMapper.insertSelective(exception);
                        order.setSignFettle(SignFettle.ABNORMAL.getCode());
                    }
                    modifyNoticeObserver(OrderEventType.RECEIVE, signature.getUserId(), order);
                }
                modifyNoticeObserver(OrderEventType.SIGN, signature.getUserId(), order);
                orderMapper.updateByPrimaryKeySelective(order);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("签署异常 --> {} {}", signature, exceptionContent, e);
            throw BusinessException.dbException("签署异常");
        }
    }

    /**
     * 通知装车
     *
     * @param container
     * @throws BusinessException
     */
    @Override
    public void notifyLoadContainer(DriverContainer container, BarcodeContext context) throws BusinessException {
        if (context != null && context instanceof CompanyCodeContext) {
            if (context.fettle().unbind()) {
                throw new BusinessException("还没有绑单订单,不能装车");
            }
            CompanyCodeContext companyCodeContext = (CompanyCodeContext) context;
            OrderAlliance alliance = getAllianceByCode(container.getLoadId(), container.getBarcode());
            if (alliance != null) {
                companyCodeContext.setOrderKey(alliance.getId());
                CustomerConcise company = alliance.getByRole(OrderRoleType.RECEIVE);
                if (company != null) {
                    container.setReceiverName(company.getCompanyName());
                }
                container.setContactName(alliance.getReceiverName());
                container.setContactNumber(alliance.getReceiverContact());
                container.setReceiveAddress(alliance.getReceiveAddress());
                container.setDeliveryNumber(alliance.getDeliveryNo());
                modifyNoticeObserver(OrderEventType.LOAD_DRIVER, container.getLoadId(), alliance);
            }
        }
    }

    /**
     * 通知客户关联注册
     *
     * @param customerKey 客户编号
     * @param companyKey  注册企业编号
     * @throws BusinessException 逻辑异常
     */
    @Override
    public void associateCustomerCompany(Long customerKey, Long companyKey) throws BusinessException {
        //关联客户时，更新订单信息
        try {
            Example example = new CustomExample(Order.class).selectProperties("id", "clientType");
            for (PartnerType partner : PartnerType.values()) {
                example.createCriteria().andIn("clientType", partner.abnormals()).andEqualTo(partner.shipper() ? "shipperId" : partner.receive() ? "receiveId" : "conveyId", customerKey);
                Collection<Order> orders = orderMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(orders)) {
                    for (Order o : orders) {
                        if (partner.shipper()) {
                            o.setShipperId(companyKey);
                        }
                        if (partner.receive()) {
                            o.setReceiveId(companyKey);
                        }
                        if (partner.convey()) {
                            o.setConveyId(companyKey);
                        }
                        o.setClientType(partner.clientType(o.getClientType(), true));
                    }
                    orderMapper.updates(orders);

                    modifyNoticeObserver(OrderEventType.UPDATE_PARTNER, 0, orders);
                }
                example.clear();
            }
        } catch (Exception e) {
            throw new BusinessException("客户关联异常", e);
        }
    }


    /**
     * 对外查询详情数据
     *
     * @param objectType 对象类型
     * @param objectKey  对象编号
     * @return
     */
    @Override
    public Object adventiveObjectByKey(ObjectType objectType, Serializable objectKey) throws BusinessException {
        if (ObjectType.ORDER == objectType && objectKey != null) {
            Order order = getOrderByKey(Long.valueOf(objectKey.toString()));
            if (null != order) {
                //订单数据,需要订单基本数据，三方公司数据，财务数据，运输数据
                return alliance(null, order, O.enterprise, O.extradata, O.customdata, O.commodities);
            }
        }
        if (ObjectType.SYNCFETTLE == objectType && objectKey != null) {
            Order order = getOrderByKey(Long.valueOf(objectKey.toString()));
            if (null != order) {
                return new AdventiveFettle(ObjectType.ORDER, order.getId(), order.getFettle(), Optional.ofNullable(order.getReceiveTime()).orElse(new Date()).getTime());
            }
        }
        return null;
    }

    @Override
    public Order getOrderBySD(Long shipperId, String deliveryNo) throws ParameterException, BusinessException {
        Assert.notBlank(deliveryNo, "送货单号不能为空");
        Assert.notBlank(shipperId, "发货方编号不能为空");
        try {
            Example example = new Example(Order.class);
            example.createCriteria().andEqualTo("deliveryNo", deliveryNo).andEqualTo("shipperId", shipperId);
            List<Order> orders = orderMapper.selectByExample(example);
            if (orders != null && orders.size() > 0) {
                return orders.get(0);
            }
            return null;
            //return orderMapper.selectOne(new Order(shipperId, deliveryNo));
        } catch (Exception e) {
            logger.error("getOrderByKey----->shipperId:{},deliveryNo:{},e:{}", shipperId, deliveryNo, e);
            throw BusinessException.dbException("订单详情查询异常");
        }
    }

    @Override
    public OrderExtra getOrderExtraByOrderKey(Long orderKey) throws ParameterException, BusinessException {
        Assert.notBlank(orderKey, "订单号不能为空");
        try {
            return orderExtraMapper.selectByType(Constants.ORDER_EXTRA_SHIP, orderKey);
        } catch (Exception e) {
            logger.error("getOrderExtraByOrderKey----->orderKey:{} ", orderKey, e);
            throw BusinessException.dbException("订单额外信息查询异常");
        }
    }

    /**
     * 开发请不要使用.给余维测试使用.用于批量绑单推送数据给tms使用
     */
    @Override
    public void bindCodeForTest(Integer uKey, List<Long> orderKeys, List<String> qrcodes) {
        Integer size = orderKeys.size() >= qrcodes.size() ? qrcodes.size() : orderKeys.size();
        for (Integer i = 0; i < size; i++) {
            Long orderKey = orderKeys.get(i);
            Order order = getOrderByKey(orderKey);
            order.setBindCode(qrcodes.get(i));
            order.setUpdateTime(new Date());
            if (order.getDeliveryTime() == null) {
                order.setDeliveryTime(order.getUpdateTime());
                if (OrderFettleType.convert(order.getFettle()).isDefault()) {
                    order.setFettle(OrderFettleType.ING.getCode());
                }
            }
            orderMapper.updateByPrimaryKeySelective(order);
            modifyNoticeObserver(OrderEventType.BINDCODE, uKey, order);
        }
    }

    @Override
    public void updateDeliveryNo(Long orderKey, String deliveryNo) throws ParameterException, BusinessException {
        Assert.notBlank(orderKey, "订单编号不能为空");
        Assert.notBlank(deliveryNo, "配送单号不能为空");
        try {
            Order order = orderMapper.selectByPrimaryKey(orderKey);
            if (order == null) {
                throw new BusinessException("订单不存在");
            }
            orderMapper.updateDeliveryNo(order.getId(), deliveryNo);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("配送单号更新异常 orderKey:{}  deliveryNo:{}", orderKey, deliveryNo);
            throw BusinessException.dbException("配送单号更新异常");
        }
    }

    @Override
    public void modifyEvaluation(Long orderKey, Integer evaluation) throws ParameterException, BusinessException {
        Assert.notBlank(orderKey, "订单编号不能为空");
        Assert.notNull(evaluation, "评价选项不能为空");
        try {
            Example example = new Example(Order.class);
            example.createCriteria().andEqualTo("id", orderKey);
            Order order = new Order();
            order.setEvaluation(evaluation);
            order.setUpdateTime(new Date());
            orderMapper.updateByExampleSelective(order, example);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("订单评价异常 orderKey:{} evaluation:{}", orderKey, evaluation);
            throw BusinessException.dbException("订单评价异常");
        }

    }

    @Override
    public void modifyException(Collection<Long> orderKeys, String content, Integer userKey, Integer type) throws ParameterException, BusinessException {
        Assert.notEmpty(orderKeys, "订单编号不能为空");
        Assert.notBlank(content, "异常描述不能为空");
        Assert.notBlank(userKey, "用户编号不能为空");
        Assert.notBlank(type, "异常类型不能为空");
        try {
            if (CollectionUtils.isNotEmpty(orderKeys)) {
                orderKeys.forEach(o -> {
                    Order order = orderMapper.selectByPrimaryKey(o);
                    if (null != order) {
                        if (order.getFettle() - OrderFettleType.COMPLETE.getCode() == 0) {
                            throw new BusinessException("该订单已完成不能重复操作");
                        }
                        order = receiveOrder(order, new Date());
                        order.setSignFettle(SignFettle.ABNORMAL.getCode());
                        orderMapper.updateByPrimaryKeySelective(order);
                        OrderException exception = new OrderException(o, content, userKey, new Date(), type);
                        exceptionMapper.insertSelective(exception);
                    }
                });
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("订单签收异常 orderKeys:{} content:{} userKey:{}", orderKeys, content, userKey);
            throw BusinessException.dbException("订单签收异常");
        }
    }

    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        if (StringUtils.equalsIgnoreCase(QueueKeys.MESSAGE_TYPE_ORDER_TIMEOUT, messageType)) {
            Order order = Optional.ofNullable(object).map(o -> getOrderByKey(Long.parseLong(o.toString()))).orElse(null);
            if (order != null && !OrderFettleType.convert(order.getFettle()).isComplete()) {
                order = receiveOrder(order, new Date());

                orderMapper.updateByPrimaryKeySelective(order);

                modifyNoticeObserver(OrderEventType.RECEIVE, UserUtil.SYSTEM_UID, order);
            }
        }
        return false;
    }
}
