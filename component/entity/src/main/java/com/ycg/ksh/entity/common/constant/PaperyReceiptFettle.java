package com.ycg.ksh.entity.common.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/2/6
 */
public enum PaperyReceiptFettle {

    NOT_RECYCLED(0, "未回收"), RECYCLED(1, "已回收"), SEND(2, "已送客户"), SUPPLIER(3, "已退供应商"), RETURN(4, "客户退回");

    private int code;
    private String desc;

    private PaperyReceiptFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static PaperyReceiptFettle convert(Integer code){
        if(code != null ){
            for (PaperyReceiptFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return PaperyReceiptFettle.NOT_RECYCLED;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public boolean not_recycled() {
        return NOT_RECYCLED == this;
    }
    public boolean recycled() {
        return RECYCLED == this;
    }
    public boolean sendCustomer() {
        return SEND == this;
    }
    public boolean supplier() {
        return SUPPLIER == this;
    }
    public boolean customerReturn() {
        return RETURN == this;
    }

}
