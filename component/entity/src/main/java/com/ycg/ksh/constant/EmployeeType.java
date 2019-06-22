package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/9
 */
public enum EmployeeType {

    COMMON(0, "普通员工"), MANAGE(1, "管理");

    private int code;
    private String desc;

    private EmployeeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static EmployeeType convert(Integer code){
        if(code != null ){
            for (EmployeeType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return EmployeeType.COMMON;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isCommon() {
        return COMMON == this;
    }
    public boolean isManage() {
        return MANAGE == this;
    }
}
