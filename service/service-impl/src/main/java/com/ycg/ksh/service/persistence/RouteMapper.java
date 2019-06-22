package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.Route;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

public interface RouteMapper extends Mapper<Route> {
	
	Page<Route> queryPageRoute(Route route,RowBounds bounds);
}