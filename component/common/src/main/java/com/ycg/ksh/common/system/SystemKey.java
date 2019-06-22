package com.ycg.ksh.common.system;

public interface SystemKey {

	String SYS_SMS_CACHE_KEY_PREFIX = "sms.valid.code.";

	String SYS_CURRENT_ENV = "sys.current.env";//当前环境
	String SYS_PROJECT_DIC = "sys.project.dic";//当前目录
	String SYS_CLASSPATH = "sys.classpath";//当前目录
	String SYS_ESIGN_ENABLE = "system.esign.enable";//电子签收是否启用


	String SYSTEM_MOBILE_COMMONLY = "system.mobile.commonly";//手机端默认常用功能
	String SYSTEM_MOBILE_COMMONLYKEYS = "system.mobile.commonlykeys";//新版非司机手机端默认常用功能
	String SYSTEM_MOBILE_DRIVERCOMMONLYKEYS = "system.mobile.drivercommonlykeys";//新版司机手机端默认常用功能
	String GLOBAL_APP_NAME="global.application.name";//全局应用名称
	
	String SYSTEM_STATUS = "system.status";//系统状态,如果为debug则为调试模式
	
	String SYS_LOCALHOST = "sys.localhost";//当前服务器host

	String WEIXIN_CALLBACK_DOMAIN = "weixin.callback.domain";//微信回调时域名
	String FRONT_HOST_DOMAIN = "front.host.domain";//前端地址
	String WX_MSG_TEMPLATE_ID="net.wx.msg.template";//模板消息ID

	String SYSTEM_WX_UNIONID = "system.wx.unionid";//是否需要微信unionid

	String STATIC_PATH_PREFIX = "static.path.prefix";//静态访问地址前缀
    
    String SYSTEM_DES_SECRET_KEY ="global.secret";//系统全局加密密钥
	String SYSTEM_FILE_ROOT="system.file.root";//共享文件根目录
}
