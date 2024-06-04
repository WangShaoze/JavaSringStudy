package com.easyChat.controller;

import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.entity.po.UserContact;
import com.easyChat.services.UserContactService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description: 联系人 Controller
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@RestController
@RequestMapping("/userContactService")
public class UserContactController extends ABaseController {
	@Resource
	private UserContactService userContactService;

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

