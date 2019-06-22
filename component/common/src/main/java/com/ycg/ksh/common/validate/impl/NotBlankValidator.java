/**
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:29:44
 */
package com.ycg.ksh.common.validate.impl;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.validate.Validator;

/**
 * 非空校验
 * <p>
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:29:44
 */
public class NotBlankValidator extends Validator {
    
    private static final String MESSAGE = "%s不能为空";
    
    private static final String NULLSTRING = "null";
    
    public NotBlankValidator() {
        super();
        message = MESSAGE;
    }
    /**
     * @see fty.com.library.validate.Validator#verify(java.lang.Object, boolean)
     * <p>
     * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:54:58
     */
    public boolean verify(Object object, boolean allowNull) {
        if(null == object) {
            return false;
        } 
        if(NULLSTRING.equalsIgnoreCase(object.toString())) {
            return false;
        }
        return StringUtils.isNotBlank(object.toString());
    }
}
