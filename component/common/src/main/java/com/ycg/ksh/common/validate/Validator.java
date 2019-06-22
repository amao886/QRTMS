/**
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:25:26
 */
package com.ycg.ksh.common.validate;


import com.ycg.ksh.common.util.StringUtils;

/**
 * 校验
 * <p>
 * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:25:26
 */
public abstract class Validator {
    
    public static final Validator NOTBLANK;
    public static final Validator NUMBER;
    public static final Validator MOBILE;
    public static final Validator NORMALWORD;
    public static final Validator BARCODE;
    public static final Validator IDCARD;
    public static final Validator CHINESENAME;
    public static final Validator DECIMAL;
    public static final Validator DATE;
    
    
    protected String message = "%s校验失败";
    
    
    static {
        NOTBLANK = new com.ycg.ksh.common.validate.impl.NotBlankValidator();
        NUMBER = new  com.ycg.ksh.common.validate.impl.NumberValidator();
        MOBILE = new  com.ycg.ksh.common.validate.impl.MobileValidator();
        NORMALWORD = new  com.ycg.ksh.common.validate.impl.NormalWordValidator();
        BARCODE = new  com.ycg.ksh.common.validate.impl.BarcodeValidator();
        IDCARD = new  com.ycg.ksh.common.validate.impl.IdcardValidator();
        CHINESENAME = new  com.ycg.ksh.common.validate.impl.ChineseNameValidator();
        DECIMAL = new  com.ycg.ksh.common.validate.impl.DecimalValidator();
        DATE = new com.ycg.ksh.common.validate.impl.DateValidator();
    }
    
    /**
     * 校验
     * <p>
     * @param object
     * @return
     */
    public boolean verify(Object object) {
        return verify(object, false);
    }
    
    /**
     * getter method for message
     * @return the message
     */
    public String getMessage(String pname) {
        return String.format(message, pname);
    }

    /**
     * 校验
     * <p>
     * @developer Create by <a href="mailto:fty628@sina.com">baymax</a> at 2017-12-19 12:54:06
     * @param object
     * @param allowNull  true:可以为空，false:不能为空
     * @return
     */
    public boolean verify(Object object, boolean allowNull){
        if(allowNull) {
            if(null == object || StringUtils.isBlank(object.toString())) {
                return true;
            }
        }else {
            return null != object;
        }
        return true;
    }


    public static void main(String[] args) {
        System.out.println(DECIMAL.verify("0.07", false));
    }

}
