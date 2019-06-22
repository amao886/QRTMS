package com.ycg.ksh.entity.common.constant;

/**
 * 待办事项状态
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/2
 */
public enum TodoFettle {
    //0：未读、1：已读、2:转催、3：已处理、4：已完成
    NOT_READ(0, "未读"), READ(1, "已读"), URGE(2, "转催"), HANDLE(3, "已处理"), COMPLETE(4, "已完成");

    private int code;
    private String desc;

    private TodoFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static TodoFettle convert(Integer code){
        if(code != null ){
            for (TodoFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return TodoFettle.NOT_READ;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public boolean notRead() {
        return NOT_READ == this;
    }
    public boolean read() {
        return READ == this;
    }
    public boolean urge() {
        return URGE == this;
    }
    public boolean handle() {
        return HANDLE == this;
    }
    public boolean complete() {
        return COMPLETE == this;
    }
}
