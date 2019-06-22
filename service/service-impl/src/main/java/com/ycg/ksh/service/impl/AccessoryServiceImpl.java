package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */

import com.ycg.ksh.entity.common.constant.HotspotType;
import com.ycg.ksh.entity.persistent.UserHotspot;
import com.ycg.ksh.service.persistence.UserHotspotMapper;
import com.ycg.ksh.service.api.AccessoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;

/**
 * 附加业务接口实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */
@Service("ksh.core.service.accessoryService")
public class AccessoryServiceImpl implements AccessoryService {


    @Resource
    UserHotspotMapper hotspotMapper;
    /**
     * 更新热点数据
     *
     * @param uKey         操作用户ID
     * @param hotspotType  热点类型
     * @param associateKeys 关联主键
     */
    @Override
    public void modifyHotspot(Integer uKey, HotspotType hotspotType, Collection<? extends Serializable> associateKeys) {
        try {
            for (Serializable associateKey : associateKeys) {
                UserHotspot hotspot = new UserHotspot();
                hotspot.setAssociateType(hotspotType.getCode());
                hotspot.setUserId(uKey);
                hotspot.setAssociateKey(String.valueOf(associateKey));
                if(hotspotMapper.selectCount(hotspot) > 0){
                    hotspotMapper.updateCount(hotspot);
                }else{
                    hotspot.setHotspotCount(1L);
                    hotspotMapper.insertSelective(hotspot);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("更新热点异常 uKey:{} tyep:{} keys:{}", uKey, hotspotType, associateKeys);
        }
    }
}
