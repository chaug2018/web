
package com.yzj.wf.scan.paramdefine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 按钮名称配置文件读取类
 * @author 陈林江
 * @version 1.0.1
 */
public class ButtonNameConfig {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ButtonNameConfig.class);

	private Properties properties = new Properties();

	private static  String CONFIG_FILENAME = "/Scan_ch.properties";
	private static ButtonNameConfig config;

	private ButtonNameConfig() {
		init();
	}
	
	private ButtonNameConfig(File file) {
		init(file);
	}
	
	public static ButtonNameConfig getInstance(String fileName){
		if(config==null){
			if(!"/.properties".equals(fileName)){
			CONFIG_FILENAME=fileName;
			}
			config=new ButtonNameConfig();
		}
		return config;
	}
	
	public static ButtonNameConfig getInstance(File file){
		if(config==null){
			config=new ButtonNameConfig(file);
		}
		return config;
	}
	
	private void init() {
		try {
			InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILENAME);
			properties.load(inputStream);
		} catch (Exception e) {
			log.error("读取" + CONFIG_FILENAME + "配置文件失败！");
		}
	}
	
	private void init(File file) {
		try {
			InputStream inputStream = new FileInputStream(file);
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
