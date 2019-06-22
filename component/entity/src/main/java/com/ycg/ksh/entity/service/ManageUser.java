package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/2
 */

import com.ycg.ksh.entity.persistent.ManagingUsers;
import com.ycg.ksh.entity.persistent.SysRole;

/**
 * 管理用户
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/2
 */
public class ManageUser extends ManagingUsers {

    private SysRole role;

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }
}
