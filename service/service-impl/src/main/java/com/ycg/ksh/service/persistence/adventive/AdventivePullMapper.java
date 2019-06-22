package com.ycg.ksh.service.persistence.adventive;

import com.ycg.ksh.entity.persistent.adventive.AdventivePull;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface AdventivePullMapper extends Mapper<AdventivePull> {

    Collection<AdventivePull> selectByAdventiveKey(Long adventiveKey);
}