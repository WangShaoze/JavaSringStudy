package com.easyChat.services;

import com.easyChat.entity.dto.TokenUserInfoDto;
import com.easyChat.entity.dto.UserContactSearchResultDto;
import com.easyChat.entity.po.UserContact;
import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.enums.UserContactStatusEnum;
import com.easyChat.exception.BusinessException;

import java.util.List;

/**
 * @Description: 联系人 业务接口
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface UserContactService {

	/**
	 * 搜索好友
	 * @param userId 当前用户的id
	 * @param contactId 需要搜索的用户或群组的ID
	 * */
	UserContactSearchResultDto searchContact(String userId,String contactId);


	/**
	 * 添加好友
	 * @param tokenUserInfoDto 用户tokenDto中的信息
	 * @param contactId 需要搜索的用户或群组的ID
	 * @param applyInfo 申请信息
	 * */
	Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String applyInfo) throws BusinessException;


	/**
	 * 移除联系人
	 * @param userId 当前用户id
	 * @param contactId 联系人id
	 * @param contactStatusEnum DEL
	 * */
	void  removeUserContact(String userId, String contactId, UserContactStatusEnum contactStatusEnum);

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

