package com.ycg.ksh.entity.service.moutai;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.moutai.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderPrint extends BaseEntity{
    //標識
    private String key;
    private String detailaddress;   //详细地址
    private String contact;         //联系方式
    private String depot;           //仓库
    private String username;        //当前登录用户
    private String year;            //当前年份
    private String month;           //当前月份
    private String day;             //当前月份下的第几天
    private String fax;          //固話
    private String conveyName;   //承运商的名称
    private String takeIdcare;   //取货人身份证号码
    private String takeName;   //取货人姓名
    private List<Order> list;
    private String customerName;

    private static Order EMPTY = new Order();

    public OrderPrint() {
        EMPTY = new Order();
        EMPTY.setScheduleNo("");
        EMPTY.setCustomerNo("");
        EMPTY.setSpecification("");
    }

    public OrderPrint(String key) {
        this();
        this.key = key;
        list = new ArrayList<Order>();
    }

    public void setDate(LocalDate date){
        year = "" + date.getYear();
        int month = date.getMonth().getValue();
        this.month = month < 10 ? "0"+ month : "" + month;
        int day = date.getDayOfMonth();
        this.day = day < 10 ? "0"+ day : "" + day;
    }

    public void setDetailaddress(String province, String city, String address){
        StringBuilder detailaddr = new StringBuilder("");
        if (StringUtils.isNotEmpty(province) && ! province.equals("null"))
            detailaddr.append(province);
        if (StringUtils.isNotEmpty(city) && ! city.equals("null"))
            detailaddr.append("-").append(city);
        if (StringUtils.isNotEmpty(address) && ! address.equals("null"))
            detailaddr.append("-").append(address);
        if(detailaddr.toString().startsWith("-"))
            detailaddr.substring(1, address.length());
        detailaddress = detailaddr.toString();
    }
    public void setContact(String contactName, String contactTel, String fax){
        StringBuilder detailcontact = new StringBuilder("");
        detailcontact.append(contactName).append("   ");
        if(fax != null)
            detailcontact.append(fax).append("   ");
        detailcontact.append(contactTel);
        contact = detailcontact.toString();
        setFax(fax);
    }

    public void add(Order order){
        list.add(order);
    }


    public OrderPrint copy(){
        OrderPrint p = new OrderPrint(key);
        p.setCustomerName(customerName);
        p.setTakeIdcare(takeIdcare);
        p.setTakeName(takeName);
        p.setConveyName(conveyName);
        p.setFax(fax);

        p.setKey(key);
        p.setDetailaddress(detailaddress);
        p.setContact(contact);
        p.setDepot(depot);
        p.setUsername(username);
        p.setYear(year);
        p.setMonth(month);
        p.setDay(day);
        return p;
    }

    public void fill(){
        if(list != null && list.size() < 8){
            Order[] orders = new Order[8 - list.size()];
            Arrays.fill(orders, EMPTY);
            Collections.addAll(list, orders);
        }
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public String getTakeIdcare() {
        return takeIdcare;
    }

    public void setTakeIdcare(String takeIdcare) {
        this.takeIdcare = takeIdcare;
    }

    public String getTakeName() {
        return takeName;
    }

    public void setTakeName(String takeName) {
        this.takeName = takeName;
    }

    public String getConveyName() {
        return conveyName;
    }

    public void setConveyName(String conveyName) {
        this.conveyName = conveyName;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDetailaddress() {
        return detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        this.detailaddress = detailaddress;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
