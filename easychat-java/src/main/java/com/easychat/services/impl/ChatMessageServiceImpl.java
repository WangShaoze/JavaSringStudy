package com.easychat.services.impl;

import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.query.ChatMessageQuery;
import com.easychat.services.ChatMessageService;
import com.easychat.mappers.ChatMessageMapper;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.vo.PaginationResultVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 聊天消息表 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/22
 */
@Service("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService{
	@Resource
	private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;


	/**
	 * 根据条件查询列表
	 */
	public List<ChatMessage> findListByParam(ChatMessageQuery query) {

		return this.chatMessageMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(ChatMessageQuery query) {

		return this.chatMessageMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<ChatMessage> list = this.findListByParam(query);
		PaginationResultVO<ChatMessage> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(ChatMessage bean) {
		return this.chatMessageMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<ChatMessage> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.chatMessageMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<ChatMessage> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.chatMessageMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 MessageId查询
	 */
	public ChatMessage getByMessageId(Long messageId) {
		return this.chatMessageMapper.selectByMessageId(messageId);
	}

	/**
	 * 根据 MessageId更新
	 */
	public Integer updateByMessageId(ChatMessage bean,Long messageId) {

		return this.chatMessageMapper.updateByMessageId(bean, messageId);
	}

	/**
	 * 根据 MessageId删除
	 */
	public Integer deleteByMessageId(Long messageId) {

		return this.chatMessageMapper.deleteByMessageId(messageId);
	}
}

