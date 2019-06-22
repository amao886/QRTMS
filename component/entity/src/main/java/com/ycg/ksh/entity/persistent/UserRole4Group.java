package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户项目组角色
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:14:40
 */
@Table(name = "`user_role_tab`")
public class UserRole4Group extends BaseEntity {

	private static final long serialVersionUID = -6118708561518982769L;

	/**
     * Id自增
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 组id
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 角色id
     */
    @Column(name = "`roleid`")
    private Integer roleid;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    
    
    /**
     * 创建一个新的 UserRole4Group实例. 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:33:14
     */
    public UserRole4Group() {
        super();
    }

    /**
     * 创建一个新的 UserRole4Group实例. 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:32:31
     * @param groupid
     * @param userid
     * @param roleid
     */
    public UserRole4Group(Integer groupid, Integer userid, Integer roleid) {
        super();
        this.groupid = groupid;
        this.userid = userid;
        this.roleid = roleid;
        this.createtime = new Date();
    }

    /**
     * 创建一个新的 UserRole4Group实例. 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 12:44:17
     * @param groupid
     * @param userid
     */
    public UserRole4Group(Integer groupid, Integer userid) {
        super();
        this.userid = userid;
        this.groupid = groupid;
    }

    /**
     * 获取Id自增
     *
     * @return id - Id自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置Id自增
     *
     * @param id Id自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return userid - 用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取组id
     *
     * @return groupid - 组id
     */
    public Integer getGroupid() {
        return groupid;
    }

    /**
     * 设置组id
     *
     * @param groupid 组id
     */
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    /**
     * 获取角色id
     *
     * @return roleid - 角色id
     */
    public Integer getRoleid() {
        return roleid;
    }

    /**
     * 设置角色id
     *
     * @param roleid 角色id
     */
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
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
}