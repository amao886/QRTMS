package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/2
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.SysMenu;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * 用户菜单
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/2
 */
public class AuthorityMenu extends BaseEntity {

    private Integer id;
    private String menuName;
    private String menuUrl;
    private String menuIcon;
    private String code;
    private Integer type;
    private Integer fettle;
    private String idKey;
    private boolean leaf = false;


    private Collection<AuthorityMenu> children;

    public AuthorityMenu() {
    }

    public AuthorityMenu(SysMenu menu) {
        setId(menu.getId());
        setMenuName(menu.getMenuName());
        setMenuUrl(menu.getMenuUrl());
        setMenuIcon(menu.getMenuIcon());
        setCode(menu.getCode());
        setType(menu.getType());
        setFettle(menu.getMenuFettle());
        setIdKey(menu.getIdKey());
    }
    public AuthorityMenu(SysMenu menu, Collection<AuthorityMenu> children) {
        this(menu);
        setChildren(children);
    }

    public boolean available(){
        return isLeaf() || CollectionUtils.isNotEmpty(getChildren());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Collection<AuthorityMenu> getChildren() {
        return children;
    }

    public void setChildren(Collection<AuthorityMenu> children) {
        this.children = children;
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

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
