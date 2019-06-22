package com.ycg.ksh.service.impl.adventive.deliveryer;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/18
 */

import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;
import com.ycg.ksh.service.impl.adventive.AdventiveDeliveryer;
import org.springframework.stereotype.Service;

/**
 * 默认推送器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/18
 */
@Service("ksh.core.service.adventive.deliveryer.default")
public class DefaultDeliveryer implements AdventiveDeliveryer {

    String SUCCESS = "success";
    String OBJECT_TYPE = "adventive_object_type";
    String OPPOSITE_KEY = "adventive_opposite_key";

    /**
     * 是否是默认的
     *
     * @return
     */
    @Override
    public boolean isDefault() { return true; }

    /**
     * 验证是否可以处理
     * @param adventive
     * @param adventiveNote
     * @return
     */
    public boolean validate(Adventive adventive, AdventiveNote adventiveNote){
        return false;
    }
    /**
     * 推送处理
     * @param adventive
     * @param adventiveNote
     * @param object
     * @return
     */
    public boolean process(Adventive adventive, AdventiveNote adventiveNote, Object object){
        //默认使用http + json 推送
        System.out.println(Globallys.toJsonString(object));
        if(StringUtils.isNotBlank(adventive.getDeliveryUrl())){
            try{
                HttpClient client = new HttpClient(adventive.getDeliveryUrl());
                client.property(OBJECT_TYPE, adventiveNote.getNoteType());
                client.property(OPPOSITE_KEY, adventiveNote.getOppositeKey());
                client.setParameterString(Globallys.toJsonString(object));
                String results = client.request();
                logger.info("------------------------------->{}", client);

                if(StringUtils.equalsIgnoreCase(SUCCESS, results)){
                    return true;
                }
            } catch (Exception e){
                logger.error("数据推送异常 {}", adventiveNote, e);
            }
        }
        return false;
    }
}
