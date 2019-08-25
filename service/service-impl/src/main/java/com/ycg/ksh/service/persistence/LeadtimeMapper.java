package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.Leadtime;
import tk.mybatis.mapper.common.Mapper;

public interface LeadtimeMapper extends Mapper<Leadtime> {
	Leadtime quseryByShipCityAndDesCity(String shipcity,String descity);
}