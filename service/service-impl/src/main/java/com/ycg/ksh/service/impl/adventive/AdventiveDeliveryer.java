package com.ycg.ksh.service.impl.adventive;

import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

/**
 * 对外处理器
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/25
 */
public interface AdventiveDeliveryer {

    final Logger logger = LoggerFactory.getLogger(AdventiveDeliveryer.class);

    final String SUCCESS = "success";

    final DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    final DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("HHmmss");

    String OBJECT_TYPE = "ADVENTIVE_OBJECT_TYPE";
    String OPPOSITE_KEY = "ADVENTIVE_OPPOSITE_KEY";

    /**
     * 是否是默认的
     * @return
     */
    default boolean isDefault() {
        return false;
    };
    /**
     * 验证是否可以处理
     * @param adventive
     * @param adventiveNote
     * @return
     */
    boolean validate(Adventive adventive, AdventiveNote adventiveNote);
    /**
     * 推送处理
     * @param adventive
     * @param adventiveNote
     * @param object
     * @return
     */
    boolean process(Adventive adventive, AdventiveNote adventiveNote, Object object);
}
