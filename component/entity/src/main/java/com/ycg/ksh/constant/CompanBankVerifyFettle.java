package com.ycg.ksh.constant;

/**
 * 打款枚举
 *
 * @author wangke
 * @create 2018-07-17 13:23
 **/
public enum CompanBankVerifyFettle {
    PAYING(1, "打款中"), VFAILED(2, "校验未通过"), VPASS(3, "校验通过"),;
    private int code;
    private String desc;

    CompanBankVerifyFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static boolean isVerify(Integer fettle) {
        if (fettle != null && fettle > 0) {
            if (fettle - PAYING.code == 0) {
                return true;
            } else if (fettle - VFAILED.code == 0) {
                return true;
            }
        }
        return false;
    }
}
