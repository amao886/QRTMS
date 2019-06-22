package com.ycg.ksh.common.extend.spring;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间和字符串双向转换
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-14 16:12:14
 */
public class DateStringFormatter implements Formatter<java.util.Date> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pattern;
	
	private String[] patterns = new String[]{ "yyyy-MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss" };
	
	public DateStringFormatter(String pattern) {
		super();
		this.pattern = pattern;
	}

	/*
	  * <p>将时间转换成字符串</p>
	  * @param object  日期时间
	  * @param locale
	  * @return  格式化之后的日期时间字符串
	  * @see org.springframework.format.Printer#print(java.lang.Object, java.util.Locale)
	  */
	@Override
	public String print(Date object, Locale locale) {
		String dateString = null;
		try {
			dateString = DateFormatUtils.format(object, pattern, locale);
			logger.info("Date to String [{}] -> [{}]", object, dateString);
		} catch (Exception e) {
			logger.error("Date to String [{}]", object, e);
		}
		return dateString;
	}

	/*
	  * <p>将字符串转换成时间</p>
	  * @param text  时间日期字符串
	  * @param locale
	  * @return 转换后的时间对象
	  * @throws ParseException
	  * @see org.springframework.format.Parser#parse(java.lang.String, java.util.Locale)
	  */
	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		Date date = null;
		try {
			date = DateUtils.parseDateStrictly(text, patterns);
			logger.info("String to Date [{}] -> [{}]", text, date);
		} catch (Exception e) {
			logger.error("String to Date [{}]", text, e);
		}
		return date;
	}

}
