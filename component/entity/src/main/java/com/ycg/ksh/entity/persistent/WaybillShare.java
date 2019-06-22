package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 运单分享
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-24 17:15:16
 */
@Table(name = "`share_user_jurisdiction_tab`")
public class WaybillShare extends BaseEntity {

	private static final long serialVersionUID = 1105482049918921675L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 接收人（对应userid）
     */
    @Column(name = "`acceptid`")
    private Integer acceptid;

    /**
     * 数据权限：1为查看所有信息，2为查看部分权限。
     */
    @Column(name = "`jurisdiction`")
    private Integer jurisdiction;

    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 运单主键
     */
    @Column(name = "`waybillid`")
    private Integer waybillid;

    /**
     * 分享人(对应用户Id)
     */
    @Column(name = "`shareid`")
    private Integer shareid;

    /**
     * 当数据权限为查看部分（2）时对应此用户上传信息。（如：异常、回单等）
     */
    @Column(name = "`paramid`")
    private Integer paramid;

    
    /**
	 * 创建一个新的 WaybillShare实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 00:02:12
	 */
	public WaybillShare() {
		super();
	}

	/**
	 * 创建一个新的 WaybillShare实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 00:01:59
	 * @param acceptid
	 * @param waybillid
	 */
	public WaybillShare(Integer acceptid, Integer waybillid) {
		super();
		this.acceptid = acceptid;
		this.waybillid = waybillid;
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
     * 获取接收人（对应userid）
     *
     * @return acceptid - 接收人（对应userid）
     */
    public Integer getAcceptid() {
        return acceptid;
    }

    /**
     * 设置接收人（对应userid）
     *
     * @param acceptid 接收人（对应userid）
     */
    public void setAcceptid(Integer acceptid) {
        this.acceptid = acceptid;
    }

    /**
     * 获取数据权限：1为查看所有信息，2为查看部分权限。
     *
     * @return jurisdiction - 数据权限：1为查看所有信息，2为查看部分权限。
     */
    public Integer getJurisdiction() {
        return jurisdiction;
    }

    /**
     * 设置数据权限：1为查看所有信息，2为查看部分权限。
     *
     * @param jurisdiction 数据权限：1为查看所有信息，2为查看部分权限。
     */
    public void setJurisdiction(Integer jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取运单主键
     *
     * @return waybillid - 运单主键
     */
    public Integer getWaybillid() {
        return waybillid;
    }

    /**
     * 设置运单主键
     *
     * @param waybillid 运单主键
     */
    public void setWaybillid(Integer waybillid) {
        this.waybillid = waybillid;
    }

    /**
     * 获取分享人(对应用户Id)
     *
     * @return shareid - 分享人(对应用户Id)
     */
    public Integer getShareid() {
        return shareid;
    }

    /**
     * 设置分享人(对应用户Id)
     *
     * @param shareid 分享人(对应用户Id)
     */
    public void setShareid(Integer shareid) {
        this.shareid = shareid;
    }

    /**
     * 获取当数据权限为查看部分（2）时对应此用户上传信息。（如：异常、回单等）
     *
     * @return paramid - 当数据权限为查看部分（2）时对应此用户上传信息。（如：异常、回单等）
     */
    public Integer getParamid() {
        return paramid;
    }

    /**
     * 设置当数据权限为查看部分（2）时对应此用户上传信息。（如：异常、回单等）
     *
     * @param paramid 当数据权限为查看部分（2）时对应此用户上传信息。（如：异常、回单等）
     */
    public void setParamid(Integer paramid) {
        this.paramid = paramid;
    }
}