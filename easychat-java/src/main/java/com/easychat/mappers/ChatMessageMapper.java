package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;



/**
 * @Description: 聊天消息表Mapper
 * @author: 王绍泽
 * @date: 2024/06/22
 */
public interface ChatMessageMapper<T, P> extends BaseMapper{
	/**
	 * 根据 MessageId 查询
	 */
	T selectByMessageId(@Param("messageId") Long messageId);

	/**
	 * 根据 MessageId 更新
	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Long messageId);

	/**
	 * 根据 MessageId 删除
	 */
	Integer deleteByMessageId(@Param("messageId") Long messageId);


}

