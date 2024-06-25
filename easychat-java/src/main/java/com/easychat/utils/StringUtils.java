package com.easychat.utils;

import com.easychat.entity.constants.Constants;
import jodd.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

public class StringUtils {
    public static final String getUserId(){
        /**
         * 生成12位UserId,需要返回11位
         * */
        return getRandomNumber(Constants.LENGTH_11);
    }

    public static final String getGroupId(){
        /**
         * 生成12位GroupId,需要返回11位
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

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isAnyEmpty(CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        } else {
            CharSequence[] arr$ = css;
            int len$ = css.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                CharSequence cs = arr$[i$];
                if (isEmpty(cs)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static String join(Object[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return "";
        } else if (array.length == 1) {
            return String.valueOf(array[0]);
        } else {
            StringBuilder sb = new StringBuilder(array.length * 16);

            for(int i = 0; i < array.length; ++i) {
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return "";
        } else if (array.length == 1) {
            return String.valueOf(array[0]);
        } else {
            StringBuilder sb = new StringBuilder(array.length * 16);

            for(int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    sb.append(separator);
                }

                sb.append(array[i]);
            }

            return sb.toString();
        }
    }
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return "";
        } else if (array.length == 1) {
            return String.valueOf(array[0]);
        } else {
            StringBuilder sb = new StringBuilder(array.length * 16);

            for(int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    sb.append(separator);
                }

                sb.append(array[i]);
            }

            return sb.toString();
        }
    }

    public static String cleanHtmlTag(String content){
        if (isEmpty(content)){
            return content;
        }
        content = content.replace("<", "&lt;");
        content = content.replace("\r\n", "<br>");
        content = content.replace("\n", "<br>");
        return content;
    }

    /**
     * 生成会话Id
     * */
    public static String genChatSessionId4User(String[] userIds){
        Arrays.sort(userIds);
        return encodingMd5(StringUtils.join(userIds, ""));
    }

    /**
     * 生成群聊会话id
     * */
    public static String genChatSessionId4Group(String groupId){
        return encodingMd5(groupId);
    }
}
