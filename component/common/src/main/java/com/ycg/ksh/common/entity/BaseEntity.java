package com.ycg.ksh.common.entity;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.IOUtils;
import com.ycg.ksh.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础实体类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:21:33
 */
public class BaseEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = -6510350532201986247L;

	@Override
	public String toString() {
		return StringUtils.toString(this);
	}
	
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(this.getClass())) {
                if("class".equals(propertyDescriptor.getName())) {
                    continue;
                }
                Method readMethod = propertyDescriptor.getReadMethod();
                if(readMethod != null) {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    map.put(propertyDescriptor.getName(), readMethod.invoke(this));
                }
            }
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public Map<String, Object> map(String key, Object value){
		Map<String, Object> map = toMap();
		if(key != null && value != null) {
			map.put(key, value);
		}
		return map;
	}
	
	public void copyProperties(BaseEntity entity){
		try {
			BeanUtils.copyProperties(entity, this);
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}

	public <T extends Object> T deepCopy() throws BusinessException{
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			oos.flush();
			bis = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bis);
			return (T) ois.readObject();
		} catch (Exception e){
			throw new BusinessException("对象复制异常:"+ this.getClass().getName(), e);
		}finally {
			IOUtils.closeQuietly(oos, baos, bis, ois);
		}
	}


	public <T extends BaseEntity> T copy(){
		T o = null;
		try{
			o = (T)clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
