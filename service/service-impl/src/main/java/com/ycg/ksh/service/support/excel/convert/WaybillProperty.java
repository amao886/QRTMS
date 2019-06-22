/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 12:39:56
 */
package com.ycg.ksh.service.support.excel.convert;

import com.ycg.ksh.common.validate.Validator;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 12:39:56
 */
public enum WaybillProperty {


    SHIPPERNAME("shipperName", "货主名称", true, new Validator[] { Validator.NOTBLANK }),//货主名称
    SHIPPERADDRESS("shipperAddress", "发货地址", true, new Validator[] { Validator.NOTBLANK }),//发货地址
    SHIPPERCONTACTNAME("shipperContactName", "发货联系人", true, new Validator[] { Validator.NOTBLANK }),//发货联系人
    SHIPPERTEL("shipperContactTel", "发货联系电话", true, new Validator[] { Validator.NOTBLANK, Validator.MOBILE }),//发货联系电话
    RECEIVERNAME("receiverName", "收货方", true, new Validator[] { Validator.NOTBLANK }),//收货客户
    RECEIVEADDRESS("receiveAddress", "收货地址", true, new Validator[] { Validator.NOTBLANK }),//收货地址
    CONTACTNAME("contactName", "联系人", true, new Validator[] { Validator.NOTBLANK }),//联系人
    CONTACTPHONE("contactPhone", "联系电话", true, new Validator[] { Validator.NOTBLANK, Validator.MOBILE }),//联系电话
    ARRIVEDAY("arriveDay", "发货后天数", new Validator[] {Validator.NUMBER}),//发货后天数
    ARRIVEHOUR("arriveHour", "时间点", new Validator[] {Validator.NUMBER}),//时间点
    ORDERSUMMARY("orderSummary", "订单摘要"),//订单摘要

    
    GOODSTYPE("goodsType", "客户料号"),//客户料号
    GOODSNAME("goodsName", "物料名称"),//物料名称
    GOODSWEIGHT("goodsWeight", "重量", new Validator[] {Validator.NUMBER}),//重量（kg）
    GOODSVOLUME("goodsVolume", "体积", new Validator[] {Validator.NUMBER}),//体积（m³）
    GOODSQUANTITY("goodsQuantity", "数量", new Validator[] {Validator.NUMBER}),//数量（件）
    SUMMARY("summary", "订单摘要");//订单摘要
    
    
    private String property;
    private String showName;
    private boolean uniqueKey;
    private Validator[] validators;
    
    private WaybillProperty(String property, String showName, boolean uniqueKey, Validator[] validators) {
        this.property = property;
        this.showName = showName;
        this.uniqueKey = uniqueKey;
        this.validators = validators;
    }
    
    private WaybillProperty(String property, String showName, Validator[] validators) {
        this(property, showName, false, validators);
    }

    private WaybillProperty(String property, String showName) {
        this(property, showName, null);
    }


    /**
     * getter method for property
     * @return the property
     */
    public String getProperty() {
        return property;
    }
    /**
     * getter method for showName
     * @return the showName
     */
    public String getShowName() {
        return showName;
    }

    /**
     * getter method for uniqueKey
     * @return the uniqueKey
     */
    public boolean isUniqueKey() {
        return uniqueKey;
    }
    /**
     * getter method for validators
     * @return the validators
     */
    public Validator[] getValidators() {
        return validators;
    }
}
