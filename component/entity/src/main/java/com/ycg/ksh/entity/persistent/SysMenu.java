package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_SYS_MENU`")
public class SysMenu extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 父级主键
     */
    @Column(name = "`P_ID`")
    private Integer pId;

    /**
     * 菜单名称
     */
    @Column(name = "`MENU_NAME`")
    private String menuName;

    /**
     * 菜单地址
     */
    @Column(name = "`MENU_URL`")
    private String menuUrl;

    /**
     * 菜单状态(1:启用，0:禁用)
     */
    @Column(name = "`MENU_FETTLE`")
    private Integer menuFettle;

    /**
     * 菜单图标
     */
    @Column(name = "`MENU_ICON`")
    private String menuIcon;

    @Column(name = "`CODE`")
    private String code;
    @Column(name = "`MENU_TYPE`")
    private Integer type;
    @Column(name = "`ID_KEY`")
    private String idKey;
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
     * 获取父级主键
     *
     * @return P_ID - 父级主键
     */
    public Integer getpId() {
        return pId;
    }

    /**
     * 设置父级主键
     *
     * @param pId 父级主键
     */
    public void setpId(Integer pId) {
        this.pId = pId;
    }

    /**
     * 获取菜单名称
     *
     * @return MENU_NAME - 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置菜单名称
     *
     * @param menuName 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * 获取菜单地址
     *
     * @return MENU_URL - 菜单地址
     */
    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * 设置菜单地址
     *
     * @param menuUrl 菜单地址
     */
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    /**
     * 获取菜单状态(1:启用，0:禁用)
     *
     * @return MENU_FETTLE - 菜单状态(1:启用，0:禁用)
     */
    public Integer getMenuFettle() {
        return menuFettle;
    }

    /**
     * 设置菜单状态(1:启用，0:禁用)
     *
     * @param menuFettle 菜单状态(1:启用，0:禁用)
     */
    public void setMenuFettle(Integer menuFettle) {
        this.menuFettle = menuFettle;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }
}