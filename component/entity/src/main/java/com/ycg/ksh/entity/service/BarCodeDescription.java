package com.ycg.ksh.entity.service;

import com.ycg.ksh.constant.BarCodeFettle;
import com.ycg.ksh.entity.persistent.Barcode;

/**
 * @Description: 条码的详细信息
 * @author
 * @date 2018/7/23 14:35
 */

public class BarCodeDescription extends Barcode {
    /* 条码状态 */
    private BarCodeFettle barCodeFettle;

    private String groupName;

    private String companyName;


    public BarCodeFettle getBarCodeFettle() {
        return barCodeFettle;
    }

    public void setBarCodeFettle(BarCodeFettle barCodeFettle) {
        this.barCodeFettle = barCodeFettle;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
