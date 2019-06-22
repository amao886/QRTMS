package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.entity.adapter.esign.Signer;
import com.ycg.ksh.constant.ObjectType;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`T_SIGN_ASSOCIATE`")
public class SignAssociate {
    /**
     * 对象类型
     */
    @Column(name = "`OBJECT_TYPE`")
    private String objectType;

    /**
     * 对象编号
     */
    @Column(name = "`OBJECT_KEY`")
    private String objectKey;

    /**
     * 第三方类型
     */
    @Column(name = "`THIRD_TYPE`")
    private String thirdType;

    /**
     * 第三方对象编号
     */
    @Column(name = "`THIRD_OBJECT_KEY`")
    private String thirdObjectKey;

    /**
     * 操作人
     */
    @Column(name = "`OPERATE_ID`")
    private Integer operateId;

    /**
     * 操作编号
     */
    @Column(name = "`OPERATE_TIME`")
    private Date operateTime;


    public SignAssociate(ObjectType objectType, Serializable objectKey) {
        this.objectType = objectType.name();
        this.objectKey = String.valueOf(objectKey);
    }

    public SignAssociate(ObjectType objectType, Serializable objectKey, Signer thirdType, Serializable thirdObjectKey, Integer operateId) {
        this.objectType = objectType.name();
        this.objectKey = String.valueOf(objectKey);
        this.thirdType = thirdType.getCode();
        this.thirdObjectKey = String.valueOf(thirdObjectKey);
        this.operateId = operateId;
    }

    public SignAssociate() {
    }

    /**
     * 获取对象类型
     *
     * @return OBJECT_TYPE - 对象类型
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * 设置对象类型
     *
     * @param objectType 对象类型
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * 获取对象编号
     *
     * @return OBJECT_KEY - 对象编号
     */
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * 设置对象编号
     *
     * @param objectKey 对象编号
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * 获取第三方类型
     *
     * @return THIRD_TYPE - 第三方类型
     */
    public String getThirdType() {
        return thirdType;
    }

    /**
     * 设置第三方类型
     *
     * @param thirdType 第三方类型
     */
    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }

    /**
     * 获取第三方对象编号
     *
     * @return THIRD_OBJECT_KEY - 第三方对象编号
     */
    public String getThirdObjectKey() {
        return thirdObjectKey;
    }

    /**
     * 设置第三方对象编号
     *
     * @param thirdObjectKey 第三方对象编号
     */
    public void setThirdObjectKey(String thirdObjectKey) {
        this.thirdObjectKey = thirdObjectKey;
    }

    /**
     * 获取操作人
     *
     * @return OPERATE_ID - 操作人
     */
    public Integer getOperateId() {
        return operateId;
    }

    /**
     * 设置操作人
     *
     * @param operateId 操作人
     */
    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    /**
     * 获取操作编号
     *
     * @return OPERATE_TIME - 操作编号
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * 设置操作编号
     *
     * @param operateTime 操作编号
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}