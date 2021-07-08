/**
 * ReadConfig.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-10
 */
package com.yzj.wf.scan.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * 读取控件的配置文件
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 * @see java.util.Properties
 */
public class ReadConfig {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReadConfig.class);

	private Properties properties = new Properties();

	private static final String CONFIG_FILENAME = "/CollectPlatform.properties";

	public ReadConfig() {
		init();
	}
	
	private void init() {
		try {
			InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILENAME);
			properties.load(inputStream);
		} catch (Exception e) {
			log.error("读取" + CONFIG_FILENAME + "配置文件失败！");
		}
	}

	/**
	 * 获取配置属性值
	 * 
	 * @param propertyName
	 *            属性key
	 * @return 不存在返回NULL
	 */
	public String getPropertyValue(String propertyName) {
		return properties.getProperty(propertyName);
	}

	/**
	 * 获取配置属性值
	 * 
	 * @param propertyName
	 *            属性key
	 * @param defaultValue
	 *            默认值
	 * @return 存在key 返回对应的value，否则返回defaultValue
	 */
	public String getPropertyValue(String propertyName, String defaultValue) {
		return properties.getProperty(propertyName, defaultValue);
	}

	public void setPropertyValue(String propertyName, String propertyValue) {
		this.properties.setProperty(propertyName, propertyValue);
	}

	public void setProperties(Properties properties) {
		Iterator<?> iterator = properties.keySet().iterator();
		String key = null;
		while(iterator.hasNext()){
			key = (String) iterator.next();
			properties.setProperty(key, (String) properties.get(key));
		}
	}
	
	public Properties getProperties(){
		return properties;
	}
	
}
