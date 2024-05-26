package com.runyin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtil {
    private final static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private final static Map<String,String> proper_map = new ConcurrentHashMap<String ,String>();
    private static Properties properties = new Properties();

    static {
        InputStream is=null;

        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(new InputStreamReader(is, "utf-8"));
            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                proper_map.put(key, properties.getProperty(key));
            }

        }catch (Exception e){
            logger.error("加载配置时出现错误！！， 错误信息：{}", e.getMessage());
        }finally {
            if (is!=null){
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key){
        return proper_map.get(key);
    }

    public static void main(String[] args) {
        String port = getProperty("server.port");
        String address = getProperty("server.address");
        String url = getProperty("spring.datasource.url");
        System.out.println(url);
        logger.info("server.address:{}, server.port: {}",address, port);
    }
}
