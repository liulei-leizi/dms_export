package com.tazh.tazhinterface.utils;

import java.io.InputStream;
/**
 * 获取配置文件中配置帮助类
 */
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PropertiesConfig {
	private Properties systemProps = null; 


	private void loadProperties() {
		systemProps = new Properties();
		try {
			InputStream systemConfiguration = PropertiesConfig.class.getClassLoader()
					.getResourceAsStream("properFile/system.properties");
			systemProps.load(systemConfiguration);
			systemConfiguration.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSystemConfiguration(String username) {
		loadProperties();
		String systemConf = systemProps.getProperty(username).trim();

		try {
			systemConf = new String(systemConf.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return systemConf;
	}
	
}