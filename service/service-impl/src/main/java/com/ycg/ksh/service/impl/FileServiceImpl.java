package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.Leadtime;
import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.persistence.CustomerMapper;
import com.ycg.ksh.service.persistence.LeadtimeMapper;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.template.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
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
    
    @Resource
    CustomerMapper customerMapper;
    @Resource
    LeadtimeMapper leadtimeMapper;

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
                waybillService.saves(uKey, gKey, customer, checkList(converter.objects().values()));
            }
            if (CollectionUtils.isNotEmpty(converter.exceptions())) {
                FileEntity exception = converter.writeExcel();
                if (exception != null) {
                    resultEntity = exception;
                }
            }
            resultEntity.modify(converter.getSuccessCount(), converter.getFailureCount());
        }catch (ParameterException | BusinessException e) {
        	throw e;
		} catch (Exception e) {
            logger.error("文件处理异常 -> {}", fileEntity, e);
        }
        return resultEntity;
    }
	
	/**
	 * 自动匹逻辑校验
	 * @param values 需要优化
	 */
    private Collection<MergeWaybill> checkList(Collection<MergeWaybill> values) throws BusinessException{
    	Collection<MergeWaybill> list = null;
    	if(CollectionUtils.isNotEmpty(values)) {
    		 list = new ArrayList<MergeWaybill>(values.size());
    	}
    	for (MergeWaybill mergeWaybill : values) {
    		//如果导入的发货人客户编码不为空查询数据库中对应的发货人信息，否则直接获取excel中数据
    		if(StringUtils.isNotBlank(mergeWaybill.getShipperCode())) {
    			Customer customerS = customerMapper.queryCustomerByCode(mergeWaybill.getShipperCode());
    			if(customerS == null) {
    				list.clear();
    				throw new BusinessException("【"+ mergeWaybill.getDeliveryNumber()+"】未匹配到对应的发货信息");
    			}
    			mergeWaybill.setShipperName(customerS.getCompanyName());
    			mergeWaybill.setShipperAddress(customerS.getFullAddress());
    			mergeWaybill.setShipperContactName(customerS.getContacts());;
    			mergeWaybill.setShipperContactTel(customerS.getContactNumber());
    			mergeWaybill.setStartStation(RegionUtils.merge(customerS.getProvince(), customerS.getCity(), customerS.getDistrict()));
    			mergeWaybill.setSimpleStartStation(RegionUtils.simple(customerS.getProvince(), customerS.getCity(), customerS.getDistrict()));
    			
    		}
    		//如果导入的收货人客户编码不为空查询数据库中对应的收货人信息，否则直接获取excel中数据
    		if(StringUtils.isNotBlank(mergeWaybill.getReceiverCode())) {
    			Customer customerR = customerMapper.queryCustomerByCode(mergeWaybill.getReceiverCode());
    			if(customerR == null) {
    				list.clear();
    				throw new BusinessException("【"+ mergeWaybill.getDeliveryNumber()+"】未匹配到对应的收货信息");
    			}
    			mergeWaybill.setReceiverName(customerR.getCompanyName());
    			mergeWaybill.setReceiveAddress(customerR.getFullAddress());
    			mergeWaybill.setContactName(customerR.getContacts());
    			mergeWaybill.setContactPhone(customerR.getContactNumber());
    			mergeWaybill.setEndStation(RegionUtils.merge(customerR.getProvince(), customerR.getCity(), customerR.getDistrict()));
    			mergeWaybill.setSimpleEndStation(RegionUtils.simple(customerR.getProvince(), customerR.getCity(), customerR.getDistrict()));
    		}
    		if(mergeWaybill.getArriveDay() == null || mergeWaybill.getArriveHour() == null) {
    			String s = (mergeWaybill.getSimpleStartStation()==null?RegionUtils.simple(RegionUtils.analyze(mergeWaybill.getShipperAddress())):mergeWaybill.getSimpleStartStation()).replaceAll("市", "");
    			String e = (mergeWaybill.getSimpleEndStation()==null?RegionUtils.simple(RegionUtils.analyze(mergeWaybill.getReceiveAddress())):mergeWaybill.getSimpleEndStation()).replaceAll("市", "");
    			if(StringUtils.isNotBlank(s) && StringUtils.isNotBlank(e)) {
    				Leadtime leadtime = leadtimeMapper.quseryByShipCityAndDesCity(s, e);
    				if(leadtime==null){
    					list.clear();
    					throw new BusinessException("【"+s+"】至【"+e+"】,送货单是【"+ mergeWaybill.getDeliveryNumber()+"】的运单未匹配到对应的运输时效");
    				}
    				mergeWaybill.setArriveDay(leadtime.getLt());
    				mergeWaybill.setArriveHour(21);
    			}
    		}
			list.add(mergeWaybill);
		}
    	return list;
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
