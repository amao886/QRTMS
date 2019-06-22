package com.ycg.ksh.common.dubbo;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class Service {

	/** slf4j logger api */
	protected static final Logger logger = LoggerFactory.getLogger(Service.class);
    
	private static volatile boolean running = true;

	private String serviceName;
	private String activeProfile;

    private String projectDic;
    private String localhost;

    public Service(String serviceName, String activeProfile) {
        this.serviceName = serviceName;
        this.activeProfile = Optional.ofNullable(activeProfile).orElseThrow(() -> new BusinessException("环境变量参数不能为空"));

        this.projectDic = Optional.ofNullable(Service.class.getResource("/")).map(r->new File(r.getFile()).getParent()).orElse(System.getProperty("user.dir"));
        this.localhost = NetUtils.getLocalHost();

        logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("SERVICENAME -> {}", serviceName);
        logger.info("ACTIVEPROFILE -> {}", activeProfile);
        logger.info("LOCALHOST -> {}", localhost);
        logger.info("PROJECTDIC -> {}", projectDic);
        logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public void execute(){
        try {
            long beginTime = System.currentTimeMillis();
            SystemUtils.put(SystemKey.SYS_PROJECT_DIC, projectDic);
            SystemUtils.put(SystemKey.SYS_LOCALHOST, localhost);
            SystemUtils.put(SystemKey.SYS_CURRENT_ENV, activeProfile);
            Globallys.initializeIDBuilder(localhost);

            final GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext();
            applicationContext.getEnvironment().setActiveProfiles(activeProfile);
            applicationContext.load("classpath:spring.xml");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    applicationContext.stop();
                    applicationContext.close();
                    logger.info("======Dubbo {} server stopped {}======", serviceName, new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()));
                } catch (Throwable t) {
                    logger.error(t.getMessage(), t);
                }
                synchronized (Service.class) {
                    running = false;
                }
            }));
            applicationContext.refresh();
            applicationContext.start();

            Map<String, ServiceMonitor> serviceMonitors = applicationContext.getBeansOfType(ServiceMonitor.class);
            if(serviceMonitors != null && !serviceMonitors.isEmpty()){
                for (ServiceMonitor serviceMonitor : serviceMonitors.values()) {
                    serviceMonitor.onServerStrated(applicationContext, activeProfile);
                }
            }
            logger.info("======Dubbo {} server[{}] started in: {} ms======", serviceName, activeProfile, (System.currentTimeMillis() - beginTime));
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
        synchronized (Service.class) {
            while (running) {
                try {
                    Service.class.wait();
                } catch (Throwable e) { }
            }
        }
    }
}