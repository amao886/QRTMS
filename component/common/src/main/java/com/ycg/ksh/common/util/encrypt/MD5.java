package com.ycg.ksh.common.util.encrypt;

import java.security.MessageDigest;

/**
 * MD5加密，单向加密，不可解密
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 08:57:35
 */
public class MD5 {
	
    public static final String KEY_MD5 = "MD5";  

    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private MD5() { }
    /**
     * MD5加密，单向加密，不可解密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 08:51:20
     * @param data  要加密的字节数组
     * @return  加密后的字节数组
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data) throws Exception {  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);  
        md5.update(data);  
        return md5.digest();  
    } 

    /**
     * MD5加密，单向加密，不可解密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 08:57:05
     * @param strObj  要加密的字符串
     * @return  加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String strObj) {
        try{
            String resultString = new String(strObj);
            resultString = byteToString(encrypt(strObj.getBytes()));
            return resultString;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    /*
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }
    */

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
}
