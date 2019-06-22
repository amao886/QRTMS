package com.ycg.ksh.entity.common.constant;

/**
 * 条码版本
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/24
 */
public enum BarCodeVersion {

    GROUP(1),//项目组版本
    ENTERPRISE(2);//企业版本

    private int code;

    BarCodeVersion(int code) {
        this.code = code;
    }

    public final static BarCodeVersion convert(Integer code){
        if(code != null ){
            for (BarCodeVersion fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return BarCodeVersion.GROUP;
    }

    public int getCode() {
        return code;
    }


    public boolean group() {
        return GROUP == this;
    }
    public boolean enterprise() {
        return ENTERPRISE == this;
    }

}
