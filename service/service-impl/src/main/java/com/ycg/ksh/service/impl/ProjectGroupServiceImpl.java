/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:35:49
 */
package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.entity.common.constant.SourceType;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.GroupCodeContext;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.observer.BarcodeObserverAdapter;
import com.ycg.ksh.service.observer.ProjectGroupObserverAdapter;
import com.ycg.ksh.service.observer.UserObserverAdapter;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目组业务逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:35:49
 */
@Service("ksh.core.service.projectGroupService")
public class ProjectGroupServiceImpl implements ProjectGroupService, WaybillObserverAdapter, BarcodeObserverAdapter, UserObserverAdapter {

    @Resource
    ProjectGroupMapper groupMapper;
    @Resource
    ProjectGroupRoleMapper groupRoleMapper;
    @Resource
    ProjectGroupPermissionMapper groupPermissionMapper;
    @Resource
    ProjectGroupMemberMapper memberMapper;
    @Resource
    UserRole4GroupMapper role4GroupMapper;
    @Resource
    BarcodeMapper barcodeMapper;
    @Resource
    UserMapper userMapper;

    @Autowired(required = false)
    Collection<ProjectGroupObserverAdapter> observers;

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:34:14
     * @see com.ycg.ksh.service.api.ProjectGroupService#save(ProjectGroup)
     * <p>
     */
    @Override
    public void save(ProjectGroup projectGroup) throws ParameterException, BusinessException {
        Assert.notNull(projectGroup, "项目组信息不能为空");
        Assert.notBlank(projectGroup.getUserid(), "创建人用户编号不能为空");
        Assert.notBlank(projectGroup.getGroupName(), "项目组名称不能为空");
        Validator validator = Validator.NORMALWORD;
        if (!validator.verify(projectGroup.getGroupName())) {
            throw new ParameterException(projectGroup.getGroupName(), validator.getMessage("项目组名称"));
        }
        projectGroup.setCount(0);
        projectGroup.setCreatetime(new Date());
        projectGroup.setUpdatetime(projectGroup.getCreatetime());
        projectGroup.setStartgoodstime("00:00");
        projectGroup.setEndgoodstime(projectGroup.getStartgoodstime());
        if (groupMapper.insertSelective(projectGroup) > 0) {
            memberMapper.insertSelective(new ProjectGroupMember(projectGroup.getId(), projectGroup.getUserid(), projectGroup.getCreatetime(), 10));
            role4GroupMapper.insertSelective(new UserRole4Group(projectGroup.getId(), projectGroup.getUserid(), 1));

            if (CollectionUtils.isNotEmpty(observers)) {
                for (ProjectGroupObserverAdapter projectGroupAbstractObserver : observers) {
                    try {
                        projectGroupAbstractObserver.notifyProjectGroupChange(projectGroup.getId(), projectGroup.getUserid(), ProjectGroupObserverAdapter.PROJECT_GROUP_ADD);
                    } catch (Exception e) {
                        logger.error("新增项目组 observer:{} {}", projectGroupAbstractObserver, projectGroup, e);
                    }
                }
            }
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:34:14
     * @see com.ycg.ksh.service.api.ProjectGroupService#update(ProjectGroup)
     * <p>
     */
    @Override
    public void update(Integer uKey, ProjectGroup projectGroup) throws ParameterException, BusinessException {
        Assert.notNull(projectGroup, "项目组信息不能为空");
        Assert.notBlank(projectGroup.getId(), "项目组编号不能为空");
        Assert.notBlank(projectGroup.getGroupName(), "项目组名称不能为空");
        Validator validator = Validator.NORMALWORD;
        if (!validator.verify(projectGroup.getGroupName())) {
            throw new ParameterException(projectGroup.getGroupName(), validator.getMessage("项目组名称"));
        }
        ProjectGroup exister = groupMapper.selectByPrimaryKey(projectGroup.getId());
        if (exister == null) {
            throw new ParameterException("项目组资源不存在");
        }
        if (exister.getUserid() - uKey != 0) {
            throw new BusinessException("你不是组长,没有权限变更组名称");
        }
        projectGroup.setUserid(null);
        projectGroup.setCreatetime(null);
        projectGroup.setUpdatetime(new Date());
        if (StringUtils.isBlank(projectGroup.getEndgoodstime())) {
            projectGroup.setEndgoodstime("00:00");
        }
        if (StringUtils.isBlank(projectGroup.getStartgoodstime())) {
            projectGroup.setStartgoodstime("00:00");
        }
        groupMapper.updateByPrimaryKeySelective(projectGroup);
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:34:14
     * @see com.ycg.ksh.service.api.ProjectGroupService#saveMember(ProjectGroupMember)
     * <p>
     */
    @Override
    public void saveMember(ProjectGroupMember projectGroupMember) throws ParameterException, BusinessException {
        Assert.notNull(projectGroupMember, "项目组成员信息不能为空");
        Assert.notBlank(projectGroupMember.getUserid(), "用户编号不能为空");
        Assert.notBlank(projectGroupMember.getGroupid(), "项目组编号不能为空");
        if (memberMapper.get(projectGroupMember.getGroupid(), projectGroupMember.getUserid()) == null) {
            projectGroupMember.setCreatetime(new Date());
            projectGroupMember.setStatus(10);
            if (memberMapper.insertSelective(projectGroupMember) > 0) {
                role4GroupMapper.insertSelective(new UserRole4Group(projectGroupMember.getGroupid(), projectGroupMember.getUserid(), 4));
                if (CollectionUtils.isNotEmpty(observers)) {
                    for (ProjectGroupObserverAdapter projectGroupAbstractObserver : observers) {
                        try {
                            projectGroupAbstractObserver.notifyProjectGroupChange(projectGroupMember.getGroupid(), projectGroupMember.getUserid(), ProjectGroupObserverAdapter.PROJECT_GROUP_MEMBER_ADD);
                        } catch (Exception e) {
                            logger.error("新增成员 observer:{} {}", projectGroupAbstractObserver, projectGroupMember, e);
                        }
                    }
                }
            }
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:34:14
     * @see com.ycg.ksh.service.api.ProjectGroupService#deleteMember(java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public void deleteMember(Integer uKey, Integer mKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notBlank(mKey, "项目组成员编号不能为空");
        ProjectGroupMember projectGroupMember = memberMapper.selectByPrimaryKey(mKey);
        if (projectGroupMember != null) {
            if (memberMapper.isMember(projectGroupMember.getGroupid(), uKey) <= 0) {
                throw new BusinessException("你不是该项目组成员");
            }
            ProjectGroupRole groupRole = groupRoleMapper.getRoleByUserKey(projectGroupMember.getGroupid(), uKey);
            if (projectGroupMember.getUserid() - uKey == 0) {
                if (groupRole != null && "ZZ".equalsIgnoreCase(groupRole.getRolecode())) {
                    throw new BusinessException("组长不能直接退出");
                }
            } else {
                if (groupRole == null || !"ZZ".equalsIgnoreCase(groupRole.getRolecode())) {
                    throw new BusinessException("你不是组长,没有权限删除组成员");
                }
            }
            if (memberMapper.deleteByPrimaryKey(projectGroupMember.getId()) > 0) {
                role4GroupMapper.delete(new UserRole4Group(projectGroupMember.getGroupid(), projectGroupMember.getUserid()));
                if (CollectionUtils.isNotEmpty(observers)) {
                    for (ProjectGroupObserverAdapter projectGroupAbstractObserver : observers) {
                        try {
                            projectGroupAbstractObserver.notifyProjectGroupChange(projectGroupMember.getGroupid(), projectGroupMember.getUserid(), ProjectGroupObserverAdapter.PROJECT_GROUP_MEMBER_DELETE);
                        } catch (Exception e) {
                            logger.error("删除成员 observer:{} uKey:{} mKey:{}", projectGroupAbstractObserver, uKey, mKey, e);
                        }
                    }
                }
            }
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:34:14
     * @see com.ycg.ksh.service.api.ProjectGroupService#modifyProjectGroupRole(java.lang.Integer, ProjectGroupRole)
     * <p>
     */
    @Override
    public void modifyProjectGroupRole(Integer uKey, UserRole4Group userRole4Group) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notNull(userRole4Group, "项目组角色信息不能为空");
        Assert.notBlank(userRole4Group.getGroupid(), "项目组编号不能为空");
        Assert.notBlank(userRole4Group.getUserid(), "成员用户编号不能为空");
        Assert.notBlank(userRole4Group.getRoleid(), "要更改的角色编号不能为空");

        if (memberMapper.isMember(userRole4Group.getGroupid(), uKey) <= 0) {
            throw new BusinessException("你不是该项目组成员");
        }
        ProjectGroupRole groupRole = groupRoleMapper.getRoleByUserKey(userRole4Group.getGroupid(), uKey);
        if (groupRole == null || !"ZZ".equalsIgnoreCase(groupRole.getRolecode())) {
            throw new BusinessException("你不是组长,没有权限变更成员角色");
        }
        UserRole4Group exister = role4GroupMapper.get(userRole4Group.getGroupid(), userRole4Group.getUserid(), userRole4Group.getRoleid());
        if (exister == null) {
            userRole4Group.setCreatetime(new Date());
            role4GroupMapper.insertSelective(userRole4Group);
        } else {
            exister.setRoleid(userRole4Group.getRoleid());
            role4GroupMapper.updateByPrimaryKeySelective(exister);
        }
        if (CollectionUtils.isNotEmpty(observers)) {
            for (ProjectGroupObserverAdapter projectGroupAbstractObserver : observers) {
                try {
                    projectGroupAbstractObserver.notifyProjectGroupChange(userRole4Group.getGroupid(), userRole4Group.getUserid(), ProjectGroupObserverAdapter.PROJECT_GROUP_ROLE_CHANGE);
                } catch (Exception e) {
                    logger.error("变更角色 observer:{} uKey:{} {}", projectGroupAbstractObserver, uKey, userRole4Group, e);
                }
            }
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 14:22:21
     * @see com.ycg.ksh.service.api.ProjectGroupService#modifyProjectGroupLeader(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public void modifyProjectGroupLeader(Integer uKey, Integer gKey, Integer nleaderKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notBlank(gKey, "项目组编号不能为空");
        Assert.notBlank(nleaderKey, "新组长用户编号不能为空");
        changeProjectGroupLeader(uKey, memberMapper.get(gKey, nleaderKey));
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 13:34:14
     * @see com.ycg.ksh.service.api.ProjectGroupService#modifyProjectGroupLeader(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public void modifyProjectGroupLeader(Integer uKey, Integer mKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        Assert.notBlank(mKey, "项目组成员编号不能为空");
        changeProjectGroupLeader(uKey, memberMapper.selectByPrimaryKey(mKey));
    }

    private void changeProjectGroupLeader(Integer uKey, ProjectGroupMember nmember) {
        if (nmember != null) {
            ProjectGroup projectGroup = groupMapper.selectByPrimaryKey(nmember.getGroupid());
            if (projectGroup.getUserid() - uKey != 0) {
                throw new BusinessException("你不是组长,没有权限变更组长");
            }
            if (projectGroup.getUserid() - nmember.getUserid() == 0) {
                throw new BusinessException("该成员已经是项目组组长");
            }
            ProjectGroup updateGroup = new ProjectGroup();
            updateGroup.setId(projectGroup.getId());
            updateGroup.setUserid(nmember.getUserid());
            updateGroup.setUpdatetime(new Date());
            if (groupMapper.updateByPrimaryKeySelective(updateGroup) > 0) {
                role4GroupMapper.update(new UserRole4Group(nmember.getGroupid(), uKey, 4));
                role4GroupMapper.update(new UserRole4Group(nmember.getGroupid(), nmember.getUserid(), 1));
                if (CollectionUtils.isNotEmpty(observers)) {
                    for (ProjectGroupObserverAdapter projectGroupAbstractObserver : observers) {
                        try {
                            projectGroupAbstractObserver.notifyProjectGroupChange(nmember.getGroupid(), uKey, ProjectGroupObserverAdapter.PROJECT_GROUP_ROLE_CHANGE);
                            projectGroupAbstractObserver.notifyProjectGroupChange(nmember.getGroupid(), nmember.getUserid(), ProjectGroupObserverAdapter.PROJECT_GROUP_ROLE_CHANGE);
                        } catch (Exception e) {
                            logger.error("变成组成 observer:{} uKey:{} {}", projectGroupAbstractObserver, uKey, nmember, e);
                        }
                    }
                }
            }
        } else {
            throw new BusinessException("该成员不属于该项目组");
        }
    }


    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 12:53:16
     * @see com.ycg.ksh.service.api.ProjectGroupService#getDetailByGroupKey(java.lang.Integer)
     * <p>
     */
    @Override
    public MergeProjectGroup getDetailByGroupKey(Integer groupKey) throws ParameterException, BusinessException {
        try {
            ProjectGroup projectGroup = groupMapper.selectByPrimaryKey(groupKey);
            if (projectGroup != null) {
                MergeProjectGroup mergeProjectGroup = toMergeProjectGroup(projectGroup);
                mergeProjectGroup.setList(listGroupMemberDetailByGroupKey(groupKey));
                return mergeProjectGroup;
            }
        } catch (Exception e) {
            logger.error("根据项目组编号查询项目信息异常 groupKey:{}", groupKey, e);
            throw BusinessException.dbException("查询项目信息异常");
        }
        return null;
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:35:49
     * @see com.ycg.ksh.service.api.ProjectGroupService#listByUserKey(java.lang.Integer)
     * <p>
     */
    @Override
    public List<ProjectGroup> listByUserKey(Integer userKey) throws ParameterException, BusinessException {
        try {
            return groupMapper.listByUserKey(userKey);
        } catch (Exception e) {
            logger.error("根据用户编号查询项目信息异常 userKey:{}", userKey, e);
            throw BusinessException.dbException("查询项目信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:24:35
     * @see com.ycg.ksh.service.api.ProjectGroupService#listByContactNumber(java.lang.String)
     * <p>
     */
    @Override
    public List<ProjectGroup> listByContactNumber(String contactNumber) throws ParameterException, BusinessException {
        try {
            return groupMapper.listByContactNumber(contactNumber);
        } catch (Exception e) {
            logger.error("根据根据联系人电话查询项目信息异常 contactNumber:{}", contactNumber, e);
            throw BusinessException.dbException("查询项目信息异常");
        }
    }

    @Override
    public List<MergeProjectGroup> listMergeByUserKey(Integer userKey) throws ParameterException, BusinessException {
        try {
            List<ProjectGroup> collection = groupMapper.listByUserKey(userKey);
            if (CollectionUtils.isNotEmpty(collection)) {
                List<MergeProjectGroup> merges = new ArrayList<MergeProjectGroup>(collection.size());
                for (ProjectGroup projectGroup : collection) {
                    merges.add(toMergeProjectGroup(projectGroup));
                }
                return merges;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("根据用户编号查询项目信息异常 userKey:{}", userKey, e);
            throw BusinessException.dbException("查询项目信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:09:18
     * @see com.ycg.ksh.service.api.ProjectGroupService#getByGroupKey(java.lang.Integer)
     * <p>
     */
    @Override
    public ProjectGroup getByGroupKey(Integer groupKey) throws ParameterException, BusinessException {
        try {
            return groupMapper.selectByPrimaryKey(groupKey);
        } catch (Exception e) {
            logger.error("根据项目组编号查询项目信息异常 groupKey:{}", groupKey, e);
            throw BusinessException.dbException("查询项目信息异常");
        }
    }

    private MergeProjectGroup toMergeProjectGroup(ProjectGroup projectGroup) throws Exception {
        MergeProjectGroup mergeProjectGroup = new MergeProjectGroup(projectGroup);
        Map<String, Number> counts = barcodeMapper.countByGroup(projectGroup.getId());
        Number number = counts.get("number");
        if (number != null) {
            mergeProjectGroup.setNumber(number.intValue());
        }
        Number useCount = counts.get("useCount");
        if (useCount != null) {
            mergeProjectGroup.setUseCount(useCount.intValue());
        }
        Number totalNum = counts.get("totalNum");
        if (totalNum != null) {
            mergeProjectGroup.setTotalNum(totalNum.intValue());
        }
        return mergeProjectGroup;
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:09:22
     * @see com.ycg.ksh.service.api.ProjectGroupService#listGroupMemberByGroupKey(java.lang.Integer)
     * <p>
     */
    @Override
    public List<ProjectGroupMember> listGroupMemberByGroupKey(Integer groupKey) throws ParameterException, BusinessException {
        try {
            Example example = new Example(ProjectGroupMember.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupid", groupKey);
            criteria.andEqualTo("status", Constant.PGROUP_MSTATUS_EFFECTIVE);
            return memberMapper.selectByExample(example);
        } catch (Exception e) {
            logger.error("根据项目组编号查询项目成员信息异常 groupKey:{}", groupKey, e);
            throw BusinessException.dbException("查询项目成员信息异常");
        }
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 12:28:09
     * @see com.ycg.ksh.service.api.ProjectGroupService#getMergeGroupMember(java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public MergeProjectGroupMember getMergeGroupMember(Integer groupKey, Integer userKey) throws ParameterException, BusinessException {
        ProjectGroupMember groupMember = memberMapper.get(groupKey, userKey);
        if (groupMember != null) {
            try {
                MergeProjectGroupMember mergeMember = new MergeProjectGroupMember(groupMember);
                mergeMember.setUser(new AssociateUser(userMapper.selectByPrimaryKey(mergeMember.getUserid())));
                mergeMember.setRolePermission(groupRoleMapper.getRoleByUserKey(groupKey, userKey));
                return mergeMember;
            } catch (Exception e) {
            }
        }
        return null;
    }

    private List<MergeProjectGroupMember> listGroupMemberDetailByGroupKey(Integer groupKey) throws ParameterException, BusinessException {
        try {
            Example example = new Example(ProjectGroupMember.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupid", groupKey);
            criteria.andEqualTo("status", Constant.PGROUP_MSTATUS_EFFECTIVE);
            List<ProjectGroupMember> members = memberMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(members)) {
                List<MergeProjectGroupMember> mergers = new ArrayList<MergeProjectGroupMember>(members.size());
                LocalCacheManager<AssociateUser> userCache = LocalCacheFactory.createUserCache(userMapper);
                Map<Integer, ProjectGroupRole> roles = listGroupRoleByGroupKey(groupKey);
                for (ProjectGroupMember projectGroupMember : members) {
                    MergeProjectGroupMember mergeProjectGroupMember = new MergeProjectGroupMember(projectGroupMember);
                    mergeProjectGroupMember.setUser(userCache.get(projectGroupMember.getUserid()));
                    mergeProjectGroupMember.setRolePermission(roles.get(projectGroupMember.getUserid()));
                    mergers.add(mergeProjectGroupMember);
                }
                return mergers;
            }
        } catch (Exception e) {
            logger.error("根据项目组编号查询项目成员信息异常 groupKey:{}", groupKey, e);
            throw BusinessException.dbException("查询项目成员信息异常");
        }
        return null;
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 15:09:24
     * @see com.ycg.ksh.service.api.ProjectGroupService#listGroupRoleByGroupKey(java.lang.Integer)
     * <p>
     */
    @Override
    public Map<Integer, ProjectGroupRole> listGroupRoleByGroupKey(Integer groupKey) throws ParameterException, BusinessException {
        try {
            Example example = new Example(UserRole4Group.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("groupid", groupKey);
            List<UserRole4Group> userRoles = role4GroupMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(userRoles)) {
                LocalCacheManager<ProjectGroupRole> cache = LocalCacheFactory.createCache(groupRoleMapper);
                Map<Integer, ProjectGroupRole> map = new HashMap<Integer, ProjectGroupRole>(userRoles.size());
                for (UserRole4Group userRole : userRoles) {
                    map.put(userRole.getUserid(), cache.get(userRole.getRoleid()));
                }
                return map;
            }
        } catch (Exception e) {
            logger.error("根据项目组编号查询项目成员信息异常 groupKey:{}", groupKey, e);
            throw BusinessException.dbException("查询项目成员信息异常");
        }
        return Collections.emptyMap();
    }

    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-21 13:03:58
     * @see com.ycg.ksh.service.api.ProjectGroupService#isMember(java.lang.Integer, java.lang.Integer)
     * <p>
     */
    @Override
    public boolean isMember(Integer groupKey, Integer userkey) throws ParameterException, BusinessException {
        if (groupKey == null || userkey == null || groupKey * userkey == 0) {
            return false;
        }
        return memberMapper.isMember(groupKey, userkey) > 0;
    }


    /**
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 10:43:45
     * @see com.ycg.ksh.service.api.PermissionService#listMergeProjectGroupRoles()
     * <p>
     */
    @Override
    public Collection<MergeProjectGroupRole> listMergeProjectGroupRoles() throws ParameterException, BusinessException {
        List<ProjectGroupRole> projectGroupRoles = groupRoleMapper.selectAll();
        if (CollectionUtils.isNotEmpty(projectGroupRoles)) {
            Collection<MergeProjectGroupRole> mCollection = new ArrayList<>(projectGroupRoles.size());
            for (ProjectGroupRole projectGroupRole : projectGroupRoles) {
                try {
                    MergeProjectGroupRole mergeProjectGroupRole = new MergeProjectGroupRole(projectGroupRole);
                    mergeProjectGroupRole.setPermissionList(groupPermissionMapper.selectByRoleKey(projectGroupRole.getId()));
                    mCollection.add(mergeProjectGroupRole);
                } catch (Exception e) {
                    logger.warn("MergeProjectGroupRole Exception {}", e.getMessage(), e);
                }
            }
            return mCollection;
        }
        return null;
    }

    @Override
    public List<ProjectGroup> listGroup() throws ParameterException, BusinessException {
        return groupMapper.selectAll();
    }

    @Override
    public void onMergeWaybill(MergeWaybill waybill, WaybillAssociate associate) throws BusinessException {
        if(waybill.getGroupid() != null){
            if (associate.isAssociateProjectGroup()) {
                LocalCacheManager<ProjectGroup> projectGroupCache = LocalCacheFactory.createCache(groupMapper);
                waybill.setGroup(projectGroupCache.get(waybill.getGroupid()));
            }
        }
    }

    @Override
    public void notifyBarcodeValidate(Barcode barcode, BarcodeContext context) {
        if(context instanceof GroupCodeContext) {
            GroupCodeContext codeContext = (GroupCodeContext) context;
            if (barcode.getGroupid() != null && barcode.getGroupid() > 0) {//条码在项目
                if (isMember(barcode.getGroupid(), context.getUserKey())) {
                    codeContext.setSourceType(SourceType.GROUP);
                    codeContext.setAllowBind(true);
                }
            }
            if (!context.isAllowBind()) {
                context.setAllowBind(barcode.getUserid() - context.getUserKey() == 0);//如果是当前用户创建的二维码
            }
        }
    }

    /**
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 15:18:54
     */
    @Override
    public void initializeUser(AuthorizeUser authorizeUser, Integer type) throws BusinessException {
        //logger.info("收到用户登录通知 -> {} type:{}", authorizeUser, type);
        if(CoreConstants.USER_LOGIN - type == 0) {
            List<ProjectGroup> groups = groupMapper.listByUserKey(authorizeUser.getId());
            if(CollectionUtils.isNotEmpty(groups)) {
                authorizeUser.setGroupKeys(groups.stream().map(g -> g.getId()).collect(Collectors.toList()));
            }
        }
    }

}
