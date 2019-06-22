package com.ycg.ksh.service.impl.adventive;

import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;
import com.ycg.ksh.service.core.entity.service.adventive.MediaObject;
import com.ycg.ksh.service.core.entity.service.adventive.PossibleObject;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class AdventiveDispatcher {

    final Logger logger = LoggerFactory.getLogger(AdventiveDispatcher.class);

    private static final String ERRORMESSAGE = "异常次数超过3次放弃处理";

    //0:等待处理,1:处理中,2:处理成功,99:放弃处理
    //private static final int NOTE_FETTLE_WAIT = 0;
    private static final int NOTE_FETTLE_ING = 1;
    private static final int NOTE_FETTLE_SUCCESS = 2;
    private static final int NOTE_FETTLE_GIVEUP = 99;


    private static final DelayQueue<MediaObject> cacheDeque = new DelayQueue<MediaObject>();

    private Predicate<AdventiveNote> handler;
    private Consumer<AdventiveNote> persistencer;
    private Function<PossibleObject, Collection<MediaObject>> filter;


    public void ready(Function<PossibleObject, Collection<MediaObject>> filter, Predicate<AdventiveNote> handler, Consumer<AdventiveNote> persistencer){
        this.filter = filter;
        this.handler = handler;
        this.persistencer = persistencer;
    }

    public void commence(){
        logger.info("AdventiveDispatcher -> 对外推送调度器已经准备就绪, 等待推送任务...");
        Globallys.executor(() -> {
            while (true){
                MediaObject mediaObject = null;
                try {
                    mediaObject = cacheDeque.take();
                    //logger.info("还有 {} 等待推送", cacheDeque.size());
                    if(mediaObject != null && !handle(mediaObject)){
                        dispatch(mediaObject.modifyNextTimes());//处理失败加入队列继续处理
                    }
                } catch (Exception e) {
                    logger.error("推送异常 {}", mediaObject);
                }
            }
        });
    }

    private boolean handle(final MediaObject mediaObject){
        boolean over = true;
        AdventiveNote adventiveNote = mediaObject.getTarget();
        logger.info("开始推送 {}", adventiveNote);
        try {
            boolean success = handler.test(adventiveNote);
            boolean keepon = mediaObject.modifyCount();
            adventiveNote.setFettle(success ? NOTE_FETTLE_SUCCESS : (keepon ? NOTE_FETTLE_ING : NOTE_FETTLE_GIVEUP));

            return NOTE_FETTLE_ING - adventiveNote.getFettle() != 0 ;

        } catch (Exception e) {
            logger.error("推送异常 {}", mediaObject);
            mediaObject.setErrors(mediaObject.getErrors() + 1);
            if(!(over = mediaObject.getErrors() <= 3)){
                adventiveNote.setRemark(ERRORMESSAGE);
            }
        }finally {
            persistencer.accept(adventiveNote);
        }
        return over;
    }

    /**
     * @param mediaObject 介质对象
     * @param immediately true:立刻执行,false:正常等待
     */
    public void dispatch(final MediaObject mediaObject, boolean immediately){
        if(mediaObject != null){
            //recorder.accept(mediaObject.getTarget());
            if(immediately){
                mediaObject.immediately();
            }
            cacheDeque.put(mediaObject);
            //logger.info("还有 {} 等待推送", cacheDeque.size());
        }
    }
    public void dispatch(final MediaObject mediaObject){
        dispatch(mediaObject, false);
    }
    /*
    public void dispatch(final Adventive adventive, final AdventiveNote adventiveNote){
        if(adventiveNote != null){
            dispatch(new MediaObject(adventiveNote, adventive.getFrequency(), adventive.getFrequencyType(), adventive.getMaxTimes()), true);
        }
    }
    */

    public void dispatch(final PossibleObject possibleObject){
        Globallys.executor(() -> {
            Collection<MediaObject> collection = filter.apply(possibleObject);
            if(CollectionUtils.isNotEmpty(collection)){
                for (MediaObject mediaObject : collection) {
                    dispatch(mediaObject, true);
                }
            }
        });
    }
}
