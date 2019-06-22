package com.ycg.ksh.entity.adapter.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 企业认证信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/22
 */
public class Enterprise extends BaseEntity {

    enum Step{
        BASE, BRANK, CASH, CHECK, CREATE;
    }

    private String requestKey;//请求编号
    //基本信息
    private String companyName;//企业名称
    private String codeORG;//组织机构代码,与信用代码二者必填一个
    private String codeUSC;//社会统一信用代码,与组织机构代码二者必填一个
    private String legalName;//法人姓名
    private String legalIdNo;//法人身份证号码
    //对公打款信息
    private String accountName;//对公账户户名,一般来说即企业名称
    private String cardNo;//企业对公银行账号
    private String subBranch;//企业银行账号开户行支行全称
    private String bank;//企业银行账号开户行名称
    private String provice;//企业银行账号开户行所在省份
    private String city;//企业银行账号开户行所在城市
    private String prcptcd;//企业用户对公账户所在的开户行的大额行号
    //打款金额
    private String cash;//打款金额

    //创建企业用户
    private String userName;//企业名称（最长99字符）
    private String phoneNo;//企业类型用户的手机号仅支持中国大陆：首位为 1，长度11位纯数字

    //打款校验结果
    private String errorMsg;

    private Step step;

    public Enterprise() { }

    public static Enterprise base(String companyName, String codeORG, String codeUSC, String legalName, String legalIdNo){
        Enterprise enterprise = new Enterprise();
        enterprise.setCompanyName(companyName);
        enterprise.setCodeORG(codeORG);
        enterprise.setCodeUSC(codeUSC);
        enterprise.setLegalName(legalName);
        enterprise.setLegalIdNo(legalIdNo);
        enterprise.setStep(Step.BASE);
        return enterprise;
    }
    public static Enterprise brank(String accountName, String cardNo, String bank, String prcptcd, String subBranch, String provice, String city){
        Enterprise enterprise = new Enterprise();
        enterprise.setAccountName(accountName);
        enterprise.setCardNo(cardNo);
        enterprise.setBank(bank);
        enterprise.setPrcptcd(prcptcd);
        enterprise.setSubBranch(subBranch);
        enterprise.setProvice(provice);
        enterprise.setCity(city);
        enterprise.setStep(Step.BRANK);
        return enterprise;
    }
    public static Enterprise cash(Serializable requestKey, String cash){
        Enterprise enterprise = new Enterprise();
        enterprise.setRequestKey(String.valueOf(requestKey));
        enterprise.setCash(new DecimalFormat("#").format(Float.parseFloat(cash) * 100f));
        enterprise.setStep(Step.CASH);
        return enterprise;
    }
    public static Enterprise check(Serializable requestKey){
        Enterprise enterprise = new Enterprise();
        enterprise.setRequestKey(String.valueOf(requestKey));
        enterprise.setStep(Step.CHECK);
        return enterprise;
    }


    public static Enterprise create(String userName, String codeORG, String codeUSC, String phoneNo){
        Enterprise enterprise = new Enterprise();
        enterprise.setUserName(userName);
        enterprise.setCodeORG(codeORG);
        enterprise.setCodeUSC(codeUSC);
        enterprise.setPhoneNo(phoneNo);
        enterprise.setStep(Step.CREATE);
        return enterprise;
    }


    public boolean stepBase(){
        return Step.BASE == step;
    }
    public boolean stepBrank(){
        return Step.BRANK == step;
    }
    public boolean stepCash(){
        return Step.CASH == step;
    }
    public boolean stepCheck(){
        return Step.CHECK == step;
    }
    public boolean stepCreate(){
        return Step.CREATE == step;
    }

    @Override
    public String toString() {
        if(stepBase()){
            return "企业工商信息对比 -> 企业名称:"+ companyName +", 组织机构代码:"+ codeORG +",社会统一信用代码:"+ codeUSC +",法人姓名:"+ legalName+" ,法人身份证号码:"+ legalIdNo;
        }
        if(stepBrank()){
            return "打款请求 -> 账户名:"+ accountName +", 公银行账号:"+ cardNo +",银行名称:"+ bank +",支行全称:"+ subBranch +",大额行号:"+ prcptcd+" ,省市:"+ provice +"-"+ city;
        }
        if(stepCash()){
            return "打款金额比对 -> 订单编号:"+ requestKey +", 金额:"+ cash;
        }
        if(stepCheck()){
            return "打款状态检查 -> 订单编号:"+ requestKey;
        }
        if(stepCreate()){
            return "创建企业用户 -> 订单编号:"+ requestKey;
        }
        return super.toString();
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCodeORG() {
        return codeORG;
    }

    public void setCodeORG(String codeORG) {
        this.codeORG = codeORG;
    }

    public String getCodeUSC() {
        return codeUSC;
    }

    public void setCodeUSC(String codeUSC) {
        this.codeUSC = codeUSC;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalIdNo() {
        return legalIdNo;
    }

    public void setLegalIdNo(String legalIdNo) {
        this.legalIdNo = legalIdNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getSubBranch() {
        return subBranch;
    }

    public void setSubBranch(String subBranch) {
        this.subBranch = subBranch;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrcptcd() {
        return prcptcd;
    }

    public void setPrcptcd(String prcptcd) {
        this.prcptcd = prcptcd;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
