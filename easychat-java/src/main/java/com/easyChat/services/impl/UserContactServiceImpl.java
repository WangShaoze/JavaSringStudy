package com.easyChat.services.impl;

import com.easyChat.entity.po.UserContact;
import com.easyChat.entity.query.UserContactQuery;
import com.easyChat.services.UserContactService;
import com.easyChat.mappers.UserContactMapper;
import com.easyChat.entity.query.SimplePage;
import com.easyChat.entity.vo.PaginationResultVO;
import com.easyChat.enums.DateTimePatternEnum;
import com.easyChat.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description: 联系人 业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("userContactService")
public class UserContactServiceImpl implements UserContactService{
	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;


	/**
	 * 根据条件查询列表
	 */
	public List<UserContact> findListByParam(UserContactQuery query) {

		return this.userContactMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(UserContactQuery query) {

		return this.userContactMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<UserContact> findListByPage(UserContactQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<UserContact> list = this.findListByParam(query);
		PaginationResultVO<UserContact> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(UserContact bean) {
		return this.userContactMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<UserContact> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<UserContact> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.userContactMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 UserIdAndContactId查询
	 */
	public UserContact getByUserIdAndContactId(String userId, String contactId) {
		return this.userContactMapper.selectByUserIdAndContactId(userId, contactId);
	}

	/**
	 * 根据 UserIdAndContactId更新
	 */
	public Integer updateByUserIdAndContactId(UserContact bean,String userId, String contactId) {

		return this.userContactMapper.updateByUserIdAndContactId(bean, userId, contactId);
	}

	/**
	 * 根据 UserIdAndContactId删除
	 */
	public Integer deleteByUserIdAndContactId(String userId, String contactId) {

		return this.userContactMapper.deleteByUserIdAndContactId(userId, contactId);
	}
}

