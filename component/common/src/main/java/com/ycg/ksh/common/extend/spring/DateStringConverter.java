package com.ycg.ksh.common.extend.spring;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/06 0006
 */

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 日期转换器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/06 0006
 */
public class DateStringConverter implements Converter<String, Date> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String[] patterns = new String[]{ "yyyy-MM-dd",  "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss" };

    @Override
    public Date convert(String source) {
        Date date = null;
        try {
            date = DateUtils.parseDateStrictly(source, patterns);;
            logger.info("String to Date [{}] -> [{}]", source, date);
        } catch (Exception e) {
            logger.error("String to Date [{}]", source, e);
        }
        return date;
    }
}
