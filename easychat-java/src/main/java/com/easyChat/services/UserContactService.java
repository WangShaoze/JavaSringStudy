package com.easyChat.services;

import com.easyChat.entity.po.UserContact;
import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description: 联系人 业务接口
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface UserContactService {


	/**
	 * 根据条件查询列表
	 */
	List<UserContact> findListByParam(UserContactQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserContactQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<UserContact> findListByPage(UserContactQuery query);


	/**
	 * 新增
	 */
	Integer add(UserContact bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserContact> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserContact> listBean);


	/**
	 * 根据 UserIdAndContactId查询
	 */
	UserContact getByUserIdAndContactId(String userId, String contactId);


	/**
	 * 根据 UserIdAndContactId更新
	 */
	Integer updateByUserIdAndContactId(UserContact bean, String userId, String contactId);


	/**
	 * 根据 UserIdAndContactId删除
	 */
	Integer deleteByUserIdAndContactId(String userId, String contactId);

}

