package com.ycg.ksh.service.impl.adventive.deliveryer;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/25
 */

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;
import com.ycg.ksh.service.impl.adventive.AdventiveDeliveryer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 与麒麟对接的逻辑处理
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/25
 */
@Service("ksh.core.service.adventive.deliveryer.kylin")
public class KylinDeliveryer implements AdventiveDeliveryer {

    private static final String CODE = "TMS";

    @Resource
    MessageQueueService mqService;

    /**
     * 验证是否可以处理
     *
     * @param adventive
     * @param adventiveNote
     * @return
     */
    @Override
    public boolean validate(Adventive adventive, AdventiveNote adventiveNote) {
        if (adventiveNote != null && adventive != null) {
            return CODE.equalsIgnoreCase(adventive.getHisCode());
        }
        return false;
    }

    /**
     * 推送处理
     *
     * @param adventive
     * @param adventiveNote 要推送的记录
     * @param object        要推送的对象， 数据类型由 adventiveNote.noteType 决定
     * @return
     */
    @Override
    public boolean process(Adventive adventive, AdventiveNote adventiveNote, Object object) {
        //处理具体的推送逻辑
        //直接将object发送到MQ,发送成功返回true,反之false
        try{
            if(logger.isDebugEnabled()){
                logger.debug("推送TMS数据 -> {}", Globallys.toJsonString(object));
            }
            MediaMessage message = new MediaMessage(adventiveNote.getId());
            message.setMessageType(object.getClass().getName());
            //message.setObject(Globallys.toJsonString(object));
            message.setObject(object);
            mqService.sendMessage(QueueKeys.ROUTE_OUT_KYLIN, message);
            return true;
        } catch (Exception e){
            logger.warn("TMS数据推送异常 {}", e.getMessage());
        }
        return false;
    }
}
