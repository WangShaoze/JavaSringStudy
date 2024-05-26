package com.easyJava;

import com.easyJava.utils.PropertiesUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 项目启动类
 * @author: 王绍泽
 * @date: 2024/05/21
 */
@SpringBootApplication
@MapperScan(basePackages = "com.easyJava.mappers")
public class RunApplication {
	public static final Logger logger = LoggerFactory.getLogger(RunApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
		logger.info("服务启动成功！ 地址: http://localhost:"+ PropertiesUtils.getProperty("server.port")+"/");
	}
}
