package com.easyChat.services.impl;

import com.easyChat.entity.config.AppConfig;
import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.dto.SysSettingDto;
import com.easyChat.entity.po.GroupInfo;
import com.easyChat.entity.po.UserContact;
import com.easyChat.entity.query.GroupInfoQuery;
import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.enums.ResponseCodeEnum;
import com.easyChat.enums.UserContactStatusEnum;
import com.easyChat.enums.UserContractTypeEnum;
import com.easyChat.exception.BusinessException;
import com.easyChat.mappers.UserContactMapper;
import com.easyChat.redis.RedisComponent;
import com.easyChat.redis.RedisUtils;
import com.easyChat.services.GroupInfoService;
import com.easyChat.mappers.GroupInfoMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.enums.DateTimePatternEnum;
import com.easyChat.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.cj.result.Field;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
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
			groupInfo.setGroupId(UserContractTypeEnum.GROUP.getPrefix()+com.easyChat.utils.StringUtils.getGroupId());
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

