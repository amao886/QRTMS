package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 项目组成员
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:46:14
 */
@Table(name = "`group_members_tab`")
public class ProjectGroupMember extends BaseEntity {

	private static final long serialVersionUID = 6430753278839698586L;

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
     * 添加时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 成员状态（10:有效群成员 ； 20：已删除的群成员）
     */
    @Column(name = "`status`")
    private Integer status;

    
    
    /**
     * 创建一个新的 ProjectGroupMember实例. 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:02:19
     */
    public ProjectGroupMember() {
        super();
    }

    /**
     * 创建一个新的 ProjectGroupMember实例. 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:02:12
     * @param userid
     * @param groupid
     * @param createtime
     * @param status
     */
    public ProjectGroupMember(Integer groupid, Integer userid, Date createtime, Integer status) {
        super();
        this.userid = userid;
        this.groupid = groupid;
        this.createtime = createtime;
        this.status = status;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
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
     * 获取添加时间
     *
     * @return createtime - 添加时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置添加时间
     *
     * @param createtime 添加时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取成员状态（10:有效群成员 ； 20：已删除的群成员）
     *
     * @return status - 成员状态（10:有效群成员 ； 20：已删除的群成员）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置成员状态（10:有效群成员 ； 20：已删除的群成员）
     *
     * @param status 成员状态（10:有效群成员 ； 20：已删除的群成员）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}