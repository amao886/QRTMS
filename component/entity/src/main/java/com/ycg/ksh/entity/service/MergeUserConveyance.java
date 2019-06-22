package com.ycg.ksh.entity.service;

/**
 * 用户/运单组合类
 *
 * @author wangke
 * @create 2018-02-28 11:24
 **/
public class MergeUserConveyance extends AssociateUser {

    private static final long serialVersionUID = 2705248346056552576L;
    //运单ID
    private Long conveyanceId;

    public Long getConveyanceId() {
        return conveyanceId;
    }

    public void setConveyanceId(Long conveyanceId) {
        this.conveyanceId = conveyanceId;
    }
}
