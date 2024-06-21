package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.GroupInfoQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.entity.vo.GroupInfoVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.po.GroupInfo;
import com.easychat.enums.GroupStatusEnum;
import com.easychat.enums.UserContactStatusEnum;
import com.easychat.exception.BusinessException;
import com.easychat.services.GroupInfoService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.easychat.services.UserContactService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description:  Controller
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@RestController("groupInfoController")
@RequestMapping("/group")
public class GroupInfoController extends ABaseController {
	@Resource
	private GroupInfoService groupInfoService;

	@Resource
	private UserContactService userContactService;

	/**
	 * 保存或者更新群组相关信息
	 * */
	@RequestMapping("/save_group")
	@GlobalInterceptor
	public ResponseVO saveGroup(HttpServletRequest request,
								String groupId,
								String groupName,
								String groupNotice,
								@NotNull Integer joinType,
								MultipartFile avatarFile,
								MultipartFile avatarCover) throws BusinessException, IOException {

		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		GroupInfo groupInfo = new GroupInfo();
		groupInfo.setGroupId(groupId);
		groupInfo.setGroupName(groupName);
		groupInfo.setGroupNotice(groupNotice);
		groupInfo.setJoinType(joinType);
		groupInfo.setGroupOwnerId(tokenUserInfoDto.getUserId());
		groupInfoService.saveGroup(groupInfo, avatarFile, avatarCover);

		return getSuccessResponseVO(null);
	}

	/**
	 * 加载群组
	 * */
	@RequestMapping("/load_my_group")
	@GlobalInterceptor
	public ResponseVO loadMyGroup(HttpServletRequest request){
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		GroupInfo groupInfo = new GroupInfo();
		GroupInfoQuery groupInfoQuery = new GroupInfoQuery();
		groupInfoQuery.setGroupOwnerId(tokenUserInfoDto.getUserId());
		groupInfoQuery.setOrderBy("create_time desc");
		List<GroupInfo> groupInfoList = groupInfoService.findListByParam(groupInfoQuery);
		return getSuccessResponseVO(groupInfoList);
	}

	/**
	 * 获取群组信息
	 * */
	@RequestMapping("/get_group_info")
	@GlobalInterceptor
	public ResponseVO getGroupInfo(HttpServletRequest request, @NotEmpty String groupId) throws BusinessException {
		GroupInfo groupInfo = getGroupDetailCommon(request, groupId);
		UserContactQuery query = new UserContactQuery();
		query.setContactId(groupId);
		Integer memberCount = userContactService.findCountByParam(query);
		groupInfo.setMemberCount(memberCount);
		return getSuccessResponseVO(groupInfo);
	}

	public GroupInfo getGroupDetailCommon(HttpServletRequest request, String groupId) throws BusinessException {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		// 查询当前用户联系人是否有这么一个群，没有不能提供群聊信息
		UserContact userContact = userContactService.getByUserIdAndContactId(tokenUserInfoDto.getUserId(), groupId);
		if (userContact==null|| !UserContactStatusEnum.FRIEND.getStatus().equals(userContact.getStatus())){
			throw new BusinessException("你不在群聊或者群聊不存在或者已解散");
		}
		GroupInfo groupInfo = groupInfoService.getByGroupId(groupId);
		// 查询群聊是否存在或者已经解散了
		if (groupId==null||!GroupStatusEnum.NORMAL.getStatus().equals(groupInfo.getStatus())){
			throw new BusinessException("群聊不存在或已解散！！");
		}
		return groupInfo;
	}

	/**
	 * 获取群聊群详细信息
	 * */
	@RequestMapping("/get_group_chat_info")
	@GlobalInterceptor
	public ResponseVO getGroupChatInfo(HttpServletRequest request, @NotEmpty String groupId) throws BusinessException {
		GroupInfo groupInfo = getGroupDetailCommon(request, groupId);
		UserContactQuery query = new UserContactQuery();
		query.setQueryUserInfo(true);
		query.setContactId(groupId);
		query.setOrderBy("create_time asc");
		query.setContactType(UserContactStatusEnum.FRIEND.getStatus());
		List<UserContact> userContactList = userContactService.findListByParam(query);
		GroupInfoVO groupInfoVO = new GroupInfoVO();
		groupInfoVO.setGroupInfo(groupInfo);
		groupInfoVO.setUserContactList(userContactList);
		return getSuccessResponseVO(groupInfo);
	}


	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(GroupInfoQuery query) {
		return getSuccessResponseVO(this.groupInfoService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(GroupInfo bean) {
		this.groupInfoService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<GroupInfo> listBean) {
		this.groupInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<GroupInfo> listBean) {
		this.groupInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 GroupId查询
	 */
	@RequestMapping("/getByGroupId")
	public ResponseVO<GroupInfo> getByGroupId(String groupId) {
		return getSuccessResponseVO(this.groupInfoService.getByGroupId(groupId));
	}

	/**
	 * 根据 GroupId更新
	 */
	@RequestMapping("/updateByGroupId")
	public ResponseVO updateByGroupId(GroupInfo bean,String groupId) {
		this.groupInfoService.updateByGroupId(bean, groupId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 GroupId删除
	 */
	@RequestMapping("/deleteByGroupId")
	public ResponseVO deleteByGroupId(String groupId) {
		this.groupInfoService.deleteByGroupId(groupId);
		return getSuccessResponseVO(null);
	}


}

