/**
 * Copyright (c) 2014 ShanghaiMed iKang,Inc. All rights reserved.
 */
package com.ycg.ksh.common.util;

import com.ycg.ksh.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:xuefeng.ding@daoyitong.com">DingXuefeng</a>
 * @date 2016-09-21 11:09:36
 */
public abstract class XmlUtils {

	
	public static String trim(String source){
		if(source != null){
			return source.trim();
		}
		return "";
	}

	public static Element root(String xmlString) throws BusinessException{
		if(!StringUtils.isEmpty(xmlString)){
			try {
				return new SAXReader().read(new StringReader(xmlString)).getRootElement();
			}catch (Exception e) {
				throw new BusinessException("xml数据解析异常\""+ xmlString +"\"", e);
			}
		}else{
			throw new BusinessException("返回异常["+ xmlString +"]");
		}
	}
	
	public static Collection<Element> collection(Element element, Collection<Element> objects){
		for (Object object : element.elements()) {
			objects.add((Element) object);
		}
		return objects;
	}
	
	public static Collection<Element> collection(Element element){
		if(element != null){
			return collection(element, new ArrayList<Element>());
		}
		return Collections.emptyList();
	}
	
	public static Collection<Element> collection(Element element, String childrenName, Collection<Element> objects){
		List<?> _objects = element.elements(childrenName);
		if(_objects != null && !_objects.isEmpty()){
			for (Object object : _objects) {
				objects.add((Element) object);
			}
		}
		return objects;
	}
	
	public static Collection<Element> collection(Element element, String childrenName){
		return collection(element, childrenName, new ArrayList<Element>());
	}
	
	public static String childText(Element element, String childName){
		return trim(element.elementTextTrim(childName));
	}
	
	public static String attribute(Element element, String attributeName){
		return trim(element.attributeValue(attributeName));
	}
}
