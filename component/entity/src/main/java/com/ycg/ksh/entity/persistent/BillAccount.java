package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * 账单
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:34:04
 */
@Table(name = "`bill_account_tab`")
public class BillAccount extends BaseEntity {

	private static final long serialVersionUID = 1073247714890494477L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`waybillid`")
    private Integer waybillid;

    @Column(name = "`userid`")
    private Integer userid;

    @Column(name = "`income`")
    private Double income;

    @Column(name = "`incomeremark`")
    private String incomeremark;

    @Column(name = "`expenditure`")
    private Double expenditure;

    @Column(name = "`expenditureremark`")
    private String expenditureremark;

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
     * @return waybillid
     */
    public Integer getWaybillid() {
        return waybillid;
    }

    /**
     * @param waybillid
     */
    public void setWaybillid(Integer waybillid) {
        this.waybillid = waybillid;
    }

    /**
     * @return userid
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * @param userid
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * @return income
     */
    public Double getIncome() {
        return income;
    }

    /**
     * @param income
     */
    public void setIncome(Double income) {
        this.income = income;
    }

    /**
     * @return incomeremark
     */
    public String getIncomeremark() {
        return incomeremark;
    }

    /**
     * @param incomeremark
     */
    public void setIncomeremark(String incomeremark) {
        this.incomeremark = incomeremark;
    }

    /**
     * @return expenditure
     */
    public Double getExpenditure() {
        return expenditure;
    }

    /**
     * @param expenditure
     */
    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    /**
     * @return expenditureremark
     */
    public String getExpenditureremark() {
        return expenditureremark;
    }

    /**
     * @param expenditureremark
     */
    public void setExpenditureremark(String expenditureremark) {
        this.expenditureremark = expenditureremark;
    }
}