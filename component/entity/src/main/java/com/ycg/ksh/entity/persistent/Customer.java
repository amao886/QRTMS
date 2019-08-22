package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户实体类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:34:39
 */
@Table(name = "`customer_tab`")
public class Customer extends BaseEntity {

	private static final long serialVersionUID = -861989845486488004L;

	/**
     * 主键Id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属用户
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 收货客户名称（公司名称）
     */
    @Column(name = "`company_name`")
    private String companyName;

    /**
     * 联系人
     */
    @Column(name = "`contacts`")
    private String contacts;

    /**
     * 联系电话
     */
    @Column(name = "`contact_number`")
    private String contactNumber;

    /**
     * 座机
     */
    @Column(name = "`tel`")
    private String tel;
    
    /**
     * 省
     */
    @Column(name = "`province`")
    private String province;
    /**
     * 市
     */
    @Column(name = "`city`")
    private String city;
    /**
     * 区县
     */
    @Column(name = "`district`")
    private String district;
    
    /**
     * 收货地址
     */
    @Column(name = "`address`")
    private String address;
    /**
     * 完整地址
     */
    @Column(name = "`fullAddress`")
    private String fullAddress;
    

    /**
     * 经度
     */
    @Column(name = "`longitude`")
    private Double longitude;
    

    /**
     * 纬度
     */
    @Column(name = "`latitude`")
    private Double latitude;
    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`updatetime`")
    private Date updatetime;

    /**
     * 客户类型(1：收货客户 ，2：发货客户)
     */
    @Column(name = "`type`")
    private Integer type;

    @Column(name = "`group_id`")
    private Integer groupId;


    @Column(name = "`arrival_day`")
    private Integer arrivalDay;

    @Column(name = "`arrival_hour`")
    private Integer arrivalHour;

    @Column(name = "`company_customer_id`")
    private Long companyCustomerId;
    
    @Column(name = "`customerCode`")
    private String customerCode;
    
    public Customer(Integer id) {
        super();
        this.id = id;
    }

    public Customer(Integer type, Integer id) {
        super();
        this.id = id;
        this.type = type;
    }

    public Customer(Integer type, Integer id, String province, String city, String district, String address) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.type = type;
    }

    public Customer() {
        super();
    }

    /**
     * 获取主键Id
     *
     * @return id - 主键Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键Id
     *
     * @param id 主键Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取所属用户
     *
     * @return userid - 所属用户
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置所属用户
     *
     * @param userid 所属用户
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取收货客户名称（公司名称）
     *
     * @return company_name - 收货客户名称（公司名称）
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置收货客户名称（公司名称）
     *
     * @param companyName 收货客户名称（公司名称）
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取联系人
     *
     * @return contacts - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * 获取联系电话
     *
     * @return contact_number - 联系电话
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * 设置联系电话
     *
     * @param contactNumber 联系电话
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * 获取座机
     *
     * @return tel - 座机
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置座机
     *
     * @param tel 座机
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取收货地址
     *
     * @return address - 收货地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置收货地址
     *
     * @param address 收货地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取创建时间
     *
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取修改时间
     *
     * @return updatetime - 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置修改时间
     *
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	/**
	 * getter method for province
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * setter method for province
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * getter method for city
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * setter method for city
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * getter method for district
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * setter method for district
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * getter method for fullAddress
	 * @return the fullAddress
	 */
	public String getFullAddress() {
		return fullAddress;
	}

	/**
	 * setter method for fullAddress
	 * @param fullAddress the fullAddress to set
	 */
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	/**
	 * getter method for longitude
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * setter method for longitude
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * getter method for latitude
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * setter method for latitude
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
     * getter method for groupId
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * setter method for groupId
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    
    /**
     * getter method for getType
     * @return the type
     */
	public Integer getType() {
		return type;
	}
	
	/**
     * setter method for type
     * @param type the type to set
     */
	public void setType(Integer type) {
		this.type = type;
	}

    public Integer getArrivalDay() {
        return arrivalDay;
    }

    public void setArrivalDay(Integer arrivalDay) {
        this.arrivalDay = arrivalDay;
    }

    public Integer getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(Integer arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public Long getCompanyCustomerId() {
        return companyCustomerId;
    }

    public void setCompanyCustomerId(Long companyCustomerId) {
        this.companyCustomerId = companyCustomerId;
    }

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
    
}