package com.ycg.ksh.entity.adapter.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/6
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 电子签收--合同签署者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/6
 */
public class ContractSigner extends BaseEntity {

    private String signerId;//签署者id
    private Integer signPositionType;//签署的定位方式：0 关键字定位，1 签 名占位符定位
    private String positionContent;//对应定位方式的内容，如果用签名占位 符定位可以传多个签名占位符，并以分号隔开,最多20个
    private Integer signValidateType;//签署验证方式：0 不校验，1 短信验证

    public ContractSigner() {
    }

    public ContractSigner(String signerId, String positionContent) {
        this.signerId = signerId;
        this.signPositionType = 0;
        this.positionContent = positionContent;
        this.signValidateType = 0;//签署验证方式：0 不校验，1 短信验证
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public Integer getSignPositionType() {
        return signPositionType;
    }

    public void setSignPositionType(Integer signPositionType) {
        this.signPositionType = signPositionType;
    }

    public String getPositionContent() {
        return positionContent;
    }

    public void setPositionContent(String positionContent) {
        this.positionContent = positionContent;
    }

    public Integer getSignValidateType() {
        return signValidateType;
    }

    public void setSignValidateType(Integer signValidateType) {
        this.signValidateType = signValidateType;
    }
}
