package com.easychat.entity.constants;

import com.easychat.enums.UserContractTypeEnum;

/**
 * @ClassName Constants
 * @Description 常量
 * @Author
 * @Date
 * */
public class Constants {
    public static final String REDIS_KEY_CHECK_CODE = "easychat:checkcode";
    public static final String REDIS_KEY_WS_USER_HEART_BEAT = "easychat:ws:user:heartbeat";
    public static final String REDIS_KEY_WS_TOKEN = "easychat:ws:token:";
    public static final String REDIS_KEY_WS_TOKEN_USERID = "easychat:ws:token:userid";

    // 用户联系人列表
    public static final String REDIS_KEY_USER_CONTACT = "easychat:ws:user:contact:";


    public static final Integer REDIS_KEY_EXPIRES_HEART_BEAT = 6;
    public static final Integer REDIS_TIME_1MINUTE = 60;
    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_TIME_1MINUTE*60*24;
    // Token 失效时间
    public static final Integer REDIS_KEY_TOKEN_EXPIRES = REDIS_TIME_1MINUTE*60*24*2;
    public static final int LENGTH_11 = 11;
    public static final int LENGTH_20 = 20;
    public static final String ROBOT_UID= UserContractTypeEnum.USER.getPrefix()+"robot";
    public static final String REDIS_KEY_SYS_SETTING= "easychat:syssetting:";


    public static final String FILE_FOLDER_FILE="/file";
    public static final String FILE_FOLDER_FILE_AVATAR_NAME="/avatar";
    public static final String IMAGE_SUFFIX=".png";
    public static final String COVER_IMAGE_SUFFIX="_cover.png";

    public static final String APPLY_INFO_TEMPLATE="我是%s";
    public static final String REGEXP_PASSWORD="/^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,18}$/";  // 密码校验正则表达式
    public static final String APP_UPDATE_FOLDER = "/app/";
    public static final String APP_EXE_SUFFIX = ".exe";
    public static final String APP_NAME = "EasyChatSetup.";


    // 3天以前的毫秒数
    public static final Long MILLIS_SECONDS_3DAYS_AGO=3*24*60*60*1000L;
}
