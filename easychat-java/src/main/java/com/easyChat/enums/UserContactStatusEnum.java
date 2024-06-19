package com.easyChat.enums;

import jodd.util.StringUtil;

public enum UserContactStatusEnum {
    /**
     * 用户交流状态
     * */
    NOT_FRIEND(0, "非好友"),
    FRIEND(1, "好友"),
    DEL(2, "已删除好友"),
    DEL_BE(3, "被删除好友"),
    BLACKLIST(4, "拉黑好友"),
    BLACKLIST_BE(5, "被好友拉黑"),
    BLACKLIST_BE_FIRST(6, "首次被好友拉黑");

    private Integer status;
    private String desc;

    UserContactStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserContactStatusEnum getByName(String name){
        try {
            if (StringUtil.isEmpty(name)){
                return null;
            }
            return UserContactStatusEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public static UserContactStatusEnum getByStatus(Integer status){
        for (UserContactStatusEnum contactStatusEnum : UserContactStatusEnum.values()) {
            if (contactStatusEnum.status.equals(status)){
                return contactStatusEnum;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
