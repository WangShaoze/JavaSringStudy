package com.easychatjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.easychatjava", exclude = DataSourceAutoConfiguration.class)
public class EasychatJavaApplication {
	public static void main(String[] args) {
		SpringApplication.run(EasychatJavaApplication.class, args);
	}
}
