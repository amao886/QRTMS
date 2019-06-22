/**
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-28 13:45:07
 */
package com.ycg.ksh.common.validate.impl;

import com.ycg.ksh.common.validate.Validator;

import java.util.regex.Pattern;

/**
 * 正常字符校验(大小写字母、数字、中文)
 * <p>
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-28 13:45:07
 */
public class NormalWordValidator extends Validator {

    private static final String MESSAGE = "%s必须为字母、数字、中文";
    public static final Pattern REGEX = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]+$");
    
    public NormalWordValidator() {
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
        return super.verify(object, allowNull) && REGEX.matcher(object.toString()).matches();
    }
    
}
