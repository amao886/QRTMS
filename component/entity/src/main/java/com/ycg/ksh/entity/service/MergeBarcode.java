package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.util.UserUtil;
import com.ycg.ksh.entity.persistent.Barcode;

public class MergeBarcode extends Barcode {
	private static final long serialVersionUID = 1L;
	
	private String uname;
	private String companyName;
	private Integer total;
	private Integer useNumber;
	private String startNum;
	private String endNum;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getStartNum() {
		return startNum;
	}
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	public Integer getUseNumber() {
		return useNumber;
	}
	public void setUseNumber(Integer useNumber) {
		this.useNumber = useNumber;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getUnamezn() {
        return UserUtil.decodeName(uname);
    }
}
