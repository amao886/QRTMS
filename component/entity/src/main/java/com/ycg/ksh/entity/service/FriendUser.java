package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/24
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.UserUtil;

import java.util.Date;

/**
 * 好友用户信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/24
 */
public class FriendUser extends BaseEntity {

    private Integer fkey;//好友表主键

    private Integer ownerKey;//所有者用户编号
    private String remarkName;//好友备注名称
    private String remarkMobile;//好友备注手机号
    private String company;//好友备注公司
    private String remark;//好友备注
    private Integer type;//好友类型

    private Integer friendKey;//好友的用户编号
    private String mobile;//好友的手机号
    private String userName;//好友用户昵称
    private Date createtime;//好友用户的注册事件
    private String subscribe;

    public String getEncryptName() {
        return userName;
    }

    public Integer getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(Integer ownerKey) {
        this.ownerKey = ownerKey;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getRemarkMobile() {
        return remarkMobile;
    }

    public void setRemarkMobile(String remarkMobile) {
        this.remarkMobile = remarkMobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFriendKey() {
        return friendKey;
    }

    public void setFriendKey(Integer friendKey) {
        this.friendKey = friendKey;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return UserUtil.decodeName(userName);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public Integer getFkey() {
        return fkey;
    }

    public void setFkey(Integer fkey) {
        this.fkey = fkey;
    }
}
