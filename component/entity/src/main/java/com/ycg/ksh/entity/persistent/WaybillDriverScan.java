package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 运输中扫码实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:40:30
 */
@Table(name = "`driver_waybill_status_tab`")
public class WaybillDriverScan extends BaseEntity {
 
	private static final long serialVersionUID = 126320798840322790L;

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
     * 用户主键（通常是司机用户）
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 状态值：1装货，2卸货
     */
    @Column(name = "`status`")
    private Integer status;

    @Column(name = "`createtime`")
    private Date createtime;

    public WaybillDriverScan() {
		super();
	}

	/**
	 * 创建一个新的 WaybillDriverScan实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 00:00:17
	 * @param waybillid
	 * @param userid
	 */
	public WaybillDriverScan(Integer waybillid, Integer userid) {
		super();
		this.waybillid = waybillid;
		this.userid = userid;
	}

	public WaybillDriverScan(Integer waybillid, Integer userid, Integer status, Date createtime) {
		super();
		this.waybillid = waybillid;
		this.userid = userid;
		this.status = status;
		this.createtime = createtime;
	}

	/**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
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
     * 获取用户主键（通常是司机用户）
     *
     * @return userid - 用户主键（通常是司机用户）
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户主键（通常是司机用户）
     *
     * @param userid 用户主键（通常是司机用户）
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取状态值：1装货，2卸货
     *
     * @return status - 状态值：1装货，2卸货
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态值：1装货，2卸货
     *
     * @param status 状态值：1装货，2卸货
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}