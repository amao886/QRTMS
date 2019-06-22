package com.ycg.ksh.entity.common.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */
public enum HotspotType {

    FRIEND(1),//好友热点
    CUSTOMER(2),//常用地址热点
    ROUTE(3),//路由热点
    COMPANY_CUSTOMER(4);//企业客户

    private int code;

    private HotspotType(int code) {
        this.code = code;
    }
    public final static HotspotType convert(Integer code){
        if(code != null ){
            for (HotspotType hotspotType : values()) {
                if(hotspotType.code - code == 0){
                    return hotspotType;
                }
            }
        }
        return HotspotType.FRIEND;
    }
    public int getCode() {
        return code;
    }
}
