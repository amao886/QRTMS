package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.UserHotspot;
import tk.mybatis.mapper.common.Mapper;

public interface UserHotspotMapper extends Mapper<UserHotspot> {

    /**
     * 更新热点
     * @param hotspot
     */
    public void updateCount(UserHotspot hotspot);

}