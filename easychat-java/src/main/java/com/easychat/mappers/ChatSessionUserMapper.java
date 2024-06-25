package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;



/**
 * @Description: 会话用户Mapper
 * @author: 王绍泽
 * @date: 2024/06/22
 */
public interface ChatSessionUserMapper<T, P> extends BaseMapper{
	/**
	 * 根据 UserIdAndContactId 查询
	 */
	T selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	/**
	 * 根据 UserIdAndContactId 更新
	 */
	Integer updateByUserIdAndContactId(@Param("bean") T t, @Param("userId") String userId, @Param("contactId") String contactId);

	/**
	 * 根据 UserIdAndContactId 删除
	 */
	Integer deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);
}

