package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
public enum FinanceType {

    ORDER(1, "订单"), WALLET(2, "用户钱包"), PLAN(3, "发货计划");

    private int code;
    private String desc;

    private FinanceType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static FinanceType convert(Integer code){
        if(code != null ){
            for (FinanceType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return FinanceType.ORDER;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
