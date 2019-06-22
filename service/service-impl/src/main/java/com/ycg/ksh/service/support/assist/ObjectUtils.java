package com.ycg.ksh.service.support.assist;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */

import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.entity.service.enterprise.PropertyDescribe;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 对象工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public class ObjectUtils {

    public static <T> T newInstance(String className) throws Exception{
        return newInstance((Class<T>) Class.forName(className));
    }

    public static <T> T newInstance(Class<T> clazz) throws Exception {
        return clazz.newInstance();
    }

    private static Stream<PropertyDescribe> filter(Class<?> clazz, Collection<PropertyDescribe> propertyDescribes){
        return propertyDescribes.stream().filter(pd -> {
            return clazz.getName().equalsIgnoreCase(pd.getClassName());
        });
    }

    public static <T> T reduceObject(Class<T> clazz, Collection<PropertyDescribe> propertyDescribes) throws Exception{
        return reduceObject(clazz, propertyDescribes, null);
    }

    public static <T> T reduceObject(Class<T> clazz, Collection<PropertyDescribe> propertyDescribes, Consumer<PropertyDescribe> consumer) throws Exception {
        T object = null;
        BeanUtilsBean beanUtils = BeanUtils.build();
        for (PropertyDescribe propertyDescribe : propertyDescribes) {
            if(!clazz.getName().equalsIgnoreCase(propertyDescribe.getClassName())){
                continue;
            }
            if(consumer != null){
                consumer.accept(propertyDescribe);
            }
            for (Map.Entry<String, Object> entry : propertyDescribe.getProperties().entrySet()) {
                if(entry.getValue() == null){
                    continue;
                }
                if(object == null){
                    object = newInstance(clazz);
                }else{
                    Object value = beanUtils.getProperty(object, entry.getKey());
                    if(value != null){
                        continue;
                    }
                }
                beanUtils.setProperty(object, entry.getKey(), entry.getValue());
            }
        }
        return object;
    }

    public static <T> T collectObject(Class<T> clazz, Collection<PropertyDescribe> propertyDescribes, BinaryOperator<T> function) throws Exception {
        T object = null;
        BeanUtilsBean beanUtils = BeanUtils.build();
        for (PropertyDescribe propertyDescribe : propertyDescribes) {
            if(!clazz.getName().equalsIgnoreCase(propertyDescribe.getClassName())){
                continue;
            }
            T temp = newInstance(clazz);
            beanUtils.populate(temp, propertyDescribe.getProperties());
            if(object == null){
                object = newInstance(clazz);
            }
            object = function.apply(object, temp);
        }
        return object;
    }

    public static <T> Collection<T> collectObjects(Class<T> clazz, Collection<PropertyDescribe> propertyDescribes) throws Exception {
        BeanUtilsBean beanUtils = BeanUtils.build();
        Collection<T> collection = new ArrayList<T>();
        for (PropertyDescribe propertyDescribe : propertyDescribes) {
            if(!clazz.getName().equalsIgnoreCase(propertyDescribe.getClassName())){
                continue;
            }
            T object = newInstance(clazz);
            beanUtils.populate(object, propertyDescribe.getProperties());
            collection.add(object);
        }
        return collection;
    }
}
