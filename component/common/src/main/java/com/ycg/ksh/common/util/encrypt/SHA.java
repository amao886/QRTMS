package com.ycg.ksh.common.util.encrypt;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

public class SHA {
	
    public static final String KEY_SHA = "SHA";

    /**
     * SHA加密 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 09:27:37
     * @param data  要加密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data) throws Exception {  
  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);  
        
        sha.update(data);  
  
        return sha.digest();  
    }  
    
    public static String encrypt(String data) throws Exception {  
        return Hex.encodeHexString(encrypt(data.getBytes()));  
    } 
}
