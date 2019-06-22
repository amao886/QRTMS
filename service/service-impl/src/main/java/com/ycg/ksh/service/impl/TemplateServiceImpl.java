package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */

import com.ycg.ksh.entity.common.constant.Constants;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.exception.TMCException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.TemplateDataType;
import com.ycg.ksh.constant.TemplateMergeType;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.enterprise.ImportTemplate;
import com.ycg.ksh.entity.persistent.enterprise.TemplateDetail;
import com.ycg.ksh.entity.persistent.enterprise.TemplateProperty;
import com.ycg.ksh.entity.service.enterprise.PropertyDescribe;
import com.ycg.ksh.entity.service.enterprise.TemplateAlliance;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.entity.service.enterprise.TemplateDescribe;
import com.ycg.ksh.service.persistence.enterprise.ImportTemplateMapper;
import com.ycg.ksh.service.persistence.enterprise.TemplateDetailMapper;
import com.ycg.ksh.service.persistence.enterprise.TemplatePropertyMapper;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.TemplateService;
import com.ycg.ksh.service.observer.TemplateObserverAdapter;
import com.ycg.ksh.service.support.excel.ExceptionObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单模板相关逻辑
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
@Service("ksh.core.service.templateService")
public class TemplateServiceImpl implements TemplateService {

    @Resource
    CacheManager cacheManager;
    @Resource
    CompanyService companyService;


    @Resource
    ImportTemplateMapper templateMapper;
    @Resource
    TemplateDetailMapper templateDetailMapper;
    @Resource
    TemplatePropertyMapper templatePropertyMapper;


    @Autowired(required = false)
    Collection<TemplateObserverAdapter> observers;


    private TemplateAlliance buildTemplateAlliance(Long templateKey) throws Exception {
        TemplateAlliance templateAlliance = null;
        ImportTemplate template = templateMapper.selectByPrimaryKey(templateKey);
        if (null != template) {
            templateAlliance = new TemplateAlliance(template);
            templateAlliance.setDescribes(listTemplateDescribe(templateKey));
            templateAlliance.setEmptyColumns(StringUtils.integerArray(template.getEmptyColumn()));
        }
        return templateAlliance;
    }

    @Override
    public Collection<TemplateDescribe> listTemplateDescribe(Long templateKey) throws ParameterException, BusinessException {
        Example example = new Example(TemplateDetail.class);
        example.createCriteria().andEqualTo("templateKey", templateKey);
        return Optional.ofNullable(templateDetailMapper.selectByExample(example)).map(list -> {
            return list.stream().map(d -> {
                if (d.getPropertyKey() != null && d.getPropertyKey() > 0) {//必填或者选填字段
                    TemplateProperty property = templatePropertyMapper.selectByPrimaryKey(d.getPropertyKey());
                    return new TemplateDescribe(property, d.getKey(), d.getPositionIndex(), d.getIsUnique(), d.isRequired());
                } else {//自定义字段
                    return new TemplateDescribe(d.getKey(), d.getCustomName(), d.getDescription(), d.getPositionIndex(), d.getIsUnique(), d.isRequired());
                }
            }).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }

    /**
     * 查询模板可配置字段基本数据(缓存一天)
     *
     * @param category 模板类别
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public Collection<TemplateProperty> listTemplateProperty(Integer category) throws ParameterException, BusinessException {
        return templatePropertyMapper.select(new TemplateProperty(category));
        /*
        Collection<TemplateProperty> collection = null;

        try {
            Object object = cacheManager.get(TemplateProperty.class.getName());
            if (null == object) {
                collection = templatePropertyMapper.selectAll();
                if (!collection.isEmpty()) {
                    cacheManager.set(TemplateProperty.class.getName(), collection, 1L, TimeUnit.DAYS);
                }
            } else {
                collection = (Collection<TemplateProperty>) object;
            }
            return collection;
        } catch (Exception e) {
            throw BusinessException.dbException("查询模板可配置字段异常");
        }
        */
    }

    /**
     * 查询当前用户所属公司的模板数据
     *
     * @param uKey 操作用户编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public Collection<ImportTemplate> listTemplates(Integer uKey, Integer category) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            Example example = new Example(ImportTemplate.class);
            example.createCriteria().andEqualTo("companyKey", company.getId()).andEqualTo("category", category);
            example.orderBy("fettle").desc().orderBy("createTime").desc();
            return templateMapper.selectByExample(example);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.dbException("查询当前用户所属企业模板数据异常");
        }
    }

    /**
     * 根据模板编号查询模板详细数据
     *
     * @param templateKey 模板编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public TemplateAlliance allianceTemplate(Long templateKey) throws ParameterException, BusinessException {
        Assert.notBlank(templateKey, "模板编号不能为空");
        try {
            return buildTemplateAlliance(templateKey);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.dbException("查询模板详情异常");
        }
    }

    /**
     * 新增模板
     *
     * @param uKey            操作用户编号
     * @param templateDetails 模板详细配置
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public Long saveTemplate(Integer uKey, ImportTemplate template, Collection<TemplateDetail> templateDetails) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notNull(template, "模板信息不能为空");
        Assert.notBlank(template.getName(), "模板名称不能为空");
        Assert.notEmpty(templateDetails, "模板必填字段配置不能为空");

        Company company = companyService.assertCompanyByUserKey(uKey);
        try {
            template.setUserKey(uKey);
            template.setKey(Globallys.nextKey());
            template.setCompanyKey(company.getId());
            template.setCreateTime(new Date());
            template.setFettle(CoreConstants.TEMPLATE_FETTLE_DEFAULT);
            template.setUpdateTime(template.getCreateTime());
            template.setStartColumn(0);
            template.setStartRow(2);
            //默认是发货模板
            template.setCategory(Optional.ofNullable(template.getCategory()).orElse(Constants.TEMPLATE_CATEGORY_PLAN));

            TemplateMergeType mergeType = TemplateMergeType.convert(template.getMergeType());
            int index = 0;
            StringBuilder builder = new StringBuilder();
            for (Iterator<TemplateDetail> iterator = templateDetails.iterator(); iterator.hasNext(); ) {
                TemplateDetail templateDetail = iterator.next();
                if (emptyColumn(templateDetail)) {//空列
                    builder.append(index).append("#");
                    iterator.remove();
                } else {
                    templateDetail.setPositionIndex(index);
                    templateDetail.setKey(Globallys.nextKey());
                    templateDetail.setTemplateKey(template.getKey());
                    templateDetail.setIsUnique(mergeType.unique(template.getCategory(), templateDetail.getPropertyKey()));
                }
                index++;
            }
            if (builder.length() > 0) {
                template.setEmptyColumn(builder.deleteCharAt(builder.length() - 1).toString());
            }
            templateMapper.insert(template);
            templateDetailMapper.inserts(templateDetails);
            return template.getKey();
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("新增模板异常uKey:{} template:{} templateDetails:{}", uKey, template, templateDetails);
            throw BusinessException.dbException("新增模板异常");
        }
    }

    private boolean emptyColumn(TemplateDetail detail) {
        return Optional.ofNullable(detail.getPropertyKey()).orElse(0L) <= 0 && StringUtils.isBlank(detail.getCustomName());
    }

    /**
     * 修改模板
     *
     * @param uKey            操作用户编号
     * @param templateDetails 模板详细配置
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public void updateTemplate(Integer uKey, ImportTemplate template, Collection<TemplateDetail> templateDetails) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notNull(template, "模板信息不能为空");
        Assert.notBlank(template.getName(), "模板名称不能为空");
        Assert.notBlank(template.getKey(), "模板编号不能为空");
        Assert.notEmpty(templateDetails, "模板必填字段配置不能为空");
        try {
            ImportTemplate importTemplate = templateMapper.selectByPrimaryKey(template.getKey());
            if (null == importTemplate) {
                throw new BusinessException("当前模板信息不存在");
            }
            importTemplate.setUpdateTime(new Date());
            importTemplate.setName(template.getName());
            importTemplate.setMergeType(template.getMergeType());
            StringBuilder builder = new StringBuilder();
            if (CollectionUtils.isNotEmpty(templateDetails)) {
                TemplateMergeType mergeType = TemplateMergeType.convert(template.getMergeType());
                Collection<Long> templateIds = templateDetailMapper.listIdsByTemplateId(importTemplate.getKey());
                int index = 0;
                for (TemplateDetail templateDetail : templateDetails) {
                    if (emptyColumn(templateDetail)) {//空列
                        builder.append(index).append("#");
                    } else {
                        templateDetail.setPositionIndex(index);
                        if (Optional.ofNullable(templateDetail.getKey()).orElse(0L) > 0) {
                            templateIds.remove(templateDetail.getKey());
                            templateDetail.setIsUnique(mergeType.unique(importTemplate.getCategory(), templateDetail.getPropertyKey()));
                            templateDetailMapper.updateByPrimaryKeySelective(templateDetail);
                        } else {
                            templateDetail.setKey(Globallys.nextKey());
                            templateDetail.setTemplateKey(importTemplate.getKey());
                            templateDetail.setIsUnique(mergeType.unique(importTemplate.getCategory(), templateDetail.getPropertyKey()));
                            templateDetailMapper.insertSelective(templateDetail);
                        }
                    }
                    index++;
                }
                if (CollectionUtils.isNotEmpty(templateIds)) {
                    templateDetailMapper.deleteTemplate(templateIds);
                }
                if (builder.length() > 0) {
                    importTemplate.setEmptyColumn(builder.deleteCharAt(builder.length() - 1).toString());
                } else {
                    importTemplate.setEmptyColumn(StringUtils.EMPTY);
                }
            }
            templateMapper.updateByPrimaryKey(importTemplate);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("模板更新异常 uKey:{} template:{} templateDetails:{}", uKey, template, templateDetails);
            throw new BusinessException("模板更新异常", e);
        }


    }

    /**
     * 删除模板
     *
     * @param uKey        操作用户编号
     * @param templateKey 要删除得模板编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public void deleteTemplate(Integer uKey, Long templateKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notBlank(templateKey, "模板编号不能为空");
        try {
            Company company = companyService.assertCompanyByUserKey(uKey);
            Example example = new Example(TemplateDetail.class);
            example.createCriteria().andEqualTo("templateKey", templateKey);
            templateDetailMapper.deleteByExample(example);
            templateMapper.deleteByPrimaryKey(templateKey);
        } catch (Exception e) {
            throw BusinessException.dbException("删除用户所属企业模板异常");
        }
    }

    /**
     * 修改模板得状态(如果当前是常用状态则修改为不常用；如果当前为不常用则修改为常用)
     *
     * @param uKey        操作用户编号
     * @param templateKey 模板编号
     * @return 返回模板最新状态
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public int modifyTemplateFettle(Integer uKey, Long templateKey) throws ParameterException, BusinessException {
        //如果当前是常用状态则修改为不常用；如果当前为不常用则修改为常用
        //CoreConstants.TEMPLATE_FETTLE_COMMONLY;
        //CoreConstants.TEMPLATE_FETTLE_DEFAULT;
        ImportTemplate importTemplate = templateMapper.selectByPrimaryKey(templateKey);
        if (null == importTemplate) {
            throw new BusinessException("当前模板信息不存在");
        }
        try {
            //设置为普通
            Example example = new Example(ImportTemplate.class);
            example.createCriteria().andEqualTo("companyKey", importTemplate.getCompanyKey()).andEqualTo("category", importTemplate.getCategory());
            templateMapper.updateByExampleSelective(new ImportTemplate(CoreConstants.TEMPLATE_FETTLE_DEFAULT), example);

            //设置常用
            if (importTemplate.getFettle() != null && importTemplate.getFettle() <= 1) {
                importTemplate.setFettle(CoreConstants.TEMPLATE_FETTLE_COMMONLY);
            } else {
                importTemplate.setFettle(CoreConstants.TEMPLATE_FETTLE_DEFAULT);
            }
            importTemplate.setUpdateTime(new Date());
            templateMapper.updateByPrimaryKeySelective(importTemplate);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("修改模板状态异常 uKey:{} templateKey:{}", uKey, templateKey);
            throw BusinessException.dbException("修改模板状态异常");
        }

        return importTemplate.getFettle();
    }

    /**
     * 指定模板导入数据
     *
     * @param context    模板编号
     * @param fileEntity 文件
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     * @TODO 此方法有大事务的问题
     */
    @Override
    public FileEntity importByFile(TemplateContext context, FileEntity fileEntity) throws ParameterException, BusinessException {
        try {
            if (context.getCompanyKey() == null || context.getCompanyKey() <= 0) {
                Company company = companyService.assertCompanyByUserKey(context.getOperatorKey());//企业编号,根据uKey获取
                context.setCompanyKey(company.getId());
            }
            context.setAlliance(buildTemplateAlliance(context.getTemplateKey()));
            File excelFile = FileUtils.file(fileEntity.getPath());
            Collection<Object[]> objects = EasyExcelBuilder.readExcel(excelFile, context.getStartRow(), context.getStartColumn() + context.size());
            if (CollectionUtils.isEmpty(objects)) {
                throw new BusinessException("上传的文件中没有可用的数据");
            }
            Map<String, PropertyDescribe> cache = new HashMap<String, PropertyDescribe>();
            Collection<ExceptionObject> exceptionObjects = new ArrayList<ExceptionObject>();
            if (CollectionUtils.isNotEmpty(context.uniques())) {
                for (Map.Entry<String, List<Object[]>> entry : groupingBy(context, objects).entrySet()) {
                    if (StringUtils.isBlank(entry.getKey())) {//合并的key为空是，不合并
                        exceptionObjects.addAll(analyze(context, cache, entry.getValue()));
                    } else {
                        exceptionObjects.addAll(analyzeByGroup(context, cache, entry.getValue()));
                    }
                }
            } else {
                exceptionObjects.addAll(analyze(context, cache, objects));
            }
            fileEntity.modify(objects.size() - exceptionObjects.size(), exceptionObjects.size());
            return writeExceptionObjects(fileEntity, excelFile, context, exceptionObjects);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("数据导入异常", e);
        }
    }

    /**
     * 分组
     *
     * @param context 模板上下文
     * @param objects 数据
     * @return 分组后的数据
     */
    private Map<String, List<Object[]>> groupingBy(TemplateContext context, Collection<Object[]> objects) {
        Map<String, List<Object[]>> collects = objects.stream().collect(Collectors.groupingBy(os -> {
            StringBuilder builder = new StringBuilder();
            for (Integer uniqueIndex : context.uniques()) {
                builder.append(Optional.ofNullable(os[uniqueIndex]).map(Object::toString).orElse(StringUtils.EMPTY));
            }
            return builder.toString();
        }));
        return collects;
    }

    /**
     * 合并分组解析
     *
     * @param context
     * @param cache
     * @param collection
     * @return
     */
    private Collection<ExceptionObject> analyzeByGroup(TemplateContext context, Map<String, PropertyDescribe> cache, Collection<Object[]> collection) {
        Collection<ExceptionObject> exceptionObjects = new ArrayList<ExceptionObject>();
        try {
            Collection<PropertyDescribe> describes = new ArrayList<PropertyDescribe>();
            for (Object[] objects : collection) {
                describes.addAll(analyzeSingleRow(context, cache, objects));
            }
            //触发导入事件
            if (CollectionUtils.isNotEmpty(observers) && CollectionUtils.isNotEmpty(describes)) {
                context.setUniqueKey(Globallys.nextKey());
                for (TemplateObserverAdapter observer : observers) {
                    observer.importSomething(context, describes);
                }
            }
        } catch (Exception e) {
            collection.stream().forEach(os -> {
                String message = e.getMessage();
                if (e instanceof TMCException) {
                    message = ((TMCException) e).getFriendlyMessage();
                }
                exceptionObjects.add(new ExceptionObject(os, message));
            });
        } finally {
            cache.clear();
        }
        return exceptionObjects;
    }

    /**
     * 普通解析，不合并
     *
     * @param context
     * @param cache
     * @param objectCollection
     * @return
     */
    private Collection<ExceptionObject> analyze(TemplateContext context, Map<String, PropertyDescribe> cache, Collection<Object[]> objectCollection) {
        Collection<ExceptionObject> exceptionObjects = new ArrayList<ExceptionObject>();
        for (Object[] objects : objectCollection) {
            try {
                Collection<PropertyDescribe> collection = analyzeSingleRow(context, cache, objects);
                //触发导入事件
                if (CollectionUtils.isNotEmpty(observers)) {
                    context.setUniqueKey(Globallys.nextKey());
                    for (TemplateObserverAdapter observer : observers) {
                        observer.importSomething(context, collection);
                    }
                }
            } catch (Exception e) {
                if (e instanceof TMCException) {
                    exceptionObjects.add(new ExceptionObject(objects, ((TMCException) e).getFriendlyMessage()));
                } else {
                    exceptionObjects.add(new ExceptionObject(objects, e.getMessage()));
                }
            } finally {
                cache.clear();
            }
        }
        return exceptionObjects;
    }

    /**
     * 解析单行数据
     *
     * @param context
     * @param cache
     * @param objects
     * @return
     */
    private Collection<PropertyDescribe> analyzeSingleRow(TemplateContext context, Map<String, PropertyDescribe> cache, Object[] objects) {
        cache.clear();
        for (int i = 0; i < objects.length; i++) {
            TemplateDescribe describe = context.mapping(i);
            if (describe == null) {
                continue;
            }
            PropertyDescribe propertyDescribe = analysis(describe, objects[i], cache.get(describe.getClassName()));
            if (propertyDescribe != null) {
                cache.put(propertyDescribe.getClassName(), propertyDescribe);
            }
        }
        return cache.values();
    }


    /**
     * 准备异常文件并写入异常数据
     *
     * @param fileEntity
     * @param excelFile
     * @param context
     * @param exceptions
     * @return
     */
    public FileEntity writeExceptionObjects(FileEntity fileEntity, File excelFile, TemplateContext context, Collection<ExceptionObject> exceptions) {
        //logger.info("-----> {}", exceptions);
        if (CollectionUtils.isNotEmpty(exceptions)) {
            try {
                File exceptionFile = null;
                fileEntity.setDirectory(SystemUtils.directoryDownload());
                fileEntity.setSuffix(FileUtils.suffix(excelFile.getName()));
                fileEntity.setFileName(FileUtils.appendSuffix(StringUtils.UUID(), fileEntity.getSuffix()));
                exceptionFile = FileUtils.file(fileEntity.getPath());
                FileUtils.copyFile(excelFile, exceptionFile);
                try (ExcelWriter excelWriter = EasyExcelBuilder.createWriteExcel(exceptionFile)) {
                    excelWriter.createSheet(context.getAlliance().getName());
                    excelWriter.clean(context.getStartRow());
                    for (ExceptionObject eObject : exceptions) {
                        for (Object object : eObject.getObjects()) {
                            excelWriter.column(object);
                        }
                        excelWriter.emphasizeColumn(eObject.getMessage());
                        excelWriter.newRow();
                    }
                    excelWriter.write();
                    fileEntity.setCount(exceptions.size());
                    fileEntity.setSize(exceptionFile.length());
                    fileEntity.setAliasName("异常数据-" + context.getAlliance().getName() + "-" + Globallys.next());
                    return fileEntity;
                }
            } catch (Exception e) {
                throw new BusinessException("异常数据保存异常", e);
            }
        }
        return fileEntity;
    }


    private Object removeDecimal(Object object, String message) {
        try {
            return new DecimalFormat("#").format(object);
        } catch (Exception e) {
            throw new BusinessException(message);
        }
    }

    /**
     * 解析单个数据
     *
     * @param describe         模板字段描述
     * @param object           单个值
     * @param propertyDescribe
     * @return
     */
    private PropertyDescribe analysis(TemplateDescribe describe, Object object, PropertyDescribe propertyDescribe) {
        if (describe != null) {
            //如果是必填字段，判断不为空
            if (describe.isRequired()) {
                if (object == null || StringUtils.isBlank(object.toString())) {
                    throw new BusinessException(describe.getName() + "不能为空");
                }
            }
            //如果是字符串，并且是空字符串
            if (object instanceof String && StringUtils.isBlank(object.toString())) {
                object = null;
            }
            //获取需要的数据类型
            TemplateDataType dataType = TemplateDataType.convert(describe.getDataType());
            //如果是整数和小数
            if (dataType.isNumber() || dataType.isDouble()) {
                object = Optional.ofNullable(object).orElse(0);
                if (object instanceof String) {
                    try {
                        object = new Double(StringUtils.replace(object.toString(), ",", ""));//去掉逗号  比如 1,250.235
                    } catch (Exception e) {
                        throw new BusinessException(describe.getName() + "必须是一个合法的数字");
                    }
                }
                if (dataType.isNumber()) {
                    //需要整数，去掉小数
                    object = removeDecimal(object, describe.getName() + "必须是一个合法的数字");
                    if (!Validator.NUMBER.verify(object)) {
                        throw new BusinessException(Validator.NUMBER.getMessage(describe.getName()));
                    }
                }
                if (dataType.isDouble() && !Validator.DECIMAL.verify(object)) {
                    throw new BusinessException(Validator.DECIMAL.getMessage(describe.getName()));
                }
            } else if (object instanceof Number) {
                object = removeDecimal(object, describe.getName() + "必须是一个合法的数字");
            }
            if (object != null && dataType.isDate() && !(object instanceof Date)) {
                //验证日期格式
                if (!Validator.DATE.verify(object)) {
                    throw new BusinessException(Validator.DATE.getMessage(describe.getName()));
                }
            }
            if (dataType.isString()) {
                object = Optional.ofNullable(object).orElse(StringUtils.EMPTY);
            }
            propertyDescribe = Optional.ofNullable(propertyDescribe).orElse(new PropertyDescribe(describe.getClassName(), describe.isCustom()));
            if (describe.isCustom()) {
                propertyDescribe.set(describe.getName(), object);
            } else {
                propertyDescribe.set(describe.getDataKey(), object);
            }
            return propertyDescribe;
        }
        return null;
    }

    /**
     * 生成模板文件
     *
     * @param uKey        操作用户编号
     * @param templateKey 模板编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  业务逻辑异常
     */
    @Override
    public FileEntity buildByTemplate(Integer uKey, Long templateKey) throws ParameterException, BusinessException {
        ExcelWriter excelWriter = null;
        try {
            TemplateAlliance alliance = buildTemplateAlliance(templateKey);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setDirectory(SystemUtils.directoryDownload());
            fileEntity.setSuffix(FileUtils.XLSX_SUFFIX);
            fileEntity.setFileName(FileUtils.appendSuffix(StringUtils.UUID(), fileEntity.getSuffix()));
            File templateFile = FileUtils.file(fileEntity.getPath());
            excelWriter = EasyExcelBuilder.createWriteExcel(templateFile);
            excelWriter.createSheet(alliance.getName());
            excelWriter.title(alliance.getName(), Optional.ofNullable(alliance.getEmptyColumns()).map(e -> e.length).orElse(0) + alliance.getDescribes().size() - 1);
            /*
            if(CollectionUtils.isEmpty(alliance.getDescribes())){
                throw new BusinessException("模板配置不完整,请检查模板配置后再操作");
            }
            */
            if (CollectionUtils.isNotEmpty(alliance.getDescribes())) {
                for (TemplateDescribe describe : alliance.getDescribes()) {
                    excelWriter.column(describe.getPositionIndex(), describe.getName());
                }
            }
            if (ArrayUtils.isNotEmpty(alliance.getEmptyColumns())) {
                for (Integer columnIndex : alliance.getEmptyColumns()) {
                    excelWriter.column(columnIndex, "空白列");
                }
            }
            excelWriter.write();
            fileEntity.setCount(1);
            fileEntity.setSize(templateFile.length());
            fileEntity.setAliasName(alliance.getName());
            return fileEntity;
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("生成模板文件异常", e);
        } finally {
            if (excelWriter != null) {
                try {
                    excelWriter.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void saveWaybillEntry(TemplateContext context, Map<? extends Serializable, Object> maps, List<Map<? extends Serializable, Object>> commodities) throws ParameterException, BusinessException, MessageException {
        Assert.notEmpty(maps, "运单录入信息不能为空");
        Assert.notEmpty(commodities, "货物信息不能为空");
        Company company = companyService.assertCompanyByUserKey(context.getOperatorKey());
        try {
            context.setAlliance(buildTemplateAlliance(context.getTemplateKey()));
            context.setUniqueKey(Globallys.nextKey());
            context.setCompanyKey(company.getId());

            Map<String, PropertyDescribe> describeMaps = new HashMap<String, PropertyDescribe>();
            //转换订单的基本数据
            Collection<PropertyDescribe> describes = new ArrayList<PropertyDescribe>(propertyDescribe(context, maps, describeMaps));
            describeMaps.clear();
            //转换订单明细数据
            if (CollectionUtils.isNotEmpty(commodities)) {
                for (Map<? extends Serializable, Object> goods : commodities) {
                    describes.addAll(propertyDescribe(context, goods, describeMaps));
                    describeMaps.clear();
                }
            }

            if (CollectionUtils.isEmpty(describes)) {
                throw new BusinessException("输入的数据有误");
            }
            //触发导入事件
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setUniqueKey(Globallys.nextKey());
                for (TemplateObserverAdapter observer : observers) {
                    observer.importSomething(context, describes);
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (MessageException mse) {
            throw new BusinessException(mse.getMessage());
        } catch (Exception e) {
            logger.info("运单录入添加异常, context:{}  maps:{} commodities:{}", context, maps, commodities);
            throw new BusinessException("运单录入添加异常", e);
        }
    }


    @Override
    public void saveByTemplateDescribe(TemplateContext context, Collection<TemplateDescribe> describes) throws ParameterException, BusinessException {
        Assert.notEmpty(describes, "模板内容不能为空");
        try {
            if (context.getCompanyKey() == null || context.getCompanyKey() <= 0) {
                Company company = companyService.assertCompanyByUserKey(context.getOperatorKey());
                context.setCompanyKey(company.getId());
            }
            context.setAlliance(buildTemplateAlliance(context.getTemplateKey()));
            context.setUniqueKey(Globallys.nextKey());

            Map<String, PropertyDescribe> cache = new HashMap<String, PropertyDescribe>();
            if (CollectionUtils.isNotEmpty(describes)) {
                for (TemplateDescribe templateDescribe : describes) {
                    propertyDescribe(context, templateDescribe, cache);
                }
            }
            Collection<PropertyDescribe> collection = cache.values();
            //触发导入事件
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setUniqueKey(Globallys.nextKey());
                for (TemplateObserverAdapter observer : observers) {
                    observer.importSomething(context, collection);
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (MessageException mse) {
            throw new BusinessException(mse.getMessage());
        } catch (Exception e) {
            logger.info("运单录入添加异常, context:{}  describes:{}", context, describes);
            throw new BusinessException("运单录入添加异常", e);
        }
    }

    @Override
    public void saveByTemplate(TemplateContext context, Collection<Map<? extends Serializable, Object>> keyValues) throws ParameterException, BusinessException {
        Assert.notEmpty(keyValues, "模板内容不能为空");
        Company company = companyService.assertCompanyByUserKey(context.getOperatorKey());
        try {
            context.setAlliance(buildTemplateAlliance(context.getTemplateKey()));
            context.setUniqueKey(Globallys.nextKey());
            context.setCompanyKey(company.getId());

            Map<String, PropertyDescribe> cache = new HashMap<String, PropertyDescribe>();
            Collection<PropertyDescribe> describes = new ArrayList<PropertyDescribe>();
            if (CollectionUtils.isNotEmpty(keyValues)) {
                for (Map<? extends Serializable, Object> keyValue : keyValues) {
                    describes.addAll(propertyDescribe(context, keyValue, cache));
                    cache.clear();
                }
            }
            if (CollectionUtils.isEmpty(describes)) {
                throw new BusinessException("输入的数据有误");
            }
            //触发导入事件
            if (CollectionUtils.isNotEmpty(observers)) {
                context.setUniqueKey(Globallys.nextKey());
                for (TemplateObserverAdapter observer : observers) {
                    observer.importSomething(context, describes);
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (MessageException mse) {
            throw new BusinessException(mse.getMessage());
        } catch (Exception e) {
            logger.info("运单录入添加异常, context:{}  keyValues:{}", context, keyValues);
            throw new BusinessException("运单录入添加异常", e);
        }
    }

    /**
     * 数据转换
     *
     * @param context
     * @param keyValues
     * @return
     */
    private Collection<PropertyDescribe> propertyDescribe(TemplateContext context, Map<? extends Serializable, Object> keyValues) {
        return propertyDescribe(context, keyValues, new HashMap<String, PropertyDescribe>());
    }

    /**
     * 数据转换
     *
     * @param context
     * @param keyValues
     * @param cache
     * @return
     */
    private Collection<PropertyDescribe> propertyDescribe(TemplateContext context, Map<? extends Serializable, Object> keyValues, Map<String, PropertyDescribe> cache) {
        if (CollectionUtils.isNotEmpty(context.describes())) {
            for (Map.Entry<? extends Serializable, Object> object : keyValues.entrySet()) {
                TemplateDescribe describe = context.mappingKey(Long.parseLong(object.getKey().toString()));
                PropertyDescribe propertyDescribe = analysis(describe, object.getValue(), cache.get(describe.getClassName()));
                if (propertyDescribe != null) {
                    cache.put(propertyDescribe.getClassName(), propertyDescribe);
                }
            }
            return cache.values();
        }
        return Collections.emptyList();
    }

    private void propertyDescribe(TemplateContext context, TemplateDescribe templateDescribe, Map<String, PropertyDescribe> cache) {
        if (CollectionUtils.isNotEmpty(context.describes())) {
            TemplateDescribe describe = context.mappingKey(templateDescribe.getDetailKey());
            PropertyDescribe propertyDescribe = analysis(describe, templateDescribe.getDataValue(), cache.get(describe.getClassName()));
            if (propertyDescribe != null) {
                cache.put(propertyDescribe.getClassName(), propertyDescribe);
            }
        }
    }

    /**
     * 数据转换
     *
     * @param context
     * @param collection
     * @return
     */
    private Collection<PropertyDescribe> propertyDescribe(TemplateContext context, Collection<Map<Long, Object>> collection) {
        if (CollectionUtils.isNotEmpty(context.describes()) && CollectionUtils.isNotEmpty(collection)) {
            Map<String, PropertyDescribe> maps = new HashMap<String, PropertyDescribe>();
            for (Map<Long, Object> keyValues : collection) {
                propertyDescribe(context, keyValues, maps);
            }
            return maps.values();
        }
        return null;
    }
}
