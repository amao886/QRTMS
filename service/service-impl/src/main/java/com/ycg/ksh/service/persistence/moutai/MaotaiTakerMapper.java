package com.ycg.ksh.service.persistence.moutai;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.moutai.Taker;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface MaotaiTakerMapper extends Mapper<Taker> {
    Page<Taker> queryTakerList(RowBounds rowBounds);

    Collection<Taker> selectTakerByConveyId(Long conveyId);
}