package com.ycg.ksh.constant;

/**
 * 菜单类型
 *
 * @author wangke
 * @create 2018-06-06 16:25
 **/
public enum MenuType {
    NORMAL(1, "企业版本权限"), GROUP(2, "项目版本权限"), BACKSTAGE(3, "后台管理权限");

    private int code;
    private String desc;

    private MenuType(int code, String desc) {
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
}
