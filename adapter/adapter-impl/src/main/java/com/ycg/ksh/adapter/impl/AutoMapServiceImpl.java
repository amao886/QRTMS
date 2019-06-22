/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 09:40:02
 */
package com.ycg.ksh.adapter.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.dubbo.ServiceMonitor;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.adapter.SysNetKey;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.adapter.api.AutoMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高德地图接口实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 09:40:02
 */
@Service("ksh.net.service.autoMapService")
public class AutoMapServiceImpl implements AutoMapService, ServiceMonitor {
    
    private final Logger logger = LoggerFactory.getLogger(AutoMapService.class);

    
    private static final String EMPTY = "[]";
    private static String url;
    private static String key;
    private static Map<String, AutoMapLocation> localCache;


    /**
     * 服务启动完成
     *
     * @param activeProfile
     */
    @Override
    public void onServerStrated(ApplicationContext context, String activeProfile) throws BusinessException {
        url = SystemUtils.get(SysNetKey.NET_AUTOMAP_HOST);
        key = SystemUtils.get(SysNetKey.NET_AUTOMAP_KEY);
        localCache = new ConcurrentHashMap<String, AutoMapLocation>();

        logger.info("高德地图初始化完成");
    }

    private String buildurl(String address) throws Exception{
        return url+"?address="+ URLEncoder.encode(address, "utf8") +"&output=json&key="+ key;
    }
    
    private AutoMapLocation loadFormStorage(String address) throws ParameterException, BusinessException{
        try {
            String json = new HttpClient(buildurl(address)).get();
            JSONObject result = JSONObject.parseObject(json);   
            if(result.getIntValue("status") == 1){
                JSONArray array = result.getJSONArray("geocodes");
                if(array != null && array.size() > 0) {
                    JSONObject object = array.getJSONObject(0);
                    AutoMapLocation location = new AutoMapLocation();
                    location.setFormatAddress(object.getString("formatted_address"));
                    location.setAdcode(object.getString("adcode"));
                    location.setProvince(object.getString("province"));
                    location.setCity(object.getString("city"));
                    location.setDistrict(object.getString("district"));
                    StringBuilder builder =  new StringBuilder();
                    String township = object.getString("township");
                    if(StringUtils.isNotBlank(township) && !EMPTY.equals(township)){
                        builder.append(township);
                    }
                    String street = object.getString("street");
                    if(StringUtils.isNotBlank(street) && !EMPTY.equals(street)){
                        builder.append(street);
                        location.setStreet(street);
                    }
                    String number = object.getString("number");
                    if(StringUtils.isNotBlank(number) && !EMPTY.equals(number)) {
                        builder.append(number);
                        location.setNumber(number);
                    }
                    location.setAddress(builder.toString());
                    String locationString = object.getString("location");
                    String[] locations = locationString.split(",");
                    location.setLongitude(locations[0]);
                    location.setLatitude(locations[1]);
                    return location;
                }
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException("地址换取经纬度异常, address:{}", address, e);
        }
    }
    
    
    @Override
    public AutoMapLocation coordinate(String address) {
        try {
            AutoMapLocation location = localCache.get(address);
            if(location == null) {
                location = loadFormStorage(address);
                if(location != null) {
                    localCache.put(address, location); 
                }
            }
            logger.info("address:{} -> {}", address, location);
            return location;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Double distance(Double slng, Double slat, Double elng, Double elat) {
        if(slng == null || slat == null || elng == null || elat == null){
            return null;
        }
        double lon1 = (Math.PI / 180) * slng;
        double lon2 = (Math.PI / 180) * elng;
        double lat1 = (Math.PI / 180) * slat;
        double lat2 = (Math.PI / 180) * elat;
        double R = 6400;// 地球半径  
        // 两点间距离 km，如果想要米的话，结果*1000就可以了  
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;  
        return d;
    }

    @Override
    public Double distance(Serializable slng, Serializable slat, Serializable elng, Serializable elat) {
        return distance(new Double(slng.toString()), new Double(slat.toString()), new Double(elng.toString()), new Double(elat.toString()));
    }
}
