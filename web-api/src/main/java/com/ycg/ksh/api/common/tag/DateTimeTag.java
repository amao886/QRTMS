package com.ycg.ksh.api.common.tag;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Date;

public class DateTimeTag extends SimpleTagSupport {

	protected final Logger logger = LoggerFactory.getLogger(DateTimeTag.class);
	
	
	private static final String DEFAULT_PATTERN ="yyyy-MM-dd HH:mm:ss";
	
	private Date date;
	private Long millis;
	private String pattern;

	@Override
	public void doTag() throws JspException, IOException {
		if(pattern == null) {
			pattern = DEFAULT_PATTERN;
		}
		try {
			String s = "";
			if(date != null) {
				s = DateFormatUtils.format(date, pattern);
			}else if(millis != null && millis>0) {
				s = DateFormatUtils.format(millis, pattern);
			}
			/*
			long time = Long.valueOf(String.valueOf(value));
			LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId());
			s = localDateTime.format(DateTimeFormatter.ofPattern(pattern));
			*/
			getJspContext().getOut().write(s);
		} catch (IOException e) {
			logger.error("自定义标签解析异常  date {} millis {} pattern {}", date, millis , pattern, e);
		}
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMillis(Long millis) {
		this.millis = millis;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
