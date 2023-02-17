package com.example.api.util;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Properties;
public class ConstantUtil {

    @Resource(name = "constant")
    private static Properties constantProperties = loadConstantPropertyFile();


    private static Properties loadConstantPropertyFile() {
        Properties props = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("application.properties");
            props.load(inputStream);
            inputStream.close();
            return props;
        } catch (Exception e) {
            return props;
        }
    }

    public static String getPropertyConstant(String key){
        return constantProperties.getProperty(key);
    }
}
