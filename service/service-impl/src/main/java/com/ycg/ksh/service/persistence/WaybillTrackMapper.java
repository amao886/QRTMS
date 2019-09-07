package com.ycg.ksh.service.persistence;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.WaybillTrack;

import java.util.List;

/**
 * 运单轨迹持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:30:51
 */
public interface WaybillTrackMapper extends CustomMapper<WaybillTrack> {


    /**
     * 批量添加用户轨迹数据
     *
     * @Author：wangke
     * @description：
     * @Date：13:45 2018/1/10
     */
    void saveTrack(List<WaybillTrack> list);

    /**
     * 查询合并定位信息（业务逻辑需优化）
     * @param waybillId
     * @param barCode
     * @return
     */
    List<WaybillTrack> queryTracks(Integer waybillId, String barCode);
}