package com.easychat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName RunApplication
 * @Description 项目入口启动类
 * @Author
 * @Date
 * */
@EnableAsync  // 满足异步调用需要
@SpringBootApplication(scanBasePackages = {"com.easychat"})
@MapperScan(basePackages = "com.easyChat.mappers")
@EnableTransactionManagement  // 事务管理
@EnableScheduling     // 任务调度
public class RunApplication {
	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
	}
}
