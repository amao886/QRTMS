package com.ycg.ksh.entity.common.constant;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/31
 */

/**
 * 任务单来源类型
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/31
 */
public enum SourceType {

    GROUP(0, "项目组"), SCAN(1, "扫码"), SHARE(2, "分享"), ASSIGN(3, "指派"), ONESELF(4, "个人");

    private int code;
    private String desc;

    private SourceType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public final static SourceType convert(Integer code){
        if(code != null ){
            for (SourceType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return SourceType.ONESELF;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean group() {
        return GROUP == this;
    }
    public boolean oneself() {
        return ONESELF == this;
    }
    public boolean scan() {
        return SCAN == this;
    }
    public boolean share() {
        return SHARE == this;
    }
    public boolean assign() {
        return ASSIGN == this;
    }

}
