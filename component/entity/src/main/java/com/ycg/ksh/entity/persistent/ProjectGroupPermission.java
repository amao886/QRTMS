package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * 项目组权限
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:46:40
 */
@Table(name = "`permission_tab`")
public class ProjectGroupPermission extends BaseEntity {

	private static final long serialVersionUID = -3367316603453245326L;

	/**
     * Id自增
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 权限名称
     */
    @Column(name = "`permissionname`")
    private String permissionname;

    /**
     * 权限代号
     */
    @Column(name = "`permissioncode`")
    private String permissioncode;

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
     * 获取权限名称
     *
     * @return permissionname - 权限名称
     */
    public String getPermissionname() {
        return permissionname;
    }

    /**
     * 设置权限名称
     *
     * @param permissionname 权限名称
     */
    public void setPermissionname(String permissionname) {
        this.permissionname = permissionname;
    }

    /**
     * 获取权限代号
     *
     * @return permissioncode - 权限代号
     */
    public String getPermissioncode() {
        return permissioncode;
    }

    /**
     * 设置权限代号
     *
     * @param permissioncode 权限代号
     */
    public void setPermissioncode(String permissioncode) {
        this.permissioncode = permissioncode;
    }
}