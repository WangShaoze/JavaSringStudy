package com.easychat.enums;

import jodd.util.StringUtil;

public enum UserContractTypeEnum {
    USER(0, "U", "好友"),
    GROUP(1, "G", "群");

    private Integer type;
    private String prefix;
    private String desc;

    UserContractTypeEnum(Integer type, String prefix, String desc) {
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }

    public static UserContractTypeEnum getByName(String name){
        try {
            if(StringUtil.isEmpty(name)){
                return null;
            }
            return UserContractTypeEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }
    public static UserContractTypeEnum getByPrefix(String prefix){
        try {
            if (StringUtil.isEmpty(prefix)||prefix.trim().length()==0){
                return null;
            }
            prefix = prefix.substring(0, 1);
            for (UserContractTypeEnum typeEnum : UserContractTypeEnum.values()) {
                if (typeEnum.prefix.equals(prefix)){
                    return typeEnum;
                }
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}
