package com.easyChat.services;

import com.easyChat.entity.po.UserInfoBeauty;
import com.easyChat.entity.query.UserInfoBeautyQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description: 靓号表 业务接口
 * @author: 王绍泽
 * @date: 2024/05/27
 */
public interface UserInfoBeautyService {


	/**
	 * 根据条件查询列表
	 */
	List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserInfoBeautyQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery query);


	/**
	 * 新增
	 */
	Integer add(UserInfoBeauty bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfoBeauty> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserInfoBeauty> listBean);


	/**
	 * 根据 Id查询
	 */
	UserInfoBeauty getById(Integer id);


	/**
	 * 根据 Id更新
	 */
	Integer updateById(UserInfoBeauty bean, Integer id);


	/**
	 * 根据 Id删除
	 */
	Integer deleteById(Integer id);


	/**
	 * 根据 UserId查询
	 */
	UserInfoBeauty getByUserId(String userId);


	/**
	 * 根据 UserId更新
	 */
	Integer updateByUserId(UserInfoBeauty bean, String userId);


	/**
	 * 根据 UserId删除
	 */
	Integer deleteByUserId(String userId);


	/**
	 * 根据 Email查询
	 */
	UserInfoBeauty getByEmail(String email);


	/**
	 * 根据 Email更新
	 */
	Integer updateByEmail(UserInfoBeauty bean, String email);


	/**
	 * 根据 Email删除
	 */
	Integer deleteByEmail(String email);

}

