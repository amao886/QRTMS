/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 08:53:49
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.constant.MenuType;
import com.ycg.ksh.entity.persistent.ProjectGroupPermission;
import com.ycg.ksh.entity.persistent.ProjectGroupRole;
import com.ycg.ksh.entity.service.AuthorityMenu;
import com.ycg.ksh.entity.service.MergeAuthorityMenu;
import com.ycg.ksh.entity.service.RoleAndPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 权限(项目组权限、运单权限)
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 08:53:49
 */
public interface PermissionService {

    final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    //private Collection<String> loadPermissions(Integer userKey, Integer groupKey);
    /**
     * 清除权限缓存数据
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-16 16:41:25
     * @param gKey  项目组编号
     * @param uKey  用户编号
     * @throws Exception
     */
    public void cleanCachePermission(Integer gKey, Integer uKey) throws ParameterException, BusinessException;
    
    /**
     * 验证是否有权限
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-16 16:41:41
     * @param gKey  项目组编号
     * @param uKey  用户编号
     * @param pKey  权限
     * @return
     */
    public boolean validate(Integer gKey, Integer uKey, String pKey) throws ParameterException, BusinessException;
    
    /**
     * 验证运单是否有权限
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:22:24
     * @param wKey  任务单编号
     * @param uKey  用户
     * @param pKey  权限
     * @return
     */
    public boolean validateByWaybillID(Integer wKey, Integer uKey, String pKey) throws ParameterException, BusinessException;

    /**
     * 获取项目组角色
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:21:21
     * @param gKey  项目组编号
     * @param uKey  用户编号
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public ProjectGroupRole getProjectGroupRole(Integer gKey, Integer uKey) throws ParameterException, BusinessException;

    /**
     * 设置项目组角色
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 09:19:38
     * @param gKey  项目组编号
     * @param uKey  用户编号
     * @param rKey  要设置的项目组角色编号
     * @throws Exception
     */
    public void updateGroupRoleByUserKey(Integer gKey, Integer uKey, int rKey) throws ParameterException, BusinessException;
    /**
     * 
     * 项目组角色和角色对应权限查询
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-04 10:20:35
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<RoleAndPermission> queryRoleAndPermission() throws ParameterException , BusinessException;
    /**
     * 
     * 查询所有权限
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-04 14:58:18
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<ProjectGroupPermission> queryAllPermission() throws ParameterException, BusinessException;
    /**
     * 
     * 角色添加
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-04 16:41:03
     * @param roleAndPermission
     * @throws ParameterException
     * @throws BusinessException
     */
    public void saveRole(RoleAndPermission roleAndPermission) throws ParameterException, BusinessException;
    /**
     * 
     * 角色删除
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-05 14:53:02
     * @param rolePermissionKey
     * @throws ParameterException
     * @throws BusinessException
     */
    public void deleteRole(Integer rolePermissionKey, Integer roleKey) throws ParameterException, BusinessException;
    
    /**
     * 
     * 查询角色权限关联实体
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-05 14:55:24
     * @param rolePermissionKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public RoleAndPermission queryByRolePermissionKey(Integer rolePermissionKey)  throws ParameterException, BusinessException;
    /**
     * 
     * 角色对应角色对象修改
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-05 14:58:05
     * @param roleAndPermission 修改对象
     * @throws ParameterException
     * @throws BusinessException
     */
    public void updateRolePermission(RoleAndPermission roleAndPermission) throws ParameterException, BusinessException;
    
    /**
     * 
     * 权限添加
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-06 10:51:54
     * @param groupPermission 权限实体
     * @throws ParameterException
     * @throws BusinessException
     */
	public void savePermission(ProjectGroupPermission groupPermission) throws ParameterException, BusinessException;
	
	/**
	 * 
	 * Add 权限删除
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-06 10:59:21
	 * @param permissionId 权限主键
	 * @throws ParameterException 参数异常
	 * @throws BusinessException 业务异常
	 */
	public void deletePermission(Integer permissionId) throws ParameterException, BusinessException;
	
	/**
	 * 
	 * 权限修改
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-06 11:03:11
	 * @param groupPermission 权限实体
	 * @throws ParameterException 参数异常
	 * @throws BusinessException 业务异常
	 */
	public void updatePermission(ProjectGroupPermission groupPermission) throws ParameterException, BusinessException;
	
	/**
	 * 
	 * 根据主键查询权限
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-06 13:40:53
	 * @param perssionKey 权限主键
	 * @throws ParameterException 参数异常
	 * @throws BusinessException 业务异常
	 * @return
	 */
	public ProjectGroupPermission queryBykey(Integer perssionKey) throws ParameterException, BusinessException;







	//****新版权限控制************************************************************

	/**
	 * 保存企业圆员工权限
	 * @param operateKey 操作人编号
	 * @param companyKey 企业编号
	 * @param employeeKey  员工编号
	 * @param insertKeys  权限编号
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public void saveAuthoritys(Integer operateKey, Long companyKey, Integer employeeKey, Collection<Integer> insertKeys) throws ParameterException, BusinessException;


	/**
	 * 查询企业员工的权限
	 * @param companyKey 企业编号
	 * @param employeekey  员工编号
	 * @param idKey
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public Collection<AuthorityMenu> listAuthoritys(Long companyKey, Integer employeekey, Integer idKey) throws ParameterException, BusinessException;

	/**
	 * 企业员工权限校验
	 * @param uKey
	 * @param authorityKey
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public boolean validateAuthority(Integer uKey, Integer authorityKey) throws ParameterException, BusinessException;
	
	/**
	 * 查询当前用户的菜单权限
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-29 12:01:07
	 * @param uKey
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public Collection<MergeAuthorityMenu> queryAuthorityMenuByUkey(Integer uKey) throws ParameterException, BusinessException;
	/**
	 * 查询当前用户的菜单权限
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-29 12:01:07
	 * @param userKey
	 * @param menuType
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public Collection<AuthorityMenu> loadAuthoritys(Integer userKey, MenuType menuType) throws ParameterException, BusinessException;

	/**
	 * 查询权限编码
	 * @param userKey  用户编号
	 * @param menuType 权限类型
	 *
	 * @return
	 *
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	public Collection<String> loadAuthorityCodes(Integer userKey, MenuType menuType) throws ParameterException, BusinessException;
}
