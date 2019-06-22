package com.ycg.ksh.entity.service.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/26
 */

import com.ycg.ksh.constant.OrderRoleType;
import com.ycg.ksh.entity.persistent.*;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 回单签署信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/26
 */
public class ReceiptSignature extends OrderSignature {


    private OrderRoleType roleType;
    private Order order;
    private UserLegalize legalize;
    private CompanyEmployee employee;
    private Company company;
    private CompanySeal companySeal;
    private PersonalSeal userSeal;

    private OrderReceipt receipt;

    public ReceiptSignature() {
    }

    public ReceiptSignature(OrderSignature signature) throws Exception {
        BeanUtils.copyProperties(this, signature);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public UserLegalize getLegalize() {
        return legalize;
    }

    public void setLegalize(UserLegalize legalize) {
        this.legalize = legalize;
    }

    public CompanyEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(CompanyEmployee employee) {
        this.employee = employee;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public OrderRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(OrderRoleType roleType) {
        this.roleType = roleType;
        if(roleType != null){
            setSignRole(roleType.getCode());
        }
    }

    public CompanySeal getCompanySeal() {
        return companySeal;
    }

    public void setCompanySeal(CompanySeal companySeal) {
        this.companySeal = companySeal;
    }

    public OrderReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(OrderReceipt receipt) {
        this.receipt = receipt;
    }

    public PersonalSeal getUserSeal() {
        return userSeal;
    }

    public void setUserSeal(PersonalSeal userSeal) {
        this.userSeal = userSeal;
    }
}
