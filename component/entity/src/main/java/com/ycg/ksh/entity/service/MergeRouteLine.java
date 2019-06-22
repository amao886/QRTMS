package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.entity.persistent.RouteLine;
import org.apache.commons.beanutils.BeanUtils;

public class MergeRouteLine extends RouteLine {

	/**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 11:18:41
	 */
	private static final long serialVersionUID = 9194633647944304774L;
	public MergeRouteLine() {
		super();
	}
	public MergeRouteLine(RouteLine routeLine) throws Exception{
		super();
		BeanUtils.copyProperties(this, routeLine);
		this.setAddress(RegionUtils.merge('/',routeLine.getProvince(), routeLine.getCity(), routeLine.getDistrict()));
	}

	private AssociateUser user;//承运人

	private String userNick;//好友姓名
	
	private String mobile;//手机号
	
	private String address;

	public String getSimpleStation(){
		return RegionUtils.simple(getProvince(), getCity(), getDistrict());
	}

	public String getStation(){
		return RegionUtils.merge(getProvince(), getCity(), getDistrict());
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AssociateUser getUser() {
		return user;
	}

	public void setUser(AssociateUser user) {
		this.user = user;
	}
}
