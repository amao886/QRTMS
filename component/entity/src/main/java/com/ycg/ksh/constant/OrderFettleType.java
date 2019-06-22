package com.ycg.ksh.constant;

/**
 * 订单状态
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
public enum OrderFettleType {

    DEFAULT(0, "生成"), ING(1,"运输中"), ARRIVED(3,"到货"), COMPLETE(4,"完成"), DISUSE(99, "作废");

    private int code;
    private String desc;

    private OrderFettleType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static OrderFettleType convert(Integer code){
        if(code != null ){
            for (OrderFettleType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return OrderFettleType.DEFAULT;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public boolean isDefault() {
        return DEFAULT == this;
    }
    public boolean isIng(){return ING == this || isDefault();}
    public boolean isArrived(){return ARRIVED == this;}
    public boolean isComplete(){return COMPLETE == this;}
    public boolean isDisuse() {
        return DISUSE == this;
    }
}
