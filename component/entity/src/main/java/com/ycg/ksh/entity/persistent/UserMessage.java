package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:14:28
 */
@Table(name = "`msg_user_tab`")
public class UserMessage extends BaseEntity {

	private static final long serialVersionUID = 6366182687469131548L;

	/**
     * 主键id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "`userId`")
    private Integer userid;

    /**
     * 创建时间
     */
    @Column(name = "`createTime`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`updateTime`")
    private Date updatetime;

    /**
     * 0微信端未读1已读
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 绑码id
     */
    @Column(name = "`barcodeid`")
    private Integer barcodeid;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户id
     *
     * @return userId - 用户id
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
     * 获取创建时间
     *
     * @return createTime - 创建时间
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
     * @return updateTime - 修改时间
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
     * 获取0微信端未读1已读
     *
     * @return status - 0微信端未读1已读
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0微信端未读1已读
     *
     * @param status 0微信端未读1已读
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取绑码id
     *
     * @return barcodeid - 绑码id
     */
    public Integer getBarcodeid() {
        return barcodeid;
    }

    /**
     * 设置绑码id
     *
     * @param barcodeid 绑码id
     */
    public void setBarcodeid(Integer barcodeid) {
        this.barcodeid = barcodeid;
    }
}