package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/2
 */
public enum SysRoleType {

    GROUP(2, 1, 2, "项目组角色"), MOUTAI(3, 101, 4, "茅台角色"), ENTERPRISE(4, 102, 1, "企业角色"), SUPER(1, 9999, 0, "超级管理员");

    private int key;
    private int code;
    private int type;//0:所有菜单;1:企业版本权限;2:项目版本权限;3:后台管理权限;4:茅台权限
    private String desc;

    private SysRoleType(int key, int code, int type, String desc) {
        this.key = key;
        this.code = code;
        this.type = type;
        this.desc = desc;
    }
    public final static SysRoleType convert(Integer code){
        if(code != null ){
            for (SysRoleType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return SysRoleType.ENTERPRISE;
    }

    public boolean isGroup() {
        return GROUP == this;
    }
    public boolean isMoutai() {
        return MOUTAI == this;
    }
    public boolean isEnterprise() { return ENTERPRISE == this; }
    public boolean isSuper() { return SUPER == this; }

    public int getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
