package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 条码实体类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:32:16
 */
@Table(name = "`barcode_tab`")
public class Barcode extends BaseEntity {

    private static final long serialVersionUID = -4278585093669115526L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源申请ID
     */
    @Column(name = "`resourceid`")
    private Integer resourceid;

    /**
     * 条码编号
     */
    @Column(name = "`barcode`")
    private String barcode;

    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     */
    @Column(name = "`bindstatus`")
    private Integer bindstatus;

    /**
     * 条码批次
     */
    @Column(name = "`code_batch`")
    private String codeBatch;

    /**
     * 所属人
     */
    @Column(name = "`userid`")
    private Integer userid;
    /**
     * 所属组
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 公司的id
     */
    @Column(name = "`company_id`")
    private Long companyId;
    
    public Barcode() {
    	super();
    }
    
    public Barcode(Integer resourceid,Date createtime,Integer groupId,Long companyId){
    	this.resourceid = resourceid;
    	this.createtime = createtime;
    	this.groupid = groupId;
    	this.companyId = companyId;
    }

    public Barcode(Integer resourceid,Integer groupId,Long companyId){
        this.resourceid = resourceid;
        this.groupid = groupId;
        this.companyId = companyId;
    }
    
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
     * 获取资源申请ID
     *
     * @return resourceid - 资源申请ID
     */
    public Integer getResourceid() {
        return resourceid;
    }

    /**
     * 设置资源申请ID
     *
     * @param resourceid 资源申请ID
     */
    public void setResourceid(Integer resourceid) {
        this.resourceid = resourceid;
    }

    /**
     * 获取条码编号
     *
     * @return barcode - 条码编号
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 设置条码编号
     *
     * @param barcode 条码编号
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
     * 获取条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     *
     * @return bindstatus - 条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     */
    public Integer getBindstatus() {
        return bindstatus;
    }

    /**
     * 设置条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     *
     * @param bindstatus 条码状态（10:已分配，20:已绑定，30:运输中，40:已结束）
     */
    public void setBindstatus(Integer bindstatus) {
        this.bindstatus = bindstatus;
    }

    /**
     * 获取条码批次
     *
     * @return code_batch - 条码批次
     */
    public String getCodeBatch() {
        return codeBatch;
    }

    /**
     * 设置条码批次
     *
     * @param codeBatch 条码批次
     */
    public void setCodeBatch(String codeBatch) {
        this.codeBatch = codeBatch;
    }

    /**
     * 获取所属组
     *
     * @return groupid - 所属组
     */
    public Integer getGroupid() {
        return groupid;
    }

    /**
     * 设置所属组
     *
     * @param groupid 所属组
     */
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    /**
     * getter method for userid
     *
     * @return the userid
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * setter method for userid
     *
     * @param userid the userid to set
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}