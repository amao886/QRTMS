package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/29
 */
public enum SealStyle {
    F1("F1", "楷体"), F2("F2", "华文仿宋"), F3("F3", "华文楷体"), F4("F4", "微软雅黑");

    private String code;
    private String fontName;

    SealStyle(String code, String fontName) {
        this.code = code;
        this.fontName = fontName;
    }

    public final static SealStyle convert(String code){
        if(code != null ){
            for (SealStyle fettle : values()) {
                if(fettle.code.equals(code)){
                    return fettle;
                }
            }
        }
        return SealStyle.F1;
    }


    public String getCode() {
        return code;
    }

    public String getFontName() {
        return fontName;
    }
}
