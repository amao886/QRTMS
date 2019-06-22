package com.ycg.ksh.core.driver.application.dto;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Driver;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业司机
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class CompanyDriverDto extends BaseEntity {

    private Long identify; // 标识
    private String name; // 真实姓名-------司机的姓名，中文，不能为空
    private String phone; //手机号码-------11位数字，不能为空，手机号校验规则为。。。
    private LocalDateTime relationTime; // 关联时间-------系统自动记录当前时间

    private Collection<DriverCarDto> cars;
    private Collection<DriverRouteDto>  routes;

    public Long getIdentify() {
        return identify;
    }

    public void setIdentify(Long identify) {
        this.identify = identify;
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


    public LocalDateTime getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(LocalDateTime relationTime) {
        this.relationTime = relationTime;
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
