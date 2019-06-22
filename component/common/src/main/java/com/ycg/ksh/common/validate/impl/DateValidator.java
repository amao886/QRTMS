package com.ycg.ksh.common.validate.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/19
 */

import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * 日期格式校验
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/19
 */
public class DateValidator extends Validator {

    private

    ThreadLocal<SimpleDateFormat> format = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    });

    private static final String MESSAGE = "%s必须为正确的日期格式";

    public static final Pattern REGEX = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|" +
            "([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|" +
            "(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

    public DateValidator() {
        super();
        message = MESSAGE;
    }
    /**
     * @see Validator#verify(Object, boolean)
     * <p>
     * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:54:58
     */
    public boolean verify(Object object, boolean allowNull) {
        if(super.verify(object, allowNull) && object instanceof String){
            try{
                BeanUtils.dateConvert(object);
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }
}
