package com.ycg.ksh.entity.common.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/25
 */
public enum AssignFettle {

    NOTYET(0),//还未指派
    WHOLE(1),//整包指派
    ROUTE(2);//路由指派

    private int code;

    private AssignFettle(int code) {
        this.code = code;
    }
    public final static AssignFettle convert(Integer code){

        if(code != null ){
            for (AssignFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return AssignFettle.NOTYET;
    }
    public int getCode() {
        return code;
    }

    public boolean notyet() {
        return NOTYET == this;
    }
    public boolean whole() {
        return WHOLE == this;
    }
    public boolean route() {
        return ROUTE == this;
    }
}
