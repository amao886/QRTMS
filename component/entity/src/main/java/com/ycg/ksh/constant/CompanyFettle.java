package com.ycg.ksh.constant;

/**
 * 企业状态
 */
public enum CompanyFettle {
    //1:注册未认证,2:工商信息对比通过,3:对公打开中,4:已认证,5:认证失败,99:禁用
    REGISTERED(1, "注册未认证"), BASE(2, "工商信息对比通过"), BRANK(3, "对公打开中"), VALIDATE(4, "已认证"), FAILE(5, "认证失败"), DIABLED(99, "禁用");
    private int code;
    private String desc;

    CompanyFettle(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public final static CompanyFettle convert(Integer code){
        if(code != null ){
            for (CompanyFettle fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return CompanyFettle.REGISTERED;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isRegistered() {
        return REGISTERED == this;
    }
    public boolean isBase() {
        return BASE == this;
    }
    public boolean isBrank() {
        return BRANK == this;
    }
    public boolean isValidate() {
        return VALIDATE == this;
    }
    public boolean isFaile() {
        return FAILE == this;
    }
    public boolean isDiabled() {
        return DIABLED == this;
    }
}
