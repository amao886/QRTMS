package com.ycg.ksh.constant;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/27 0027
 */

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/27 0027
 */
public enum CompanyConfigType {
    SEND_NOTICE("send_notice", "0", "发货通知"),TRACKING("tracking_setting","0","跟踪设置");
    private String code;
    private String defaultValue;
    private String desc;

    CompanyConfigType(String code, String defaultValue, String desc) {
        this.code = code;
        this.defaultValue = defaultValue;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
