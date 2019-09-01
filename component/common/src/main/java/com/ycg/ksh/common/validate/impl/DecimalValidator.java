package  com.ycg.ksh.common.validate.impl;

import com.ycg.ksh.common.validate.Validator;

import java.util.regex.Pattern;

/**
 * 小数校验
 */
public class DecimalValidator extends Validator {

    private static final String MESSAGE = "%s必须为数字(整数/小数)";
    public static final Pattern REGEX = Pattern.compile("^0|([1-9]\\d*\\.?\\d*)|(0\\.\\d*)$");

    public DecimalValidator() {
        super();
        message = MESSAGE;
    }
    /**
     * @see fty.com.library.validate.Validator#verify(java.lang.Object, boolean)
     * <p>
     * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:54:58
     */
    public boolean verify(Object object, boolean allowNull) {
    	if(allowNull && super.verify(object, allowNull)) {
    		return super.verify(object, allowNull);
    	}
        return super.verify(object, allowNull) && REGEX.matcher(object.toString()).matches();
    }
}
