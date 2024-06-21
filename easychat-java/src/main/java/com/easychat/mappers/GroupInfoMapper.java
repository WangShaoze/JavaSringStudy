package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;



/**
 * @Description: Mapper
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface GroupInfoMapper<T, P> extends BaseMapper{
	/**
	 * 根据 GroupId 查询
	 */
	T selectByGroupId(@Param("groupId") String groupId);

	/**
	 * 根据 GroupId 更新
	 */
	Integer updateByGroupId(@Param("bean") T t, @Param("groupId") String groupId);

	/**
	 * 根据 GroupId 删除
	 */
	Integer deleteByGroupId(@Param("groupId") String groupId);


}

