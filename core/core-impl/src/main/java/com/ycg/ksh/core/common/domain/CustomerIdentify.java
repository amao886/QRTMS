package com.ycg.ksh.core.common.domain;

import com.ycg.ksh.entity.common.constant.PartnerType;

/**
 * 客户标识
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/14 0014
 */
public class CustomerIdentify extends Model {

    private PartnerType type;
    private Long identify;
    private Integer regType;

    public CustomerIdentify() {
    }

    public CustomerIdentify(PartnerType type, Long identify, Integer regType) {
        this.type = type;
        this.identify = identify;
        this.regType = regType;
    }

    public boolean reg(){
        return type.registered(regType);
    }

    public PartnerType getType() {
        return type;
    }

    public Long getIdentify() {
        return identify;
    }

    public Integer getRegType() {
        return regType;
    }
}
