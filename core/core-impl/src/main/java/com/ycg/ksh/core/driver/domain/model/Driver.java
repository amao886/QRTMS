package com.ycg.ksh.core.driver.domain.model;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.Registrys;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 司机实体
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/30 0030
 */

public class Driver extends Model {

    private Long identify; // 司机标识-------与用户有对应关系
    private String name; // 真实姓名-------司机的姓名，中文，不能为空
    private String phone; //手机号码-------11位数字，不能为空，手机号校验规则为。。。
    private LocalDateTime registerTime; // 注册时间-------系统自动记录当前时间

    private Set<DriverCar>  cars;
    private Set<DriverRoute>  routes;

    public Driver() { }

    private void nameAndPhnoe(String name, String phone) throws ParameterException, BusinessException{
        this.setName(name);
        this.setPhone(phone);
        if(Registrys.driverRepository().existByPhnoe(phone, identify)){
            throw new BusinessException("手机号["+ phone +"] 已经注册了司机");
        }
    }


    public void register(Long driverKey, String name, String phone, Collection<DriverCar> _cars, Collection<DriverRoute> _routes)  throws BusinessException{
        if(Registrys.driverRepository().existByDriverKey(driverKey)){
            throw new BusinessException("已经注册过司机了");
        }
        setIdentify(driverKey);
        nameAndPhnoe(name, phone);
        driverCars(_cars);
        driverRoutes(_routes);
        setRegisterTime(LocalDateTime.now());
        Registrys.driverRepository().save(this);
    }

    public void modify(String name, String phone, Collection<DriverCar>  _cars, Collection<DriverRoute>  _routes) throws BusinessException{
        this.nameAndPhnoe(name, phone);
        this.driverCars(_cars);
        this.driverRoutes(_routes);
        Registrys.driverRepository().modify(this);
    }

    public DriverAwaitInfo driverAwaitInfo(){
        return  new DriverAwaitInfo(identify, name, phone);
    }


    private void modifyCars(Collection<DriverCar> collection){
        if(CollectionUtils.isNotEmpty(cars)){
            cars.clear();
        }
        cars.addAll(collection);
    }

    private void driverCars(Collection<DriverCar> _cars) throws BusinessException {
        cars = Optional.ofNullable(cars).orElse(new HashSet<DriverCar>());
        if (CollectionUtils.isNotEmpty(_cars)) {
            _cars = _cars.stream().map(c -> c.modify(identify)).collect(Collectors.toList());
            modifyCars(cars.stream().filter(_cars::contains).collect(Collectors.toSet()));
            cars.addAll(validateFilterExist(_cars.stream().filter(r -> !cars.contains(r)).collect(Collectors.toList())));
        }else{
            cars.clear();
        }
    }

    private Collection<DriverCar> validateFilterExist(Collection<DriverCar> newcars) throws BusinessException {
        if(CollectionUtils.isNotEmpty(newcars)){
            Collection<DriverCar> exists = Registrys.driverRepository().listCarByLicense(newcars.stream().map(DriverCar::getLicense).collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(exists)){
                Map<String, Long> map = exists.stream().collect(Collectors.toMap(DriverCar::getLicense, DriverCar::getDriverKey));
                Collection<DriverCar>  unqualifieds = newcars.stream().filter(e-> map.containsKey(e.getLicense()) && map.get(e.getLicense()) - e.getDriverKey() != 0).collect(Collectors.toSet());
                if(CollectionUtils.isNotEmpty(unqualifieds)){
                    throw new BusinessException("车牌["+ unqualifieds.stream().map(DriverCar::getLicense).collect(Collectors.joining(",")) +"]已经被其他司机注册了");
                }
                return newcars.stream().filter(e-> !map.containsKey(e.getLicense()) || map.get(e.getLicense()) - e.getDriverKey() == 0).collect(Collectors.toSet());
            }
        }
        return newcars;
    }
    private void modifyRoutes(Collection<DriverRoute> collection){
        if(CollectionUtils.isNotEmpty(routes)){
            routes.clear();
        }
        routes.addAll(collection);
    }
    private void driverRoutes(Collection<DriverRoute>  _routes) throws BusinessException{
        routes = Optional.ofNullable(routes).orElse(new HashSet<DriverRoute>());
        if(CollectionUtils.isNotEmpty(_routes)){
            _routes = _routes.stream().map(c -> c.modify(identify)).collect(Collectors.toList());
            modifyRoutes(routes.stream().filter(_routes::contains).collect(Collectors.toSet()));
            routes.addAll(_routes.stream().filter(r-> !routes.contains(r)).collect(Collectors.toList()));
        }
    }

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
        Validator validator = Validator.CHINESENAME;
        if(!validator.verify(name, false)){
            throw new ParameterException(validator.getMessage("姓名"));
        }
        if(name.length() > 20){
            throw new ParameterException("姓名长度不能大于20");
        }
        this.name = Optional.of(name).orElseThrow(()-> new ParameterException("司机姓名不能为空"));
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        Validator validator = Validator.MOBILE;
        if(!validator.verify(phone, false)){
            throw new ParameterException(validator.getMessage("手机号"));
        }
        this.phone = Optional.of(phone).orElseThrow(()-> new ParameterException("司机手机号不能为空"));
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = Optional.ofNullable(registerTime).orElse(LocalDateTime.now());
    }

    public Set<DriverCar> getCars() {
        return cars;
    }

    public Set<DriverRoute> getRoutes() {
        return routes;
    }

    public void setCars(Set<DriverCar> cars) {
        this.cars = cars;
    }

    public void setRoutes(Set<DriverRoute> routes) {
        this.routes = routes;
    }
}
