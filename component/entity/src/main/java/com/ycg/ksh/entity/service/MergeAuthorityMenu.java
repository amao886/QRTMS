package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.EmployeeAuthority;

public class MergeAuthorityMenu extends EmployeeAuthority {

	/**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-29 09:25:27
	 */
	private static final long serialVersionUID = 2912239992965888287L;

	private String menuName;

	/**
	 * getter method for menuName
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * setter method for menuName
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
}
