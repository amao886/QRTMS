package com.ycg.ksh.service.api;

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件服务(电子表格导入等)
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
public interface FileService {

    final Logger logger = LoggerFactory.getLogger(FileService.class);

    /**
     * 通过模板文件批量保存数据
     *
     * @param uKey        操作人编号
     * @param partnerType 角色
     * @param templateKey 模板编号
     * @param fileEntity  数据文件
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    FileEntity saveByTemplateFile(Integer uKey, PartnerType partnerType, Long templateKey, FileEntity fileEntity) throws ParameterException, BusinessException;

    /**
     * 从文件中保存运单信息
     * <p>
     *
     * @param uKey       操作人ID
     * @param gKey       项目组ID
     * @param sendKey    发货方编号
     * @param fileEntity 文件信息
     * @return 异常的数据文件
     * @throws ParameterException
     * @throws BusinessException
     */
    FileEntity saveByFile(Integer uKey, Integer gKey, Integer sendKey, FileEntity fileEntity) throws ParameterException, BusinessException;


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
    FileEntity saveOrderByFile(Integer uKey, Long conveyKey, FileEntity fileEntity) throws ParameterException, BusinessException;

    /**
     * 茅台导入客户
     *
     * @param uKey
     * @param fileEntity
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    FileEntity saveCustomerByFile(Integer uKey, FileEntity fileEntity) throws ParameterException, BusinessException;

    /**
     * 功能描述: 仓库管理导入入库单
     *
     * @param uKey
     * @param fileEntity
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @auther: wangke
     * @date: 2018/9/5 11:19
     */
    FileEntity saveInboundOrder(Integer uKey, FileEntity fileEntity) throws ParameterException, BusinessException;

}
