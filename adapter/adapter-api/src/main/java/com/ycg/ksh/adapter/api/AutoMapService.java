/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 09:32:44
 */
package com.ycg.ksh.adapter.api;

import com.ycg.ksh.entity.adapter.AutoMapLocation;

import java.io.Serializable;

/**
 * 高德地图相关接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 09:32:44
 */
public interface AutoMapService {
    
    /**
     * 将地址转换成高德地图地理数据
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 09:35:12
     * @param address
     * @return
     */
    AutoMapLocation coordinate(String address);
    
    /**
     * 计算两个经纬度之间的距离
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 09:35:43
     * @param slng
     * @param slat
     * @param elng
     * @param elat
     * @return
     */
    Double distance(Double slng, Double slat, Double elng, Double elat);
    Double distance(Serializable slng, Serializable slat, Serializable elng, Serializable elat);
}