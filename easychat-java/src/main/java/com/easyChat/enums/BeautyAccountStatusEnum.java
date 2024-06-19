package com.easyChat.enums;

import java.security.PublicKey;

public enum BeautyAccountStatusEnum {
    NO_USE(0, "未使用"),
    USED(1, "使用");

    private Integer status;
    private String desc;

    BeautyAccountStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;

    }

    public BeautyAccountStatusEnum getByStatus(Integer status){
        for (BeautyAccountStatusEnum statusEnum : BeautyAccountStatusEnum.values()) {
            if (statusEnum.status.equals(status)){
                return statusEnum;
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
