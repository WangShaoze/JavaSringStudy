package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;



/**
 * @Description: 联系人Mapper
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface UserContactMapper<T, P> extends BaseMapper{
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

	/**
	 * 根据条件参数去查询
	 * */
	T selectByParam(@Param("query") P query);


	/**
	 * 根据条件参数去更新
	 * */
	Integer updateByParam(@Param("bean") T t, @Param("query") P query);
//
//	/**
//	 * 根据条件参数去删除
//	 * */
//	Integer delByParam(@Param("query") P query);

}

