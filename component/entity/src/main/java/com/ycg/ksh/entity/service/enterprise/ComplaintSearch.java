package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.common.constant.PartnerType;

import java.util.Date;

/**
 * 投诉查询
 *
 * @author: wangke
 * @create: 2018-10-30 15:57
 **/

public class ComplaintSearch extends BaseEntity {

    private String likeString; //送货单号/发货方/收货方/投诉人

    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间

    private Long shipperId; //发货方ID
    private Long receiveId;//收货方ID
    private Long conveyId;//承运方

    private Integer uesrKey;//用户ID

    private PartnerType partnerType; //用户身份

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public Integer getUesrKey() {
        return uesrKey;
    }

    public void setUesrKey(Integer uesrKey) {
        this.uesrKey = uesrKey;
    }

    public Long getConveyId() {
        return conveyId;
    }

    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }

    public Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(Date secondTime) {
        this.secondTime = secondTime;
    }
}
