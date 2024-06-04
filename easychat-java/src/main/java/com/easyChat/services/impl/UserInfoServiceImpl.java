package com.easyChat.services.impl;

import com.easyChat.entity.config.AppConfig;
import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.dto.TokenUserInfoDto;
import com.easyChat.entity.po.UserInfo;
import com.easyChat.entity.po.UserInfoBeauty;
import com.easyChat.entity.query.UserInfoBeautyQuery;
import com.easyChat.entity.query.UserInfoQuery;
import com.easyChat.entity.vo.UserInfoVO;
import com.easyChat.enums.BeautyAccountStatusEnum;
import com.easyChat.enums.UserContractTypeEnum;
import com.easyChat.enums.UserStatusEnum;
import com.easyChat.exception.BusinessException;
import com.easyChat.mappers.UserInfoBeautyMapper;
import com.easyChat.redis.RedisComponent;
import com.easyChat.services.UserInfoService;
import com.easyChat.mappers.UserInfoMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.enums.DateTimePatternEnum;
import com.easyChat.utils.CopyTools;
import com.easyChat.utils.DateUtils;
import java.util.Date;

import com.easyChat.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jodd.util.ArraysUtil;
import jodd.util.StringUtil;
import org.springframework.format.annotation.DateTimeFormat;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		this.userInfoMapper.insert(userInfo);

		if (useBeautyAccount){
			UserInfoBeauty userInfoBeauty1 = new UserInfoBeauty();
			userInfoBeauty1.setStatus(BeautyAccountStatusEnum.USE.getStatus());
			userInfoBeautyMapper.updateById(userInfoBeauty1, userInfoBeauty.getId());
		}

		// TODO 创建机器人好友
	}

	@Override
	public UserInfoVO login(String email, String password) throws BusinessException {

		UserInfo userInfo = userInfoMapper.selectByEmail(email);
		if (userInfo==null||userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号不存存或密码不正确");
		}
		if (UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
			throw new BusinessException("账号已禁用");
		}

		// TODO 查询群组
		// TODO 查询联系人

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
