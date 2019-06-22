package com.ycg.ksh.api.mobile.util;

public interface WxConstant {

	long WEIXINTIMEOUT = 7200;// 微信7200秒刷新下token

	int TEMPORARY_TIME = 600;

	int BINDCODE = 1;// 绑定条码

	int TOLOCATIONLIST = 2;// 扫描定位

	int MINERESOUCE = 3;// 我的资源

	int SCANADDGROUP = 4;// 扫描加群

	int SCANCODE = 5;// 扫描登录并判断条码

	int SHAREDETAILS = 6;// 分享详情

	int MINE = 7;// 个人中心

	int WAILLBILL = 8;// 我的任务单

	int TOHOME = 9;// 首页

	int UPLOADRECEIPT = 10;// 上传回单

	int POSITIONLIST = 11;// 任务详情

	int EX_UPIMAGE = 0;// 异常图片
	int RC_UPIMAGE = 1;// 回单图片

	String SINGLFLAG = "singlemessage";// 分享给朋友标记
	String GROUPFLAG = "groupmessage";// 分享到群标记

	/** 微信回调state参数值 */
	String STATE_INDEX = "INDEX";// 首页
	String STATE_CENTER = "CENTER";// 个人中心
	String STATE_BING = "BING";// 绑定手机号
	String STATE_SCAN = "SCAN";// 扫码
	String STATE_SHARE = "SHARE";// 分享
	String STATE_ADDRESS = "ADDRESS";// 分享地址
	String STATE_GROUP = "GROUP";// 扫码添加组
	String STATE_DISPATCH = "DISPATCH";// 派送
	String STATE_FRIEND = "FRIEND";// 扫描添加好友
	String STATE_COMPANY = "COMPANY";//客户管理-扫码绑定企业

	/** 微信回调关键参数 */
	String PARMA_KEY_CODE = "code";
	String PARMA_KEY_STATE = "state";

}
