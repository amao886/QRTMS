/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:09:51
 */
package com.ycg.ksh.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.common.barcode.PDFBuilder;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.MapCollection;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.lock.DistributedSynchronize;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.Validator;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.entity.common.constant.PermissionCode;
import com.ycg.ksh.entity.common.constant.ReceiptVerifyFettle;
import com.ycg.ksh.entity.common.constant.SourceType;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.DriverContainer;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.persistent.Leadtime;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.entity.persistent.WaybillDriverScan;
import com.ycg.ksh.entity.persistent.WaybillReceipt;
import com.ycg.ksh.entity.persistent.WaybillShare;
import com.ycg.ksh.entity.persistent.WaybillTrack;
import com.ycg.ksh.entity.service.MergeApplyRes;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.WaybillAssociate;
import com.ycg.ksh.entity.service.WaybillContext;
import com.ycg.ksh.entity.service.WaybillSerach;
import com.ycg.ksh.entity.service.WaybillSimple;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.entity.service.depot.DepotAlliance;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.api.CustomerService;
import com.ycg.ksh.service.api.PermissionService;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.WaybillService;
import com.ycg.ksh.service.api.WaybillTrackService;
import com.ycg.ksh.service.observer.BarcodeObserverAdapter;
import com.ycg.ksh.service.observer.DriverContainerObserverAdapter;
import com.ycg.ksh.service.observer.ReceiptObserverAdapter;
import com.ycg.ksh.service.observer.TrackObserverAdapter;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.persistence.CustomerMapper;
import com.ycg.ksh.service.persistence.GoodsMapper;
import com.ycg.ksh.service.persistence.LeadtimeMapper;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.persistence.WaybillDriverScanMapper;
import com.ycg.ksh.service.persistence.WaybillMapper;
import com.ycg.ksh.service.persistence.WaybillShareMapper;
import com.ycg.ksh.service.support.assist.WaybillUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 任务单相关业务逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:09:51
 */
@Service("ksh.core.service.waybillService")
public class WaybillServiceImpl implements WaybillService, ReceiptObserverAdapter, TrackObserverAdapter, BarcodeObserverAdapter, DriverContainerObserverAdapter {

    private static final String LOCK = "WAYBILL";
    private static final String CACHE_KEY = "WAYBILL#";

    @Resource
    DistributedSynchronize distributedSynchronize;
    @Resource
    CacheManager cacheManager;
    @Resource
    AutoMapService autoMapService;
    @Resource
    WaybillMapper waybillMapper;
    @Resource
    BarCodeService barCodeService;
    @Resource
    ProjectGroupService projectGroupService;
    @Resource
    CustomerService customerService;
    @Resource
    PermissionService permissionService;
    @Resource
    WaybillTrackService trackService;
    @Resource
    WaybillShareMapper shareMapper;
    @Resource
    WaybillDriverScanMapper driverScanMapper;
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    CustomerMapper customerMapper;
    @Resource
    LeadtimeMapper leadtimeMapper;
    @Resource
    SmsService smsService;

    @Autowired(required = false)
    Collection<WaybillObserverAdapter> observers;

    @PostConstruct
    public void initialize(){
        try {
            distributedSynchronize.initialize(LOCK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modifyWaybillContext(WaybillContext context) {
        if (context.fettleChange() && CollectionUtils.isNotEmpty(observers)) {
            context.setExecute(false);
            for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                waybillAbstractObserver.onWaybillFettleChange(context);
            }
        }
        if (context.getReceiptCount() != null && context.getReceiptCount() > 0) {
            context.setReceiptVerifyCount(Math.min(context.getReceiptVerifyCount(), context.getReceiptCount()));
            if (context.getReceiptCount() == 0) {
                context.setReceiptVerifyStatus(ReceiptVerifyFettle.UN_UPLOAD);// 未上传
            } else if (context.getReceiptVerifyCount() <= 0 && context.getReceiptCount() > 0) {
                context.setReceiptVerifyStatus(ReceiptVerifyFettle.WAIT);// 未审核
            } else if (context.getReceiptVerifyCount() > 0 && context.getReceiptVerifyCount() < context.getReceiptCount()) {
                context.setReceiptVerifyStatus(ReceiptVerifyFettle.ING);// 审核中
            } else {
                context.setReceiptVerifyStatus(ReceiptVerifyFettle.ALREADY);// 已审核
            }
        }
        if(WaybillUtils.validateRelatively(context)){
            context.setArrivaltime(WaybillUtils.arrivaltime(context));
        }
        context.setUpdatetime(new Date());
        waybillMapper.updateByPrimaryKeySelective(context.getUpdate());
        cacheManager.delete(CACHE_KEY + context.getId());
    }

    private boolean validate(String code) {
        if (waybillMapper.countByCode(code) > 0) {
            throw new BusinessException("条码编号[" + code + "]已经绑定不能重复绑定");
        }
        return true;
    }
    //手机端条码绑定
    @Override
    public Waybill saveBind(WaybillContext context) throws ParameterException, BusinessException {
        logger.debug("save bind================> context:{}", context);
        Assert.notNull(context.getUpdate(), "运单信息不能为空");
        Assert.notNull(context.getBarcode(), "条码编号不能为空");
        Assert.notNull(context.getDeliveryNumber(), "送货单号不能为空");
        Assert.notEmpty(context.getCustomers(), "收货信息/发货信息不能为空");
        try {
            if (!distributedSynchronize.lock(LOCK, context.getBarcode())) {
                throw new BusinessException("条码号[" + context.getBarcode() + "]正在绑定中");
            }
            if(validate(context.getBarcode())){
                WaybillUtils.initializeSomething(context.getUpdate(), Constant.WAYBILL_STATUS_BIND);
                context.setUserid(context.getUserKey());
                context.setBindTime(context.getCreatetime());
                if (CollectionUtils.isNotEmpty(observers)) {
                    for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                        waybillAbstractObserver.onInitializeWaybill(context);
                    }
                }
                //context.setArrivaltime(WaybillUtils.arrivaltime(context));
                if (waybillMapper.insertSelective(context.getUpdate()) > 0) {
                    if(CollectionUtils.isNotEmpty(context.getCommodities())){
                        for (Goods goods : context.getCommodities()) {
                            goods.setCreateTime(context.getUpdatetime());
                            goods.setUpdateTime(context.getUpdatetime());
                            goods.setWaybillid(context.getId());
                        }
                        goodsMapper.insertBatch(context.getCommodities());
                    }
                    if (CollectionUtils.isNotEmpty(observers)) {
                        context.setExecute(false);
                        for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                            waybillAbstractObserver.onCompleteWaybill(context, true);
                        }
                    }
                }
                modifyWaybillContext(context);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("save bind -> context:{}", context, e);
            throw BusinessException.dbException("绑定运单异常");
        } finally {
            distributedSynchronize.unlock(LOCK, context.getBarcode());
        }
        return context.getUpdate();
    }

    /*
     * @see com.ycg.ksh.service.api.WaybillService#saveSelectBind(java.lang.Integer, Waybill, java.lang.Integer, java.lang.Integer)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-21 10:48:41
     */
    @Override
    public Waybill saveSelectBind(WaybillContext context) throws ParameterException, BusinessException {
        logger.debug("save select bind================> context:{}", context);
        Assert.notNull(context.getUpdate(), "任务单为空");
        Assert.notNull(context.getId(), "任务单主键为空");
        Assert.notNull(context.getBarcode(), "条码编号不能为空");
        try {
            if (!distributedSynchronize.lock(LOCK, context.getBarcode())) {
                throw new BusinessException("条码号[" + context.getBarcode() + "]正在绑定中");
            }
            if(validate(context.getBarcode())){
                context.setPersistence(getWaybillById(context.getId()));
                if (context.getPersistence() == null) {
                    throw new BusinessException("指定运单信息不存在");
                }
                Assert.notBlank(context.getDeliveryNumber(), "送货单号不能为空");
                context.setUserid(context.getUserKey());
                context.setWaybillStatus(WaybillFettle.BOUND);
                context.setBindTime(new Date());
                if (CollectionUtils.isNotEmpty(observers)) {
                    for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                        waybillAbstractObserver.onInitializeWaybill(context);
                    }
                }
                //context.setArrivaltime(WaybillUtils.arrivaltime(context));
                if (CollectionUtils.isNotEmpty(observers)) {
                    context.setExecute(false);
                    for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                        waybillAbstractObserver.onCompleteWaybill(context, true);
                    }
                }
                modifyWaybillContext(context);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("save select bind================> context:{}", context, e);
            throw BusinessException.dbException("选择绑定运单异常");
        } finally {
            distributedSynchronize.unlock(LOCK, context.getBarcode());
        }
        return context.getUpdate();
    }
    /**
     * 判断电子围栏
     * <p>
     *
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 15:31:08
     */
    private boolean validateFence(Double longitude, Double latitude, String wlongitude, String wlatitude, Double radius) {
        if (StringUtils.isBlank(wlongitude) || StringUtils.isBlank(wlatitude)) {
            return false;
        }
        if (longitude == null || latitude == null) {
            return false;
        }
        Double wlng = new Double(wlongitude), wlat = new Double(wlatitude);
        Double gradius = autoMapService.distance(longitude, latitude, wlng, wlat);
        if (gradius != null && gradius <= radius) {// 开关状态为开时，且半径小于设置的半径则设置已送达状态
            return true;
        }
        return false;
    }
    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 13:15:08
     * @see com.ycg.ksh.service.api.WaybillService#insertShare(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public void insertShare(Integer userId, Integer shareId, Integer waybillId) throws ParameterException, BusinessException {
        Assert.notNull(userId, "用户编号不能为空");
        Assert.notNull(shareId, "分享人编号不能为空");
        Assert.notNull(waybillId, "任务编号不能为空");
        try {
            if (userId - shareId > 0 && driverScanMapper.selectByUserId(waybillId, userId) == null) {// 还不是我的任务
                Waybill waybill = getWaybillById(waybillId);
                if (waybill == null) {
                    throw new BusinessException("任务单数据不存在!!!");
                }
                WaybillShare waybillShare = new WaybillShare();
                waybillShare.setAcceptid(shareId);
                waybillShare.setWaybillid(waybillId);
                WaybillShare share = shareMapper.selectOne(waybillShare);
                if (share != null) {// 是二次分享
                    waybillShare.setParamid(share.getParamid());
                } else {
                    waybillShare.setParamid(shareId);
                }
                if (projectGroupService.isMember(waybill.getGroupid(), waybillShare.getParamid())) {
                    waybillShare.setJurisdiction(1);
                } else {
                    waybillShare.setJurisdiction(2);
                }
                waybillShare.setCreatetime(new Date());
                waybillShare.setAcceptid(userId);
                waybillShare.setShareid(shareId);
                shareMapper.insertSelective(waybillShare);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("recordShare -> userId:{} shareId:{} waybill:{}", userId, shareId, waybillId, e);
            throw BusinessException.dbException("记录分享信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 09:57:42
     * @see com.ycg.ksh.service.api.WaybillService#update(WaybillContext)
     * <p>
     */
    @Override
    public void update(WaybillContext context) throws ParameterException, BusinessException {
        logger.debug("update -> {} ", context);
        Assert.notNull(context.getUpdate(), "运单信息不能为空");
        Assert.notNull(context.getId(), "运单ID不能为空");
        WaybillUtils.completeSomething(context);
        Assert.notEmpty(context.getCommodities(), "至少填写一项货物信息");
        try {
            context.setPersistence(getWaybillById(context.getId()));
            if (context.getPersistence() == null) {
                throw new BusinessException("指定运单信息不存在");
            }
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setExecute(false);
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onInitializeWaybill(context);
                }
            }
            context.setUpdatetime(new Date());
            //context.setArrivaltime(WaybillUtils.arrivaltime(context));
            if(CollectionUtils.isNotEmpty(context.getCommodities())){
                for (Goods goods : context.getCommodities()) {
                    if (goods.getId() != null) {
                        goods.setUpdateTime(context.getUpdatetime());
                        goodsMapper.updateByPrimaryKey(goods);
                    } else {
                        goods.setCreateTime(context.getUpdatetime());
                        goods.setUpdateTime(context.getUpdatetime());
                        goods.setWaybillid(context.getId());
                        goodsMapper.insertSelective(goods);
                    }
                }
            }
            //根据货物ID删除
            if (CollectionUtils.isNotEmpty(context.getDeleteCommodityKeys())) {
                Example example = new Example(Goods.class);
                example.createCriteria().andIn("id", context.getDeleteCommodityKeys());
                goodsMapper.deleteByExample(example);
            }
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setExecute(false);
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onCompleteWaybill(context, false);
                }
            }
            modifyWaybillContext(context);
            //要求到货时间为空的情况下，设置空值
            if (context.getArrivaltime() == null) {
                waybillMapper.updateArrivalTime(context.getId());
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("update -> context:{}", context, e);
            throw BusinessException.dbException("更新运单异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 13:03:53
     * @see com.ycg.ksh.service.api.WaybillService#save(WaybillContext)
     * <p>
     */
    @Override
    public Waybill save(WaybillContext context) throws ParameterException, BusinessException {
        logger.debug("save -> context:{}", context);
        Assert.notNull(context.getUpdate(), "运单信息不能为空");
        WaybillUtils.completeSomething(context.getUpdate(), context.getCommodities());
        Assert.notEmpty(context.getCommodities(), "至少填写一项货物信息");
        try {
            WaybillUtils.initializeSomething(context.getUpdate(), Constant.WAYBILL_STATUS_WAIT);
            context.setUserid(context.getUserKey());
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setExecute(false);
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onInitializeWaybill(context);
                }
            }
            //context.setArrivaltime(WaybillUtils.arrivaltime(context));
            if (waybillMapper.insertSelective(context.getUpdate()) > 0) {
                for (Goods goods : context.getCommodities()) {
                    goods.setCreateTime(context.getUpdatetime());
                    goods.setUpdateTime(context.getUpdatetime());
                    goods.setWaybillid(context.getId());
                }
                goodsMapper.insertBatch(context.getCommodities());
                if (CollectionUtils.isNotEmpty(observers)) {
                    context.setExecute(false);
                    for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                        waybillAbstractObserver.onCompleteWaybill(context, false);
                    }
                }
                modifyWaybillContext(context);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("save -> waybillContext:{}", context, e);
            throw BusinessException.dbException("新增任务单异常");
        }
        return context.getUpdate();
    }

    @Override
    public void delete(Integer uKey,  Collection<Integer> deleteWaybillKeys) throws ParameterException, BusinessException {
        Assert.notEmpty(deleteWaybillKeys, "至少选择一项要删除的数据");
        Assert.notBlank(uKey, "操作人不能为空");
        for (Integer waybillKey : deleteWaybillKeys) {
            Waybill waybill = getWaybillById(waybillKey);
            if (waybill != null) {
                if (!WaybillFettle.convert(waybill.getWaybillStatus()).unbind()) {
                    throw new BusinessException("任务单不是未绑定状态不能删除");
                }
                if(CollectionUtils.isNotEmpty(observers)){
                    for (WaybillObserverAdapter observer : observers) {
                        observer.onDeleteWaybill(uKey, waybill);
                    }
                }
                //删除任务单的货物信息
                goodsMapper.deleteByWaybillId(waybill.getId());
                //删除任务单
                waybillMapper.deleteByPrimaryKey(waybill.getId());
                //从缓存中移除
                cacheManager.delete(CACHE_KEY + waybill.getId());
                if(StringUtils.isNotBlank(waybill.getBarcode())){
                    cacheManager.delete(CACHE_KEY + waybill.getBarcode());
                }
            }
        }
    }

    @Override
    public void confirmReceive(Integer userId, Collection<Integer> waybillKeys, Integer way) throws ParameterException, BusinessException {
        Assert.notBlank(userId, "操作人用户编号不能为空");
        Assert.notEmpty(waybillKeys, "至少选择一条任务单数据");
        try {
            for (Integer waybillKey : waybillKeys) {
                Waybill waybill = getWaybillById(waybillKey);
                if (waybill == null) {
                    throw new ParameterException(waybillKey, "指定任务单不存在");
                }
                //判断用户是否有操作权限(确定到货，权限修改)
                /*if (!permissionService.validateByWaybillID(waybillKey, userId, PermissionCode.CONFIRM_RECEIVE)) {
                    throw new BusinessException("没有确认到货的权限");
                }*/
                WaybillFettle waybillFettle = WaybillFettle.convert(waybill.getWaybillStatus());
                if (waybillFettle.unbind()) {
                    throw new BusinessException("未绑定状态不能确认收货!!!");
                }
                if (!waybillFettle.receive()) {
                    WaybillContext context = WaybillContext.buildContext(userId, waybill);
                    context.setWaybillStatus(WaybillFettle.RECEIVE);
                    context.setConfirmDeliveryWay(way);
                    if (context.getActualArrivalTime() == null) {
                        context.setActualArrivalTime(new Date());
                    }
                    modifyWaybillContext(context);
                    //确定到货发送短信
                    String sendContent = String.format(Constant.SMS_SIGN_STRING,context.getReceiverName(),context.getDeliveryNumber());
                    String m = context.getContactPhone();
                    if(Validator.isMobile((m))) {
                    	smsService.sendmsg(m, sendContent);
                    }
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("confirmReceive -> uKey:{} waybillKeys:{} way:{}", userId, waybillKeys, way, e);
            throw BusinessException.dbException("确认到货异常");
        }
    }

    /**
     * @param userId
     * @param waybillKeys
     * @param way
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void confirmArrive(Integer userId, Collection<Integer> waybillKeys, Integer way) throws ParameterException, BusinessException {
        Assert.notBlank(userId, "操作人用户编号不能为空");
        Assert.notEmpty(waybillKeys, "至少选择一条任务单数据");
        try {
            for (Integer waybillKey : waybillKeys) {
                Waybill waybill = getWaybillById(waybillKey);
                if (waybill == null) {
                    throw new ParameterException(waybillKey, "指定任务单不存在");
                }
                //判断用户是否有操作权限
                if (!permissionService.validateByWaybillID(waybillKey, userId, PermissionCode.CONFIRM_SEND)) {
                    throw new BusinessException("没有确认送达的权限");
                }
                WaybillFettle waybillFettle = WaybillFettle.convert(waybill.getWaybillStatus());
                if (waybillFettle.unbind()) {
                    throw new BusinessException("未绑定状态不能确认送达!!!");
                }
                if (waybillFettle.receive()) {
                    throw new BusinessException("收货状态不能确认送达!!!");
                }
                if (!waybillFettle.arrive()) {
                    WaybillContext context = WaybillContext.buildContext(userId, waybill);
                    context.setWaybillStatus(WaybillFettle.ARRIVE);
                    context.setConfirmDeliveryWay(way);
                    if (context.getActualArrivalTime() == null) {
                        context.setActualArrivalTime(new Date());
                    }
                    modifyWaybillContext(context);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("confirmArrive -> uKey:{} waybillKeys:{} way:{}", userId, waybillKeys, way, e);
            throw BusinessException.dbException("确认送达异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 09:25:04
     * @see com.ycg.ksh.service.api.WaybillService#getWaybillByCode(java.lang.String)
     * <p>
     */
    @Override
    public Waybill getWaybillByCode(String barcodeString) throws ParameterException, BusinessException {
        Assert.notNull(barcodeString, "条码编号不能为空");
        try {
            String cacheKey = CACHE_KEY + barcodeString;
            Waybill waybill = null;
            Object cacheObject = cacheManager.get(cacheKey);
            if (cacheObject == null) {
                waybill = waybillMapper.selectByCode(barcodeString);
                cacheManager.set(cacheKey, waybill, 30L, TimeUnit.SECONDS);
            } else {
                waybill = (Waybill) cacheObject;
            }
            return waybill;
        } catch (Exception e) {
            logger.error("getWaybillByCode -> barcodeString:{}", barcodeString, e);
            throw BusinessException.dbException("查询异常");
        }
    }

    /**
     * @param waybillId 运单编号
     * @return 对应编号的运单信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:09:51
     * @see com.ycg.ksh.service.api.WaybillService#getWaybillById(java.lang.Integer)
     * <p>
     */
    @Override
    public Waybill getWaybillById(Integer waybillId) throws ParameterException, BusinessException {
        Assert.notNull(waybillId, "运单ID不能为空");
        try {
            String cacheKey = CACHE_KEY + waybillId;
            Waybill waybill = null;
            Object cacheObject = cacheManager.get(cacheKey);
            if (cacheObject == null) {
                waybill = waybillMapper.selectByPrimaryKey(waybillId);
                cacheManager.set(cacheKey, waybill, 30L, TimeUnit.SECONDS);
            } else {
                waybill = (Waybill) cacheObject;
            }
            return waybill;
        } catch (Exception e) {
            logger.error("getWaybillById -> waybillId:{}", waybillId, e);
            throw BusinessException.dbException("查询异常");
        }
    }

    /**
     * @param uKey  操作人
     * @param wKey  任务单编号
     * @param associate  关联信息
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public MergeWaybill getWaybillById(Integer uKey, Integer wKey, WaybillAssociate associate) throws ParameterException, BusinessException {
        Assert.notNull(wKey, "运单ID不能为空");
        Assert.notNull(uKey, "操作人用户编号不能为空");
        try {
            SourceType sourceType = null;
            Waybill waybill = getWaybillById(wKey);
            if (waybill != null) {
                MergeWaybill mergeWaybill = mergeWaybill(waybill, associate);
                if (StringUtils.isNotBlank(waybill.getBarcode())) {
                    BarcodeContext vbarcode = barCodeService.validateNotDecrypt(uKey, waybill.getBarcode());
                    if(vbarcode instanceof GroupCodeContext) {
                        GroupCodeContext codeContext = (GroupCodeContext) vbarcode;
                        mergeWaybill.setIsGroupMermer(codeContext.isAllowBind());
                        sourceType = codeContext.getSourceType();
                    }else{
                        throw new BusinessException("企业二维码");
                    }
                } else {
                    if (waybill.getGroupid() != null && waybill.getGroupid() > 0) {
                        sourceType = SourceType.ONESELF;
                    } else {
                        sourceType = SourceType.GROUP;
                    }
                }
                if(sourceType != null){
                    mergeWaybill.setHaveType(sourceType.getCode());
                }
                return mergeWaybill;
            }
        } catch (Exception e) {
            logger.error("getWaybillById -> wKey:{} uKey:{} associate:{}", wKey, uKey, associate, e);
            throw BusinessException.dbException("查询异常");
        }
        throw new BusinessException("指定任务单不存在");
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 18:20:08
     * @see com.ycg.ksh.service.api.WaybillService#listBindWaybill(java.lang.Integer)
     * <p>
     */
    @Override
    public Collection<Waybill> listBindWaybill(Integer uKey) throws ParameterException, BusinessException {
        Example example = new Example(Waybill.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userid", uKey);
        criteria.andNotEqualTo("waybillStatus", Constant.WAYBILL_STATUS_WAIT);
        Calendar dateTime = Calendar.getInstance();
        dateTime.add(Calendar.HOUR_OF_DAY, -24);
        criteria.andGreaterThanOrEqualTo("bindTime", dateTime.getTime());
        example.orderBy("bindTime").desc();
        return waybillMapper.selectByExample(example);
    }

    @Override
    public Collection<Waybill> listWaybill(WaybillSerach serach) throws ParameterException, BusinessException {
        return waybillMapper.listBySomething(serach);
    }

    /**
     * @param context
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 11:10:56
     * @see com.ycg.ksh.service.api.WaybillService#pageMergeWaybill(WaybillContext)
     * <p>
     */
    @Override
    public CustomPage<MergeWaybill> pageMergeWaybill(WaybillContext context) throws ParameterException, BusinessException {
        PageScope scope = context.getPageScope();
        if (scope == null) {
            scope = PageScope.DEFAULT;
        }
        Page<Waybill> page = null;
        if (context.getAssociate().isAssociateConveyance()){
            page = waybillMapper.listBySomethingConveyance(context.getSearch(), new RowBounds(scope.getPageNum(), scope.getPageSize()));
        }else{
            page = waybillMapper.listBySomething(context.getSearch(), new RowBounds(scope.getPageNum(), scope.getPageSize()));
        }
        return new CustomPage<MergeWaybill>(page.getPageNum(), page.getPageSize(), page.getTotal(), mergeWaybill(page, context.getAssociate()));
    }

    /**
     * 任务单部分数据查询
     *
     * @param uKey
     * @param serach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<WaybillSimple> pageWaybillSimple(Integer uKey, WaybillSerach serach, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        Page<Waybill> page = waybillMapper.listBySomethingConveyance(serach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        Collection<WaybillSimple> simpleCollection = new ArrayList<WaybillSimple>(page.size());
        for (Waybill waybill : page) {
            WaybillSimple simple = new WaybillSimple(waybill.getId(), waybill.getGroupid(), waybill.getDeliveryNumber(), waybill.getBarcode(), waybill.getWaybillStatus());
            simple.setReceiveName(waybill.getReceiverName());
            simple.setReceiveAddress(RegionUtils.replace(waybill.getEndStation()) +  waybill.getReceiveAddress());
            if(waybill.getWaybillStatus() < WaybillFettle.ARRIVE.getCode()){
                if(waybill.getGroupid() != null && waybill.getGroupid() > 0){
                    simple.setAllowEdit(projectGroupService.isMember(waybill.getGroupid(), uKey));
                }else{
                    simple.setAllowEdit(waybill.getUserid() - uKey == 0);
                }
            }else{
                simple.setAllowEdit(false);
            }
            simpleCollection.add(simple);
        }
        return new CustomPage<WaybillSimple>(page.getPageNum(), page.getPageSize(), page.getTotal(), simpleCollection);
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-04 08:19:52
     * @see com.ycg.ksh.service.api.WaybillService#pageDailyWaybill(WaybillContext)
     * <p>
     */
    @Override
    public CustomPage<MergeWaybill> pageDailyWaybill(WaybillContext context) throws ParameterException, BusinessException {
        PageScope scope = context.getPageScope();
        if (scope == null) {
            scope = PageScope.DEFAULT;
        }
        WaybillSerach serach = context.getSearch();
        Assert.notBlank(serach.getGroupId(), "查询每日统计详情项目组必填");
        ProjectGroup projectGroup = projectGroupService.getByGroupKey(serach.getGroupId());
        if (projectGroup != null) {
            int start = projectGroup.getStartHour(), end = projectGroup.getEndHour();
            Calendar calendar = Calendar.getInstance();
            if (serach.getFirstTime() != null && start > 0) {
                calendar.setTime(serach.getFirstTime());
                calendar.add(Calendar.HOUR_OF_DAY, start);
                serach.setFirstTime(calendar.getTime());
            }
            if (serach.getSecondTime() != null && end > 0) {
                calendar.setTime(serach.getSecondTime());
                calendar.add(Calendar.HOUR_OF_DAY, end);
                serach.setSecondTime(calendar.getTime());
            }
        }
        Page<Waybill> page = null;
        if (context.getAssociate().isAssociateConveyance()){
            page = waybillMapper.listBySomethingConveyance(context.getSearch(), new RowBounds(scope.getPageNum(), scope.getPageSize()));
        }else{
            page = waybillMapper.listTotalBySomething(context.getSearch(), new RowBounds(scope.getPageNum(), scope.getPageSize()));
        }
        return new CustomPage<MergeWaybill>(page.getPageNum(), page.getPageSize(), page.getTotal(), mergeWaybill(page, context.getAssociate()));
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 17:06:24
     * @see com.ycg.ksh.service.api.WaybillService#pageWaybill(WaybillContext)
     * <p>
     */
    @Override
    public CustomPage<Waybill> pageWaybill(WaybillContext context) throws ParameterException, BusinessException {
        PageScope scope = context.getPageScope();
        if (scope == null) {
            scope = PageScope.DEFAULT;
        }
        Page<Waybill> page = null;
        if (context.getAssociate().isAssociateConveyance()){
            page = waybillMapper.listBySomethingConveyance(context.getSearch(), new RowBounds(scope.getPageNum(), scope.getPageSize()));
        }else{
            page = waybillMapper.listBySomething(context.getSearch(), new RowBounds(scope.getPageNum(), scope.getPageSize()));
        }
        return new CustomPage<Waybill>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    /**
     * @param context
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 10:58:07
     * @see com.ycg.ksh.service.api.WaybillService#listMergeWaybill(WaybillContext)
     * <p>
     */
    @Override
    public Collection<MergeWaybill> listMergeWaybill(WaybillContext context) throws ParameterException, BusinessException {
        WaybillAssociate associate = context.getAssociate();
        if (associate.isAssociateConveyance()){
            return mergeWaybill(waybillMapper.listBySomethingConveyance(context.getSearch()), associate);
        }else{
            return mergeWaybill(waybillMapper.listBySomething(context.getSearch()), associate);
        }
    }

    private Collection<MergeWaybill> mergeWaybill(Collection<? extends Waybill> results, WaybillAssociate associate) {
        Collection<MergeWaybill> collection = new ArrayList<MergeWaybill>(results.size());
        for (Waybill waybill : results) {
            try {
                collection.add(mergeWaybill(waybill, associate));
            } catch (Exception e) {
            }
        }
        return collection;
    }

    private MergeWaybill mergeWaybill(Waybill waybill, WaybillAssociate associate) {
        try {
            MergeWaybill mergeWaybill = null;
            if (MergeWaybill.class.isInstance(waybill)) {
                mergeWaybill = (MergeWaybill) waybill;
            } else {
                mergeWaybill = new MergeWaybill(waybill);
            }
            if (CollectionUtils.isNotEmpty(observers)) {
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onMergeWaybill(mergeWaybill, associate);
                }
            }
            Integer[] relativelys = WaybillUtils.relativelys(waybill.getCreatetime(), waybill.getBindTime(), waybill.getDeliveryTime(), waybill.getArrivaltime());
            mergeWaybill.setArriveDay(relativelys[0]);
            mergeWaybill.setArriveHour(relativelys[1]);
            if (associate.isAssociateCommodity()) {//货物信息列表
                mergeWaybill.setGoods(goodsMapper.select(new Goods(waybill.getId())));
            }
            mergeWaybill.setTransportDays(WaybillUtils.transportDays(waybill));
            return mergeWaybill;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 我的任务查询分页
     *
     * @param waybillContext
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<MergeWaybill> queryMyTaskPage(WaybillContext waybillContext) throws ParameterException, BusinessException {
        PageScope pageScope = waybillContext.getPageScope();
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        WaybillSerach serach = waybillContext.getSearch();
        Page<MergeWaybill> page = waybillMapper.queryMyTaskPage(serach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<MergeWaybill>(page.getPageNum(), page.getPageSize(), page.getTotal(), mergeWaybill(page, waybillContext.getAssociate()));
    }

    @Override
    public Collection<Goods> listGoodsById(Integer waybillId) throws ParameterException, BusinessException {
        Example example = new Example(Goods.class);
        example.createCriteria().andEqualTo("waybillid", waybillId);
        return goodsMapper.selectByExample(example);
    }

    @Override
    public void saveWcWaybill(WaybillContext context) throws ParameterException, BusinessException {
        logger.debug("saveWcWaybill -> context:{}", context);
        Assert.notEmpty(context.getCustomers(), "收货信息/发货信息不能为空");
        try {
            WaybillUtils.initializeSomething(context.getUpdate(), Constant.WAYBILL_STATUS_WAIT);
            context.setUserid(context.getUserKey());
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setExecute(false);
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onInitializeWaybill(context);
                }
            }
            WaybillUtils.completeSomething(context);
            if (waybillMapper.insertSelective(context.getUpdate()) > 0) {
                if(CollectionUtils.isNotEmpty(context.getCommodities())){
                    for (Goods goods : context.getCommodities()) {
                        goods.setCreateTime(context.getUpdatetime());
                        goods.setUpdateTime(context.getUpdatetime());
                        goods.setWaybillid(context.getId());
                    }
                    goodsMapper.insertBatch(context.getCommodities());
                }
                if (CollectionUtils.isNotEmpty(observers)) {
                    context.setExecute(false);
                    for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                        waybillAbstractObserver.onCompleteWaybill(context, false);
                    }
                }
                modifyWaybillContext(context);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveWcWaybill -> context:{}", context, e);
            throw BusinessException.dbException("关联客户信息异常");
        }
    }

    @Override
    public void updateWcWaybill(WaybillContext context) throws ParameterException, BusinessException {
        logger.info("updateWcWaybill -> {}", context);
        Assert.notNull(context.getUpdate(), "任务单信息为空");
        Assert.notBlank(context.getId(), "任务单编号为空");
        try {
            context.setPersistence(getWaybillById(context.getId()));
            if (context.getPersistence() == null) {
                throw new BusinessException("指定的任务单不存在");
            }
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setExecute(false);
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onInitializeWaybill(context);
                }
            }
            context.setUpdatetime(new Date());
            WaybillUtils.completeSomething(context);
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setExecute(false);
                for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                    waybillAbstractObserver.onCompleteWaybill(context, false);
                }
            }
            modifyWaybillContext(context);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("updateWcWaybill -> {}", context, e);
            throw BusinessException.dbException("updateWcWaybill异常");
        }
    }

    @Override
    public void updateDelayStatus() throws ParameterException, BusinessException {
        Collection<Integer> delaies = new ArrayList<Integer>(2);
        delaies.add(Constant.WAYBILL_DAY_WAIT);
        delaies.add(Constant.WAYBILL_DAY_NOTYET);
        Example example = new Example(Waybill.class);
        example.createCriteria().andIn("delay", delaies).andGreaterThan("waybillStatus", Constant.WAYBILL_STATUS_WAIT);
        Collection<Waybill> waybills = waybillMapper.selectByExample(example);
        if (null != waybills && waybills.size() > 0) {
            Long checkTime = System.currentTimeMillis(), ytime = null, dtime = null;
            MapCollection<Integer, Integer> mapCollection = new MapCollection<Integer, Integer>(4);
            for (Waybill waybill : waybills) {
                //未绑定运单不处理
                if (waybill.getWaybillStatus() != 10) {
                    ytime = (null != waybill.getArrivaltime() ? waybill.getArrivaltime().getTime() : null);
                    dtime = (null != waybill.getActualArrivalTime() ? waybill.getActualArrivalTime().getTime() : null);
                    // 延迟情况，当前时间大于要求到货 并且实际到货时间 Null
                    Integer status = null;
                    if (null != ytime) {
                        if (checkTime > ytime) {
                            if (null == dtime) {// 当前时间大于等于要求到货时间并且还没有送到-----------------------------------未到已超时
                                status = Constant.WAYBILL_DAY_NOTYET;
                            } else {
                                if (dtime <= ytime) {// 当前时间大于等于要求到货时间并且实际送达时间小于等于要求到货时间--------------已送到未超时
                                    status = Constant.WAYBILL_DAY_NOT_DELAYED;
                                } else {// 当前时间大于等于要求到货时间并且实际送达时间大于要求到货时间------------------------------已送到并超时
                                    status = Constant.WAYBILL_DAY_DELAYED;
                                }
                            }
                        } else {// 未超时
                            if (null != dtime) {// 当前时间小于要求到货时间并且已经送到----------------------------------------已送到未超时
                                status = Constant.WAYBILL_DAY_NOT_DELAYED;
                            }
                        }
                    } else {// 没有要求到货时间的-------------------------------------------------------------------------未超时
                        status = Constant.WAYBILL_DAY_NOT_DELAYED;
                    }
                    if (status != null && status != waybill.getDelay()) {
                        mapCollection.push(status, waybill.getId());
                    }
                }
            }
            for (Map.Entry<Integer, List<Integer>> entry : mapCollection.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                waybillMapper.batchUpdateDelayStatus(entry.getKey(), entry.getValue());
                logger.info("更新运单延迟状态 -> 状态:{} 更新 {} 单", entry.getKey(), entry.getValue().size());
            }
        }
    }


    @Override
    public void notifyUploadReceipt(WaybillContext context, WaybillReceipt receipt, Integer count) throws BusinessException {
        logger.info("收到回单上传通知 -> {} {} count:{}", context, receipt, count);
        try {
            if (count != null && count > 0) {
                if (context.getReceiptCount() == null) {
                    context.setReceiptCount(count);
                } else {
                    context.setReceiptCount(context.getReceiptCount() + count);
                }
                WaybillFettle waybillFettle = context.getWaybillStatus();
                if (!waybillFettle.unbind() && !waybillFettle.cancel()) {
                    //context.setWaybillStatus(WaybillFettle.ARRIVE);//2019-9-23 业务待确认
                    context.setConfirmDeliveryWay(Constant.CONFIRM_DELIVERY_WAY_RECEIPT);
                }
                if (context.getActualArrivalTime() == null) {
                    context.setActualArrivalTime(new Date());
                }
                if (context.isExecute()) {
                    modifyWaybillContext(context);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("notifyUploadReceipt -> {} {} count:{}", context, receipt, count, e);
            throw BusinessException.dbException("通知更新回单信息异常");
        }
    }

    @Override
    public void notifyVerifyReceipt(WaybillContext context, Integer status, Integer preteritStatus, Integer count) throws BusinessException {
        logger.info("收到回单审核通知 -> {} status:{} preteritStatus:{}", context, status, preteritStatus);
        try {
            if (status != null && status >= 0 && preteritStatus != status && count != null && count > 0) {
                if (preteritStatus == null || preteritStatus < 0) {
                    if (context.getReceiptVerifyCount() == null) {
                        context.setReceiptVerifyCount(count);
                    } else {
                        context.setReceiptVerifyCount(context.getReceiptVerifyCount() + count);
                    }
                } else {
                    if (Constant.VERIFYSTRTUS_REJECT - preteritStatus == 0) {//如果之前是不合格的
                        if (context.getReceiptUnqualifyCount() == null) {
                            context.setReceiptUnqualifyCount(0);
                        } else {
                            context.setReceiptUnqualifyCount(Math.max(0, context.getReceiptUnqualifyCount() - count));
                        }
                    }
                }
                if (Constant.VERIFYSTRTUS_REJECT - status == 0) {//本次审核不合格
                    if (context.getReceiptUnqualifyCount() == null) {
                        context.setReceiptUnqualifyCount(count);
                    } else {
                        context.setReceiptUnqualifyCount(context.getReceiptUnqualifyCount() + count);
                    }
                }
                if (context.isExecute()) {
                    modifyWaybillContext(context);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("notifyVerifyReceipt -> {} status:{} preteritStatus:{}", context, status, preteritStatus, e);
            throw BusinessException.dbException("更新回单审核数异常");
        }
    }

    @Override
    public void notifyLocationReport(WaybillContext context, WaybillTrack track, boolean driverScan) throws ParameterException, BusinessException {
        logger.info("收到运单轨迹上报通知 -> {} {}", context, track);
        try {
            if (context.getPositionCount() == null) {
                context.setPositionCount(1);
            } else {
                context.setPositionCount(context.getPositionCount() + 1);
            }
            Date currentTime = track.getCreatetime();
            context.setAddress(track.getLocations());
            context.setLoactionTime(currentTime);//最新位置上报时间
            //计算最新定位地址和目的地之间的距离
            if(StringUtils.isNotBlank(context.getLongitude()) && StringUtils.isNotBlank(context.getLatitude())) {//如果目的经纬度为空从数据库查询,
            	Double dis = autoMapService.distance(track.getLongitude(), track.getLatitude(), context.getLongitude(), context.getLatitude());
            	context.setPreDistance(context.getDistance()==null?new BigDecimal(0):context.getDistance());
            	context.setDistance(BigDecimal.valueOf(dis));
            	logger.info("distance========>:{}",dis);
            }
            
            if (context.getDeliveryTime() == null && context.getPositionCount() > 1) {
                context.setDeliveryTime(StringUtils.isNotBlank(context.getLoadTime()) ? DateUtils.parseToDate(context.getLoadTime()) : currentTime);
                context.setArrivaltime(WaybillUtils.arrivaltime(context));
            }
            WaybillFettle waybillFettle = context.getWaybillStatus();
            if(driverScan){
            	logger.info("TrackObserverAdapter=========测试定位driverScan=========={}, waybillFettle{}",driverScan,waybillFettle);
                if(waybillFettle.bind()){
                    context.setWaybillStatus(WaybillFettle.ING);
                    logger.info("setWaybillStatus=========测试定位1111111==========");
                }
                if (waybillFettle.ing() || waybillFettle.bind()) {
                    if (context.getFenceStatus() != null && Constant.GRATESATUS_ON - context.getFenceStatus() == 0) {
                        if (validateFence(track.getLongitude(), track.getLatitude(), context.getLongitude(), context.getLatitude(), context.getFenceRadius())) {
                            logger.info("触发电子围栏 -> uKey:{} wKey:{} location:{} lng:{} lat:{}", context.getLongitude(), context.getLatitude(), track.getLocations(), track.getLongitude(), track.getLatitude());
                            context.setWaybillStatus(WaybillFettle.ARRIVE);
                            context.setConfirmDeliveryWay(Constant.CONFIRM_DELIVERY_WAY_FENCE);
                            if (context.getActualArrivalTime() == null) {
                                context.setActualArrivalTime(currentTime);
                            }
                        }
                    }
                }
            }
            if (context.isExecute()) {
                waybillMapper.updateByPrimaryKeySelective(context.getUpdate());
                logger.info("<===setWaybillStatus=========update=========={}",context.getUpdate());
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("notifyLocationReport -> {} {}", context, track, e);
            throw BusinessException.dbException("更新定位次数异常");
        }
    }

    @Override
    public void notifyBarcodeValidate(Barcode barcode, BarcodeContext context) {
        logger.info("收到运单装车通知 -> {} {}", barcode, context);
        if(context instanceof GroupCodeContext) {
            GroupCodeContext codeContext = (GroupCodeContext) context;
            Waybill waybill = getWaybillByCode(barcode.getBarcode());
            if (waybill != null) {
                codeContext.setWaybillId(waybill.getId());
                codeContext.setStatus(WaybillFettle.convert(waybill.getWaybillStatus()));
                if (codeContext.getSourceType() != null && !codeContext.getSourceType().group()) {
                    if (waybill.getUserid() - context.getUserKey() == 0) {
                        codeContext.setSourceType(SourceType.ONESELF);
                        context.setAllowBind(true);
                    }
                }
                if (codeContext.getSourceType() == null) {
                    if (driverScanMapper.selectCount(new WaybillDriverScan(waybill.getId(), context.getUserKey())) > 0) {
                        codeContext.setSourceType(SourceType.SCAN);
                    } else {
                        if (shareMapper.selectCount(new WaybillShare(context.getUserKey(), waybill.getId())) > 0) {
                            codeContext.setSourceType(SourceType.SHARE);
                        }
                    }
                }
            }
            context.setWaitBindCount(waybillMapper.countWaitBind(codeContext.getOwnerKey(), codeContext.getGroupId()));
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
    	logger.info("收到运单装车通知2 -> {} {}", container, context);
    	if (context != null && context instanceof GroupCodeContext && context.fettle().bind()) {
            Waybill waybill = getWaybillByCode(context.getBarcode());
            container.setReceiverName(waybill.getReceiverName());
            container.setContactName(waybill.getContactName());
            container.setContactNumber(waybill.getContactPhone());
            container.setDeliveryNumber(waybill.getDeliveryNumber());
            container.setReceiveAddress(waybill.getReceiveAddress());
            container.setLatitude(waybill.getLatitude());
            container.setLongitude(waybill.getLongitude());
            if(waybill.getDeliveryTime() == null){
                Waybill update = new Waybill();
                update.setId(waybill.getId());
                update.setDeliveryTime(container.getLoadTime());
                if(StringUtils.isNotBlank(waybill.getLoadTime())) {
                	update.setDeliveryTime(DateUtils.parseToDate(waybill.getLoadTime()));//更新发货时间
            	}
                update.setArrivaltime(WaybillUtils.arrivaltime(update.getDeliveryTime(), waybill));
                waybillMapper.updateByPrimaryKeySelective(update);
            }
        }
    }

    private void coordinate(Waybill waybill, boolean exception) {
        String address = waybill.getEndStation() + waybill.getReceiveAddress();
        if (StringUtils.isNotBlank(address)) {
            try {
                AutoMapLocation location = autoMapService.coordinate(address);
                if (location != null) {
                    waybill.setLatitude(location.getLatitude());
                    waybill.setLongitude(location.getLongitude());
                }
            } catch (Exception e) {
                logger.error("获取客户地址经纬度异常 {} {}", waybill, e);
            }
        }
        if (exception) {
            if (waybill.getLatitude() == null || waybill.getLongitude() == null) {
                throw new BusinessException("经纬度获取异常,请检查收货地址!!!");
            }
        }
    }

    /**
     * 批量保存任务单
     *
     * @param collection
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saves(Integer uKey, Integer gKey, Customer customer, Collection<MergeWaybill> collection) throws ParameterException, BusinessException {
        for (MergeWaybill mergeWaybill : collection) {
    		Waybill waybill = WaybillUtils.initializeSomething(mergeWaybill, Constant.WAYBILL_STATUS_WAIT);
    		waybill.setUserid(uKey);
    		waybill.setGroupid(gKey);
    		Integer aDay = null, aHour = null;
    		if (mergeWaybill.getArriveDay() != null) {
    			aDay = Integer.valueOf(mergeWaybill.getArriveDay());
    		}
    		if (mergeWaybill.getArriveHour() != null) {
    			aHour = Integer.valueOf(mergeWaybill.getArriveHour());
    		}
    		if(customer != null){
    			waybill.setShipperAddress(customer.getAddress());
    			waybill.setShipperContactName(customer.getContacts());
    			waybill.setShipperContactTel(customer.getContactNumber());
    			waybill.setShipperName(customer.getCompanyName());
    			waybill.setShipperTel(customer.getTel());
    			waybill.setStartStation(RegionUtils.merge(customer.getProvince(), customer.getCity(), customer.getDistrict()));
    			waybill.setSimpleStartStation(RegionUtils.simple(customer.getProvince(), customer.getCity(), customer.getDistrict()));
    		}
    		coordinate(waybill, mergeWaybill.getShipperAddress(), mergeWaybill.getReceiveAddress());//处理坐标
    		WaybillUtils.completeSomething(waybill, mergeWaybill.getGoods());
    		waybill.setArrivaltime(WaybillUtils.arrivaltime(waybill, aDay, aHour));
    		if (waybillMapper.insertSelective(mergeWaybill) > 0) {
    			if (CollectionUtils.isNotEmpty(mergeWaybill.getGoods())) {
    				for (Goods goods : mergeWaybill.getGoods()) {
    					goods.setWaybillid(waybill.getId());
    					goods.setCreateTime(waybill.getCreatetime());
    					goods.setUpdateTime(waybill.getCreatetime());
    				}
    				goodsMapper.insertBatch(mergeWaybill.getGoods());
    			}
    			WaybillContext context = WaybillContext.buildContext(uKey, waybill);
    			if (CollectionUtils.isNotEmpty(observers)) {
    				context.setExecute(false);
    				for (WaybillObserverAdapter waybillAbstractObserver : observers) {
    					waybillAbstractObserver.onCompleteWaybill(context, false);
    				}
    			}
    			modifyWaybillContext(context);
    		}
    	}
    }

    private void coordinate(Waybill waybill, String sendAddress, String reciveAddress){
        if (StringUtils.isNotBlank(sendAddress) && StringUtils.isBlank(waybill.getStartStation())) {
            try {
                AutoMapLocation location = autoMapService.coordinate(sendAddress);
                if(location != null){
                    waybill.setStartStation(RegionUtils.merge(location.getProvince(), location.getCity(), location.getDistrict()));
                    waybill.setSimpleStartStation(RegionUtils.simple(location.getProvince(), location.getCity(), location.getDistrict()));
                    waybill.setShipperAddress(sendAddress);
                }
            } catch (Exception e) {
                logger.error("获取发货人地址异常 {} {}", waybill, sendAddress, e);
            }
        }
        if (StringUtils.isNotBlank(reciveAddress)) {
            try {
                AutoMapLocation location = autoMapService.coordinate(reciveAddress);
                if(location != null){
                    waybill.setEndStation(RegionUtils.merge(location.getProvince(), location.getCity(), location.getDistrict()));
                    waybill.setSimpleEndStation(RegionUtils.simple(location.getProvince(), location.getCity(), location.getDistrict()));
                    waybill.setReceiveAddress(reciveAddress);
                    waybill.setLatitude(location.getLatitude());
                    waybill.setLongitude(location.getLongitude());
                }
            } catch (Exception e) {
                logger.error("获取收货货人地址异常 {} {}", waybill, reciveAddress, e);
            }
        }
    }
    @Override
    public MergeWaybill getWaybillById(Integer id, WaybillAssociate associate) throws ParameterException, BusinessException {
        Assert.notBlank(id, "任务单主键为空");
        try {
            return mergeWaybill(getWaybillById(id), associate);
        } catch (Exception e) {
            logger.debug("queryById error:{}", e);
            throw BusinessException.dbException(Constant.QUERY_FAIL);
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 13:44:57
     * @see com.ycg.ksh.service.api.WaybillService#updateFenceStatus(java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public Integer updateFenceStatus(Integer waybillId, Integer status) throws ParameterException, BusinessException {
        Assert.notNull(waybillId, "运单编号不能为空");
        Waybill exister = getWaybillById(waybillId);
        if (exister == null) {
            throw new ParameterException(waybillId, "运单信息不存在!!!");
        }
        if (StringUtils.isBlank(exister.getReceiveAddress())) {
            throw new BusinessException("收货地址异常不能开启电子围栏");
        }
        Waybill waybill = new Waybill(waybillId);
        if (StringUtils.isBlank(exister.getLatitude()) || StringUtils.isBlank(exister.getLongitude())) {
            coordinate(exister, false);
            waybill.setLatitude(exister.getLatitude());
            waybill.setLongitude(exister.getLongitude());
            if (StringUtils.isBlank(waybill.getLatitude()) || StringUtils.isBlank(waybill.getLongitude())) {
                throw new BusinessException("收货地址错误不能开启电子围栏");
            }
        }
        if (exister.getFenceRadius() == null || exister.getFenceRadius() <= 0) {
            waybill.setFenceRadius(5d);//电子围栏半径默认5公里
        }
        if (status == null) {//没有传状态，就自动切换
            if (exister.getFenceStatus() == null || exister.getFenceStatus() != Constant.GRATESATUS_ON) {
                status = Constant.GRATESATUS_ON;
            } else {
                status = Constant.GRATESATUS_OFF;
            }
        } else {
            if (status != Constant.GRATESATUS_ON) {
                status = Constant.GRATESATUS_OFF;
            } else {
                status = Constant.GRATESATUS_ON;
            }
        }
        waybill.setFenceStatus(status);
        waybillMapper.updateByPrimaryKeySelective(waybill);
        return status;
    }

    @Override
    public void updateWaybillStatusById(Waybill waybill) {
        waybillMapper.updateWaybillStatusById(waybill);
    }

    /**
     * 打印查询
     *
     * @param userKey
     * @param waybillKeys
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<MergeWaybill> listPrint(Integer userKey, Collection<Integer> waybillKeys, Integer count ,Integer gKey) throws ParameterException, BusinessException {
        Example example = new Example(Waybill.class);
        example.createCriteria().andIn("id", waybillKeys);
        Collection<Waybill> collection = buildBarcode(userKey, waybillMapper.selectByExample(example), gKey);
        if(CollectionUtils.isNotEmpty(collection)){
            Collection<MergeWaybill> mergeWaybills = new ArrayList<MergeWaybill>(collection.size());
            try {
                int number = 1;
                for (Waybill waybill : collection) {
                    Collection<Goods> goods = goodsMapper.select(new Goods(waybill.getId()));
                    while (CollectionUtils.isNotEmpty(goods)){
                        MergeWaybill mergeWaybill = new MergeWaybill(waybill);
                        int index = 0;
                        for (Iterator<Goods> iterator = goods.iterator(); iterator.hasNext(); ) {
                            if(index++ >= count){ break; }
                            mergeWaybill.addGoods(iterator.next());
                            iterator.remove();
                        }
                        mergeWaybill.setCodeString(com.ycg.ksh.common.system.SystemUtils.buildQRcodeContext(mergeWaybill.getBarcode()));
                        mergeWaybill.setIndex(number++);
                        mergeWaybills.add(mergeWaybill);
                    }
                }
            } catch (Exception e) {
                logger.error("list print {}", waybillKeys, e);
            }
            return mergeWaybills;
        }
        return Collections.emptyList();
    }

    @Override
    public void updateGoods(WaybillContext waybillContext) throws ParameterException, BusinessException {
        Assert.notEmpty(waybillContext.getCommodities(),"至少填写一项货物信息");
        WaybillUtils.completeSomething(waybillContext);
        waybillContext.setUpdatetime(new Date());
        waybillContext.setPersistence(getWaybillById(waybillContext.getId()));
        if(null == waybillContext.getPersistence()){
            throw  new BusinessException("任务单不存在");
        }
        if(CollectionUtils.isNotEmpty(waybillContext.getCommodities())){
            for (Goods goods : waybillContext.getCommodities()) {
                if (goods.getId() != null) {
                    goods.setUpdateTime(waybillContext.getUpdatetime());
                    goodsMapper.updateByPrimaryKey(goods);
                } else {
                    goods.setCreateTime(waybillContext.getUpdatetime());
                    goods.setUpdateTime(waybillContext.getUpdatetime());
                    goods.setWaybillid(waybillContext.getId());
                    goodsMapper.insertSelective(goods);
                }
            }
        }
       /* //根据货物ID删除
        if (CollectionUtils.isNotEmpty(waybillContext.getDeleteCommodityKeys())) {
            Example example = new Example(Goods.class);
            example.createCriteria().andIn("id", waybillContext.getDeleteCommodityKeys());
            goodsMapper.deleteByExample(example);
        }*/
        if (CollectionUtils.isNotEmpty(observers)) {
            waybillContext.setExecute(false);
            for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                waybillAbstractObserver.onCompleteWaybill(waybillContext, false);
            }
        }
        modifyWaybillContext(waybillContext);
    }

    @Override
    public void deleteGoods(WaybillContext waybillContext) throws ParameterException, BusinessException {
        waybillContext.setPersistence(getWaybillById(waybillContext.getId()));
        if(null == waybillContext.getPersistence()){
            throw  new BusinessException("任务单不存在");
        }
        if (CollectionUtils.isNotEmpty(waybillContext.getDeleteCommodityKeys())) {
            Example example = new Example(Goods.class);
            example.createCriteria().andIn("id", waybillContext.getDeleteCommodityKeys());
            goodsMapper.deleteByExample(example);
        }
        if (CollectionUtils.isNotEmpty(observers)) {
            waybillContext.setExecute(false);
            for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                waybillAbstractObserver.onCompleteWaybill(waybillContext, false);
            }
        }
        modifyWaybillContext(waybillContext);
    }

    private Collection<Waybill> buildBarcode(Integer userKey, Collection<Waybill> waybills,Integer groupId) throws ParameterException, BusinessException {
        if(CollectionUtils.isNotEmpty(waybills)){
            //StringBuilder builder = new StringBuilder();
            Date ctime = new Date();
            MergeApplyRes mergeApplyRes = barCodeService.queryTotalCountByGroupId(groupId);
            List<Barcode> barcodeList = barCodeService.queryOneBarcodeByGroupId(groupId);
            if(mergeApplyRes.getAvailableTotal()==0 || mergeApplyRes.getAvailableTotal() < waybills.size())
            	throw new BusinessException("[该项目组剩可用条码数量不足，请申请后再操作！]");
            int i = 0;
            for (Waybill waybill : waybills) {
                if (StringUtils.isBlank(waybill.getBarcode())){
                    //String waybillKey = String.valueOf(waybill.getId());
                    //int count = Math.max(0, 10 - waybillKey.length());
                    //生成二维码并绑定
                    //builder.append("0").append(waybillKey).append(RandomUtils.string(count));//打印条码生产需要修改
                	Barcode barcode = barcodeList.get(i);
                    if(barcode == null) throw new BusinessException("[该项目组剩可用条码数量不足，请申请后再操作！]");
                    String barcodeStr = barcode.getBarcode();
                    WaybillContext context = WaybillContext.buildContext(userKey, waybill);
                    context.setBarcode(barcodeStr);
                    context.setWaybillStatus(WaybillFettle.BOUND);
                    context.setBindTime(ctime);
                    logger.info("==========buildBarcode=========>barcode:{}",barcode.getBarcode());
                    context.setDeliveryTime(StringUtils.isNotBlank(waybill.getLoadTime()) ? DateUtils.parseToDate(waybill.getLoadTime()) : ctime);
                    //builder.setLength(0);
                    //barCodeService.save(context.getUserid(), context.getGroupid(), context.getBarcode());
                    barCodeService.updateStatusById(barcode);
                    if (CollectionUtils.isNotEmpty(observers)) {
                        context.setExecute(false);
                        for (WaybillObserverAdapter waybillAbstractObserver : observers) {
                            waybillAbstractObserver.onCompleteWaybill(context, true);
                        }
                    }
                    modifyWaybillContext(context);
                    waybill.setBarcode(context.getBarcode());
                    i++;
                }
            }
        }
        return waybills;
    }

	@Override
	public MergeWaybill getWaybillByCode(String barcodeString, WaybillAssociate associate)
			throws ParameterException, BusinessException {
		Assert.notBlank(barcodeString, "条码号为空");
		try {
			return mergeWaybill(getWaybillByCode(barcodeString), associate);
		} catch (Exception e) {
			logger.debug("getWaybillByCode error:{}", e);
			throw BusinessException.dbException(Constant.QUERY_FAIL);
		}
	}

	@Override
	public FileEntity listExportWaybill(JSONObject req) throws ParameterException, BusinessException {
		Assert.notBlank(req.getString("deliverStartTime"),"导出条件开始时间不能为空");
		Assert.notBlank(req.getString("deliverEndTime"),"导出条件结束时间不能为空");
        ExcelWriter easyExcel = null;
        try {
            if(DateUtils.daysBetween(req.getString("deliverStartTime"), req.getString("deliverEndTime"))>31) {
                throw new BusinessException("最多只能导出一个月数据");
            }
            Collection<Waybill> depotAlliances = waybillMapper.listExportWabills(req);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setSuffix(FileUtils.XLSX_SUFFIX);
            fileEntity.setDirectory(SystemUtils.directoryDownload());
            fileEntity.setFileName(FileUtils.appendSuffix("" + System.nanoTime(), FileUtils.XLSX_SUFFIX));
            File destFile = FileUtils.newFile(fileEntity.getDirectory(), fileEntity.getFileName());
            easyExcel = EasyExcelBuilder.createWriteExcel(destFile);
            easyExcel.createSheet("跟踪表明细");
            easyExcel.columnWidth(120,150, 100, 100,350, 150, 30, 30, 60, 120, 120, 350, 30, 30);
            easyExcel.header("提货日期","经销商简称","始发城市", "目的城市","配送地址",  "送货单号", "数量", "体积", "车型", "预计到达","实际到达日期","当前位置","距离目的地剩余（km）","上一次距离目的地剩余（km）");
            for (Waybill waybill : depotAlliances) {
            	List<Goods> goods = goodsMapper.select(new Goods(waybill.getId()));
            	String goodName = null;
            	if(CollectionUtils.isNotEmpty(goods)) {
            		goodName = goods.get(0).getGoodsName();
            	}
                easyExcel.row(StringUtils.isNotBlank(waybill.getLoadTime()) ? DateUtils.dataformat(waybill.getLoadTime(),"yyyy-MM-dd") : "",waybill.getReceiverName(),StringUtils.isNotBlank(waybill.getSimpleStartStation())?waybill.getSimpleStartStation():"",waybill.getSimpleEndStation(),
                        StringUtils.isNotBlank(waybill.getReceiveAddress())? waybill.getReceiveAddress():"",StringUtils.isNotBlank(waybill.getLoadNo())? waybill.getLoadNo():"",StringUtils.isNotBlank(waybill.getDeliveryNumber()) ? waybill.getDeliveryNumber() :"",waybill.getNumber(),waybill.getVolume()==null?0:waybill.getVolume(),
                        StringUtils.isNotBlank(waybill.getCarType()) ? waybill.getCarType() : "",waybill.getArrivaltime() != null ? DateUtils.formatDate(waybill.getArrivaltime()) : "",waybill.getActualArrivalTime()!= null ? DateUtils.formatDate(waybill.getActualArrivalTime()):"",StringUtils.isNotBlank(waybill.getAddress()) ? waybill.getAddress() : "", 
                                waybill.getDistance()!=null?waybill.getDistance():"",waybill.getPreDistance()!=null?waybill.getPreDistance():"",goodName);
            }
            easyExcel.write();
            fileEntity.setPath(destFile.getPath());
            fileEntity.setCount(1);
            fileEntity.setSize(FileUtils.size(destFile.length(), FileUtils.ONE_MB));
            return fileEntity;
        } catch(ParameterException | BusinessException e) {
        	throw e;
        }catch (Exception e) {
            logger.error("任务单导出异常 {} ", req);
            throw new BusinessException("任务单导出异常", e);
        } finally {
            if (easyExcel != null) {
                try {
                    easyExcel.close();
                } catch (Exception e) {
                }
            }
        }
	}

	@Override
	public Collection<Waybill> listunBindWaybill(JSONObject json) throws ParameterException, BusinessException {
		try {
			Example example = new Example(Waybill.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("userid", json.getLong("userid"));
			criteria.andEqualTo("waybillStatus", Constant.WAYBILL_STATUS_WAIT);
			if(json  != null) {
				Long groupId = json.getLong("groupId");
				if(groupId !=null && groupId > 0) {
					criteria.andEqualTo("groupid", groupId);
				}
				if(json.getString("waybillIds")!=null) {
					logger.info("waybillIds================{}",json.getString("waybillIds"));
					criteria.andIn("id", StringUtils.integerCollection(json.getString("waybillIds")));
				}
				if(StringUtils.isNotBlank(json.getString("deliverStartTime"))) {
					criteria.andGreaterThanOrEqualTo("createtime", json.getString("deliverStartTime"));
				}
				if(StringUtils.isNotBlank(json.getString("deliverEndTime"))) {
					criteria.andLessThanOrEqualTo("createtime", json.getString("deliverEndTime"));
				}
			}
			example.orderBy("createtime").desc();
			return waybillMapper.selectByExample(example);
		} catch (Exception e) {
			throw new BusinessException("查询未绑定信息异常");
		}
        
	}

	@Override
	public void batchBind(Integer userKey,Collection<Waybill> con,Integer groupId) throws ParameterException, BusinessException {
		Assert.notNull(con, "绑定列表不能为空");
		Assert.notBlank(groupId, "群编号不能为空");
		try {
			buildBarcode(userKey, con, groupId);
		} catch (ParameterException | BusinessException e) {
			throw e;
		}catch (Exception e) {
			throw new BusinessException("批量绑定异常");
		}
	}

	@Override
	public FileEntity buildPDF(RequestObject object) throws BusinessException, ParameterException {
		Integer gKey = object.getInteger("groupId");
		Assert.notNull(gKey, "项目编号不能为空");
		Page<Waybill> waybills = null;
		int total = 0, pageNum = 1;
		String suffix = "pdf";
		try {
			WaybillSerach serach = object.toJavaBean(WaybillSerach.class);
			serach.setWaybillKeys(StringUtils.integerCollection(object.get("waybillIds")));
			logger.info("buildPDF====================>serach:{}",serach);
			serach.setWaybillFettles(new Integer[]{20});
			File directory = new File(SystemUtils.directoryTemp(suffix + gKey));
			if (directory.exists()) {
				FileUtils.deleteDirectory(directory);
			}
			directory = FileUtils.directory(directory);
			do {
				waybills = waybillMapper.listBySomething(serach,new RowBounds(pageNum++, 1000));
				if(CollectionUtils.isNotEmpty(waybills)) {
					PDFBuilder builder = new PDFBuilder(FileUtils.file(directory, FileUtils.appendSuffix(System.currentTimeMillis()+"", suffix)));
					builder.ready();
					for (Waybill waybill : waybills) {
						builder.insert(waybill.getBarcode(), waybill.getDeliveryNumber(), SystemUtils.buildQRcodeContext(waybill.getBarcode()));
					}
					total += waybills.size();
					builder.close();
				}else {
                    if (pageNum == 1) {
                    	throw new BusinessException("未找到已绑定的任务单");
                    }
                }
			}while(total < waybills.getTotal());
			File[] files = directory.listFiles();
            if (files != null) {
                FileEntity entity = new FileEntity();
                if (files.length > 1) {
                    File zipFile = FileUtils.zip(directory, new File(SystemUtils.directoryDownload()));
                    if (zipFile != null) {
                        entity.setCount(files.length);
                        entity.setDirectory(zipFile.getParent());
                        entity.setFileName(zipFile.getName());
                        entity.setSize(FileUtils.size(zipFile.length(), FileUtils.ONE_MB));
                    }
                } else {
                    File file = FileUtils.copyFileToDirectory(files[0], new File(SystemUtils.directoryDownload()), true);
                    if (file != null) {
                        entity.setCount(files.length);
                        entity.setDirectory(file.getParent());
                        entity.setFileName(file.getName());
                        entity.setSize(FileUtils.size(file.length(), FileUtils.ONE_MB));
                    }
                }
                FileUtils.deleteDirectory(directory);
                return entity;
            }
		} catch (BusinessException | ParameterException e) {
			throw e;
		}catch (Exception e) {
			logger.error("PDF文件生成异常：object：{},e:{}",object,e);
			throw new BusinessException("PDF文件生成异常,稍后再尝试!!!");
		}
		return null;
	}

    /**
     * @see com.ycg.ksh.service.api.WaybillService#batchUpdateArrivaltime(com.ycg.ksh.common.entity.RequestObject)
     */
    @Override
    public void batchUpdateArrivaltime(RequestObject object) throws BusinessException, ParameterException {
        String waybillKeyString  = object.get("waybillIds");
        Date arrivaltime = object.getDate("arrivaltime");
        Assert.notNull(waybillKeyString, "运单编号不能为空");
        Assert.notNull(arrivaltime, "要求到货时间不能为空");
        try {
            Collection<Integer> collection = StringUtils.integerCollection(waybillKeyString);
            waybillMapper.batchUpdateArrivaltime(arrivaltime, collection);
        } catch (Exception e) {
            logger.error("batchUpdateArrivaltime()========object:{},e:{}",object,e);
            throw BusinessException.dbException(Constant.FAIL_MSG);
        }
    }
    
	@Override
	public Waybill getWaybillByDeliveryNumber(String deliveryNumber) throws ParameterException, BusinessException {
		 Assert.notNull(deliveryNumber, "条码编号不能为空");
	        try {
	            String cacheKey = CACHE_KEY + deliveryNumber;
	            Waybill waybill = null;
	            Object cacheObject = cacheManager.get(cacheKey);
	            if (cacheObject == null) {
	                waybill = waybillMapper.selectByDeliveryNumber(deliveryNumber);
	                cacheManager.set(cacheKey, waybill, 30L, TimeUnit.SECONDS);
	            } else {
	                waybill = (Waybill) cacheObject;
	            }
	            return waybill;
	        } catch (Exception e) {
	            logger.error("getWaybillByDeliveryNumber -> deliveryNumber:{}", deliveryNumber, e);
	            throw BusinessException.dbException("根据送货单号查询运单信息异常");
	        }
	}
}
