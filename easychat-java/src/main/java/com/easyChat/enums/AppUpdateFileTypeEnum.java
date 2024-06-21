package com.easyChat.enums;

public enum AppUpdateFileTypeEnum {
    LOCAL(0, "本地"), OUTER_LINK(1, "外链");

    private Integer type;
    private String description;

    AppUpdateFileTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static AppUpdateFileTypeEnum getByType(Integer type){
        // 更具枚举定义字段查找该枚举
        for (AppUpdateFileTypeEnum typeEnum : AppUpdateFileTypeEnum.values()) {
            if(typeEnum.type.equals(type)){
                return typeEnum;
            }
        }
        return null;
    }
}
