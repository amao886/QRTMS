package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.RouteLine;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface RouteLineMapper extends Mapper<RouteLine> {

	Collection<RouteLine> queryListByRouteId(Integer routeId);
}