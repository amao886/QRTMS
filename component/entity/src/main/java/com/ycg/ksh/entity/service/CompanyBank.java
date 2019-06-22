package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.CompanyBankVerify;

public class CompanyBank extends CompanyBankVerify {

	private static final long serialVersionUID = 5500857181213983865L;

	private Company company;//企业信息

	public CompanyBank() {
	}

	public CompanyBank(CompanyBankVerify bankVerify, Company company) {
		try{
			BeanUtils.build().copyProperties(this, bankVerify);
		}catch (Exception e){ }
		this.company = company;
	}

	/**
	 * getter method for company
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * setter method for company
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

}
