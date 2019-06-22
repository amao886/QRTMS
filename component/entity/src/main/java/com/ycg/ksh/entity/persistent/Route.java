package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`route_tab`")
public class Route extends BaseEntity{
    /**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-24 10:39:34
	 */
	private static final long serialVersionUID = 1722577172501333340L;

	/**
     * 主键
     */
	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户主键
     */
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 路由名称
     */
    @Column(name = "`route_name`")
    private String routeName;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`updatetime`")
    private Date updatetime;

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
     * 获取用户主键
     *
     * @return user_id - 用户主键
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户主键
     *
     * @param userId 用户主键
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取路由名称
     *
     * @return route_name - 路由名称
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * 设置路由名称
     *
     * @param routeName 路由名称
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    /**
     * 获取创建时间
     *
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取修改时间
     *
     * @return updatetime - 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置修改时间
     *
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}