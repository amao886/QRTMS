package com.ycg.ksh.core.driver.domain.model;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */

import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.util.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *  司机等货信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class DriverAwaitInfo extends Model {

    private Long waitKey;//标识
    private Long driverKey;//司机标识-------所属司机的唯一标识
    private String driverName;//司机名称
    private String driverPhone;//司机电话
    private LocalDate startTime;//等货开始时间-------时间的精确度
    private LocalDateTime releaseTime;//发布时间-------发布等货信息的时间，系统自动记录为系统当前时间，精确到时分秒？
    private Integer status;//状态-------有效、失效...

    private DriverCar car;
    private DriverRoute route;

    public DriverAwaitInfo() {
    }

    public DriverAwaitInfo(Long driverKey, String driverName, String driverPhone) {
        this.driverKey = driverKey;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public void release( LocalDate startTime, DriverCar car, DriverRoute route){
        //发布等货信息
        setWaitKey(Registrys.driverAwaitInfoRepository().nextIdentify());
        setStartTime(startTime);
        setReleaseTime(LocalDateTime.now());
        setStatus(Constants.AWAIT_STAUS_EFFECTIVE);

        setCar(car);
        setRoute(route);

        //持久化
        Registrys.driverAwaitInfoRepository().save(this);
    }

    public void invalid(){
        if(status == Constants.AWAIT_STAUS_EFFECTIVE){
            setStatus(Constants.AWAIT_STAUS_INVALID);
            Registrys.driverAwaitInfoRepository().modify(this);
        }
    }

    protected void setWaitKey(Long waitKey) {
        this.waitKey = waitKey;
    }

    protected void setDriverKey(Long driverKey) {
        this.driverKey = driverKey;
    }

    protected void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    protected void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    protected void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    protected void setReleaseTime(LocalDateTime releaseTime) {
        this.releaseTime = releaseTime;
    }

    protected void setStatus(Integer status) {
        this.status = status;
    }

    protected void setCar(DriverCar car) {
        this.car = car;
    }

    protected void setRoute(DriverRoute route) {
        this.route = route;
    }

    public Long getWaitKey() {
        return waitKey;
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDateTime getReleaseTime() {
        return releaseTime;
    }

    public Integer getStatus() {
        return status;
    }

    public DriverCar getCar() {
        return car;
    }

    public DriverRoute getRoute() {
        return route;
    }
}
