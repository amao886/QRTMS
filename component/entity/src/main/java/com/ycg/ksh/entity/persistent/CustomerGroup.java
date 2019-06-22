package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户组实体类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:34:57
 */
@Table(name = "`customer_group_tab`")
public class CustomerGroup extends BaseEntity {

	private static final long serialVersionUID = -7075800643097433538L;

	/**
     * Id自增
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户id
     */
    @Column(name = "`customerid`")
    private Integer customerid;

    /**
     * 资源组id
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 用户id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

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
     * 获取客户id
     *
     * @return customerid - 客户id
     */
    public Integer getCustomerid() {
        return customerid;
    }

    /**
     * 设置客户id
     *
     * @param customerid 客户id
     */
    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    /**
     * 获取资源组id
     *
     * @return groupid - 资源组id
     */
    public Integer getGroupid() {
        return groupid;
    }

    /**
     * 设置资源组id
     *
     * @param groupid 资源组id
     */
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
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