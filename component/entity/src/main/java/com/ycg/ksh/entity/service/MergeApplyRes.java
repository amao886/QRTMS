package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.entity.persistent.ApplyRes;

/**
 * @Author:wangke
 * @Description:
 * @Date:Create in 14:51 2018/1/4
 * @Modified By:
 */
public class MergeApplyRes extends ApplyRes {

    private static final long serialVersionUID = 8978645501907889915L;
    /**
     * 可用条数
     */
    private Integer availableTotal;

    /**
     * 已用条数
     */
    private Integer alreadyTotal;

    /**
     * 总条数
     */
    private Integer total;

    /**
     * 昵称
     */
    private String uname;

    /**
     * 手机号码
     */
    private String mobilephone;

    private String groupName;

    private Integer changeUserId;

    /**
     * 所属公司的名称
     */
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getChangeUserId() {
        return changeUserId;
    }

    public void setChangeUserId(Integer changeUserId) {
        this.changeUserId = changeUserId;
    }

    public Integer getAvailableTotal() {
        return availableTotal;
    }

    public void setAvailableTotal(Integer availableTotal) {
        this.availableTotal = availableTotal;
    }

    public Integer getAlreadyTotal() {
        return alreadyTotal;
    }

    public void setAlreadyTotal(Integer alreadyTotal) {
        this.alreadyTotal = alreadyTotal;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }
    
    public String getUnamezn() {
        return UserUtil.decodeName(uname);
    }
}
