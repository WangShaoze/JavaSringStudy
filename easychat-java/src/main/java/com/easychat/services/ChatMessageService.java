package com.easychat.services;

import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.query.ChatMessageQuery;
import com.easychat.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description: 聊天消息表 业务接口
 * @author: 王绍泽
 * @date: 2024/06/22
 */
public interface ChatMessageService {


	/**
	 * 根据条件查询列表
	 */
	List<ChatMessage> findListByParam(ChatMessageQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ChatMessageQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery query);


	/**
	 * 新增
	 */
	Integer add(ChatMessage bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatMessage> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ChatMessage> listBean);


	/**
	 * 根据 MessageId查询
	 */
	ChatMessage getByMessageId(Long messageId);


	/**
	 * 根据 MessageId更新
	 */
	Integer updateByMessageId(ChatMessage bean, Long messageId);


	/**
	 * 根据 MessageId删除
	 */
	Integer deleteByMessageId(Long messageId);

}

