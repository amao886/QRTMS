package com.ycg.ksh.core.driver.domain.model;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.util.Constants;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业司机
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/04 0004
 */
public class CompanyDriver extends Model {

    private Long identify; // 标识
    private Long compaynKey; // 企业标识
    private Long driverKey; // 司机标识-------与系统司机有对应关系
    private String name; // 真实姓名-------司机的姓名，中文，不能为空
    private String phone; //手机号码-------11位数字，不能为空
    private LocalDateTime relationTime; // 关联时间-------系统自动记录当前时间
    private Integer status;//状态---待审核、合格、不合格


    private Set<CompanyDriverCar>  cars;
    private Set<CompanyDriverRoute>  routes;

    public CompanyDriver() { }

    public CompanyDriver( Long compaynKey) {
        this.compaynKey = compaynKey;
    }

    private void transformDriver(Driver driver){
        setDriverKey(driver.getIdentify());
        setName(driver.getName());
        setPhone(driver.getPhone());
        if(CollectionUtils.isNotEmpty(driver.getCars())){
            setCars(driver.getCars().stream().map(c->new CompanyDriverCar(identify, c.getType(), c.getLength(), c.getLoadValue(), c.getLicense())).collect(Collectors.toSet()));
        }
        if(CollectionUtils.isNotEmpty(driver.getRoutes())){
            setRoutes(driver.getRoutes().stream().map(c->new CompanyDriverRoute(identify, c.getType(), c.getStart(), c.getEnd())).collect(Collectors.toSet()));
        }
    }

    public void  create(Driver driver){
        setIdentify(Registrys.companyDriverRepository().nextIdentify());
        transformDriver(driver);
        setRelationTime(LocalDateTime.now());
        setStatus(Constants.CDRIVER_STATUS_QUALIFIED);
        Registrys.companyDriverRepository().save(this);
    }
    public void  synchronize(Driver driver){
        //同步逻辑, 后面可能会加入状态的限制
        transformDriver(driver);
        Registrys.companyDriverRepository().modify(this);
    }

    public void modify(String name, String phone, Collection<CompanyDriverCar>  _cars, Collection<CompanyDriverRoute>  _routes) throws BusinessException{
        this.setName(name);
        this.setPhone(phone);
        if(Registrys.companyDriverRepository().existByPhnoe(phone, identify)){
            throw new BusinessException("["+ phone +"] 已经注册了司机");
        }
        this.driverCars(_cars);
        this.driverRoutes(_routes);
        Registrys.companyDriverRepository().modify(this);
    }
    private void modifyCars(Collection<CompanyDriverCar> collection){
        if(CollectionUtils.isNotEmpty(cars)){
            cars.clear();
        }
        cars.addAll(collection);
    }

    private void driverCars(Collection<CompanyDriverCar> _cars) throws BusinessException {
        cars = Optional.ofNullable(cars).orElse(new HashSet<CompanyDriverCar>());
        if (CollectionUtils.isNotEmpty(_cars)) {
            _cars = _cars.stream().map(c -> c.modify(identify)).collect(Collectors.toList());
            modifyCars(cars.stream().filter(_cars::contains).collect(Collectors.toSet()));
            cars.addAll(validateFilterExist(_cars.stream().filter(r -> !cars.contains(r)).collect(Collectors.toList())));
        }
    }

    private Collection<CompanyDriverCar> validateFilterExist(Collection<CompanyDriverCar> newcars) throws BusinessException {
        if(CollectionUtils.isNotEmpty(newcars)){
            Collection<CompanyDriverCar> exists = Registrys.companyDriverRepository().listCarByLicense(newcars.stream().map(CompanyDriverCar::getLicense).collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(exists)){
                Map<String, Long> map = exists.stream().collect(Collectors.toMap(CompanyDriverCar::getLicense, CompanyDriverCar::getDriverKey));
                Collection<CompanyDriverCar>  unqualifieds = newcars.stream().filter(e-> map.containsKey(e.getLicense()) && map.get(e.getLicense()) - e.getDriverKey() != 0).collect(Collectors.toSet());
                if(CollectionUtils.isNotEmpty(unqualifieds)){
                    throw new BusinessException("车牌["+ unqualifieds.stream().map(CompanyDriverCar::getLicense).collect(Collectors.joining(",")) +"]已经被其他司机注册了");
                }
                return newcars.stream().filter(e-> !map.containsKey(e.getLicense()) || map.get(e.getLicense()) - e.getDriverKey() == 0).collect(Collectors.toSet());
            }
        }
        return newcars;
    }
    private void modifyRoutes(Collection<CompanyDriverRoute> collection){
        if(CollectionUtils.isNotEmpty(routes)){
            routes.clear();
        }
        routes.addAll(collection);
    }
    private void driverRoutes(Collection<CompanyDriverRoute>  _routes) throws BusinessException{
        routes = Optional.ofNullable(routes).orElse(new HashSet<CompanyDriverRoute>());
        if(CollectionUtils.isNotEmpty(_routes)){_routes = _routes.stream().map(c -> c.modify(identify)).collect(Collectors.toList());
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

    public Long getCompaynKey() {
        return compaynKey;
    }

    public void setCompaynKey(Long compaynKey) {
        this.compaynKey = Optional.of(compaynKey).orElseThrow(()-> new ParameterException("企业编号不能为空"));
    }

    public Long getDriverKey() {
        return driverKey;
    }

    public void setDriverKey(Long driverKey) {
        this.driverKey = Optional.of(driverKey).orElseThrow(()-> new ParameterException("司机编号不能为空"));;
    }

    public LocalDateTime getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(LocalDateTime relationTime) {
        this.relationTime = Optional.ofNullable(relationTime).orElse(LocalDateTime.now());;
    }

    public Set<CompanyDriverCar> getCars() {
        return cars;
    }

    public Set<CompanyDriverRoute> getRoutes() {
        return routes;
    }

    public void setCars(Set<CompanyDriverCar> cars) {
        this.cars = cars;
    }

    public void setRoutes(Set<CompanyDriverRoute> routes) {
        this.routes = routes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
