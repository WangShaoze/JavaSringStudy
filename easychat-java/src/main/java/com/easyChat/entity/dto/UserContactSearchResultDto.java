package com.easyChat.entity.dto;

import com.easyChat.enums.UserContactStatusEnum;

public class UserContactSearchResultDto {
    private String contactId;
    private String contactType;
    private String nickName;   // 用户的昵称
    private Integer status;
    private String statusName;
    private Integer sex;
    private String areaName;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        UserContactStatusEnum userContactStatusEnum = UserContactStatusEnum.getByStatus(status);
        return userContactStatusEnum==null?null:userContactStatusEnum.getDesc();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
