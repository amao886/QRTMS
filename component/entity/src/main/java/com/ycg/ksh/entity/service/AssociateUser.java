/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 14:40:42
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.User;

import java.util.Date;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-27 14:40:42
 */
public class AssociateUser extends BaseEntity {

	private static final long serialVersionUID = 4377196495617413985L;
	
	private Integer id;
	private String mobilephone;
	private String encryptName;
	private String unamezn;
    private Date createtime;
	private String subscribe;
	private String company;//备注公司
	private Integer type;//0:普通 1：货主 2：承运商 3：司机

	public AssociateUser() {
		super();
	}
	
	public AssociateUser(User user) {
		if(user != null) {
			this.id = user.getId();
			this.mobilephone = user.getMobilephone();
			this.unamezn = user.getUnamezn();
			this.encryptName = user.getUname();
			this.createtime = user.getCreatetime();
			this.subscribe = user.getPassword();
		}
	}

	@Override
	public String toString() {
		return "User->{id:"+ id +", name:"+ unamezn + "}";
	}

	/**
	 * getter method for id
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * setter method for id
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * getter method for mobilephone
	 * @return the mobilephone
	 */
	public String getMobilephone() {
		return mobilephone;
	}
	/**
	 * setter method for mobilephone
	 * @param mobilephone the mobilephone to set
	 */
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	/**
	 * getter method for unamezn
	 * @return the unamezn
	 */
	public String getUnamezn() {
		return unamezn;
	}
	/**
	 * setter method for unamezn
	 * @param unamezn the unamezn to set
	 */
	public void setUnamezn(String unamezn) {
		this.unamezn = unamezn;
	}

    /**
     * getter method for createtime
     * @return the createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * setter method for createtime
     * @param createtime the createtime to set
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getEncryptName() {
		return encryptName;
	}

	public void setEncryptName(String encryptName) {
		this.encryptName = encryptName;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
