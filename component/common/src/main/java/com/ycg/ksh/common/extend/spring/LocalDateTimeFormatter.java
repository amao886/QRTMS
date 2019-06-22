package com.ycg.ksh.common.extend.spring;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * 日期时间和字符串双向转换
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-14 16:12:14
 */
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public String print(LocalDateTime dateTime, Locale locale) {
		String dateString = null;
		try {
			dateString = Optional.ofNullable(dateTime).map(d->dateTime.format(format)).orElse(null);
			logger.info("LocalDateTime to String [{}] -> [{}]", dateTime, dateString);
		} catch (Exception e) {
			logger.error("LocalDateTime to String [{}]", dateTime, e);
		}
		return dateString;
	}

	@Override
	public LocalDateTime parse(String text, Locale locale) throws ParseException {
		LocalDateTime dateTime = null;
		try {
			dateTime = Optional.ofNullable(text).map(s->LocalDateTime.parse(s, format)).orElse(null);
			logger.info("String to LocalDateTime [{}] -> [{}]", text, dateTime);
		} catch (Exception e) {
			logger.error("String to LocalDateTime [{}]", text, e);
		}
		return dateTime;
	}

}
