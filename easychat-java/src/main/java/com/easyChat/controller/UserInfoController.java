package com.easyChat.controller;

import com.easyChat.entity.query.UserInfoQuery;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.entity.po.UserInfo;
import com.easyChat.services.UserInfoService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description: 用户信息表 Controller
 * @author: 王绍泽
 * @date: 2024/05/27
 */
@RestController
@RequestMapping("/userInfoService")
public class UserInfoController extends ABaseController {
	@Resource
	private UserInfoService userInfoService;

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

