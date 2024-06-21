package com.easychat.services.impl;

import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.GroupInfoQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.enums.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserContactMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.services.GroupInfoService;
import com.easychat.mappers.GroupInfoMapper;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.vo.PaginationResultVO;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description:  业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("groupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService{
	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private AppConfig appConfig;

	/**
	 * 解散群组操作
	 * @param groupOwnerId 群主id
	 * @param groupId 群 id
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dissolutionGroup(String groupOwnerId, String groupId) throws BusinessException {
		GroupInfo dbInfo = this.groupInfoMapper.selectByGroupId(groupId);
		if (null==dbInfo || !dbInfo.getGroupOwnerId().equals(groupOwnerId)){
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
		this.userContactMapper.updateByParam(userContact,userContactQuery);

		// TODO 移除相关群成员的联系人缓存
		// TODO 发消息 1.更行会话信息  2.记录群消息   3.发送解散通知消息

	}

	/**
	 * 保存或者更新群组信息
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws BusinessException, IOException {

		Date curDate = new Date();
		// 创建
		if (StringUtils.isEmpty(groupInfo.getGroupId())){
			GroupInfoQuery groupInfoQuery = new GroupInfoQuery();
			groupInfoQuery.setGroupOwnerId(groupInfo.getGroupOwnerId());
			Integer count = this.groupInfoMapper.selectCount(groupInfoQuery);
			SysSettingDto sysSettingDto = redisComponent.getSysSetting();

			if (count>= sysSettingDto.getMaxGroupCount()){
				throw new BusinessException("最多能支持创建"+sysSettingDto.getMaxGroupCount()+"个群聊");
			}

			if (null==avatarFile){
//				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}

			groupInfo.setCreateTime(curDate);
			groupInfo.setGroupId(UserContractTypeEnum.GROUP.getPrefix()+com.easychat.utils.StringUtils.getGroupId());
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

			// TODO 创建会话
			// TODO 发送消息
		}else {

			// 更新群组信息
			GroupInfo dbGroupInfo = this.groupInfoMapper.selectByGroupId(groupInfo.getGroupId());
			if (!dbGroupInfo.getGroupOwnerId().equals(groupInfo.getGroupOwnerId())){
				/*防止别人不走前端界面直接走接口，恶意攻击*/
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			this.groupInfoMapper.updateByGroupId(groupInfo, groupInfo.getGroupId());

			// TODO 更新相关表的冗余信息
			// TODO 修改群昵称发送ws消息（这类消息需要实时更新）
		}

		if (null==avatarFile){
			return;
		}
		String baseFolder = appConfig.getProjectFolder()+ Constants.FILE_FOLDER_FILE;
		File targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_FILE_AVATAR_NAME);
		if (!targetFileFolder.exists()){
			targetFileFolder.mkdirs();
		}
		String filePath = targetFileFolder.getPath()+"/"+groupInfo.getGroupId()+Constants.IMAGE_SUFFIX;


		// 上传头像的图片
		avatarFile.transferTo(new File(filePath));
		avatarCover.transferTo(new File(filePath+Constants.COVER_IMAGE_SUFFIX));
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
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.groupInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<GroupInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
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
	public Integer updateByGroupId(GroupInfo bean,String groupId) {

		return this.groupInfoMapper.updateByGroupId(bean, groupId);
	}

	/**
	 * 根据 GroupId删除
	 */
	public Integer deleteByGroupId(String groupId) {

		return this.groupInfoMapper.deleteByGroupId(groupId);
	}
}

