package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.CoreConstants;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

@Table(name = "`user_commonly`")
public class UserCommonly extends BaseEntity {
    /**
     * 用户主键
     */
    @Id
    @Column(name = "`user_key`")
    private Integer userKey;

    /**
     * 常用功能编号(#隔开)
     */
    @Column(name = "`commonly`")
    private String commonly;
    /**
     * 企业版常用功能编号(#隔开)
     */
    @Column(name = "`commonly_keys`")
    private String commonlyKeys;
    /**
     * 企业版司机常用功能编号(#隔开)
     */
    @Column(name = "`driver_commonly_keys`")
    private String driverCommonlyKeys;

    /**
     * 用户身份标识(1:司机,2:发货发,3:承运方,4:收货方)
     */
    @Column(name = "`identity_key`")
    private Integer identityKey;

    public UserCommonly() {}

    public UserCommonly(Integer userKey, Integer identityKey) {
        this.userKey = userKey;
        this.identityKey = identityKey;
    }
    /*
    public UserCommonly(Integer userKey, String commonlyKeys) {
        this.userKey = userKey;
        this.commonlyKeys = commonlyKeys;
        this.identityKey = 2;//默认非司机
    }
    */

    public boolean isDriver(){
        return identityKey != null && identityKey == CoreConstants.USER_CATEGORY_DRIVER;
    }


    /**
     * 获取用户主键
     *
     * @return user_key - 用户主键
     */
    public Integer getUserKey() {
        return userKey;
    }

    /**
     * 设置用户主键
     *
     * @param userKey 用户主键
     */
    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    /**
     * 获取常用功能编号(#隔开)
     *
     * @return commonly - 常用功能编号(#隔开)
     */
    public String getCommonly() {
        return commonly;
    }

    /**
     * 设置常用功能编号(#隔开)
     *
     * @param commonly 常用功能编号(#隔开)
     */
    public void setCommonly(String commonly) {
        this.commonly = commonly;
    }

    public String getCommonlyKeys() {
        return commonlyKeys;
    }

    public void setCommonlyKeys(String commonlyKeys) {
        this.commonlyKeys = commonlyKeys;
    }

    public Integer getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(Integer identityKey) {
        this.identityKey = identityKey;
    }

    public String getDriverCommonlyKeys() {
        return driverCommonlyKeys;
    }

    public void setDriverCommonlyKeys(String driverCommonlyKeys) {
        this.driverCommonlyKeys = driverCommonlyKeys;
    }

    public Collection<String> commonlies(boolean newversion){
        Collection<String> commonlies = new ArrayList<String>();
        String _commonly = commonly;
        if(newversion){
            _commonly = isDriver() ? driverCommonlyKeys : commonlyKeys;
        }
        StringTokenizer tokenizer = new StringTokenizer(_commonly, "#");
        while (tokenizer.hasMoreTokens()) {
            commonlies.add(tokenizer.nextToken());
        }
        return commonlies;
    }
}