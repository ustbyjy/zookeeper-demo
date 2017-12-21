package com.ascend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "config.properties";
        props = new Properties();
        try {
            props.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
            logger.info("配置文件读取成功");
        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }

    public static String getStringValue(String key) {
        String value = props.getProperty(key.trim());
        if (value == null || "".equals(value.trim())) {
            return null;
        }
        return value.trim();
    }

    public static String getStringValue(String key, String defaultValue) {
        String value = getStringValue(key);
        return value != null ? value.trim() : defaultValue;
    }

    public static Integer getIntValue(String key) {
        String value = getStringValue(key);
        if (value == null || "".equals(value.trim())) {
            return null;
        }
        Integer intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (Exception e) {
            logger.info("getIntValue", e);
            intValue = null;
        }
        return intValue;
    }

    public static Integer getIntValue(String key, int defaultValue) {
        Integer intValue = getIntValue(key);
        return intValue != null ? intValue : defaultValue;
    }
}
