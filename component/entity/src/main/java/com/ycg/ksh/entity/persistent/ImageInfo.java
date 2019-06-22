package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 回单图片和异常图片
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:31:54
 */
@Table(name = "`address_tab`")
public class ImageInfo extends BaseEntity {
    
	private static final long serialVersionUID = -3537242269079701896L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 图片路径
     */
    @Column(name = "`path`")
    private String path;

    /**
     * 上传时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 异常主键
     */
    @Column(name = "`exception_repor_id`")
    private Integer exceptionReporId;

    /**
     * 回单主键
     */
    @Column(name = "`receiptid`")
    private Integer receiptid;

    /**
     * 审核状态;1:合格,0:不合格
     */
    @Column(name = "`verify_status`")
    private Integer verifyStatus;
    
    /**
     * 审核备注,不合格原因
     */
    @Column(name = "`verify_remark`")
    private String verifyRemark;
    
    
    /**
     * 审核人ID
     */
    @Column(name = "`verify_uid`")
    private Integer verifyUid;
    
    
    /**
     * 审核时间
     */
    @Column(name = "`verify_date`")
    private Date verifyDate;
    
    
    
    public ImageInfo() {
		super();
	}

	public ImageInfo(Integer verifyUid, Integer id, Integer verifyStatus, String verifyRemark) {
		super();
		this.id = id;
		this.verifyStatus = verifyStatus;
		this.verifyRemark = verifyRemark;
		this.verifyUid = verifyUid;
	}

	public ImageInfo(String path, Date createtime, Integer receiptid) {
		super();
		this.path = path;
		this.createtime = createtime;
		this.receiptid = receiptid;
	}

	public ImageInfo(Integer exceptionReporId, String path, Date createtime) {
		super();
		this.path = path;
		this.createtime = createtime;
		this.exceptionReporId = exceptionReporId;
	}

	/**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取图片路径
     *
     * @return path - 图片路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置图片路径
     *
     * @param path 图片路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取上传时间
     *
     * @return createtime - 上传时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置上传时间
     *
     * @param createtime 上传时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取异常主键
     *
     * @return exception_repor_id - 异常主键
     */
    public Integer getExceptionReporId() {
        return exceptionReporId;
    }

    /**
     * 设置异常主键
     *
     * @param exceptionReporId 异常主键
     */
    public void setExceptionReporId(Integer exceptionReporId) {
        this.exceptionReporId = exceptionReporId;
    }

    /**
     * 获取回单主键
     *
     * @return receiptid - 回单主键
     */
    public Integer getReceiptid() {
        return receiptid;
    }

    /**
     * 设置回单主键
     *
     * @param receiptid 回单主键
     */
    public void setReceiptid(Integer receiptid) {
        this.receiptid = receiptid;
    }

	/**
	 * getter method for verifyStatus
	 * @return the verifyStatus
	 */
	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	/**
	 * setter method for verifyStatus
	 * @param verifyStatus the verifyStatus to set
	 */
	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	/**
	 * getter method for verifyRemark
	 * @return the verifyRemark
	 */
	public String getVerifyRemark() {
		return verifyRemark;
	}

	/**
	 * setter method for verifyRemark
	 * @param verifyRemark the verifyRemark to set
	 */
	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	/**
	 * getter method for verifyUid
	 * @return the verifyUid
	 */
	public Integer getVerifyUid() {
		return verifyUid;
	}

	/**
	 * setter method for verifyUid
	 * @param verifyUid the verifyUid to set
	 */
	public void setVerifyUid(Integer verifyUid) {
		this.verifyUid = verifyUid;
	}

	/**
	 * getter method for verifyDate
	 * @return the verifyDate
	 */
	public Date getVerifyDate() {
		return verifyDate;
	}

	/**
	 * setter method for verifyDate
	 * @param verifyDate the verifyDate to set
	 */
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
}