package com.easyChat.services.impl;

import com.easyChat.entity.po.UserContactApply;
import com.easyChat.entity.query.UserContactApplyQuery;
import com.easyChat.services.UserContactApplyService;
import com.easyChat.mappers.UserContactApplyMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 联系人申请 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("userContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService{
	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;


	/**
	 * 根据条件查询列表
	 */
	public List<UserContactApply> findListByParam(UserContactApplyQuery query) {

		return this.userContactApplyMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserContactApplyQuery query) {

		return this.userContactApplyMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<UserContactApply> list = this.findListByParam(query);
		PaginationResultVO<UserContactApply> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserContactApply bean) {
		return this.userContactApplyMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserContactApply> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactApplyMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<UserContactApply> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 ApplyId查询
	 */
	public UserContactApply getByApplyId(Integer applyId) {
		return this.userContactApplyMapper.selectByApplyId(applyId);
	}

	/**
	 * 根据 ApplyId更新
	 */
	public Integer updateByApplyId(UserContactApply bean,Integer applyId) {

		return this.userContactApplyMapper.updateByApplyId(bean, applyId);
	}

	/**
	 * 根据 ApplyId删除
	 */
	public Integer deleteByApplyId(Integer applyId) {

		return this.userContactApplyMapper.deleteByApplyId(applyId);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId查询
	 */
	public UserContactApply getByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId更新
	 */
	public Integer updateByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean,String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
	}

	/**
	 * 根据 ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	public Integer deleteByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {

		return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}
}

