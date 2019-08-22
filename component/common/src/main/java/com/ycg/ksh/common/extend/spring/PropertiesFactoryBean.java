package com.ycg.ksh.common.extend.spring;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;

/**
 * 自定义配置文件加载过程
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-14 16:21:32
 */
public class PropertiesFactoryBean extends org.springframework.beans.factory.config.PropertiesFactoryBean implements ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationContext applicationContext;

	private Environment environment;

	private String locationXml;

	public void setLocationXml(String locationXml) {
		this.locationXml = locationXml;
	}


	/**
	 * 根据启动参数,动态读取配置文件
	 */
	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
		String env = SystemUtils.get(SystemKey.SYS_CURRENT_ENV);
		SAXReader reader = new SAXReader();
		try {
			props.putAll(SystemUtils.properties());
			Document document = null;
			Resource resource = new ClassPathResource(locationXml);
			if(resource.exists()){
				document = reader.read(resource.getInputStream());
			}else{
				document = reader.read(new File(SystemUtils.projectPath(), locationXml));
			}
			Element rootElement = document.getRootElement();
			loopReader(rootElement.element("common"), props, null);
			Element environmentElement = rootElement.element("environment");
			if(environmentElement != null){
				if(env != null && env.length() > 0){
					loopReader(environmentElement.element(env), props, null);
				}else{
					for (Iterator<Element> iterator = environmentElement.elementIterator(); iterator.hasNext(); ) {
						Element nextElement =  iterator.next();
						if(environment.acceptsProfiles(nextElement.getName())){
							loopReader(nextElement, props, null);
						}
					}
				}
			}
			print(props);
		} catch (DocumentException e) {
			throw new IOException("xml配置文件解析失败", e);
		}
	}

	private void loopReader(Element element, Properties props, String pname){
		if(element != null){
			for (Iterator<Element> iterator = element.elementIterator(); iterator.hasNext(); ) {
				Element nextElement =  iterator.next();
				if(nextElement != null){
					String name = nextElement.getName();
					if(pname != null && pname.length() > 0){
						name = pname +"."+ name;
					}
					String value = nextElement.attributeValue("value");
					if(value != null){
						props.put(name, value);
					}
					loopReader(nextElement, props, name);
				}
			}
		}
	}

	private void print(Properties props){
		logger.info("系统配置信息如下:");
		logger.info("========================================="+ SystemUtils.get(SystemKey.SYS_CURRENT_ENV) +"=================================================");
		if(MapUtils.isEmpty(props)){
			logger.info("没有任何配置信息");
		}else{
			List<String> keys = props.keySet().stream().map(k -> String.valueOf(k)).collect(Collectors.toList());
			Collections.sort(keys);
			for (String key : keys) {
				Object value = props.get(key);
				SystemUtils.put(key, String.valueOf(value));
				logger.info("{} = {}", key, value);
			}
			/*
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				SystemUtils.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
				logger.info("{} = {}", entry.getKey(), entry.getValue());
			}
			*/
		}
		logger.info("=============================================================================================");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		this.environment = applicationContext.getEnvironment();
	}
}
