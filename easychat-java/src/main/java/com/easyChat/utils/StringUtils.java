package com.easyChat.utils;

import com.easyChat.entity.constants.Constants;
import jodd.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {
    public static final String getUserId(){
        /**
         * 生成12位UserId,需要返回11位
         * */
        return getRandomNumber(Constants.LENGTH_11);
    }

    public static String getRandomNumber(int count){
        return RandomStringUtils.random(count, false, true);
    }

    public static String getRandomString(int count){
        return RandomStringUtils.random(count, true, true);
    }

    public static final String encodingMd5(String password){
        return StringUtil.isEmpty(password)?null: DigestUtils.md5Hex(password);
    }
}
