package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.TransitionTrack;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface TransitionTrackMapper extends Mapper<TransitionTrack> {
	
	void inserts(Collection<TransitionTrack> collection);
}