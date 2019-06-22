package com.ycg.ksh.entity.common.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/8
 */
public enum UserType {
    //0普通用户, 1超级管理员, 2管理员
    COMMON(0, "普通用户"), MANAGER(2, "管理员"), SUPER(1, "超级管理员");

    private int code;
    private String desc;

    private UserType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static UserType convert(Integer code){
        if(code != null ){
            for (UserType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return UserType.COMMON;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean common() {
        return COMMON == this;
    }
    public boolean manager() {
        return MANAGER == this;
    }
    public boolean superManager() { return SUPER == this; }
}
