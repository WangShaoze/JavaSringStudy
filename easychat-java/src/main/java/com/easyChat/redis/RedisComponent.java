package com.easyChat.redis;

import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.dto.SysSettingDto;
import com.easyChat.entity.dto.TokenUserInfoDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("redisComponent")
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取心跳
     * 如果心跳存在，说明该用户已经登陆了
     * */
    public Long getUserHeartBeat(String userId){
        return (Long) redisUtils.get(Constants.REDIS_KEY_WS_USER_HEART_BEAT+userId);
    }

    /**
     * redis中存储Token
     * */
    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto){
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN+tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_DAY*2);
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN_USERID+tokenUserInfoDto.getToken(), tokenUserInfoDto.getToken(), Constants.REDIS_KEY_EXPIRES_DAY*2);
    }

    /**
     * 获取系统设置
     * */
    public SysSettingDto getSysSetting(){
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        sysSettingDto = sysSettingDto==null?new SysSettingDto():sysSettingDto;
        return sysSettingDto;
    }

}
