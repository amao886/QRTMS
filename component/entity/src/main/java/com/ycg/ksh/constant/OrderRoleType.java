package com.ycg.ksh.constant;

/**
 * 签章角色
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
public enum OrderRoleType {

    //发货方, 收货方, 承运方
    SHIPPER(1, "发货方", "发货单位盖章", "发货人签字"), CONVEY(2, "承运方", "物流商盖章", "物流商签字"), RECEIVE(3, "收货方", "收货单位业务章", "收货人签字");

    private int code;
    private String desc;
    private String enterprisePosition;
    private String personalPosition;

    private OrderRoleType(int code, String desc, String enterprise, String personal) {
        this.code = code;
        this.desc = desc;
        this.enterprisePosition = enterprise;
        this.personalPosition = personal;
    }
    public static OrderRoleType convert(Integer code){
        if(code != null ){
            for (OrderRoleType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return OrderRoleType.SHIPPER;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getEnterprisePosition() {
        return enterprisePosition;
    }

    public String getPersonalPosition() {
        return personalPosition;
    }

    public boolean isShipper() {
        return SHIPPER == this;
    }
    public boolean isReceive() {
        return RECEIVE == this;
    }
    public boolean isConvey() { return CONVEY == this; }
}
