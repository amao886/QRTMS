package com.ycg.ksh.api;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/23
 */

/**
 * 权限
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/23
 */
public enum Authoritys {
    ;

    private String code;
    private String name;

    Authoritys(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public final static Authoritys convert(String code){
        if(code != null ){
            for (Authoritys fettle : values()) {
                if(fettle.code.equalsIgnoreCase(code)){
                    return fettle;
                }
            }
        }
        return null;
    }
}
