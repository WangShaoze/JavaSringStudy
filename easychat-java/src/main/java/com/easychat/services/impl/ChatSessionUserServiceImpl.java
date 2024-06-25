package com.easychat.services.impl;

import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.ChatSessionUserQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.enums.MessageTypeEnum;
import com.easychat.enums.UserContactStatusEnum;
import com.easychat.enums.UserContractTypeEnum;
import com.easychat.mappers.UserContactMapper;
import com.easychat.services.ChatSessionUserService;
import com.easychat.mappers.ChatSessionUserMapper;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.vo.PaginationResultVO;

import javax.annotation.Resource;

import com.easychat.websocket.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 会话用户 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/22
 */
@Service("chatSessionUserService")
public class ChatSessionUserServiceImpl implements ChatSessionUserService {
    @Resource
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    /**
     * 更新冗余信息
     */
    @Override
    public void updateRedundantInformation(String contactNameUpdate, String contactId) {
        ChatSessionUser updateInfo = new ChatSessionUser();
        updateInfo.setContactName(contactNameUpdate);

        ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
        chatSessionUserQuery.setContactId(contactId);
        chatSessionUserMapper.updateByParam(updateInfo, chatSessionUserQuery);

        UserContractTypeEnum contractTypeEnum = UserContractTypeEnum.getByPrefix(contactId);
        if (UserContractTypeEnum.GROUP==contractTypeEnum){
            MessageSendDto messageSendDto = new MessageSendDto();
            messageSendDto.setContactType(UserContractTypeEnum.getByPrefix(contactId).getType());
            messageSendDto.setContactId(contactId);
            messageSendDto.setExtendData(contactNameUpdate);
            messageSendDto.setMessageType(MessageTypeEnum.CONTACT_NAME_UPDATE.getType());
            messageHandler.sendMessage(messageSendDto);
        }else{
            UserContactQuery userContactQuery = new UserContactQuery();
            userContactQuery.setContactType(UserContractTypeEnum.USER.getType());
            userContactQuery.setContactId(contactId);
            userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            List<UserContact> userContactList = userContactMapper.selectList(userContactQuery);
            for (UserContact userContact : userContactList) {
                MessageSendDto messageSendDto = new MessageSendDto();
                messageSendDto.setContactType(UserContractTypeEnum.getByPrefix(contactId).getType());
                messageSendDto.setContactId(userContact.getContactId());
                messageSendDto.setExtendData(contactNameUpdate);
                messageSendDto.setMessageType(MessageTypeEnum.CONTACT_NAME_UPDATE.getType());
                messageSendDto.setSendUserId(contactId);
                messageSendDto.setSendUserNickName(contactNameUpdate);
                messageHandler.sendMessage(messageSendDto);
            }
        }
    }

    /**
     * 根据条件查询列表
     */
    public List<ChatSessionUser> findListByParam(ChatSessionUserQuery query) {

        return this.chatSessionUserMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(ChatSessionUserQuery query) {

        return this.chatSessionUserMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery query) {
        Integer count = this.findCountByParam(query);
        SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
        query.setSimplePage(simplePage);
        List<ChatSessionUser> list = this.findListByParam(query);
        PaginationResultVO<ChatSessionUser> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(ChatSessionUser bean) {
        return this.chatSessionUserMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<ChatSessionUser> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.chatSessionUserMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改
     */
    public Integer addOrUpdateBatch(List<ChatSessionUser> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.chatSessionUserMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据 UserIdAndContactId查询
     */
    public ChatSessionUser getByUserIdAndContactId(String userId, String contactId) {
        return this.chatSessionUserMapper.selectByUserIdAndContactId(userId, contactId);
    }

    /**
     * 根据 UserIdAndContactId更新
     */
    public Integer updateByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId) {

        return this.chatSessionUserMapper.updateByUserIdAndContactId(bean, userId, contactId);
    }

    /**
     * 根据 UserIdAndContactId删除
     */
    public Integer deleteByUserIdAndContactId(String userId, String contactId) {

        return this.chatSessionUserMapper.deleteByUserIdAndContactId(userId, contactId);
    }
}

