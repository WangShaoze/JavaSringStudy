package com.easychat.controller;

import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.po.UserContactApply;
import com.easychat.services.UserContactApplyService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description: 联系人申请 Controller
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@RestController("userContactApplyController")
@RequestMapping("/userContactApply")
public class UserContactApplyController extends ABaseController {
	@Resource
	private UserContactApplyService userContactApplyService;

	/**
	 * 加载数据
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserContactApplyQuery query) {
		return getSuccessResponseVO(this.userContactApplyService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(UserContactApply bean) {
		this.userContactApplyService.add(bean);
		return getSuccessResponseVO(null);	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(List<UserContactApply> listBean) {
		this.userContactApplyService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(List<UserContactApply> listBean) {
		this.userContactApplyService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 ApplyId查询
	 */
	@RequestMapping("/getByApplyId")
	public ResponseVO<UserContactApply> getByApplyId(Integer applyId) {
		return getSuccessResponseVO(this.userContactApplyService.getByApplyId(applyId));
	}

	/**
	 * 根据 ApplyId更新
	 */
	@RequestMapping("/updateByApplyId")
	public ResponseVO updateByApplyId(UserContactApply bean,Integer applyId) {
		this.userContactApplyService.updateByApplyId(bean, applyId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 ApplyId删除
	 */
	@RequestMapping("/deleteByApplyId")
	public ResponseVO deleteByApplyId(Integer applyId) {
		this.userContactApplyService.deleteByApplyId(applyId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	@RequestMapping("/getByApplyUserIdAndReceiveUserIdAndContactId")
	public ResponseVO<UserContactApply> getByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return getSuccessResponseVO(this.userContactApplyService.getByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId));
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	@RequestMapping("/updateByApplyUserIdAndReceiveUserIdAndContactId")
	public ResponseVO updateByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean,String applyUserId, String receiveUserId, String contactId) {
		this.userContactApplyService.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	@RequestMapping("/deleteByApplyUserIdAndReceiveUserIdAndContactId")
	public ResponseVO deleteByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		this.userContactApplyService.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
		return getSuccessResponseVO(null);
	}
}

