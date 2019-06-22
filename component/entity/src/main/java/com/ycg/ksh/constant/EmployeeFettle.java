package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/9
 */
public enum EmployeeFettle {

    DISABLED(0, "禁用"), ENABLE(1, "正常");

    private int code;
    private String desc;

    EmployeeFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static EmployeeFettle convert(Integer code){
        if(code != null ){
            for (EmployeeFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return EmployeeFettle.ENABLE;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isEnable() {
        return ENABLE == this;
    }
    public boolean isDisabled() {
        return DISABLED == this;
    }
}
