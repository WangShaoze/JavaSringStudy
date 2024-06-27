package com.easychat.services.impl;

import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.po.*;
import com.easychat.entity.query.*;
import com.easychat.enums.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.*;
import com.easychat.redis.RedisComponent;
import com.easychat.services.UserContactApplyService;
import com.easychat.entity.vo.PaginationResultVO;
import javax.annotation.Resource;

import com.easychat.utils.CopyTools;
import com.easychat.utils.StringUtils;
import com.easychat.websocket.ChannelContextUtils;
import com.easychat.websocket.MessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 联系人申请 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("userContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService{
	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

	// userContactMapper
	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private RedisComponent redisComponent;

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
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

	@Resource
	private ChannelContextUtils channelContextUtils;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dealWithApply(String userId, Integer applyId, Integer status) throws BusinessException {
		UserContactApplyStatusEnum statusEnum = UserContactApplyStatusEnum.getStatus(status);
		// 没有状态，或者状态还是待处理状态时直接报参数异常
		if (statusEnum==null||UserContactApplyStatusEnum.INIT==statusEnum){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		// 获取 申请人信息，如果数据库中的申请信息不存在或者接收者id不是当前用户，说明越权了
		UserContactApply userContactApply = this.userContactApplyMapper.selectByApplyId(applyId);
		if (userContactApply==null||!userId.equals(userContactApply.getReceiveUserId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		// 修改状态
		UserContactApply updateApplyInfo = new UserContactApply();
		updateApplyInfo.setStatus(statusEnum.getStatus());
		updateApplyInfo.setLastApplyTime(System.currentTimeMillis());

		UserContactApplyQuery userContactApplyQuery = new UserContactApplyQuery();
		userContactApplyQuery.setApplyId(applyId);
		// 这里加入状态作为条件，是防止状态不是 "待处理" 时候，对状态进行修改
		userContactApplyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());

		Integer count = userContactApplyMapper.updateByParam(updateApplyInfo, userContactApplyQuery);
		if (count == 0){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		if (UserContactApplyStatusEnum.PASS.getStatus().equals(status)){
			// 添加联系人
			addContact(
					userContactApply.getApplyUserId(),
					userContactApply.getReceiveUserId(),
					userContactApply.getContactId(),
					userContactApply.getContactType(),
					userContactApply.getApplyInfo()
			);
			return;
		}

		if (UserContactApplyStatusEnum.BLACKLIST.getStatus().equals(status)){
			// 当来黑申请者时候，需要在联系人表中更新相关信息
			Date curDate = new Date();
			UserContact userContact = new UserContact();
			userContact.setUserId(userContactApply.getApplyUserId());
			userContact.setContactId(userContactApply.getContactId());
			userContact.setContactType(userContactApply.getContactType());
			userContact.setCreateTime(curDate);
			userContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
			userContact.setLastUpdateTime(curDate);
			userContactMapper.insertOrUpdate(userContact);
		}
	}


	/**
	 * @param applyUserId 申请人的用户id
	 * @param receiveUserId 接收者的用户id
	 * @param contactId 联系人id( userId or groupId)
	 * @param contactType 联系人类型( user or group )
	 * @param applyInfo 申请信息
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addContact(String applyUserId, String receiveUserId, String contactId, Integer contactType, String applyInfo) throws BusinessException {
		// 群聊人数
		if (UserContractTypeEnum.GROUP.getType().equals(contactType)){  // 群组
			UserContactQuery userContactQuery = new UserContactQuery();
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());

			// 查询已经加入该群的人的数量
			Integer count = userContactMapper.selectCount(userContactQuery);
			SysSettingDto sysSettingDto = redisComponent.getSysSetting();
			if (count >= sysSettingDto.getMaxGroupMemberCount()){
				throw new BusinessException("成员已满，无法加入！");
			}
		}

		Date curDate = new Date();
		// 同意的时候双方添加好友
		List<UserContact> contactList = new ArrayList<>();
		// 申请人添加对方
		UserContact userContact = new UserContact();
		userContact.setUserId(applyUserId);
		userContact.setContactId(contactId);
		userContact.setContactType(contactType);
		userContact.setCreateTime(curDate);
		userContact.setLastUpdateTime(curDate);
		userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		contactList.add(userContact);
		// 如果是申请好友，接收人添加申请人，如果接受方是群组则不用添加申请人
		if (UserContractTypeEnum.USER.getType().equals(contactType)){
			// 用户 《-》 用户
			userContact = new UserContact();
			userContact.setUserId(contactId);
			userContact.setContactId(applyUserId);
			userContact.setContactType(contactType);
			userContact.setCreateTime(curDate);
			userContact.setLastUpdateTime(curDate);
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			contactList.add(userContact);
		}
		// 批量插入
		userContactMapper.insertOrUpdateBatch(contactList);

		// 在Redis中添加联系人的id
		if (UserContractTypeEnum.USER.getType().equals(contactType)){
			redisComponent.addUserContact(receiveUserId, applyUserId);
		}
		redisComponent.addUserContact(applyUserId, contactId);

		// 创建会话 发送消息
		String sessionId=null;
		if (UserContractTypeEnum.USER.getType().equals(contactType)){
			sessionId = StringUtils.genChatSessionId4User(new String[]{applyUserId, receiveUserId});
		}else{
			sessionId = StringUtils.genChatSessionId4Group(contactId);
		}

		List<ChatSessionUser> chatSessionUserList = new ArrayList<>();
		if (UserContractTypeEnum.USER.getType().equals(contactType)){
			// 创建会话
			ChatSession chatSession = new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastMessage(applyInfo);
			chatSession.setLastReceiveTime(curDate.getTime());
			this.chatSessionMapper.insertOrUpdate(chatSession);


			// 声请人session
			ChatSessionUser applyChatSessionUser = new ChatSessionUser();
			applyChatSessionUser.setUserId(applyUserId);
			applyChatSessionUser.setContactId(contactId);
			applyChatSessionUser.setSessionId(sessionId);
			UserInfo contactUserInfo = userInfoMapper.selectByUserId(contactId);
			applyChatSessionUser.setContactName(contactUserInfo.getNickName());
			chatSessionUserList.add(applyChatSessionUser);

			// 接收人session
			ChatSessionUser contactChatSessionUser = new ChatSessionUser();
			contactChatSessionUser.setUserId(contactId);
			contactChatSessionUser.setContactId(applyUserId);
			contactChatSessionUser.setSessionId(sessionId);
			UserInfo applyUserInfo = userInfoMapper.selectByUserId(applyUserId);
			contactChatSessionUser.setContactName(applyUserInfo.getNickName());
			chatSessionUserList.add(contactChatSessionUser);

			// 记录消息表
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
			chatMessage.setMessageContent(applyInfo);
			chatMessage.setSendUserId(applyUserId);
			chatMessage.setSendUserNickName(applyUserInfo.getNickName());
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(contactId);
			chatMessage.setContactType(UserContractTypeEnum.USER.getType());
			chatMessageMapper.insert(chatMessage);

			MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
			// 发送给接收人
			messageHandler.sendMessage(messageSendDto);

			// 发送给申请人，发送人就是接收人，联系人就是申请人
			messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND_SELF.getType());
			messageSendDto.setContactId(applyUserId);
			messageSendDto.setExtendData(contactUserInfo);
			messageHandler.sendMessage(messageSendDto);
		}else {
			// 加入群组
			ChatSessionUser chatSessionUser = new ChatSessionUser();
			chatSessionUser.setUserId(applyUserId);
			chatSessionUser.setContactId(contactId);
			GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);

			chatSessionUser.setContactName(groupInfo.getGroupName());
			chatSessionUser.setSessionId(sessionId);
			chatSessionUserMapper.insertOrUpdate(chatSessionUser);

			UserInfo applyUserInfo = userInfoMapper.selectByUserId(applyUserId);
			String sendMessage = String.format(MessageTypeEnum.ADD_GROUP.getInitMessage(), applyUserInfo.getNickName());
			// 增加session信息
			ChatSession chatSession = new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastReceiveTime(curDate.getTime());
			chatSession.setLastMessage(sendMessage);
			chatSessionMapper.insertOrUpdate(chatSession);
			// 增加聊天消息
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.ADD_GROUP.getType());
			chatMessage.setMessageContent(sendMessage);
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(contactId);
			chatMessage.setContactType(UserContractTypeEnum.GROUP.getType());
			chatMessage.setStatus(MessageStatusEnum.SENT.getStatus());
			chatMessageMapper.insert(chatMessage);

			// 将群组添加到联系人
			redisComponent.addUserContact(applyUserId, groupInfo.getGroupId());

			// 将联系人的通道添加到群组的通道
			channelContextUtils.addUser2Group(applyUserId, groupInfo.getGroupId());

			// 发送消息
			MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
			messageSendDto.setContactId(contactId);

			// 获取群成员的数量
			UserContactQuery userContactQuery = new UserContactQuery();
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			Integer memberCount = userContactMapper.selectCount(userContactQuery);
			messageSendDto.setMemberCount(memberCount);
			messageSendDto.setContactName(groupInfo.getGroupName());
			messageHandler.sendMessage(messageSendDto);
		}

	}

	/**
	 * 根据条件查询列表
	 */
	public List<UserContactApply> findListByParam(UserContactApplyQuery query) {

		return this.userContactApplyMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserContactApplyQuery query) {

		return this.userContactApplyMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<UserContactApply> list = this.findListByParam(query);
		PaginationResultVO<UserContactApply> result = new PaginationResultVO(
				count, simplePage.getPageSize(), simplePage.getPageNo(),
				simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserContactApply bean) {
		return this.userContactApplyMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserContactApply> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactApplyMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<UserContactApply> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 ApplyId查询
	 */
	public UserContactApply getByApplyId(Integer applyId) {
		return this.userContactApplyMapper.selectByApplyId(applyId);
	}

	/**
	 * 根据 ApplyId更新
	 */
	public Integer updateByApplyId(UserContactApply bean,Integer applyId) {

		return this.userContactApplyMapper.updateByApplyId(bean, applyId);
	}

	/**
	 * 根据 ApplyId删除
	 */
	public Integer deleteByApplyId(Integer applyId) {

		return this.userContactApplyMapper.deleteByApplyId(applyId);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	public UserContactApply getByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	public Integer updateByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean,String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	public Integer deleteByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}
}

