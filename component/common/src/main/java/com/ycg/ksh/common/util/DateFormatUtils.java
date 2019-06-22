/**
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:41:30
 */
package com.ycg.ksh.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:41:30
 */
public class DateFormatUtils extends org.apache.commons.lang.time.DateFormatUtils {

	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String format(Date date) {
		return format(date, DEFAULT_FORMAT);
	}
	
	public static String format(Calendar cal) {
		return format(cal, DEFAULT_FORMAT);
	}
}
