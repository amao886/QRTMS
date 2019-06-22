package com.ycg.ksh.entity.common.constant;

/**
 * 回单审核状态
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/2/6
 */
public enum ReceiptVerifyFettle {

    UN_UPLOAD(1, "未上传"), WAIT(2, "待审核"), ING(3, "审核中"),ALREADY(4, "已审核");

    private int code;
    private String desc;

    private ReceiptVerifyFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static ReceiptVerifyFettle convert(Integer code){
        if(code != null ){
            for (ReceiptVerifyFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return ReceiptVerifyFettle.UN_UPLOAD;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean un_upload() {
        return UN_UPLOAD == this;
    }
    public boolean waitVerify() {
        return WAIT == this;
    }
    public boolean ingVerify() {
        return ING == this;
    }
    public boolean already() {
        return ALREADY == this;
    }

}
