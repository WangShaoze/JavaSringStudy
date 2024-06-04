package com.easyChat.controller;

import com.easyChat.entity.query.UserInfoBeautyQuery;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.entity.po.UserInfoBeauty;
import com.easyChat.services.UserInfoBeautyService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description: 靓号表 Controller
 * @author: 王绍泽
 * @date: 2024/05/27
 */
@RestController
@RequestMapping("/userInfoBeautyService")
public class UserInfoBeautyController extends ABaseController {
	@Resource
	private UserInfoBeautyService userInfoBeautyService;

	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserInfoBeautyQuery query) {
		return getSuccessResponseVO(this.userInfoBeautyService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserInfoBeauty bean) {
		this.userInfoBeautyService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<UserInfoBeauty> listBean) {
		this.userInfoBeautyService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<UserInfoBeauty> listBean) {
		this.userInfoBeautyService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Id查询
	 */
	@RequestMapping("/getById")
	public ResponseVO<UserInfoBeauty> getById(Integer id) {
		return getSuccessResponseVO(this.userInfoBeautyService.getById(id));
	}

	/**
	 * 根据 Id更新
	 */
	@RequestMapping("/updateById")
	public ResponseVO updateById(UserInfoBeauty bean,Integer id) {
		this.userInfoBeautyService.updateById(bean, id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Id删除
	 */
	@RequestMapping("/deleteById")
	public ResponseVO deleteById(Integer id) {
		this.userInfoBeautyService.deleteById(id);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 UserId查询
	 */
	@RequestMapping("/getByUserId")
	public ResponseVO<UserInfoBeauty> getByUserId(String userId) {
		return getSuccessResponseVO(this.userInfoBeautyService.getByUserId(userId));
	}

	/**
	 * 根据 UserId更新
	 */
	@RequestMapping("/updateByUserId")
	public ResponseVO updateByUserId(UserInfoBeauty bean,String userId) {
		this.userInfoBeautyService.updateByUserId(bean, userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 UserId删除
	 */
	@RequestMapping("/deleteByUserId")
	public ResponseVO deleteByUserId(String userId) {
		this.userInfoBeautyService.deleteByUserId(userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Email查询
	 */
	@RequestMapping("/getByEmail")
	public ResponseVO<UserInfoBeauty> getByEmail(String email) {
		return getSuccessResponseVO(this.userInfoBeautyService.getByEmail(email));
	}

	/**
	 * 根据 Email更新
	 */
	@RequestMapping("/updateByEmail")
	public ResponseVO updateByEmail(UserInfoBeauty bean,String email) {
		this.userInfoBeautyService.updateByEmail(bean, email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 Email删除
	 */
	@RequestMapping("/deleteByEmail")
	public ResponseVO deleteByEmail(String email) {
		this.userInfoBeautyService.deleteByEmail(email);
		return getSuccessResponseVO(null);
	}
}

