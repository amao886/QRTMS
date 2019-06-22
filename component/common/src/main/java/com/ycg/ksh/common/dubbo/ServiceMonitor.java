package com.ycg.ksh.common.dubbo;

import com.ycg.ksh.common.exception.BusinessException;
import org.springframework.context.ApplicationContext;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/16
 */
public interface ServiceMonitor {

    /**
     * 服务启动完成
     * @param context
     * @param activeProfile
     * @throws BusinessException
     */
    void onServerStrated(ApplicationContext context, String activeProfile) throws BusinessException;

}
