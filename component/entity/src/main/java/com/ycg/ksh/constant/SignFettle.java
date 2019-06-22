package com.ycg.ksh.constant;

public enum SignFettle {

    DEFAULT(0, "未签收"), INTACT(1, "正常签收"), ABNORMAL(2, "异常签收");

    private int code;
    private String desc;

    private SignFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static SignFettle convert(Integer code){
        if(code != null ){
            for (SignFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return SignFettle.DEFAULT;
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
    public boolean isIntact() {
        return INTACT == this;
    }
    public boolean isAbnormal() {
        return ABNORMAL == this;
    }

}
