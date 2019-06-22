/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
 */
package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.EmployeeType;
import com.ycg.ksh.constant.MenuType;
import com.ycg.ksh.constant.SysRoleType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.AuthorityMenu;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.MergeAuthorityMenu;
import com.ycg.ksh.entity.service.RoleAndPermission;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.api.PermissionService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.api.WaybillService;
import com.ycg.ksh.service.observer.OrderObserverAdapter;
import com.ycg.ksh.service.observer.ProjectGroupObserverAdapter;
import com.ycg.ksh.service.observer.UserObserverAdapter;
import com.ycg.ksh.service.util.AuthorityUtil;
import com.ycg.ksh.service.util.O;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 权限(项目组权限、运单权限)
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
 */
@Service("ksh.core.service.permissionService")
public class PermissionServiceImpl implements PermissionService , UserObserverAdapter, ProjectGroupObserverAdapter, OrderObserverAdapter {
    
    private static final String GP_CACHE_KEY_PREFIX = "GP";
    private static final String GR_CACHE_KEY_PREFIX = "GR";
	private static final String SA_CACHE_KEY_PREFIX = "SA";//权限
    
    @Resource
    private CacheManager cacheManger;
    @Resource
    private ProjectGroupMapper groupMapper;
    @Resource
    private ProjectGroupRoleMapper groupRoleMapper;
    @Resource
    private ProjectGroupPermissionMapper groupPermissionMapper;
    @Resource
    private UserRole4GroupMapper userRole4GroupMapper;
    @Resource
    private ProjectGroupMemberMapper groupMemberMapper;
    
    @Resource
    private WaybillService waybillService;
    @Resource
    private RolePermissionMapper rolePermissionMapper;

	@Resource
	private UserService userService;
	@Resource
	SysMenuMapper menuMapper;
	@Resource
	SysRoleMapper roleMapper;
	@Resource
	EmployeeAuthorityMapper employeeAuthorityMapper;
	@Resource
	CompanyMapper companyMapper;
    
    private String cachePermissionKey(Integer groupKey, Integer userKey) {
        return GP_CACHE_KEY_PREFIX +"#"+ userKey +"#"+ groupKey;
    }
    
    private String cacheRoleKey(Integer groupKey, Integer userKey) {
        return GR_CACHE_KEY_PREFIX +"#"+ userKey +"#"+ groupKey;
    }

	private String cacheAuthorityKey(Integer uKey) {
		return SA_CACHE_KEY_PREFIX +"#"+ uKey;
	}
    /**
     * @see com.ycg.ksh.service.api.PermissionService#cleanCachePermission(java.lang.Integer, java.lang.Integer)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
     */
    @Override
    public void cleanCachePermission(Integer gKey, Integer uKey) throws ParameterException, BusinessException {
        cacheManger.delete(cachePermissionKey(gKey, uKey));
        cacheManger.delete(cacheRoleKey(gKey, uKey));
        logger.info("清除权限缓存数据  gKey {} uKey {}", gKey, uKey);
    }
    
    /**
     * @see com.ycg.ksh.service.api.PermissionService#validate(java.lang.Integer, java.lang.Integer, java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
     */
    @Override
    public boolean validate(Integer gKey, Integer uKey, String pKey) throws ParameterException, BusinessException {
        Collection<Serializable> collection = loadPermissions(gKey, uKey);
        if(CollectionUtils.isNotEmpty(collection)) {
            return collection.contains(pKey);
        }
        return false;
    }
    
    /**
     * @see com.ycg.ksh.service.api.PermissionService#validateByWaybillID(java.lang.Integer, java.lang.Integer, java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
     */
    @Override
    public boolean validateByWaybillID(Integer wKey, Integer uKey, String pKey) throws ParameterException, BusinessException {
		Waybill waybill = waybillService.getWaybillById(wKey);
		if(waybill != null){
			if(waybill.getGroupid() != null && waybill.getGroupid() > 0){
				return validate(waybill.getGroupid(), uKey, pKey);
			}
			return waybill.getUserid() - uKey == 0;
		}
        return false;
    }
    
    @SuppressWarnings("unchecked")
	private Collection<Serializable> loadPermissions(Integer groupKey, Integer userKey){
        String cacheKey = cachePermissionKey(groupKey, userKey);
        Collection<Serializable> collection = null;
        Object cacheObject = cacheManger.get(cacheKey);
        if(cacheObject != null) {
            collection = (Collection<Serializable>) cacheObject;
        }
        if(CollectionUtils.isEmpty(collection)) {
            collection = cachePermissions(groupKey, userKey);
            if(CollectionUtils.isNotEmpty(collection)) {
                cacheManger.set(cacheKey, collection, 2L, TimeUnit.HOURS);
            }
        }
        return collection;
    }
    
    private Collection<Serializable> cachePermissions(Integer groupKey, Integer userKey){
        Collection<ProjectGroupPermission> permissions = groupPermissionMapper.selectByGroupUser(groupKey, userKey);
        if(CollectionUtils.isNotEmpty(permissions)) {
            Collection<Serializable> collection = new ArrayList<Serializable>(permissions.size());
            for (ProjectGroupPermission projectGroupPermission : permissions) {
                if(!collection.contains(projectGroupPermission.getPermissioncode())) {
                    collection.add(projectGroupPermission.getPermissioncode());
                }
            }
            return collection;
        }
        return null;
    }
    
    /**
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
     */
    @Override
    public ProjectGroupRole getProjectGroupRole(Integer gKey, Integer uKey) throws ParameterException, BusinessException {
        Assert.notBlank(gKey, "项目组编号不能为空");
        Assert.notBlank(uKey, "组员用户编号不能为空");
        String cacheKey = cacheRoleKey(gKey, uKey);
        Object object = cacheManger.get(cacheKey);
        if(object == null) {
            ProjectGroupRole groupRole = groupRoleMapper.getRoleByUserKey(gKey, uKey);
            if(groupRole != null) {
                cacheManger.set(cacheKey, groupRole, 2L, TimeUnit.HOURS);
            }
            return groupRole;
        }
        return (ProjectGroupRole) object;
    }
    /**
     * @see com.ycg.ksh.service.api.PermissionService#updateGroupRoleByUserKey(java.lang.Integer, java.lang.Integer, int)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:25:21
     */
    @Override
    public void updateGroupRoleByUserKey(Integer gKey, Integer uKey, int rKey) throws ParameterException, BusinessException {
        Assert.notBlank(gKey, "项目组编号不能为空");
        Assert.notBlank(uKey, "组员用户编号不能为空");
        Assert.notBlank(rKey, "角色编号不能为空");
        if(groupMemberMapper.isMember(gKey, uKey) <= 0) {
            throw new BusinessException("该用户不是该组的成员");
        }
        UserRole4Group userRole = new UserRole4Group(gKey, uKey, rKey);
        Example example = new Example(UserRole4Group.class);
        example.createCriteria().andEqualTo("groupid", gKey).andEqualTo("userid", uKey);
        userRole4GroupMapper.updateByExampleSelective(userRole, example);
        cleanCachePermission(gKey, uKey);
    }

	@Override
	public void notifyUserChange(AuthorizeUser authorizeUser, Integer type, UserContext context) throws BusinessException {
		//logger.info("收到用户登录通知 -> {} type:{}", authorizeUser, type);
		if(authorizeUser == null){
			return;
		}
		if (CoreConstants.USER_REFRESH - type == 0) {//退出登陆
			if(CollectionUtils.isNotEmpty(authorizeUser.getGroupKeys())){
				for (Integer groupKey : authorizeUser.getGroupKeys()) {
					cleanCachePermission(groupKey, authorizeUser.getId());
				}
			}
			cacheManger.delete(cacheAuthorityKey(authorizeUser.getId()));
		}
	}

	/**
	 * 初始化用户权限数据
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 15:18:54
     */
    @Override
    public void initializeUser(AuthorizeUser authorizeUser, Integer type) throws BusinessException {
        //logger.info("收到用户登录通知 -> {} type:{}", authorizeUser, type);
		if (CoreConstants.USER_AUTHORITY - type == 0) {//加载用户权限
			if(authorizeUser.getHasGroup()){
				for (Integer groupKey : authorizeUser.getGroupKeys()) {
					cleanCachePermission(groupKey, authorizeUser.getId());
					Collection<Serializable> collection = cachePermissions(groupKey, authorizeUser.getId());
					if(CollectionUtils.isNotEmpty(collection)) {
						cacheManger.set(cachePermissionKey(groupKey, authorizeUser.getId()), collection, 2L, TimeUnit.HOURS);
					}
					ProjectGroupRole groupRole = groupRoleMapper.getRoleByUserKey(groupKey, authorizeUser.getId());
					if(groupRole != null) {
						cacheManger.set(cacheRoleKey(groupKey, authorizeUser.getId()), groupRole, 2L, TimeUnit.HOURS);
					}
				}
			}
			ManagingUsers manager = authorizeUser.getManager();
			if (manager != null) {
				//-----------------
				authorizeUser.setSysRole(roleMapper.selectByPrimaryKey(Optional.ofNullable(manager.getUserType()).orElse(SysRoleType.ENTERPRISE.getKey())));
			} else {
				//加载企业角色
				authorizeUser.setSysRole(roleMapper.selectByPrimaryKey(SysRoleType.ENTERPRISE.getKey()));
			}
			//转换权限code
			authorizeUser.setAuthorityKeys(AuthorityUtil.reduceAuthority(loadAuthoritys(authorizeUser), AuthorityMenu::getCode));
		}
    }
	/**
	 * 根据用户编号加载用户权限菜单
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 15:18:54
	 */
	@Override
	public Collection<AuthorityMenu> loadAuthoritys(Integer userKey, MenuType menuType) throws ParameterException, BusinessException {
        Collection<AuthorityMenu> collection = loadAuthoritys(userService.get(userKey));
        if(CollectionUtils.isNotEmpty(collection) && menuType != null){
            return AuthorityUtil.filterAuthority(collection, m -> m.getType() != null && m.getType() - menuType.getCode() == 0);
        }
        return collection;
	}

	@Override
	public Collection<String> loadAuthorityCodes(Integer userKey, MenuType menuType) throws ParameterException, BusinessException{
		return AuthorityUtil.reduceAuthority(loadAuthoritys(userService.get(userKey)), AuthorityMenu::getCode);
	}

	private Collection<AuthorityMenu> listAuthority(SysRoleType role, Predicate<SysMenu> predicate){
	    Collection<SysMenu> collection = role.isSuper() ? menuMapper.listAll() : menuMapper.listAllByRoleKey(role.getKey(), role.getType());
        if(CollectionUtils.isNotEmpty(collection)){
            return AuthorityUtil.cycleConverts(collection, predicate);
        }
        return Collections.emptyList();
    }


	private Collection<AuthorityMenu> loadAuthoritys(AuthorizeUser authorizeUser) {
		//return cacheManger.get(cacheAuthorityKey(authorizeUser.getId()), 60L, ()->{
        Integer idKey = authorizeUser.getIdentityKey();
        SysRoleType roleType = authorizeUser.getRoleType();
        if(roleType.isSuper()){//超级管理员
            return listAuthority(SysRoleType.SUPER, m-> AuthorityUtil.validateIdentityKey(m.getIdKey(), idKey));
        }else{
            Collection<AuthorityMenu> collection = new ArrayList<AuthorityMenu>();
            if(authorizeUser.getHasGroup()){
                collection.addAll(listAuthority(SysRoleType.GROUP, m -> true));
            }
            CompanyEmployee employee = authorizeUser.getEmployee();
            if(employee == null || EmployeeType.convert(employee.getEmployeeType()).isManage()){//不是员工或者是企业管理员，根据角色加载权限菜单
                collection.addAll(listAuthority(roleType, m -> AuthorityUtil.validateIdentityKey(m.getIdKey(), idKey)));
            }else{
                collection.addAll(listAuthorityMenu(employee.getCompanyId(), employee.getEmployeeId(), m-> AuthorityUtil.validateIdentityKey(m.getIdKey(), idKey) && m.getType() != null && roleType.getType() - m.getType() == 0));//查询员工在企业里的权限列表
            }
            return collection;
        }
		//});
	}

	/*private Collection<AuthorityMenu> loadByType(Integer roleKey, SysRoleType roleType, Integer idKey){
		Collection<AuthorityMenu> collection = new ArrayList<AuthorityMenu>();
		if(roleType.isSuper()){
			collection = menuMapper.listByPKey(0, roleType.getType());
			if (CollectionUtils.isNotEmpty(collection)) {
				for (AuthorityMenu authorityMenu : collection) {
					authorityMenu.setChildren(menuMapper.listByPKey(authorityMenu.getId(), roleType.getType()));
				}
			}
		}else{
			collection = menuMapper.listByRoleKey(roleKey, 0, roleType.getType());
			if (CollectionUtils.isNotEmpty(collection)) {
				for (AuthorityMenu authorityMenu : collection) {
					authorityMenu.setChildren(menuMapper.listByRoleKey(roleKey, authorityMenu.getId(), roleType.getType()));
				}
			}
		}
		return collection.stream().filter(c-> validate(c.getIdKey(), idKey)).peek(m->{
			if(CollectionUtils.isNotEmpty(m.getChildren())){
				m.setChildren(m.getChildren().stream().filter(cc-> validate(cc.getIdKey(), idKey)).collect(Collectors.toList()));
			}
		}).filter(c-> CollectionUtils.isNotEmpty(c.getChildren())).collect(Collectors.toList());
	}*/

    @Override
    public void notifyProjectGroupChange(Integer gKey, Integer uKey, Integer type) throws BusinessException {
        logger.info("收到项目组信息变化通知 -> gKey:{} uKey:{} type:{}", gKey, uKey, type);
        if(PROJECT_GROUP_MEMBER_DELETE - type == 0 || PROJECT_GROUP_ROLE_CHANGE - type == 0) {
            cleanCachePermission(gKey, uKey);
        }
    }

    /**
     * 
     * @see com.ycg.ksh.service.api.PermissionService#queryRoleAndPermission()
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-04 11:47:16
     */
	@Override
	public Collection<RoleAndPermission> queryRoleAndPermission() throws ParameterException, BusinessException {
		try {
			Collection<RolePermission> list = Optional.ofNullable(rolePermissionMapper.queryRolePermission()).orElse(Collections.emptyList());
			Collection<RoleAndPermission> result = new ArrayList<>(list.size());
			for (RolePermission rolePermission : list) {
				RoleAndPermission rp = initRoleAndPermission(rolePermission);
				result.add(rp);
			}
			return result;
		} catch (ParameterException | BusinessException e) {
			throw e;
		}catch (Exception e) {
			logger.debug("queryRoleAndPermission e:{}",e);
            throw BusinessException.dbException("查询角色和角色对应权限异常");
		}
	}
	/**
	 * 
	 * TODO 数据封装
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-05 16:46:03
	 * @param rolePermission
	 * @return
	 * @throws Exception
	 */
	private RoleAndPermission initRoleAndPermission(RolePermission rolePermission) throws Exception {
		Integer roleId = rolePermission.getRoleid();
		RoleAndPermission rp = new RoleAndPermission(rolePermission);
		ProjectGroupRole projectGroupRole = groupRoleMapper.selectByPrimaryKey(roleId);
		rp.setRoleName(projectGroupRole.getRolename());
		rp.setRoleCode(projectGroupRole.getRolecode());
		rp.setProjectGroupPermissions(groupPermissionMapper.selectByRoleKey(roleId));
		return rp;
	}

	@Override
	public Collection<ProjectGroupPermission> queryAllPermission() throws ParameterException, BusinessException {
		try {
			return groupPermissionMapper.selectAll(); 
		} catch (Exception e) {
			logger.debug("queryAllPermission e:{}",e);
			throw BusinessException.dbException("查询权限异常");
		}
	}

	@Override
	public void saveRole(RoleAndPermission roleAndPermission) throws ParameterException, BusinessException {
		Assert.notBlank(roleAndPermission.getRoleName(), "角色名称不能为空");
		Assert.notBlank(roleAndPermission.getRoleCode(), "角色编号不能为空");
		Assert.notEmpty(roleAndPermission.getProjectGroupPermissions(), "权限编号不能为空");
		try {
			String roleName = roleAndPermission.getRoleName();
			String roleCode = roleAndPermission.getRoleCode();
			ProjectGroupRole projectGroupRole = new ProjectGroupRole(roleName, roleCode);
			if(groupRoleMapper.getRoleByRoleCode(roleAndPermission.getRoleCode())>0){
				Example example = new Example(ProjectGroupRole.class);
				example.createCriteria().andEqualTo("rolecode", roleCode);
				groupRoleMapper.updateByExampleSelective(projectGroupRole, example);
				if(logger.isDebugEnabled()){
					logger.debug("saveRole() update role roleCode:{}, roleId:{}", roleAndPermission.getRoleCode(), roleAndPermission.getId());
				}
			}else{
				if (groupRoleMapper.insertSelective(projectGroupRole) > 0) {
					roleAndPermission.setRoleid(projectGroupRole.getId());
					roleAndPermission.setCreatetime(new Date());
					for (ProjectGroupPermission groupPermission : roleAndPermission.getProjectGroupPermissions()) {
						roleAndPermission.setId(null);
						roleAndPermission.setPermissionid(groupPermission.getId());
						rolePermissionMapper.insertSelective(roleAndPermission);
					}
				}
			}
		} catch (Exception e) {
			logger.debug("saveRole roleAndPermission:{}, e:{}", roleAndPermission, e);
            throw BusinessException.dbException("新增角色异常");
		}
	}

	@Override
	public void deleteRole(Integer rolePermissionKey,Integer roleKey) throws ParameterException, BusinessException {
		Assert.notBlank(rolePermissionKey, "角色权限主键为空");
		Assert.notBlank(roleKey, "角色主键为空");
		if(logger.isDebugEnabled()){
			logger.debug("deleteRole====>  rolePermissionKey:{}, roleKey:{}", rolePermissionKey, roleKey);
		}
		try {
			if(rolePermissionMapper.deleteByPrimaryKey(rolePermissionKey) > 0){
				groupRoleMapper.deleteByPrimaryKey(roleKey);
			}
		} catch (Exception e) {
			logger.debug("deleteRole  rolePermissionKey:{}, e:{}",rolePermissionKey, e);
            throw BusinessException.dbException("角色删除异常");
		}
	}

	@Override
	public RoleAndPermission queryByRolePermissionKey(Integer rolePermissionKey) throws ParameterException, BusinessException {
		try {
			RolePermission rolePermission = rolePermissionMapper.selectByPrimaryKey(rolePermissionKey);
			RoleAndPermission rp = initRoleAndPermission(rolePermission);
			return rp;
		} catch (Exception e) {
			logger.debug("querybyKey  rolePermissionKey:{}, e:{}",rolePermissionKey, e);
            throw BusinessException.dbException("角色查询异常");
		}
	}

	@Override
	public void updateRolePermission(RoleAndPermission roleAndPermission) throws ParameterException, BusinessException {
		try {
			String roleName = roleAndPermission.getRoleName();
			String roleCode = roleAndPermission.getRoleCode();
			ProjectGroupRole projectGroupRole = new ProjectGroupRole(roleName, roleCode);
			projectGroupRole.setId(roleAndPermission.getRoleid());
			//更新角色
			if(groupRoleMapper.updateByPrimaryKeySelective(projectGroupRole)>0){
				//删除老权限
				if(rolePermissionMapper.deleteByRoleId(roleAndPermission.getRoleid()) > 0){
					//添加新权限
					roleAndPermission.setRoleid(projectGroupRole.getId());
					roleAndPermission.setCreatetime(new Date());
					for (ProjectGroupPermission groupPermission : roleAndPermission.getProjectGroupPermissions()) {
						roleAndPermission.setId(null);
						roleAndPermission.setPermissionid(groupPermission.getId());
						rolePermissionMapper.insertSelective(roleAndPermission);
					}
				}
			}
		} catch (ParameterException | BusinessException e) {
			throw e;
		}catch (Exception e) {
			logger.debug("updateRolePermission  roleAndPermission:{}, e:{}",roleAndPermission, e);
            throw BusinessException.dbException("角色修改异常");
		}
	}

	@Override
	public void savePermission(ProjectGroupPermission groupPermission) throws ParameterException, BusinessException {
		try {
			groupPermissionMapper.insertSelective(groupPermission);
		} catch (Exception e) {
			logger.debug("savePermission  groupPermission:{}, e:{}", groupPermission, e);
            throw BusinessException.dbException("新增权限异常");
		}
	}

	@Override
	public void deletePermission(Integer permissionId) throws ParameterException, BusinessException {
		try {
			groupPermissionMapper.deleteByPrimaryKey(permissionId);
		} catch (Exception e) {
			logger.debug("deletePermission  permissionId:{}, e:{}", permissionId, e);
            throw BusinessException.dbException("删除权限异常");
		}
	}

	@Override
	public void updatePermission(ProjectGroupPermission groupPermission) throws ParameterException, BusinessException {
		try {
			groupPermissionMapper.updateByPrimaryKeySelective(groupPermission);
		} catch (Exception e) {
			logger.debug("updatePermission  groupPermission:{}, e:{}", groupPermission, e);
            throw BusinessException.dbException("修改权限异常");
		}
	}

	@Override
	public ProjectGroupPermission queryBykey(Integer perssionKey) throws ParameterException, BusinessException {
		try {
			return groupPermissionMapper.selectByPrimaryKey(perssionKey);
		} catch (Exception e) {
			logger.debug("queryBykey  perssionKey:{}, e:{}", perssionKey, e);
            throw BusinessException.dbException("查询权限异常");
		}
	}

	private Collection<AuthorityMenu> listAuthorityMenu(Long companyKey, Integer uKey, Predicate<SysMenu> predicate){
        Collection<SysMenu> allMenus = employeeAuthorityMapper.listSysMenu(companyKey, uKey);
        if(CollectionUtils.isNotEmpty(allMenus)){
            return AuthorityUtil.cycleConverts(allMenus, predicate);
        }
        return Collections.emptyList();
    }

	/**
	 * @param operateKey 操作人编号
	 * @param companyKey 企业编号
	 * @param employeeKey  员工编号
	 * @param selectAuthorityKeys 权限编号
	 *
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	@Override
	public void saveAuthoritys(Integer operateKey, Long companyKey, Integer employeeKey, Collection<Integer> selectAuthorityKeys) throws ParameterException, BusinessException {
		Assert.notBlank(operateKey, "操作人编号不能为空");
		Assert.notBlank(companyKey, "企业编号不能为空");
		Assert.notBlank(employeeKey, "员工编号不能为空");
		Assert.notEmpty(selectAuthorityKeys, "至少分配一项菜单权限");
		try{
            final Integer identityKey = userService.loadUserIdentityKey(operateKey);
			Collection<SysMenu> collection = menuMapper.selectByIdentities(selectAuthorityKeys);
			if(CollectionUtils.isEmpty(collection)){
				throw new BusinessException("权限菜单选择有误");
			}
			/* 检查父节点 */
			final Collection<Integer> insertKeys = Stream.concat(selectAuthorityKeys.stream(), collection.stream().filter(s-> s.getpId() > 0).map(SysMenu::getpId)).distinct().collect(Collectors.toList());
			Collection<AuthorityMenu> existsCollection = listAuthorityMenu(companyKey, employeeKey, m -> true);//已经存在的权限
            if(CollectionUtils.isNotEmpty(existsCollection)){
                final Collection<Integer> deleteKeys = new ArrayList<Integer>();
                AuthorityUtil.inspectEmployeeAuthority(existsCollection, m ->{
                    if(!m.available() || m.isLeaf() && !insertKeys.contains(m.getId()) && AuthorityUtil.validateIdentityKey(m.getIdKey(), identityKey)){//如果当前权限不在选中的权限中，并且是当前身份标识所对应的权限，则需要删除
                        deleteKeys.add(m.getId());
                        return true;
                    }
                    insertKeys.remove(m.getId());//已经村则的权限，不需要新增
                    return false;
                });
                if(CollectionUtils.isNotEmpty(deleteKeys)){//删除需要删除的权限
                    Example example = new Example(EmployeeAuthority.class);
                    example.createCriteria().andEqualTo("companyId", companyKey).andEqualTo("employeeId", employeeKey).andIn("authorityId", deleteKeys);
                    employeeAuthorityMapper.deleteByExample(example);
                }
            }
            if(CollectionUtils.isNotEmpty(insertKeys)){//新增不存在的权限
                Date cdate = new Date();
                Collection<EmployeeAuthority> authorities = insertKeys.stream().map(key -> new EmployeeAuthority(Globallys.nextKey(), companyKey, employeeKey, key, cdate)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(authorities)){
                    employeeAuthorityMapper.inserts(authorities);//保存新的权限数据
                }
            }
            cacheManger.delete(cacheAuthorityKey(employeeKey));//清楚权限缓存
		}catch (BusinessException | ParameterException e){
			throw  e;
		}catch (Exception e){
			logger.error("保存员工权限异常 {} {} {}", companyKey, employeeKey, selectAuthorityKeys, e);
			throw new BusinessException("保存员工权限异常");
		}
	}

	@Override
	public Collection<AuthorityMenu> listAuthoritys(Long companyKey, Integer employeeKey, Integer idKey) throws ParameterException, BusinessException{
		Assert.notBlank(companyKey, "企业编号不能为空");
		Assert.notBlank(employeeKey, "员工编号不能为空");
		try{
			return listAuthorityMenu(companyKey, employeeKey, m->AuthorityUtil.validateIdentityKey(m.getIdKey(), idKey));
		}catch (BusinessException | ParameterException e){
			throw  e;
		}catch (Exception e){
			logger.error("查询员工权限异常 {} {}", companyKey, employeeKey, e);
			throw new BusinessException("查询员工权限异常");
		}
	}

	@Override
	public boolean validateAuthority(Integer uKey, Integer authorityKey) throws ParameterException, BusinessException {
        Collection<Integer> cacheObject = cacheManger.get(cacheAuthorityKey(uKey));
		if(cacheObject != null){
			return cacheObject.contains(authorityKey);
		}
		return false;
	}
	
	/**
	 * 微信对应菜单权限查询
	 * @see com.ycg.ksh.service.api.PermissionService#queryAuthorityMenuByUkey(java.lang.Integer)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-29 16:24:52
	 */
	@Override
	public Collection<MergeAuthorityMenu> queryAuthorityMenuByUkey(Integer uKey)
			throws ParameterException, BusinessException {
		Assert.notBlank(uKey, "用户编号不能为空");
		try {
			Company company = companyMapper.getCompanyByUserKey(uKey);
			if(company == null || company.getId() == null){
				throw new BusinessException("非企业用户");
			}
			return employeeAuthorityMapper.listAuthorityMenu(company.getId(), uKey);
		}catch (ParameterException | BusinessException e) {
			throw e;
		} catch (Exception e) {
		    logger.info("查询员工菜单权限异常->: {}", uKey, e);
			throw BusinessException.dbException("查询员工菜单权限异常");
		}
	}

	/**
	 * 联合订单数据
	 *
	 * @param uKey
	 * @param alliance
	 * @param flags
	 * @throws BusinessException
	 */
	@Override
	public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
		if(uKey != null && uKey > 0 && Arrays.binarySearch(flags, O.allowreceive) >= 0){
			alliance.setAllowReceive(validateAuthority(uKey, 48));//是否有收货管理权限
		}
	}
}
