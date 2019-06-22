package com.ycg.ksh.constant;

/**
 * 自定义数据类型
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
public enum CustomDataType {

    ORDER(1, "订单"), PLAN(2, "计划");

    private int code;
    private String desc;

    private CustomDataType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static CustomDataType convert(Integer code){
        if(code != null ){
            for (CustomDataType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return CustomDataType.ORDER;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
