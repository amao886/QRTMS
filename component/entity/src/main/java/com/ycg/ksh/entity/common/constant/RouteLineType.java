package com.ycg.ksh.entity.common.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/25
 */
public enum RouteLineType {

    START(1),//起点
    TRANSIT(2),//中转
    END(3);//终点

    private int code;

    private RouteLineType(int code) {
        this.code = code;
    }
    public final static RouteLineType convert(Integer code){
        if(code != null ){
            for (RouteLineType routeLineType : values()) {
                if(routeLineType.code - code == 0){
                    return routeLineType;
                }
            }
        }
        return RouteLineType.START;
    }
    public int getCode() {
        return code;
    }

    public boolean start() {
        return START == this;
    }
    public boolean transit() {
        return TRANSIT == this;
    }
    public boolean end() {
        return END == this;
    }
}
