/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
 */
package com.ycg.ksh.adapter.impl;

import com.baidu.aip.ocr.AipOcr;
import com.ycg.ksh.adapter.api.BaiduService;
import com.ycg.ksh.common.dubbo.ServiceMonitor;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.adapter.SysNetKey;
import com.ycg.ksh.entity.adapter.baidu.WordInfo;
import com.ycg.ksh.entity.adapter.baidu.WordResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 百度接口实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
 */
@Service("ksh.net.service.baiduService")
public class BaiduServiceImpl implements BaiduService , ServiceMonitor {
    
    private static final HashMap<String, String> OPTIONS = new HashMap<String, String>(0);
    
    private static AipOcr client;


    /**
     * 服务启动完成
     *
     * @param activeProfile
     */
    @Override
    public void onServerStrated(ApplicationContext context, String activeProfile) throws BusinessException {
        String app_id = SystemUtils.get(SysNetKey.NET_BAIDU_APP_ID);
        String app_key = SystemUtils.get(SysNetKey.NET_BAIDU_APP_KEY);
        String secret_key = SystemUtils.get(SysNetKey.NET_BAIDU_SECRET_KEY);
        client = new AipOcr(app_id, app_key, secret_key);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        logger.info("初始化百度SDK appId:{} appKey:{} secretKey:{}", app_id, app_key, secret_key);
    }

    private WordResult convert(JSONObject object) {
        if(object == null) {
            return null;
        }
        WordResult result = new WordResult(object.getLong("log_id"), object.getInt("words_result_num"));
        JSONArray arrays = object.getJSONArray("words_result");
        if(arrays != null && arrays.length() > 0) {
            List<WordInfo> collection = new ArrayList<WordInfo>(arrays.length());
            for (int i = 0; i < arrays.length(); i++) {
                WordInfo info = new WordInfo();
                JSONObject jsonObject = arrays.getJSONObject(i);
                info.setWords(jsonObject.getString("words"));
                if(jsonObject.isNull("location")) {
                    JSONObject loaction = jsonObject.getJSONObject("location");
                    info.setTop(loaction.getInt("top"));
                    info.setLeft(loaction.getInt("left"));
                    info.setHeight(loaction.getInt("height"));
                    info.setWidth(loaction.getInt("width"));
                }
                collection.add(info);
            }
            result.setWordInfos(collection);
        }
        return result;
    }
    
    /**
     * @see BaiduService#general(java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
     */
    @Override
    public WordResult general(String path) {
        return convert(client.general(path, OPTIONS));
    }
    
    /**
     * @see BaiduService#general(byte[])
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
     */
    @Override
    public WordResult general(byte[] bytes) {
        return convert(client.general(bytes, OPTIONS));
    }
    
    /**
     * @see BaiduService#accurateGeneral(java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
     */
    @Override
    public WordResult accurateGeneral(String path) {
        return convert(client.accurateGeneral(path, OPTIONS));
    }
    
    /**
     * @see BaiduService#accurateGeneral(byte[])
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
     */
    @Override
    public WordResult accurateGeneral(byte[] bytes) {
        return convert(client.accurateGeneral(bytes, OPTIONS));
    }
    
    /**
     * @see BaiduService#basicAccurateGeneral(java.lang.String)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
     */
    @Override
    public WordResult basicAccurateGeneral(String path) {
        return convert(client.basicAccurateGeneral(path, OPTIONS));
    }
    
    /**
     * @see BaiduService#basicAccurateGeneral(byte[])
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:30:00
     */
    @Override
    public WordResult basicAccurateGeneral(byte[] bytes) {
        return convert(client.basicAccurateGeneral(bytes, OPTIONS));
    }
    
}
