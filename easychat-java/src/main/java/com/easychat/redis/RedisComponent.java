package com.easychat.redis;

import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.UserContactApply;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName RedisComponent
 * @Description redis常用的操作
 * @Author
 * @Date
 */
@Component("redisComponent")
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取心跳
     * 如果心跳存在，说明该用户已经登陆了
     */
    public Long getUserHeartBeat(String userId) {
        return (Long) redisUtils.get(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }

    /**
     * 保存心跳
     */
    public void saveHeartBeat(String userId) {
        redisUtils.setex(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId, System.currentTimeMillis(), Constants.REDIS_KEY_EXPIRES_HEART_BEAT);
    }

    /**
     * 用户退出清除心跳
     */
    public void removeUserHeartBeat(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }

    /**
     * redis中存储Token
     */
    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto) {
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN + tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.REDIS_KEY_TOKEN_EXPIRES);
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN_USERID + tokenUserInfoDto.getUserId(), tokenUserInfoDto.getToken(), Constants.REDIS_KEY_TOKEN_EXPIRES);
    }


    /**
     * 通过 userId 获取token信息
     */
    public TokenUserInfoDto getTokenUserInfoDtoByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN_USERID + userId);
        return getTokenUserInfoDto(token);
    }


    /**
     * redis中获取token信息
     */
    public TokenUserInfoDto getTokenUserInfoDto(String token) {
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN + token);
        return tokenUserInfoDto;
    }

    /**
     * 通过用户Id清理用户的Token信息
     */
    public void cleanUserTokenByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN_USERID + userId);
        if (token == null) {
            return;
        }
        redisUtils.delete(Constants.REDIS_KEY_WS_TOKEN + token);
    }

    /**
     * 获取系统设置
     */
    public SysSettingDto getSysSetting() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        sysSettingDto = sysSettingDto == null ? new SysSettingDto() : sysSettingDto;
        return sysSettingDto;
    }

    /**
     * 保存系统设置
     */
    public void saveSysSetting(SysSettingDto sysSettingDto) {
        redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    /**
     * 批量添加联系人 到redis
     */
    public void addUserContactBatch(String userId, List<String> contactList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_USER_CONTACT + userId, contactList, Constants.REDIS_KEY_TOKEN_EXPIRES);
    }

    /**
     * 添加联系人 到redis中
     */

    public void addUserContact(String userId, String contactId) {
        List<String> contactIdList = getUserContactList(userId);
        if (contactIdList.contains(contactId)) {
            return;
        }
        redisUtils.lpush(Constants.REDIS_KEY_USER_CONTACT + userId, contactId, Constants.REDIS_KEY_TOKEN_EXPIRES);
    }

    /**
     * 从redis中获取联系人列表
     */
    public List<String> getUserContactList(String userId) {
        return (List<String>) redisUtils.getQueueList(Constants.REDIS_KEY_USER_CONTACT + userId);
    }

    /**
     * 清空redis联系人列表
     */
    public void clearUserContact(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_USER_CONTACT + userId);
    }

    /**
     * 移除redis中联系人的缓存
     * */
    public void removeUserUserContact(String userId, String contactId){
        redisUtils.remove(Constants.REDIS_KEY_USER_CONTACT + userId, contactId);
    }
}
