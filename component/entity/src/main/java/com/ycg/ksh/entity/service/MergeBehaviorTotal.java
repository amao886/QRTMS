package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.SysRequestSerial;

import java.util.List;
import java.util.Map;

/**
 * 用户行为整合类
 *
 * @author wangke
 * @create 2018-03-15 16:22
 **/
public class MergeBehaviorTotal extends SysRequestSerial {

    private static final long serialVersionUID = -668180554973834150L;

    //多条件参数
    private String likeString;

    //操作动作
    private String functionPoint;

    //组ID
    private Integer groupId;

    //用户手机号码
    private String mobilephone;

    //操作模块
    private String subordinateModule;

    //根据urikey日期分组查询list
    private List<Map> groupCount;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //查询天数
    private Integer falg;

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Integer getFalg() {
        return falg;
    }

    public void setFalg(Integer falg) {
        this.falg = falg;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFunctionPoint() {
        return functionPoint;
    }

    public void setFunctionPoint(String functionPoint) {
        this.functionPoint = functionPoint;
    }

    public List<Map> getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(List<Map> groupCount) {
        this.groupCount = groupCount;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getSubordinateModule() {
        return subordinateModule;
    }

    public void setSubordinateModule(String subordinateModule) {
        this.subordinateModule = subordinateModule;
    }
}
