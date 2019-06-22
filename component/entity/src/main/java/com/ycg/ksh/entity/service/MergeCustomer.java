package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import org.apache.commons.beanutils.BeanUtils;
/**
 * 
 * TODO web 客户列表实体
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 13:55:28
 */
public class MergeCustomer extends Customer{

	private static final long serialVersionUID = -6191230729055788215L;
	
	private ProjectGroup group;
	
	private String region; //省市区

    public MergeCustomer() {
        super();
    }

    public MergeCustomer(Customer customer) throws Exception {
        super();
        BeanUtils.copyProperties(this, customer);
        setRegion(getProvince()+(getCity()!=null ? getCity():"")+(getDistrict()!=null?getDistrict():""));
    }
	
    /**
     * getter method for group
     * @return the group
     */
    public ProjectGroup getGroup() {
        return group;
    }

    /**
     * setter method for group
     * @param group the group to set
     */
    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

	public String getRegion() {
        if(region == null || "null".equals(region)){
            return "";
        }
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
		
	}
}
