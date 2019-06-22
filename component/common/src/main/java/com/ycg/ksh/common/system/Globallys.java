package com.ycg.ksh.common.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.StringUtils;
import javafx.scene.shape.HLineTo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public abstract class Globallys {

    private static final Pattern PATTERN = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");


    public static DateTimeFormatter DF_YM = DateTimeFormatter.ofPattern("yyyyMM");
    public static DateTimeFormatter DF_YMD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter DF_YMRHMS = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    public static DateTimeFormatter DF_L_YM = DateTimeFormatter.ofPattern("yyyy-MM");
    public static DateTimeFormatter DF_L_YMD  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter DF_L_YMRHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ExecutorService executor;

    private static GloballyIDBuilder idBuilder;

    private static ThreadLocal<Map<Serializable, Object>> local = ThreadLocal.withInitial(HashMap::new);


    public static synchronized ExecutorService executor(){
        if(executor == null){
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }

    public static void executor(Runnable runnable){
        executor().execute(runnable);
    }

    private enum Singleton{

        OBJECTMAPPER;

        private com.fasterxml.jackson.databind.ObjectMapper instance;

        private Singleton(){
            instance = new com.fasterxml.jackson.databind.ObjectMapper();
        }

        public com.fasterxml.jackson.databind.ObjectMapper getInstance(){
            return instance;
        }
    }

    public static com.fasterxml.jackson.databind.ObjectMapper objectMapper(){
        return Singleton.OBJECTMAPPER.getInstance();
    }

    public static com.fasterxml.jackson.databind.ObjectMapper objectMapper(boolean create){
        return create ? new com.fasterxml.jackson.databind.ObjectMapper() : objectMapper();
    }

    public static ObjectNode createObjectNode(){
        return objectMapper().createObjectNode();
    }

    public static String toJsonString(Object object){
        return JSON.toJSONString(object);
    }

    public static JSONObject createJsonObject(){
        return new JSONObject();
    }

    public static <T> Collection<T> toJavaObjects(String jsonString, Class<T> beanClazz){
        try{
            return JSONArray.parseArray(jsonString, beanClazz);
        }catch (Exception e){
            throw new ParameterException("必须是一个正确的数组JSON格式字符串");
        }
    }

    public static <T> T toJavaObject(String jsonString, Class<T> clazz){
        try{
            return JSONObject.parseObject(jsonString, clazz);
        }catch (Exception e){
            throw new ParameterException("必须是一个正确的JSON格式字符串");
        }
    }

    /**
     * 根据本地机器IP初始化ID生成器
     * <p>
     * @param localIp
     */
    public static void initializeIDBuilder(String localIp) {
        if(PATTERN.matcher(localIp).matches()) {
            String[] ipitems = localIp.split("\\.");
            long datacenter = Long.parseLong(ipitems[0]) + Long.parseLong(ipitems[1]);
            long worker = Long.parseLong(ipitems[2]) + Long.parseLong(ipitems[3]);
            idBuilder = new GloballyIDBuilder(datacenter, worker);
        }else {
            idBuilder = new GloballyIDBuilder(1, 1);
        }
    }

    public static long nextKey(){
        return idBuilder.next();
    }

    public static String next(){
        return String.valueOf(idBuilder.next());
    }
    public static String UUID(){
        return StringUtils.upperCase(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
    }

    /**
     * 线程级别的记忆存储
     * @param srorageKey 存储key
     * @param object   存储的value
     */
    public static void memoryStorage(Serializable srorageKey, Object object){
        local.get().put(srorageKey, object);
    }

    /**
     * 获取线程级别的存储数据
     * @param srorageKey 存储key
     * @return
     * @param <T>
     *
     */
    public static <T> T memoryStorage(Serializable srorageKey){
        Object o = local.get().get(srorageKey);
        if(o != null){
            return (T) o;
        }
        return null;
    }
}
