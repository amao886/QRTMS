package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/20
 */

import com.ycg.ksh.entity.persistent.LocationTrack;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 位置轨迹
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/20
 */
public class AllianceLocation extends LocationTrack {

    private String reporterName;
    private String reporterMobile;

    public AllianceLocation() { }

    public AllianceLocation(LocationTrack locationTrack) {
        this();
        try{
            BeanUtils.copyProperties(this, locationTrack);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterMobile() {
        return reporterMobile;
    }

    public void setReporterMobile(String reporterMobile) {
        this.reporterMobile = reporterMobile;
    }
}
