package com.easyChat.enums;

import com.easyChat.exception.BusinessException;
import jodd.util.StringUtil;

public enum GroupStatusEnum {
    NORMAL(1, "正常"),
    DISSOLUTION(0, "已解散");

    private Integer status;
    private String desc;

    GroupStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static GroupStatusEnum getByName(String name){
        try{
            if (StringUtil.isEmpty(name)){
                return null;
            }
            return GroupStatusEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public static GroupStatusEnum getByType(Integer status){
        for (GroupStatusEnum groupStatusEnum : GroupStatusEnum.values()) {
            if (groupStatusEnum.getStatus().equals(status)){
                return groupStatusEnum;
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
