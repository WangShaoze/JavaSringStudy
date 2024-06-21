package com.easychat.enums;

public enum AppUpdateStatusEnum {
    INIT(0,"未发布"),
    GREYSCALE(1,"灰度发布"),
    ALL(2,"全网发布");

    private Integer status;
    private String description;

    AppUpdateStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }


    public static AppUpdateStatusEnum getByStatus(Integer status){
        for (AppUpdateStatusEnum appUpdateStatusEnum : AppUpdateStatusEnum.values()) {
            if (appUpdateStatusEnum.status.equals(status)){
                return appUpdateStatusEnum;
            }
        }
        return null;
    }

}
