package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/14
 */
public enum BarCodeFettle {

    UNBIND(10, "未绑单"), BIND(20, "已绑单");

    private int code;
    private String desc;

    private BarCodeFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static BarCodeFettle convert(Integer code){
        if(code != null ){
            for (BarCodeFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
            //兼容老的二维码状态
            if(code >= 20){
                return BarCodeFettle.BIND;
            }
        }
        return BarCodeFettle.UNBIND;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean unbind() {
        return UNBIND == this;
    }
    public boolean bind() {
        return BIND == this;
    }
}
