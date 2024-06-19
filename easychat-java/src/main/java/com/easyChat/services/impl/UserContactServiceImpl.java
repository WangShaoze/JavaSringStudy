package com.easyChat.services.impl;

import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.dto.TokenUserInfoDto;
import com.easyChat.entity.dto.UserContactSearchResultDto;
import com.easyChat.entity.po.GroupInfo;
import com.easyChat.entity.po.UserContact;
import com.easyChat.entity.po.UserContactApply;
import com.easyChat.entity.po.UserInfo;
import com.easyChat.entity.query.*;
import com.easyChat.enums.*;
import com.easyChat.exception.BusinessException;
import com.easyChat.mappers.GroupInfoMapper;
import com.easyChat.mappers.UserContactApplyMapper;
import com.easyChat.mappers.UserInfoMapper;
import com.easyChat.services.UserContactApplyService;
import com.easyChat.services.UserContactService;
import com.easyChat.mappers.UserContactMapper;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.utils.CopyTools;
import com.easyChat.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jodd.util.ArraysUtil;
import jodd.util.StringUtil;
import org.springframework.format.annotation.DateTimeFormat;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 联系人 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("userContactService")
public class UserContactServiceImpl implements UserContactService{
	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

	@Resource
	private UserContactApplyService userContactApplyService;

	/**
	 * 搜索好友
	 * @param userId 当前用户的id
	 * @param contactId 需要搜索的用户或群组的ID
	 * */
	@Override
	public UserContactSearchResultDto searchContact(String userId, String contactId) {
		// 根据 需要搜索的用户或群组的ID 前缀获取它的联系类型，是群组还是用户
		UserContractTypeEnum userContractTypeEnum = UserContractTypeEnum.getByPrefix(contactId);
		if (userContractTypeEnum==null){
			// 没有收到联系类型
			return null;
		}
		UserContactSearchResultDto resultDto = new UserContactSearchResultDto();
		switch (userContractTypeEnum){
			case USER:
				// 如果是用户 获取用户信息
				UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
				if (userInfo==null){
					return null;
				}
				resultDto = CopyTools.copy(userInfo, UserContactSearchResultDto.class);
				break;
			case GROUP:
				GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
				if (groupInfo==null){
					return null;
				}
				resultDto.setNickName(groupInfo.getGroupName());
				break;
		}
		resultDto.setContactId(contactId);
		resultDto.setContactType(userContractTypeEnum.toString());

		// 如果搜索的是自己
		if (userId.equals(contactId)){
			resultDto.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			return resultDto;
		}

		// 查询是否是好友,根据状态后面的操作是不一样的
		UserContact userContact = userContactMapper.selectByUserIdAndContactId(userId, contactId);
		resultDto.setStatus(userContact==null?null:userContact.getStatus());
		return resultDto;
	}

	/**
	 * 添加好友
	 * @param tokenUserInfoDto 用户tokenDto中的信息
	 * @param contactId 需要搜索的用户或群组的ID
	 * @param applyInfo 申请信息
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String applyInfo) throws BusinessException {
		UserContractTypeEnum userContractTypeEnum = UserContractTypeEnum.getByPrefix(contactId);
		if (userContractTypeEnum==null){
			// 正常不会出现，如果出现说明该操作者没有走前端页面
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		// 获取申请人信息
		String applyUserId = tokenUserInfoDto.getUserId();

		// 默认申请信息,如果没有申请信息，也需要给出一个默认的申请信息
		applyInfo = StringUtil.isEmpty(applyInfo)?String.format(Constants.APPLY_INFO_TEMPLATE, tokenUserInfoDto.getNickName()):applyInfo;

		Long curTime = System.currentTimeMillis();
		Integer joinType;
		String receiveUserId=contactId;  // 如果是加群聊通知群主，如果是加用户，那就直接是该用户的id

		// 查询对方好友是否已经添加，如果已经拉黑无法添加
		UserContact userContact = userContactMapper.selectByUserIdAndContactId(applyUserId, contactId);
		if (userContact!=null&& ArraysUtil.contains(new Integer[]{
				UserContactStatusEnum.BLACKLIST_BE.getStatus(),
				UserContactStatusEnum.BLACKLIST_BE_FIRST.getStatus()
		}, userContact.getStatus())){
			throw new BusinessException("对方已将你拉黑，无法添加");
		}

		if (userContractTypeEnum==UserContractTypeEnum.GROUP){
			// 如果加群聊
			GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
			if (groupInfo==null|| groupInfo.getStatus().equals(GroupStatusEnum.DISSOLUTION.getStatus())){
				throw new BusinessException("群聊不存在或者已经解散");
			}
			// 加群聊通知群主
			receiveUserId =groupInfo.getGroupOwnerId();
			joinType = groupInfo.getJoinType();
		}else{
			// 如果是加 用户
			UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
			if (userInfo==null){
				// 正常不会出现，如果出现说明该操作者没有走前端页面
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			joinType = userInfo.getJoinType();
		}

		// 直接加入不用记录申请记录
		if (joinType.equals(JoinTypeEnum.JOIN.getType())){
			// 添加联系人
			userContactApplyService.addContact(
					applyUserId, receiveUserId, contactId,
					userContractTypeEnum.getType(), applyInfo);
			return joinType;
		}
		UserContactApply userContactApplyInDB = userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
		if (userContactApplyInDB==null){
			UserContactApply userContactApply = new UserContactApply();
			userContactApply.setApplyUserId(applyUserId);
			userContactApply.setReceiveUserId(receiveUserId);
			userContactApply.setContactId(contactId);
			userContactApply.setContactType(userContractTypeEnum.getType());
			userContactApply.setLastApplyTime(curTime);
			userContactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			userContactApply.setApplyInfo(applyInfo);
			userContactApplyMapper.insert(userContactApply);
		}else{
			// 更新状态
			UserContactApply userContactApply = new UserContactApply();
			userContactApply.setLastApplyTime(curTime);
			userContactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			userContactApply.setApplyInfo(applyInfo);
			userContactApplyMapper.updateByApplyId(userContactApply, userContactApplyInDB.getApplyId());
		}

		if (userContactApplyInDB==null||!userContactApplyInDB.getStatus().equals(UserContactApplyStatusEnum.INIT.getStatus())){
			// TODO 发送 ws 消息
		}
		return joinType;
	}


	/**
	 * 移除联系人
	 * @param userId 当前用户id
	 * @param contactId 联系人id
	 * @param contactStatusEnum DEL
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeUserContact(String userId, String contactId, UserContactStatusEnum contactStatusEnum) {
		// 移除好友
		UserContact userContact = new UserContact();
		userContact.setStatus(contactStatusEnum.getStatus());
		userContactMapper.updateByUserIdAndContactId(userContact, userId, contactId);

		// 在好友的联系人列表中移除自己
		UserContact friendContact = new UserContact();
		if(UserContactStatusEnum.DEL == contactStatusEnum){
			friendContact.setStatus(UserContactStatusEnum.DEL_BE.getStatus());
		} else if(UserContactStatusEnum.BLACKLIST == contactStatusEnum){
			friendContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
		}
		userContactMapper.updateByUserIdAndContactId(friendContact, contactId, userId);

		// TODO 从我的好友列表缓存中删除好友
		// TODO 从好友列表缓存中删除我

	}

	/**
	 * 根据条件查询列表
	 */
	public List<UserContact> findListByParam(UserContactQuery query) {

		return this.userContactMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserContactQuery query) {

		return this.userContactMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserContact> findListByPage(UserContactQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<UserContact> list = this.findListByParam(query);
		PaginationResultVO<UserContact> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserContact bean) {
		return this.userContactMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserContact> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<UserContact> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 UserIdAndContactId查询
	 */
	public UserContact getByUserIdAndContactId(String userId, String contactId) {
		return this.userContactMapper.selectByUserIdAndContactId(userId, contactId);
	}

	/**
	 * 根据 UserIdAndContactId更新
	 */
	public Integer updateByUserIdAndContactId(UserContact bean,String userId, String contactId) {

		return this.userContactMapper.updateByUserIdAndContactId(bean, userId, contactId);
	}

	/**
	 * 根据 UserIdAndContactId删除
	 */
	public Integer deleteByUserIdAndContactId(String userId, String contactId) {

		return this.userContactMapper.deleteByUserIdAndContactId(userId, contactId);
	}
}

