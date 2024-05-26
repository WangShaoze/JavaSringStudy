package com.show.words.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final Map<String,String> PROPER_MAP = new ConcurrentHashMap<>();

    private static Properties properties = new Properties();
    static {
        InputStream is=null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(new InputStreamReader(is, "utf-8"));

            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                PROPER_MAP.put(key, properties.getProperty(key));
            }
        }catch (Exception e){
            logger.error("加载配置时出错！！，错误信息:{}", e.getMessage());
        }finally {
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String getProperty(String key){
        return PROPER_MAP.get(key);
    }
    public static void main(String[] args) {
        logger.info("spring.application.name:{}",getProperty("spring.application.name"));
    }
}
