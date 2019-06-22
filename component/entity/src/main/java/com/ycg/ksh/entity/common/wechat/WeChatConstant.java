package com.ycg.ksh.entity.common.wechat;

public interface WeChatConstant {

	//推送消息
	String MSGTYPE_EVENT = "event";//事件
	String MSGTYPE_TEXT = "text";//文本消息
	String MSGTYPE_IMAGE = "image";//图片消息
	String MSGTYPE_VOICE = "voice";//语音消息
	String MSGTYPE_VIDEO = "video";//视频消息
	String MSGTYPE_LOCATION = "location";//地理位置消息
	String MSGTYPE_LINK = "link";//链接消息
	String MSGTYPE_SHORTVIDEO = "shortvideo";//小视频消息
	//推送事件
	String MSGEVENT_SUBSCRIBE = "subscribe";//订阅
	String MSGEVENT_UNSUBSCRIBE = "unsubscribe";//取消订阅
	String MSGEVENT_SCAN = "SCAN";//扫描带参二维码
	String MSGEVENT_LOCATION = "LOCATION";//位置上报事件
	String MSGEVENT_CLICK = "CLICK";//自定义菜单事件
	String MSGEVENT_VIEW = "VIEW";//点击菜单跳转链接时的事件推送
	

	String DEFAULT_URL = "https://api.weixin.qq.com/cgi-bin";
	
	String CACHE_KEY_JSAPITICKET = "CACHE_KEY_WX_JSAPITICKET";
	
	
	//授权作用域
	String AUTH_SCOPE_BASE ="snsapi_base";//基础授权
	String AUTH_SCOPE_USERINFO ="snsapi_userinfo";//获取用户基本信息
	String AUTH_SCOPE_LOGIN ="snsapi_login";//扫码登陆
	
	//关注状态
	String SUBSCRIBE_OK = "1";//已关注
	String SUBSCRIBE_UN = "0";//未关注
}
