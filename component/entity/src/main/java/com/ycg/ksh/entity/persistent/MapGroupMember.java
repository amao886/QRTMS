package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`map_group_member`")
public class MapGroupMember extends BaseEntity {

	private static final long serialVersionUID = -3898473154542877931L;

	/**
     * 组ID
     */
    @Id
    @Column(name = "`group_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer groupId;

    /**
     * 用户Id
     */
    @Id
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 角色(1:组长,2:组员)
     */
    @Column(name = "`role_id`")
    private Integer roleId;

    
    
    /**
	 * 创建一个新的 MapGroupMember实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:47:53
	 */
	public MapGroupMember() {
		super();
	}

	/**
	 * 创建一个新的 MapGroupMember实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:47:46
	 * @param groupId
	 * @param userId
	 * @param roleId
	 */
	public MapGroupMember(Integer groupId, Integer userId, Integer roleId) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.roleId = roleId;
	}

	/**
     * 获取组ID
     *
     * @return group_id - 组ID
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置组ID
     *
     * @param groupId 组ID
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取用户Id
     *
     * @return user_id - 用户Id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户Id
     *
     * @param userId 用户Id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取角色(1:组长,2:组员)
     *
     * @return role_id - 角色(1:组长,2:组员)
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色(1:组长,2:组员)
     *
     * @param roleId 角色(1:组长,2:组员)
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}