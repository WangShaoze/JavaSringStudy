package com.easyChat.entity.po;

import java.io.Serializable;
import com.easyChat.enums.DateTimePatternEnum;
import com.easyChat.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;



/**
 * @Description: 
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public class GroupInfo implements Serializable {
	/**
	 * 群ID
	 */
	private String groupId;

	/**
	 * 群组名
	 */
	private String groupName;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern ="YYYY-MM-dd HH:mm:ss", timezone ="GMT+8")
	@DateTimeFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 群公告
	 */
	private String groupNotice;

	/**
	 * 0：直接加入1：管理员同意后加入
	 */
	private Integer joinType;

	/**
	 * 状态1:正常 0:解散
	 */
	private Integer status;

	/**
	 * 群组id
	 */
	private String groupOwnerId;

	/**
	 * 群成员数量
	 * */
	private Integer memberCount;

	/**
	 * 群主昵称
	 * */
	private String groupOwnerNickName;

	public String getGroupOwnerNickName() {
		return groupOwnerNickName;
	}

	public void setGroupOwnerNickName(String groupOwnerNickName) {
		this.groupOwnerNickName = groupOwnerNickName;
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

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

	public void setGroupOwnerId(String groupOwnerId) { 
		this.groupOwnerId = groupOwnerId;
	}
	public String getGroupOwnerId() { 
		return this.groupOwnerId;
	}


	@Override
	public String toString() {
		return "群ID:"  + (groupId == null ? "空" : groupId ) + ", 群组名:"  + (groupName == null ? "空" : groupName ) + ", 创建时间:"  + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) ) + ", 群公告:"  + (groupNotice == null ? "空" : groupNotice ) + ", 0：直接加入1：管理员同意后加入:"  + (joinType == null ? "空" : joinType ) + ", 状态1:正常 0:解散:"  + (status == null ? "空" : status ) + ", 群组id:"  + (groupOwnerId == null ? "空" : groupOwnerId );
	}
}

