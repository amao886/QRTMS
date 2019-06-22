package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 个人印章
 *
 * @author wangke
 * @create 2018-06-27 10:33
 **/
@Table(name = "T_PERSONAL_SEAL")
public class PersonalSeal extends BaseEntity {

    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 印章样式
     */
    @Column(name = "`SEAL_STYLE`")
    private String sealStyle;

    /**
     * 印章图片保存地址
     */
    @Column(name = "`SEAL_IMG_PATH`")
    private String sealImgPath;

    /**
     * 印章类型
     */
    @Column(name = "`SEAL_TYPE`")
    private Integer sealType;

    /**
     * 保存时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    public PersonalSeal() {
    }

    public PersonalSeal(Integer userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSealStyle() {
        return sealStyle;
    }

    public void setSealStyle(String sealStyle) {
        this.sealStyle = sealStyle;
    }

    public String getSealImgPath() {
        return sealImgPath;
    }

    public void setSealImgPath(String sealImgPath) {
        this.sealImgPath = sealImgPath;
    }

    public Integer getSealType() {
        return sealType;
    }

    public void setSealType(Integer sealType) {
        this.sealType = sealType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
