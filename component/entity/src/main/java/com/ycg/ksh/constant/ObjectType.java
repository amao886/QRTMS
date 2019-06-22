package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/12
 */
public enum ObjectType {

    USERLEGALIZE, //个人认证
    BANKVERIFY, //对公打款
    COMPANY, //企业认证
    ORDER, //订单数据
    RECEIPT, //回单
    COMPANYSEAL, //企业印章
    USERSEAL, //企业印章
    LOCATIONTRACK, //轨迹数据
    SYNCFETTLE; //同步状态

    public final static ObjectType convert(String code){
        if(code != null ){
            for (ObjectType fettle : values()) {
                if(fettle.name().equalsIgnoreCase(code)){
                    return fettle;
                }
            }
        }
        return ObjectType.ORDER;
    }
}
