package com.easychat.services;

import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.query.ChatSessionUserQuery;
import com.easychat.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description: 会话用户 业务接口
 * @author: 王绍泽
 * @date: 2024/06/22
 */
public interface ChatSessionUserService {

	/**
	 * 更新冗余信息
	 * */
	void updateRedundantInformation(String contactNameUpdate, String userId);


	/**
	 * 根据条件查询列表
	 */
	List<ChatSessionUser> findListByParam(ChatSessionUserQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ChatSessionUserQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery query);


	/**
	 * 新增
	 */
	Integer add(ChatSessionUser bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatSessionUser> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ChatSessionUser> listBean);


	/**
	 * 根据 UserIdAndContactId查询
	 */
	ChatSessionUser getByUserIdAndContactId(String userId, String contactId);


	/**
	 * 根据 UserIdAndContactId更新
	 */
	Integer updateByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId);


	/**
	 * 根据 UserIdAndContactId删除
	 */
	Integer deleteByUserIdAndContactId(String userId, String contactId);

}

