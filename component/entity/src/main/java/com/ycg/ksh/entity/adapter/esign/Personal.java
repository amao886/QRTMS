package com.ycg.ksh.entity.adapter.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.io.Serializable;

/**
 * 个人用户认证信息实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */
public class Personal extends BaseEntity {

    enum Step{
        VERIFY, CREATE, MODIFY;
    }

    private String name;//姓名
    private String idNo;//身份证号码
    private String brankNo;//银行账号
    private String mobile;//银行预留手机号

    private String requestKey;//请求编号
    private String smsCode;//短信验证码

    private Step step;

    public Personal() {
    }

    public Personal(Step step) {
        this.step = step;
    }

    public static Personal verify(String name, String idNo,String brankNo, String mobile){
        Personal personal = new Personal(Step.VERIFY);
        personal.setName(name);
        personal.setIdNo(idNo);
        personal.setBrankNo(brankNo);
        personal.setMobile(mobile);
        return personal;
    }
    public static Personal create(String name, String idNo, String mobile){
        Personal personal = new Personal(Step.CREATE);
        personal.setName(name);
        personal.setIdNo(idNo);
        personal.setMobile(mobile);
        return personal;
    }
    public static Personal modify(Serializable requestKey, String name, String mobile){
        Personal personal = new Personal(Step.MODIFY);
        personal.setRequestKey(String.valueOf(requestKey));
        personal.setName(name);
        personal.setMobile(mobile);
        return personal;
    }

    public boolean stepVerify(){
        return Step.VERIFY == step;
    }
    public boolean stepCreate(){
        return Step.CREATE == step;
    }
    public boolean stepModify(){
        return Step.MODIFY == step;
    }

    @Override
    public String toString() {
        if(stepVerify()){
            return "实名认证 -> 姓名:"+ name +", 身份证号码:"+ idNo +",银行账号:"+ brankNo +",预留手机号:"+ mobile;
        }
        if(stepCreate()){
            return "创建用户 -> 姓名:"+ name +", 身份证号码:"+ idNo +",手机号:"+ mobile;
        }
        if(stepModify()){
            return "更新用户 -> 编号:"+ requestKey +", 姓名:"+ name +",手机号:"+ mobile;
        }
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBrankNo() {
        return brankNo;
    }

    public void setBrankNo(String brankNo) {
        this.brankNo = brankNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
