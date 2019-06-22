package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`waybill_schedule`")
public class WaybillSchedule extends BaseEntity {

	private static final long serialVersionUID = -6805342887370599149L;
	/**
	 * 主键id
	 */
	@Id
	@Column(name = "`id`")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * group主键
	 */
	@Column(name = "`groupid`")
	private Integer groupid;

	/**
	 * user主键
	 */
	@Column(name = "`userid`")
	private Integer userid;

	/**
	 * 导入时间
	 */
	@Column(name = "`createtime`")
	private Date createtime;

	/**
	 * 备注
	 */
	@Column(name = "`remark`")
	private String remark;

	/**
	 * 重量
	 */
	@Column(name = "`weight`")
	private Double weight;

	/**
	 * 体积
	 */
	@Column(name = "`volume`")
	private Double volume;

	/**
	 * 数量
	 */
	@Column(name = "`number`")
	private Integer number;

	/**
	 * 收货客户名称/公司名称
	 */
	@Column(name = "`receiver_name`")
	private String receiverName;

	/**
	 * 收获客户座机
	 */
	@Column(name = "`receiver_tel`")
	private String receiverTel;

	/**
	 * 收货地址
	 */
	@Column(name = "`receive_address`")
	private String receiveAddress;

	/**
	 * 收货联系人姓名
	 */
	@Column(name = "`contact_name`")
	private String contactName;

	/**
	 * 收货联系人电话
	 */
	@Column(name = "`contact_phone`")
	private String contactPhone;

	/**
	 * 经度
	 */
	@Column(name = "`longitude`")
	private String longitude;

	/**
	 * 纬度
	 */
	@Column(name = "`latitude`")
	private String latitude;

	/**
	 * 电子围栏开关1开0关
	 */
	@Column(name = "`fence_status`")
	private Integer fenceStatus;

	/**
	 * 计划状态(0:正常,1:已绑定,99:作废)
	 */
	@Column(name = "`schedule_status`")
	private Integer scheduleStatus;

	/**
	 * 要求到货时间天数
	 */
	@Column(name = "`arrive_day`")
	private Integer arrive_day;

	/**
	 * 要求到货时间小时数
	 */
	@Column(name = "`arrive_hour`")
	private Integer arrive_hour;
	/**
	 * 半径
	 */
	@Column(name = "`radius`")
	private Double radius;

	public Integer getArrive_day() {
		return arrive_day;
	}

	public void setArrive_day(Integer arrive_day) {
		this.arrive_day = arrive_day;
	}

	public Integer getArrive_hour() {
		return arrive_hour;
	}

	public void setArrive_hour(Integer arrive_hour) {
		this.arrive_hour = arrive_hour;
	}

	/**
	 * 获取主键id
	 *
	 * @return id - 主键id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置主键id
	 *
	 * @param id
	 *            主键id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取group主键
	 *
	 * @return groupid - group主键
	 */
	public Integer getGroupid() {
		return groupid;
	}

	/**
	 * 设置group主键
	 *
	 * @param groupid
	 *            group主键
	 */
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	/**
	 * 获取user主键
	 *
	 * @return userid - user主键
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * 设置user主键
	 *
	 * @param userid
	 *            user主键
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * 获取导入时间
	 *
	 * @return createtime - 导入时间
	 */
	public Date getCreatetime() {
		return createtime;
	}

	/**
	 * 设置导入时间
	 *
	 * @param createtime
	 *            导入时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	/**
	 * 获取备注
	 *
	 * @return remark - 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 *
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取重量
	 *
	 * @return weight - 重量
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * 设置重量
	 *
	 * @param weight
	 *            重量
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/**
	 * 获取体积
	 *
	 * @return volume - 体积
	 */
	public Double getVolume() {
		return volume;
	}

	/**
	 * 设置体积
	 *
	 * @param volume
	 *            体积
	 */
	public void setVolume(Double volume) {
		this.volume = volume;
	}

	/**
	 * 获取数量
	 *
	 * @return number - 数量
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * 设置数量
	 *
	 * @param number
	 *            数量
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * 获取收货客户名称/公司名称
	 *
	 * @return receiver_name - 收货客户名称/公司名称
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * 设置收货客户名称/公司名称
	 *
	 * @param receiverName
	 *            收货客户名称/公司名称
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * 获取收获客户座机
	 *
	 * @return receiver_tel - 收获客户座机
	 */
	public String getReceiverTel() {
		return receiverTel;
	}

	/**
	 * 设置收获客户座机
	 *
	 * @param receiverTel
	 *            收获客户座机
	 */
	public void setReceiverTel(String receiverTel) {
		this.receiverTel = receiverTel;
	}

	/**
	 * 获取收货地址
	 *
	 * @return receive_address - 收货地址
	 */
	public String getReceiveAddress() {
		return receiveAddress;
	}

	/**
	 * 设置收货地址
	 *
	 * @param receiveAddress
	 *            收货地址
	 */
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	/**
	 * 获取收货联系人姓名
	 *
	 * @return contact_name - 收货联系人姓名
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * 设置收货联系人姓名
	 *
	 * @param contactName
	 *            收货联系人姓名
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * 获取收货联系人电话
	 *
	 * @return contact_phone - 收货联系人电话
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * 设置收货联系人电话
	 *
	 * @param contactPhone
	 *            收货联系人电话
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	/**
	 * 获取经度
	 *
	 * @return longitude - 经度
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * 设置经度
	 *
	 * @param longitude
	 *            经度
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * 获取纬度
	 *
	 * @return latitude - 纬度
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * 设置纬度
	 *
	 * @param latitude
	 *            纬度
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * 获取电子围栏开关1开0关
	 *
	 * @return fence_status - 电子围栏开关1开0关
	 */
	public Integer getFenceStatus() {
		return fenceStatus;
	}

	/**
	 * 设置电子围栏开关1开0关
	 *
	 * @param fenceStatus
	 *            电子围栏开关1开0关
	 */
	public void setFenceStatus(Integer fenceStatus) {
		this.fenceStatus = fenceStatus;
	}

	/**
	 * 获取计划状态(0:正常,1:已绑定,99:作废)
	 *
	 * @return schedule_status - 计划状态(0:正常,1:已绑定,99:作废)
	 */
	public Integer getScheduleStatus() {
		return scheduleStatus;
	}

	/**
	 * 设置计划状态(0:正常,1:已绑定,99:作废)
	 *
	 * @param scheduleStatus
	 *            计划状态(0:正常,1:已绑定,99:作废)
	 */
	public void setScheduleStatus(Integer scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

}