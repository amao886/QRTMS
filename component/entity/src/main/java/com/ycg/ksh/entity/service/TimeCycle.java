/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:01:53
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Date;

/**
 * 时间周期
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:01:53
 */
public class TimeCycle extends BaseEntity {

	private static final long serialVersionUID = -7715123843382919053L;

	/**
	 * 开始时间
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:10:44
	 */
	private Date stime;
	/**
	 * 结束时间
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-15 11:10:47
	 */
	private Date etime;
	
	public TimeCycle(Date stime, Date etime) {
		super();
		this.stime = stime;
		this.etime = etime;
	}
	
	/**
	 * getter method for stime
	 * @return the stime
	 */
	public Date getStime() {
		return stime;
	}
	/**
	 * setter method for stime
	 * @param stime the stime to set
	 */
	public void setStime(Date stime) {
		this.stime = stime;
	}
	/**
	 * getter method for etime
	 * @return the etime
	 */
	public Date getEtime() {
		return etime;
	}
	/**
	 * setter method for etime
	 * @param etime the etime to set
	 */
	public void setEtime(Date etime) {
		this.etime = etime;
	}
}
