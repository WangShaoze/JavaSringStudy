package com.easychat.services;

import com.easychat.entity.po.ChatSession;
import com.easychat.entity.query.ChatSessionQuery;
import com.easychat.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description: 会话信息 业务接口
 * @author: 王绍泽
 * @date: 2024/06/22
 */
public interface ChatSessionService {


	/**
	 * 根据条件查询列表
	 */
	List<ChatSession> findListByParam(ChatSessionQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ChatSessionQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery query);


	/**
	 * 新增
	 */
	Integer add(ChatSession bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatSession> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ChatSession> listBean);


	/**
	 * 根据 SessionId查询
	 */
	ChatSession getBySessionId(String sessionId);


	/**
	 * 根据 SessionId更新
	 */
	Integer updateBySessionId(ChatSession bean, String sessionId);


	/**
	 * 根据 SessionId删除
	 */
	Integer deleteBySessionId(String sessionId);

}

