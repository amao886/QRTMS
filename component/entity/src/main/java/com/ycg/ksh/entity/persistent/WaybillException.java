package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.UserUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 异常报告
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:41:51
 */
@Table(name = "`exception_repor_tab`")
public class WaybillException extends BaseEntity {

	private static final long serialVersionUID = -4937098566097727664L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 异常内容（描述）
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 上传人主键
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 异常上报时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 运单id
     */
    @Column(name = "`waybillid`")
    private Integer waybillid;

    /**
     * 上报人姓名
     */
    @Column(name = "`uname`")
    private String uname;

    /**
     * 运单ID
     */
    @Column(name = "`conveyance_id`")
    private Long conveyanceId;

    public Long getConveyanceId() {
        return conveyanceId;
    }

    public void setConveyanceId(Long conveyanceId) {
        this.conveyanceId = conveyanceId;
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
     * 获取异常内容（描述）
     *
     * @return content - 异常内容（描述）
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置异常内容（描述）
     *
     * @param content 异常内容（描述）
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取上传人主键
     *
     * @return userid - 上传人主键
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置上传人主键
     *
     * @param userid 上传人主键
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取异常上报时间
     *
     * @return createtime - 异常上报时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置异常上报时间
     *
     * @param createtime 异常上报时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
     * 获取上报人姓名
     *
     * @return uname - 上报人姓名
     */
    public String getUname() {
        return uname;
    }

    /**
     * 设置上报人姓名
     *
     * @param uname 上报人姓名
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUnamezn() {
        return UserUtil.decodeName(uname);
    }
}