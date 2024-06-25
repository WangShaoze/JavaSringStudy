package com.easychat.enums;

public enum MessageStatusEnum {
    SENDING(0, "正在发送"),
    SENT(1, "已发送");

    private Integer status;
    private String desc;

    MessageStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MessageStatusEnum getByStatus(Integer status){
        for (MessageStatusEnum statusEnum : MessageStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)){
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
