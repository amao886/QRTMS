package com.ycg.ksh.constant;

/**
 * 订单状态
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
public enum OrderEventType implements OperateEventType {

    CREATE(1, "录入发货单", true),
    CREATE_IMPORT(12, "导入发货单", true),
    CREATE_GENERATE(13, "生成发货单", true),
    CREATE_SYNC(14, "同步发货单", true),
    BINDCODE(2,"扫码绑单", true),
    LOCATE(3, "上报位置", true),
    UPLOADRECEIPT(4, "上传回单", true),
    ERECEIPT(5, "生成电子回单", true),
    SIGNCODE(6,"签署校验码", true),
    SIGN(7,"货物送达，签收成功", true),
    ARRIVED(8, "到货", true),
    RECEIVE(9, "已签收", true),
    UPDATE_PARTNER(96, "三方更新", false),
    LOAD_DRIVER(97, "装车完成", true),
    DELETE(98, "删除", false),
    DISUSE(99, "作废", false);

    private int code;
    private String desc;
    private boolean note;

    private OrderEventType(int code, String desc, boolean note) {
        this.code = code;
        this.desc = desc;
        this.note = note;
    }

    public final static OrderEventType convert(Integer code){
        if(code != null ){
            for (OrderEventType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return OrderEventType.CREATE;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isNote() {
        return note;
    }

    public void setNote(boolean note) {
        this.note = note;
    }

    public boolean log(){
        return this != UPDATE_PARTNER;
    }

    public boolean isShow(){
        return this == CREATE || this == BINDCODE || this == LOCATE || this == UPLOADRECEIPT || this == RECEIVE || this == LOAD_DRIVER;
    }

    public boolean isCreate() {
        return CREATE == this;
    }
    public boolean isBindCode(){return BINDCODE == this;}
    public boolean isLocate(){return LOCATE == this;}
    public boolean isUploadReceipt(){return UPLOADRECEIPT == this;}
    public boolean isEreceipt() {
        return ERECEIPT == this;
    }
    public boolean isSign() {
        return SIGN == this;
    }
    public boolean isReceive() {
        return RECEIVE == this;
    }
    public boolean isDisuse() {
        return DISUSE == this;
    }
}
