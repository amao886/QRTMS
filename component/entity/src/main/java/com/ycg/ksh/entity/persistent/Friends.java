package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`friends_tab`")
public class Friends extends BaseEntity {

    private static final long serialVersionUID = -633206220153151380L;
    /**
     * 主键自增
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 当前用户Id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 好友手机号
     */
    @Column(name = "`mobile_phone`")
    private String mobilePhone;

    /**
     * 好友姓名
     */
    @Column(name = "`full_name`")
    private String fullName;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 所属公司
     */
    @Column(name = "`company`")
    private String company;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;

    /**
     * 对应user_tab id
     */
    @Column(name = "`pid`")
    private Integer pid;

    /**
     * 好友类型
     */
    @Column(name = "`friends_type`")
    private Integer friendsType;

    public Integer getFriendsType() {
        return friendsType;
    }

    public void setFriendsType(Integer friendsType) {
        this.friendsType = friendsType;
    }

    public Friends() {
        super();
    }

    public Friends(Integer userid, Integer pid) {
        super();
        this.userid = userid;
        this.pid = pid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 获取主键自增
     *
     * @return id - 主键自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键自增
     *
     * @param id 主键自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取当前用户Id
     *
     * @return userid - 当前用户Id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置当前用户Id
     *
     * @param userid 当前用户Id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取好友手机号
     *
     * @return mobile_phone - 好友手机号
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 设置好友手机号
     *
     * @param mobilePhone 好友手机号
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * 获取好友姓名
     *
     * @return full_name - 好友姓名
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 设置好友姓名
     *
     * @param fullName 好友姓名
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取所属公司
     *
     * @return company - 所属公司
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置所属公司
     *
     * @param company 所属公司
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}