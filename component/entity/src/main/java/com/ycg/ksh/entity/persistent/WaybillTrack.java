package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 运单轨迹
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:15:28
 */
@Table(name = "`track_tab`")
public class WaybillTrack extends BaseEntity {

	private static final long serialVersionUID = -2277807134751042346L;

	/**
     * 轨迹主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 运单主键
     */
    @Column(name = "`waybillid`")
    private Integer waybillid;

    /**
     * 用户（user）主键 轨迹上传人的id
     */
    @Column(name = "`userid`")
    private Integer userid;

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
     * 记录坐标位置
     */
    @Column(name = "`locations`")
    private String locations;

    /**
     * 轨迹上传时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 位置描述
     */
    @Column(name = "`describe`")
    private String describe;
    
    
    public WaybillTrack() {
		super();
	}

	public WaybillTrack(Integer waybillid, Integer userid, Double longitude, Double latitude, String locations, Date createtime) {
		super();
		this.waybillid = waybillid;
		this.userid = userid;
		this.longitude = longitude;
		this.latitude = latitude;
		this.locations = locations;
		this.createtime = createtime;
	}
	
	public WaybillTrack(Integer waybillid) {
        super();
        this.waybillid = waybillid;
    }
	
	public WaybillTrack(Integer waybillid, Integer userid, Date createtime) {
		super();
		this.waybillid = waybillid;
		this.userid = userid;
		this.createtime = createtime;
	}

    public WaybillTrack(Double longitude, Double latitude, String locations) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locations = locations;
    }

    /**
     * 获取轨迹主键
     *
     * @return id - 轨迹主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置轨迹主键
     *
     * @param id 轨迹主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取运单主键
     *
     * @return waybillid - 运单主键
     */
    public Integer getWaybillid() {
        return waybillid;
    }

    /**
     * 设置运单主键
     *
     * @param waybillid 运单主键
     */
    public void setWaybillid(Integer waybillid) {
        this.waybillid = waybillid;
    }

    /**
     * 获取用户（user）主键 轨迹上传人的id
     *
     * @return userid - 用户（user）主键 轨迹上传人的id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户（user）主键 轨迹上传人的id
     *
     * @param userid 用户（user）主键 轨迹上传人的id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
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
     * 获取记录坐标位置
     *
     * @return locations - 记录坐标位置
     */
    public String getLocations() {
        return locations;
    }

    /**
     * 设置记录坐标位置
     *
     * @param locations 记录坐标位置
     */
    public void setLocations(String locations) {
        this.locations = locations;
    }

    /**
     * 获取轨迹上传时间
     *
     * @return createtime - 轨迹上传时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置轨迹上传时间
     *
     * @param createtime 轨迹上传时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}