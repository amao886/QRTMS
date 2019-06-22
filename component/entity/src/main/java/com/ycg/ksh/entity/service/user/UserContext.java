package com.ycg.ksh.entity.service.user;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/16 0016
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 登陆上下文
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/16 0016
 */
public class UserContext extends BaseEntity {

    private Integer loginType;
    private Integer clientType;
    private String clientHost;
    private String env;

    private Integer userKey;
    private Integer adminKey;

    private String openid;
    private String subscribe;
    private String unionid;
    private String mobilephone;
    private String uname;
    private String headImg;



    private boolean create = true;

    public UserContext() {
    }

    public UserContext(Integer loginType, Integer clientType, String env) {
        this.loginType = loginType;
        this.clientType = clientType;
        this.env = env;
    }

    public UserContext(Integer userKey, Integer loginType, Integer clientType, String env) {
        this.loginType = loginType;
        this.clientType = clientType;
        this.env = env;
        this.userKey = userKey;
    }

    public UserContext(Integer userKey, Integer loginType, Integer clientType, String clientHost, String env) {
        this.loginType = loginType;
        this.clientType = clientType;
        this.clientHost = clientHost;
        this.env = env;
        this.userKey = userKey;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer getAdminKey() {
        return adminKey;
    }

    public void setAdminKey(Integer adminKey) {
        this.adminKey = adminKey;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getUserKey() {
        return userKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

}
