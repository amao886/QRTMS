package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/26
 */
public enum ReceiptEventType implements OperateEventType {

    BUILD(1,"生成回单"),
    VALIDATE(2, "签署校验"),
    SIGN(3,"回单签署");

    private int code;
    private String desc;

    private ReceiptEventType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public final static ReceiptEventType convert(Integer code){
        if(code != null ){
            for (ReceiptEventType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return ReceiptEventType.BUILD;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isBuild() {
        return BUILD == this;
    }
    public boolean isValidate(){return VALIDATE == this;}
    public boolean isSign(){return SIGN == this;}
}
