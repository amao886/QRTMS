package com.ycg.ksh.constant;

/**
 * 资源类型
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/23
 */
public enum ResType {

    MONEY(1, "现金"), INTEGRAL(2, "积分");

    private int code;
    private String desc;

    private ResType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static ResType convert(Integer code){
        if(code != null ){
            for (ResType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return ResType.INTEGRAL;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isMoney() {
        return MONEY == this;
    }
    public boolean isIntegral() {
        return INTEGRAL == this;
    }
}
