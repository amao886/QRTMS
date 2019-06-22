package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/8
 */

import com.ycg.ksh.constant.SysRoleType;
import com.ycg.ksh.entity.persistent.CompanyEmployee;
import com.ycg.ksh.entity.persistent.ManagingUsers;
import com.ycg.ksh.entity.persistent.SysRole;
import com.ycg.ksh.entity.persistent.User;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 验证用户
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/8
 */
public class AuthorizeUser extends User {


    private SysRole sysRole;
    private SysRoleType roleType;

    private String token;
    private CompanyEmployee employee;

    private Integer adminKey;
    private ManagingUsers manager;

    private Collection<String> authorityKeys;//权限code

    private Long companyKey;
    private String companyName;
    private Integer fettle;     //是否认证: 0:未认证,1:已认证
    private Integer identityKey;     //用户身份标识(1:司机,2:发货发,3:承运方,4:收货方)

    private Collection<Integer> groupKeys;//项目组编号

    public AuthorizeUser() {}

    public AuthorizeUser(User user) {
        super();
        try {
            BeanUtils.copyProperties(this, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modify(User user) {
        try {
            BeanUtils.copyProperties(this, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean getHasGroup(){
        return groupKeys != null && groupKeys.size() > 0;
    }

    public boolean getAdmin(){
        return manager != null && getRoleType().isSuper();
    }

    public SysRole getSysRole() {
        return sysRole;
    }

    public void setSysRole(SysRole sysRole) {
        if(sysRole != null){
            setRoleType(SysRoleType.convert(sysRole.getRoleCategory()));
        }
        this.sysRole = sysRole;
    }

    public SysRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(SysRoleType roleType) {
        this.roleType = roleType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CompanyEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(CompanyEmployee employee) {
        this.employee = employee;
    }

    public Integer getAdminKey() {
        return adminKey;
    }

    public void setAdminKey(Integer adminKey) {
        this.adminKey = adminKey;
    }

    public ManagingUsers getManager() {
        return manager;
    }

    public void setManager(ManagingUsers manager) {
        this.manager = manager;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Collection<String> getAuthorityKeys() {
        return authorityKeys;
    }

    public void setAuthorityKeys(Collection<String> authorityKeys) {
        this.authorityKeys = authorityKeys;
    }

    public Collection<Integer> getGroupKeys() {
        return groupKeys;
    }

    public void setGroupKeys(Collection<Integer> groupKeys) {
        this.groupKeys = groupKeys;
    }

    public Integer getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(Integer identityKey) {
        this.identityKey = identityKey;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }
}
