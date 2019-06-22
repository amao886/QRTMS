package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/23
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 运单查询
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/23
 */
public class ConveyanceSearch extends BaseEntity {


    private String barcode;
    private String conveyanceKey;
    private Long parentKey;//父运单ID
    private String likeString;// 模糊查询字符串(任务单号、运单号、送货单号)
    private String shipperName;// 发货方名称模糊查询字符串
    private String transporter;// 承运人名称模糊查询字符串
    private String startStation;//发展
    private String endStation;//到站
    private Integer createKey;// 用户
    private Integer groupKey;// 组
    private Date firstTime;// 开始时间
    private Date secondTime;// 结束时间
    private Integer assignFettle;//运单指派状态
    private Integer fettle;//运单状态
    private Boolean haveChild;//是否有子运单
    private Integer receipt;//回单状态
    private Integer locationCount;//上报位置次数
    private Date requirements;//到货要求
    private Boolean cancel = true;//是否要查询取消的运单


    public void setTime(Date date, int d) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        if(d <= 0){
            setSecondTime(DateUtils.maxOfDay(cal));
            if (d != 0) {
                cal.add(Calendar.DAY_OF_YEAR, d);
            }
            setFirstTime(DateUtils.minOfDay(cal));
        }else{
            setFirstTime(DateUtils.minOfDay(cal));
            if (d != 0) {
                cal.add(Calendar.DAY_OF_YEAR, d);
            }
            setSecondTime(DateUtils.maxOfDay(cal));
        }
    }

    public void setRequirementsTime(Date date, int d) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, d);// 今天+1天
        Date tomorrow = c.getTime();
        setRequirements(DateUtils.minOfDay(tomorrow));
    }

    public void setTime(Date firstTime, Date secondTime, int d) {
        Calendar cal = Calendar.getInstance();
        if (secondTime != null) {
            cal.setTime(secondTime);
        }
        setSecondTime(DateUtils.maxOfDay(cal));
        if (firstTime != null) {
            cal.setTime(firstTime);
        } else if (d != 0) {
            cal.add(Calendar.DAY_OF_YEAR, d);
        }
        setFirstTime(DateUtils.minOfDay(cal));
    }
    public ConveyanceSearch() {
        super();
    }
    public ConveyanceSearch(Long parentKey, Boolean cancel) {
        this.parentKey = parentKey;
        this.cancel = cancel;
    }

    public Long getParentKey() {
        return parentKey;
    }

    public void setParentKey(Long parentKey) {
        this.parentKey = parentKey;
    }

    public Integer getReceipt() {
        return receipt;
    }

    public void setReceipt(Integer receipt) {
        this.receipt = receipt;
    }

    public Integer getLocationCount() {
        return locationCount;
    }

    public void setLocationCount(Integer locationCount) {
        this.locationCount = locationCount;
    }

    public Date getRequirements() {
        return requirements;
    }

    public void setRequirements(Date requirements) {
        this.requirements = requirements;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Integer getCreateKey() {
        return createKey;
    }

    public void setCreateKey(Integer createKey) {
        this.createKey = createKey;
    }

    public Integer getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(Integer groupKey) {
        this.groupKey = groupKey;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(Date secondTime) {
        this.secondTime = secondTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public Integer getAssignFettle() {
        return assignFettle;
    }

    public void setAssignFettle(Integer assignFettle) {
        this.assignFettle = assignFettle;
    }

    public Boolean getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getConveyanceKey() {
        return conveyanceKey;
    }

    public void setConveyanceKey(String conveyanceKey) {
        this.conveyanceKey = conveyanceKey;
    }
}
