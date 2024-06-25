package com.easychat.entity.dto;

import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.po.ChatSessionUser;

import java.util.List;

/**
 * @ClassName WsInitData
 * @Description ws初始化的信息
 * @Author
 * @Date
 * */
public class WsInitData {
    private List<ChatSessionUser> chatSessionList;

    private List<ChatMessage> chatMessageList;

    private Integer applyCount;

    public List<ChatSessionUser> getChatSessionList() {
        return chatSessionList;
    }

    public void setChatSessionList(List<ChatSessionUser> chatSessionList) {
        this.chatSessionList = chatSessionList;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }
}
