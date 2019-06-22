package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.enterprise.ImportTemplate;
import com.ycg.ksh.entity.persistent.enterprise.TemplateDetail;
import com.ycg.ksh.entity.persistent.enterprise.TemplateProperty;
import com.ycg.ksh.entity.service.enterprise.TemplateAlliance;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.entity.service.enterprise.TemplateDescribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 模板
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public interface TemplateService {

    final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    /**
     * 查询模板可配置字段基本数据(缓存一天)
     *
     * @param category 模板类别
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    Collection<TemplateProperty> listTemplateProperty(Integer category) throws ParameterException, BusinessException;

    /**
     * 查询当前用户所属公司的模板数据
     *
     * @param uKey 操作用户编号
     * @param category 模板类型
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    Collection<ImportTemplate> listTemplates(Integer uKey, Integer category) throws ParameterException, BusinessException;

    /**
     * 查询当前用户所属公司的模板数据描述
     *
     * @param templateKey 模板编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    Collection<TemplateDescribe> listTemplateDescribe(Long templateKey) throws ParameterException, BusinessException;

    /**
     * 根据模板编号查询模板详细数据
     *
     * @param templateKey 模板编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    TemplateAlliance allianceTemplate(Long templateKey) throws ParameterException, BusinessException;

    /**
     * 新增模板
     *
     * @param uKey            操作用户编号
     * @param template        模板
     * @param templateDetails 模板详细配置
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    Long saveTemplate(Integer uKey, ImportTemplate template, Collection<TemplateDetail> templateDetails) throws ParameterException, BusinessException;

    /**
     * 修改模板
     *
     * @param uKey            操作用户编号
     * @param template        模板
     * @param templateDetails 模板详细配置
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    void updateTemplate(Integer uKey, ImportTemplate template, Collection<TemplateDetail> templateDetails) throws ParameterException, BusinessException;

    /**
     * 删除模板
     *
     * @param uKey        操作用户编号
     * @param templateKey 要删除得模板编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    void deleteTemplate(Integer uKey, Long templateKey) throws ParameterException, BusinessException;

    /**
     * 修改模板得状态(如果当前是常用状态则修改为不常用；如果当前为不常用则修改为常用)
     *
     * @param uKey        操作用户编号
     * @param templateKey 模板编号
     * @return 返回模板最新状态
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    int modifyTemplateFettle(Integer uKey, Long templateKey) throws ParameterException, BusinessException;

    /**
     * 指定模板导入数据
     *
     * @param context
     * @param fileEntity 文件
     * @return 导入后有异常的数据
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    FileEntity importByFile(TemplateContext context, FileEntity fileEntity) throws ParameterException, BusinessException;

    /**
     * 生成模板文件
     *
     * @param uKey        操作用户编号
     * @param templateKey 模板编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    FileEntity buildByTemplate(Integer uKey, Long templateKey) throws ParameterException, BusinessException;


    /***
     * 运单录入
     * @param templateContext
     * @param maps
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveWaybillEntry(TemplateContext context, Map<? extends Serializable, Object> maps, List<Map<? extends Serializable, Object>> commodities) throws ParameterException, BusinessException, MessageException;

    /**
     * @param context
     * @param keyValues
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveByTemplate(TemplateContext context, Collection<Map<? extends Serializable, Object>> keyValues) throws ParameterException, BusinessException;


    void saveByTemplateDescribe(TemplateContext context, Collection<TemplateDescribe> describes) throws ParameterException, BusinessException;
}
