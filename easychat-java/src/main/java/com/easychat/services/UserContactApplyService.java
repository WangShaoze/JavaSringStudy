package com.easychat.services;

import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.exception.BusinessException;

import java.util.List;

/**
 * @Description: 联系人申请 业务接口
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public interface UserContactApplyService {

	/**
	 * @param userId 当前用户id
	 * @param applyId 页码
	 * @param status (1:同意，2:拒绝，3: 拉黑)
	 * */
	void dealWithApply(String userId, Integer applyId, Integer status) throws BusinessException;


	/**
	 * @param applyUserId 申请人的用户id
	 * @param receiveUserId 接收者的用户id
	 * @param contactId 联系人id( userId or groupId)
	 * @param contactType 联系人类型( user or group )
	 * @param applyInfo 申请信息
	 * */
	void addContact(String applyUserId,String receiveUserId, String contactId, Integer contactType, String applyInfo) throws BusinessException;

	/**
	 * 根据条件查询列表
	 */
	List<UserContactApply> findListByParam(UserContactApplyQuery query);


	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserContactApplyQuery query);


	/**
	 * 分页查询
	 */
	PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query);


	/**
	 * 新增
	 */
	Integer add(UserContactApply bean);


	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserContactApply> listBean);


	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserContactApply> listBean);


	/**
	 * 根据 ApplyId查询
	 */
	UserContactApply getByApplyId(Integer applyId);


	/**
	 * 根据 ApplyId更新
	 */
	Integer updateByApplyId(UserContactApply bean, Integer applyId);


	/**
	 * 根据 ApplyId删除
	 */
	Integer deleteByApplyId(Integer applyId);


	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	UserContactApply getByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);


	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	Integer updateByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId);


	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	Integer deleteByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);

}

