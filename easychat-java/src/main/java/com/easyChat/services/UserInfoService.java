package com.easyChat.services;

import com.easyChat.entity.dto.TokenUserInfoDto;
import com.easyChat.entity.po.UserInfo;
import com.easyChat.entity.query.UserInfoQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.entity.vo.UserInfoVO;
import com.easyChat.exception.BusinessException;

import java.util.List;

/**
 * @Description: 用户信息表 业务接口
 * @author: 王绍泽
 * @date: 2024/05/27
 */
public interface UserInfoService {


	/**
	 * 根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserInfoQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);


	/**
	 * 新增
	 */
	Integer add(UserInfo bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);


	/**
	 * 根据 UserId查询
	 */
	UserInfo getByUserId(String userId);


	/**
	 * 根据 UserId更新
	 */
	Integer updateByUserId(UserInfo bean, String userId);


	/**
	 * 根据 UserId删除
	 */
	Integer deleteByUserId(String userId);


	/**
	 * 根据 Email查询
	 */
	UserInfo getByEmail(String email);


	/**
	 * 根据 Email更新
	 */
	Integer updateByEmail(UserInfo bean, String email);


	/**
	 * 根据 Email删除
	 */
	Integer deleteByEmail(String email);

	/**
	 * 注册
	 * */
	void register(String email, String nickName, String password) throws BusinessException;

	/**
	 * 登录
	 * */
	UserInfoVO login(String email, String password) throws BusinessException;
}

