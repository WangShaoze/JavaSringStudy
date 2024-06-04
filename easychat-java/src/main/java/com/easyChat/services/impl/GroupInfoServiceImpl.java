package com.easyChat.services.impl;

import com.easyChat.entity.po.GroupInfo;
import com.easyChat.entity.query.GroupInfoQuery;
import com.easyChat.services.GroupInfoService;
import com.easyChat.mappers.GroupInfoMapper;
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
 * @Description:  业务接口实现
 * @author: 王绍泽
 * @date: 2024/06/04
 */
@Service("groupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService{
	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;


	/**
	 * 根据条件查询列表
	 */
	public List<GroupInfo> findListByParam(GroupInfoQuery query) {

		return this.groupInfoMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(GroupInfoQuery query) {

		return this.groupInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query) {
		Integer count = this.findCountByParam(query);
		SimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());
		query.setSimplePage(simplePage);
		List<GroupInfo> list = this.findListByParam(query);
		PaginationResultVO<GroupInfo> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(GroupInfo bean) {
		return this.groupInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<GroupInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.groupInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增/修改
	 */
	public Integer addOrUpdateBatch(List<GroupInfo> listBean) {
		if (listBean==null||listBean.isEmpty()){
			return 0;
		}
		return this.groupInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据 GroupId查询
	 */
	public GroupInfo getByGroupId(String groupId) {
		return this.groupInfoMapper.selectByGroupId(groupId);
	}

	/**
	 * 根据 GroupId更新
	 */
	public Integer updateByGroupId(GroupInfo bean,String groupId) {

		return this.groupInfoMapper.updateByGroupId(bean, groupId);
	}

	/**
	 * 根据 GroupId删除
	 */
	public Integer deleteByGroupId(String groupId) {

		return this.groupInfoMapper.deleteByGroupId(groupId);
	}
}

