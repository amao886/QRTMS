package com.ycg.ksh.service.support.excel.convert;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/26
 */

import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.service.support.excel.Property;

/**
 * 转换处理器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/26
 */
public class ConvertHandler {

    private static final String DEFAULT_MESSAGE = "缺少数据列";

    private Property property;
    private int column;//excel文件列
    private boolean uniqueKey;//是否参与为唯一校验
    private Validator[] validators;//校验器
    private Converter converter;

    public ConvertHandler(Property property, int column, boolean uniqueKey, Validator[] validators, Converter converter) {
        this.property = property;
        this.column = column;
        this.uniqueKey = uniqueKey;
        this.validators = validators;
        this.converter = converter;
    }
    public ConvertHandler(String name, String showName, int column, boolean uniqueKey, Validator[] validators, Converter converter) {
        this(new Property(name, showName), column, uniqueKey, validators, converter);
    }
    public ConvertHandler(Property property, int column, Validator[] validators, Converter converter) {
        this(property, column, false, validators, converter);
    }
    public ConvertHandler(Property property, int column, boolean uniqueKey, Converter converter) {
        this(property, column, uniqueKey, null, converter);
    }
    public ConvertHandler(Property property, int column, Converter converter) {
        this(property, column, false, null, converter);
    }

    public ConvertHandler(Property property, int column, boolean uniqueKey, Validator[] validators) {
        this(property, column, uniqueKey, validators, null);
    }
    public ConvertHandler(Property property, int column, Validator[] validators) {
        this(property, column, false, validators, null);
    }

    public ConvertHandler(Property property, int column) {
        this(property, column, false, null, null);
    }

    public Object getValue(Object object){
        if(converter != null){
            return converter.convert(object);
        }
        return object;
    }

    public Object getValue(Object[] objects){
        return getValue(objects[column]);
    }

    public String validate(Object[] objects){
        if(column >= objects.length) {
            return DEFAULT_MESSAGE;
        }
        if(validators != null) {
            for (Validator v : validators) {
                if(!v.verify(getValue(objects), true)) {
                    return v.getMessage(property.getShowName());
                }
            }
        }
        return null;
    }

    public int getColumn() {
        return column;
    }

    public boolean isUniqueKey() {
        return uniqueKey;
    }

    public Validator[] getValidators() {
        return validators;
    }

    public Converter getConverter() {
        return converter;
    }

    public Property getProperty() {
        return property;
    }

    public String getPropertyName(){
        return property.getName();
    }
    public String getPropertyShowName(){
        return property.getShowName();
    }

}
