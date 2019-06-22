package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
public enum TemplateDataType {

    STRING(1), NUMBER(2), DOUBLE(3), DATE(4);

    private int code;

    TemplateDataType(int code) {
        this.code = code;
    }

    public final static TemplateDataType convert(Integer code){
        if(code != null ){
            for (TemplateDataType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return TemplateDataType.STRING;
    }

    public boolean isString() {
        return STRING == this;
    }
    public boolean isNumber() {
        return NUMBER == this;
    }
    public boolean isDouble() { return DOUBLE == this; }
    public boolean isDate() { return DATE == this; }
}
