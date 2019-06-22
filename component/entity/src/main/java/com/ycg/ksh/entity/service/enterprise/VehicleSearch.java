package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 车辆查询整合
 *
 * @author: wangke
 * @create: 2018-10-21 09:28
 **/

public class VehicleSearch extends BaseEntity {

    private Long likeString; //要车单号

    private Long lastCompanyKey;//上级企业

    private Long conveyId; //承运商

    private Long companKey;

    public Long getCompanKey() {
        return companKey;
    }

    public void setCompanKey(Long companKey) {
        this.companKey = companKey;
    }

    public Long getLikeString() {
        return likeString;
    }

    public void setLikeString(Long likeString) {
        this.likeString = likeString;
    }

    public Long getLastCompanyKey() {
        return lastCompanyKey;
    }

    public void setLastCompanyKey(Long lastCompanyKey) {
        this.lastCompanyKey = lastCompanyKey;
    }

    public Long getConveyId() {
        return conveyId;
    }

    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }
}
