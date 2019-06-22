package com.ycg.ksh.common.validate.impl;/**
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2018/5/9
 */

import com.ycg.ksh.common.validate.Validator;

import java.util.regex.Pattern;

/**
 * 中文姓名校验
 * <p>
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2018/5/9
 */
public class ChineseNameValidator extends Validator {

    private static final String MESSAGE = "%s必须为中文汉字";
    public static final Pattern REGEX = Pattern.compile("^[\\u4e00-\\u9fa5]+$");

    public ChineseNameValidator() {
        super();
        message = MESSAGE;
    }
    /**
     * @see fty.com.library.validate.Validator#verify(java.lang.Object, boolean)
     * <p>
     * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-28 13:45:07
     */
    @Override
    public boolean verify(Object object, boolean allowNull) {
        return  super.verify(object, allowNull) && REGEX.matcher(object.toString()).matches();
    }
}
