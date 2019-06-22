package com.ycg.ksh.common.entity;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 通用实体类，主要用于spring mvc接收参数用
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:24:37
 */
public class RequestObject extends HashMap<String, String> {

    private static final long serialVersionUID = -1500586863456655586L;

    public static final RequestObject EMPTY = new RequestObject(Collections.EMPTY_MAP);

    private static final String[] DATE_FORMATS = new String[]{
            "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
            "yyyyMMdd HH", "yyyy-MM-dd HH", "yyyy/MM/dd HH",
            "yyyyMMdd HH:mm", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm",
            "yyyyMMdd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"
    };

    public RequestObject() {
        super();
    }

    public RequestObject(Map<String, String[]> parameterMap) {
        this();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getValue().length == 1) {
                put(entry.getKey(), entry.getValue()[0]);
            } else {
                put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
            }
        }
    }

    /**
     * @param key
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-25 14:49:11
     * @see java.util.HashMap#get(java.lang.Object)
     * <p>
     */
    @Override
    public String get(Object key) {
        Object value = super.get(key);
        if (value != null) {
            return StringUtils.trimToEmpty(value.toString());
        }
        return null;
    }

    /**
     * 判断指定key的值是否为空
     * <p>
     *
     * @param key
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:25:32
     */
    public boolean stringIsNull(String key) {
        return StringUtils.isBlank(get(key));
    }

    /**
     * 判断指定key的值是否为空，如果为空就抛出异常
     * <p>
     *
     * @param key
     * @param message 异常信息
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:25:36
     */
    public boolean stringIsNull(String key, String message) {
        String value = get(key);
        if (StringUtils.isBlank(value)) {
            throw new ParameterException(value, message);
        }
        return true;
    }

    /**
     * 赋值
     * <p>
     *
     * @param key
     * @param object
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:25:40
     */
    public void put(String key, Object object) {
        if (object != null) {
            put(key, String.valueOf(object));
        }
    }

    /**
     * 获取Integer类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:25:43
     */
    public Integer getInteger(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ParameterException("[" + pname + "]必须是一个有效的整数");
            }
        }
        return null;
    }

    /**
     * 获取Long类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:25:49
     */
    public Long getLong(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ParameterException("[" + pname + "]必须是一个有效的整数");
            }
        }
        return null;
    }

    /**
     * 获取Float类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:25:52
     */
    public Float getFloat(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                throw new ParameterException("[" + pname + "]必须是一个有效的数字");
            }
        }
        return null;
    }

    /**
     * 获取double类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:26:02
     */
    public Double getDouble(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new ParameterException("[" + pname + "]必须是一个有效的数字");
            }
        }
        return null;
    }

    /**
     * 获取Short类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:26:05
     */
    public Short getShort(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            return Short.parseShort(value);
        }
        return null;
    }

    /**
     * 获取Boolean类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:26:08
     */
    public Boolean getBoolean(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    /**
     * 获取时间类型的参数
     * <p>
     *
     * @param pname
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-30 08:26:11
     */
    public Date getDate(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            try {
                if (StringUtils.isNumeric(value)) {
                    return new Date(Long.parseLong(value));
                }
                return DateUtils.parseDate(value, DATE_FORMATS);
            } catch (ParseException e) {
                throw new ParameterException("[" + pname + "]必须是一个有效的日期");
            }
        }
        return null;
    }


    public LocalDateTime getLocalDateTime(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            if (value.length() < 19) {
                return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            } else {
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        }
        return null;
    }

    public LocalDate getLocalDate(String pname) {
        String value = get(pname);
        if (StringUtils.isNotBlank(value)) {
            return LocalDate.parse(value);
        }
        return null;
    }

    public <T extends BaseEntity> T toJavaBean(final Supplier<T> supplier) throws ReflectiveOperationException {
        T instance = supplier.get();
        if (size() > 0) {
            BeanUtils.build().populate(instance, this);
        }
        return instance;
    }

    public <T extends BaseEntity> T toJavaBean(Class<T> clazz) throws ReflectiveOperationException {
        T instance = clazz.newInstance();
        if (size() > 0) {
            BeanUtils.build().populate(instance, this);
        }
        return instance;
    }
}
