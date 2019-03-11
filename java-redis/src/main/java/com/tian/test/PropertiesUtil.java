package com.tian.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * 读取配置文件共同方法
 * 
 * @ClassName: PropertiesUtil
 * @author wuxiaohua
 * @date 2016年4月14日 下午2:48:36
 */
public class PropertiesUtil {

	private static Properties properties;
	
	public PropertiesUtil(String resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}
	
	/**
	 * 获取配置文件中的属性集合
	 * 
	 * @author wuxiaohua
	 * @date 2016年4月14日 下午2:49:02
	 * @param configFileName 属性文件路径
	 * @return 属性集合
	 */
	private Properties loadProperties(String configFileName) {
		InputStream in = null;
		try {
			in = PropertiesUtil.class.getResourceAsStream(configFileName);
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 取出Property，取不到返回空字符串.
	 */
	private String getValue(String key) {
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}
		return "";
	}

	/**
	 * 取出String类型的Property，如果都为Null则抛出异常.
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * 取出String类型的Property，如果都为Null则返回Default值.
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 取出Integer类型的Property，如果都为Null或内容错误则抛出异常.
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 取出Integer类型的Property，如果都为Null则返回Default值，如果内容错误则抛出异常
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Double类型的Property，如果都为Null或内容错误则抛出异常.
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 取出Double类型的Property，如果都为Null则返回Default值，如果内容错误则抛出异常
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Boolean类型的Property，如果都为Null抛出异常,如果内容不是true/false则返回false.
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Property，如果都为Null则返回Default值,如果内容不为true/false则返回false.
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

    
	public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        PropertiesUtil.properties = properties;
    }
	
	

}
