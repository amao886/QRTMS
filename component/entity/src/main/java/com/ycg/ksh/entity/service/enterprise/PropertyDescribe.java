package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */

import java.util.HashMap;
import java.util.Map;

/**
 * 属性描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public class PropertyDescribe {

    private boolean custom = false;
    /**
     * 字段所属类
     */
    private String className;
    /**
     * 属性和属性值
     */
    private Map<String, Object> properties;//key=字段属性名,value=字段属性值


    public PropertyDescribe() {
    }

    public PropertyDescribe(String className, boolean custom) {
        this.className = className;
        this.custom = custom;
    }

    public void set(String key, Object value){
        if(properties == null){
            properties = new HashMap<String, Object>();
        }
        Object v = properties.get(key);
        if(v == null && value != null){
            properties.put(key, value);
        }
    }
    public void delete(String key){
        if(properties != null){
            properties.remove(key);
        }
    }

    public <T> T get(String key){
        if(properties != null){
            Object object = properties.get(key);
            if(object != null){
                return (T) object;
            }
        }
        return null;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
