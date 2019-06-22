/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 10:37:05
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.common.constant.PaperyReceiptFettle;
import com.ycg.ksh.entity.common.constant.ReceiptVerifyFettle;
import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.entity.persistent.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * 任务单上下文
 * <p>
 */
public class WaybillContext extends BaseEntity {

	private static final long serialVersionUID = 1628986786208984417L;

	private Waybill persistence;
	private Waybill update;

	private boolean execute = true;//是否要执行持久化操作

	private WaybillAssociate associate;//查询关联信息

	//客户信息
	private Customer[] customers;
    /**发货信息(10:根据ID查询,21:客户端传不保存,22:客户端传要保存)*/
    private int sendType;
    /**收货信息(10:根据ID查询,21:客户端传不保存,22:客户端传要保存)*/
    private int receiveType;

    /**要求到货时间配置*/
    private Integer arriveDay;
    private Integer arriveHour;

    /**绑定时的轨迹*/
    private WaybillTrack waybillTrack;

    /**操作用户信息*/
    private Integer userKey;
    private String mobile;

    //查询信息
    private WaybillSerach search;
	private PageScope pageScope;

	/**货物信息*/
    private Collection<Goods> commodities;//商品明细
    /**要删除的货物信息*/
    private Collection<Integer> deleteCommodityKeys;//要删除商品的主键

	private WaybillContext() {
		super();
	}

    public void buildContext(Waybill persistent, Waybill update){
    	this.setPersistence(persistent);
    	this.setUpdate(update);
    }

    public static WaybillContext buildContext(Integer userKey, Waybill waybill) {
		WaybillContext context = buildContext(waybill);
		context.setUserKey(userKey);
		return context;
	}

	public static WaybillContext buildContext(Integer userKey, Waybill persistence, Waybill update) {
		WaybillContext context = new WaybillContext();
		context.setPersistence(persistence);
		context.setUpdate(update);
		context.setUserKey(userKey);
		return context;
	}

    public static WaybillContext buildContext(User user, Waybill waybill) {
		WaybillContext context = buildContext(waybill);
		context.setUserKey(user.getId());
		context.setMobile(user.getMobilephone());
        return context;
	}

	public static WaybillContext buildContext(User user, WaybillSerach waybillSerach, PageScope pageScope) {
		WaybillContext context = buildContext(user);
		context.setSearch(waybillSerach);
		context.setPageScope(pageScope);
		return context;
	}
	public static WaybillContext buildContext(User user, WaybillSerach waybillSerach) {
		WaybillContext context = buildContext(user);
		context.setSearch(waybillSerach);
		return context;
	}

    public static WaybillContext buildContext(Waybill waybill) {
		WaybillContext context = new WaybillContext();
		if(waybill.getId() == null || waybill.getId() <= 0){
			context.setUpdate(waybill);
		}else{
			context.setUserKey(waybill.getUserid());
			context.setPersistence(waybill);
			context.setUpdate(new Waybill(waybill.getId()));
		}
        return context;
	}

	public static WaybillContext buildContext(User user) {
		WaybillContext context = new WaybillContext();
		context.setUserKey(user.getId());
		context.setMobile(user.getMobilephone());
        return context;
	}

	public static WaybillContext buildContext(User user, WaybillAssociate associate) {
		WaybillContext context = buildContext(user);
		context.setAssociate(associate);
		return context;
	}

	public Waybill getPersistence() {
		return persistence;
	}

	public void setPersistence(Waybill persistence) {
		this.persistence = persistence;
	}

	public Waybill getUpdate() {
		if(update != null){
			if (StringUtils.isNotBlank(update.getStartStation())) {
				String[] arrays = RegionUtils.split(update.getStartStation());
				update.setStartStation(RegionUtils.merge(arrays));
				update.setSimpleStartStation(RegionUtils.simple(arrays));
			}
			if (StringUtils.isNotBlank(update.getEndStation())) {
				String[] arrays = RegionUtils.split(update.getEndStation());
				update.setEndStation(RegionUtils.merge(arrays));
				update.setSimpleEndStation(RegionUtils.simple(arrays));
			}
		}
		return update;
	}

	public void setUpdate(Waybill update) {
		this.update = update;
	}

	public boolean fettleChange(){
		if(update != null && update.getWaybillStatus() != null){
			if(persistence == null){
				return true;
			}
			if(update.getWaybillStatus() - persistence.getWaybillStatus() != 0){
				return true;
			}
		}
		return false;
	}


	public boolean isExecute() {
		return execute;
	}

	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	public WaybillAssociate getAssociate() {
		if(associate == null){
			return WaybillAssociate.EMPTY;
		}
		return associate;
	}

	public void setAssociate(WaybillAssociate associate) {
		this.associate = associate;
	}

	public Customer[] getCustomers() {
		return customers;
	}

	public void setCustomers(Customer[] customers) {
		this.customers = customers;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(int receiveType) {
		this.receiveType = receiveType;
	}

	public Integer getArriveDay() {
		return arriveDay;
	}

	public void setArriveDay(Integer arriveDay) {
		this.arriveDay = arriveDay;
	}

	public Integer getArriveHour() {
		return arriveHour;
	}

	public void setArriveHour(Integer arriveHour) {
		this.arriveHour = arriveHour;
	}

	public WaybillTrack getWaybillTrack() {
		return waybillTrack;
	}

	public void setWaybillTrack(WaybillTrack waybillTrack) {
		this.waybillTrack = waybillTrack;
	}

	public Integer getUserKey() {
		return userKey;
	}

	public void setUserKey(Integer userKey) {
		this.userKey = userKey;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public WaybillSerach getSearch() {
		return search;
	}

	public void setSearch(WaybillSerach search) {
		this.search = search;
	}

	public PageScope getPageScope() {
		return pageScope;
	}

	public void setPageScope(PageScope pageScope) {
		this.pageScope = pageScope;
	}

	public Collection<Goods> getCommodities() {
		return commodities;
	}

	public void setCommodities(Collection<Goods> commodities) {
		this.commodities = commodities;
	}

	public void addCommodity(Goods goods){
		if(commodities == null){
			commodities = new ArrayList<Goods>();
		}
		commodities.add(goods);
	}

	public Collection<Integer> getDeleteCommodityKeys() {
		return deleteCommodityKeys;
	}

	public void setDeleteCommodityKeys(Collection<Integer> deleteCommodityKeys) {
		this.deleteCommodityKeys = deleteCommodityKeys;
	}

	public String getShipperName() {
		String shipperName = null;
		if(update != null){
			shipperName = update.getShipperName();
		}
		if(StringUtils.isBlank(shipperName) && persistence != null){
			shipperName = persistence.getShipperName();
		}
		return shipperName;
	}

	public void setShipperName(String shipperName) {
		update.setShipperName(shipperName);
	}

	public Integer getDelay() {
		Integer delay = null;
		if(update != null){
			delay = update.getDelay();
		}
		if(delay == null && persistence != null){
			delay = persistence.getDelay();
		}
		return delay;
	}

	public void setDelay(Integer delay) {
		update.setDelay(delay);
	}

	public Integer getId() {
		Integer id = null;
		if(update != null){
			id = update.getId();
		}
		if((id == null || id <= 0) && persistence != null){
			id = persistence.getId();
		}
		return id;
	}

	public void setId(Integer id) {
		update.setId(id);
	}

	/**
	 * 获取条码主键
	 *
	 * @return barcodeid - 条码主键
	 */
	public Integer getBarcodeid() {
		Integer barcodeid = null;
		if(update != null){
			barcodeid = update.getBarcodeid();
		}
		if((barcodeid == null || barcodeid <= 0) && persistence != null){
			barcodeid = persistence.getBarcodeid();
		}
		return barcodeid;
	}

	/**
	 * 设置条码主键
	 *
	 * @param barcodeid 条码主键
	 */
	public void setBarcodeid(Integer barcodeid) {
		update.setBarcodeid(barcodeid);
	}

	/**
	 * getter method for barcode
	 *
	 * @return the barcode
	 */
	public String getBarcode() {
		String barcode = null;
		if(update != null){
			barcode = update.getBarcode();
		}
		if(StringUtils.isBlank(barcode) && persistence != null){
			barcode = persistence.getBarcode();
		}
		return barcode;
	}

	/**
	 * setter method for barcode
	 *
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		update.setBarcode(barcode);
	}

	/**
	 * 获取group主键
	 *
	 * @return groupid - group主键
	 */
	public Integer getGroupid() {
		Integer groupid = null;
		if(update != null){
			groupid = update.getGroupid();
		}
		if((groupid == null || groupid <= 0) && persistence != null){
			groupid = persistence.getGroupid();
		}
		return groupid;
	}

	/**
	 * 设置group主键
	 *
	 * @param groupid group主键
	 */
	public void setGroupid(Integer groupid) {
		update.setGroupid(groupid);
	}

	/**
	 * 获取订单摘要
	 *
	 * @return order_summary - 订单摘要
	 */
	public String getOrderSummary() {
		String orderSummary = null;
		if(update != null){
			orderSummary = update.getOrderSummary();
		}
		if(StringUtils.isBlank(orderSummary) && persistence != null){
			orderSummary = persistence.getOrderSummary();
		}
		return orderSummary;
	}

	/**
	 * 设置订单摘要
	 *
	 * @param orderSummary 订单摘要
	 */
	public void setOrderSummary(String orderSummary) {
		update.setOrderSummary(orderSummary);
	}

	/**
	 * 获取收货客户主键
	 *
	 * @return customerid - 收货客户主键
	 */
	public Integer getCustomerid() {
		Integer customerid = null;
		if(update != null){
			customerid = update.getCustomerid();
		}
		if((customerid == null || customerid <= 0) && persistence != null){
			customerid = persistence.getCustomerid();
		}
		return customerid;
	}

	/**
	 * 设置收货客户主键
	 *
	 * @param customerid 收货客户主键
	 */
	public void setCustomerid(Integer customerid) {
		update.setCustomerid(customerid);
	}

	/**
	 * 获取user主键
	 *
	 * @return userid - user主键
	 */
	public Integer getUserid() {
		Integer userid = null;
		if(update != null){
			userid = update.getUserid();
		}
		if((userid == null || userid <= 0) && persistence != null){
			userid = persistence.getUserid();
		}
		return userid;
	}

	/**
	 * 设置user主键
	 *
	 * @param userid user主键
	 */
	public void setUserid(Integer userid) {
		update.setUserid(userid);
	}

	/**
	 * 获取运单最新位置
	 *
	 * @return address - 运单最新位置
	 */
	public String getAddress() {
		String address = null;
		if(update != null){
			address = update.getAddress();
		}
		if(StringUtils.isBlank(address) && persistence != null){
			address = persistence.getAddress();
		}
		return address;
	}

	/**
	 * 设置运单最新位置
	 *
	 * @param address 运单最新位置
	 */
	public void setAddress(String address) {
		update.setAddress(address);
	}

	/**
	 * 获取运单最新位置上报时间
	 * @return the loactionTime
	 */
	public Date getLoactionTime() {
		Date loactionTime = null;
		if(update != null){
			loactionTime = update.getLoactionTime();
		}
		if(loactionTime == null && persistence != null){
			loactionTime = persistence.getLoactionTime();
		}
		return loactionTime;
	}

	/**
	 * 设置运单最新位置上报时间
	 * @param loactionTime the loactionTime to set
	 */
	public void setLoactionTime(Date loactionTime) {
		update.setLoactionTime(loactionTime);
	}

	/**
	 * 获取要求到货时间
	 *
	 * @return arrivaltime - 要求到货时间
	 */
	public Date getArrivaltime() {
		Date arrivaltime = null;
		if(update != null){
			arrivaltime = update.getArrivaltime();
		}
		if(arrivaltime == null && persistence != null){
			arrivaltime = persistence.getArrivaltime();
		}
		return arrivaltime;
	}

	/**
	 * 设置要求到货时间
	 *
	 * @param arrivaltime 要求到货时间
	 */
	public void setArrivaltime(Date arrivaltime) {
		update.setArrivaltime(arrivaltime);
	}

	/**
	 * 获取绑定时间
	 *
	 * @return createtime - 绑定时间
	 */
	public Date getCreatetime() {
		Date createtime = null;
		if(update != null){
			createtime = update.getCreatetime();
		}
		if(createtime == null && persistence != null){
			createtime = persistence.getCreatetime();
		}
		return createtime;
	}

	/**
	 * 设置绑定时间
	 *
	 * @param createtime 绑定时间
	 */
	public void setCreatetime(Date createtime) {
		update.setCreatetime(createtime);
	}

	/**
	 * 获取修改时间
	 *
	 * @return updatetime - 修改时间
	 */
	public Date getUpdatetime() {
		Date updatetime = null;
		if(update != null){
			updatetime = update.getUpdatetime();
		}
		if(updatetime == null && persistence != null){
			updatetime = persistence.getUpdatetime();
		}
		return updatetime;
	}

	/**
	 * 设置修改时间
	 *
	 * @param updatetime 修改时间
	 */
	public void setUpdatetime(Date updatetime) {
		update.setUpdatetime(updatetime);
	}

	/**
	 * 获取实际达到时间
	 *
	 * @return actual_arrival_time - 实际达到时间
	 */
	public Date getActualArrivalTime() {
		Date actualArrivalTime = null;
		if(update != null){
			actualArrivalTime = update.getActualArrivalTime();
		}
		if(actualArrivalTime == null && persistence != null){
			actualArrivalTime = persistence.getActualArrivalTime();
		}
		return actualArrivalTime;
	}

	/**
	 * 设置实际达到时间
	 *
	 * @param actualArrivalTime 实际达到时间
	 */
	public void setActualArrivalTime(Date actualArrivalTime) {
		update.setActualArrivalTime(actualArrivalTime);
	}

	/**
	 * 获取备注
	 *
	 * @return remark - 备注
	 */
	public String getRemark() {
		String remark = null;
		if(update != null){
			remark = update.getRemark();
		}
		if(StringUtils.isBlank(remark) && persistence != null){
			remark = persistence.getRemark();
		}
		return remark;
	}

	/**
	 * 设置备注
	 *
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		update.setRemark(remark);
	}

	/**
	 * 获取重量
	 *
	 * @return weight - 重量
	 */
	public Double getWeight() {
		Double weight  = null;
		if(update != null){
			weight = update.getWeight();
		}
		if(weight == null && persistence != null){
			weight = persistence.getWeight();
		}
		return weight;
	}

	/**
	 * 设置重量
	 *
	 * @param weight 重量
	 */
	public void setWeight(Double weight) {
		update.setWeight(weight);
	}

	/**
	 * 获取体积
	 *
	 * @return volume - 体积
	 */
	public Double getVolume() {
		Double volume  = null;
		if(update != null){
			volume = update.getVolume();
		}
		if(volume == null && persistence != null){
			volume = persistence.getVolume();
		}
		return volume;
	}

	/**
	 * 设置体积
	 *
	 * @param volume 体积
	 */
	public void setVolume(Double volume) {
		update.setVolume(volume);
	}

	/**
	 * 获取数量
	 *
	 * @return number - 数量
	 */
	public Integer getNumber() {
		Integer number = null;
		if(update != null){
			number = update.getNumber();
		}
		if((number == null || number <= 0) && persistence != null){
			number = persistence.getNumber();
		}
		return number;
	}

	/**
	 * 设置数量
	 *
	 * @param number 数量
	 */
	public void setNumber(Integer number) {
		update.setNumber(number);
	}

	/**
	 * 获取送货单号
	 *
	 * @return delivery_number - 送货单号
	 */
	public String getDeliveryNumber() {
		String deliveryNumber = null;
		if(update != null){
			deliveryNumber = update.getDeliveryNumber();
		}
		if(StringUtils.isBlank(deliveryNumber) && persistence != null){
			deliveryNumber = persistence.getDeliveryNumber();
		}
		return deliveryNumber;
	}

	/**
	 * 设置送货单号
	 *
	 * @param deliveryNumber 送货单号
	 */
	public void setDeliveryNumber(String deliveryNumber) {
		update.setDeliveryNumber(deliveryNumber);
	}

	/**
	 * 获取纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
	 *
	 * @return papery_receipt_status - 纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
	 */
	public Integer getPaperyReceiptStatus() {
		Integer paperyReceiptStatus = null;
		if(update != null){
			paperyReceiptStatus = update.getPaperyReceiptStatus();
		}
		if((paperyReceiptStatus == null || paperyReceiptStatus <= 0) && persistence != null){
			paperyReceiptStatus = persistence.getPaperyReceiptStatus();
		}
		return paperyReceiptStatus;
	}

	/**
	 * 设置纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
	 *
	 * @param paperyReceiptStatus 纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)
	 */
	public void setPaperyReceiptStatus(PaperyReceiptFettle paperyReceiptFettle) {
		update.setReceiptVerifyStatus(paperyReceiptFettle.getCode());
	}

	/**
	 * 获取确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
	 *
	 * @return confirm_delivery_way - 确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
	 */
	public Integer getConfirmDeliveryWay() {
		Integer confirmDeliveryWay = null;
		if(update != null){
			confirmDeliveryWay = update.getConfirmDeliveryWay();
		}
		if((confirmDeliveryWay == null || confirmDeliveryWay <= 0) && persistence != null){
			confirmDeliveryWay = persistence.getConfirmDeliveryWay();
		}
		return confirmDeliveryWay;
	}

	/**
	 * 设置确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
	 *
	 * @param confirmDeliveryWay 确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)
	 */
	public void setConfirmDeliveryWay(Integer confirmDeliveryWay) {
		update.setConfirmDeliveryWay(confirmDeliveryWay);
	}

	/**
	 * 获取收货客户名称/公司名称
	 *
	 * @return receiver_name - 收货客户名称/公司名称
	 */
	public String getReceiverName() {
		String receiverName = null;
		if(update != null){
			receiverName = update.getReceiverName();
		}
		if(StringUtils.isBlank(receiverName) && persistence != null){
			receiverName = persistence.getReceiverName();
		}
		return receiverName;
	}

	/**
	 * 设置收货客户名称/公司名称
	 *
	 * @param receiverName 收货客户名称/公司名称
	 */
	public void setReceiverName(String receiverName) {
		update.setReceiverName(receiverName);
	}

	/**
	 * 获取收获客户座机
	 *
	 * @return receiver_tel - 收获客户座机
	 */
	public String getReceiverTel() {
		String receiverTel = null;
		if(update != null){
			receiverTel = update.getReceiverTel();
		}
		if(StringUtils.isBlank(receiverTel) && persistence != null){
			receiverTel = persistence.getReceiverTel();
		}
		return receiverTel;
	}

	/**
	 * 设置收获客户座机
	 *
	 * @param receiverTel 收获客户座机
	 */
	public void setReceiverTel(String receiverTel) {
		update.setReceiverTel(receiverTel);
	}

	/**
	 * 获取收货地址
	 *
	 * @return receive_address - 收货地址
	 */
	public String getReceiveAddress() {
		String receiveAddress = null;
		if(update != null){
			receiveAddress = update.getReceiveAddress();
		}
		if(StringUtils.isBlank(receiveAddress) && persistence != null){
			receiveAddress = persistence.getReceiveAddress();
		}
		return receiveAddress;
	}

	/**
	 * 设置收货地址
	 *
	 * @param receiveAddress 收货地址
	 */
	public void setReceiveAddress(String receiveAddress) {
		update.setReceiveAddress(receiveAddress);
	}

	/**
	 * 获取收货联系人姓名
	 *
	 * @return contact_name - 收货联系人姓名
	 */
	public String getContactName() {
		String contactName = null;
		if(update != null){
			contactName = update.getContactName();
		}
		if(StringUtils.isBlank(contactName) && persistence != null){
			contactName = persistence.getContactName();
		}
		return contactName;
	}

	/**
	 * 设置收货联系人姓名
	 *
	 * @param contactName 收货联系人姓名
	 */
	public void setContactName(String contactName) {
		update.setContactName(contactName);
	}

	/**
	 * 获取收货联系人电话
	 *
	 * @return contact_phone - 收货联系人电话
	 */
	public String getContactPhone() {
		String contactPhone = null;
		if(update != null){
			contactPhone = update.getContactPhone();
		}
		if(StringUtils.isBlank(contactPhone) && persistence != null){
			contactPhone = persistence.getContactPhone();
		}
		return contactPhone;
	}

	/**
	 * 设置收货联系人电话
	 *
	 * @param contactPhone 收货联系人电话
	 */
	public void setContactPhone(String contactPhone) {
		update.setContactPhone(contactPhone);
	}

	/**
	 * 获取经度
	 *
	 * @return longitude - 经度
	 */
	public String getLongitude() {
		String longitude = null;
		if(update != null){
			longitude = update.getLongitude();
		}
		if(StringUtils.isBlank(longitude) && persistence != null){
			longitude = persistence.getLongitude();
		}
		return longitude;
	}

	/**
	 * 设置经度
	 *
	 * @param longitude 经度
	 */
	public void setLongitude(String longitude) {
		update.setLongitude(longitude);
	}

	/**
	 * 获取纬度
	 *
	 * @return latitude - 纬度
	 */
	public String getLatitude() {
		String latitude = null;
		if(update != null){
			latitude = update.getLatitude();
		}
		if(StringUtils.isBlank(latitude) && persistence != null){
			latitude = persistence.getLatitude();
		}
		return latitude;
	}

	/**
	 * 设置纬度
	 *
	 * @param latitude 纬度
	 */
	public void setLatitude(String latitude) {
		update.setLatitude(latitude);
	}

	/**
	 * 获取电子围栏开关1开0关
	 *
	 * @return fence_status - 电子围栏开关1开0关
	 */
	public Integer getFenceStatus() {
		Integer fenceStatus = null;
		if(update != null){
			fenceStatus = update.getFenceStatus();
		}
		if((fenceStatus == null || fenceStatus <= 0) && persistence != null ){
			fenceStatus = persistence.getFenceStatus();
		}
		return fenceStatus;
	}

	/**
	 * 设置电子围栏开关1开0关
	 *
	 * @param fenceStatus 电子围栏开关1开0关
	 */
	public void setFenceStatus(Integer fenceStatus) {
		update.setFenceStatus(fenceStatus);
	}

	/**
	 * 获取定位次数
	 *
	 * @return position_count - 定位次数
	 */
	public Integer getPositionCount() {
		Integer positionCount = null;
		if(update != null){
			positionCount = update.getPositionCount();
		}
		if((positionCount == null || positionCount <= 0) && persistence != null) {
			positionCount = persistence.getPositionCount();
		}
		return positionCount;
	}

	/**
	 * 设置定位次数
	 *
	 * @param positionCount 定位次数
	 */
	public void setPositionCount(Integer positionCount) {
		update.setPositionCount(positionCount);
	}
	/**
	 * 获取回单数
	 *
	 * @return receipt_count - 回单数
	 */
	public Integer getReceiptCount() {
		Integer receiptCount = null;
		if(update != null){
			receiptCount = update.getReceiptCount();
		}
		if((receiptCount == null || receiptCount <= 0) && persistence != null){
			receiptCount = persistence.getReceiptCount();
		}
		return receiptCount;
	}

	/**
	 * 设置回单数
	 *
	 * @param receiptCount 回单数
	 */
	public void setReceiptCount(Integer receiptCount) {
		update.setReceiptCount(receiptCount);
	}

	/**
	 * 获取回单审核数
	 *
	 * @return receipt_verify_count - 回单审核数
	 */
	public Integer getReceiptVerifyCount() {
		Integer receiptVerifyCount = null;
		if(update != null){
			receiptVerifyCount = update.getReceiptVerifyCount();
		}
		if((receiptVerifyCount == null || receiptVerifyCount <= 0) && persistence != null ){
			receiptVerifyCount = persistence.getReceiptVerifyCount();
		}
		return receiptVerifyCount;
	}

	/**
	 * 设置回单审核数
	 *
	 * @param receiptVerifyCount 回单审核数
	 */
	public void setReceiptVerifyCount(Integer receiptVerifyCount) {
		update.setReceiptVerifyCount(receiptVerifyCount);
	}

	/**
	 * 获取回单审核状态(1:未上传,2:待审核,3:审核中,4:已审核)
	 *
	 * @return receipt_verify_status - 回单审核状态(1:未上传,2:待审核,3:审核中,4:已审核)
	 */
	public ReceiptVerifyFettle getReceiptVerifyStatus() {
		Integer fettle = null;
		if(update != null){
			fettle = update.getReceiptVerifyStatus();
		}
		if((fettle == null || fettle <= 0)  && persistence != null){
			fettle = persistence.getReceiptVerifyStatus();
		}
		return ReceiptVerifyFettle.convert(fettle);
	}

	/**
	 * 设置回单审核状态(1:未上传,2:待审核,3:审核中,4:已审核)
	 *
	 * @param receiptVerifyFettle 回单审核状态(1:未上传,2:待审核,3:审核中,4:已审核)
	 */
	public void setReceiptVerifyStatus(ReceiptVerifyFettle receiptVerifyFettle) {
		update.setReceiptVerifyStatus(receiptVerifyFettle.getCode());
	}

	/**
	 * 获取任务状态(20:发货,30:运输中,35:送达,40:收货)
	 *
	 * @return waybill_status - 任务状态(20:发货,30:运输中,35:送达,40:收货)
	 */
	public WaybillFettle getWaybillStatus() {
		Integer fettle = null;
		if(update != null){
			fettle = update.getWaybillStatus();
		}
		if((fettle == null || fettle <= 0) && persistence != null){
			fettle = persistence.getWaybillStatus();
		}
		return WaybillFettle.convert(fettle);
	}

	/**
	 * 设置任务状态(20:发货,30:运输中,35:送达,40:收货)
	 *
	 * @param waybillStatus 任务状态(20:发货,30:运输中,35:送达,40:收货)
	 */
	public void setWaybillStatus(WaybillFettle waybillFettle) {
		update.setWaybillStatus(waybillFettle.getCode());
	}

	/**
	 * getter method for fenceRadius
	 *
	 * @return the fenceRadius
	 */
	public Double getFenceRadius() {
		Double fenceRadius  = null;
		if(update != null){
			fenceRadius = update.getFenceRadius();
		}
		if(fenceRadius == null && persistence != null){
			fenceRadius = persistence.getFenceRadius();
		}
		return fenceRadius;
	}

	/**
	 * setter method for fenceRadius
	 *
	 * @param fenceRadius the fenceRadius to set
	 */
	public void setFenceRadius(Double fenceRadius) {
		update.setFenceRadius(fenceRadius);
	}

	/**
	 * getter method for receiptUnqualifyCount
	 *
	 * @return the receiptUnqualifyCount
	 */
	public Integer getReceiptUnqualifyCount() {
		Integer receiptUnqualifyCount = null;
		if(update != null){
			receiptUnqualifyCount = update.getReceiptUnqualifyCount();
		}
		if((receiptUnqualifyCount == null || receiptUnqualifyCount <= 0) && persistence != null){
			receiptUnqualifyCount = persistence.getReceiptUnqualifyCount();
		}
		return receiptUnqualifyCount;
	}

	/**
	 * setter method for receiptUnqualifyCount
	 *
	 * @param receiptUnqualifyCount the receiptUnqualifyCount to set
	 */
	public void setReceiptUnqualifyCount(Integer receiptUnqualifyCount) {
		update.setReceiptUnqualifyCount(receiptUnqualifyCount);
	}

	/**
	 * 获取始发地
	 *
	 * @return start_station - 始发地
	 */
	public String getStartStation() {
		String startStation = null;
		if(update != null){
			startStation = update.getStartStation();
		}
		if(StringUtils.isBlank(startStation) && persistence != null){
			startStation = persistence.getStartStation();
		}
		return startStation;
	}

	/**
	 * 设置始发地
	 *
	 * @param startStation 始发地
	 */
	public void setStartStation(String startStation) {
		update.setStartStation(startStation);
	}

	/**
	 * 获取始发地简称
	 *
	 * @return simple_start_station - 始发地简称
	 */
	public String getSimpleStartStation() {
		String simpleStartStation = null;
		if(update != null){
			simpleStartStation = update.getSimpleStartStation();
		}
		if(StringUtils.isBlank(simpleStartStation) && persistence != null){
			simpleStartStation = persistence.getSimpleStartStation();
		}
		return simpleStartStation;
	}

	/**
	 * 设置始发地简称
	 *
	 * @param simpleStartStation 始发地简称
	 */
	public void setSimpleStartStation(String simpleStartStation) {
		update.setSimpleStartStation(simpleStartStation);
	}

	/**
	 * 获取目的地
	 *
	 * @return end_station - 目的地
	 */
	public String getEndStation() {
		String endStation = null;
		if(update != null){
			endStation = update.getEndStation();
		}
		if(StringUtils.isBlank(endStation) && persistence != null){
			endStation = persistence.getEndStation();
		}
		return endStation;
	}

	/**
	 * 设置目的地
	 *
	 * @param endStation 目的地
	 */
	public void setEndStation(String endStation) {
		update.setEndStation(endStation);
	}

	/**
	 * 获取目的地简称
	 *
	 * @return simple_end_station - 目的地简称
	 */
	public String getSimpleEndStation() {
		String simpleEndStation = null;
		if(update != null){
			simpleEndStation = update.getSimpleEndStation();
		}
		if(StringUtils.isBlank(simpleEndStation) && persistence != null){
			simpleEndStation = persistence.getSimpleEndStation();
		}
		return simpleEndStation;
	}

	/**
	 * 设置目的地简称
	 *
	 * @param simpleEndStation 目的地简称
	 */
	public void setSimpleEndStation(String simpleEndStation) {
		update.setSimpleEndStation(simpleEndStation);
	}
	/**
	 * 获取发货方地址
	 *
	 * @return shipper_address - 发货方地址
	 */
	public String getShipperAddress() {
		String shipperAddress = null;
		if(update != null){
			shipperAddress = update.getShipperAddress();
		}
		if(StringUtils.isBlank(shipperAddress) && persistence != null){
			shipperAddress = persistence.getShipperAddress();
		}
		return shipperAddress;
	}

	/**
	 * 设置发货方地址
	 *
	 * @param shipperAddress 发货方地址
	 */
	public void setShipperAddress(String shipperAddress) {
		update.setShipperAddress(shipperAddress);
	}

	/**
	 * 获取发货方固定电话
	 *
	 * @return shipper_tel - 发货方固定电话
	 */
	public String getShipperTel() {
		String shipperTel = null;
		if(update != null){
			shipperTel = update.getShipperTel();
		}
		if(StringUtils.isBlank(shipperTel) && persistence != null){
			shipperTel = persistence.getShipperTel();
		}
		return shipperTel;
	}

	/**
	 * 设置发货方固定电话
	 *
	 * @param shipperTel 发货方固定电话
	 */
	public void setShipperTel(String shipperTel) {
		update.setShipperTel(shipperTel);
	}

	/**
	 * 获取发货方联系人
	 *
	 * @return shipper_contact_name - 发货方联系人
	 */
	public String getShipperContactName() {
		String shipperContactName = null;
		if(update != null){
			shipperContactName = update.getShipperContactName();
		}
		if(StringUtils.isBlank(shipperContactName) && persistence != null){
			shipperContactName = persistence.getShipperContactName();
		}
		return shipperContactName;
	}

	/**
	 * 设置发货方联系人
	 *
	 * @param shipperContactName 发货方联系人
	 */
	public void setShipperContactName(String shipperContactName) {
		update.setShipperContactName(shipperContactName);
	}

	/**
	 * 获取发货方联系人电话
	 *
	 * @return shipper_contact_tel - 发货方联系人电话
	 */
	public String getShipperContactTel() {
		String shipperContactTel = null;
		if(update != null){
			shipperContactTel = update.getShipperContactTel();
		}
		if(StringUtils.isBlank(shipperContactTel) && persistence != null){
			shipperContactTel = persistence.getShipperContactTel();
		}
		return shipperContactTel;
	}

	/**
	 * 设置发货方联系人电话
	 *
	 * @param shipperContactTel 发货方联系人电话
	 */
	public void setShipperContactTel(String shipperContactTel) {
		update.setShipperContactTel(shipperContactTel);
	}

	/**
	 * 获取绑定时间
	 *
	 * @return bind_time - 绑定时间
	 */
	public Date getBindTime() {
		Date bindTime = null;
		if(update != null){
			bindTime = update.getBindTime();
		}
		if(bindTime == null && persistence != null){
			bindTime = persistence.getBindTime();
		}
		return bindTime;
	}

	/**
	 * 设置绑定时间
	 *
	 * @param bindTime 绑定时间
	 */
	public void setBindTime(Date bindTime) {
		update.setBindTime(bindTime);
		if(update.getDeliveryTime() == null){
			update.setDeliveryTime(bindTime);
		}
	}

	/**
	 * 获取发货运输开始时间
	 *
	 * @return delivery_time - 发货运输开始时间
	 */
	public Date getDeliveryTime() {
		Date deliveryTime = null;
		if(update != null){
			deliveryTime = update.getDeliveryTime();
		}
		if(deliveryTime == null && persistence != null){
			deliveryTime = persistence.getDeliveryTime();
		}
		return deliveryTime;
	}

	/**
	 * 设置发货运输开始时间
	 *
	 * @param deliveryTime 发货运输开始时间
	 */
	public void setDeliveryTime(Date deliveryTime) {
		update.setDeliveryTime(deliveryTime);
	}

}
