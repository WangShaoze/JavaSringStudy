package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;



/**
 * @Description: 联系人申请Mapper
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface UserContactApplyMapper<T, P> extends BaseMapper{
	/**
	 * 根据 ApplyId 查询
	 */
	T selectByApplyId(@Param("applyId") Integer applyId);

//	/**
//	 * 更具 查询体 更新
//	 * */
//	Integer updateByParam(@Param("bean") T t, @Param("query") P query);

	/**
	 *
	 * 根据 ApplyId 更新
	 */
	Integer updateByApplyId(@Param("bean") T t, @Param("applyId") Integer applyId);

	/**
	 * 根据 ApplyId 删除
	 */
	Integer deleteByApplyId(@Param("applyId") Integer applyId);


	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId 查询
	 */
	T selectByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId 更新
	 */
	Integer updateByApplyUserIdAndReceiveUserIdAndContactId(@Param("bean") T t, @Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId 删除
	 */
	Integer deleteByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);


}

