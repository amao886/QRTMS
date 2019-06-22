/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:02
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.ProjectGroupMember;
import com.ycg.ksh.entity.persistent.ProjectGroupRole;
import com.ycg.ksh.entity.persistent.UserRole4Group;
import com.ycg.ksh.entity.service.MergeProjectGroup;
import com.ycg.ksh.entity.service.MergeProjectGroupMember;
import com.ycg.ksh.entity.service.MergeProjectGroupRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目组业务逻辑接口
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:53:02
 */
public interface ProjectGroupService {

    final Logger logger = LoggerFactory.getLogger(ProjectGroupService.class);

    /**
     * 新增一个项目组
     * <p>
     *
     * @param projectGroup 项目组信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:25:28
     */
    void save(ProjectGroup projectGroup) throws ParameterException, BusinessException;

    /**
     * 更新项目组
     * <p>
     *
     * @param uKey         操作人用户编号
     * @param projectGroup 项目组信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:26:04
     */
    void update(Integer uKey, ProjectGroup projectGroup) throws ParameterException, BusinessException;

    /**
     * 新增项目组成员
     * <p>
     *
     * @param projectGroupMember 项目组成员信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:27:06
     */
    void saveMember(ProjectGroupMember projectGroupMember) throws ParameterException, BusinessException;

    /**
     * 删除项目组成员
     * <p>
     *
     * @param uKey 操作人用户编号
     * @param mKey 要删除的项目成员编号
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:28:56
     */
    void deleteMember(Integer uKey, Integer mKey) throws ParameterException, BusinessException;

    /**
     * 修改项目角色
     * <p>
     *
     * @param uKey           操作人用户编号
     * @param userRole4Group 角色信息
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:30:24
     */
    void modifyProjectGroupRole(Integer uKey, UserRole4Group userRole4Group) throws ParameterException, BusinessException;

    /**
     * 变更项目组组长
     * <p>
     *
     * @param uKey       操作人用户编号
     * @param gKey       项目组编号
     * @param nleaderKey 新组长用户编号
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:32:09
     */
    void modifyProjectGroupLeader(Integer uKey, Integer gKey, Integer nleaderKey) throws ParameterException, BusinessException;

    /**
     * 变更项目组组长
     * <p>
     *
     * @param uKey 操作人用户编号
     * @param mKey 组成员编号
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:21:41
     */
    void modifyProjectGroupLeader(Integer uKey, Integer mKey) throws ParameterException, BusinessException;

    /**
     * 判断是否是项目组成员
     * <p>
     *
     * @param groupKey 项目编号
     * @param userkey  用户编号
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-21 12:40:11
     */
    boolean isMember(Integer groupKey, Integer userkey) throws ParameterException, BusinessException;

    /**
     * 根据项目组编号查询项目组信息
     * <p>
     *
     * @param groupKey 项目组编号
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 14:40:12
     */
    ProjectGroup getByGroupKey(Integer groupKey) throws ParameterException, BusinessException;

    /**
     * 根据项目组编号查询项目组信息,包含组成员信息
     * <p>
     *
     * @param groupKey 项目组编号
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 14:09:33
     */
    MergeProjectGroup getDetailByGroupKey(Integer groupKey) throws ParameterException, BusinessException;

    /**
     * 根据用户ID查询该用户的所有组信息
     * <p>
     *
     * @param userKey 用户编号
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:34:41
     */
    List<ProjectGroup> listByUserKey(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 根据联系人电话查询项目信息
     * <p>
     *
     * @param contactNumber
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:22:10
     */
    List<ProjectGroup> listByContactNumber(String contactNumber) throws ParameterException, BusinessException;

    /**
     * 查询指定用户所有项目详情
     * <p>
     *
     * @param userKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 11:52:55
     */
    List<MergeProjectGroup> listMergeByUserKey(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 根据项目组编号查询项目组成员信息
     * <p>
     *
     * @param groupKey 项目组编号
     * @return
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 14:11:04
     */
    List<ProjectGroupMember> listGroupMemberByGroupKey(Integer groupKey) throws ParameterException, BusinessException;

    /**
     * 查询组成员信息
     * <p>
     *
     * @param groupKey
     * @param userKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 10:32:43
     */
    MergeProjectGroupMember getMergeGroupMember(Integer groupKey, Integer userKey) throws ParameterException, BusinessException;

    /**
     * 根据项目组编号查询该组所有成员的角色
     * <p>
     *
     * @param groupKey 项目组编号
     * @return key:成员用户编号, value:成员角色
     * @throws ParameterException 参数错误时
     * @throws BusinessException  业务逻辑处理异常时
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 14:57:10
     */
    Map<Integer, ProjectGroupRole> listGroupRoleByGroupKey(Integer groupKey) throws ParameterException, BusinessException;

    /**
     * 查询所有角色以及角色对应的权限
     * <p>
     *
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:41:45
     */
    Collection<MergeProjectGroupRole> listMergeProjectGroupRoles() throws ParameterException, BusinessException;

    /**
     * 查詢所有項目組信息
     *
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/3/16 16:13
     */
    List<ProjectGroup> listGroup() throws ParameterException, BusinessException;

}
