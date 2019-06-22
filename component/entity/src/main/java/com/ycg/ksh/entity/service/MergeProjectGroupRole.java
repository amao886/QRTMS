/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:38:55
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ProjectGroupPermission;
import com.ycg.ksh.entity.persistent.ProjectGroupRole;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 项目角色信息,包含角色权限
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:38:55
 */
public class MergeProjectGroupRole extends ProjectGroupRole {

    private static final long serialVersionUID = -2609205165845893067L;
    
    private Collection<ProjectGroupPermission> permissionList;
    
    public MergeProjectGroupRole() {
        super();
    }
    public MergeProjectGroupRole(ProjectGroupRole groupRole) throws Exception {
        super();
        BeanUtils.copyProperties(this, groupRole);
    }
    /**
     * getter method for permissionList
     * @return the permissionList
     */
    public Collection<ProjectGroupPermission> getPermissionList() {
        return permissionList;
    }
    /**
     * setter method for permissionList
     * @param permissionList the permissionList to set
     */
    public void setPermissionList(Collection<ProjectGroupPermission> permissionList) {
        this.permissionList = permissionList;
    }
}
