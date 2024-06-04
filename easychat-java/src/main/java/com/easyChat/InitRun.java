package com.easyChat;

import com.easyChat.redis.RedisUtils;
import com.easyChat.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@Component("initRun")
public class InitRun implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void run(ApplicationArguments args){
        try {
            dataSource.getConnection();
            redisUtils.get("test");
            logger.info("服务启动成功: http://localhost:"+ PropertiesUtils.getProperty("server.port")+"/api/");
        }catch (SQLException e){
            logger.error("数据库连接异常！请检查数据库配置！");
        }catch (RedisConnectionFailureException e){
            logger.error("Redis连接失败！！");
        }catch (Exception e){
            logger.error("服务启动失败！！");
        }
    }
}
