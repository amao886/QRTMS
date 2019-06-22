package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/30
 */
public enum LocationType {

    ORDER(1, "订单轨迹");

    private int code;
    private String desc;

    private LocationType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static LocationType convert(Integer code){
        if(code != null ){
            for (LocationType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return LocationType.ORDER;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isOrder() {
        return ORDER == this;
    }
}
