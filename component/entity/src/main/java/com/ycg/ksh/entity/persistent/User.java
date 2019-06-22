package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.UserUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:11:34
 */
@Table(name = "`user_tab`")
public class User extends BaseEntity {

	private static final long serialVersionUID = -6288371751310547090L;

	/**
     * 用户Id自增
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "`username`")
    private String username;

    /**
     * 用户密码
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 用户微信openid
     */
    @Column(name = "`openid`")
    private String openid;

    /**
     * 手机号
     */
    @Column(name = "`mobilephone`")
    private String mobilephone;

    /**
     * 姓名
     */
    @Column(name = "`uname`")
    private String uname;
    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`updatetime`")
    private Date updatetime;


    /**
     * 微信头像
     */
    @Column(name = "`head_img`")
    private String headImg;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;

    public User() {
		super();
	}

	public User(Integer id, String mobilephone) {
        super();
        this.id = id;
        this.mobilephone = mobilephone;
    }

	public User(String username, String password, String openid, String uname) {
		super();
		this.username = username;
		this.password = password;
		this.openid = openid;
		this.uname = uname;
	}

	/**
     * 获取用户Id自增
     *
     * @return id - 用户Id自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户Id自增
     *
     * @param id 用户Id自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取用户密码
     *
     * @return password - 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户微信openid
     *
     * @return openid - 用户微信openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置用户微信openid
     *
     * @param openid 用户微信openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * 获取手机号
     *
     * @return mobilephone - 手机号
     */
    public String getMobilephone() {
        return mobilephone;
    }

    /**
     * 设置手机号
     *
     * @param mobilephone 手机号
     */
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    /**
     * 获取姓名
     *
     * @return uname - 姓名
     */
    public String getUname() {
        return uname;
    }

    /**
     * 设置姓名
     *
     * @param uname 姓名
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * 获取创建时间
     *
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取修改时间
     *
     * @return updatetime - 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置修改时间
     *
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
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
    
	public String getUnamezn() {
		return UserUtil.decodeName(uname);
    }

    public String getHeadImg() {
        if(headImg != null && headImg.length() > 0){
            return headImg;
        }
        return null;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}