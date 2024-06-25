package com.easychat.services.impl;

import com.easychat.entity.po.ChatSession;
import com.easychat.entity.query.ChatSessionQuery;
import com.easychat.services.ChatSessionService;
import com.easychat.mappers.ChatSessionMapper;
import com.easychat.entity.query.SimplePage;
import com.easychat.entity.vo.PaginationResultVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 会话信息 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/22
 */
@Service("chatSessionService")
public class ChatSessionServiceImpl implements ChatSessionService{
	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;


	/**
	 * 根据条件查询列表
	 */
	public List<ChatSession> findListByParam(ChatSessionQuery query) {

		return this.chatSessionMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(ChatSessionQuery query) {

		return this.chatSessionMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<ChatSession> list = this.findListByParam(query);
		PaginationResultVO<ChatSession> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(ChatSession bean) {
		return this.chatSessionMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<ChatSession> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.chatSessionMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<ChatSession> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.chatSessionMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 SessionId查询
	 */
	public ChatSession getBySessionId(String sessionId) {
		return this.chatSessionMapper.selectBySessionId(sessionId);
	}

	/**
	 * 根据 SessionId更新
	 */
	public Integer updateBySessionId(ChatSession bean,String sessionId) {

		return this.chatSessionMapper.updateBySessionId(bean, sessionId);
	}

	/**
	 * 根据 SessionId删除
	 */
	public Integer deleteBySessionId(String sessionId) {

		return this.chatSessionMapper.deleteBySessionId(sessionId);
	}
}

