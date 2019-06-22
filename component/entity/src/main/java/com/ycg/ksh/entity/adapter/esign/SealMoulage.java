package com.ycg.ksh.entity.adapter.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/5
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 电子签收---印章
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/5
 */
public class SealMoulage extends BaseEntity {

    enum Step {
        PERSONAL, ENTERPRISE;
    }

    private String requestKey;//请求编号
    private String signerId;//用户id,

    //个人印章
    private String borderType;//边框样式：默认B1（B1：有边框，B2：无边 框）
    private String fontFamily;//字体样式：默认F1（F1：楷体，F2：华文仿宋， F3：华文楷体，F4：微软雅黑）

    //企业印章
    private String styleType;//章的形状：默认1(1：常规圆章，视觉效果最佳为 <=32字，2：椭圆章，视觉效果最佳为<=18字)
    private String textContent;//横向文文案，长度<= 12字符，可选参数，长 度建议控制在10个字以内，视觉效果最佳为7个字，避免使用英文字母。
    private String keyContent;//防伪码信息，长度13位纯数字，可选参数


    private Step step;


    public boolean stepPersonal() {
        return Step.PERSONAL == step;
    }

    public boolean stepEnterprise() {
        return Step.ENTERPRISE == step;
    }

    public SealMoulage() {
    }

    public static SealMoulage personal(String signerId, String borderType, String fontFamily) {
        SealMoulage sealMoulage = new SealMoulage();
        sealMoulage.setSignerId(signerId);
        sealMoulage.setBorderType(borderType);
        sealMoulage.setFontFamily(fontFamily);
        sealMoulage.setStep(Step.PERSONAL);
        return sealMoulage;
    }

    public static SealMoulage enterprise(String signerId, String styleType, String textContent, String keyContent) {
        SealMoulage sealMoulage = new SealMoulage();
        sealMoulage.setSignerId(signerId);
        sealMoulage.setStyleType(styleType.equalsIgnoreCase("F1") ? "1" : "2");
        sealMoulage.setTextContent(textContent);
        sealMoulage.setKeyContent(keyContent);
        sealMoulage.setStep(Step.ENTERPRISE);
        return sealMoulage;
    }

    @Override
    public String toString() {
        if (stepPersonal()) {
            return "创建个人印章 -> 编号:" + signerId + ", 边框样式:" + borderType + ",字体样式:" + fontFamily;
        }
        if (stepEnterprise()) {
            return "创建企业印章 -> 编号:" + signerId + ", 章的形状:" + styleType + ",横向文文案:" + textContent + ",防伪码信息:" + keyContent;
        }
        return super.toString();
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public String getBorderType() {
        return borderType;
    }

    public void setBorderType(String borderType) {
        this.borderType = borderType;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getKeyContent() {
        return keyContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }
}
