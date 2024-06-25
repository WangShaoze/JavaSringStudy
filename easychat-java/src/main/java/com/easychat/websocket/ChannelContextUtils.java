package com.easychat.websocket;

import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.WsInitData;
import com.easychat.entity.po.*;
import com.easychat.entity.query.ChatMessageQuery;
import com.easychat.entity.query.ChatSessionUserQuery;
import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.enums.MessageTypeEnum;
import com.easychat.enums.UserContactApplyStatusEnum;
import com.easychat.enums.UserContractTypeEnum;
import com.easychat.mappers.ChatMessageMapper;
import com.easychat.mappers.ChatSessionUserMapper;
import com.easychat.mappers.UserContactApplyMapper;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.services.ChatSessionUserService;
import com.easychat.utils.JsonUtils;
import com.easychat.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName ChannelContextUtils
 * @Description ws通道工具类
 * @Author
 * @Date
 * */
@Component
public class ChannelContextUtils {
    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ChannelGroup> GROUP_CONTEXT_MAP = new ConcurrentHashMap<>();

    @Resource
    private RedisComponent redisComponent;
    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Resource
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

    public void addContext(String userId, Channel channel){
        // 获取
        String channelId = channel.id().toString();
        AttributeKey attributeKey = null;
        if (!AttributeKey.exists(channelId)){
            attributeKey = AttributeKey.newInstance(channelId);
        }else{
            attributeKey = AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);


        // 获取联系人列表
        List<String> contactIdList = redisComponent.getUserContactList(userId);
        for (String groupId:contactIdList){
            // 将群组添加到 channel中的ChannelGroup去
            if (groupId.startsWith(UserContractTypeEnum.GROUP.getPrefix())){
                add2Group(groupId, channel);
            }
        }
        USER_CONTEXT_MAP.put(userId, channel);
        redisComponent.saveHeartBeat(userId);  // 保存用户心跳

        // 更新用户最后连接时间
        UserInfo updateInfo = new UserInfo();
        updateInfo.setLastLoginTime(new Date());
        userInfoMapper.updateByUserId(updateInfo, userId);

        //给用户发消息
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        Long sourceLastOfflineTime = userInfo.getLastOffTime();
        Long lastOfflineTime = sourceLastOfflineTime;
        if (sourceLastOfflineTime!=null&&System.currentTimeMillis()- Constants.MILLIS_SECONDS_3DAYS_AGO>sourceLastOfflineTime){
            lastOfflineTime = System.currentTimeMillis()- Constants.MILLIS_SECONDS_3DAYS_AGO;
        }

        /**
         * 1.查询会话信息，查询用户所有的会话信息，保证在用户换设备会话同步
         * */
        ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
        chatSessionUserQuery.setUserId(userId);
        chatSessionUserQuery.setOrderBy("last_receive_time desc");
        List<ChatSessionUser> chatSessionUserList = chatSessionUserMapper.selectList(chatSessionUserQuery);

        WsInitData wsInitData = new WsInitData();
        wsInitData.setChatSessionList(chatSessionUserList);

        /**
         * 2. 查询聊天消息
         * */
        List<String> groupIdList = contactIdList.stream().filter(item->item.startsWith(UserContractTypeEnum.GROUP.getPrefix())).collect(Collectors.toList());
        groupIdList.add(userId);
        ChatMessageQuery chatMessageQuery = new ChatMessageQuery();
        chatMessageQuery.setContactIdList(groupIdList);
        chatMessageQuery.setLastReceiveTime(lastOfflineTime);
        List<ChatMessage> chatMessageList = chatMessageMapper.selectList(chatMessageQuery);
        wsInitData.setChatMessageList(chatMessageList);
        /**
         * 3.查询新增好友的数量
         * */
        UserContactApplyQuery query = new UserContactApplyQuery();
        query.setReceiveUserId(userId);
        query.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
        query.setLastApplyTimeStamp(lastOfflineTime);
        wsInitData.setApplyCount(userContactApplyMapper.selectCount(query));

        // 发送消息
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageType(MessageTypeEnum.INIT.getType());
        messageSendDto.setContactId(userId);
        messageSendDto.setExtendData(wsInitData);
        sendMsg(messageSendDto, userId);
    }


    private void add2Group(String groupId, Channel chanel){
        if (chanel==null){
            return;
        }
        ChannelGroup group = GROUP_CONTEXT_MAP.get(groupId);
        if (group==null){
            group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CONTEXT_MAP.put(groupId, group);
        }
        group.add(chanel);

    }

//    public void send2Group(String message){
//        ChannelGroup group = GROUP_CONTEXT_MAP.get("100000");
//        group.writeAndFlush(new TextWebSocketFrame(message));
//    }

    /**
     * 将联系人的channel添加到群组channels中
     * */
    public void addUser2Group(String userId, String groupId){
        if (userId==null || groupId==null){
            return;
        }
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if (channel==null){
            return;
        }
        ChannelGroup channelGroup = GROUP_CONTEXT_MAP.get(groupId);
        if (channelGroup==null){
            return;
        }
        channelGroup.add(channel);
    }

    /**
     * 移除用户信息
     * */
    public void removeContext(Channel channel){
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        if (StringUtils.isEmpty(userId)){
            USER_CONTEXT_MAP.remove(userId);
        }
        redisComponent.removeUserHeartBeat(userId);

        // 更新用户最后离线时间
        UserInfo userInfo = new UserInfo();
        userInfo.setLastOffTime(System.currentTimeMillis());
        userInfoMapper.updateByUserId(userInfo, userId);
    }

    // 实现发消息
    public void sendMessage(MessageSendDto messageSendDto){
        UserContractTypeEnum userContractTypeEnum = UserContractTypeEnum.getByPrefix(messageSendDto.getContactId());
        switch (userContractTypeEnum){
            case USER:
                send2User(messageSendDto);
                break;
            case GROUP:
                send2Group(messageSendDto);
                break;
        }
    }

    // 发消息给用户
    private void send2User(MessageSendDto messageSendDto){
        String contactId=messageSendDto.getContactId();
        if (StringUtils.isEmpty(contactId)){
            return;
        }
        sendMsg(messageSendDto, contactId);

        // 强制下线
        if (MessageTypeEnum.FORCE_OFF_LINE.getType().equals(messageSendDto.getMessageType())){
            // 关闭通道
            closeContext(contactId);
        }
    }

    // 当与联系人的通讯中断时调用，关闭相关资源
    public void closeContext(String userId){
        if (StringUtils.isEmpty(userId)){
            return;
        }
        // 清理用户token信息
        redisComponent.cleanUserTokenByUserId(userId);
        // 关闭通道
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if (channel==null){
            return;
        }
        channel.close();
    }

    // 发消息给群组
    private void send2Group(MessageSendDto messageSendDto){
        if (StringUtils.isEmpty(messageSendDto.getContactId())){
            return;
        }
        ChannelGroup channelGroup = GROUP_CONTEXT_MAP.get(messageSendDto.getContactId());
        if (channelGroup==null){
            return;
        }
        channelGroup.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObject2Json(messageSendDto)));
    }


    // 发送消息 channel
    public static void sendMsg(MessageSendDto messageSendDto, String receiveId){
        if (receiveId==null){
            return;
        }
        Channel channel = USER_CONTEXT_MAP.get(receiveId);
        if (channel==null){
            return;
        }

        // 对于客户端而言，联系人就是发送人，所以这里就转一下在发送
        if (MessageTypeEnum.ADD_FRIEND_SELF.getType().equals(messageSendDto.getMessageType())){
            // 自己给自己发的时候
            UserInfo userInfo = (UserInfo) messageSendDto.getExtendData();
            messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
            messageSendDto.setContactId(userInfo.getUserId());
            messageSendDto.setContactName(userInfo.getNickName());
            messageSendDto.setExtendData(null);
        }else {
            messageSendDto.setContactId(messageSendDto.getSendUserId());
            messageSendDto.setContactName(messageSendDto.getSendUserNickName());
        }
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObject2Json(messageSendDto)));
    }

}
