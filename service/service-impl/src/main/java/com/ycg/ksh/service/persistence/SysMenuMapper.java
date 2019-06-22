package com.ycg.ksh.service.persistence;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.SysMenu;
import com.ycg.ksh.entity.service.AuthorityMenu;

import java.util.Collection;

public interface SysMenuMapper extends CustomMapper<SysMenu> {

    /**
     * 查询员工权限菜单
     *
     * @param cKey 企业编号
     * @param eKey 员工编号
     * @param pKey 父级权限编号
     * @return
     */
    Collection<AuthorityMenu> listEmployeeAuthorityByPKey(Long cKey, Integer eKey, Integer pKey, Integer menuType);


    /**
     * @return
     */
    Collection<SysMenu> listAll();
    /**
     * @param roleKey
     * @param menuType
     *
     * @return
     */
    Collection<SysMenu> listAllByRoleKey(Integer roleKey, Integer menuType);
    /**
     * 根据角色编号和父级菜单编号查询用户菜单数据
     *
     * @param roleKey
     * @param pKey
     * @return
     */
    Collection<AuthorityMenu> listByRoleKey(Integer roleKey, Integer pKey, Integer menuType);


    /**
     * 查询父节点
     * @param pKey
     * @return
     */
    Collection<AuthorityMenu> listByPKey(Integer pKey, Integer menuType);

    /**
     * 根据父节点查询子节点
     * @return
     */
    Collection<AuthorityMenu> listByEmployeePid(Long cKey, Integer eKey,Integer menuType);
}