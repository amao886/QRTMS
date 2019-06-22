package com.ycg.ksh.service.api;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */

import com.ycg.ksh.entity.common.constant.HotspotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

/**
 * 附加业务接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */
public interface AccessoryService {

    final Logger logger = LoggerFactory.getLogger(AccessoryService.class);

    /**
     * 更新热点数据
     * @param uKey 操作用户ID
     * @param hotspotType 热点类型
     * @param associateKeys 热点关联数据主键
     */
    public void modifyHotspot(Integer uKey, HotspotType hotspotType, Collection<? extends Serializable> associateKeys);
}
