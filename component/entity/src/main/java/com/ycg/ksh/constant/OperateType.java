package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/28
 */
public enum OperateType {

    ORDER(1, "订单操作"), ESIGN(2, "电子签收操作");

    private int code;
    private String desc;

    private OperateType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static OperateType convert(Integer code){
        if(code != null ){
            for (OperateType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return OperateType.ORDER;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
