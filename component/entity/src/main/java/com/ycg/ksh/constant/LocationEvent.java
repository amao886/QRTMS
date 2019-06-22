package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/30
 */
public enum LocationEvent {

    SCANCODE(1, "扫码上报"), DRIVERCONTAINER(2, "装车上报"), SYNC(9999, "异步处理");

    private int code;
    private String desc;

    private LocationEvent(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static LocationEvent convert(Integer code){
        if(code != null ){
            for (LocationEvent fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return LocationEvent.SCANCODE;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean scanCode() {
        return SCANCODE == this;
    }
    public boolean driverContainer() {
        return DRIVERCONTAINER == this;
    }
    public boolean sync() {
        return SYNC == this;
    }
}
