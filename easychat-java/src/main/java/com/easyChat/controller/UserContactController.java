package com.easyChat.controller;

import com.easyChat.annotation.GlobalInterceptor;
import com.easyChat.entity.dto.TokenUserInfoDto;
import com.easyChat.entity.dto.UserContactSearchResultDto;
import com.easyChat.entity.po.UserContactApply;
import com.easyChat.entity.po.UserInfo;
import com.easyChat.entity.query.UserContactApplyQuery;
import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.entity.po.UserContact;
import com.easyChat.enums.PageSize;
import com.easyChat.exception.BusinessException;
import com.easyChat.services.UserContactApplyService;
import com.easyChat.services.UserContactService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.easyChat.services.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description: 联系人 Controller
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@RestController
@RequestMapping("/contact")
public class UserContactController extends ABaseController {

	@Resource
	private UserContactService userContactService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private UserContactApplyService userContactApplyService;


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
	 * 添加好友
	 * @param contactId 需要搜索的用户或群组的ID
	 * @param applyInfo 申请信息
	 * */
	@RequestMapping("/add_contact")
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

