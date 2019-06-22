package com.ycg.ksh.api;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

public class CustomContextListener extends org.springframework.web.context.ContextLoaderListener {
	
	protected final Logger logger = LoggerFactory.getLogger(CustomContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.debug("========================开始初始化========================");
		SystemUtils.put(SystemKey.SYS_CURRENT_ENV, event.getServletContext().getInitParameter("spring.profiles.active"));
		SystemUtils.put(SystemKey.SYS_LOCALHOST, NetUtils.getLocalHost());
		super.contextInitialized(event);
		logger.debug("========================初始化完成========================");
	}
}
