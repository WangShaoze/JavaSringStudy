package com.easyChat.entity.constants;

import com.easyChat.enums.UserContractTypeEnum;

public class Constants {
    public static final String REDIS_KEY_CHECK_CODE = "easychat:checkcode";
    public static final String REDIS_KEY_WS_USER_HEART_BEAT = "easychat:ws:user:heartbeat";
    public static final String REDIS_KEY_WS_TOKEN = "easychat:ws:token:";
    public static final String REDIS_KEY_WS_TOKEN_USERID = "easychat:ws:token:userid";


    public static final Integer REDIS_TIME_1MINUTE = 60;
    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_TIME_1MINUTE*60*24;
    public static final int LENGTH_11 = 11;
    public static final int LENGTH_20 = 20;
    public static final String ROBOT_UID= UserContractTypeEnum.USER.getPrefix()+"robot";
    public static final String REDIS_KEY_SYS_SETTING= "easychat:syssetting:";



}
