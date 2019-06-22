/**
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2018-01-09 13:51:44
 */
package com.ycg.ksh.common.validate.impl;

import com.ycg.ksh.common.validate.Validator;

import java.util.regex.Pattern;

/**
 * 条码校验
 * <p>
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2018-01-09 13:51:44
 */
public class BarcodeValidator extends Validator {

    private static final String MESSAGE = "%s无效的条码号";
    public static final Pattern REGEX = Pattern.compile("^(\\d{11})|(\\d{14})$");
	
    public BarcodeValidator() {
        super();
        message = MESSAGE;
    }
    /**
	 * @see fty.com.library.validate.Validator#verify(java.lang.Object, boolean)
	 * <p>
	 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2018-01-09 13:51:44
	 */
	@Override
	public boolean verify(Object object, boolean allowNull) {
        return  super.verify(object, allowNull) && REGEX.matcher(object.toString()).matches();
	}
}
