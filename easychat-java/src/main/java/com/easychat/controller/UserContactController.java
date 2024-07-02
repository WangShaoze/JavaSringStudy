package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.dto.UserContactSearchResultDto;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.enums.*;
import com.easychat.exception.BusinessException;
import com.easychat.services.GroupInfoService;
import com.easychat.services.UserContactApplyService;
import com.easychat.services.UserContactService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.easychat.services.UserInfoService;
import com.easychat.utils.CopyTools;
import jodd.util.ArraysUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @ClassName UserContactController
 * @Description 用户管理
 * @Author
 * @Date
 * */
@RestController
@RequestMapping("/contact")
public class UserContactController extends ABaseController {

	@Resource
	private UserContactService userContactService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private UserContactApplyService userContactApplyService;

	@Resource
	private GroupInfoService groupInfoService;


	/**
	 * 搜索好友
	 * @param contactId 需要搜索的用户或群组的ID
	 * */
	@RequestMapping("/search")
	@GlobalInterceptor
	public ResponseVO search(HttpServletRequest request, @NotEmpty String contactId){
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserContactSearchResultDto resultDto = userContactService.searchContact(tokenUserInfoDto.getUserId(), contactId);
		return getSuccessResponseVO(resultDto);
	}

	/**
	 * 添加联系人
	 * @param contactId 需要搜索的用户或群组的ID
	 * @param applyInfo 申请信息
	 * */
	@RequestMapping("/apply_add")
	@GlobalInterceptor
	public ResponseVO addContact(HttpServletRequest request, @NotEmpty String contactId, String applyInfo) throws BusinessException {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		Integer joinType = userContactService.applyAdd(tokenUserInfoDto, contactId, applyInfo);
		return getSuccessResponseVO(joinType);
	}

	/**
	 * 查询好友申请列表
	 * @param pageNo 页码
	 * */
	@RequestMapping("/load_apply")
	@GlobalInterceptor
	public ResponseVO loadApply(HttpServletRequest request, Integer pageNo){
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserContactApplyQuery applyQuery = new UserContactApplyQuery();
		applyQuery.setOrderBy("last_apply_time desc");
		applyQuery.setReceiveUserId(tokenUserInfoDto.getUserId());
		applyQuery.setPageNo(pageNo);
		applyQuery.setPageSize(PageSize.SIZE15.getSize());
		applyQuery.setQueryContactInfo(true);
		PaginationResultVO<UserContactApply> resultVO = userContactApplyService.findListByPage(applyQuery);
		return getSuccessResponseVO(resultVO);
	}

	/**
	 * 处理好友申请
	 * @param applyId 页码
	 * @param status (1:同意，2:拒绝，3: 拉黑)
	 * */
	@RequestMapping("/deal_with_apply")
	@GlobalInterceptor
	public ResponseVO dealWithApply(HttpServletRequest request, @NotNull Integer applyId, @NotNull Integer status) throws BusinessException {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		this.userContactApplyService.dealWithApply(tokenUserInfoDto.getUserId(), applyId, status);
		return getSuccessResponseVO(null);
	}

	/**
	 * 获取联系人列表
	 * @param contactType (U -> user 用户 || G -> group 群组)
	 * */
	@RequestMapping("/load_contact")
	@GlobalInterceptor
	public ResponseVO loadContact(HttpServletRequest request, @NotNull String contactType) throws BusinessException {
		UserContractTypeEnum  userContractTypeEnum = UserContractTypeEnum.getByName(contactType);
		if(null==userContractTypeEnum){  // 传入的联系人类型，如果没有在库中找到该类型说明传入参数错误
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserContactQuery userContactQuery = new UserContactQuery();
		userContactQuery.setUserId(tokenUserInfoDto.getUserId());
		userContactQuery.setContactType(userContractTypeEnum.getType());
		if (UserContractTypeEnum.USER==userContractTypeEnum){
			userContactQuery.setQueryUserInfo(true);
		}else if (UserContractTypeEnum.GROUP==userContractTypeEnum){
			userContactQuery.setQueryGroupInfo(true);
			userContactQuery.setQueryExcludeMyGroup(true);  // 排除属于自己的群组账号
		}
		userContactQuery.setOrderBy("last_update_time desc");
		userContactQuery.setStatusArray(new Integer[]{
				UserContactStatusEnum.FRIEND.getStatus(),
				UserContactStatusEnum.DEL_BE.getStatus(),
				UserContactStatusEnum.BLACKLIST_BE.getStatus(),
				UserContactStatusEnum.BLACKLIST_BE_FIRST.getStatus(),
		});
		List<UserContact> contactList = userContactService.findListByParam(userContactQuery);
		return getSuccessResponseVO(contactList);
	}

	/**
	 * 查询联系人的信息 (不一定是好友，可能是同一个群的)
	 * @param contactId 需要查询联系人的信息的ID
	 * */
	@RequestMapping("/get_contact_info")
	@GlobalInterceptor
	public ResponseVO getContactInfo(HttpServletRequest request, @NotNull String contactId) throws BusinessException {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserContractTypeEnum contractTypeEnum = UserContractTypeEnum.getByPrefix(contactId);
		if (contractTypeEnum==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		UserInfoVO userInfoVO = null;
		if (contractTypeEnum==UserContractTypeEnum.USER){
			UserInfo userInfo = userInfoService.getByUserId(contactId);
			userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
			userInfoVO.setConcatStatus(UserContactStatusEnum.NOT_FRIEND.getStatus());
			UserContact userContact = userContactService.getByUserIdAndContactId(tokenUserInfoDto.getUserId(), contactId);
			if (userContact!=null){
				userInfoVO.setConcatStatus(UserContactStatusEnum.FRIEND.getStatus());
			}
		}
		return getSuccessResponseVO(userInfoVO);
	}

	/**
	 * 查询联系人的信息 （一定是好友或者以前是好友）
	 * @param contactId 需要查询联系人的信息的ID
	 * */
	@RequestMapping("/get_contact_user_info")
	@GlobalInterceptor
	public ResponseVO getContactUserInfo(HttpServletRequest request, @NotNull String contactId) throws BusinessException{
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserContact userContact = userContactService.getByUserIdAndContactId(tokenUserInfoDto.getUserId(), contactId);
		if (null==userContact|| !ArraysUtil.contains(new Integer[]{
				UserContactStatusEnum.FRIEND.getStatus(),
				UserContactStatusEnum.DEL_BE.getStatus(),
				UserContactStatusEnum.BLACKLIST_BE.getStatus()
		}, userContact.getStatus())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);  // 抛出参数异常
		}
		UserInfo userInfo = userInfoService.getByUserId(contactId);
		UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
		return getSuccessResponseVO(userInfoVO);
	}


	/**
	 * 删除联系人
	 * @param contactId 联系人id
	 * */
	 @RequestMapping("del_contact")
	 @GlobalInterceptor
	 public ResponseVO delContact(HttpServletRequest request, @NotNull String contactId){
		 TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		 userContactService.removeUserContact(tokenUserInfoDto.getUserId(), contactId, UserContactStatusEnum.DEL);
		 return getSuccessResponseVO(null);
	 }

	/**
	 * 拉黑联系人
	 * @param contactId 联系人id
	 * */
	@RequestMapping("add_contact_to_black_list")
	@GlobalInterceptor
	public ResponseVO addContact2BlackList(HttpServletRequest request, @NotNull String contactId){
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		userContactService.removeUserContact(tokenUserInfoDto.getUserId(), contactId, UserContactStatusEnum.BLACKLIST);
		return getSuccessResponseVO(null);
	}


	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserContactQuery query) {
		return getSuccessResponseVO(this.userContactService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserContact bean) {
		this.userContactService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<UserContact> listBean) {
		this.userContactService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<UserContact> listBean) {
		this.userContactService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 UserIdAndContactId查询
	 */
	@RequestMapping("/getByUserIdAndContactId")
	public ResponseVO<UserContact> getByUserIdAndContactId(String userId, String contactId) {
		return getSuccessResponseVO(this.userContactService.getByUserIdAndContactId(userId, contactId));
	}

	/**
	 * 根据 UserIdAndContactId更新
	 */
	@RequestMapping("/updateByUserIdAndContactId")
	public ResponseVO updateByUserIdAndContactId(UserContact bean,String userId, String contactId) {
		this.userContactService.updateByUserIdAndContactId(bean, userId, contactId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 UserIdAndContactId删除
	 */
	@RequestMapping("/deleteByUserIdAndContactId")
	public ResponseVO deleteByUserIdAndContactId(String userId, String contactId) {
		this.userContactService.deleteByUserIdAndContactId(userId, contactId);
		return getSuccessResponseVO(null);
	}
}




