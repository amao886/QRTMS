package com.ycg.ksh.core.driver.application.transform;

import com.google.common.collect.Lists;
import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.core.driver.application.dto.*;
import com.ycg.ksh.core.driver.domain.model.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  司机数据传输对象转换
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class DriverDtoTransformer {

    private static final DriverCarDto EMPTY_DRIVERCARDTO = new DriverCarDto();
    private static final DriverRouteDto EMPTY_DRIVERROUTEDTO = new DriverRouteDto();


    public DriverDto dtoFromDriver(Driver driver){
        DriverDto dto = new DriverDto();
        dto.setDriverKey(driver.getIdentify());
        dto.setName(driver.getName());
        dto.setPhone(driver.getPhone());
        dto.setRegisterTime(driver.getRegisterTime());
        dto.setCars(dtoFromDriverCar(driver.getCars()));
        dto.setRoutes(dtoFromDriverRoute(driver.getRoutes()));
        return dto;
    }

    public DriverAwaitInfoDto dtoFromDriverAwaitInfo(DriverAwaitInfo info){
        DriverAwaitInfoDto dto = new DriverAwaitInfoDto();
        dto.setDriverKey(info.getDriverKey());
        dto.setDriverName(info.getDriverName());
        dto.setDriverPhone(info.getDriverPhone());
        dto.setAwaitKey(info.getWaitKey());
        dto.setReleaseTime(info.getReleaseTime());
        dto.setStartTime(info.getStartTime());
        dto.setStatus(info.getStatus());

        DriverCar car = info.getCar();
        if(car != null){
            dto.setCarType(car.getType());
            dto.setLength(car.getLength());
            dto.setLicense(car.getLicense());
            dto.setLoadValue(car.getLoadValue());
        }

        DriverRoute route = info.getRoute();
        if(route != null){
            dto.setRouteType(route.getType());
            dto.setStart(route.getStart());
            dto.setEnd(route.getEnd());
        }
        return dto;
    }

    public CompanyDriverDto dtoFromCompanyDriver(CompanyDriver driver){
        CompanyDriverDto dto = new CompanyDriverDto();
        dto.setIdentify(driver.getIdentify());
        dto.setName(driver.getName());
        dto.setPhone(driver.getPhone());
        dto.setRelationTime(driver.getRelationTime());
        dto.setCars(dtoFromCompanyDriverCar(driver.getCars()));
        dto.setRoutes(dtoFromCompanyDriverRoute(driver.getRoutes()));
        return dto;
    }

    public Collection<CompanyDriverDto> dtoFromCompanyDriver(Collection<CompanyDriver> drivers){
        return drivers.stream().map(this::dtoFromCompanyDriver).collect(Collectors.toList());
    }

    public Collection<DriverAwaitInfoDto> dtoFromDriverAwaitInfo(Collection<DriverAwaitInfo> infos){
        return infos.stream().map(this::dtoFromDriverAwaitInfo).collect(Collectors.toList());
    }

    public DriverCar transformDriverCar(DriverCarDto carDto){
        return Optional.ofNullable(carDto).map(d->new DriverCar(d.getType(), d.getLength(), d.getLoadValue(), d.getLicense())).orElse(null);
    }
    public DriverRoute transformDriverRoute(DriverRouteDto routeDto){
        return Optional.ofNullable(routeDto).map(d->new DriverRoute(d.getType(), RegionUtils.analyze(d.getStarts()), RegionUtils.analyze(d.getEnds()))).orElse(null);
    }

    public Collection<DriverCar> transformDriverCar(Collection<DriverCarDto> dtos){
        return dtos.stream().map(this::transformDriverCar).collect(Collectors.toList());
    }
    public Collection<DriverRoute> transformDriverRoute(Collection<DriverRouteDto> dtos){
        return dtos.stream().map(this::transformDriverRoute).collect(Collectors.toList());
    }

    public DriverCar transformDriverCar(DriverAwaitInfoDto dto){
        return Optional.ofNullable(dto).map(d->new DriverCar(d.getCarType(), d.getLength(), d.getLoadValue(), d.getLicense())).orElse(null);
    }
    public DriverRoute transformDriverRoute(DriverAwaitInfoDto dto){
        return Optional.ofNullable(dto).map(d->new DriverRoute(d.getRouteType(), d.getStart(), d.getEnd())).orElse(null);
    }

    public CompanyDriverCar transformCompanyDriverCar(DriverCarDto carDto){
        return new CompanyDriverCar(carDto.getType(), carDto.getLength(), carDto.getLoadValue(), carDto.getLicense());
    }
    public CompanyDriverRoute transformCompanyDriverRoute(DriverRouteDto routeDto){
        return new CompanyDriverRoute(routeDto.getType(), RegionUtils.analyze(routeDto.getStarts()), RegionUtils.analyze(routeDto.getEnds()));
    }

    public Collection<CompanyDriverCar> transformCompanyDriverCar(Collection<DriverCarDto> dtos){
        return dtos.stream().map(this::transformCompanyDriverCar).collect(Collectors.toList());
    }
    public Collection<CompanyDriverRoute> transformCompanyDriverRoute(Collection<DriverRouteDto> dtos){
        return dtos.stream().map(this::transformCompanyDriverRoute).collect(Collectors.toList());
    }

    public DriverCarDto dtoFromDriverCar(DriverCar car){
        return Optional.ofNullable(car).map(d->new DriverCarDto(d.getType(), d.getLength(), d.getLoadValue(), d.getLicense())).orElse(EMPTY_DRIVERCARDTO);
    }
    public DriverRouteDto dtoFromDriverRoute(DriverRoute route){
        return Optional.ofNullable(route).map(d->new DriverRouteDto(d.getType(), RegionUtils.analyze(d.getStart()), RegionUtils.analyze(d.getEnd()))).orElse(EMPTY_DRIVERROUTEDTO);
    }

    public Collection<DriverCarDto> dtoFromDriverCar(Collection<DriverCar> cars){
        if(CollectionUtils.isEmpty(cars)){
            return Lists.newArrayList(EMPTY_DRIVERCARDTO);
        }
        return cars.stream().map(this::dtoFromDriverCar).collect(Collectors.toList());
    }
    public Collection<DriverRouteDto> dtoFromDriverRoute(Collection<DriverRoute> routes){
        if(CollectionUtils.isEmpty(routes)){
            return Lists.newArrayList(EMPTY_DRIVERROUTEDTO);
        }
        return routes.stream().map(this::dtoFromDriverRoute).collect(Collectors.toList());
    }

    public DriverCarDto dtoFromCompanyDriverCar(CompanyDriverCar car){
        return Optional.ofNullable(car).map(d->new DriverCarDto(d.getType(), d.getLength(), d.getLoadValue(), d.getLicense())).orElse(EMPTY_DRIVERCARDTO);
    }
    public DriverRouteDto dtoFromCompanyDriverRoute(CompanyDriverRoute route){
        return Optional.ofNullable(route).map(d->new DriverRouteDto(d.getType(), RegionUtils.analyze(d.getStart()), RegionUtils.analyze(d.getEnd()))).orElse(EMPTY_DRIVERROUTEDTO);
    }

    public Collection<DriverCarDto> dtoFromCompanyDriverCar(Collection<CompanyDriverCar> cars){
        if(CollectionUtils.isEmpty(cars)){
            return Lists.newArrayList(EMPTY_DRIVERCARDTO);
        }
        return cars.stream().map(this::dtoFromCompanyDriverCar).collect(Collectors.toList());
    }
    public Collection<DriverRouteDto> dtoFromCompanyDriverRoute(Collection<CompanyDriverRoute> routes){
        if(CollectionUtils.isEmpty(routes)){
            return Lists.newArrayList(EMPTY_DRIVERROUTEDTO);
        }
        return routes.stream().map(this::dtoFromCompanyDriverRoute).collect(Collectors.toList());
    }

}
