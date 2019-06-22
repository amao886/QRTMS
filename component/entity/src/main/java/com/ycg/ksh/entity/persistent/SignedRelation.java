package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`T_SIGNED_RELATION`")
public class SignedRelation extends BaseEntity{
    /**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 15:01:40
	 */
	private static final long serialVersionUID = 3311920406140872829L;

	/**
     * 编号
     */
	@Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_ID`")
    private Integer createId;

    /**
     * 发货方公司
     */
    @Column(name = "`COMPANY_NAME`")
    private String companyName;

    /**
     * 签署方公司编号
     */
    @Column(name = "`SIGN_COMPANY_NAME`")
    private String signCompanyName;

    /**
     * 创建时间
     */
    @Column(name = "`CREATETIME`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`UPDATETIME`")
    private Date updatetime;

    /**
     * 签收开始时间
     */
    @Column(name = "`STARTTIME`")
    private Date starttime;

    /**
     * 签收结束时间
     */
    @Column(name = "`ENDTIME`")
    private Date endtime;

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_ID - 创建人
     */
    public Integer getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    /**
     * 获取发货方公司
     *
     * @return COMPANY_ID - 发货方公司
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置发货方公司
     *
     * @param companyId 发货方公司
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取签署方公司
     *
     * @return SIGN_COMPANY_ID - 签署方公司
     */
    public String getSignCompanyName() {
        return signCompanyName;
    }

    /**
     * 设置签署方公司编号
     *
     * @param signCompanyId 签署方公司
     */
    public void setSignCompanyName(String signCompanyName) {
        this.signCompanyName = signCompanyName;
    }

    /**
     * 获取创建时间
     *
     * @return CREATETIME - 创建时间
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
     * @return UPDATETIME - 修改时间
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
     * 获取签收开始时间
     *
     * @return STARTTIME - 签收开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置签收开始时间
     *
     * @param starttime 签收开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取签收结束时间
     *
     * @return ENDTIME - 签收结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置签收结束时间
     *
     * @param endtime 签收结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}