/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 13:54:08
 */
package com.ycg.ksh.common.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;

import java.util.Date;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 13:54:08
 */
public class BeanUtils {
 
    private static final String[] DATE_FORMATS = new String[] { 
            "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd", 
            "yyyyMMdd HH", "yyyy-MM-dd HH", "yyyy/MM/dd HH",
            "yyyyMMdd HH:mm", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm",
            "yyyyMMdd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"
        };

    private static final DateConverter DATE_CONVERTER = new DateConverter();

    static {
        DATE_CONVERTER.setUseLocaleFormat(true);
        DATE_CONVERTER.setPatterns(DATE_FORMATS);
    }

    private enum EnumObject{  
        
        SINGLETON_OBJECT;  
          
        private BeanUtilsBean beanUtil;  
          
        private EnumObject(){
            ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
            convertUtilsBean.register(new IntegerConverter(null), Integer.class);
            convertUtilsBean.register(new LongConverter(null), Long.class);
            convertUtilsBean.register(new BooleanConverter(null), Boolean.class);
            convertUtilsBean.register(DATE_CONVERTER, java.util.Date.class);
            beanUtil = new BeanUtilsBean(convertUtilsBean);
        }  
    }

    public static <T> T copy(Class<T> clazz, Object orig){
        try{
            T obj = clazz.newInstance();
            build().copyProperties(obj, orig);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static BeanUtilsBean build() {
        return EnumObject.SINGLETON_OBJECT.beanUtil;
    }

    public static Date dateConvert(Object value){
        return DATE_CONVERTER.convert(Date.class, value);
    }
}
