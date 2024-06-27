package com.easychat.services.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.*;
import com.easychat.entity.query.*;
import com.easychat.enums.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.*;
import com.easychat.redis.RedisComponent;
import com.easychat.services.ChatSessionUserService;
import com.easychat.services.GroupInfoService;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.services.UserContactApplyService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;


import javax.annotation.Resource;

import com.easychat.websocket.ChannelContextUtils;
import com.easychat.websocket.MessageHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description: 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("groupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService {
    @Resource
    private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    @Resource
    private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private ChannelContextUtils channelContextUtils;

    @Resource
    private ChatSessionUserService chatSessionUserService;

    @Resource
    private UserContactApplyService userContactApplyService;

    @Resource
    @Lazy   // 自己调用自己为了避免循环引用的问题，这里必须延迟加载
    private GroupInfoService groupInfoService;

    /**
     * 解散群组操作
     *
     * @param groupOwnerId 群主id
     * @param groupId      群 id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dissolutionGroup(String groupOwnerId, String groupId) throws BusinessException {
        GroupInfo dbInfo = this.groupInfoMapper.selectByGroupId(groupId);
        if (null == dbInfo || !dbInfo.getGroupOwnerId().equals(groupOwnerId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        // 删除群组
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setStatus(GroupStatusEnum.DISSOLUTION.getStatus());
        this.groupInfoMapper.updateByGroupId(groupInfo, groupId);

        // 更新群联系人的信息
        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        userContactQuery.setContactType(UserContractTypeEnum.GROUP.getType());

        UserContact userContact = new UserContact();
        userContact.setStatus(UserContactStatusEnum.DEL.getStatus());
        this.userContactMapper.updateByParam(userContact, userContactQuery);

        // 移除相关群成员的联系人缓存
        List<UserContact> contactList = userContactMapper.selectList(userContactQuery);
        for (UserContact contact : contactList) {
            redisComponent.removeUserUserContact(userContact.getUserId(), userContact.getContactId());
        }
        // 发消息 1.更新会话信息  2.记录群消息   3.发送解散通知消息
        String sessionId = StringUtils.genChatSessionId4Group(groupId);
        Date curDate = new Date();
        String messageContent = MessageTypeEnum.DISSOLUTION_GROUP.getInitMessage();

        ChatSession chatSession = new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setLastMessage(messageContent);
        chatSession.setLastReceiveTime(curDate.getTime());
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setSendTime(curDate.getTime());
        chatMessage.setStatus(MessageStatusEnum.SENT.getStatus());
        chatMessage.setContactType(UserContractTypeEnum.GROUP.getType());
        chatMessage.setContactId(groupId);
        chatMessage.setMessageType(MessageTypeEnum.DISSOLUTION_GROUP.getType());
        chatMessage.setMessageContent(messageContent);
        chatMessageMapper.insert(chatMessage);
        MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
        messageSendDto.setExtendData(groupOwnerId);
        messageHandler.sendMessage(messageSendDto);
    }

    /**
     * 保存或者更新群组信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws BusinessException, IOException {

        Date curDate = new Date();
        // 创建
        if (StringUtils.isEmpty(groupInfo.getGroupId())) {
            GroupInfoQuery groupInfoQuery = new GroupInfoQuery();
            groupInfoQuery.setGroupOwnerId(groupInfo.getGroupOwnerId());
            groupInfoQuery.setStatus(GroupStatusEnum.NORMAL.getStatus());
            Integer count = this.groupInfoMapper.selectCount(groupInfoQuery);
            SysSettingDto sysSettingDto = redisComponent.getSysSetting();

            if (count >= sysSettingDto.getMaxGroupCount()) {
                throw new BusinessException("最多能支持创建" + sysSettingDto.getMaxGroupCount() + "个群聊");
            }

            if (null == avatarFile) {
//				throw new BusinessException(ResponseCodeEnum.CODE_600);
            }

            groupInfo.setCreateTime(curDate);
            groupInfo.setGroupId(UserContractTypeEnum.GROUP.getPrefix() + com.easychat.utils.StringUtils.getGroupId());
            this.groupInfoMapper.insert(groupInfo);

            // 将群组添加为自己的联系人
            UserContact userContact = new UserContact();
            userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            userContact.setContactType(UserContractTypeEnum.GROUP.getType());
            userContact.setContactId(groupInfo.getGroupId());
            userContact.setUserId(groupInfo.getGroupOwnerId());
            userContact.setCreateTime(curDate);
            userContact.setLastUpdateTime(curDate);
            this.userContactMapper.insert(userContact);

            // 创建会话
            String sessionId = StringUtils.genChatSessionId4Group(groupInfo.getGroupId());
            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionId);
            chatSession.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            chatSession.setLastReceiveTime(curDate.getTime());
            chatSessionMapper.insertOrUpdate(chatSession);

            ChatSessionUser chatSessionUser = new ChatSessionUser();
            chatSessionUser.setSessionId(sessionId);
            chatSessionUser.setUserId(groupInfo.getGroupOwnerId());
            chatSessionUser.setContactId(groupInfo.getGroupId());
            chatSessionUser.setContactName(groupInfo.getGroupName());
            chatSessionUserMapper.insert(chatSessionUser);

            // 创建消息
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSessionId(sessionId);
            chatMessage.setMessageType(MessageTypeEnum.GROUP_CREATE.getType());
            chatMessage.setMessageContent(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            chatMessage.setSendTime(curDate.getTime());
            chatMessage.setContactId(groupInfo.getGroupId());
            chatMessage.setContactType(UserContractTypeEnum.GROUP.getType());
            chatMessage.setStatus(MessageStatusEnum.SENT.getStatus());
            chatMessageMapper.insert(chatMessage);

            // 将群组添加到联系人
            redisComponent.addUserContact(groupInfo.getGroupOwnerId(), groupInfo.getGroupId());

            // 将联系人的通道添加到群组的通道
            channelContextUtils.addUser2Group(groupInfo.getGroupOwnerId(), groupInfo.getGroupId());

            //  发送 ws 消息
            chatSessionUser.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            chatSessionUser.setLastReceiveTime(curDate.getTime());
            chatSessionUser.setMemberCount(1);
            MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
            messageSendDto.setExtendData(chatSessionUser);
            messageSendDto.setMessageContent(chatSessionUser.getLastMessage());
            messageHandler.sendMessage(messageSendDto);
        } else {

            // 更新群组信息
            GroupInfo dbGroupInfo = this.groupInfoMapper.selectByGroupId(groupInfo.getGroupId());
            if (!dbGroupInfo.getGroupOwnerId().equals(groupInfo.getGroupOwnerId())) {
                /*防止别人不走前端界面直接走接口，恶意攻击*/
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            this.groupInfoMapper.updateByGroupId(groupInfo, groupInfo.getGroupId());

            // 更新相关表的冗余信息
            String contactNameUpdate = null;
            if (!dbGroupInfo.getGroupName().equals(groupInfo.getGroupName())) {
                contactNameUpdate = groupInfo.getGroupName();
            }
            if (contactNameUpdate == null) {
                return;
            }
            chatSessionUserService.updateRedundantInformation(contactNameUpdate, groupInfo.getGroupId());
        }

        if (null == avatarFile) {
            return;
        }
        String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
        File targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_FILE_AVATAR_NAME);
        if (!targetFileFolder.exists()) {
            targetFileFolder.mkdirs();
        }
        String filePath = targetFileFolder.getPath() + "/" + groupInfo.getGroupId() + Constants.IMAGE_SUFFIX;


        // 上传头像的图片
        avatarFile.transferTo(new File(filePath));
        avatarCover.transferTo(new File(filePath + Constants.COVER_IMAGE_SUFFIX));
    }

    @Override
    public void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfoDto, String groupId, String selectContacts, Integer opType) throws BusinessException {
        GroupInfo groupInfo = groupInfoMapper.selectByGroupId(groupId);
        if (groupInfo == null || !groupInfo.getGroupOwnerId().equals(tokenUserInfoDto.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        String[] contactIdArr = selectContacts.split(",");
        for (String contactId : contactIdArr) {
            if (Constants.ZERO.equals(opType)) {
                // 剔除群聊
                // 这里为了保证事务不失效，必须在service层调用该方法
                groupInfoService.leaveGroup(contactId, groupId, MessageTypeEnum.REMOVE_GROUP);
            } else {
                // 拉人入群
                userContactApplyService.addContact(contactId, null, groupId, UserContractTypeEnum.GROUP.getType(), null);
            }
        }
    }

    /**
     * 退出或被剔出群聊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum) throws BusinessException {
        GroupInfo groupInfo = groupInfoMapper.selectByGroupId(groupId);
        if (groupInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (groupInfo.getGroupOwnerId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Integer count = userContactMapper.deleteByUserIdAndContactId(userId, groupId);
        if (count == 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        String sessionId = StringUtils.genChatSessionId4Group(groupId);
        Date curDate = new Date();
        String messageContent = String.format(messageTypeEnum.getInitMessage(), userInfo.getNickName());

        ChatSession chatSession = new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setLastMessage(messageContent);
        chatSession.setLastReceiveTime(curDate.getTime());
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setMessageType(messageTypeEnum.getType());
        chatMessage.setSendTime(curDate.getTime());
        chatMessage.setStatus(MessageStatusEnum.SENT.getStatus());
        chatMessage.setContactId(groupId);
        chatMessage.setContactType(UserContractTypeEnum.GROUP.getType());
        chatMessageMapper.insert(chatMessage);

        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        Integer memberCount = userContactMapper.selectCount(userContactQuery);
        MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
        messageSendDto.setExtendData(userId);
        messageSendDto.setMemberCount(memberCount);
        messageHandler.sendMessage(messageSendDto);
    }

    /**
     * 根据条件查询列表
     */
    public List<GroupInfo> findListByParam(GroupInfoQuery query) {

        return this.groupInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(GroupInfoQuery query) {

        return this.groupInfoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query) {
        Integer count = this.findCountByParam(query);
        SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
        query.setSimplePage(simplePage);
        List<GroupInfo> list = this.findListByParam(query);
        PaginationResultVO<GroupInfo> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(GroupInfo bean) {
        return this.groupInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<GroupInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.groupInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改
     */
    public Integer addOrUpdateBatch(List<GroupInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.groupInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据 GroupId查询
     */
    public GroupInfo getByGroupId(String groupId) {
        return this.groupInfoMapper.selectByGroupId(groupId);
    }

    /**
     * 根据 GroupId更新
     */
    public Integer updateByGroupId(GroupInfo bean, String groupId) {

        return this.groupInfoMapper.updateByGroupId(bean, groupId);
    }

    /**
     * 根据 GroupId删除
     */
    public Integer deleteByGroupId(String groupId) {

        return this.groupInfoMapper.deleteByGroupId(groupId);
    }
}

