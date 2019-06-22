package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`map_address`")
public class MapAddress extends BaseEntity {

	private static final long serialVersionUID = 849869002441073388L;

	/**
     * 主键ID
     */
    @Id
    @Column(name = "`id`")
    private Integer id;

    /**
     * 地址
     */
    @Column(name = "`address`")
    private String address;
    /**
     * 结构化地址
     */
    @Column(name = "`format_address`")
    private String formatAddress;
    /**
     * 包裹数量
     */
    @Column(name = "`package_count`")
	private Integer packageCount;
    /**
     * 地址类型
     */
    @Column(name = "`address_type`")
    private Integer addressType;
    /**
     * 纬度
     */
    @Column(name = "`latitude`")
    private String latitude;

    /**
     * 经度
     */
    @Column(name = "`longitude`")
    private String longitude;

    /**
     * 用户编号
     */
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 备注
     */
    @Column(name = "`remark`")
    private String remark;
    /**
     * 更新时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 状态(1:没完成,2:已完成)
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取经度
     *
     * @return latitude - 经度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置经度
     *
     * @param latitude 经度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取纬度
     *
     * @return longitude - 纬度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置纬度
     *
     * @param longitude 纬度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取用户编号
     *
     * @return user_id - 用户编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户编号
     *
     * @param userId 用户编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
	 * getter method for remark
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * setter method for remark
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
     * 获取更新时间
     *
     * @return modify_time - 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取状态(1:没完成,2:已完成)
     *
     * @return status - 状态(1:没完成,2:已完成)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态(1:没完成,2:已完成)
     *
     * @param status 状态(1:没完成,2:已完成)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

	/**
	 * getter method for packageCount
	 * @return the packageCount
	 */
	public Integer getPackageCount() {
		return packageCount;
	}

	/**
	 * setter method for packageCount
	 * @param packageCount the packageCount to set
	 */
	public void setPackageCount(Integer packageCount) {
		this.packageCount = packageCount;
	}

	/**
	 * getter method for addressType
	 * @return the addressType
	 */
	public Integer getAddressType() {
		return addressType;
	}

	/**
	 * setter method for addressType
	 * @param addressType the addressType to set
	 */
	public void setAddressType(Integer addressType) {
		this.addressType = addressType;
	}

	/**
	 * getter method for formatAddress
	 * @return the formatAddress
	 */
	public String getFormatAddress() {
		return formatAddress;
	}

	/**
	 * setter method for formatAddress
	 * @param formatAddress the formatAddress to set
	 */
	public void setFormatAddress(String formatAddress) {
		this.formatAddress = formatAddress;
	}
}