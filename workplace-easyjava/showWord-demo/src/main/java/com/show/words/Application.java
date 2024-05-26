package com.show.words;

import com.show.words.utils.PropertiesUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.show.words.mappers")
public class Application {
    public final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static String address="https://www.baidu.com";
    static {
        address = "http://localhost:"+ PropertiesUtil.getProperty("server.port");
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("单词展示服务启动成功:{}", address);
    }
}
