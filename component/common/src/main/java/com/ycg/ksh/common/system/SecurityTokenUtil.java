/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-07-11 12:44:15
 */
package com.ycg.ksh.common.system;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.encrypt.Base64;
import com.ycg.ksh.common.util.encrypt.DES;
import com.ycg.ksh.common.util.encrypt.MD5;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * api接口token生成工具
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-07-11 12:44:15
 */
public class SecurityTokenUtil {

	public static final String SECRET = "QYWPV9XDY4H7CVK730";
	public static final String APPEND_KEY = ":";

	private static  final int CONFUSE = 32;
	private static final int LEN = 4;

	private static int byteArrayToInt(byte[] b) {
		return   b[3] & 0xFF |
				(b[2] & 0xFF) << 8 |
				(b[1] & 0xFF) << 16 |
				(b[0] & 0xFF) << 24;
	}
	private static byte[] intToByteArray(int a) {
		return new byte[] {
				(byte) ((a >> 24) & 0xFF),
				(byte) ((a >> 16) & 0xFF),
				(byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF)
		};
	}

	private static byte[] md5(Serializable src) throws Exception {
		return  Objects.requireNonNull(MD5.encrypt(SECRET + src.toString())).toUpperCase().getBytes();
	}


	public static String createToken(Serializable userKey) {
		try{
            byte[] srcs = md5(userKey), keys = String.valueOf(userKey).getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(srcs.length + keys.length);
            for (int i = 0; i < keys.length; i++) {
                buffer.put(srcs[i]);
                buffer.put(keys[i]);
            }
            if(srcs.length > keys.length){
                buffer.put(srcs, keys.length, srcs.length - keys.length);
            }
			return new String(buffer.array());
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getUserIdByToken(String authToken) {
		try{
		    byte[] bytes = Objects.requireNonNull(authToken.getBytes());
		    if(bytes.length > CONFUSE){
		        byte[]  keys = new byte[bytes.length - CONFUSE];
                for (int i = 0; i < keys.length; i++) {
                    keys[i] = bytes[i * 2 + 1];
                }
                return new String(keys);
            }else{
				return getUserIdByToken1(authToken);
			}
		}catch (Exception e){
			return getUserIdByToken1(authToken);
		}
	}

	public static String getUserIdByToken1(String authToken) {
		try {
			if (authToken == null) {
				return null;
			}
			String token = DES.decrypt(authToken, SECRET);
			return token.substring(0, token.indexOf(APPEND_KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*public static String createToken(String userId) {
		try{
			long expires = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
			String token = DES.encrypt(userId + APPEND_KEY + expires + APPEND_KEY + computeSignature(userId, expires), SECRET);
			return token;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static String computeSignature(String userName, long expires) throws Exception {
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(userName).append(APPEND_KEY);
		signatureBuilder.append(expires).append(APPEND_KEY);
		signatureBuilder.append(SECRET);
		return DES.encrypt(MD5.encrypt(signatureBuilder.toString()), SECRET);
	}

	public static String getUserIdByToken(String authToken) throws Exception {
		try {
			if (authToken == null) {
				return null;
			}
			String token = DES.decrypt(authToken, SECRET);
			return token.substring(0, token.indexOf(APPEND_KEY));
		} catch (Exception e) {
			throw new BusinessException("Token解析异常", e);
		}
	}

	public static boolean validateToken(String authToken, String userName) throws Exception {
		try {
			if (authToken == null) {
				return false;
			}
			String token = DES.decrypt(authToken, SECRET);
			String[] parts = token.split(APPEND_KEY);
			if(parts.length != 3){
				return false;
			}
			long expires = Long.parseLong(parts[1]);
			String signature = parts[2];
			String signatureToMatch = computeSignature(userName, expires);
			//return expires >= System.currentTimeMillis() && signature.equals(signatureToMatch);
			return signature.equals(signatureToMatch);
		} catch (Exception e) {
			return false;
		}
	}
	*/
	public static void main(String[] args) throws Exception {
		
		
	    Integer userKey = 2894;
		String token = SecurityTokenUtil.createToken(userKey);//用户ID，user表中的ID
		System.out.println(token);
        System.out.println(SecurityTokenUtil.getUserIdByToken("F139A480DBFB40AE536B00AE5ED417F884"));
        
		//System.out.println(SecurityTokenUtil.getUserIdByToken("104DA1AE76B0279FC29FACA60BDEC82F28EBA5ABD0C4749061B99DEB13F583DE2039546E87ED22FE83A6C41A22FB58DFD7457691F945313615EDBDBBCC13750506B0F8643FF8172F3BE4C2B1218FF3151441F5F8146D969AFC45E50243E02E5CFAE3515F796BE7DE"));

		//System.out.println(Base64.decode("d3hiMTQ1MzhhNjI2YzNjMGI3", "utf8"));
		//System.out.println(Base64.decode("MDkwZmFmNGRjOWFkYjhmOGRjZjc0ZjVjNGZmZDM1ZTQ=", "utf8"));
        /*long userKey = 337903607587840L;
        byte[] srcs = md5(userKey);
        System.out.println(Arrays.toString(srcs));
        byte[] keys = String.valueOf(userKey).getBytes();
        System.out.println(Arrays.toString(keys));
        ByteBuffer buffer = ByteBuffer.allocate(srcs.length + keys.length);
        for (int i = 0; i < keys.length; i++) {
            buffer.put(srcs[i]);
            buffer.put(keys[i]);
            System.out.println(buffer.position());
        }
        if(srcs.length > keys.length){
            System.out.println(buffer.position());
            buffer.put(srcs, keys.length, srcs.length - keys.length);
        }
        System.out.println(Arrays.toString(buffer.array()));*/
	}
}
