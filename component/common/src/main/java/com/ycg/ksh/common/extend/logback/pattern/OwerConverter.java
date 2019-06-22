package com.ycg.ksh.common.extend.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

public class OwerConverter extends ClassicConverter {

	private static final String KEY = "wclient";
	
	@Override
	public String convert(ILoggingEvent event) {
		String wclient = MDC.get(KEY);
		if(wclient == null) {
			wclient = "T-"+ Thread.currentThread().getId();
		}
		return wclient;
	}

}
