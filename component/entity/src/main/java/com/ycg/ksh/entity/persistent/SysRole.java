package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_SYS_ROLE`")
public class SysRole extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "`ROLE_NAME`")
    private String roleName;

    /**
     * 角色状态(1:启用，0:禁用)
     */
    @Column(name = "`ROLE_FETTLE`")
    private Integer roleFettle;

    /**
     * 角色类别(1:普通角色，9999:超级管理员)
     */
    @Column(name = "`ROLE_CATEGORY`")
    private Integer roleCategory;

    /**
     * 获取主键
     *
     * @return ID - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取角色名称
     *
     * @return ROLE_NAME - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取角色状态(1:启用，0:禁用)
     *
     * @return ROLE_FETTLE - 角色状态(1:启用，0:禁用)
     */
    public Integer getRoleFettle() {
        return roleFettle;
    }

    /**
     * 设置角色状态(1:启用，0:禁用)
     *
     * @param roleFettle 角色状态(1:启用，0:禁用)
     */
    public void setRoleFettle(Integer roleFettle) {
        this.roleFettle = roleFettle;
    }

    /**
     * 获取角色类别(1:普通角色，9999:超级管理员)
     *
     * @return ROLE_CATEGORY - 角色类别(1:普通角色，9999:超级管理员)
     */
    public Integer getRoleCategory() {
        return roleCategory;
    }

    /**
     * 设置角色类别(1:普通角色，9999:超级管理员)
     *
     * @param roleCategory 角色类别(1:普通角色，9999:超级管理员)
     */
    public void setRoleCategory(Integer roleCategory) {
        this.roleCategory = roleCategory;
    }
}