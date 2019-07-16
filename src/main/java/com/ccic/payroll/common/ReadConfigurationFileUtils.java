package com.ccic.payroll.common;

import java.io.InputStream;
import java.util.Properties;

public class ReadConfigurationFileUtils {
    public static String readConfigurationByProperties(String filePath, String key) throws Exception {

        Properties properties = new Properties();
        InputStream inStream = ReadConfigurationFileUtils.class.getResourceAsStream("/" + filePath);
        properties.load(inStream);
        String value = properties.getProperty(key);
        return value;
    }
}
