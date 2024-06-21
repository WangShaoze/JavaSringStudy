package com.easyChat.services;

import com.easyChat.entity.po.GroupInfo;
import com.easyChat.entity.query.GroupInfoQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description:  业务接口
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface GroupInfoService {

	/**
	 * 解散群组操作
	 * @param groupOwnerId 群主id
	 * @param groupId 群 id
	 * */
	void dissolutionGroup(String groupOwnerId, String groupId) throws BusinessException;


	/**
	 * 保存或者更新群组信息
	 * */
	void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws BusinessException, IOException;


	/**
	 * 根据条件查询列表
	 */
	List<GroupInfo> findListByParam(GroupInfoQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(GroupInfoQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query);


	/**
	 * 新增
	 */
	Integer add(GroupInfo bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<GroupInfo> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<GroupInfo> listBean);


	/**
	 * 根据 GroupId查询
	 */
	GroupInfo getByGroupId(String groupId);


	/**
	 * 根据 GroupId更新
	 */
	Integer updateByGroupId(GroupInfo bean, String groupId);


	/**
	 * 根据 GroupId删除
	 */
	Integer deleteByGroupId(String groupId);

}

