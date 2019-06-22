package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * 项目组角色
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:13:28
 */
@Table(name = "`role_tab`")
public class ProjectGroupRole extends BaseEntity {

	private static final long serialVersionUID = -5555291916192884119L;
	public ProjectGroupRole() {
		super();
	}
	
	public ProjectGroupRole(String roleName, String roleCode) {
		super();
		this.rolename = roleName;
		this.rolecode = roleCode;
	}
	/**
     * Id自增
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "`rolename`")
    private String rolename;

    /**
     * 角色代号
     */
    @Column(name = "`rolecode`")
    private String rolecode;

    /**
     * 获取Id自增
     *
     * @return id - Id自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置Id自增
     *
     * @param id Id自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取角色名称
     *
     * @return rolename - 角色名称
     */
    public String getRolename() {
        return rolename;
    }

    /**
     * 设置角色名称
     *
     * @param rolename 角色名称
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    /**
     * 获取角色代号
     *
     * @return rolecode - 角色代号
     */
    public String getRolecode() {
        return rolecode;
    }

    /**
     * 设置角色代号
     *
     * @param rolecode 角色代号
     */
    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }
}