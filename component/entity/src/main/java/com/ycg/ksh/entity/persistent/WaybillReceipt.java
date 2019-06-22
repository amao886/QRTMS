package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.UserUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 回单
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:47:00
 */
@Table(name = "`receipt_tab`")
public class WaybillReceipt extends BaseEntity {

	private static final long serialVersionUID = 9150210445381055118L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 回单上传时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 上传人id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 运单id
     */
    @Column(name = "`waybillid`")
    private Integer waybillid;

    /**
     * 上传人姓名
     */
    @Column(name = "`uname`")
    private String uname;


    
    public WaybillReceipt() {
		super();
	}

	public WaybillReceipt(Integer userid, String uname, Integer waybillid) {
		super();
		this.userid = userid;
		this.waybillid = waybillid;
		this.uname = uname;
		this.createtime = new Date();
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
     * 获取回单上传时间
     *
     * @return createtime - 回单上传时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置回单上传时间
     *
     * @param createtime 回单上传时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取上传人id
     *
     * @return userid - 上传人id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置上传人id
     *
     * @param userid 上传人id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取运单id
     *
     * @return waybillid - 运单id
     */
    public Integer getWaybillid() {
        return waybillid;
    }

    /**
     * 设置运单id
     *
     * @param waybillid 运单id
     */
    public void setWaybillid(Integer waybillid) {
        this.waybillid = waybillid;
    }

    /**
     * 获取上传人姓名
     *
     * @return uname - 上传人姓名
     */
    public String getUname() {
        return uname;
    }

    /**
     * 设置上传人姓名
     *
     * @param uname 上传人姓名
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUnamezn() {
        return UserUtil.decodeName(uname);
    }
}