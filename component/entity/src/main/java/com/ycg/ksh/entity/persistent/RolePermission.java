package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`role_permission_tab`")
public class RolePermission extends BaseEntity{

	private static final long serialVersionUID = -3925772924847378162L;

	/**
     * Id自增
     */
	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色id
     */
    @Column(name = "`roleid`")
    private Integer roleid;

    /**
     * 权限id
     */
    @Column(name = "`permissionid`")
    private Integer permissionid;

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
     * 获取权限id
     *
     * @return permissionid - 权限id
     */
    public Integer getPermissionid() {
        return permissionid;
    }

    /**
     * 设置权限id
     *
     * @param permissionid 权限id
     */
    public void setPermissionid(Integer permissionid) {
        this.permissionid = permissionid;
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