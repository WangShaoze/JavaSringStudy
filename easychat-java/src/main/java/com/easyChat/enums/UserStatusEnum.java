package com.easyChat.enums;

public enum UserStatusEnum {
    /**
     * 状态(0: 禁用,1:启用)
     * */
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");
    private Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserStatusEnum getByStatus(Integer status){
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.status.equals(status)){
                return userStatusEnum;
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
