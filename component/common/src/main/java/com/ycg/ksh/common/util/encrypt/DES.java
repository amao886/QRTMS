package com.ycg.ksh.common.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DES {
    /** 
     * ALGORITHM 算法 <br> 
     * 可替换为以下任意一种算法，同时key值的size相应改变。 
     * <pre> 
     * DES                  key size must be equal to 56 
     * DESede(TripleDES)    key size must be equal to 112 or 168 
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
     * RC2                  key size must be between 40 and 1024 bits 
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits 
     * </pre> 
     *  
     * 在Key toKey(byte[] key)方法中使用下述代码 
     * <pre>
     * SecretKey secretKey = new SecretKeySpec(key, ALGORITHM); 
     * </pre>
     * 替换
     * <pre> 
     * DESKeySpec dks = new DESKeySpec(key); 
     * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM); 
     * SecretKey secretKey = keyFactory.generateSecret(dks); 
     * </pre>
     */  
    public static final String ALGORITHM = "DES";

    /**
     * DES加密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 10:23:00
     * @param src  要加密的字节数组
     * @param key  密钥字节数组，长度必须是8的倍数
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 执行加密操作
        return cipher.doFinal(src);
    }

    /**
     * DES加密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 10:23:34
     * @param src  要加密的字符串
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public final static String encrypt(String src, String key) throws Exception {
    	return byte2hex(encrypt(src.getBytes(), key.getBytes()));
    }

    /**
     * DES解密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 10:24:12
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    /**
     * DES解密
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 10:24:47
     * @param data  要解密的字符串
     * @param key   密钥字符串
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data, String key) throws Exception {
    	return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
    }


    private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b!=null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1){
				hs.append('0');
			}
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
    
    private static byte[] hex2byte(byte[] b) {
        if((b.length % 2) != 0){
        	throw new IllegalArgumentException("长度不是偶数");
        }
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }
    
    public static void main(String[] args) throws Exception {
    	String secretKey = "QYWPV9XDY4H7CVK730";
		String sss = DES.encrypt("17539000036", secretKey);
    	System.out.println(sss);
		System.out.println(DES.decrypt(sss, secretKey));
	}
}
