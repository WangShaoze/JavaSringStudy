package com.easyChat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {
    private static Properties props = new Properties();
    private static Map<String, String> PROPER_MAP = new ConcurrentHashMap<String, String>();

    static {
        InputStream is =null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(new InputStreamReader(is, "utf8"));

            Iterator<Object> iterator = props.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                PROPER_MAP.put(key, props.getProperty(key));

            }
        }catch (Exception e){
            e.printStackTrace();
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
        return PROPER_MAP.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getProperty("spring.datasource.url"));
    }
}
