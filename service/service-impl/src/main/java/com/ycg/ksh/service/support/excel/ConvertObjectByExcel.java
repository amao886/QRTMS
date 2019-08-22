package com.ycg.ksh.service.support.excel;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.excel.EasyExcelBuilder;
import com.ycg.ksh.common.excel.ExcelReader;
import com.ycg.ksh.common.excel.ExcelWriter;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/26
 */
public abstract class ConvertObjectByExcel<T> {
    final Logger logger = LoggerFactory.getLogger(ConvertObjectByExcel.class);

    protected Class<T> clazz;

    protected File excelFile;
    protected Integer startRow;//开始行数
    protected Integer columns;//总列数

    protected Map<String, T> objects;
    protected Collection<ExceptionObject> exceptions;
    protected List<Object[]> source;//读取到的excel 数据
    protected Map<String, List<Object[]>> systematics;

    protected Integer successCount = 0;
    protected Integer failureCount = 0;


    public abstract Collection<ConvertHandler> analyzeObjects();
    public abstract Collection<ConvertHandler> analyzeCommodity();
    public abstract ConvertObjectByExcel<T> readExcel(FileEntity fileEntity) throws Exception;
    public abstract void mergeSomething(T object, Object[] excelObjects) throws Exception;

    public ConvertObjectByExcel() {
        Type genType = this.getClass().getGenericSuperclass();
        if(ParameterizedType.class.isInstance(genType)){
            clazz = (Class<T>) ((ParameterizedType) genType).getActualTypeArguments()[0];
        }
        if(clazz == null){
            throw new BusinessException("不能获取转换对象类型");
        }
    }

    public ConvertObjectByExcel<T> readExcel(FileEntity fileEntity, Integer startRow, Integer columns) throws Exception {
        this.startRow = startRow;
        this.columns = columns;
        excelFile = FileUtils.file(fileEntity.getPath());
        try (ExcelReader excelReader = EasyExcelBuilder.createReadExcel(excelFile)) {
            excelReader.setIndex(startRow, columns);
            source = excelReader.readExcel();
        }
        return this;
    }

    public boolean have() {
        if(objects != null && !objects.isEmpty()) {
            return true;
        }
        return false;
    }

    protected void addException(ExceptionObject exceptionObject) {
        exceptions = Optional.ofNullable(exceptions).orElseGet(() -> {
            return new ArrayList<ExceptionObject>();
        });
        exceptions.add(exceptionObject);
        failureCount = failureCount + 1;
    }


    public FileEntity writeExcel() throws Exception {
        if(CollectionUtils.isNotEmpty(exceptions)) {
            File exceptionFile = null;
            FileEntity fileEntity = new FileEntity();
            fileEntity.setDirectory(SystemUtils.directoryDownload());
            fileEntity.setSuffix(FileUtils.suffix(excelFile.getName()));
            fileEntity.setFileName(FileUtils.appendSuffix(StringUtils.UUID(), fileEntity.getSuffix()));
            exceptionFile = FileUtils.file(fileEntity.getPath());
            FileUtils.copyFile(excelFile, exceptionFile);
            try(ExcelWriter excelWriter = EasyExcelBuilder.createWriteExcel(exceptionFile)){
                excelWriter.createSheet("导入异常数据");
                excelWriter.clean(startRow);
                for (ExceptionObject eObject : exceptions) {
                    for (Object object : eObject.getObjects()) {
                        excelWriter.column(object);
                    }
                    excelWriter.column(eObject.getMessage());
                    excelWriter.newRow();
                }
                excelWriter.write();
                fileEntity.setCount(exceptions.size());
                fileEntity.setSize(exceptionFile.length());
                return fileEntity;
            }
        }
        return null;
    }

    public ConvertObjectByExcel<T> convert() {
        if(CollectionUtils.isNotEmpty(source)) {
            convert(0, source.size());
        }
        return this;
    }

    public void convert(int start, int end) {
        for (int i = start; i < end; i++) {
            Object[] objects = source.get(i);
            try {
                if(validateEmpty(objects)) {//空白行
                    continue;
                }
                ExceptionObject exception = validate(objects);
                if(exception == null) {
                    convert(objects);
                }else {
                    addException(exception);
                }
            } catch (Exception e) {
                addException(new ExceptionObject(objects, e.getMessage()));
            }
        }
    }

    protected void appendMessage(StringBuilder builder, String message){
        if(StringUtils.isNotBlank(message)){
            if(builder.length() > 0 ){
                builder.append(",");
            }
            builder.append(message);
        }
    }

    private ExceptionObject validate(Object[] objects) {
        StringBuilder builder = new StringBuilder();
        for (ConvertHandler handler : analyzeObjects()) {
            appendMessage(builder, handler.validate(objects));
        }
        for (ConvertHandler handler : analyzeCommodity()) {
            appendMessage(builder, handler.validate(objects));
        }
        if(builder.length() > 0) {
            return new ExceptionObject(objects, builder.toString());
        }
        return null;
    }

    protected boolean validateEmpty(Object[] values) {
        boolean empty = true;
        for (ConvertHandler handler : analyzeObjects()) {
            if(Validator.NOTBLANK.verify(handler.getValue(values))) {
                empty = false;
                break;
            }
        }
        return empty;
    }


    protected void convert(Object[] excelObjects) throws Exception {
        objects = Optional.ofNullable(objects).orElseGet(() -> {
            return new ConcurrentHashMap<String, T>();
        });
        StringBuilder builder = new StringBuilder();
        for (ConvertHandler handler : analyzeObjects()) {
            if(handler.isUniqueKey()) {
                builder.append("#").append(String.valueOf(handler.getValue(excelObjects)));
            }
        }
        if(builder.length() <= 0){
            builder.append(Globallys.nextKey());
        }
        String uniqueKey = MD5.encrypt(builder.toString());
        T object = convert(objects.get(uniqueKey), excelObjects);
        mergeSomething(object, excelObjects);
        objects.put(uniqueKey, object);
        systematics(uniqueKey, excelObjects);
        successCount = successCount + 1;
    }

    protected void systematics(String uniqueKey, Object[] objects){
        systematics = Optional.ofNullable(systematics).orElseGet(() -> {
            return new ConcurrentHashMap<String, List<Object[]>>();
        });
        List<Object[]> classification = Optional.ofNullable(systematics.get(uniqueKey)).orElseGet(() -> {
            return new ArrayList<Object[]>(1);
        });
        classification.add(objects);
        systematics.put(uniqueKey, classification);
    }

    protected T convert(T object, Object[] objects) throws Exception {
        if(object == null) {
            object = clazz.newInstance();
        }
        for (ConvertHandler handler : analyzeObjects()) {
            convert(object, objects, handler);
        }
        return object;
    }

    protected void convert(Object object, Object[] objects, ConvertHandler handler) throws Exception {
        try{
            Object value = handler.getValue(objects);
            if(Validator.NOTBLANK.verify(value)) {//校验是否是空值
                BeanUtilsBean beanUtils = BeanUtils.build();
                Property property = handler.getProperty();
                Object v = beanUtils.getProperty(object, handler.getPropertyName());
                if(property.isAppend() && !String.valueOf(v).contains(property.getShowName())){
                    value = property.getShowName() +"["+ String.valueOf(value) +"]";
                    if(v != null){
                        value = String.valueOf(v) +" "+ String.valueOf(value);
                    }
                }
                beanUtils.setProperty(object, handler.getPropertyName(), value);
            }
        }catch (Exception e){
            throw e;
        }
    }


    public void exceptions(Map<String, String> exceptions){
        for (Map.Entry<String, String> entry : exceptions.entrySet()) {
            List<Object[]> collection = systematics.get(entry.getKey());
            if(CollectionUtils.isNotEmpty(collection)){
                for (Object[] objects : collection) {
                    addException(new ExceptionObject(objects, entry.getValue()));
                    successCount = successCount - 1;
                }
            }
        }
        successCount = Math.max(0, successCount);
    }


    /**
     * getter method for waybills
     * @return the waybills
     */
    public Map<String, T> objects() {
        return objects;
    }

    /**
     * getter method for exceptions
     * @return the exceptions
     */
    public Collection<ExceptionObject> exceptions() {
        return exceptions;
    }

    /**
     * getter method for source
     * @return the source
     */
    public Collection<Object[]> source() {
        return source;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }
}
