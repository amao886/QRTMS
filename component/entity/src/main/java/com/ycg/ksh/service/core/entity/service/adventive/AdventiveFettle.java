package com.ycg.ksh.service.core.entity.service.adventive;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/24
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.ObjectType;

/**
 * 对外状态同步
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/24
 */
public class AdventiveFettle extends BaseEntity {

    private ObjectType objectType;//对象类型
    private Long objectKey;//对象编号
    private Integer fettle;//当前状态
    private Long changeTime;//变化的时间

    public AdventiveFettle() {
    }

    public AdventiveFettle(ObjectType objectType, Long objectKey, Integer fettle, Long changeTime) {
        this.objectType = objectType;
        this.objectKey = objectKey;
        this.fettle = fettle;
        this.changeTime = changeTime;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Long getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(Long objectKey) {
        this.objectKey = objectKey;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }
}
