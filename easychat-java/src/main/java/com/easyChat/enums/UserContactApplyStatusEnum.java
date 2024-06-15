package com.easyChat.enums;

import jodd.util.StringUtil;

public enum UserContactApplyStatusEnum {
    INIT(0,"待处理"),
    PASS(1,"已同意"),
    REJECT(2,"已拒绝"),
    BLACKLIST(3,"已拉黑");

    private Integer status;
    private String desc;

    UserContactApplyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserContactApplyStatusEnum getStatus(String name){
        try {
            if (StringUtil.isEmpty(name)){
                return null;
            }
            return UserContactApplyStatusEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }

    }

    public static UserContactApplyStatusEnum getStatus(Integer status){
        for (UserContactApplyStatusEnum applyStatusEnum : UserContactApplyStatusEnum.values()) {
            if (applyStatusEnum.getStatus().equals(status)){
                return applyStatusEnum;
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
