package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 通讯录搜索组合类
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 16:42 2017/12/11
 * @Modified By:
 */
public class FriendsSerach extends BaseEntity {
    private static final long serialVersionUID = 2029767980616838929L;

    private Integer userId;

    private String likeString;


    private Boolean registered;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }
}
