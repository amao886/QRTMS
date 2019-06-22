/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:58:31
 */
package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.*;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.driver.OrderDeliver;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.persistence.driver.OrderDeliverMapper;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.observer.*;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * 司机装车
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:58:31
 */
@Service("ksh.core.service.driverService")
public class DriverServiceImpl implements DriverService, BarcodeObserverAdapter, WaybillObserverAdapter, ReceiptObserverAdapter, OrderObserverAdapter, LocationObserverAdapter {

    @Resource
    protected BarCodeService barcodeService;
    @Resource
    protected WaybillService waybillService;
    @Resource
    protected AutoMapService autoMapService;
    @Resource
    protected WeChatApiService apiService;
    @Resource
    protected ImageStorageService imageStorageService;
    @Resource
    protected WaybillTrackService trackService;
    @Resource
    protected ReceiptService receiptService;
    @Resource
    protected OrderService orderService;


    @Resource
    protected DriverContainerMapper driverContainerMapper;
    @Resource
    protected OrderDeliverMapper orderDeliverMapper;
    @Resource
    protected DriverTrackMapper driverTrackMapper;
    @Resource
    protected TransitionTrackMapper transitionTrackMapper;
    @Resource
    protected UserMapper userMapper;
    @Resource
    protected CompanyMapper companyMapper;


    @Autowired(required = false)
    Collection<DriverContainerObserverAdapter> observers;

    @Override
    public void onInitializeWaybill(WaybillContext context) throws BusinessException {
        if (StringUtils.isNotBlank(context.getBarcode())) {
            try {
                if (context.getWaybillStatus().bind() && context.getDeliveryTime() == null) {
                    DriverContainer container = driverContainerMapper.first(context.getBarcode());
                    if (container != null) {
                        context.setDeliveryTime(container.getLoadTime());//更新发货时间
                    }
                }
            } catch (Exception e) {
                logger.error("创建任务单时更新数据异常 {}", context, e);
                throw new BusinessException("创建任务单时更新数据异常");
            }
        }
    }

    /**
     * 任务单绑定或更新完成事件
     * <p>
     *
     * @param context 任务单上下文
     * @param binding true:绑定，false:更新
     * @throws BusinessException
     */
    @Override
    public void onCompleteWaybill(WaybillContext context, boolean binding) throws BusinessException {
        if (StringUtils.isNotBlank(context.getBarcode())) {
            try {
                DriverContainer container = driverContainerMapper.last(context.getBarcode());
                if (container != null) {
                    DriverContainer updater = new DriverContainer();
                    updater.setBarcode(context.getBarcode());
                    updater.setReceiverName(context.getReceiverName());
                    updater.setReceiveAddress(context.getStartStation().replace("-", "") + context.getReceiveAddress());
                    updater.setContactName(context.getContactName());
                    updater.setContactNumber(context.getContactPhone());
                    updater.setDeliveryNumber(context.getDeliveryNumber());
                    updater.setLatitude(context.getLatitude());
                    updater.setLongitude(context.getLongitude());
                    updater.setBindStatus(WaybillFettle.BOUND.getCode());

                    Example example = new Example(DriverContainer.class);
                    example.createCriteria().andEqualTo("barcode", context.getBarcode());
                    driverContainerMapper.updateByExampleSelective(updater, example);
                }
            } catch (Exception e) {
                logger.error("创建任务单时更新数据异常 {}", context, e);
                throw new BusinessException("创建任务单时更新数据异常");
            }
        }
    }

    @Override
    public void onWaybillFettleChange(WaybillContext waybillContext) throws BusinessException {
        if (StringUtils.isNotBlank(waybillContext.getBarcode())) {
            try {
                WaybillFettle waybillFettle = waybillContext.getWaybillStatus();
                if (waybillFettle.receive()) {//确认到货时
                    //确认到货时卸货
                    unloadQuiet(waybillContext.getUserKey(), waybillContext.getBarcode());
                }
            } catch (Exception e) {
                logger.error("任务单状态变化时卸货异常 {}", waybillContext, e);
                throw new BusinessException("任务单状态变化时卸货异常");
            }
        }
    }

    @Override
    public void notifyBarcodeValidate(Barcode barcode, BarcodeContext context) {
        DriverContainer container = driverContainerMapper.last(barcode.getBarcode());
        if (container != null) {
            context.setLoadKey(container.getLoadId());
            context.setUnloadKey(container.getUnloadId());
        }
        if (context instanceof GroupCodeContext) {
            GroupCodeContext codeContext = (GroupCodeContext) context;
            codeContext.setNeedBingImage(false);
            if (codeContext.getStatus().unbind()) {
                Integer count = imageStorageService.count(Constant.IMAGE_TYPE_DELIVERY, barcode.getBarcode());
                if (count == null || count <= 0) {
                    codeContext.setNeedBingImage(true);
                }
            }
        }
    }

    @Override
    public void notifyTransitionReceipt(TransitionReceipt receipt, Integer count) throws BusinessException {
        if (StringUtils.isNotBlank(receipt.getBarcode()) && count != null && count > 0) {
            try {
                //上传回单时卸货
                unloadQuiet(receipt.getUserId(), receipt.getBarcode());
            } catch (Exception e) {
                logger.error("上传回单卸货时异常 {} {}", receipt, count, e);
                throw new BusinessException("上传回单卸货时异常");
            }
        }
    }

    @Override
    public void notifyUploadReceipt(WaybillContext context, WaybillReceipt receipt, Integer count) throws BusinessException {
        if (StringUtils.isNotBlank(context.getBarcode()) && count != null && count > 0) {
            try {
                //上传回单时卸货
                unloadQuiet(context.getUserKey(), context.getBarcode());
            } catch (Exception e) {
                logger.error("上传回单卸货时异常 {} {}", receipt, count, e);
                throw new BusinessException("上传回单卸货时异常");
            }
        }
    }

    @Override
    public MergeDriverContainer getMergeByBarcode(String barcode) throws ParameterException, BusinessException {
        Assert.notBlank(barcode, "条码号不能为空");
        DriverContainer container = driverContainerMapper.last(barcode);
        if (container == null) {
            throw new ParameterException(barcode, "指定的装车信息不存在");
        }
        try {
            MergeDriverContainer mergeDriverContainer = new MergeDriverContainer(container);
            LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createUserCache(userMapper);
            mergeDriverContainer.setLoader(associateUserCache.get(mergeDriverContainer.getLoadId()));
            mergeDriverContainer.setUnloader(associateUserCache.get(mergeDriverContainer.getUnloadId()));
            mergeDriverContainer.setImages(imageStorageService.list(Constant.IMAGE_TYPE_DELIVERY, mergeDriverContainer.getBarcode()));
            mergeDriverContainer.setTracks(listDriverTrack(mergeDriverContainer.getBarcode()));
            mergeDriverContainer.setReceipts(receiptService.listTransitionReceipt(mergeDriverContainer.getBarcode()));
            return mergeDriverContainer;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("getMergeByBarcode -> barcode:{}", barcode, e);
            throw BusinessException.dbException("根据barcode查询装车详情 异常");
        }
    }

    @Override
    public MergeDriverContainer getMergeByKey(Long dKey) throws ParameterException, BusinessException {
        if (dKey == null || dKey <= 0) {
            throw new ParameterException(dKey, "装车编号不能为空");
        }
        DriverContainer container = driverContainerMapper.selectByPrimaryKey(dKey);
        if (container == null) {
            throw new ParameterException(dKey, "指定的装车信息不存在");
        }
        try {
            MergeDriverContainer mergeDriverContainer = new MergeDriverContainer(container);
            LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createUserCache(userMapper);
            mergeDriverContainer.setLoader(associateUserCache.get(mergeDriverContainer.getLoadId()));
            mergeDriverContainer.setUnloader(associateUserCache.get(mergeDriverContainer.getUnloadId()));
            mergeDriverContainer.setImages(imageStorageService.list(Constant.IMAGE_TYPE_DELIVERY, mergeDriverContainer.getBarcode()));
            mergeDriverContainer.setTracks(listDriverTrack(mergeDriverContainer.getBarcode()));
            mergeDriverContainer.setReceipts(receiptService.listTransitionReceipt(mergeDriverContainer.getBarcode()));
            return mergeDriverContainer;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("getMergeByKey -> dKey:{}", dKey, e);
            throw BusinessException.dbException("查询装车详情异常");
        }
    }

    @Override
    public Collection<DriverContainer> listByLoadKey(Integer loadKey) throws ParameterException, BusinessException {
        Example example = new Example(DriverContainer.class);
        example.createCriteria().andEqualTo("loadId", loadKey).andEqualTo("unload", false);
        return driverContainerMapper.selectByExample(example);
    }

    public Collection<ImageStorage> getImageStorages(String barCode) {
        return imageStorageService.list(Constant.IMAGE_TYPE_TRANSITION_RECEIPT, barCode);
    }

    private DriverContainer initialize(Integer uKey, String code) {
        DriverContainer container = new DriverContainer();
        container.setBarcode(code);
        container.setLoadId(uKey);
        container.setLoadTime(new Date());
        container.setBindStatus(Constant.WAYBILL_STATUS_WAIT);
        container.setUnload(false);
        return container;
    }

    @Override
    public String load(Integer uKey, String code) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户ID不能为空");
        Assert.notBlank(code, "条码号不能为空");
        BarcodeContext context = barcodeService.validateNotDecrypt(uKey, code);
        try {
            logger.info("===================" + context);
            if (context.isComplete()) {
                throw new BusinessException("该任务已经送达");
            }
            if (context.isNeedBingImage()) {
                return context.getBarcode();
            }
            if (context.getLoadKey() != null && context.getUnloadKey() == null) {//如果已经装车，并且没有卸货
                if (context.getLoadKey() - uKey == 0) {//如果装车人是自己
                    throw new BusinessException("宝贝已经在车上了");
                } else {//装车人不是自己的，先卸货再装货
                    unloadQuiet(uKey, context.getBarcode());
                    loadByBarcodeContext(uKey, context);
                }
            } else {//没有装车或者已经卸货了
                loadByBarcodeContext(uKey, context);
            }
            return null;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("load -> uKey:{} code:{}", uKey, code, e);
            throw BusinessException.dbException("装车异常");
        }
    }


    private void loadByBarcodeContext(Integer uKey, BarcodeContext context) {
        DriverContainer container = new DriverContainer();
        container.setId(Globallys.nextKey());
        container.setBarcode(context.getBarcode());
        container.setBindStatus(context.fettle().getCode());
        container.setLoadId(uKey);
        container.setUnload(false);
        container.setLoadTime(new Date());
        if (CollectionUtils.isNotEmpty(observers)) {
            for (DriverContainerObserverAdapter observer : observers) {
                observer.notifyLoadContainer(container, context);
            }
        }
        if (StringUtils.isNotBlank(container.getReceiveAddress()) &&
                (container.getLatitude() == null || container.getLongitude() == null)) {
            AutoMapLocation location = autoMapService.coordinate(container.getReceiveAddress());
            if (location != null) {
                container.setLatitude(location.getLatitude());
                container.setLongitude(location.getLongitude());
            }
        }
        driverContainerMapper.insertSelective(container);
    }

    @Override
    public String unload(Integer uKey, String code) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户ID不能为空");
        Assert.notBlank(code, "条码号不能为空");
        Barcode barcode = barcodeService.getBarcode(code);
        try {
            DriverContainer exister = driverContainerMapper.last(barcode.getBarcode());
            if (exister == null) {
                throw new BusinessException("该条码未装车无需卸货");
            } else if (exister.getUnloadId() != null || exister.getUnload()) {
                throw new BusinessException("已经卸货");
            } else {
                unloadQuiet(uKey, barcode.getBarcode());
            }
            return barcode.getBarcode();
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("unload -> uKey:{} code:{}", uKey, code, e);
            throw BusinessException.dbException("卸货异常");
        }
    }


    private void unloadQuiet(Integer unloadKey, String barcode) {
        if (driverContainerMapper.countUnloadByCode(barcode) > 0) {
            DriverContainer container = new DriverContainer();
            container.setUnload(true);
            container.setUnloadId(unloadKey);
            container.setUnloadTime(new Date());
            Example example = new Example(DriverContainer.class);
            example.createCriteria().andEqualTo("barcode", barcode).andEqualTo("unload", false);
            driverContainerMapper.updateByExampleSelective(container, example);
        }
    }

    @Override
    public CustomPage<MergeDriverContainer> pageByDriver(DriverContainerSearch serach, PageScope pageScope) throws ParameterException, BusinessException {
        Page<DriverContainer> page = driverContainerMapper.listByDriver(serach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        Collection<MergeDriverContainer> collection = new ArrayList<MergeDriverContainer>(page.size());
        if (CollectionUtils.isNotEmpty(page)) {
            LocalCacheManager<AssociateUser> localCache = LocalCacheFactory.createUserCache(userMapper);
            for (DriverContainer driverContainer : page) {
                try {
                    MergeDriverContainer mergeDriverContainer = new MergeDriverContainer(driverContainer);
                    mergeDriverContainer.setLoader(localCache.get(driverContainer.getLoadId()));
                    if (driverContainer.getUnloadId() != null) {
                        mergeDriverContainer.setUnloader(localCache.get(driverContainer.getUnloadId()));
                    }
                    collection.add(mergeDriverContainer);
                } catch (Exception e) {
                    logger.error("merge DriverContainer exception", e);
                }
            }
        }
        return new CustomPage<MergeDriverContainer>(page.getPageNum(), page.getPageSize(), page.getTotal(), collection);
    }

    @Override
    public void saveDeliveryImage(Integer uKey, String code, Collection<String> collection, boolean fromWx)
            throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户ID不能为空");
        Assert.notNull(code, "条码信息不能为空");
        Assert.notEmpty(collection, "送货单图片信息不能为空");
        try {
            Barcode barcode = barcodeService.getBarcode(code);
            if (barcode == null) {
                throw new ParameterException(code, "无效的条码号");
            }
            DriverContainer container = driverContainerMapper.last(barcode.getBarcode());
            if (container == null) {
                container = initialize(uKey, barcode.getBarcode());
                container.setId(Globallys.nextKey());
                driverContainerMapper.insertSelective(container);
            }
            if (CollectionUtils.isNotEmpty(collection)) {
                if (fromWx) {
                    collection = apiService.downImages(collection);
                }
                if (CollectionUtils.isNotEmpty(collection)) {
                    imageStorageService.save(Constant.IMAGE_TYPE_DELIVERY, container.getBarcode(), collection);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveDeliveryImage -> uKey:{} barcode:{} paths:{}", uKey, code, collection, e);
            throw BusinessException.dbException("送货单图片保存异常");
        }
    }

    @Override
    public CustomPage<DriverContainer> pageBySomething(DriverContainerSearch serach, PageScope pageScope) throws ParameterException, BusinessException {
        Page<DriverContainer> page = driverContainerMapper.listBySomething(serach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<DriverContainer>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public List<DriverContainer> queryDriverContainerList(Integer userKey, DriverContainerSearch search) throws ParameterException, BusinessException {
        search.setBindStatus(Constant.BARCODE_STATUS_NO);
        search.setUserId(userKey);
        search.setScope(0);
        return driverContainerMapper.listBySomething(search);
    }

    @Override
    public Integer loadCount(Integer userKey) throws ParameterException, BusinessException {
        Example example = new Example(DriverContainer.class);
        example.createCriteria().andEqualTo("loadId", userKey).andEqualTo("unload", false);
        return driverContainerMapper.selectCountByExample(example);
    }

    /**
     * 司机定位
     *
     * @param uKey
     * @param barcode
     * @param driverTrack
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveLoaction(Integer uKey, String barcode, DriverTrack driverTrack) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户ID不能为空");
        Assert.notNull(driverTrack, "轨迹信息不能为空");
        Assert.notBlank(driverTrack.getReportLoaction(), "轨迹地址不能为空");
        try {
            Collection<DriverContainer> containers = null;
            if (StringUtils.isNotBlank(barcode)) {
                DriverContainer container = driverContainerMapper.last(barcode);
                if (container == null) {
                    throw new BusinessException("还未装货");
                }
                containers = Lists.newArrayList(container);
            } else {
                containers = listByLoadKey(uKey);
            }
            if (StringUtils.isBlank(driverTrack.getLatitude()) || StringUtils.isBlank(driverTrack.getLongitude())) {
                AutoMapLocation loaction = autoMapService.coordinate(driverTrack.getReportLoaction());
                if (loaction != null) {
                    driverTrack.setLatitude(loaction.getLatitude());
                    driverTrack.setLongitude(loaction.getLongitude());
                }
            }
            if (StringUtils.isBlank(driverTrack.getLatitude()) || StringUtils.isBlank(driverTrack.getLongitude())) {
                throw new ParameterException(driverTrack.getReportLoaction(), "获取经纬度异常,稍后重试");
            }
            driverTrack.setUserId(uKey);
            driverTrack.setReportTime(new Date());
            driverTrack.setId(Globallys.nextKey());
            if (driverTrackMapper.insertSelective(driverTrack) > 0 && CollectionUtils.isNotEmpty(containers)) {
                Collection<TransitionTrack> transitionTracks = new ArrayList<TransitionTrack>();
                Collection<Barcode> barcodes = new ArrayList<Barcode>();
                for (DriverContainer container : containers) {
                    BarCodeFettle codeFettle = BarCodeFettle.convert(container.getBindStatus());
                    if (codeFettle.bind()) {
                        barcodes.add(barcodeService.loadBarcode(container.getBarcode()));
                    } else {
                        transitionTracks.add(new TransitionTrack(Globallys.nextKey(), container.getBarcode(), driverTrack.getId()));
                    }
                }
                if (CollectionUtils.isNotEmpty(barcodes) && CollectionUtils.isNotEmpty(observers)) {
                    for (DriverContainerObserverAdapter abstractObserver : observers) {
                        abstractObserver.notifyLocationReport(uKey, driverTrack, barcodes);
                    }
                }
                if (CollectionUtils.isNotEmpty(transitionTracks)) {
                    transitionTrackMapper.inserts(transitionTracks);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("loaction -> uKey:{} {}", uKey, driverTrack, e);
            throw BusinessException.dbException("上报位置信息异常");
        }
    }

    @Override
    public Collection<MergeDriverTrack> listDriverTrack(String barcode) throws ParameterException, BusinessException {
        Assert.notBlank(barcode, "条码号不能为空");
        try {
            Collection<DriverTrack> driverTracks = driverTrackMapper.selectByBarcode(barcode);
            if (CollectionUtils.isNotEmpty(driverTracks)) {
                Collection<MergeDriverTrack> collection = new ArrayList<MergeDriverTrack>(driverTracks.size());
                LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createUserCache(userMapper);
                for (DriverTrack driverTrack : driverTracks) {
                    MergeDriverTrack mergeDriverTrack = new MergeDriverTrack(driverTrack);
                    mergeDriverTrack.setReporter(associateUserCache.get(mergeDriverTrack.getUserId()));
                    collection.add(mergeDriverTrack);
                }
                return collection;
            }
            return Collections.emptyList();
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("listDriverTrack -> barcode:{}", barcode, e);
            throw BusinessException.dbException("查询定位信息异常");
        }
    }

    @Override
    public MergeDriverTrack lastDriverTrack(Integer uKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户ID不能为空");
        try {
            DriverTrack driverTrack = driverTrackMapper.selectLast(uKey);
            if (driverTrack != null) {
                MergeDriverTrack mergeDriverTrack = new MergeDriverTrack(driverTrack);
                mergeDriverTrack.setReporter(LocalCacheFactory.createUserCache(userMapper).get(mergeDriverTrack.getUserId()));
                return mergeDriverTrack;
            }
            return null;
        } catch (Exception e) {
            logger.error("lastDriverTrack -> uKey:{}", uKey, e);
            throw BusinessException.dbException("查询自己最后一次位置上报信息异常");
        }
    }

    /**
     * 更新订单
     *
     * @param uKey
     * @param order
     * @param eventType
     * @throws BusinessException
     */
    @Override
    public void modifyOrder(Integer uKey, Order order, OrderEventType eventType) throws BusinessException {
        if (order != null && StringUtils.isNotBlank(order.getBindCode()) && eventType.isReceive()) {
            //确认收货事件
            if((OrderEventType.RECEIVE == eventType || eventType == OrderEventType.ARRIVED) && OrderFettleType.convert(order.getFettle()).isComplete()){
                try {
                    //确认到货时卸货
                    unloadQuiet(uKey, order.getBindCode());
                } catch (Exception e) {
                    logger.error("订单确认收货时,卸货异常 -> uKey:{} orderKey:{} barcode:{}", uKey, order.getId(), order.getBindCode(), e);
                }
            }
            //装车事件
            if(OrderEventType.LOAD_DRIVER == eventType){
                saveOrderDeliver(uKey, order.getId(), new Date());
            }
        }
    }

    private void saveOrderDeliver(Integer uKey, Long oKey, Date time) {
        try {
            OrderDeliver deliver = new OrderDeliver();
            deliver.setOrderKey(oKey);
            deliver.setUserKey(uKey);
            if (orderDeliverMapper.selectCount(deliver) <= 0) {
                deliver.setId(Globallys.nextKey());
                deliver.setCreateTime(time == null ? new Date() : time);
                orderDeliverMapper.insertSelective(deliver);
            }
        } catch (Exception e) {
            logger.error("保存订单运输记录异常 uKey:{} oKey:{}", uKey, oKey);
        }
    }

    /**
     * 位置上报时记录
     *
     * @param locationEvent 定位事件
     * @param locationType  定位类型
     * @param locationTrack 定位轨迹
     * @throws BusinessException
     */
    @Override
    public void notifyLocationReport(LocationEvent locationEvent, LocationType locationType, LocationTrack locationTrack) throws BusinessException {
        if (!locationEvent.sync() && locationType.isOrder() && StringUtils.isNotBlank(locationTrack.getHostId())) {
            saveOrderDeliver(locationTrack.getUserId(), Long.parseLong(locationTrack.getHostId()), locationTrack.getCreateTime());
        }
    }

    /**
     * 根据条件分页查需订单运输记录
     *
     * @param search 查询条件(search.userKey 不可以为空)
     * @param scope  分页信息
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<OrderAlliance> pageOrderDeliver(OrderSearch search, PageScope scope) throws ParameterException, BusinessException {
        Assert.notBlank(search.getUserKey(), "用户编号不能为空");
        //根据查询条件联表查询(T_ORDER_DELIVER left join T_ORDER)，查除Order信息
        Page<Order> page = null;//分页查询 orderDeliverMapper
        if (StringUtils.isNotBlank(search.getLikeString())) {
            //根据货主名称模糊查询多个公司ID
            Collection<Long> companies = companyMapper.listCompanyKeyByName(search.getLikeString());
            if (CollectionUtils.isNotEmpty(companies)) {
                search.setCompanyKeys(companies);
            }
        }
        page = orderDeliverMapper.pageOrderDeliver(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        return new CustomPage<OrderAlliance>(page.getPageNum(), page.getPageSize(), page.getTotal(), orderService.alliance(search.getUserKey(), page, O.DEFAULT_LISTS));
    }

    /**
     * 根据条件查需订单运输记录
     *
     * @param search 查询条件(search.userKey 不可以为空)
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<OrderAlliance> listOrderDeliver(OrderSearch search) throws ParameterException, BusinessException {
        Assert.notBlank(search.getUserKey(), "用户编号不能为空");
        //根据查询条件联表查询(T_ORDER_DELIVER left join T_ORDER)，查除Order信息
        Page<Order> page = null;//分页查询 orderDeliverMapper
        if (StringUtils.isNotBlank(search.getLikeString())) {
            //根据货主名称模糊查询多个公司ID
            Collection<Long> companies = companyMapper.listCompanyKeyByName(search.getLikeString());
            if (CollectionUtils.isNotEmpty(companies)) {
                search.setCompanyKeys(companies);
            }
        }
        return orderService.alliance(search.getUserKey(), orderDeliverMapper.pageOrderDeliver(search), O.DEFAULT_LISTS);
    }
}
