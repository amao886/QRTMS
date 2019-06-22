package com.ycg.ksh.service.persistence.moutai;

import com.ycg.ksh.entity.persistent.moutai.Convey;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface MaotaiConveyMapper extends Mapper<Convey> {

    /**
     * 根据供应商名称模糊查询供应商信息
     * @param carrierUnit
     * @return
     */
    Collection<Convey> selectConveyByConveyName(String carrierUnit);
}