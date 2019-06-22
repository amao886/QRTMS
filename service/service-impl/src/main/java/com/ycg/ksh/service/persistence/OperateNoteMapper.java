package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.OperateNote;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OperateNoteMapper extends Mapper<OperateNote> {

    OperateNote queryByHostId(@Param("orderKey") Long orderKey);
}