package com.ycg.ksh.core.driver.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 司机数据传输对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class DriverDto extends BaseEntity {

    private Long driverKey;
    private String name; // 真实姓名-------司机的姓名，中文，不能为空
    private String phone; //手机号码-------11位数字，不能为空，手机号校验规则为。。。
    private LocalDateTime registerTime; // 注册时间-------系统自动记录当前时间

    private Collection<DriverCarDto> cars;
    private Collection<DriverRouteDto>  routes;

    public DriverDto() {
    }

    public DriverDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public void setDriverKey(Long driverKey) {
        this.driverKey = driverKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public Collection<DriverCarDto> getCars() {
        return cars;
    }

    public void setCars(Collection<DriverCarDto> cars) {
        this.cars = cars;
    }

    public Collection<DriverRouteDto> getRoutes() {
        return routes;
    }

    public void setRoutes(Collection<DriverRouteDto> routes) {
        this.routes = routes;
    }
}
