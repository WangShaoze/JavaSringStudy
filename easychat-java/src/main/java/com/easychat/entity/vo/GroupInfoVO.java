package com.easychat.entity.vo;

import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.po.UserContact;

import java.util.List;

/**
 * @ClassName GroupInfoVO
 * @Description 群组信息
 * @Author
 * @Date
 * */
public class GroupInfoVO {
    private GroupInfo groupInfo;

    private List<UserContact> userContactList;

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public List<UserContact> getUserContactList() {
        return userContactList;
    }

    public void setUserContactList(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }
}
