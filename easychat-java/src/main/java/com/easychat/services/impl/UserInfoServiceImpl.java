package com.easychat.services.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.entity.query.UserInfoBeautyQuery;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.enums.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserInfoBeautyMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.services.ChatSessionUserService;
import com.easychat.services.UserContactService;
import com.easychat.services.UserInfoService;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.utils.CopyTools;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.easychat.utils.StringUtils;
import com.easychat.websocket.MessageHandler;
import jodd.util.ArraysUtil;
import jodd.util.StringUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 用户信息表 业务接口实现
 * @author: 王绍泽
 * @date: 2024/05/27
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService{
	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private UserInfoBeautyMapper<UserInfoBeauty, UserInfoBeautyQuery> userInfoBeautyMapper;

	@Resource
	private AppConfig appConfig;

	@Resource
	private TokenUserInfoDto tokenUserInfoDto;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private UserContactService userContactService;

	@Resource
	private ChatSessionUserService chatSessionUserService;

	@Resource
	private MessageHandler messageHandler;

	/**
	 * 更新用户信息
	 * @param userInfo 当前登录的用户信息
	 * @param avatarFile 未压缩的大图
	 * @param avatarCover 压缩后的小图
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserInfo(UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
		if(avatarFile!=null){
			String baseFolder = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE;
			File targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_FILE_AVATAR_NAME);
			if (!targetFileFolder.exists()){
				targetFileFolder.mkdirs();
			}
			String filePath = targetFileFolder.getPath()+"/"+userInfo.getUserId()+Constants.IMAGE_SUFFIX;
			avatarFile.transferTo(new File(filePath));
			avatarCover.transferTo(new File(targetFileFolder.getParent()+"/"+userInfo.getUserId()+Constants.COVER_IMAGE_SUFFIX));
		}
		UserInfo dbInfo = userInfoMapper.selectByUserId(userInfo.getUserId());  // 细节：先查询效果更好，英伟更新相当于需要修改数据库，所以一定会开启事务，如果后面的要做的事情太多，这个查询效率就会很低
		userInfoMapper.updateByUserId(userInfo, userInfo.getUserId());

		String contactNameUpdate = null;
		if (!dbInfo.getNickName().equals(userInfo.getNickName())){
			contactNameUpdate = userInfo.getNickName();
		}
		if (contactNameUpdate==null){
			return;
		}

		// 更新token中的昵称
		TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByUserId(userInfo.getUserId());
		tokenUserInfoDto.setNickName(contactNameUpdate);
		redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

		// 更新昵称信息
		chatSessionUserService.updateRedundantInformation(contactNameUpdate, userInfo.getUserId());

	}

	/**
	 * 更新用户状态
	 *
	 * @param status 用户当前状态
	 * @param userId 用户id
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserStatus(Integer status, String userId) throws BusinessException {
		UserStatusEnum userStatusEnum = UserStatusEnum.getByStatus(status);
		// 判断状态是否符合要求
		if (userStatusEnum==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		// 更新状态
		UserInfo userInfo = new UserInfo();
		userInfo.setStatus(userStatusEnum.getStatus());
		this.userInfoMapper.updateByUserId(userInfo, userId);
	}

	/**
	 * 强制下线
	 *
	 * @param userId 用户id
	 */
	@Override
	public void forceOffline(String userId) throws BusinessException {
		// 强制下线
		MessageSendDto messageSendDto = new MessageSendDto();
		messageSendDto.setContactType(UserContractTypeEnum.USER.getType());
		messageSendDto.setMessageType(MessageTypeEnum.FORCE_OFF_LINE.getType());
		messageSendDto.setContactId(userId);
		messageHandler.sendMessage(messageSendDto);
	}

	/**
	 * 根据条件查询列表
	 */
	public List<UserInfo> findListByParam(UserInfoQuery query) {

		return this.userInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserInfoQuery query) {

		return this.userInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<UserInfo> list = this.findListByParam(query);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 UserId查询
	 */
	public UserInfo getByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据 UserId更新
	 */
	public Integer updateByUserId(UserInfo bean,String userId) {

		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据 UserId删除
	 */
	public Integer deleteByUserId(String userId) {

		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据 Email查询
	 */
	public UserInfo getByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据 Email更新
	 */
	public Integer updateByEmail(UserInfo bean,String email) {

		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据 Email删除
	 */
	public Integer deleteByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 注册
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String nickName, String password) throws BusinessException {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);

		if (userInfo!=null){
			throw new BusinessException("账号已经存在");
		}
		String userId = null;

		// 判断是否使用了靓号
		UserInfoBeauty userInfoBeauty = userInfoBeautyMapper.selectByEmail(email);
		boolean useBeautyAccount = (null!= userInfoBeauty)&&(userInfoBeauty.getStatus().equals(BeautyAccountStatusEnum.NO_USE.getStatus()));
		if (useBeautyAccount){
			userId = userInfoBeauty.getUserId();
		}else{
			userId = UserContractTypeEnum.USER.getPrefix()+StringUtils.getUserId();
		}

		Date curDate = new Date();
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setEmail(email);
		userInfo.setNickName(nickName);
		userInfo.setPassword(StringUtils.encodingMd5(password));
		userInfo.setCreateTime(curDate);
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setLastOffTime(curDate.getTime());
		userInfo.setJoinType(JoinTypeEnum.APPLY.getType());
		this.userInfoMapper.insert(userInfo);

		if (useBeautyAccount){
			UserInfoBeauty userInfoBeauty1 = new UserInfoBeauty();
			userInfoBeauty1.setStatus(BeautyAccountStatusEnum.USED.getStatus());
			userInfoBeautyMapper.updateById(userInfoBeauty1, userInfoBeauty.getId());
		}

		userContactService.addContact4Robot(userId);
	}

	@Override
	public UserInfoVO login(String email, String password) throws BusinessException {

		UserInfo userInfo = userInfoMapper.selectByEmail(email);
		if (userInfo==null||!userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号不存存或密码不正确");
		}
		if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
			// 禁用账号
			throw new BusinessException(ResponseCodeEnum.CODE_904);
		}

		//  查询联系人
		UserContactQuery contactQuery = new UserContactQuery();
		contactQuery.setUserId(userInfo.getUserId());
		contactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		List<UserContact> contactList = userContactService.findListByParam(contactQuery);
		List<String> contactIdList = contactList.stream().map(item->item.getContactId()).collect(Collectors.toList());

		// 将联系人列表添加到redis数据库中去
		redisComponent.clearUserContact(userInfo.getUserId());
		if (!contactIdList.isEmpty()){
			redisComponent.addUserContactBatch(userInfo.getUserId(), contactIdList);
		}

		// 判断他是不是管理员
		TokenUserInfoDto tokenUserInfoDto = getUserInfoToken(userInfo);


		// 根据心跳判断
		Long lastHeartBeat = redisComponent.getUserHeartBeat(userInfo.getUserId());
		if (lastHeartBeat !=null){
			throw new BusinessException("此账号已经在其他设备登录，请退出登录并重试！");
		}
		// 将token保存到redis中
		String token = StringUtils.encodingMd5(tokenUserInfoDto.getUserId()+StringUtils.getRandomString(Constants.LENGTH_20));
		tokenUserInfoDto.setToken(token);
		redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

		// 将 userInfo 中的数据拷贝到 UserInfoVO
		UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
		userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());
		userInfoVO.setToken(tokenUserInfoDto.getToken());
		return userInfoVO;
	}

	private TokenUserInfoDto getUserInfoToken(UserInfo userInfo) {
		TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
		tokenUserInfoDto.setUserId(userInfo.getUserId());
		tokenUserInfoDto.setNickName(userInfo.getNickName());

		// 判断他是不是管理员
		String adminEmails = appConfig.getAdminEmails();
		if (StringUtil.isNotEmpty(adminEmails)&&(ArraysUtil.contains(adminEmails.split(","), userInfo.getEmail()))){
			tokenUserInfoDto.setAdmin(true);
		}else {
			tokenUserInfoDto.setAdmin(false);
		}
		return tokenUserInfoDto;
	}


	public static void main(String[] args) {
		System.out.println(UserContractTypeEnum.getByName("user"));
		System.out.println(UserContractTypeEnum.getByName("group"));
		System.out.println(UserContractTypeEnum.getByPrefix("U1243143"));
		System.out.println(UserContractTypeEnum.getByPrefix("G87943232"));
		System.out.println(StringUtils.getUserId());
	}
}

