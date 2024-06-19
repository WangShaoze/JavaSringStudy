package com.easyChat.controller;

import com.easyChat.annotation.GlobalInterceptor;
import com.easyChat.entity.constants.Constants;
import com.easyChat.entity.dto.TokenUserInfoDto;
import com.easyChat.entity.query.UserInfoQuery;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.entity.po.UserInfo;
import com.easyChat.entity.vo.UserInfoVO;
import com.easyChat.services.UserInfoService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.easyChat.utils.CopyTools;
import com.easyChat.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description: 用户信息表 Controller
 * @author: 王绍泽
 * @date: 2024/05/27
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController {
	@Resource
	private UserInfoService userInfoService;


	/**
	 * 获取当前用户的信息
	 * */
	@RequestMapping("get_user_info")
	@GlobalInterceptor
	public ResponseVO getUserInfo(HttpServletRequest request){
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserInfo userInfo = userInfoService.getByUserId(tokenUserInfoDto.getUserId());
		UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
		userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());
		return getSuccessResponseVO(userInfoVO);
	}

	/**
	 * 保存用户信息
	 * @param userInfo  当前登录的用户，用户信息
	 * @param avatarFile 未压缩的大图
	 * @param avatarCover 压缩后的小图
	 * */
	@RequestMapping("save_user_info")
	@GlobalInterceptor
	public ResponseVO saveUserInfo(
			HttpServletRequest request,
		   	UserInfo userInfo,
		   	MultipartFile avatarFile,
		   	MultipartFile avatarCover
		   ) throws IOException {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		userInfo.setUserId(tokenUserInfoDto.getUserId());
		userInfo.setPassword(null);
		userInfo.setStatus(null);
		userInfo.setCreateTime(null);
		userInfo.setLastLoginTime(null);
		this.userInfoService.updateUserInfo(userInfo, avatarFile, avatarCover);
		return getUserInfo(request);
	}


	/**
	 * 更新密码
	 * @param newPassword  新密码
	 * */
	@RequestMapping("update_password")
	@GlobalInterceptor
	public ResponseVO updatePassword(HttpServletRequest request,
									 @NotEmpty @Pattern(regexp = Constants.REGEXP_PASSWORD) String newPassword) {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		UserInfo userInfo = new UserInfo();
		userInfo.setPassword(StringUtils.encodingMd5(newPassword));
		this.userInfoService.updateByUserId(userInfo, tokenUserInfoDto.getUserId());
		// TODO 强制退出，重新登录
		return getSuccessResponseVO(null);
	}


	/**
	 *  退出登录
	 * */
	@RequestMapping("logout")
	@GlobalInterceptor
	public ResponseVO logout(HttpServletRequest request) {
		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
		// TODO 退出登录 关闭 WS 连接
		return getSuccessResponseVO(null);
	}




	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserInfoQuery query) {
		return getSuccessResponseVO(this.userInfoService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserInfo bean) {
		this.userInfoService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<UserInfo> listBean) {
		this.userInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<UserInfo> listBean) {
		this.userInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 UserId查询
	 */
	@RequestMapping("/getByUserId")
	public ResponseVO<UserInfo> getByUserId(String userId) {
		return getSuccessResponseVO(this.userInfoService.getByUserId(userId));
	}

	/**
	 * 根据 UserId更新
	 */
	@RequestMapping("/updateByUserId")
	public ResponseVO updateByUserId(UserInfo bean,String userId) {
		this.userInfoService.updateByUserId(bean, userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 UserId删除
	 */
	@RequestMapping("/deleteByUserId")
	public ResponseVO deleteByUserId(String userId) {
		this.userInfoService.deleteByUserId(userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Email查询
	 */
	@RequestMapping("/getByEmail")
	public ResponseVO<UserInfo> getByEmail(String email) {
		return getSuccessResponseVO(this.userInfoService.getByEmail(email));
	}

	/**
	 * 根据 Email更新
	 */
	@RequestMapping("/updateByEmail")
	public ResponseVO updateByEmail(UserInfo bean,String email) {
		this.userInfoService.updateByEmail(bean, email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Email删除
	 */
	@RequestMapping("/deleteByEmail")
	public ResponseVO deleteByEmail(String email) {
		this.userInfoService.deleteByEmail(email);
		return getSuccessResponseVO(null);
	}
}

