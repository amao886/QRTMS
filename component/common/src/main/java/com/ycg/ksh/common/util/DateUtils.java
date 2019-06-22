package com.ycg.ksh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");

    public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long DAY_IN_MILLIS = 86400000L;
    private static final long HOUR_IN_MILLIS = 3600000L;
    private static final long MINUTE_IN_MILLIS = 60000L;
    private static final long SECOND_IN_MILLIS = 1000L;

    private static SimpleDateFormat getSDFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static Date minOfDay(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return minOfDay(cal);
        }
        return null;
    }

    public static Date maxOfDay(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return maxOfDay(cal);
        }
        return null;
    }


    /**
     * 获取一天的最小时间
     *
     * @param cal
     * @return
     */
    public static Date minOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取一天的最大时间
     *
     * @param cal
     * @return
     */
    public static Date maxOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }


    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Calendar getCalendar(long millis) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date(millis));
        return cal;
    }

    public static Date getDate() {
        return new Date();
    }

    public static Date getDate(long millis) {
        return new Date(millis);
    }

    public static String timestamptoStr(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date);
    }

    public static Timestamp str2Timestamp(String str) {
        Date date = str2Date(str, date_sdf);
        return new Timestamp(date.getTime());
    }

    public static Date str2Date(String str, SimpleDateFormat sdf) {
        if ((null == str) || ("".equals(str))) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String date2Str(Date date) {
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    public static String dataformat(String data, String format) {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sformat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sformat.format(date);
    }

    public static String date2Str(Date date, SimpleDateFormat date_sdf) {
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    public static String getDateTime() {
        Date date = new Date();
        return datetimeFormat.format(date);
    }

    public static String getDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }

    public static Timestamp getTimestamp(String time) {
        return new Timestamp(Long.parseLong(time));
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Timestamp getCalendarTimestamp(Calendar cal) {
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp gettimestamp() {
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(dt);
        Timestamp buydate = Timestamp.valueOf(nowTime);
        return buydate;
    }

    public static long getMillis() {
        return new Date().getTime();
    }

    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    public static long getMillis(Date date) {
        return date.getTime();
    }

    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }

    public static String formatDate() {
        return date_sdf.format(getCalendar().getTime());
    }

    public static String formatYmdCN(Date date) {
        return date_sdf_wz.format(date);
    }

    public static String formatDateymd() {
        return yyyyMMdd.format(getCalendar().getTime());
    }

    public static String getDataString(SimpleDateFormat formatstr) {
        return formatstr.format(getCalendar().getTime());
    }

    public static String formatDate(Calendar cal) {
        return date_sdf.format(cal.getTime());
    }

    public static String formatDate(Date date) {
        return date_sdf.format(date);
    }

    public static String formatDate(long millis) {
        return date_sdf.format(new Date(millis));
    }

    public static String formatDate(String pattern) {
        return getSDFormat(pattern).format(getCalendar().getTime());
    }

    public static String formatDate(Calendar cal, String pattern) {
        return getSDFormat(pattern).format(cal.getTime());
    }

    /**
     * 将date日期转换指定格式字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        return getSDFormat(pattern).format(date);
    }

    public static String formatTime() {
        return time_sdf.format(getCalendar().getTime());
    }

    public static String formatTime(long millis) {
        return time_sdf.format(new Date(millis));
    }

    public static String formatTime(Calendar cal) {
        return time_sdf.format(cal.getTime());
    }

    public static String formatTime(Date date) {
        return time_sdf.format(date);
    }

    public static String formatShortTime() {
        return short_time_sdf.format(getCalendar().getTime());
    }

    public static String formatShortTime(long millis) {
        return short_time_sdf.format(new Date(millis));
    }

    public static String formatShortTime(Calendar cal) {
        return short_time_sdf.format(cal.getTime());
    }

    public static String formatShortTime(Date date) {
        return short_time_sdf.format(date);
    }

    public static Date parseDate(String src, String pattern) throws ParseException {
        return getSDFormat(pattern).parse(src);
    }

    public static Calendar parseCalendar(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
        Calendar cal = parseCalendar(src, pattern);
        cal.add(5, amount);
        return formatDate(cal);
    }

    public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }

    public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {
        long millisDiff = getMillis(calSrc) - getMillis(calDes);

        if (flag == 'y') {
            return calSrc.get(1) - calDes.get(1);
        }

        if (flag == 'd') {
            return (int) (millisDiff / DAY_IN_MILLIS);
        }

        if (flag == 'h') {
            return (int) (millisDiff / HOUR_IN_MILLIS);
        }

        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_IN_MILLIS);
        }

        if (flag == 's') {
            return (int) (millisDiff / SECOND_IN_MILLIS);
        }

        return 0;
    }

    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(1);
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(5, 1);
        System.out.println(cDay.getTime());
        return cDay.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(5, cDay.getActualMaximum(5));
        System.out.println(cDay.getTime());
        return cDay.getTime();
    }

    public static Date getDateBeforeYear(Date date, int year) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(1, year);
        return dateTime.getTime();
    }

    public static Date getDateBeforeMonth(Date date, int month) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(2, month);
        return dateTime.getTime();
    }

    public static Date getDateAfterMonth(Date date, int m) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(2, -m);
        return dateTime.getTime();
    }

    public static Date getDateBeforeDay(Date date, int day) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(5, day);
        return dateTime.getTime();
    }

    public static Date getDateAfterDay(Date date, int day) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(5, -day);
        return dateTime.getTime();
    }

    public static Date getDateAfterHour(Date date, int hour) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(10, hour);
        return dateTime.getTime();
    }

    public static Date getDateAfterMinute(Date date, int minute) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(12, minute);
        return dateTime.getTime();
    }

    public static Date getDateAfterSeconds(Date date, int second) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(13, second);
        return dateTime.getTime();
    }

    public static Date getDateBeforeSeconds(Date date, int second) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(13, -second);
        return dateTime.getTime();
    }

    public static Date getEndDayOfUpMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 0);
        return calendar.getTime();
    }

    public static Date getFirstDayOfUpMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, -1);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static int daysDiffer(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int day1 = calendar.get(6);
        calendar.setTime(date2);
        int day2 = calendar.get(6);
        return day2 - day1;
    }


    public static int differentDays(Calendar cal, Date date1, Date date2) {
        cal.setTime(date1);
        int year1 = cal.get(Calendar.YEAR);
        int day1 = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(date2);
        int year2 = cal.get(Calendar.YEAR);
        int day2 = cal.get(Calendar.DAY_OF_YEAR);
        if (year1 != year2) {   //同一年
            int timeDistance = 0;
            for (int i = Math.min(year1, year2); i < Math.max(year1, year2); i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { //闰年
                    timeDistance += 366;
                } else {//不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (Math.abs(day2 - day1));
        } else {//不同年
            return Math.abs(day2 - day1);
        }
    }

    public static int differentDays(Date date1, Date date2) {
        return differentDays(Calendar.getInstance(), date1, date2);
    }

    public static int getIntervalDays(Date date1, Date date2) {
        if ((null == date1) || (null == date2)) {
            return -1;
        }
        long intervalMilli = date2.getTime() - date1.getTime();
        return (int) (intervalMilli / DAY_IN_MILLIS);
    }

    public static long getIntervalTimes(Date date1, Date date2) {
        if ((null == date1) || (null == date2)) {
            return -1L;
        }
        return date2.getTime() - date1.getTime();
    }

    public static double getIntervalHours(Date date1, Date date2) {
        return getIntervalTimes(date1, date2) / 3600000.0D;
    }

    public static Date setDateTime(Date date, int hour, int minute, int second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.set(10, hour);
        dateTime.set(12, minute);
        dateTime.set(13, second);
        return dateTime.getTime();
    }

    public static String getTimeFromMinute(int minute) {
        return getTimeFromMinute(minute, "HH:mm");
    }

    private static String getTimeFromMinute(int minute, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = setDateTime(new Date(), 0, 0, 0);
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTime(date);
        dateTime.add(12, minute);
        return sdf.format(dateTime.getTime());
    }

    public static String getDetailTimeByDate(Integer minute) {
        StringBuffer timeString = new StringBuffer();
        if (minute == null) {
            return "";
        }
        if (minute.intValue() >= 1440) {
            int day = minute.intValue() / 1440;
            int nextMin = minute.intValue() - day * 1440;
            int hour = nextMin / 60;
            int min = nextMin - hour * 60;
            timeString.append(day + "天");
            if ((hour != 0) || (min != 0)) {
                timeString.append(hour + "小时");
            }
            if (min != 0)
                timeString.append(min + "分钟");
        } else if (minute.intValue() >= 60) {
            int hour = minute.intValue() / 60;
            int min = minute.intValue() - hour * 60;
            timeString.append(hour + "小时");
            if (min != 0)
                timeString.append(min + "分钟");
        } else {
            timeString.append(minute + "分钟");
        }
        return timeString.toString();
    }

    public static String formatDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        if (date != null) {
            dateStr = format.format(date);
        }
        return dateStr;
    }

    public static String formatTimeToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String dateStr = "";
        if (date != null) {
            dateStr = format.format(date);
        }
        return dateStr;
    }

    public static String formatDateToSimple(Date date) {
        String customDate = "";
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        customDate = sdf.format(date);
        return customDate;
    }

    /**
     * 字符串转换指定日期格式
     * 字符串为空返回当前日期格式不进行转换
     *
     * @param strDate
     * @param format
     * @return
     */
    public static Date parseToDate(String strDate, String format) {
        try {
            if ((strDate == null) || (strDate.trim().length() == 0)) {
                return new Date();
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(strDate);
        } catch (ParseException e) {
            logger.error("[ERROR：时间转换失败;]", e);
        }
        return new Date();
    }

    public static Date parseToDate(String strDate) {
        return parseToDate(strDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDateAddTime(Date date, Date time) {
        String d1 = formatDate(date, "yyyy/MM/dd");
        String d2 = formatDate(time, "HH:mm:ss");
        Date allDate = parseToDate(d1 + " " + d2, "yyyy/MM/dd HH:mm:ss");
        return allDate;
    }

    /**
     * 字符串转换日期，格式：yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static Date strDate(String str) {
        return str2Date(str, date_sdf);
    }

    /**
     * 字符串转换日期时间，格式：yyyy-MM-dd HH:mm
     *
     * @param str
     * @return
     */
    public static Date strTimeDate(String str) {
        return str2Date(str, time_sdf);
    }

    /**
     * 日期转换字符串，格式：yyyy-MM-dd HH:mm
     *
     * @param Date
     * @return
     */
    public static String timeToString(Date date) {
        String dastr = null;
        if (date != null) {
            dastr = time_sdf.format(date);
        }
        return dastr;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        String result = date_sdf.format(today);
        return result;
    }

    // 获取月的第一天转换日期
    public static String getMonthFirstDay() {
        Calendar cale = getCalendar();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return date_sdf.format(cale.getTime());
    }

    // 获取月的最后一天转换日期
    public static String getMonthLastDay() {
        Calendar cale = getCalendar();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return date_sdf.format(cale.getTime());
    }

    // 获取指定日期间隔多少天的日期
    public static String getSpecifiedDayAfter(String specifiedDay, int interval) throws ParseException {
        Calendar c = getCalendar();
        Date date = date_sdf.parse(specifiedDay);
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + interval);
        return date_sdf.format(c.getTime());
    }

    // 获取指定日期的小时数
    public static int getSpecifiedDayHour(String specifiedDay) throws ParseException {
        Calendar c = getCalendar();
        Date date = datetimeFormat.parse(specifiedDay);
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    //比较时间大小
    public static boolean dateCompareTo(Date date1, Date date2) {
        boolean flag = false;
        if (date2 == null) {
            flag = true;
        } else {
            long startTime = date1.getTime();
            long endTime = date2.getTime();
            if (startTime >= endTime) {
                flag = true;
            }
        }
        return flag;
    }

    //当前时间和传递时间比较
    public static boolean dateCompareTo(Date date1) {
        boolean flag = true;
        Calendar cal = Calendar.getInstance();
        long ctime = cal.getTimeInMillis();
        cal.setTime(date1);
        if (cal.getTimeInMillis() < ctime) {
            flag = false;
        }
        return flag;
    }

    /**
     * 当前天数减去指定天数
     *
     * @param days      天数
     * @param formatter 日期指定格式化
     * @return
     */
    public static String getMinusTime(long days, DateTimeFormatter formatter) {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusDays(days);
        return localDate.format(formatter);
    }

    /**
     * 当前天数加上指定天数
     *
     * @param days      天数
     * @param formatter 日期指定格式化
     * @return
     */
    public static String getPlusTime(long days, DateTimeFormatter formatter) {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(days);
        return localDate.format(formatter);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        smdate = date_sdf.parse(date_sdf.format(smdate));
        bdate = date_sdf.parse(date_sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(date_sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前月第一天
     */
    public static String firstDayOfMonth() {
        LocalDate localDate = LocalDate.now();
        return localDate.with(TemporalAdjusters.firstDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 获取当前月最后一天
     */
    public static String lastDayOfMonth() {
        LocalDate localDate = LocalDate.now();
        return localDate.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 根据指定时间 加减 返回月份
     *
     * @param date    日期
     * @param Integer addSubtract
     * @return 返回月份
     */
    public static int getMonth(Date date, Integer addSubtract) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.MONTH) + 1;
    }

    /**
     * 根据指定时间返回月份
     *
     * @param date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.YEAR);
    }

    /**
     * 获取两个时间之间的日期
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<Date> dateSplit(Date startDate, Date endDate)
            throws Exception {
        if (!startDate.before(endDate))
            throw new Exception("开始时间应该在结束时间之后");
        Long spi = endDate.getTime() - startDate.getTime();
        Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

        List<Date> dateList = new ArrayList<Date>();
        dateList.add(endDate);
        for (int i = 1; i <= step; i++) {
            dateList.add(new Date(dateList.get(i - 1).getTime()
                    - (24 * 60 * 60 * 1000)));// 比上一天减一
        }
        return dateList;
    }

    public static List<Date> getDateList() throws Exception {
        Date start = date_sdf.parse(DateUtils.getPastDate(6));
        Date end = date_sdf.parse(DateUtils.getPastDate(0));
        return dateSplit(start, end);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime;
    }


    public static LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

}
