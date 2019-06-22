package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/23
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.User;

/**
 * 用户信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/23
 */
public class ConciseUser extends BaseEntity {

    /**
     * 用户Id自增
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String unamezn;
    /**
     * 手机号
     */
    private String mobilephone;

    /**
     * 微信头像
     */
    private String headImg;


    public ConciseUser() {
    }

    public ConciseUser(User user) {
        id = user.getId();
        unamezn = user.getUnamezn();
        mobilephone = user.getMobilephone();
        headImg = user.getHeadImg();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnamezn() {
        return unamezn;
    }

    public void setUnamezn(String unamezn) {
        this.unamezn = unamezn;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}

