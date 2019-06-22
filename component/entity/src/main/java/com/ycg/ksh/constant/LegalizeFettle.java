package com.ycg.ksh.constant;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/12
 */

/**
 * 实名认证状态
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/12
 */
public enum LegalizeFettle {

    NOTVERIFY(0), VERIFY(1), CREATE(2);

    private int code;

    private LegalizeFettle(int code) {
        this.code = code;
    }
    public final static LegalizeFettle convert(Integer code){
        if(code != null ){
            for (LegalizeFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return LegalizeFettle.NOTVERIFY;
    }
    public int getCode() {
        return code;
    }

    public boolean isNotVerify() {
        return NOTVERIFY == this;
    }
    public boolean isVerify() {
        return VERIFY == this;
    }
    public boolean isCreate() {
        return CREATE == this;
    }
}
