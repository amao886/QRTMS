/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:09:51
 */
package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.ycg.ksh.adapter.api.ESignService;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.system.FrontUtils;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SecurityTokenUtil;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.constant.*;
import com.ycg.ksh.entity.adapter.esign.Contract;
import com.ycg.ksh.entity.adapter.esign.ContractSigner;
import com.ycg.ksh.entity.adapter.esign.Signer;
import com.ycg.ksh.entity.adapter.wechat.TemplateDataValue;
import com.ycg.ksh.entity.adapter.wechat.TemplateMesssage;
import com.ycg.ksh.entity.adapter.wechat.TemplateType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderUtil;
import com.ycg.ksh.entity.service.esign.ReceiptSignature;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.observer.AdventiveObserverAdapter;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.observer.ReceiptObserverAdapter;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import com.ycg.ksh.service.support.pdf.PDFFactory;
import com.ycg.ksh.service.support.pdf.SealObject;
import com.ycg.ksh.service.util.O;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 回单相关业务逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:09:51
 */
@Service("ksh.core.service.receiptService")
public class ReceiptServiceImpl implements ReceiptService, WaybillObserverAdapter, OrderObserverAdapter, AdventiveObserverAdapter {

    private final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    private static final String SIGN_CODE = "SC#%d#%d";

    //操作状态
    private static final String[] STATUSARRAYS = new String[]{"未回收", "已回收", "已送客户", "已退供应商", "客户退回"};

    //操作类型
    private static final String[] INFEASIBLE_STATUSARRAYS = new String[]{"未回收", "回单回收", "送交客户", "退供应商", "客户退回"};


    @Resource
    private CacheManager cacheManager;
    @Resource
    private MessageQueueService queueService;
    @Resource
    private WeChatApiService apiService;
    @Resource
    private ImageStorageService imageStorageService;
    @Resource
    private BarCodeService barcodeService;
    @Resource
    private CompanyService companyService;
    @Resource
    private UserService userService;
    @Resource
    private OrderService orderService;
    @Resource
    private ESignService esignService;
    @Resource
    private SupportService supportService;
    @Resource
    private SmsService smsService;


    @Resource
    private UserMapper userMapper;
    @Resource
    private OrderSignatureMapper signatureMapper;
    @Resource
    private OrderReceiptMapper orderReceiptMapper;
    @Resource
    private WaybillReceiptStatusMapper waybillReceiptStatusMapper;
    @Resource
    private ReceiptScanBatchMapper receiptScanBatchMapper;
    @Resource
    private WaybillMapper waybillMapper;
    @Resource
    private ProjectGroupMemberMapper projectGroupMemberMapper;
    @Resource
    private PaperReceiptMapper paperReceiptMapper;
    @Resource
    private WaybillService waybillService;
    @Resource
    private PaperyReceiptMapper paperyReceiptMapper;
    @Resource
    private WaybillReceiptMapper receiptMapper;
    @Resource
    private TransitionReceiptMapper transitionReceiptMapper;
    @Resource
    private ProjectGroupMapper projectGroupMapper;
    @Resource
    private WaybillReceiptViewMapper waybillReceiptViewMapper;
    @Resource
    private ImageInfoMapper imageInfoMapper;

    @Autowired(required = false)
    private Collection<ReceiptObserverAdapter> observers;

    @Override
    public Collection<MergeTransitionReceipt> listTransitionReceipt(String barcode) throws ParameterException, BusinessException {
        if (StringUtils.isNotBlank(barcode)) {
            try {
                Example receiptExample = new Example(TransitionReceipt.class);
                Criteria criteria = receiptExample.createCriteria();
                criteria.andEqualTo("barcode", barcode);
                receiptExample.orderBy("reportTime").asc();
                List<TransitionReceipt> transitionReceipts = transitionReceiptMapper.selectByExample(receiptExample);
                if (CollectionUtils.isNotEmpty(transitionReceipts)) {
                    Collection<MergeTransitionReceipt> collection = new ArrayList<MergeTransitionReceipt>(transitionReceipts.size());
                    LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createUserCache(userMapper);
                    for (TransitionReceipt transitionReceipt : transitionReceipts) {
                        MergeTransitionReceipt mergeTransitionReceipt = new MergeTransitionReceipt(transitionReceipt);
                        mergeTransitionReceipt.setReporter(associateUserCache.get(mergeTransitionReceipt.getUserId()));
                        mergeTransitionReceipt.setImages(imageStorageService.list(Constant.IMAGE_TYPE_TRANSITION_RECEIPT, mergeTransitionReceipt.getId()));

                        collection.add(mergeTransitionReceipt);
                    }
                    return collection;
                }
            } catch (BusinessException | ParameterException e) {
                throw e;
            } catch (Exception e) {
                logger.error("listTransitionReceipt -> barcode:{}", barcode, e);
                throw BusinessException.dbException("查询临时回单信息异常");
            }
        }
        return Collections.emptyList();
    }

    /**
     * @param sendCycle
     * @param type
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 16:41:34
     * @see com.ycg.ksh.service.api.ReceiptService#exportPaperyReceipt(PaperyReceipt,
     * TimeCycle,
     * TimeCycle, java.lang.Integer)
     * <p>
     */
    @Override
    public FileEntity exportPaperyReceipt(PaperyReceipt receipt, TimeCycle sendCycle, TimeCycle bindCycle, Integer type) throws ParameterException, BusinessException {
        ExcelWriter easyExcel = null;
        try {
            List<PaperyReceipt> collection = null;
            if (1 == type) {// 查询库存回单
                collection = paperyReceiptMapper.listInventoryReceipt(receipt, sendCycle, bindCycle);
            } else {
                collection = paperyReceiptMapper.listPaperyReceipt(receipt, sendCycle, bindCycle);
            }
            FileEntity fileEntity = new FileEntity();
            fileEntity.setSuffix(FileUtils.XLSX_SUFFIX);
            fileEntity.setDirectory(SystemUtils.directoryDownload());
            fileEntity.setFileName(FileUtils.appendSuffix(receipt.getUserId() + "_" + System.nanoTime(), FileUtils.XLSX_SUFFIX));
            File destFile = FileUtils.newFile(fileEntity.getDirectory(), fileEntity.getFileName());
            easyExcel = EasyExcelBuilder.createWriteExcel(destFile);
            easyExcel.createSheet((1 == type) ? "库存回单" : "送交客户");
            easyExcel.columnWidth(30, 25, 30, 30, 10, 20);
            easyExcel.header("任务单绑定日期", "送货单号", "地区", "收货客户名称", "件数", "重量", "体积", "状态");
            for (PaperyReceipt rcModel : collection) {
                easyExcel.row(rcModel.getCreatetime(), rcModel.getDeliveryNumber(), rcModel.getAddress(), rcModel.getCompanyName(), rcModel.getNumber(), rcModel.getWeight(), rcModel.getVolume(), STATUSARRAYS[rcModel.getReceiptStatus()]);
            }
            easyExcel.write();
            logger.debug("export file -> {}", destFile);
            fileEntity.setPath(destFile.getPath());
            fileEntity.setCount(1);
            fileEntity.setSize(FileUtils.size(destFile.length(), FileUtils.ONE_MB));
            return fileEntity;
        } catch (Exception e) {
            logger.error("纸质回单导出文件异常 {} {} {} {}", receipt, sendCycle, bindCycle, type, e);
            throw new BusinessException("导出回单文件异常");
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
    public void transitionReceipt(Integer uKey, String code, Collection<String> collection, boolean fromWx) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户ID不能为空");
        Assert.notNull(code, "条码信息不能为空");
        Assert.notEmpty(collection, "回单图片信息不能为空");
        try {
            Barcode barcode = barcodeService.getBarcode(code);
            if (barcode == null) {
                throw new ParameterException(code, "无效的条码号");
            }
            TransitionReceipt receipt = new TransitionReceipt();
            receipt.setId(Globallys.nextKey());
            receipt.setBarcode(barcode.getBarcode());
            receipt.setUserId(uKey);
            receipt.setReportTime(new Date());
            if (transitionReceiptMapper.insertSelective(receipt) > 0 && CollectionUtils.isNotEmpty(collection)) {
                if (fromWx) {
                    collection = apiService.downImages(collection);
                }
                if (CollectionUtils.isNotEmpty(collection = apiService.downImages(collection))) {
                    imageStorageService.save(Constant.IMAGE_TYPE_TRANSITION_RECEIPT, receipt.getId(), collection);
                    if (CollectionUtils.isNotEmpty(observers)) {
                        // 通知回单上传观察者
                        for (ReceiptObserverAdapter abstractObserver : observers) {
                            abstractObserver.notifyTransitionReceipt(receipt, collection.size());
                        }
                    }
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveReceipt -> uKey:{} barcode:{} paths:{}", uKey, code, collection, e);
            throw BusinessException.dbException("回单信息保存异常");
        }
    }

    /**
     * @param user
     * @param waybillId
     * @param collection
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveReceipt(User user, Integer waybillId, Collection<String> collection, boolean fromWx) throws ParameterException, BusinessException {
        Assert.notEmpty(collection, "回单图片地址不能为空");
        try {
            Waybill waybill = waybillService.getWaybillById(waybillId);
            if (waybill == null) {
                throw new ParameterException(waybillId, "指定任务单不存在");
            }
            WaybillReceipt receipt = new WaybillReceipt(user.getId(), user.getUname(), waybillId);
            if (receiptMapper.insert(receipt) > 0) {
                if (fromWx) {
                    collection = apiService.downImages(collection);
                }
                if (CollectionUtils.isNotEmpty(collection)) {
                    Collection<ImageInfo> imageInfos = new ArrayList<ImageInfo>(collection.size());
                    for (String path : collection) {
                        imageInfos.add(new ImageInfo(path, receipt.getCreatetime(), receipt.getId()));
                    }
                    saveReceipt(WaybillContext.buildContext(user, waybill), receipt, imageInfos, true);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("回单信息保存异常 {} {} {}", user.getId(), waybillId, collection, e);
            throw BusinessException.dbException("回单信息保存异常");
        }
    }

    @Override
    public void savePaperReceipt(PaperReceipt receipt, Collection<String> images, boolean fromWx) throws ParameterException, BusinessException {
        Assert.notEmpty(images, "回单图片地址不能为空");
        Assert.notNull(receipt, "回单信息不能为空");
        Assert.notBlank(receipt.getOrderKey(), "订单编号不能为空");
        try {
            Order order = orderService.getOrderByKey(receipt.getOrderKey());
            if (order == null) {
                throw new ParameterException(order, "指定订单不存在");
            }
            receipt.setId(Globallys.nextKey());
            receipt.setCreateTime(new Date());
            if (fromWx) {
                images = apiService.downImages(images);
            }
            if (CollectionUtils.isNotEmpty(images)) {
                receipt.setReceiptCount(images.size());
                List<ImageStorage> imageStorageList = new ArrayList<ImageStorage>(images.size());
                for (String path : images) {
                    imageStorageList.add(new ImageStorage(Constant.IMAGE_TYPE_RECEIPT, receipt.getId(), path));
                }
                saveStorage(imageStorageList, receipt);
            }
            paperReceiptMapper.insertSelective(receipt);

        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("上传回单异常  {} {} {} {} {}", receipt, images, fromWx, Constant.IMAGE_TYPE_RECEIPT);
            throw BusinessException.dbException("上传回单异常");
        }
    }


    private void saveStorage(List<ImageStorage> images, PaperReceipt paperReceipt) throws ParameterException, BusinessException {
        if (null != images && images.size() > 0) {
            imageStorageService.save(images);
            if (CollectionUtils.isNotEmpty(observers)) {
                // 回单上传观察者
                for (ReceiptObserverAdapter abstractObserver : observers) {
                    abstractObserver.notifyUploadReceipt(paperReceipt);
                }
            }
        }
    }


    private void saveReceipt(WaybillContext waybillContext, WaybillReceipt waybillReceipt, Collection<ImageInfo> imageInfos, boolean persistent) throws ParameterException, BusinessException {
        if (CollectionUtils.isNotEmpty(imageInfos)) {
            imageInfoMapper.inserts(imageInfos);
            if (CollectionUtils.isNotEmpty(observers)) {
                waybillContext.setExecute(persistent);
                // 通知回单上传观察者
                for (ReceiptObserverAdapter abstractObserver : observers) {
                    abstractObserver.notifyUploadReceipt(waybillContext, waybillReceipt, imageInfos.size());
                }
            }
        }
    }


    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 17:41:34
     * @see com.ycg.ksh.service.api.ReceiptService#modifyVerify(java.lang.Integer,
     * ImageInfo)
     * <p>
     */
    @Override
    public void modifyVerify(Integer waybillId, ImageInfo imageInfo) throws ParameterException, BusinessException {
        Assert.notNull(imageInfo, "回单审核信息不能为空");
        Assert.notNull(imageInfo.getId(), "审核编号不能为空");
        try {
            Waybill waybill = waybillService.getWaybillById(waybillId);
            if (waybill == null) {
                throw new ParameterException(waybillId, "指定任务单不存在");
            }
            ImageInfo exister = imageInfoMapper.selectByPrimaryKey(imageInfo.getId());
            if (exister != null) {
                imageInfo.setVerifyDate(new Date());
                if (imageInfoMapper.updateByPrimaryKeySelective(imageInfo) > 0) {
                    if (CollectionUtils.isNotEmpty(observers)) {
                        // 通知回单审核观察者
                        for (ReceiptObserverAdapter abstractObserver : observers) {
                            abstractObserver.notifyVerifyReceipt(WaybillContext.buildContext(waybill), imageInfo.getVerifyStatus(), exister.getVerifyStatus(), 1);
                        }
                    }
                }
            } else {
                throw new BusinessException("回单信息不存在!!!");
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("回单审核异常 {} {}", waybillId, imageInfo, e);
            throw BusinessException.dbException("回单审核异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 09:36:55
     * @see com.ycg.ksh.service.api.ReceiptService#listByWaybillId(java.lang.Integer)
     * <p>
     */
    @Override
    public List<MergeReceipt> listByWaybillId(Integer waybillId) throws ParameterException, BusinessException {
        Assert.notNull(waybillId, "运单编号不能为空");
        try {
            List<WaybillReceipt> receipts = receiptMapper.selectByWaybillId(waybillId);
            if (CollectionUtils.isNotEmpty(receipts)) {
                List<MergeReceipt> mergeWaybillReceipts = new ArrayList<MergeReceipt>(receipts.size());
                LocalCacheManager<AssociateUser> cache = LocalCacheFactory.createUserCache(userMapper);
                for (WaybillReceipt receipt : receipts) {
                    MergeReceipt merge = new MergeReceipt(receipt);
                    merge.setImages(imageInfoMapper.selectByReceiptId(receipt.getId()));
                    merge.setUser(cache.get(receipt.getUserid()));
                    mergeWaybillReceipts.add(merge);
                }
                return mergeWaybillReceipts;
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("查询运单回单信息异常, waybillId : {}", waybillId, e);
            throw BusinessException.dbException("查询运单回单信息异常");
        }
        return null;
    }

    @Override
    public CustomPage<WaybillReceiptView> queryReceiptPage(Map<String, Object> map, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = new PageScope(10, 1);
        }

        Page<WaybillReceiptView> page = waybillReceiptViewMapper.queryListPage(getTrimMap(map), new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));

        Collection<WaybillReceiptView> collection = new ArrayList<WaybillReceiptView>(page);
        return new CustomPage<WaybillReceiptView>(page.getPageNum(), page.getPageSize(), page.getTotal(), collection);
    }

    /**
     * 纸质回单查询过滤String类型空格
     */
    private Map<String, Object> getTrimMap(Map<String, Object> map) {
        Map<String, Object> currMap = new ConcurrentHashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue().getClass().equals(String.class)) {
                currMap.put(entry.getKey(), StringUtils.trimToEmpty(String.valueOf(entry.getValue())));
            } else {
                currMap.put(entry.getKey(), entry.getValue());
            }
        }
        return currMap;
    }

    @Override
    public CustomPage<WaybillReceiptView> historyRecordList(Map<String, Object> map, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = new PageScope(10, 1);
        }
        Page<WaybillReceiptView> page = waybillReceiptViewMapper.historyRecordList(getTrimMap(map), new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        Collection<WaybillReceiptView> collection = new ArrayList<WaybillReceiptView>(page);
        for (WaybillReceiptView waybillReceiptView : collection) {
            if (waybillReceiptView.getGroupid() == 0) {
                waybillReceiptView.setGroupName("个人用户");
            } else {
                waybillReceiptView.setGroupName(projectGroupMapper.selectByPrimaryKey(waybillReceiptView.getGroupid()).getGroupName());
            }
        }
        return new CustomPage<WaybillReceiptView>(page.getPageNum(), page.getPageSize(), page.getTotal(), collection);
    }

    private boolean validate(Integer cstatus, Integer dstatus) {

        //入库操作
        if ((dstatus == 1 || dstatus == 4) && (cstatus != 2 && cstatus != 3 && cstatus != 0)) { //回单回收 客户退回
            return false;
        }
        //出库操作
        if ((dstatus == 2 || dstatus == 3) && (cstatus != 1 && cstatus != 4)) { //2:送交客户 3:退供应商
            return false;
        }
/*
        if (dstatus == 1 && (cstatus != 0 && cstatus != 3)) {//回收回单
            return false;
        }
        if ((dstatus == 2 || dstatus == 3) && (cstatus != 1 && cstatus != 4)) {//2:送交客户 3:退供应商
            return false;
        }
        if (dstatus == 4 && cstatus != 2) {//客户退回
            return false;
        }*/
        return true;
    }

    @Override
    public MergeWayBillReceipt singleScan(PaperyReceipt paperyReceipt, Integer flag) throws ParameterException, BusinessException {
        logger.debug("THE ARGUMENTS OF singleScan method ReceiptServiceImpl :" + paperyReceipt);
        Assert.notNull(paperyReceipt, Constant.PARAMS_ERROR);
        Integer receiptStatus = paperyReceipt.getReceiptStatus();
        Integer userId = paperyReceipt.getUserId();
        WaybillReceiptStatus waybillReceiptStatus = new WaybillReceiptStatus();
        //批次表model
        ReceiptScanBatch receiptScanBatch = new ReceiptScanBatch();
        //根据barcode查询条形码信息
        Barcode barcode = barcodeService.getBarcode(paperyReceipt.getBarcode());
        if (barcode == null || barcode.getBindstatus() <= 10) {
            throw new BusinessException("条码不存在或未绑定");
        }
        //根据barcode获取运单信息实体
        Waybill waybill = waybillService.getWaybillByCode(barcode.getBarcode());
        if (waybill == null) {
            throw new BusinessException("运单查询异常");
        }
        if (waybill.getGroupid() == 0) {
            if (userId - waybill.getUserid() != 0) {
                throw new BusinessException("你没有权限扫描该条码");
            }
        } else if (null == projectGroupMemberMapper.get(waybill.getGroupid(), userId)) {
            throw new BusinessException("你没有权限扫描该条码");
        }
        //设置批次表信息
        receiptScanBatch.setBatchNumber(paperyReceipt.getBatchNumber());
        receiptScanBatch.setCreatetime(new Date());
        receiptScanBatch.setGroupId(waybill.getGroupid());
        receiptScanBatch.setUserId(userId);
        receiptScanBatch.setWaybillId(waybill.getId());

        if (receiptStatus != waybill.getPaperyReceiptStatus()) {
            String c_status = STATUSARRAYS[waybill.getPaperyReceiptStatus()], d_status = INFEASIBLE_STATUSARRAYS[receiptStatus];
            if (!validate(waybill.getPaperyReceiptStatus(), receiptStatus)) {
                if (StringUtils.isNotBlank(waybill.getDeliveryNumber())) {
                    throw new BusinessException(String.format(Constant.ABNORMAL_STATE_OF_RECEIPT2, waybill.getDeliveryNumber(), c_status, d_status));
                } else {
                    throw new BusinessException(String.format(Constant.ABNORMAL_STATE_OF_RECEIPT1, c_status, d_status));
                }
            }

            //修改运单回单状态
            waybill.setPaperyReceiptStatus(flag > 1 ? flag : receiptStatus);
            waybillService.updateWaybillStatusById(waybill);
            if (barcode.getBindstatus() < 35) {//没有到货，更改为到货
                //修改条形码状态 小于 40 和 小于 35  并且 大于10的数据
                barcode.setBindstatus(35);
                barcodeService.updateStatusById(barcode);
            }
            waybillReceiptStatus.setWaybillId(waybill.getId());
            waybillReceiptStatus.setUserId(userId);
            waybillReceiptStatus.setModifyTime(new Date());
            for (int i = 1; i <= flag; i++) {
                waybillReceiptStatus.setReceiptStatus(flag > 1 ? i : receiptStatus);
                //插入waybill_receipt_status 表 ，关联运单
                insertWaybillReceiptStatus(waybillReceiptStatus);

                //添加批次表信息
                receiptScanBatch.setReceiptStatus(flag > 1 ? i : receiptStatus);
                insertReceiptScanBatch(receiptScanBatch);
            }

        }

        //查询批次表详情
        ReceiptScanBatch rsbDetail = receiptScanBatchMapper.queryReceiptScanBatch(receiptScanBatch);

        //根据运单ID查询回单信息图片集合
        MergeWayBillReceipt receipt = new MergeWayBillReceipt();
        receipt.setReceiptList(listByWaybillId(waybill.getId()));
        receipt.setWaybill(waybill);
        logger.debug("-------------------------------回单信息------------------------" + receipt);
        if (null != receipt) {
            if (null != rsbDetail) {
                //设置批次详情
                receipt.setBatch(rsbDetail);
            }
        }
        return receipt;
    }


    public void insertWaybillReceiptStatus(WaybillReceiptStatus waybillReceiptStatus) {
        //插入waybill_receipt_status 表 ，关联运单
        waybillReceiptStatusMapper.insertWaybillReceiptStatus(waybillReceiptStatus);
    }

    public void insertReceiptScanBatch(ReceiptScanBatch receiptScanBatch) {
        //添加批次表信息
        receiptScanBatchMapper.insertMapper(receiptScanBatch);
    }

    @Override
    public String getBatchNumber() throws ParameterException, BusinessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        map.put("batchNumber", formatter.format(date));
        List<ReceiptScanBatch> list = receiptScanBatchMapper.queryListGroup(map);
        String thisCode = null;
        if (null != list && list.size() > 0) {
            if (list.size() < 10) {
                thisCode = formatter.format(date) + "-0" + list.size();
            } else {
                thisCode = formatter.format(date) + "-" + list.size();
            }
        }
        String batchNumber = ReceiptScanBatchUtil.getnumber(formatter, thisCode);
        return batchNumber;
    }

    @Override
    public Integer modifyReceiptStatus(MergeReceiptStatus mergeReceipt) throws ParameterException, BusinessException {
        Assert.notNull(mergeReceipt, Constant.PARAMS_ERROR);
        Assert.notEmpty(mergeReceipt.getWaybills(), "任务单ID不能为空");
        WaybillReceiptStatus waybillReceiptStatus = new WaybillReceiptStatus();
        Integer receiptStatus = mergeReceipt.getReceiptStatus();
        Integer userId = mergeReceipt.getUserId();
        Integer falg = 0;
        for (Integer waybillId : mergeReceipt.getWaybills()) {
            Waybill waybillDeail = waybillService.getWaybillById(waybillId);
            if (waybillDeail == null) {
                throw new BusinessException("任务单查询异常");
            }
            if (receiptStatus != waybillDeail.getPaperyReceiptStatus()) {
                if (!validate(waybillDeail.getPaperyReceiptStatus(), receiptStatus)) {
                    if (mergeReceipt.getWaybills().size() == 1) {
                        falg = 0;
                    }
                    continue;
                } else {
                    //修改运单回单状态
                    waybillDeail.setPaperyReceiptStatus(receiptStatus);
                    waybillService.updateWaybillStatusById(waybillDeail);
                    waybillReceiptStatus.setWaybillId(waybillDeail.getId());
                    waybillReceiptStatus.setReceiptStatus(receiptStatus);
                    waybillReceiptStatus.setUserId(Integer.valueOf(userId));
                    waybillReceiptStatus.setModifyTime(new Date());
                    //插入waybill_receipt_status 表 ，关联运单
                    waybillReceiptStatusMapper.insertWaybillReceiptStatus(waybillReceiptStatus);
                    falg++;
                }
            }
        }
        return falg;
    }

    @Override
    public List<MergeWayBillReceipt> listImagesByWaybillId(Integer waybillId) throws ParameterException, BusinessException {
        return null;
    }

    @Override
    public CustomPage<MergeReceiptStatistics> statisticsWaybill(MergeReceiptStatistics statistics, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = new PageScope(10, 1);
        }
        Page<MergeReceiptStatistics> page = receiptMapper.statisticsWaybill(statistics, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        Collection<MergeReceiptStatistics> collection = new ArrayList<MergeReceiptStatistics>(page);
        return new CustomPage<MergeReceiptStatistics>(page.getPageNum(), page.getPageSize(), page.getTotal(), collection);
    }

    @Override
    public CustomPage<MergeReceiptStatistics> receiptAnalysis(MergeReceiptStatistics statistics, PageScope pageScope) throws ParameterException, BusinessException {
        if (pageScope == null) {
            pageScope = new PageScope(10, 1);
        }
        Page<MergeReceiptStatistics> page = receiptMapper.listWaybillGroupBy(statistics, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        Collection<MergeReceiptStatistics> statisticsArrayList = new ArrayList<MergeReceiptStatistics>(page);
        if (null != statisticsArrayList && statisticsArrayList.size() > 0) {
            DecimalFormat df1 = new DecimalFormat("##.00%");
            for (MergeReceiptStatistics statistics1 : statisticsArrayList) {
                //总数
                Integer count = statistics1.getCountGroupid();
                //未回收百分比
                if (statistics1.getUnRecycleCount() > 0) {
                    statistics1.setUnRecyclePercent(df1.format((float) statistics1.getUnRecycleCount() / count));
                }
                //已退承运商百分比
                if (statistics1.getRetiredSupplierCount() > 0) {
                    statistics1.setRetiredSupplierPercent(df1.format((float) statistics1.getRetiredSupplierCount() / count));
                }
                //待收合计
                statistics1.setDueIn(statistics1.getUnRecycleCount() + statistics1.getRetiredSupplierCount());
                //待收百分比
                if (statistics1.getDueIn() > 0) {
                    statistics1.setDueInPercent(df1.format((float) statistics1.getDueIn() / count));
                }
                //回收百分比
                if (statistics1.getRecycleCount() > 0) {
                    statistics1.setRecycleCountPercent(df1.format((float) statistics1.getRecycleCount() / count));
                }

                //已退客户百分比
                if (statistics1.getRetiredClientsCount() > 0) {
                    statistics1.setRetiredClientsPercent(df1.format((float) statistics1.getRetiredClientsCount() / count));
                }
                //在库合计
                statistics1.setStore(statistics1.getRecycleCount() + statistics1.getRetiredClientsCount());
                //在库百分比
                if (statistics1.getStore() > 0) {
                    statistics1.setStorePercent(df1.format((float) statistics1.getStore() / count));
                }
                //递交客户百分比
                if (statistics1.getEndCustomercount() > 0) {
                    statistics1.setEndCustomerPercent(df1.format((float) statistics1.getEndCustomercount() / count));
                }
                if (statistics1.getGroupid() <= 0) {
                    statistics1.setGroupName("个人用户");
                } else {
                    statistics1.setGroupName(projectGroupMapper.selectByPrimaryKey(statistics1.getGroupid()).getGroupName());
                }
            }
        }
        return new CustomPage<MergeReceiptStatistics>(page.getPageNum(), page.getPageSize(), page.getTotal(), statisticsArrayList);
    }

    @Override
    public Integer[] receiptStorageList(MergeReceiptStatus mergeReceiptStatus) throws ParameterException, BusinessException {
        Integer flag[] = {0, 0};
        Example example = new Example(Waybill.class);
        example.createCriteria().andEqualTo("deliveryNumber", mergeReceiptStatus.getDeliveryNumber());
        List<Waybill> waybills = waybillMapper.selectByExample(example);
        if (null == waybills) {
            throw new BusinessException("当前运单下没有任务单");
        }
        for (Waybill waybill : waybills) {
            if (waybill.getGroupid() == 0 && (waybill.getUserid() != mergeReceiptStatus.getUserId())) {
                throw new BusinessException("你没有权限编辑该任务单!!!");
            }
            if (null == projectGroupMemberMapper.get(waybill.getGroupid(), mergeReceiptStatus.getUserId())) {
                throw new BusinessException("你没有权限编辑该任务单!!!");
            }
            ReceiptScanBatch receiptScanBatch = new ReceiptScanBatch();
            WaybillReceiptStatus waybillReceiptStatus = new WaybillReceiptStatus();

            if (validate(waybill.getPaperyReceiptStatus(), mergeReceiptStatus.getReceiptStatus())) {
                //修改运单回单状态
                waybill.setPaperyReceiptStatus(mergeReceiptStatus.getReceiptStatus());
                waybillService.updateWaybillStatusById(waybill);
                Barcode barcode = barcodeService.getBarcode(waybill.getBarcode());
                if (barcode.getBindstatus() < 35) {
                    //没有到货，更改为到货
                    //修改条形码状态 小于 40 和 小于 35  并且 大于10的数据
                    barcode.setBindstatus(35);
                    barcodeService.updateStatusById(barcode);
                }

                //设置批次表信息
                receiptScanBatch.setBatchNumber(mergeReceiptStatus.getBatchNumber());
                receiptScanBatch.setCreatetime(new Date());
                receiptScanBatch.setGroupId(waybill.getGroupid());
                receiptScanBatch.setUserId(mergeReceiptStatus.getUserId());
                receiptScanBatch.setWaybillId(waybill.getId());
                receiptScanBatch.setReceiptStatus(mergeReceiptStatus.getReceiptStatus());
                insertReceiptScanBatch(receiptScanBatch);

                //设置回单记录
                waybillReceiptStatus.setWaybillId(waybill.getId());
                waybillReceiptStatus.setUserId(mergeReceiptStatus.getUserId());
                waybillReceiptStatus.setModifyTime(new Date());
                waybillReceiptStatus.setReceiptStatus(mergeReceiptStatus.getReceiptStatus());
                insertWaybillReceiptStatus(waybillReceiptStatus);
                flag[1]++;
            }
        }
        flag[0] = waybills.size();
        return flag;
    }

    @Override
    public CustomPage<MergeReceiptResult> queryReceiptList(WaybillSerach serach, PageScope pageScope) {
        if (null == pageScope) {
            pageScope = PageScope.DEFAULT;
        }
        Page<MergeReceiptResult> page = receiptMapper.queryReceiptList(serach, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        Collection<MergeReceiptResult> lists = new ArrayList<MergeReceiptResult>(page);
        if (CollectionUtils.isNotEmpty(lists)) {
            for (MergeReceiptResult receiptResult : lists) {
                ProjectGroup group = projectGroupMapper.selectByPrimaryKey(receiptResult.getGroupid());
                if (null != group) {
                    receiptResult.setGroupName(group.getGroupName());
                } else {
                    receiptResult.setGroupName("个人用户");
                }
                receiptResult.setReceiptStatusName(getReceiptStatusStr(receiptResult));
            }
        }
        return new CustomPage<MergeReceiptResult>(page.getPageNum(), page.getPageSize(), page.getTotal(), lists);
    }

    @Override
    public CustomPage<MergeOperationRecord> queryOperationRecord(MergeOperationRecord record, PageScope pageScope) {
        if (null == pageScope) {
            pageScope = PageScope.DEFAULT;
        }
        Page<MergeOperationRecord> page = null;
        if (record.getSearchType() == 1) {
            page = receiptMapper.queryPaperyReceiptRecord(record, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        } else {
            page = receiptMapper.queryElectronicReceiptRecord(record, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        }
        Collection<MergeOperationRecord> mergeOperationRecords = new ArrayList<>(page);
        if (CollectionUtils.isNotEmpty(mergeOperationRecords)) {
            LocalCacheManager<AssociateUser> cache = LocalCacheFactory.createUserCache(userMapper);
            for (MergeOperationRecord mergeOperationRecord : mergeOperationRecords) {
                mergeOperationRecord.setOperationName(cache.get(mergeOperationRecord.getUserid()).getUnamezn());
            }
        }
        return new CustomPage<MergeOperationRecord>(page.getPageNum(), page.getPageSize(), page.getTotal(), mergeOperationRecords);
    }

    //回单审核状态
    public String getReceiptStatusStr(MergeReceiptResult receiptResult) {
        if (receiptResult.getReceiptCount() == 0) {
            return "未上传";
        } else {
            if ((receiptResult.getReceiptCount() > 0) && (receiptResult.getReceiptCount() - receiptResult.getReceiptVerifyCount() == receiptResult.getReceiptCount())) {
                return "已上传-全部未审核";
            }
            if (receiptResult.getReceiptCount() - receiptResult.getReceiptVerifyCount() != 0) {
                return "已上传-部分未审核";
            }
            if ((receiptResult.getReceiptCount() - receiptResult.getReceiptVerifyCount() == 0) && (receiptResult.getReceiptUnqualifyCount() > 0)) {
                return "已审核-有不合格";
            }
            if ((receiptResult.getReceiptCount() - receiptResult.getReceiptVerifyCount() == 0)
                    && (receiptResult.getReceiptUnqualifyCount() == 0)) {
                return "已审核完成";
            }
        }
        return null;
    }

    @Override
    public void onWaybillFettleChange(WaybillContext wcontext) throws BusinessException {
        logger.info("收到任务单状态变化通知 -> {}", wcontext);
        if (wcontext.getWaybillStatus().receive()) {
            // 更新所有未审核的回单审核状态更改为合格
            int count = imageInfoMapper.updateVerifyStatusByWaybillId(wcontext.getId(), wcontext.getUserKey(), Constant.VERIFYSTRTUS_APPROVAL);
            if (count > 0) {
                if (CollectionUtils.isNotEmpty(observers)) {
                    //通知回单审核观察者
                    for (ReceiptObserverAdapter abstractObserver : observers) {
                        abstractObserver.notifyVerifyReceipt(wcontext, Constant.VERIFYSTRTUS_APPROVAL, null, count);
                    }
                }
            }
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 16:40:06
     * @see WaybillObserverAdapter( MergeWaybill , WaybillAssociate )
     * <p>
     */
    @Override
    public void onMergeWaybill(MergeWaybill waybill, WaybillAssociate associate) throws BusinessException {
        if (associate.isAssociateReceipt()) {
            waybill.setReceipts(listByWaybillId(waybill.getId()));
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
        try {
            if (binding && StringUtils.isNotBlank(context.getBarcode())) {
                Collection<MergeTransitionReceipt> transitionReceipts = listTransitionReceipt(context.getBarcode());
                if (CollectionUtils.isNotEmpty(transitionReceipts)) {
                    for (MergeTransitionReceipt transitionReceipt : transitionReceipts) {
                        WaybillReceipt waybillReceipt = new WaybillReceipt();
                        waybillReceipt.setCreatetime(transitionReceipt.getReportTime());
                        waybillReceipt.setUserid(transitionReceipt.getUserId());
                        if (transitionReceipt.getReporter() != null) {
                            waybillReceipt.setUname(transitionReceipt.getReporter().getEncryptName());
                        }
                        waybillReceipt.setWaybillid(context.getId());
                        if (receiptMapper.insertSelective(waybillReceipt) > 0 && CollectionUtils.isNotEmpty(transitionReceipt.getImages())) {
                            Collection<ImageInfo> imageInfos = new ArrayList<ImageInfo>(transitionReceipt.getImages().size());
                            for (ImageStorage image : transitionReceipt.getImages()) {
                                ImageInfo imageInfo = new ImageInfo();
                                imageInfo.setCreatetime(new Date());
                                imageInfo.setPath(image.getStoragePath());
                                imageInfo.setReceiptid(waybillReceipt.getId());
                                imageInfos.add(imageInfo);
                            }
                            saveReceipt(context, waybillReceipt, imageInfos, false);
                        }
                    }
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("临时回单复制异常 {}", context, e);
            throw new BusinessException("更新任务单处理回单信息时异常 ");
        }
    }


    /**
     * 生成电子回单
     *
     * @param uKey
     * @param alliance
     * @return
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public FileEntity buildReceipt(Integer uKey, OrderAlliance alliance) throws BusinessException, ParameterException {
        OrderReceipt receipt = orderReceiptMapper.selectByPrimaryKey(alliance.getId());
        if (receipt != null) {
            throw new BusinessException("电子回单已经生成");
        }
        FileEntity fileEntity = buildReceipt(alliance);
        if (fileEntity != null) {
            OrderReceipt orderReceipt = new OrderReceipt();
            orderReceipt.setOrderNo(alliance.getId());
            orderReceipt.setPdfPath(fileEntity.persistence());
            orderReceipt.setCreateTime(new Date());
            orderReceipt.setUpdateTime(orderReceipt.getCreateTime());
            orderReceipt.setUserId(uKey);
            orderReceipt.setFettle(ReceiptFettleType.WAIT.getCode());
            orderReceiptMapper.insertSelective(orderReceipt);
            Contract contract = Contract.file("电子回单", String.valueOf(alliance.getId()), SystemUtils.fileRootPath(orderReceipt.getPdfPath()));
            if(SystemUtils.esignEnable()){
                contract = esignService.buildContract(Signer.YUNHETONG, contract);
                if(contract != null && contract.getRequestKey() != null){
                    supportService.saveSignAssociate(new SignAssociate(ObjectType.RECEIPT, alliance.getId(), Signer.YUNHETONG, contract.getRequestKey(), uKey));
                    //添加签署者
                    addContractSigner(alliance.getId(), alliance);
                }
            }
            supportService.saveOperateNote(uKey, OperateType.ESIGN, alliance.getId(), ESignEventType.BUILDCONTRACT, contract.toString());
        }
        return fileEntity;
    }

    private void addContractSigner(Serializable contractNo, OrderAlliance alliance){
        if(SystemUtils.esignEnable()){
            Collection<ContractSigner> signers = new ArrayList<ContractSigner>(3);
            for (OrderRoleType roleType : OrderRoleType.values()) {
                if(roleType.isShipper()){
                    continue;
                }
                CustomerConcise integrationy = alliance.getByRole(roleType);
                if(integrationy != null){
                    SignAssociate associate = supportService.getSignAssociate(ObjectType.COMPANY, integrationy.getId());
                    if(associate == null){
                        throw new BusinessException(roleType.getDesc() + "企业["+ integrationy.getCompanyName() +"]还未开通电子签收功能");
                    }
                    signers.add(new ContractSigner(associate.getThirdObjectKey(), roleType.getEnterprisePosition()));
                    /*associate = supportService.getSignAssociate(ObjectType.USERLEGALIZE, integrationy.getEmployeeKey());
                    if(associate == null){
                        throw new BusinessException(roleType.getDesc() + "员工["+ integrationy.getEmployeeName() +"]尚未实名认证");
                    }
                    signers.add(new ContractSigner(associate.getThirdObjectKey(), roleType.getPersonalPosition()));*/
                }
            }
            if(CollectionUtils.isEmpty(signers)){
                throw new BusinessException("没有符合要求的签署方信息,电子回单无法创建");
            }
            esignService.addContractSigner(Signer.YUNHETONG, contractNo, signers);
        }
    }

    /**
     * 验证该公司是否已经签章
     *
     * @param orderKey
     * @param companyKey
     * @return
     */
    public boolean validateSing(Long orderKey, Long companyKey) throws BusinessException, ParameterException {
        Example example = new Example(OrderSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderKey);
        criteria.andEqualTo("companyId", companyKey);
        return signatureMapper.selectCountByExample(example) > 0;
    }


    /**
     * 发送回单签署验证码
     *
     * @param uKey
     * @param orderKeys
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public void sendSignatureCode(Integer uKey, Collection<Long> orderKeys) throws BusinessException, ParameterException {
        Assert.notBlank(uKey, "用户编号不能空");
        Assert.notEmpty(orderKeys, "订单编号不能空");
        UserLegalize legalize = userService.getUserLegalize(uKey);
        if(legalize == null || legalize.getFettle() == 0){
            throw new BusinessException("当前用户未通过实名认证");
        }
        String code = smsService.sendCode(legalize.getMobilePhone(), "签署验证码%s,你正在使用电子回单签署,需要进行意愿校验,请勿向任何人提供你收到的短信校验码,10分钟内有效.");
        //记录发送的短息校验码
        for (Long orderKey : orderKeys) {
            cacheManager.set(String.format(SIGN_CODE, uKey, orderKey), code, 10L, TimeUnit.MINUTES);
            supportService.saveOperateNote(uKey, OperateType.ESIGN, orderKey, OrderEventType.SIGNCODE, legalize.getMobilePhone() +"->"+ code, code);
        }
    }

    /**
     * 批量签署
     *
     * @param code
     * @param orderkeys
     * @param exception
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public int signature(String code, OrderSignature orderSign, Collection<Long> orderkeys, String exception) throws BusinessException, ParameterException {
        Assert.notBlank(code, "签署短信校验码不能为空");
        Assert.notEmpty(orderkeys, "订单编号不能空");
        Assert.notNull(orderSign, "签署信息不能为空");
        Assert.notBlank(orderSign.getUserId(), "签署人编号不能为空");
        //Assert.notBlank(orderSign.getPersonalSeal(), "个人印章不能为空");
        Assert.notBlank(orderSign.getSealId(), "企业印章不能为空");
        int count = 0;
        for (Long orderkey : orderkeys) {
            orderSign.setOrderId(orderkey);
            try{
                signature(code, orderSign, exception);
                count++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 签署校验
     *
     * @param uKey
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public void signatureValidate(Integer uKey) throws BusinessException, ParameterException {
        ReceiptSignature signature = new ReceiptSignature();
        signature.setUserId(uKey);
        if(CollectionUtils.isNotEmpty(observers)){
            for (ReceiptObserverAdapter observer : observers) {
                observer.notifySignReceipt(ReceiptEventType.VALIDATE, signature, null);
            }
        }
    }

    /**
     * 签署电子回单
     *
     * @throws BusinessException
     * @throws ParameterException
     */
    @Override
    public void signature(String code, OrderSignature orderSign, String exception) throws BusinessException, ParameterException {
        Assert.notBlank(code, "签署短信校验码不能为空");
        Assert.notNull(orderSign, "签署信息不能为空");
        Assert.notBlank(orderSign.getUserId(), "签署人编号不能为空");
        Assert.notBlank(orderSign.getOrderId(), "签署订单编号不能为空");
        Assert.notBlank(orderSign.getSealId(), "企业印章不能为空");
        try {
            String smsKey = String.format(SIGN_CODE, orderSign.getUserId(), orderSign.getOrderId());
            Object vcode = cacheManager.get(smsKey);
            if(vcode == null){
                throw new BusinessException("SMS_CODE_ERROR","短信校验码已经过期");
            }
            if(!StringUtils.equalsIgnoreCase(String.valueOf(vcode), code)){
                throw new BusinessException("SMS_CODE_ERROR","短信校验码输入有误,请重新输入");
            }
            //记录填写短息校验码
            supportService.saveOperateNote(orderSign.getUserId(), OperateType.ESIGN, orderSign.getOrderId(), OrderEventType.SIGNCODE, code);
            cacheManager.delete(smsKey);

            OrderReceipt receipt = getDigitizedReceiptByKey(orderSign.getOrderId());
            if (receipt == null) {
                throw new BusinessException("该订单尚未生成电子回单");
            }
            ReceiptSignature signature = new ReceiptSignature(orderSign);
            signature.setReceipt(receipt);
            if(CollectionUtils.isNotEmpty(observers)){
                for (ReceiptObserverAdapter observer : observers) {
                    observer.notifySignReceipt(ReceiptEventType.VALIDATE, signature, exception);
                }
            }
            if(validateSing(signature.getOrderId(), signature.getCompanyId())){
                throw new BusinessException("所属公司已经签章");
            }
            OrderRoleType roleType = OrderUtil.signRoleType(signature.getOrder(), signature.getCompanyId());
            signature.setRoleType(roleType);
            signature.setId(Globallys.nextKey());
            signature.setCreateTime(new Date());
            signature.setSignRole(signature.getSignRole());
            signature.setCompanyId(signature.getCompanyId());
            signature(signature);

            ReceiptFettleType receiptFettle = ReceiptFettleType.convert(receipt.getFettle());
            if (roleType.isReceive()) {
                //如果是收货方签收 -> 承运方已签 ? 签收完成 ：收货方签收
                receiptFettle = receiptFettle.isCsign() ? ReceiptFettleType.YSIGN : ReceiptFettleType.SSIGN;
            } else if (roleType.isConvey()) {
                //如果是承运方签收 -> 收货方已签 ? 签收完成 ：承运方签收
                receiptFettle = receiptFettle.isSsign() ? ReceiptFettleType.YSIGN : ReceiptFettleType.CSIGN;
            }
            //modifyNoticeObserver(OrderEventType.SIGN, signature.getUserId(), alliance);
            if(CollectionUtils.isNotEmpty(observers)){
                for (ReceiptObserverAdapter observer : observers) {
                    observer.notifySignReceipt(ReceiptEventType.SIGN, signature, exception);
                }
            }
            signature.setSignRole(roleType.getCode());
            signatureMapper.insert(signature);

            orderReceiptMapper.updateByPrimaryKeySelective(new OrderReceipt(receipt.getOrderNo(), receiptFettle.getCode(), orderSign.getCreateTime()));
            if (roleType.isReceive()) {
                sendTemplateMessage(signature, exception);
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("signature --> {} {}", orderSign, exception, e);
            throw BusinessException.dbException("电子回单签章异常");
        }
    }


    private void signature(ReceiptSignature signature) throws BusinessException, ParameterException {
        try {
            if(SystemUtils.esignEnable()){
                //获取第三方回单编号
                SignAssociate ra = supportService.getSignAssociate(ObjectType.RECEIPT, signature.getOrderId());
                if(ra == null){
                    throw new BusinessException("当前订单尚未生产电子回单");
                }
                Company company = signature.getCompany();
                //获取第三方企业编号
                SignAssociate ca = supportService.getSignAssociate(ObjectType.COMPANY, company.getId());
                if(ca == null){
                    throw new BusinessException("企业["+ company.getCompanyName() +"]还未开通电子签收功能");
                }
                CompanySeal companySeal = signature.getCompanySeal();
                //获取第三方企业印章编号
                SignAssociate cma = supportService.getSignAssociate(ObjectType.COMPANYSEAL, companySeal.getId());
                if(cma == null){
                    throw new BusinessException("企业["+ company.getCompanyName() +"]还未创建印章");
                }
                //开始企业签章
                esignService.signContract(Signer.YUNHETONG, ra.getThirdObjectKey(), ca.getThirdObjectKey(), cma.getThirdObjectKey());
                /*UserLegalize legalize = signature.getLegalize();
                //获取第三方个人编号
                SignAssociate ua = supportService.getSignAssociate(ObjectType.USERLEGALIZE, legalize.getId());
                if(ua != null){
                    throw new BusinessException("当前用户["+ legalize.getName() +"]尚未通过实名认证");
                }
                PersonalSeal personalSeal = signature.getUserSeal();
                //获取第三方个人印章编号
                SignAssociate uma = supportService.getSignAssociate(ObjectType.USERSEAL, personalSeal.getId());
                if(cma != null){
                    throw new BusinessException("当前用户["+ legalize.getName() +"]尚未创建印章");
                }
                //开始个人签章
                esignService.signContract(Signer.YUNHETONG, ra.getThirdObjectKey(), ua.getThirdObjectKey(), uma.getThirdObjectKey());*/

                //记录电子签署操作
                supportService.saveOperateNote(signature.getUserId(), OperateType.ESIGN, signature.getId(), ESignEventType.SIGNCONTRACT, "电子回单签署");//内容待修改

            }else{
                CompanySeal companySeal = signature.getCompanySeal();
                //PersonalSeal personalSeal = signature.getUserSeal();
                Collection<SealObject> sealObjects = new ArrayList<SealObject>(1);
                OrderRoleType roleType = signature.getRoleType();
                sealObjects.add(new SealObject(roleType.getEnterprisePosition(), SystemUtils.fileRootPath(companySeal.getSealData())));
                //sealObjects.add(new SealObject(roleType.getPersonalPosition(), SystemUtils.fileRootPath(personalSeal.getSealImgPath())));
                String path = SystemUtils.fileRootPath(Directory.ERECEIPT.sub(FileUtils.appendSuffix(String.valueOf(signature.getOrderId()), FileUtils.PDF_SUFFIX)));
                logger.info("signature->{}", path);
                PDFFactory.getInstance().signature(new File(path), sealObjects);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("电子回单签署异常 {} {}", signature, e);
            throw new BusinessException("电子回单签署异常");
        }
    }

    private Collection<OrderAlliance> pageAlliance(OrderAlliance source, int count){
        if(CollectionUtils.isNotEmpty(source.getCommodities()) && source.getCommodities().size() > count){
            Collection<OrderAlliance> collection = new ArrayList<OrderAlliance>();
            while (CollectionUtils.isNotEmpty(source.getCommodities())){
                OrderAlliance alliance = source.deepCopy();
                int index = 0;
                alliance.getCommodities().clear();
                for (Iterator<OrderCommodity> iterator = source.getCommodities().iterator(); iterator.hasNext(); ) {
                    if(index++ >= count){ break; }
                    alliance.addCommodity(iterator.next());
                    iterator.remove();
                }
                collection.add(alliance);
            }
            return collection;
        }
        return Lists.newArrayList(source);
    }

    private FileEntity buildReceipt(OrderAlliance alliance) throws BusinessException, ParameterException {
        CompanyTemplate companyTemplate = companyService.getTemplateByCompanyByKey(alliance.getShipperId());
        if (companyTemplate == null) {
            throw new BusinessException("当前企业没有电子回单模板");
        }
        try {
            Charset charset = Charset.forName("utf-8");
            String pdfName = String.valueOf(alliance.getId());
            PDFFactory pdfFactory = PDFFactory.getInstance();
            String templateContent = new String(Base64.decodeBase64(companyTemplate.getHtmlString()), charset);
            String cssContent = new String(Base64.decodeBase64(companyTemplate.getCssString()), charset);

            return pdfFactory.createByTemplate(pdfName, pageAlliance(alliance, 8), templateContent, cssContent);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("生成电子回单异常 {}", alliance, e);
            throw new BusinessException("生成电子回单异常");
        }
    }

    /**
     * 发送模板消息
     *
     * @param signature
     * @param exception
     */
    private void sendTemplateMessage(ReceiptSignature signature, String exception) {
        logger.info("签收发送模板消息 {}", signature);
        Order order = signature.getOrder();
        User user = userService.getUserByKey(order.getCreateUserId());
        try {
            /*
            first:配送单号为123456789012的配送单已经完好签收,请悉知!
            订单单号keyword1:149252965982208
            收货客户keyword2:远成物流杭州分公司
            签收时间keyword3:2018年04月27日 10点30分
            签收类型keyword4:完好签收
            remark:感谢你使用合同物流管理平台平台 {{first.DATA}}
            */
            if (user != null && StringUtils.isNotBlank(user.getUsername())) {
                TemplateMesssage messsage = new TemplateMesssage(user.getUsername());
                messsage.addData("first", new TemplateDataValue("配送单" + order.getDeliveryNo() + "已经签收,请悉知!", "#173177"));
                messsage.addData("keyword1", new TemplateDataValue(order.getOrderNo(), "#173177"));
                if (StringUtils.isNotBlank(exception)) {
                    messsage.addData("keyword4", new TemplateDataValue("异常签收", "#FF0000"));
                    messsage.setTemplateType(TemplateType.YCSIGN);
                } else {
                    messsage.addData("keyword4", new TemplateDataValue("完好签收", "#00FF00"));
                    messsage.setTemplateType(TemplateType.WHSIGN);
                }
                messsage.setUrl(FrontUtils.eleReceipt(SecurityTokenUtil.createToken(String.valueOf(user.getId())), order.getId()));
                Company company = signature.getCompany();

                messsage.addData("keyword2", new TemplateDataValue(company.getCompanyName(), "#173177"));
                messsage.addData("keyword3", new TemplateDataValue(DateUtils.formatDate(signature.getCreateTime(), "yyyy年MM月dd日 HH点mm分"), "#173177"));

                messsage.addData("remark", new TemplateDataValue("点击详情查看具体信息,感谢使用合同物流管理平台平台", "#173177"));
                queueService.sendNetMessage(new MediaMessage(signature.getId(), messsage));
            }
        } catch (Exception e) {
            logger.error("发送模板消息异常 {} {}", user.getId(), user.getUsername(), e);
        }
    }

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
        if(Arrays.binarySearch(flags, O.paperreceipt) >= 0){
            alliance.setImageStorages(listIamgesByOrderKey(alliance.getId()));
        }
        if(Arrays.binarySearch(flags, O.receiptfettle) >= 0){
            if (ReceiptFettleType.convert(alliance.getReceiptFettle()).isDefault()) {
                OrderReceipt receipt = getDigitizedReceiptByKey(alliance.getId());
                if (receipt != null) {
                    alliance.setReceiptFettle(receipt.getFettle());
                } else {
                    alliance.setReceiptFettle(ReceiptFettleType.DEFAULT.getCode());
                }
            }
        }
    }

    @Override
    public Collection<ImageStorage> listIamgesByOrderKey(Long orderKey) throws BusinessException, ParameterException {
        Assert.notBlank(orderKey, "订单编号不能为空");
        Collection<PaperReceipt> paperReceipts = paperReceiptMapper.select(new PaperReceipt(orderKey));
        if(CollectionUtils.isNotEmpty(paperReceipts)){
            Collection<ImageStorage> storages = new ArrayList<ImageStorage>();
            for (PaperReceipt paperReceipt : paperReceipts) {
                storages.addAll(imageStorageService.list(Constant.IMAGE_TYPE_RECEIPT, paperReceipt.getId()));
            }
            return storages;
        }
        return null;
    }

    @Override
    public FileEntity exportReceipt(Integer uKey, Collection<Long> orderKeys) throws BusinessException, ParameterException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notEmpty(orderKeys, "订单ID为空");
        File srcFile = null;
        String user_temp_path = SystemUtils.directoryUserTemp(uKey);
        try {
            int count = 0;
            String zip_temp_path = FileUtils.path(user_temp_path, "电子回单文件");
            File tempDirectory = FileUtils.directory(zip_temp_path);
            for (Long orderKey : orderKeys) {
                if (null == orderKey || orderKey <= 0) { continue; }
                OrderReceipt orderReceipt = getDigitizedReceiptByKey(orderKey);
                if (null == orderReceipt) { continue; }
               //if(StringUtils.isBlank(orderReceipt.getPdfPath())){//未下载回单
                    //下载并更新回单地址
                orderReceipt = download(orderReceipt);
               // }
                if(StringUtils.isBlank(orderReceipt.getPdfPath())){
                    continue;
                }
                srcFile = FileUtils.file(SystemUtils.fileRootPath(orderReceipt.getPdfPath()), false);
                FileUtils.copyFileToDirectory(srcFile, tempDirectory, false);
                count++;
            }
            return new FileEntity(FileUtils.zip(zip_temp_path, SystemUtils.directoryDownload()), count);
        } catch (Exception be) {
            logger.error("电子回单导出文件异常 {} {}", uKey, orderKeys, be);
            throw new BusinessException("导出回单文件异常");
        } finally {
            try {
                FileUtils.deleteDirectory(user_temp_path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private OrderReceipt download(OrderReceipt orderReceipt){
        try{
           if(SystemUtils.esignEnable()){
               //获取第三方回单编号
               SignAssociate ra = supportService.getSignAssociate(ObjectType.RECEIPT, orderReceipt.getOrderNo());
               if(ra != null){
                   FileEntity fileEntity = esignService.download(Signer.YUNHETONG, orderReceipt.getOrderNo());
                   if(fileEntity != null){
                       orderReceipt.setPdfPath(fileEntity.persistence());
                       orderReceipt.setUpdateTime(new Date());
                       orderReceiptMapper.updateByPrimaryKeySelective(orderReceipt);
                   }
               }
           }
        }catch (Exception e){
            logger.error("电子回单下载异常 {}", orderReceipt, e);
        }
        return orderReceipt;
    }


    @Override
    public OrderReceipt getDigitizedReceiptByKey(Long orderKey) throws BusinessException, ParameterException {
        Assert.notBlank(orderKey, "订单编号不能为空");
        try {
            return orderReceiptMapper.selectByPrimaryKey(orderKey);
        } catch (Exception be) {
            logger.error("查询电子回单信息异常 {}", orderKey, be);
            throw new BusinessException("查询电子回单信息异常");
        }
    }

    @Override
    public Object adventiveObjectByKey(ObjectType objectType, Serializable objectKey) throws Exception {
        if(ObjectType.RECEIPT == objectType){
            return paperReceiptMapper.selectByPrimaryKey(Long.parseLong(objectKey.toString()));
        }
        return null;
    }
}
