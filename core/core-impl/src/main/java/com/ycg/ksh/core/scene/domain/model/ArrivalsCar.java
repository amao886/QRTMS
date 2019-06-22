package com.ycg.ksh.core.scene.domain.model;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.util.Constants;


/**
 * 车辆到达信息值对象
 *
 * @author: wangke
 * @create: 2018-12-11 14:23
 **/

public class ArrivalsCar extends Model {


    private Integer arrivalType;//到达类型

    private String inDriverName; //司机名称

    private String inDriverContact; //司机电话

    private String inLicense; //车牌号

    public ArrivalsCar() {

    }

    public ArrivalsCar(String inDriverName, String inDriverContact, String inLicense, Integer arrivalType) {
        this.setArrivalType(arrivalType);
        this.setInDriverName(inDriverName);
        this.setInDriverContact(inDriverContact);
        this.setInLicense(inLicense);
    }

    public Integer getArrivalType() {
        return arrivalType;
    }

    private void setArrivalType(Integer arrivalType) {
        Assert.notBlank(arrivalType, "备注选项不能为空");
        this.arrivalType = arrivalType;
    }

    public boolean verify() {
        return this.arrivalType.equals(Constants.ARRIVAL_TYPE_INCONSISTENT) || this.arrivalType.equals(Constants.ARRIVAL_TYPE_NO);
    }


    public String getInDriverName() {
        return inDriverName;
    }

    private void setInDriverName(String inDriverName) {
        if (verify()) {
            Assert.notBlank(inDriverName, "登记司机姓名不能为空");
        }
        this.inDriverName = inDriverName;
    }

    public String getInDriverContact() {
        return inDriverContact;
    }

    private void setInDriverContact(String inDriverContact) {
        if (verify()) {
            Assert.notBlank(inDriverContact, "登记司机电话不能为空");
        }
        this.inDriverContact = inDriverContact;
    }

    public String getInLicense() {
        return inLicense;
    }

    private void setInLicense(String inLicense) {
        if (verify()) {
            Assert.notBlank(inLicense, "登记车牌号不能为空");
        }
        this.inLicense = inLicense;
    }

}
