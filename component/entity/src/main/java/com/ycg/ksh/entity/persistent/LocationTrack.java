package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.LocationType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`T_LOCATION_TRACK`")
public class LocationTrack extends BaseEntity {
    /**
     * 轨迹主键
     */
    @Id
    @Column(name = "`ID`")
    private Long id;

    /**
     * 关联类型
     */
    @Column(name = "`HOST_TYPE`")
    private Integer hostType;

    /**
     * 关联主键编号
     */
    @Column(name = "`HOST_ID`")
    private String hostId;

    /**
     * 用户（user）主键 轨迹上传人的id
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 经度
     */
    @Column(name = "`LONGITUDE`")
    private Double longitude;

    /**
     * 纬度
     */
    @Column(name = "`LATITUDE`")
    private Double latitude;

    /**
     * 位置地址
     */
    @Column(name = "`LOCATION`")
    private String location;

    /**
     * 保存时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    public LocationTrack() {
    }

    public LocationTrack(Serializable hostId, String longitude, String latitude, String location) {
        this.hostId = String.valueOf(hostId);
        this.longitude = new Double(longitude);
        this.latitude = new Double(latitude);
        this.location = location;
    }

    public LocationTrack(Serializable hostId, Double longitude, Double latitude, String location) {
        this.hostId = String.valueOf(hostId);
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public LocationTrack(LocationType locationType, Serializable hostId, String longitude, String latitude, String location) {
        this.hostType = locationType.getCode();
        this.hostId = String.valueOf(hostId);
        this.longitude = new Double(longitude);
        this.latitude = new Double(latitude);
        this.location = location;
    }

    public LocationTrack(LocationType locationType, Serializable hostId, Double longitude, Double latitude, String location) {
        this.hostType = locationType.getCode();
        this.hostId = String.valueOf(hostId);
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }


    /**
     * 获取轨迹主键
     *
     * @return ID - 轨迹主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置轨迹主键
     *
     * @param id 轨迹主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取关联类型
     *
     * @return HOST_TYPE - 关联类型
     */
    public Integer getHostType() {
        return hostType;
    }

    /**
     * 设置关联类型
     *
     * @param hostType 关联类型
     */
    public void setHostType(Integer hostType) {
        this.hostType = hostType;
    }

    /**
     * 获取关联主键编号
     *
     * @return HOST_ID - 关联主键编号
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * 设置关联主键编号
     *
     * @param hostId 关联主键编号
     */
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    /**
     * 获取用户（user）主键 轨迹上传人的id
     *
     * @return USER_ID - 用户（user）主键 轨迹上传人的id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户（user）主键 轨迹上传人的id
     *
     * @param userId 用户（user）主键 轨迹上传人的id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取经度
     *
     * @return LONGITUDE - 经度
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
     * @return LATITUDE - 纬度
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
     * 获取位置地址
     *
     * @return LOCATION - 位置地址
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置位置地址
     *
     * @param location 位置地址
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取保存时间
     *
     * @return CREATE_TIME - 保存时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置保存时间
     *
     * @param createTime 保存时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}