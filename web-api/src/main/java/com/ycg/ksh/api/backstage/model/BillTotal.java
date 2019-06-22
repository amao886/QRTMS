package com.ycg.ksh.api.backstage.model;

import com.ycg.ksh.common.entity.RequestEntity;

/**        
 * 名称：    BillTotal    
 * 描述：    任务单统计
 * 创建人：zcl    
 * 创建时间：2017年11月17日 下午1:59:03
 * @version        
 */
public class BillTotal extends RequestEntity{
	
	private static final long serialVersionUID = 5778670408435192486L;
    private Integer allCount;  //发货任务总数
    private String  companyName; //客户名称
    private String createTime;//发货日期
    private Integer toCount;//应到任务数
    private Integer sendCount;//已送达任务数
    private Integer timeCount;//按时送达任务数
    private Integer groupid;//用户组id
    private String  sendRate; //准时到达率
    private String startTime;//开始日期
    private String endTime;//结束日期
    private Integer userId;//用户id
    private String year;//年
    private String month;//月
    private Integer flag;//统计标记null全部 ,1昨天，2最近7天，3最近三十天
	public Integer getAllCount() {
		return allCount;
	}
	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getToCount() {
		return toCount;
	}
	public void setToCount(Integer toCount) {
		this.toCount = toCount;
	}
	public Integer getSendCount() {
		return sendCount;
	}
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	public Integer getTimeCount() {
		return timeCount;
	}
	public void setTimeCount(Integer timeCount) {
		this.timeCount = timeCount;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getSendRate() {
		return sendRate;
	}
	public void setSendRate(String sendRate) {
		this.sendRate = sendRate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
}
