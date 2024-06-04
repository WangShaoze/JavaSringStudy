package com.easyChat.controller;

import com.easyChat.entity.query.GroupInfoQuery;
import com.easyChat.entity.vo.ResponseVO;
import com.easyChat.entity.po.GroupInfo;
import com.easyChat.services.GroupInfoService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description:  Controller
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@RestController
@RequestMapping("/groupInfoService")
public class GroupInfoController extends ABaseController {
	@Resource
	private GroupInfoService groupInfoService;

	@RequestMapping("/save_group")
	public ResponseVO saveGroup(HttpServletRequest request,
								@NotEmpty String groupId,
								String groupName,
								String groupNotice,
								@NotNull Integer joinType,
								MultipartFile avatarFile,
								MultipartFile avatarCover){

		String token = request.getHeader("token");

		return getSuccessResponseVO(null);
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

