package com.easyChat.services.impl;

import com.easyChat.entity.dto.SysSettingDto;
import com.easyChat.entity.po.UserContact;
import com.easyChat.entity.po.UserContactApply;
import com.easyChat.entity.query.UserContactApplyQuery;
import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.enums.ResponseCodeEnum;
import com.easyChat.enums.UserContactApplyStatusEnum;
import com.easyChat.enums.UserContactStatusEnum;
import com.easyChat.enums.UserContractTypeEnum;
import com.easyChat.exception.BusinessException;
import com.easyChat.mappers.UserContactMapper;
import com.easyChat.redis.RedisComponent;
import com.easyChat.services.UserContactApplyService;
import com.easyChat.mappers.UserContactApplyMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;
import javax.annotation.Resource;
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

		// TODO 如果是好友，接收人也添加申请人为好友 添加缓存

		// TODO 创建会话 发送消息

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

