package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`grate_tab`")
public class CustomerGrate extends BaseEntity {

	private static final long serialVersionUID = 8356049320971835152L;

	/**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户表id
     */
    @Column(name = "`customerid`")
    private Integer customerid;

    /**
     * 经度
     */
    @Column(name = "`longitude`")
    private Double longitude;

    /**
     * 纬度
     */
    @Column(name = "`latitude`")
    private Double latitude;

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
     * 创建人
     */
    @Column(name = "`create_userid`")
    private Integer createUserid;

    /**
     * 修改人
     */
    @Column(name = "`update_userid`")
    private Integer updateUserid;

    /**
     * 上报时间
     */
    @Column(name = "`uploadtime`")
    private Date uploadtime;

    /**
     * 上报人
     */
    @Column(name = "`upload_userid`")
    private Integer uploadUserid;

    /**
     * 半径
     */
    @Column(name = "`radius`")
    private Double radius;

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
     * 获取客户表id
     *
     * @return customerid - 客户表id
     */
    public Integer getCustomerid() {
        return customerid;
    }

    /**
     * 设置客户表id
     *
     * @param customerid 客户表id
     */
    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    /**
     * 获取创建人
     *
     * @return create_userid - 创建人
     */
    public Integer getCreateUserid() {
        return createUserid;
    }

    /**
     * 设置创建人
     *
     * @param createUserid 创建人
     */
    public void setCreateUserid(Integer createUserid) {
        this.createUserid = createUserid;
    }

    /**
     * 获取修改人
     *
     * @return update_userid - 修改人
     */
    public Integer getUpdateUserid() {
        return updateUserid;
    }

    /**
     * 设置修改人
     *
     * @param updateUserid 修改人
     */
    public void setUpdateUserid(Integer updateUserid) {
        this.updateUserid = updateUserid;
    }

    /**
     * 获取上报时间
     *
     * @return uploadtime - 上报时间
     */
    public Date getUploadtime() {
        return uploadtime;
    }

    /**
     * 设置上报时间
     *
     * @param uploadtime 上报时间
     */
    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    /**
     * 获取上报人
     *
     * @return upload_userid - 上报人
     */
    public Integer getUploadUserid() {
        return uploadUserid;
    }

    /**
     * 设置上报人
     *
     * @param uploadUserid 上报人
     */
    public void setUploadUserid(Integer uploadUserid) {
        this.uploadUserid = uploadUserid;
    }

    /**
     * 获取半径
     *
     * @return radius - 半径
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * 设置半径
     *
     * @param radius 半径
     */
    public void setRadius(Double radius) {
        this.radius = radius;
    }
}