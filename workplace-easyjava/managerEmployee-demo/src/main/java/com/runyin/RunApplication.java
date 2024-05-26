package com.runyin;

import com.runyin.utils.PropertiesUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.runyin.mappers")
public class RunApplication {
    //通过反射的方式获取日志
    public static final Logger logger = LoggerFactory.getLogger(RunApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
        logger.info("服务启动成功！！ 地址: http://{}:{}", PropertiesUtil.getProperty("server.address"), PropertiesUtil.getProperty("server.port"));
    }
}
