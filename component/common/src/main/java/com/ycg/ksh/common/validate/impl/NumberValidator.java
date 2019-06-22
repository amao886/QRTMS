/**
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:32:15
 */
package com.ycg.ksh.common.validate.impl;

import com.ycg.ksh.common.validate.Validator;

import java.util.regex.Pattern;

/**
 * 数字校验
 * <p>
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:32:15
 */
public class NumberValidator extends Validator {

    private static final String MESSAGE = "%s必须为整数";
    public static final Pattern REGEX = Pattern.compile("^0|([1-9]\\d*)$");

    public NumberValidator() {
        super();
        message = MESSAGE;
    }
    /**
     * @see fty.com.library.validate.Validator#verify(java.lang.Object, boolean)
     * <p>
     * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:54:58
     */
    public boolean verify(Object object, boolean allowNull) {
        return super.verify(object, allowNull) && REGEX.matcher(object.toString()).matches();
    }
}
