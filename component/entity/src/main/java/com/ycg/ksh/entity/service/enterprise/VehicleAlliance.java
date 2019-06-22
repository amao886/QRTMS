package com.ycg.ksh.entity.service.enterprise;

import com.ycg.ksh.entity.persistent.enterprise.Vehicle;
import com.ycg.ksh.entity.persistent.enterprise.VehicleDesignate;

import java.util.Collection;

/**
 * 车辆管理综合类
 *
 * @author: wangke
 * @create: 2018-10-22 14:21
 **/

public class VehicleAlliance extends Vehicle {
    private CompanyConcise convey;//物流商
    private CompanyConcise lastCompany;//上级企业
    private VehicleDesignate designate;//指派数据


    private boolean confirm;//是否可以确认派车


    private Collection<CompanyConcise> concises;//物流商集合

    public VehicleDesignate getDesignate() {
        return designate;
    }

    public void setDesignate(VehicleDesignate designate) {
        this.designate = designate;
    }

    public CompanyConcise getConvey() {
        return convey;
    }

    public void setConvey(CompanyConcise convey) {
        this.convey = convey;
    }

    public CompanyConcise getLastCompany() {
        return lastCompany;
    }

    public void setLastCompany(CompanyConcise lastCompany) {
        this.lastCompany = lastCompany;
    }

    public Collection<CompanyConcise> getConcises() {
        return concises;
    }

    public void setConcises(Collection<CompanyConcise> concises) {
        this.concises = concises;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}
