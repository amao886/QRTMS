package com.ycg.ksh.common.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/24
 */

import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 用户工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/24
 */
public class UserUtil {

    public static final int SYSTEM_UID = 0;

    private static final String CHARSETNAME = "utf-8";

    public static String decodeName(String uname) {
        if(StringUtils.isEmpty(uname)){
            return uname;
        }
        try {
            String unamezn = new String(Base64.decodeBase64(uname), CHARSETNAME);
            if(unamezn.contains("�") || StringUtils.isEmpty(unamezn)){
                return uname;
            }else{
                return unamezn;
            }
        } catch (Exception e) {
            return uname;
        }
    }

    public static String encodeName(String uname) {
        if(StringUtils.isEmpty(uname)){
            return uname;
        }
        try {
            return Base64.encodeBase64String(uname.getBytes(CHARSETNAME));
        } catch (Exception e) {
            return uname;
        }
    }

    public static String encodeMobile(String mobile) {
        if(StringUtils.isEmpty(mobile)){
            return mobile;
        }
        try {
            int len = mobile.length();
            if(len > 7){
                return mobile.replaceAll("(\\d{3})\\d{"+ (len - 7) +"}(\\d{4})", "$1"+ asterisk(len - 7) +"$2");
            }
            return mobile;
        } catch (Exception e) {
            return mobile;
        }
    }

    public static String encodeIdNo(String idNo) {
        if(StringUtils.isEmpty(idNo)){
            return idNo;
        }
        try {
            int len = idNo.length();
            if(len > 4){
                return idNo.replaceAll("(\\d{"+ (len - 4) +"})(\\d{4})", "$1****");
            }
            return idNo;
        } catch (Exception e) {
            return idNo;
        }
    }
    public static String encodeBrankNo(String brankNo) {
        if(StringUtils.isEmpty(brankNo)){
            return brankNo;
        }
        try {
            int len = brankNo.length();
            if(len > 8){
                return brankNo.replaceAll("(\\d{4})\\d{"+ (len - 8) +"}(\\d{4})", "$1"+asterisk(len - 8)+"$2");
            }
            return brankNo;
        } catch (Exception e) {
            return brankNo;
        }
    }

    private static String asterisk(int count){
        char[] chars = new char[count];
        Arrays.fill(chars, '*');
        return new String(chars);
    }

    public static void main(String[] args) {

        System.out.println(UserUtil.asterisk(10));
        System.out.println(UserUtil.encodeMobile("19921600261"));
        System.out.println(UserUtil.encodeIdNo("421181198608229110"));
        System.out.println(UserUtil.encodeBrankNo("421181198608229110"));
    }

}
