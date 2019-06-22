package com.ycg.ksh.common.system;

import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.encrypt.DES;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统工具类，系统配置属性...
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-28 10:02:24
 */
public class SystemUtils {
	
	public static final String SEPARATOR = "/"; 

	private static final Map<String, String> properties = new ConcurrentHashMap<String, String>();

	private static volatile boolean startup = false;
	
	public static boolean isStartup() {
		return startup;
	}

	public static synchronized void setStartup(boolean _startup) {
		startup = _startup;
	}
	/**
	 * jdbc配置des加密解密密钥
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-08 13:24:10
	 */
	public static final String JDBC_DES_KEY = "PTVSxBY45Lcw8CWIzmXgADsfo2OH3hMu";

	public static Map<String, String> properties() {
		return properties;
	}

	/**
	 * 新增配置项
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-09 09:21:19
	 * @param key
	 * @param value
	 */
	public static void put(String key, String value) {
		properties.put(key, value);
	}

	/**
	 * 取出指定配置项
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-09 09:21:22
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return properties.get(key);
	}

	/**
	 * 获取 微信回调时域名
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-28 10:07:11
	 * @return
	 */
	public static String getCallBackDomain() {
		return get(SystemKey.WEIXIN_CALLBACK_DOMAIN);
	}
	
    /**
     * 组装完整的请求url
     * <p>
     * @param base_url
	 * @param sub_url
     * @return
     */
    public static String buildUrl(String base_url, String sub_url) {
    	return normalize(base_url) + SEPARATOR + normalize(sub_url);
    }
	 /** 
     * 去掉路径的前后 /
     */  
    public static String normalize(String path) {  
        String temp = path;  
        if(path.startsWith(SEPARATOR)) {  
            temp = temp.substring(1);
        }  
        if(path.endsWith(SEPARATOR)) {  
            temp = temp.substring(0, temp.length() - 1);  
            return normalize(temp);  
        }else {  
            return temp;  
        }  
    } 
    
    public static String last(String path){
    	String temp = normalize(path);  
    	return temp.substring(temp.lastIndexOf(SEPARATOR) + 1);
    }

	public static String env(){
		return get(SystemKey.SYS_CURRENT_ENV);
	}

	public static boolean needUnionid(){
		String str = get(SystemKey.SYSTEM_WX_UNIONID);
		if(str != null){
			return Boolean.parseBoolean(str);
		}
		return true;
	}

	public static boolean esignEnable(){
		String enable = get(SystemKey.SYS_ESIGN_ENABLE);
		if(StringUtils.isNotBlank(enable)) {
			if(enable.equalsIgnoreCase("true")) {
				return true;
			}
		}
		return false;
	}

    /**
     * 系统当前是否处于调试状态
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-12 11:23:48
     * @return
     */
    public static boolean debug() {
    	String status = get(SystemKey.SYSTEM_STATUS);
    	if(StringUtils.isNotBlank(status)) {
    		if(status.equalsIgnoreCase("debug")) {
        		return true;
        	}
    	}
    	return false;
    }

    /**
     * 系统加密密钥
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:21:31
     * @return
     */
    public static String secretKey() {
    	return get(SystemKey.SYSTEM_DES_SECRET_KEY);
    }
	public static String projectPath() {
		return get(SystemKey.SYS_PROJECT_DIC);
	}
	public static String classpath() {
		return get(SystemKey.SYS_CLASSPATH);
	}

	/**
	 * 共享目录的根目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:21:56
	 * @return
	 */
	public static String fileRootPath() {
		return get(SystemKey.SYSTEM_FILE_ROOT);
	}

	public static String fileRootPath(Directory directory) {
		return FileUtils.path(fileRootPath(), directory.getDir());
	}

	public static String fileRootPath(String subPath) {
		return FileUtils.path(fileRootPath(), subPath);
	}

	public static String directoryPDF() {
		return fileRootPath(Directory.PDF.getDir());
	}

	/**
	 * 临时文件夹二级目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:17:48
	 * @param subDic 临时文件二级目录
	 * @return
	 */
	public static String directoryTemp(String subDic) {
        return fileRootPath(Directory.TEMP.sub(subDic));
    }

	public static String directoryUserTemp(Integer uKey) {
		return fileRootPath(Directory.TEMP.sub(FileUtils.path(String.valueOf(uKey), String.valueOf(System.nanoTime()))));
	}

    /**
     * 临时文件夹
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:18:39
     * @return
     */
    public static String directoryTemp() {
        return fileRootPath(Directory.TEMP.getDir());
    }
	/**
	 * 文件上传根目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:06
	 * @return
	 */
	public static String directoryUpload() {
		return fileRootPath(Directory.UPLOAD.getDir());
	}
	/**
	 * 文件上传根目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:06
	 * @return
	 */
	public static String directoryUpload(String subPath) {
		return fileRootPath(Directory.UPLOAD.sub(subPath));
	}
	
	/**
	 * 下载文件根目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:22
	 * @return
	 */
	public static String directoryDownload() {
		return fileRootPath(Directory.DOWN.getDir());
	}
	
	public static String directoryDownload(String subPath) {
		return fileRootPath(Directory.DOWN.sub(subPath));
	}

	/**
	 * 静态资源
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:35
	 * @return
	 */
	public static String staticPathPrefix() {
		return get(SystemKey.STATIC_PATH_PREFIX);
	}

	/**
	 * 静态资源
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:35
	 * @return
	 */
	public static String staticUrl(String subPath) {
		return buildUrl(get(SystemKey.STATIC_PATH_PREFIX), subPath);
	}

    /**
     * 图片静态资源url前缀
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:35
     * @return
     */
    public static String imagepath(String subPath) {
		return staticUrl(Directory.UPLOAD.sub(subPath));
	}

	/**
	 * 图片静态资源url前缀
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:35
	 * @return
	 */
	public static String imagepath() {
		return staticUrl(Directory.UPLOAD.getDir());
	}
    /**
     * 下载静态资源url前缀
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:54
     * @return
     */
    public static String downpath() {
		return buildUrl(get(SystemKey.STATIC_PATH_PREFIX), Directory.DOWN.getDir());
	}
    
    /**
     * 创建二维码扫描内容
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 09:41:28
     * @param barcode
     * @return
     * @throws Exception
     */
    public static String buildQRcodeContext(String barcode) throws Exception{
    	return buildUrl(getCallBackDomain(), "mobile/wechat/scan/"+ DES.encrypt(barcode, secretKey()) +"?state=SCAN");
	}

	/**
	 * 图片静态资源url前缀
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-22 09:20:35
	 * @return
	 */
	public static String sealImagepath() {
		return staticUrl(Directory.SEAL.getDir());
	}

	public static String smsKey(String mobile){
		return SystemKey.SYS_SMS_CACHE_KEY_PREFIX + mobile;
	}
}
