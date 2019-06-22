package com.ycg.ksh.entity.adapter.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */

/**
 * 电子签收供应商
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */
public enum Signer{

    YUNHETONG("yunhetong", "云合同");

    private String code;//代码
    private String name;//名称

    private Signer(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
