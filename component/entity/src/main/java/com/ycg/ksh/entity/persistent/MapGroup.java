package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`map_group`")
public class MapGroup extends BaseEntity {

	private static final long serialVersionUID = -1908178563783907444L;

	/**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 组名
     */
    @Column(name = "`group_name`")
    private String groupName;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    
    
    /**
	 * 创建一个新的 MapGroup实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:44:55
	 */
	public MapGroup() {
		super();
	}

	/**
	 * 创建一个新的 MapGroup实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 21:44:49
	 * @param groupName
	 * @param createTime
	 */
	public MapGroup(String groupName, Date createTime) {
		super();
		this.groupName = groupName;
		this.createTime = createTime;
	}

	/**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取组名
     *
     * @return group_name - 组名
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置组名
     *
     * @param groupName 组名
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}