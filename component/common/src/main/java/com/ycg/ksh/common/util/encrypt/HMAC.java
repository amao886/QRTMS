package com.ycg.ksh.common.util.encrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMAC {
    /** 
     * MAC算法可选以下多种算法 
     *  
     * <pre> 
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512 
     * </pre> 
     */  
    public static final String KEY_MAC = "HmacMD5";
    
    /**
     * 初始化HMAC密钥 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 09:25:10
     * @return
     * @throws Exception
     */
    public static String initMacKey() throws Exception {  
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);  
        SecretKey secretKey = keyGenerator.generateKey();  
        return Base64.encode(secretKey.getEncoded());  
    }  
  
 
    /**
     * HMAC加密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 09:25:19
     * @param data  要加密的字节数组
     * @param key   密钥 
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String key) throws Exception {  
        SecretKey secretKey = new SecretKeySpec(Base64.decode(key), KEY_MAC);  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        mac.init(secretKey);  
        return mac.doFinal(data);  
  
    }
    
    public static String encrypt(String data, String key) throws Exception {  
        return new String(encrypt(data.getBytes(), key));  
    }
}
