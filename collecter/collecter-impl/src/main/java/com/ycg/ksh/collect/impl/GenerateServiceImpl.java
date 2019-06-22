package com.ycg.ksh.collect.impl;

import com.ycg.ksh.collect.jdbc.CollectJdbcTemplate;
import com.ycg.ksh.collect.jdbc.ServiceJdbcTemplate;
import com.ycg.ksh.collect.support.generate.BarCodeBuilder;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.EmployeeType;
import com.ycg.ksh.entity.collecter.GenerateBarcode;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.persistent.enterprise.EmployeeCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  生成某些数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */
@Service("ksh.collect.generateService")
public class GenerateServiceImpl implements RabbitMessageListener {

    private final Logger logger = LoggerFactory.getLogger(GenerateServiceImpl.class);

    @Resource
    ServiceJdbcTemplate serviceJdbcTemplate;
    @Resource
    CollectJdbcTemplate collectJdbcTemplate;


    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        try {
            if (StringUtils.equalsIgnoreCase(messageType, QueueKeys.MESSAGE_TYPE_GENERATE_BARCODE)) {
                ApplyRes applyRes = serviceJdbcTemplate.getApplyRes(Integer.parseInt(object.toString()));
                if(applyRes != null && Constant.CODE_RES_READY  == applyRes.getPrintStatus()){
                    generateBarcode(applyRes);
                }
            }
            return true;
        } catch (Exception ex) {
            throw new BusinessException("处理"+ messageType +"消息异常", ex);
        }
    }

    @Transactional
    void generateBarcode(ApplyRes applyRes) throws BusinessException{
        try {
            long day = BarCodeBuilder.day(System.currentTimeMillis());
            GenerateBarcode generate = collectJdbcTemplate.getGenerateBarcode(day);
            long maxCode = generate.getMaxCode();
            Date ctime = new Date();
            BarCodeBuilder builder = new BarCodeBuilder(day, maxCode);
            Collection<Long> collection = builder.build(applyRes.getNumber());
            String batch = builder.batch();
            Collection<Barcode> results = collection.stream().map(c->{
                Barcode barcode = new Barcode();
                barcode.setBarcode(String.valueOf(c));
                barcode.setUserid(applyRes.getUserid());
                barcode.setBindstatus(Constant.BARCODE_STATUS_NO);
                barcode.setGroupid(Optional.ofNullable(applyRes.getGroupid()).orElse(0));
                barcode.setCreatetime(ctime);
                barcode.setCodeBatch(batch);
                barcode.setResourceid(applyRes.getId());
                barcode.setCompanyId(Optional.ofNullable(applyRes.getCompanyId()).orElse(0L));
                return barcode;
            }).collect(Collectors.toList());

            generate.setMaxCode(builder.getMaxCode());
            generate.setTotalCount(generate.getTotalCount() + builder.getCount());

            if(!collectJdbcTemplate.modifyGenerateBarcode(generate, maxCode)){
                throw new BusinessException("条码生成没有成功-> "+ applyRes.toString());
            }
            applyRes.setStartNum(builder.minCode());
            applyRes.setEndNum(builder.maxCode());
            applyRes.setPrintStatus(Constant.CODE_RES_BUILDED);
            if(!serviceJdbcTemplate.modifyAppRes(applyRes, Constant.CODE_RES_READY)){
                throw new BusinessException("条码生成没有成功-> "+ applyRes.toString());
            }
            serviceJdbcTemplate.barcode(results);
            logger.info("生成条码 {} -> {} - {}", day,  builder.getMinCode(), builder.getMaxCode());
        } catch (Exception e) {
            applyRes.setPrintStatus(Constant.CODE_RES_APPLY);
            serviceJdbcTemplate.modifyAppRes(applyRes, Constant.CODE_RES_READY);
            throw new BusinessException("条码生成没有成功", e);
        }
    }
}
