package com.ycg.ksh.constant;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/19
 */
public enum ESignEventType implements OperateEventType {
    PERSONALVERIFY(11, "个人基本信息校验"), PERSONALCREATE(12, "个人用户创建"), PERSONALMODIFY(13, "个人用户更新"),
    ENTERPRISEBASE(21, "企业基本信息校验"), ENTERPRISEBRANK(22, "企业对公打款信息"), ENTERPRISECASH(23, "企业打款金额校验"), ENTERPRISECHECK(24, "查询打款状态"), ENTERPRISECREATE(25, "企业用户创建"),
    SEALPERSONAL(31, "创建个人印章"), SEALENTERPRIS(32, "创建企业印章"),
    BUILDCONTRACT(41, "创建合同"), ADDCONTRACTSIGNER(42, "添加签署者"), SIGNCONTRACT(43, "签署合同"), DOWNLOADCONTRACT(44, "下载合同文件");

    private int code;
    private String desc;

    private ESignEventType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public final static ESignEventType convert(Integer code){
        if(code != null ){
            for (ESignEventType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return ESignEventType.PERSONALVERIFY;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getDesc() {
        return null;
    }
}
