package com.ycg.ksh.core.scene.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;


/**
 * 车辆到达信息传输对象
 *
 * @author: wangke
 * @create: 2018-12-11 14:23
 **/

public class ArrivalsCarDto extends BaseEntity {

    private String inDriverName; //司机名称

    private String inDriverContact; //司机电话

    private String inLicense; //车牌号

    private Integer arrivalType;//到达类型

    public ArrivalsCarDto(){}

    public ArrivalsCarDto(String inDriverName, String inDriverContact, String inLicense, Integer arrivalType) {
        this.inDriverName = inDriverName;
        this.inDriverContact = inDriverContact;
        this.inLicense = inLicense;
        this.arrivalType = arrivalType;
    }

    public String getInDriverName() {
        return inDriverName;
    }

    public void setInDriverName(String inDriverName) {
        this.inDriverName = inDriverName;
    }

    public String getInDriverContact() {
        return inDriverContact;
    }

    public void setInDriverContact(String inDriverContact) {
        this.inDriverContact = inDriverContact;
    }

    public String getInLicense() {
        return inLicense;
    }

    public void setInLicense(String inLicense) {
        this.inLicense = inLicense;
    }

    public Integer getArrivalType() {
        return arrivalType;
    }

    public void setArrivalType(Integer arrivalType) {
        this.arrivalType = arrivalType;
    }
}
