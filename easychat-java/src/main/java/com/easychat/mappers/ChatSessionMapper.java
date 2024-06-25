package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;



/**
 * @Description: 会话信息Mapper
 * @author: 王绍泽
 * @date: 2024/06/22
 */
public interface ChatSessionMapper<T, P> extends BaseMapper{
	/**
	 * 根据 SessionId 查询
	 */
	T selectBySessionId(@Param("sessionId") String sessionId);

	/**
	 * 根据 SessionId 更新
	 */
	Integer updateBySessionId(@Param("bean") T t, @Param("sessionId") String sessionId);

	/**
	 * 根据 SessionId 删除
	 */
	Integer deleteBySessionId(@Param("sessionId") String sessionId);


}

