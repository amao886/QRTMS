package com.ycg.ksh.constant;

/**
 *  企业电子签署状态
 */
public enum CompanySignFettle {

    //0:未开通,1:已开通
    NOTOPEN(0, "未开通"), OPEN(1, "已开通");

    private int code;
    private String desc;

    CompanySignFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public final static CompanySignFettle convert(Integer code){
        if(code != null ){
            for (CompanySignFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return CompanySignFettle.NOTOPEN;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isNotOpen() {
        return NOTOPEN == this;
    }
    public boolean isOpen() {
        return OPEN == this;
    }
}
