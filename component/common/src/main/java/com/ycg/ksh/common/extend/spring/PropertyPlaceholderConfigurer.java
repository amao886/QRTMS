package com.ycg.ksh.common.extend.spring;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/7
 */

import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import org.apache.commons.collections.MapUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义配置文件加载
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/7
 */
public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Resource locationXml;

    public void setLocationXml(Resource locationXml) {
        this.locationXml = locationXml;
    }

    /**
     * 根据启动参数,动态读取配置文件
     */
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        String environment = System.getProperty("spring.profiles.active", "local");

        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(locationXml.getInputStream());
            Element rootElement = document.getRootElement();
            Element commonElement = rootElement.element("common");
            if(commonElement != null){
                for (Iterator<Element> iterator = commonElement.elementIterator(); iterator.hasNext(); ) {
                    Element nextElement =  iterator.next();
                    if(nextElement != null){
                        props.put(nextElement.getName(), nextElement.attributeValue("value"));
                    }
                }
            }
            Element environmentElement = rootElement.element("environment");
            if(environmentElement != null){
                Element activeElement = environmentElement.element(environment);
                if(activeElement != null){
                    for (Iterator<Element> iterator = activeElement.elementIterator(); iterator.hasNext(); ) {
                        Element nextElement =  iterator.next();
                        if(nextElement != null){
                            props.put(nextElement.getName(), nextElement.attributeValue("value"));
                        }
                    }
                }
            }
            SystemUtils.put(SystemKey.SYS_CURRENT_ENV, environment);
            print(props);
        } catch (DocumentException e) {
            throw new IOException("xml配置文件解析失败", e);
        }
    }

    private void print(Properties props){
        logger.info("系统配置信息如下:");
        logger.info("========================================="+ SystemUtils.get(SystemKey.SYS_CURRENT_ENV) +"=================================================");
        if(MapUtils.isEmpty(props)){
            logger.info("没有任何配置信息");
        }else{
           for (Map.Entry<Object, Object> entry : props.entrySet()) {
                SystemUtils.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                logger.info("{} = {}", entry.getKey(), entry.getValue());
           }
        }
        logger.info("=============================================================================================");
    }
}
