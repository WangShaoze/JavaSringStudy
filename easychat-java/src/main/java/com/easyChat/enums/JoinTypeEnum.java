package com.easyChat.enums;

import jodd.util.StringUtil;

public enum JoinTypeEnum {
    JOIN(0, "直接加入"),
    APPLY(1, "需要审核");

    private Integer type;
    private String desc;

    JoinTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static JoinTypeEnum getByName(String name){
        try {
            if (StringUtil.isEmpty(name)){
                return null;
            }
            return JoinTypeEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public static JoinTypeEnum getByType(Integer type){
        for (JoinTypeEnum joinTypeEnum : JoinTypeEnum.values()) {
            if (joinTypeEnum.getType().equals(type)){
                return joinTypeEnum;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
