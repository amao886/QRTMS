package com.ycg.ksh.entity.service.barcode;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/28
 */

import com.ycg.ksh.constant.BarCodeFettle;
import com.ycg.ksh.constant.OrderFettleType;
import com.ycg.ksh.constant.OrderRoleType;

/**
 * 企业二维码上线文
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/28
 */
public class CompanyCodeContext extends BarcodeContext {

    private Long companyKey;//企业编号
    private Long orderKey;//已绑定的订单编号
    private OrderFettleType fettleType;//状态
    private OrderRoleType roleType;



    public CompanyCodeContext() {
    }

    public CompanyCodeContext(Long companyKey, OrderFettleType fettleType) {
        this.companyKey = companyKey;
        this.fettleType = fettleType;
    }

    @Override
    public BarCodeFettle fettle() {
        if(super.fettle().unbind()){
            if(orderKey != null && orderKey > 0){
                return BarCodeFettle.BIND;
            }
        }
        return super.fettle();
    }


    @Override
    public boolean isComplete() {
        return fettleType.isComplete() || fettleType.isDisuse();
    }

    @Override
    public boolean isCompany() {
        return true;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public Long getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(Long orderKey) {
        this.orderKey = orderKey;
    }

    public OrderFettleType getFettleType() {
        return fettleType;
    }

    public void setFettleType(OrderFettleType fettleType) {
        this.fettleType = fettleType;
    }

    public OrderRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(OrderRoleType roleType) {
        this.roleType = roleType;
    }
}
