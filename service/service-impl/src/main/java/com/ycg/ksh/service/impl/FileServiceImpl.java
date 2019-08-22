package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.template.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 文件服务实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
@Service("ksh.core.service.fileService")
public class FileServiceImpl implements FileService {

    @Resource
    WaybillService waybillService;
    @Resource
    CustomerService customerService;
    @Resource
    OrderService orderService;
    @Resource
    TemplateService templateService;
    @Resource
    MoutaiService moutaiService;
    @Resource
    DepotOutboundService depotOutboundService;

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 09:19:13
     * @see com.ycg.ksh.service.api.WaybillService#saveByFile(java.lang.Integer, java.lang.Integer, com.ycg.ksh.common.entity.FileEntity)
     * <p>
     */
	@Override
    public FileEntity saveByFile(Integer uKey, Integer gKey, Integer sendKey, FileEntity fileEntity) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "操作人编号不能为空");
        Assert.notNull(fileEntity, "需要一个文件");
        FileEntity resultEntity = new FileEntity();
        try {
            logger.debug("开始处理文件 -> {}", fileEntity);
            if (gKey == null) {
                gKey = 0;
            }
            ConvertObjectByExcel<MergeWaybill> converter = null;
            Customer customer = null;
            if (17 == gKey) {//金发项目组
                Assert.notNull(sendKey, "请选择发货方信息");
                customer = customerService.queryByKey(sendKey);
                if (customer == null) {
                    throw new ParameterException("选择的发货方信息不存在");
                }
                converter = new JinFaTemplate();
            } else {
                converter = new DefaultTemplate();
            }
            if (converter.readExcel(fileEntity).convert().have()) {
                waybillService.saves(uKey, gKey, customer, converter.objects().values());
            }
            if (CollectionUtils.isNotEmpty(converter.exceptions())) {
                FileEntity exception = converter.writeExcel();
                if (exception != null) {
                    resultEntity = exception;
                }
            }
            resultEntity.modify(converter.getSuccessCount(), converter.getFailureCount());
        } catch (Exception e) {
            logger.error("文件处理异常 -> {}", fileEntity, e);
        }
        return resultEntity;
    }

    /**
     * 通过模板文件批量保存数据
     *
     * @param uKey        操作人编号
     * @param templateKey 模板编号
     * @param fileEntity  数据文件
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public FileEntity saveByTemplateFile(Integer uKey, PartnerType partnerType, Long templateKey, FileEntity fileEntity) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "操作人编号不能为空");
        Assert.notNull(fileEntity, "需要一个文件");
        try {
            logger.info("开始处理文件 -> {}", fileEntity);

            fileEntity = templateService.importByFile(new TemplateContext(uKey, partnerType, templateKey), fileEntity);

            logger.info("文件处理完成 -> {}", fileEntity);

            return fileEntity;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("文件处理异常 -> {}", fileEntity, e);
            throw new BusinessException("文件处理异常", e);
        }
    }

    /**
     * 茅台配送单导入
     *
     * @param uKey       操作用户编号
     * @param conveyKey  承运单位
     * @param fileEntity 文件信息
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public FileEntity saveOrderByFile(Integer uKey, Long conveyKey, FileEntity fileEntity) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "操作人编号不能为空");
        Assert.notNull(conveyKey, "承运单位编号不能为空");
        if (conveyKey < 1)
            throw new ParameterException("承运单位标号不能为空");
        Assert.notNull(fileEntity, "需要一个文件");
        FileEntity resultEntity = new FileEntity();
        try {
            logger.debug("开始处理文件 -> {}", fileEntity);
            ConvertObjectByExcel<Order> converter = new MTOrderTemplate();
            if (converter.readExcel(fileEntity).convert().have()) {
                Map<String, String> exceptions = moutaiService.saveOrders(uKey, conveyKey, converter.objects());
                if (exceptions != null && !exceptions.isEmpty()) {
                    converter.exceptions(exceptions);
                }
            }
            if (CollectionUtils.isNotEmpty(converter.exceptions())) {
                FileEntity exception = converter.writeExcel();
                if (exception != null) {
                    resultEntity = exception;
                }
            }
            resultEntity.modify(converter.getSuccessCount(), converter.getFailureCount());
            logger.debug("文件处理完成 -> {}", resultEntity);
        } catch (Exception e) {
            logger.error("文件处理异常 -> {}", fileEntity, e);
        }
        return resultEntity;
    }

    /**
     * 茅台导入客户
     *
     * @param uKey
     * @param fileEntity
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public FileEntity saveCustomerByFile(Integer uKey, FileEntity fileEntity) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "操作人编号不能为空");
        Assert.notNull(fileEntity, "需要一个文件");
        FileEntity resultEntity = new FileEntity();
        try {
            logger.debug("开始处理文件 -> {}", fileEntity);
            ConvertObjectByExcel<com.ycg.ksh.entity.persistent.moutai.Customer> converter = new MTCustomerTemplate();
            if (converter.readExcel(fileEntity).convert().have()) {
                Map<String, String> exceptions = moutaiService.saveCustomers(uKey, converter.objects());
                if (exceptions != null && !exceptions.isEmpty()) {
                    converter.exceptions(exceptions);
                }
            }
            if (CollectionUtils.isNotEmpty(converter.exceptions())) {
                FileEntity exception = converter.writeExcel();
                if (exception != null) {
                    resultEntity = exception;
                }
            }
            resultEntity.modify(converter.getSuccessCount(), converter.getFailureCount());
            logger.debug("文件处理完成 -> {}", resultEntity);
        } catch (Exception e) {
            logger.error("文件处理异常 -> {}", fileEntity, e);
        }
        return resultEntity;
    }

    @Override
    public FileEntity saveInboundOrder(Integer uKey, FileEntity fileEntity) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "操作人编号不能为空");
        Assert.notNull(fileEntity, "需要一个文件");
        FileEntity resultEntity = new FileEntity();
        try {
            logger.debug("开始处理文件 -> {}", fileEntity);
            ConvertObjectByExcel<InboundOrder> converter = new InboundOrderTemplate();
            if (converter.readExcel(fileEntity).convert().have()) {
                depotOutboundService.saveInboundOrders(uKey, converter.objects());
            }
            if (CollectionUtils.isNotEmpty(converter.exceptions())) {
                FileEntity exception = converter.writeExcel();
                if (exception != null) {
                    resultEntity = exception;
                }
            }
            resultEntity.modify(converter.getSuccessCount(), converter.getFailureCount());
            logger.debug("文件处理完成 -> {}", resultEntity);
        } catch (Exception e) {
            logger.error("文件处理异常 -> {}", fileEntity, e);
        }
        return resultEntity;
    }
}
