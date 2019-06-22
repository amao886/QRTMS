package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`route_line_tab`")
public class RouteLine extends BaseEntity{
    /**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-24 10:39:54
	 */
	private static final long serialVersionUID = 4982531488507879356L;

	/**
     * 主键
     */
	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 路由主键
     */
    @Column(name = "`route_id`")
    private Integer routeId;

    /**
     * 用户好友主键
     */
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 父节点
     */
    @Column(name = "`pid`")
    private Integer pid;

    /**
     * 线路类型(1:起点，2：中转，3：终点)
     */
    @Column(name = "`line_type`")
    private Integer lineType;

    /**
     * 省份
     */
    @Column(name = "`province`")
    private String province;

    /**
     * 城市
     */
    @Column(name = "`city`")
    private String city;

    /**
     * 区/县
     */
    @Column(name = "`district`")
    private String district;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

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
     * 获取路由主键
     *
     * @return route_id - 路由主键
     */
    public Integer getRouteId() {
        return routeId;
    }

    /**
     * 设置路由主键
     *
     * @param routeId 路由主键
     */
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    /**
     * 获取好友主键
     *
     * @return friends_id - 好友主键
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置好友主键
     *
     * @param friendsId 好友主键
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取父节点
     *
     * @return pid - 父节点
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 设置父节点
     *
     * @param pid 父节点
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 获取线路类型(1:起点，2：中转，3：终点)
     *
     * @return line_type - 线路类型(1:起点，2：中转，3：终点)
     */
    public Integer getLineType() {
        return lineType;
    }

    /**
     * 设置线路类型(1:起点，2：中转，3：终点)
     *
     * @param lineType 线路类型(1:起点，2：中转，3：终点)
     */
    public void setLineType(Integer lineType) {
        this.lineType = lineType;
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取区/县
     *
     * @return district - 区/县
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 设置区/县
     *
     * @param district 区/县
     */
    public void setDistrict(String district) {
        this.district = district;
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
}