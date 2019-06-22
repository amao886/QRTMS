package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 资源申请
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:47:38
 */
@Table(name = "`resourcse_app_tab`")
public class ApplyRes extends BaseEntity {

	private static final long serialVersionUID = -4694565229789280153L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 所属组
     */
    @Column(name = "`groupid`")
    private Integer groupid;

    /**
     * 申请数量
     */
    @Column(name = "`number`")
    private Integer number;
    /**
     * 申请人id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 资源申请时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 状态(0:已申请未生成, 1:可以生成,2:已生成未下载,3:已经下载过)
     */
    @Column(name = "`print_status`")
    private Integer printStatus;

    @Column(name = "`remark`")
    private String remark;
    /**
     * 开始号码
     */
    @Column(name = "`start_num`")
    private String startNum;
    /**
     * 结束号码
     */
    @Column(name = "`end_num`")
    private String endNum;

    /**
     * 公司的id
     */
    @Column(name = "`company_id`")
    private Long companyId;

    public ApplyRes() {
        super();
    }

    public ApplyRes(Integer id, Integer printStatus) {
        super();
        this.id = id;
        this.printStatus = printStatus;
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
     * 获取申请数量
     *
     * @return number - 申请数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置申请数量
     *
     * @param number 申请数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取申请人id
     *
     * @return userid - 申请人id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置申请人id
     *
     * @param userid 申请人id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取资源申请时间
     *
     * @return createtime - 资源申请时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置资源申请时间
     *
     * @param createtime 资源申请时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取打印状态
     *
     * @return print_status - 打印状态
     */
    public Integer getPrintStatus() {
        return printStatus;
    }

    /**
     * 设置打印状态
     *
     * @param printStatus 打印状态
     */
    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * getter method for startNum
     * @return the startNum
     */
    public String getStartNum() {
        return startNum;
    }

    /**
     * setter method for startNum
     * @param startNum the startNum to set
     */
    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    /**
     * getter method for endNum
     * @return the endNum
     */
    public String getEndNum() {
        return endNum;
    }

    /**
     * setter method for endNum
     * @param endNum the endNum to set
     */
    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }
}