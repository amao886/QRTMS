package com.ycg.ksh.entity.adapter.wechat;

import com.ycg.ksh.common.util.StringUtils;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/8
 */
public enum TemplateType {

    WHSIGN("whsign", "订单完好签收通知"), YCSIGN("ycsign", "订单异常签收通知"), LEGALIZE("legalize", "个人用户认证通知"),
    REGISTER("register", "注册成功通知"), LOGIN("login", "登录操作通知"), BINDMOBILE("bindmobile", "绑定手机号通知"), DELIVERY("delivery", "发货通知");

    private String key;
    private String desc;

    TemplateType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static TemplateType convert(String key){
        if(key != null ){
            for (TemplateType type : values()) {
                if(StringUtils.equalsIgnoreCase(type.key, key)){
                    return type;
                }
            }
        }
        return null;
    }

    public String key(String pkey){
        return pkey +"."+ key;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
