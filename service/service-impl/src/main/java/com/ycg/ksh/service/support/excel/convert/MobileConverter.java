package com.ycg.ksh.service.support.excel.convert;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/27
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号转换
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/27
 */
public class MobileConverter extends Converter {

    private static final Pattern pattern = Pattern.compile("1\\d{10}");

    @Override
    public Object convert(Object source) {
        if(source != null){
            Matcher matcher = pattern.matcher(source.toString());
            if(matcher.find()){
                return matcher.group();
            }
        }
        return null;
    }
}
