package com.ycg.ksh.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.Page;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.SysRoleType;
import com.ycg.ksh.entity.persistent.ManagingUsers;
import com.ycg.ksh.entity.persistent.SysRole;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.ManageUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.persistence.ManagingUsersMapper;
import com.ycg.ksh.service.persistence.SysRoleMapper;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.api.ManagingUsersService;
import com.ycg.ksh.service.observer.UserObserverAdapter;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 后台管理用户业务实现类
 *
 * @author wangke
 * @create 2018-03-20 10:50
 **/
@Service("ksh.core.service.managingUsersService")
public class ManagingUsersServiceImpl implements ManagingUsersService, UserObserverAdapter {

    @Autowired
    ManagingUsersMapper managingUsersMapper;

    @Resource
    UserMapper userMapper;
    @Resource
    SysRoleMapper roleMapper;

    /**
     * 用户事件通知
     * <p>
     *
     * @param authorizeUser
     * @param type          事件类型
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 10:53:18
     */
    @Override
    public void initializeUser(AuthorizeUser authorizeUser, Integer type) throws BusinessException {
        if (CoreConstants.USER_LOGIN - type == 0) {
            authorizeUser.setManager(bindUser(authorizeUser.getId(), authorizeUser.getAdminKey()));
        }
    }
    @Override
    public CustomPage<ManageUser> queryUsersList(ManagingUsers managingUsers, PageScope pageScope) throws ParameterException, BusinessException {
        if (null == pageScope) {
            pageScope = PageScope.DEFAULT;
        }
        Page<ManageUser> page = managingUsersMapper.queryUsersList(managingUsers, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        if (CollectionUtils.isNotEmpty(page)) {
            for (ManageUser manageUser : page) {
                manageUser.setRole(roleMapper.selectByPrimaryKey(manageUser.getUserType()));
            }
        }
        return new CustomPage<ManageUser>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public void saveUser(int uKey, ManagingUsers manageUser) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notNull(manageUser, Constant.PARAMS_ERROR);
        Assert.notBlank(manageUser.getUsername(), "用户名称不能为空");
        Assert.notBlank(manageUser.getPassword(), "用户密码不能为空");
        Assert.notBlank(manageUser.getRealName(), "用户真实姓名不能为空");
        SysRole sysRole = roleMapper.getRole(uKey);
        if (sysRole == null || !SysRoleType.convert(sysRole.getRoleCategory()).isSuper()) {
            throw new BusinessException("没有权限添加用户");
        }
        manageUser.setCreateTime(new Date());
        if (manageUser.getUserType() == null) {
            manageUser.setUserType(SysRoleType.ENTERPRISE.getCode());
        }
        manageUser.setUserStatus(1);
        Example example = new Example(ManagingUsers.class);
        example.createCriteria().andEqualTo("username", manageUser.getUsername());
        if (managingUsersMapper.selectCountByExample(example) > 0) {
            throw new BusinessException("该用户已经被注册");
        }
        managingUsersMapper.saveUser(manageUser);
    }

    @Override
    public void updateUserInfo(int uKey, ManagingUsers manageUser) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notNull(manageUser, Constant.PARAMS_ERROR);
        Assert.notBlank(manageUser.getId(), "用户ID为空");
        /* Assert.notBlank(manageUser.getRealName(), "用户真实姓名不能为空");*/
        if (manageUser.getUserType() == null) {
            manageUser.setUserType(SysRoleType.ENTERPRISE.getCode());
        }
        managingUsersMapper.updateByPrimaryKeySelective(manageUser);
    }

    @Override
    public void deleteUser(int uKey, Integer id) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        Assert.notBlank(id, "要删除的用户ID为空");
        SysRole sysRole = roleMapper.getRole(uKey);
        if (sysRole == null || !SysRoleType.convert(sysRole.getRoleCategory()).isSuper()) {
            throw new BusinessException("没有权限删除用户");
        }
        ManagingUsers manageUser = managingUsersMapper.selectByPrimaryKey(id);
        if (manageUser == null) {
            throw new BusinessException("要删除的用户信息不存在");
        }
        ManagingUsers users = new ManagingUsers();
        users.setId(id);
        users.setUserStatus(0);
        managingUsersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public ManagingUsers queryUserById(Integer id) throws ParameterException, BusinessException {
        Assert.notBlank(id, "用户ID为空");
        return managingUsersMapper.selectByPrimaryKey(id);
    }

    @Override
    public void bindUser(ManagingUsers users) throws ParameterException, BusinessException {
        Assert.notBlank(users.getId(), "后台用户ID为空");
        Assert.notBlank(users.getUserId(), "微信用户ID为空");
        managingUsersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public ManagingUsers userlogin(ManagingUsers users) throws ParameterException, BusinessException {
        Assert.notNull(users, "用户信息不能为空");
        ManagingUsers exister = managingUsersMapper.checkUserInfo(users);
        if (null == exister) {
            throw new BusinessException("当前用户不存在");
        }
        if (!exister.getPassword().equals(users.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        exister.setLastLoginIp(users.getLastLoginIp());
        exister.setLastLoginTime(new Date());
        managingUsersMapper.updateByPrimaryKeySelective(exister);
        return exister;
    }

    @Override
    public ManagingUsers bindUser(Integer userId, Integer adminKey) throws ParameterException, BusinessException {
        Assert.notBlank(userId, "微信用户ID为空");
        try {
            ManagingUsers manager = managingUsersMapper.loadByUserID(userId);
            if (adminKey != null && adminKey > 0) {
                if (manager != null) {
                    throw new BusinessException("该用户已绑定其他帐号");
                }
                manager = managingUsersMapper.selectByPrimaryKey(adminKey);
                if (null == manager) {
                    throw new BusinessException("获取后台用户信息异常");
                }
                manager.setUserId(userId);
                managingUsersMapper.updateByPrimaryKeySelective(manager);
            }
            return manager;
        }catch (BusinessException | ParameterException e) {
        	throw e;
        } catch (Exception e) {
            logger.error("bindUser -> userId:{} adminKey:{}", userId, adminKey, e);
            throw BusinessException.dbException("管理员账号绑定异常");
        }
    }
}
