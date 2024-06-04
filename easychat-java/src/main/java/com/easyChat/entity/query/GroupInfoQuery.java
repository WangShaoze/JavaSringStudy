package com.easyChat.entity.query;

import java.util.Date;



/**
 * @Description: 查询对象
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public class GroupInfoQuery extends BaseQuery {
	/**
	 * 群ID
	 */
	private String groupId;

	private String groupIdFuzzy;

	/**
	 * 群组名
	 */
	private String groupName;

	private String groupNameFuzzy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 群公告
	 */
	private String groupNotice;

	private String groupNoticeFuzzy;

	/**
	 * 0：直接加入1：管理员同意后加入
	 */
	private Integer joinType;

	/**
	 * 状态1:正常 0:解散
	 */
	private Integer status;

	public void setGroupId(String groupId) { 
		this.groupId = groupId;
	}
	public String getGroupId() { 
		return this.groupId;
	}

	public void setGroupName(String groupName) { 
		this.groupName = groupName;
	}
	public String getGroupName() { 
		return this.groupName;
	}

	public void setCreateTime(Date createTime) { 
		this.createTime = createTime;
	}
	public Date getCreateTime() { 
		return this.createTime;
	}

	public void setGroupNotice(String groupNotice) { 
		this.groupNotice = groupNotice;
	}
	public String getGroupNotice() { 
		return this.groupNotice;
	}

	public void setJoinType(Integer joinType) { 
		this.joinType = joinType;
	}
	public Integer getJoinType() { 
		return this.joinType;
	}

	public void setStatus(Integer status) { 
		this.status = status;
	}
	public Integer getStatus() { 
		return this.status;
	}

	public void setGroupIdFuzzy(String groupIdFuzzy) { 
		this.groupIdFuzzy = groupIdFuzzy;
	}
	public String getGroupIdFuzzy() { 
		return this.groupIdFuzzy;
	}

	public void setGroupNameFuzzy(String groupNameFuzzy) { 
		this.groupNameFuzzy = groupNameFuzzy;
	}
	public String getGroupNameFuzzy() { 
		return this.groupNameFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) { 
		this.createTimeStart = createTimeStart;
	}
	public String getCreateTimeStart() { 
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) { 
		this.createTimeEnd = createTimeEnd;
	}
	public String getCreateTimeEnd() { 
		return this.createTimeEnd;
	}

	public void setGroupNoticeFuzzy(String groupNoticeFuzzy) { 
		this.groupNoticeFuzzy = groupNoticeFuzzy;
	}
	public String getGroupNoticeFuzzy() { 
		return this.groupNoticeFuzzy;
	}


}
