package com.ycg.ksh.common.util;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyFileUtils {
	/**
	 * 下载文件时，针对不同浏览器，进行附件名的编码
	 * 
	 * @param filename
	 *            下载文件名
	 * @param agent
	 *            客户端浏览器
	 * @return 编码后的下载附件名
	 * @throws IOException
	 *            
	 */
	@SuppressWarnings("restriction")
	public static String encodeDownloadFilename(String filename, String agent) {
		try {
			if (agent.contains("Firefox")) { // 火狐浏览器
				filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
				filename = filename.replaceAll("\r\n", "");
			} else { // IE及其他浏览器
				filename = URLEncoder.encode(filename, "utf-8");
				filename = filename.replace("+", " ");
			}
			return filename;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("附件名转换错误" + e);
		}
	}
}
