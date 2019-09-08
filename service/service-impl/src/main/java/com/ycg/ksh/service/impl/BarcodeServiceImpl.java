package com.ycg.ksh.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.common.barcode.CodeBuilder;
import com.ycg.ksh.common.barcode.PDFBuilder;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.common.util.encrypt.DES;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.constant.BarCodeFettle;
import com.ycg.ksh.constant.OrderEventType;
import com.ycg.ksh.constant.OrderFettleType;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.service.BarCodeDescription;
import com.ycg.ksh.entity.service.BarcodeSearch;
import com.ycg.ksh.entity.service.DecryptBarcode;
import com.ycg.ksh.entity.service.MergeApplyRes;
import com.ycg.ksh.entity.service.MergeBarcode;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.WaybillContext;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.CompanyCodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.observer.BarcodeObserverAdapter;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.persistence.ApplyResMapper;
import com.ycg.ksh.service.persistence.BarcodeMapper;
import com.ycg.ksh.service.persistence.CompanyMapper;
import com.ycg.ksh.service.persistence.OrderMapper;
import com.ycg.ksh.service.persistence.ProjectGroupMapper;

import tk.mybatis.mapper.entity.Example;

@Service("ksh.core.service.barCodeService")
public class BarcodeServiceImpl implements BarCodeService, WaybillObserverAdapter, OrderObserverAdapter{

    private static final String CODE_KEY = "CODE#";

    @Resource
    MessageQueueService queueService;

    @Resource
    BarcodeMapper barcodeMapper;
    @Resource
    ApplyResMapper applyResMapper;
    @Resource
    CacheManager cacheManager;
    @Resource
    OrderMapper orderMapper;

    @Resource
    CompanyMapper companyMapper;
    @Resource
    ProjectGroupMapper groupMapper;

    @Autowired(required = false)
    Collection<BarcodeObserverAdapter> observers;

    private Barcode get(String code){
        return cacheManager.get(CODE_KEY + code, TimeUnit.HOURS.toMinutes(1L), () -> {
            return barcodeMapper.getByCode(code);
        });
        //Assert.notBlank(code, "条码编号不能为空");
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
        if(eventType.isBindCode()){//绑定事件
            barcodeMapper.modifyStatus(order.getBindCode(), Constant.BARCODE_STATUS_BIND);
            cacheManager.delete(CODE_KEY + order.getBindCode());
        }
    }

    private DecryptBarcode decryptValidate(String code) throws ParameterException, BusinessException {
        DecryptBarcode decryptBarcode = new DecryptBarcode(code, false);
        Validator validator = Validator.NUMBER;
        if (!validator.verify(decryptBarcode.getCode())) {
            try {
                decryptBarcode.setCode(DES.decrypt(decryptBarcode.getCode(), SystemUtils.secretKey()));
                decryptBarcode.setDecrypt(true);
            } catch (Exception e) {
                throw new ParameterException(decryptBarcode.getCode(), "无效的条码");
            }
        }
        validator = Validator.BARCODE;
        if (!validator.verify(decryptBarcode.getCode())) {
            throw new ParameterException(decryptBarcode.getCode(), validator.getMessage(""));
        }
        return decryptBarcode;
    }

    @Override
    public Barcode validate(String code) throws ParameterException, BusinessException {
        DecryptBarcode decryptBarcode = decryptValidate(code);
        Barcode barcode = get(decryptBarcode.getCode());
        if (barcode == null) {
            throw new ParameterException(decryptBarcode.getCode(), "无效条码");
        }
        if (!decryptBarcode.isDecrypt()) {
            Date createTime = barcode.getCreatetime();
            //如果是旧条码，并且不是在2018-01-01之前生成的，就是无效的
            if (createTime != null && createTime.getTime() > 1514736000000L) {
                throw new ParameterException(decryptBarcode.getCode(), "无效条码");
            }
        }
        return barcode;
    }

    @Override
    public BarcodeContext validateNotDecrypt(Integer userId, String code) throws ParameterException, BusinessException {
        DecryptBarcode decryptBarcode = decryptValidate(code);
        Barcode barcode = get(decryptBarcode.getCode());
        if (barcode == null) {
            throw new ParameterException(code, "无效条码");
        }
        BarcodeContext context = buildContext(userId, barcode);
        if (CollectionUtils.isNotEmpty(observers)) {
            for (BarcodeObserverAdapter barcodeObserverAdapter : observers) {
                barcodeObserverAdapter.notifyBarcodeValidate(barcode, context);
            }
        }
        return context;
    }

    private BarcodeContext buildContext(Integer userId, Barcode barcode){
        BarcodeContext context = null;
        if(barcode.getCompanyId() != null && barcode.getCompanyId() > 0){
            context = new CompanyCodeContext(barcode.getCompanyId(), OrderFettleType.convert(barcode.getBindstatus()));
        }else{
            context = new GroupCodeContext(barcode.getGroupid(), barcode.getUserid(), WaybillFettle.convert(barcode.getBindstatus()));
        }
        context.setCodeFettle(BarCodeFettle.convert(barcode.getBindstatus()));
        context.setUserKey(userId);
        context.setBarcode(barcode.getBarcode());
        return context;
    }



    @Override
    public BarcodeContext validate(Integer userId, String code) throws ParameterException, BusinessException {
        Assert.notNull(code, Constant.PARAMS_ERROR);
        Barcode barcode = validate(code);
        if (barcode == null) {
            throw new ParameterException(code, Constant.INVALID_BARCODE);
        }
        BarcodeContext context = buildContext(userId, barcode);
        if (CollectionUtils.isNotEmpty(observers)) {
            for (BarcodeObserverAdapter barcodeObserverAdapter : observers) {
                barcodeObserverAdapter.notifyBarcodeValidate(barcode, context);
            }
        }
        return context;
    }

    /**
     * @return Barcode 条码信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务异常
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-11-24 13:52:32
     * @see com.ycg.ksh.service.api.BarCodeService#getBarcode(java.lang.String)
     * <p>
     */
    @Override
    public Barcode getBarcode(String code) throws ParameterException, BusinessException {
        try {
            DecryptBarcode decryptBarcode = decryptValidate(code);
            Barcode barcode = get(decryptBarcode.getCode());
            if (barcode == null) {
                throw new BusinessException("无效的二维码");
            }
            return barcode;
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("条码信息查询异常:{}", code, e);
            throw BusinessException.dbException("条码信息查询异常");
        }
    }

    /**
     * 查询条码信息
     * <p>
     *
     * @param barcode
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-11-24 13:33:13
     */
    @Override
    public Barcode loadBarcode(String barcode) throws ParameterException, BusinessException {
        try {
            return get(barcode);
        } catch (Exception e) {
            logger.error("条码信息查询异常:{}", barcode, e);
            throw BusinessException.dbException("条码信息查询异常");
        }
    }

    /**
     * @param applyRes
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveBuildCode(ApplyRes applyRes) throws ParameterException, BusinessException {
        logger.info("开始生成条码 {}", applyRes);
        Date applyTime = null, ctime = new Date();
        if ((applyTime = applyRes.getCreatetime()) == null) {
            applyTime = ctime;
        }
        int len = 6;
        String lastCode = barcodeMapper.selectLastCode(DateUtils.formatDate(applyTime, "yyyy-MM-dd"), 5 + len);
        CodeBuilder builder = CodeBuilder.build(len, lastCode);
        int total = 0, times = applyRes.getNumber();
        ApplyRes updater = new ApplyRes(applyRes.getId(), Constant.CODE_RES_BUILDED);
        Collection<Barcode> collection = new ArrayList<Barcode>(200);
        do {
            Barcode barcode = new Barcode();
            barcode.setBarcode(builder.next());
            barcode.setUserid(applyRes.getUserid());
            barcode.setBindstatus(Constant.BARCODE_STATUS_NO);
            barcode.setGroupid(applyRes.getGroupid());
            barcode.setCreatetime(ctime);
            barcode.setCodeBatch(lastCode);
            barcode.setResourceid(applyRes.getId());
            barcode.setCompanyId(applyRes.getCompanyId());
            collection.add(barcode);
            if (collection.size() % 200 == 0) {//每200个初始化一次
                barcodeMapper.inserts(collection);
                collection.clear();
            }
            if (++total == 1) {
                updater.setStartNum(barcode.getBarcode());
            }
            lastCode = barcode.getBarcode();
        } while (times - total > 0);
        if (collection.size() > 0) {
            barcodeMapper.inserts(collection);
        }
        updater.setEndNum(lastCode);
        applyResMapper.updateByPrimaryKeySelective(updater);
    }

    @Override
    public CustomPage<MergeApplyRes> pageApplyResList(MergeApplyRes applyRes, PageScope pageScope, Integer type) throws BusinessException {
        Page<MergeApplyRes> page = null;
        switch (type) {
            case 1:
                page= applyResMapper.queryApplyResList(applyRes, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
                break;
            case 2:
            	String uname = applyRes.getUname();
            	if(StringUtils.isNotBlank(uname)){
            		applyRes.setUname(UserUtil.encodeName(uname));
            	}
                page = applyResMapper.queryApplyResListv2(applyRes,new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
                break;
            default:
                page = new Page<MergeApplyRes>();
        }
        return new CustomPage<MergeApplyRes>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public MergeApplyRes queryTotalCount(Integer userKey) throws ParameterException, BusinessException {

        MergeApplyRes mergeApplyRes = new MergeApplyRes();
        Map<String, Number> map = barcodeMapper.countByUser(userKey);
        if (null != map) {
            Number number = map.get("useCount");
            if (number != null) {
                mergeApplyRes.setAlreadyTotal(number.intValue());
            }
            Number useCount = map.get("number");
            if (useCount != null) {
                mergeApplyRes.setAvailableTotal(useCount.intValue());
            }
            Number totalNum = map.get("totalNum");
            if (totalNum != null) {
                mergeApplyRes.setTotal(totalNum.intValue());
            }
        }
        return mergeApplyRes;
    }

    @Override
    public void updateBarCodeChange(ApplyRes applyRes) throws ParameterException, BusinessException {
        Assert.notNull(applyRes, "变更信息不能为空");
        Long companyId = applyRes.getCompanyId();
        Assert.notBlank(companyId, "变更企业不能为空");
        String startNum = applyRes.getStartNum(), endNum = applyRes.getEndNum();
        if ((startNum.length() != 11 && startNum.length() != 14) || (endNum.length() != 11 && endNum.length() != 14)) {
            throw new BusinessException("开始编号或结束编号格式有误");
        }
        if(!Validator.NUMBER.verify(startNum) || !Validator.NUMBER.verify(startNum)){
        	 throw new BusinessException("条码格式有误");
        }
        try {
        	 Example example = new Example(Barcode.class);
             Example.Criteria criteria = example.createCriteria();
             criteria.andGreaterThanOrEqualTo("barcode",startNum);
        	 criteria.andLessThanOrEqualTo("barcode", endNum);
        	 criteria.andEqualTo("bindstatus", BarCodeFettle.UNBIND.getCode());
             int count = barcodeMapper.selectCountByExample(example);
             Long dif = Long.parseLong(endNum) - Long.parseLong(startNum) + 1;
             if(dif <= 0)throw new BusinessException("条码号段不正确");
             if(count == dif){
            	 Long companyIdOld = barcodeMapper.getByCode(startNum).getCompanyId();
            	 Long companyIdOldEnd = barcodeMapper.getByCode(endNum).getCompanyId();
            	 if(null != companyIdOld && null != companyIdOldEnd){
            		 if(companyId == companyIdOld || companyId == companyIdOldEnd) throw new BusinessException("该条码号段已经属于该企业");
            	 }
                 applyRes.setCreatetime(new Date());
                 applyRes.setNumber(dif.intValue());
                 applyRes.setPrintStatus(2);//条码已生成
                 if(applyResMapper.insertSelective(applyRes)>0){
                	 criteria.andEqualTo("companyId", companyIdOld);
                	 Barcode barcode = new Barcode(applyRes.getId(), 0, companyId);
                	 barcodeMapper.updateByExampleSelective(barcode, example);
                 }
             }else {
                 throw new BusinessException("当前区间有已使用的条码");
             }
		}catch (ParameterException | BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("updateBarCodeChange -> applyRes:{}", applyRes, e);
	        throw BusinessException.dbException("条码变更异常");
		}
    }

    @Override
    public BarCodeDescription getBarcodeDescription(String barcode) throws BusinessException,ParameterException{
        Barcode barCode = getBarcode(barcode);
        BarCodeDescription description = new BarCodeDescription();
        try {
            BeanUtils.build().copyProperties(description,barCode);
        } catch (Exception e) {
            throw new BusinessException("数据转换异常");
        }
        Integer groupId;Long companyId;
        if((groupId = description.getGroupid()) != null && groupId > 0)    //属于项目组
            description.setGroupName(Optional.ofNullable(groupMapper.selectByPrimaryKey(groupId))
                    .map(group -> group.getGroupName()).orElse(""));

        if ((companyId = description.getCompanyId()) != null && companyId > 0)     //属于公司
            description.setCompanyName(Optional.ofNullable(companyMapper.selectByPrimaryKey(companyId))
                .map(company -> company.getCompanyName()).orElse(""));

        Order order = orderMapper.selectByCode(barcode);
        if (order == null)
            description.setBarCodeFettle(BarCodeFettle.UNBIND);
        else
            description.setBarCodeFettle(BarCodeFettle.BIND);

        return description;
    }

    @Override
    public void updateBarCodeChangeOld(MergeApplyRes mergeApplyRes) throws ParameterException, BusinessException {
        Assert.notNull(mergeApplyRes, "变更信息不能为空");
        String startNum = mergeApplyRes.getStartNum(), endNum = mergeApplyRes.getEndNum();
        Integer userId = mergeApplyRes.getUserid(), changeUserId = mergeApplyRes.getChangeUserId();
        Assert.notBlank(userId, "所属用户不能为空");
        Assert.notBlank(changeUserId, "变更用户不能为空");
        if ((startNum.length() != 11 && startNum.length() != 14) || (endNum.length() != 11 && endNum.length() != 14)) {
            throw new BusinessException("开始编号或结束编号格式有误");
        }
        if (changeUserId - userId == 0 && mergeApplyRes.getGroupid() <= 0) {
            throw new BusinessException("所属用户和变更用户不能相同");
        }
        try {
        	Example example = new Example(Barcode.class);
        	Example.Criteria criteria = example.createCriteria();
        	criteria.andGreaterThanOrEqualTo("barcode", startNum);
        	criteria.andLessThanOrEqualTo("barcode", endNum);
        	criteria.andEqualTo("userid", userId);
        	criteria.andGreaterThan("bindstatus", 10);
        	int count = barcodeMapper.selectCountByExample(example);
        	if (count <= 0) {
        		Example example1 = new Example(Barcode.class);
        		Example.Criteria criteria1 = example1.createCriteria();
        		Barcode barcode = new Barcode();
        		criteria1.andGreaterThanOrEqualTo("barcode", startNum);
        		criteria1.andLessThanOrEqualTo("barcode", endNum);
        		criteria1.andEqualTo("userid", userId);
        		criteria1.andEqualTo("bindstatus", 10);
        		if (barcodeMapper.selectCountByExample(example1) <= 0) {
        			throw new BusinessException("当前区间无可变更的条码");
        		}
        		barcode.setGroupid(mergeApplyRes.getGroupid());
        		barcode.setUserid(changeUserId);
        		barcodeMapper.updateByExampleSelective(barcode, example1);
        	} else {
        		throw new BusinessException("当前区间有已使用的条码");
        	}
		} catch (ParameterException | BusinessException e) {
			throw e;
		}catch (Exception e) {
			logger.error("updateBarCodeChange -> mergeApplyRes:{}", mergeApplyRes, e);
	        throw BusinessException.dbException("条码变更异常");
		}
    }

    @Override
    public void saveBuildReady(Integer uKey, Integer applyId) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人ID不能为空");
        Assert.notBlank(applyId, "申请ID不能为空");
        try {
            ApplyRes applyRes = applyResMapper.selectByPrimaryKey(applyId);
            if (applyRes == null) {
                throw new ParameterException(applyId, "该申请记录不存在");
            }
            if(Constant.CODE_RES_APPLY  == applyRes.getPrintStatus()){
                applyResMapper.updateByPrimaryKeySelective(new ApplyRes(applyRes.getId(), Constant.CODE_RES_READY));

                queueService.sendCollectMessage(new MediaMessage(QueueKeys.MESSAGE_TYPE_GENERATE_BARCODE,  Globallys.UUID(), applyRes.getId()));
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveBuildReady ->uKey:{} applyId:{}", uKey, applyId, e);
            throw BusinessException.dbException("更改申请状态异常");
        }
    }

    @Override
    public List<Map<String, Object>> barcodeListByResId(Integer resId) throws ParameterException, BusinessException {
        Assert.notBlank(resId, Constant.PARAMS_ERROR);
        List<Map<String, Object>> list = barcodeMapper.barcodeListByResId(resId);
        return list;
    }

    @Override
    public FileEntity buildPDF(Integer uKey, Integer resKey) throws ParameterException, BusinessException {
        Assert.notBlank(resKey, "申请ID不能为空");
        BarcodeSearch search = new BarcodeSearch(resKey, Constant.BARCODE_STATUS_NO);
        Page<Barcode> barcodes = null;
        int total = 0, pageNum = 1;
        String suffix = "pdf";
        try {
            File directory = new File(SystemUtils.directoryTemp(suffix + resKey));
            if (directory.exists()) {
                FileUtils.deleteDirectory(directory);
            }
            directory = FileUtils.directory(directory);
            do {
                barcodes = barcodeMapper.listBySomething(search, new RowBounds(pageNum++, 1000));
                if (CollectionUtils.isNotEmpty(barcodes)) {
                    StringBuilder fileName = new StringBuilder(barcodes.get(0).getBarcode());
                    if (barcodes.size() > 1) {
                        fileName.append("-").append(barcodes.get(barcodes.size() - 1).getBarcode());
                    }
                    PDFBuilder builder = new PDFBuilder(FileUtils.file(directory, FileUtils.appendSuffix(fileName.toString(), suffix)));
                    builder.ready();
                    for (Barcode barcode : barcodes) {
                        builder.insert(barcode.getBarcode(), SystemUtils.buildQRcodeContext(barcode.getBarcode()));
                    }
                    total += barcodes.size();
                    builder.close();
                } else {
                    if (pageNum == 1) {
                        throw new BusinessException("没有可用条码,或者条码已经使用完");
                    }
                }
            } while (total < barcodes.getTotal());
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
                applyResMapper.updateByPrimaryKeySelective(new ApplyRes(resKey, Constant.CODE_RES_DOWNLOAD));
                return entity;
            }
        } catch (Exception e) {
            logger.error("PDF文件生成异常 uKey:{} resKey:{}", uKey, resKey, e);
            throw new BusinessException("PDF文件生成异常,稍后再尝试!!!");
        }
        return null;
    }

    @Override
    public void updateStatusById(Barcode barcode) throws ParameterException, BusinessException {
        barcodeMapper.updateStatusById(barcode);
    }


    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 09:51:07
     * @see com.ycg.ksh.service.api.BarCodeService( ApplyRes )
     * <p>
     */
    @Override
    public void applyRes(Integer uKey, ApplyRes applyRes) throws ParameterException, BusinessException {
        Assert.notNull(applyRes, "申请信息不能为空");
        Assert.notBetween(applyRes.getNumber(), 1D, 9999D);
        try {
            applyRes.setUserid(uKey);
            applyRes.setCreatetime(new Date());
            applyRes.setPrintStatus(Constant.CODE_RES_READY);
            if(applyResMapper.insertSelective(applyRes) > 0){
                queueService.sendCollectMessage(new MediaMessage(QueueKeys.MESSAGE_TYPE_GENERATE_BARCODE,  Globallys.UUID(), applyRes.getId()));
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("applyRes -> uKey:{} {}", uKey, applyRes, e);
            throw BusinessException.dbException("二维码申请异常");
        }
    }

    @Override
    public Collection<ApplyRes> listNotBuildApplies() throws ParameterException, BusinessException {
        Example example = new Example(ApplyRes.class);
        example.createCriteria().andEqualTo("printStatus", Constant.CODE_RES_READY);
        return applyResMapper.selectByExample(example);
    }

    @Override
    public void onInitializeWaybill(WaybillContext context) throws BusinessException {
        if (StringUtils.isNotBlank(context.getUpdate().getBarcode())) {
            if(context.getPersistence() == null ||
                    StringUtils.isBlank(context.getPersistence().getBarcode())){
                Barcode barcode = getBarcode(context.getBarcode());
                if (barcode == null) {
                    throw new ParameterException(context.getBarcode(), "条码编号[" + context.getBarcode() + "]不存在");
                }
                if (Constant.BARCODE_STATUS_BIND - barcode.getBindstatus() == 0) {
                    throw new BusinessException("条码编号[" + context.getBarcode() + "]已经绑定不能重复绑定");
                }
                context.setBarcodeid(barcode.getId());
                context.setBarcode(barcode.getBarcode());
                context.setGroupid(barcode.getGroupid());
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
        String barcode = context.getBarcode();
        if (StringUtils.isNotBlank(barcode) && !context.getWaybillStatus().unbind()) {
            barcodeMapper.modifyStatus(barcode, Constant.BARCODE_STATUS_BIND);
            cacheManager.delete(CODE_KEY + barcode);
        }
    }

    /**
     * 保存barcode
     *
     * @param uKey
     * @param gKey
     * @param barcode
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void save(Integer uKey, Integer gKey, String barcode) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户ID不能为空");
        Assert.notBlank(barcode, "条码号不能为空");
        try {
            Barcode object = new Barcode();
            object.setBarcode(barcode);
            object.setUserid(uKey);
            object.setGroupid(gKey);
            object.setCreatetime(new Date());
            object.setBindstatus(WaybillFettle.UNBIND.getCode());
            barcodeMapper.insert(object);
        }catch (Exception e) {
            logger.error("save -> uKey:{} gKey:{} barcode:{}", uKey, gKey, barcode, e);
            throw BusinessException.dbException("持久化二维码异常");
        }
    }

	@Override
	public CustomPage<MergeApplyRes> pageAllApplyRes(String uname, String mobilePhone, PageScope pageScope) throws BusinessException {
		try {
			if(StringUtils.isNotBlank(uname)){
				uname = UserUtil.encodeName(uname);
			}
			Page<MergeApplyRes> page = applyResMapper.queryAllApplayRes(uname, mobilePhone, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
			return new CustomPage<MergeApplyRes>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
		} catch (Exception e) {
			 logger.error("pageAllApplyRes -> uname:{},mobilePhone:{},e:{}", uname, mobilePhone, e);
	         throw BusinessException.dbException("申请条码记录查询异常");
		}
	}
	
	@Override
	public CustomPage<MergeBarcode> pageBarcodeToCompany(BarcodeSearch barcode,PageScope pageScope)
			throws BusinessException, ParameterException {
		try {
			String startNum = barcode.getStartNum();
			String endNum = barcode.getEndNum();
			Collection<Long> companyIds = null;
			Example example = new Example(Barcode.class);
			Example.Criteria criteria = example.createCriteria();
			if(StringUtils.isNotBlank(startNum) && StringUtils.isNotBlank(endNum)){
				criteria.andBetween("barcode", startNum, endNum);
				Collection<Barcode> collection = barcodeMapper.selectByExample(example);
				companyIds = collection.parallelStream().map(Barcode::getCompanyId).collect(Collectors.toList());
			}else{
				if(StringUtils.isNotBlank(startNum) || StringUtils.isNotBlank(endNum)){
					if(StringUtils.isNotBlank(startNum)){
						criteria.andBetween("barcode", startNum, startNum);
					}
					if(StringUtils.isNotBlank(endNum)){
						criteria.andBetween("barcode", endNum, endNum);
					}
					Collection<Barcode> collection = barcodeMapper.selectByExample(example);
					companyIds = collection.parallelStream().map(Barcode::getCompanyId).collect(Collectors.toList());
				}
			}
			if(CollectionUtils.isNotEmpty(companyIds)){
				barcode.setCompanyIds(companyIds);
			}
			Page<MergeBarcode> page = barcodeMapper.queryBarcodeToCompany(barcode, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
			return new CustomPage<MergeBarcode>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
		} catch (Exception e) {
			 logger.error("pageBarcodeToCompany -> barcode:{},pageScope:{},e:{}", barcode, pageScope, e);
	         throw BusinessException.dbException("所属企业条码列表查询异常");
		}
	}
	
	@Override
	public MergeApplyRes queryTotalCountByGroupId(Integer gKey) throws ParameterException, BusinessException {
		Assert.notBlank(gKey, "项目组编码不能为空");
		try {
			Map<String, Number> map = barcodeMapper.countByGroup(gKey);
			if(null !=map) {
				Integer number = map.get("useCount")!=null ? map.get("useCount").intValue() : 0;
	            Integer useCount = map.get("number")!= null ? map.get("number").intValue(): 0;
	            Integer totalNum = map.get("totalNum")!= null ? map.get("totalNum").intValue() : 0;
	            return new MergeApplyRes(totalNum,useCount,number);
			}
		} catch (Exception e) {
			logger.error("queryTotalCountByGroupId -> gKey:{},e:{}", gKey, e);
			throw BusinessException.dbException("查询项目组可用条码异常");
		}
		return null;
	}
	
	@Override
	public List<Barcode> queryOneBarcodeByGroupId(Integer gKey) throws ParameterException, BusinessException {
		Assert.notBlank(gKey, "项目组不能为空");
		try {
			return barcodeMapper.queryOneBarcodeByGroupId(gKey);
		} catch (Exception e) {
			logger.error("queryOneBarcodeByGroupId=======> gKey:{},e:{}",gKey,e);
			throw BusinessException.dbException("查询");
		}
	}

}
