package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

public class CompanySearch extends BaseEntity{

	/**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-02 16:18:45
	 */
	private static final long serialVersionUID = -1274939937616643078L;

	private Integer userId;
	
	private String companyName;

	private String companyCode;

	private Integer fettle;

	private Integer signFettle;

	/**
	 * getter method for companyName
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * setter method for companyName
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * getter method for fettle
	 * @return the fettle
	 */
	public Integer getFettle() {
		return fettle;
	}

	/**
	 * setter method for fettle
	 * @param fettle the fettle to set
	 */
	public void setFettle(Integer fettle) {
		this.fettle = fettle;
	}

	/**
	 * getter method for signFettle
	 * @return the signFettle
	 */
	public Integer getSignFettle() {
		return signFettle;
	}

	/**
	 * setter method for signFettle
	 * @param signFettle the signFettle to set
	 */
	public void setSignFettle(Integer signFettle) {
		this.signFettle = signFettle;
	}

	/**
	 * getter method for userId
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * setter method for userId
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * getter method for companyCode
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * setter method for companyCode
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
}
