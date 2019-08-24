/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 10:15:52
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.Waybill;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 任务单详情，包括客户、条码、组、回单、轨迹...
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-21 10:15:52
 */
public class MergeWaybill extends Waybill {

    private static final long serialVersionUID = -7599960058662076839L;

    private Integer index;
    private String codeString;

    private Integer haveType;//任务来自1扫码2分享
    private Integer transportDays;// 已运天数
    private Boolean isGroupMermer;//是否是项目组成员
    private String shareName;//分享人名称
    private String shareId;//分享Id
    
    private ProjectGroup group;//组
    private Collection<MergeWaybillException> exceptions;//异常
    private Collection<MergeReceipt> receipts;//回单
    private Collection<MergeTrack> tracks;//轨迹
    private Collection<Goods> goods;//货物明细
    
    private Integer arriveDay;
    private Integer arriveHour;
    
    //发货客户编码
    private String shipperCode;
    //收获客户编码
    private String receiverCode;

    public MergeWaybill() {
        super();
    }

    public MergeWaybill(Waybill waybill) throws Exception {
        super();
        BeanUtils.copyProperties(this, waybill);
    }
    /**
     * @see com.ycg.ksh.common.entity.BaseEntity#toString()
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-21 10:28:03
     */
    @Override
    public String toString() {
        return StringUtils.toString(this);
    }

    public void addGoods(Goods obj){
        if(goods == null){
            goods = new ArrayList<Goods>();
        }
        goods.add(obj);
    }
    /**
     * getter method for group
     *
     * @return the group
     */
    public ProjectGroup getGroup() {
        return group;
    }

    /**
     * setter method for group
     *
     * @param group the group to set
     */
    public void setGroup(ProjectGroup group) {
        this.group = group;
    }

    /**
     * getter method for haveType
     *
     * @return the haveType
     */
    public Integer getHaveType() {
        return haveType;
    }

    /**
     * setter method for haveType
     *
     * @param haveType the haveType to set
     */
    public void setHaveType(Integer haveType) {
        this.haveType = haveType;
    }

    /**
     * getter method for transportDays
     *
     * @return the transportDays
     */
    public Integer getTransportDays() {
        return transportDays;
    }

    /**
     * setter method for transportDays
     *
     * @param transportDays the transportDays to set
     */
    public void setTransportDays(Integer transportDays) {
        this.transportDays = transportDays;
    }

    /**
     * getter method for isGroupMermer
     *
     * @return the isGroupMermer
     */
    public Boolean getIsGroupMermer() {
        return isGroupMermer;
    }

    /**
     * setter method for isGroupMermer
     *
     * @param isGroupMermer the isGroupMermer to set
     */
    public void setIsGroupMermer(Boolean isGroupMermer) {
        this.isGroupMermer = isGroupMermer;
    }

    /**
     * getter method for shareName
     * @return the shareName
     */
    public String getShareName() {
        return shareName;
    }

    /**
     * setter method for shareName
     * @param shareName the shareName to set
     */
    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    /**
     * getter method for shareId
     * @return the shareId
     */
    public String getShareId() {
        return shareId;
    }

    /**
     * setter method for shareId
     * @param shareId the shareId to set
     */
    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    /**
     * getter method for exceptions
     * @return the exceptions
     */
    public Collection<MergeWaybillException> getExceptions() {
        return exceptions;
    }

    /**
     * setter method for exceptions
     * @param exceptions the exceptions to set
     */
    public void setExceptions(Collection<MergeWaybillException> exceptions) {
        this.exceptions = exceptions;
    }

    /**
     * getter method for receipts
     * @return the receipts
     */
    public Collection<MergeReceipt> getReceipts() {
        return receipts;
    }

    /**
     * setter method for receipts
     * @param receipts the receipts to set
     */
    public void setReceipts(Collection<MergeReceipt> receipts) {
        this.receipts = receipts;
    }

    /**
     * getter method for tracks
     * @return the tracks
     */
    public Collection<MergeTrack> getTracks() {
        return tracks;
    }

    /**
     * setter method for tracks
     * @param tracks the tracks to set
     */
    public void setTracks(Collection<MergeTrack> tracks) {
        this.tracks = tracks;
    }

    /**
     * getter method for goods
     * @return the goods
     */
    public Collection<Goods> getGoods() {
        return goods;
    }

    /**
     * setter method for goods
     * @param goods the goods to set
     */
    public void setGoods(Collection<Goods> goods) {
        this.goods = goods;
    }

    /**
     * getter method for arriveDay
     * @return the arriveDay
     */
    public Integer getArriveDay() {
        return arriveDay;
    }

    /**
     * setter method for arriveDay
     * @param arriveDay the arriveDay to set
     */
    public void setArriveDay(Integer arriveDay) {
        this.arriveDay = arriveDay;
    }

    /**
     * getter method for arriveHour
     * @return the arriveHour
     */
    public Integer getArriveHour() {
        return arriveHour;
    }

    /**
     * setter method for arriveHour
     * @param arriveHour the arriveHour to set
     */
    public void setArriveHour(Integer arriveHour) {
        this.arriveHour = arriveHour;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getCodeString() {
        return codeString;
    }

    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }

	public String getShipperCode() {
		return shipperCode;
	}

	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}

	public String getReceiverCode() {
		return receiverCode;
	}

	public void setReceiverCode(String receiverCode) {
		this.receiverCode = receiverCode;
	}
}
