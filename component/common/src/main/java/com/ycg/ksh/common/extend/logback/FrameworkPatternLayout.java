package com.ycg.ksh.common.extend.logback;

import ch.qos.logback.classic.PatternLayout;
import com.ycg.ksh.common.extend.logback.pattern.HostConverter;
import com.ycg.ksh.common.extend.logback.pattern.OwerConverter;

public class FrameworkPatternLayout extends PatternLayout {

	static{
		defaultConverterMap.put("host", HostConverter.class.getName());
		defaultConverterMap.put("ower", OwerConverter.class.getName());
	}
}
