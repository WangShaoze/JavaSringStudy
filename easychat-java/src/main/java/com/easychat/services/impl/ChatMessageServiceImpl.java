package com.easychat.services.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.po.ChatSession;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.ChatMessageQuery;
import com.easychat.entity.query.ChatSessionQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.enums.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.ChatSessionMapper;
import com.easychat.mappers.UserContactMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.services.ChatMessageService;
import com.easychat.mappers.ChatMessageMapper;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.vo.PaginationResultVO;
import javax.annotation.Resource;

import com.easychat.utils.CopyTools;
import com.easychat.utils.DateUtils;
import com.easychat.utils.StringUtils;
import com.easychat.websocket.MessageHandler;
import jodd.util.ArraysUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description: 聊天消息表 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/22
 */
@Service("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService{
	private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

	@Resource
	private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

	@Resource
	private MessageHandler messageHandler;

	@Resource
	private AppConfig appConfig;

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;


	@Override
	public MessageSendDto saveMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto) throws BusinessException {
		// 不是机器人回复
		if (!tokenUserInfoDto.getUserId().equals(Constants.ROBOT_UID)){
			List<String> contactList = redisComponent.getUserContactList(tokenUserInfoDto.getUserId());
			if (!contactList.contains(chatMessage.getContactId())){
				// 如果好友列表中没有找到该好友
				UserContractTypeEnum userContractTypeEnum = UserContractTypeEnum.getByPrefix(chatMessage.getContactId());
				assert userContractTypeEnum != null;
				if (userContractTypeEnum.equals(UserContractTypeEnum.USER)){
					throw new BusinessException(ResponseCodeEnum.CODE_902);
				}else{
					throw new BusinessException(ResponseCodeEnum.CODE_903);
				}
			}
		}

		String sessionId=null;
		String sendUserId = tokenUserInfoDto.getUserId();
		String contactId = chatMessage.getContactId();

		UserContractTypeEnum userContractTypeEnum = UserContractTypeEnum.getByPrefix(contactId);
		if (userContractTypeEnum==UserContractTypeEnum.USER){
			sessionId = StringUtils.genChatSessionId4User(new String[]{sendUserId, contactId});
		}else{
			sessionId = StringUtils.genChatSessionId4Group(contactId);
		}
		chatMessage.setSessionId(sessionId);

		Long curTime = System.currentTimeMillis();
		chatMessage.setSendTime(curTime);

		// 对消息类型进行判断
		MessageTypeEnum messageTypeEnum = MessageTypeEnum.getByType(chatMessage.getMessageType());

		if (messageTypeEnum==null||!ArraysUtil.contains(new Integer[]{
				MessageTypeEnum.CHAT.getType(), MessageTypeEnum.MEDIA_CHAT.getType()
		}, messageTypeEnum.getType())){
			// 限制只允许 文本信息 和 媒体文件的聊天信息,如果是其他类型的消息直接报参数异常
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		// 净化代码，方式 html js 代码注入
		String messageContent = StringUtils.cleanHtmlTag(chatMessage.getMessageContent());
		chatMessage.setMessageContent(messageContent);

		// 更新会话
		ChatSession chatSession = new ChatSession();
		chatSession.setLastMessage(messageContent);
		if (UserContractTypeEnum.GROUP==userContractTypeEnum){
			chatSession.setLastMessage(tokenUserInfoDto.getNickName()+":"+messageContent);
		}
		chatSession.setLastReceiveTime(curTime);
		chatSessionMapper.updateBySessionId(chatSession, sessionId);

		// 记录消息
		chatMessage.setSendUserId(sendUserId);
		chatMessage.setSendUserNickName(tokenUserInfoDto.getNickName());
		chatMessage.setContactType(userContractTypeEnum.getType());
		chatMessageMapper.insert(chatMessage);

		MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
		if (Constants.ROBOT_UID.equals(contactId)){
			// 如果是和机器人聊天
			SysSettingDto sysSettingDto = redisComponent.getSysSetting();
			TokenUserInfoDto robot = new TokenUserInfoDto();
			robot.setNickName(sysSettingDto.getRobotNickName());
			robot.setUserId(sysSettingDto.getRobotUid());
			ChatMessage robotChatMessage = new ChatMessage();
			robotChatMessage.setContactId(sendUserId);
			// TODO 对接AI接口实现恢复消息
			robotChatMessage.setMessageContent("我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。");
			robotChatMessage.setMessageType(MessageTypeEnum.CHAT.getType());

			// 递归实现机器人自动回复消息实际原理是将 send_user_id 和 contact_id 进行交换，第二次不会走这个if，直接进入else发送消息
			saveMessage(robotChatMessage, robot);
		}else{
			messageHandler.sendMessage(messageSendDto);
		}
		return messageSendDto;
	}

	@Override
	public void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile cover) throws BusinessException {
		ChatMessage chatMessage = chatMessageMapper.selectByMessageId(messageId);
		if (chatMessage==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (!chatMessage.getSendUserId().equals(userId)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		SysSettingDto sysSettingDto = redisComponent.getSysSetting();
		String fileSuffix = StringUtils.getFileSuffix(file.getOriginalFilename());
		if (StringUtils.isNotEmpty(fileSuffix) && ArrayUtils.contains(Constants.IMAGE_SUFFIX_LIST, fileSuffix.toLowerCase()) && (file.getSize() > sysSettingDto.getMaxImageSize()*Constants.FILE_SIZE_MB)){
			logger.info("图片太大了，超出限制大小");
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}else if (StringUtils.isNotEmpty(fileSuffix) && ArrayUtils.contains(Constants.VIDEO_SUFFIX_LIST, fileSuffix.toLowerCase()) && (file.getSize() > sysSettingDto.getMaxImageSize()*Constants.FILE_SIZE_MB)){
			logger.info("视频太大了，超出限制大小");
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}else {
			if (
					StringUtils.isNotEmpty(fileSuffix) &&
							!ArrayUtils.contains(Constants.VIDEO_SUFFIX_LIST, fileSuffix.toLowerCase()) &&
							!ArrayUtils.contains(Constants.IMAGE_SUFFIX_LIST, fileSuffix.toLowerCase()) &&
							(file.getSize() > sysSettingDto.getMaxImageSize()*Constants.FILE_SIZE_MB)
			){   // 有后缀，既不是音频也不是视频，但是文件大小超出限制也不行
				logger.info("文件太大，超出限制大小");
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			String fileName = file.getOriginalFilename();
			String fileExtName = StringUtils.getFileSuffix(fileName);
			String fileRealName = messageId+fileExtName;
			String coverFileRealName =null;
			if (cover!=null){
				coverFileRealName = messageId+Constants.COVER_IMAGE_SUFFIX;
			}
			String month = DateUtils.format(new Date(chatMessage.getSendTime()), DateTimePatternEnum.YYYYMM.getPattern());
			File folder = new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+"/"+month);
			if (!folder.exists()){
				folder.mkdirs();
			}
			File uploadFile = new File(folder.getPath()+"/"+fileRealName);
			File uploadCoverFile = null;
			if (coverFileRealName!=null){
				uploadCoverFile = new File(folder.getPath()+"/"+coverFileRealName);
			}

			try {
				file.transferTo(uploadFile);
				if (uploadCoverFile!=null){
					cover.transferTo(uploadCoverFile);
				}
			} catch (IOException e) {
				logger.error("文件上传失败！",e);
				throw new BusinessException("文件上传失败！");
			}

			ChatMessage uploadInfo = new ChatMessage();
			uploadInfo.setStatus(MessageStatusEnum.SENT.getStatus());
			ChatMessageQuery chatMessageQuery = new ChatMessageQuery();
			chatMessageQuery.setMessageId(messageId);
			chatMessageQuery.setStatus(MessageStatusEnum.SENDING.getStatus());
			chatMessageMapper.updateByParam(uploadInfo, chatMessageQuery);

			MessageSendDto messageSendDto = new MessageSendDto();
			messageSendDto.setStatus(MessageStatusEnum.SENT.getStatus());
			messageSendDto.setMessageId(messageId);
			messageSendDto.setMessageType(MessageTypeEnum.FILE_UPLOAD.getType());
			messageSendDto.setContactId(chatMessage.getContactId());
			messageHandler.sendMessage(messageSendDto);
		}


	}


	/**
	 * 下载文件
	 * */
	@Override
	public File downloadFile(TokenUserInfoDto tokenUserInfoDto, Long messageId, Boolean showCover) throws BusinessException {
		ChatMessage chatMessage = chatMessageMapper.selectByMessageId(messageId);
		String contactId = chatMessage.getContactId();
		UserContractTypeEnum contractTypeEnum = UserContractTypeEnum.getByPrefix(contactId);
		if (contractTypeEnum==UserContractTypeEnum.USER&&!tokenUserInfoDto.getUserId().equals(chatMessage.getContactId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (contractTypeEnum==UserContractTypeEnum.GROUP){
			UserContactQuery userContactQuery = new UserContactQuery();
			userContactQuery.setUserId(userContactQuery.getUserId());
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			userContactQuery.setContactType(UserContractTypeEnum.GROUP.getType());
			Integer contactCount = userContactMapper.selectCount(userContactQuery);
			if (contactCount==0){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}

		String month = DateUtils.format(new Date(chatMessage.getSendTime()), DateTimePatternEnum.YYYYMM.getPattern());
		File folder = new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+month);
		if (!folder.exists()){
			folder.mkdirs();
		}
		String fileName = chatMessage.getFileName();
		String fileExtName = StringUtils.getFileSuffix(fileName);
		String fileRealName = messageId+fileExtName;
		if (showCover!=null&&showCover){
			fileRealName = messageId+Constants.COVER_IMAGE_SUFFIX;
		}
		File file = new File(folder.getPath()+File.separator+fileRealName);
		if (!file.exists()){
			logger.error("文件不存在:{}", messageId);
			throw new BusinessException(ResponseCodeEnum.CODE_602);
		}
		return file;
	}

	/**
	 * 根据条件查询列表
	 */
	public List<ChatMessage> findListByParam(ChatMessageQuery query) {

		return this.chatMessageMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(ChatMessageQuery query) {

		return this.chatMessageMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<ChatMessage> list = this.findListByParam(query);
		PaginationResultVO<ChatMessage> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(ChatMessage bean) {
		return this.chatMessageMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<ChatMessage> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.chatMessageMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<ChatMessage> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.chatMessageMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 MessageId查询
	 */
	public ChatMessage getByMessageId(Long messageId) {
		return this.chatMessageMapper.selectByMessageId(messageId);
	}

	/**
	 * 根据 MessageId更新
	 */
	public Integer updateByMessageId(ChatMessage bean,Long messageId) {

		return this.chatMessageMapper.updateByMessageId(bean, messageId);
	}

	/**
	 * 根据 MessageId删除
	 */
	public Integer deleteByMessageId(Long messageId) {

		return this.chatMessageMapper.deleteByMessageId(messageId);
	}
}

