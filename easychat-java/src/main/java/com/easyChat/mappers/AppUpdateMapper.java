package com.easyChat.mappers;

import com.easyChat.entity.po.AppUpdate;
import org.apache.ibatis.annotations.Param;



/**
 * @Description: app发布Mapper
 * @author: 王绍泽
 * @date: 2024/06/21
 */
public interface AppUpdateMapper<T, P> extends BaseMapper{
	/**
	 * 根据 Id 查询
	 */
	T selectById(@Param("id") Integer id);

	/**
	 * 根据 Id 更新
	 */
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);

	/**
	 * 根据 Id 删除
	 */
	Integer deleteById(@Param("id") Integer id);


	/**
	 * 根据 Version 查询
	 */
	T selectByVersion(@Param("version") String version);

	/**
	 * 根据 Version 更新
	 */
	Integer updateByVersion(@Param("bean") T t, @Param("version") String version);

	/**
	 * 根据 Version 删除
	 */
	Integer deleteByVersion(@Param("version") String version);

	/**
	 * 查询出最新的 更新版本
	 * @param appVersion 罪行更新的版本
	 * @param uid 用户id
	 * */
	T selectLatestUpdate(@Param("appVersion") String appVersion, @Param("uid") String uid);

}

