/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 10:08:24
 */
package com.ycg.ksh.adapter;

import com.ycg.ksh.common.system.SystemKey;

/**
 * 系统配置KEY
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 10:08:24
 */
public interface SysNetKey extends SystemKey{
    
    /**微信*/
    String NET_WX_HOST = "net.wx.host";//微信host
    String NET_WX_COMMON_APPID = "net.wx.common.appid";
    String NET_WX_COMMON_APPSECRET = "net.wx.common.appsecret";
    String NET_WX_LOGIN_APPID = "net.wx.login.appid";
    String NET_WX_LOGIN_APPSECRET = "net.wx.login.appsecret";
    
    /**高德*/
    String NET_AUTOMAP_HOST = "net.automap.host";//高德地图host
    String NET_AUTOMAP_KEY = "net.automap.key";//高德地图key
    
    /**百度*/
    String NET_BAIDU_APP_ID = "net.baidu.app.id";
    String NET_BAIDU_APP_KEY = "net.baidu.app.key";
    String NET_BAIDU_SECRET_KEY = "net.baidu.secret.key";

    /**邮件*/
    String NET_EMAIL_EXCEPTION_KEY = "net.email.exception.key";
}
