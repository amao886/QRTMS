package com.ycg.ksh.constant;

/**
 * 回单状态
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
public enum ReceiptFettleType {

    DEFAULT(0, "未生成电子回单"), WAIT(1, "已生成电子回单"), SSIGN(2, "收货方已签"), CSIGN(3, "承运商已签"), YSIGN(4, "双发都已签");

    private int code;
    private String desc;

    private ReceiptFettleType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static ReceiptFettleType convert(Integer code){
        if(code != null ){
            for (ReceiptFettleType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return ReceiptFettleType.DEFAULT;
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
    public boolean isWait() {
        return WAIT == this;
    }
    public boolean isSsign() {
        return SSIGN == this;
    }
    public boolean isCsign() {
        return CSIGN == this;
    }
    public boolean isYsign() {
        return YSIGN == this;
    }
}
