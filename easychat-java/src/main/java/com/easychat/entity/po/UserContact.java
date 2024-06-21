package com.easychat.entity.po;

import java.io.Serializable;
import com.easychat.enums.DateTimePatternEnum;
import com.easychat.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;



/**
 * @Description: 联系人
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public class UserContact implements Serializable {
	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 联系人ID或者群组ID
	 */
	private String contactId;

	/**
	 * 联系人类型0:好友 1:群组
	 */
	private Integer contactType;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern ="YYYY-MM-dd HH:mm:ss", timezone ="GMT+8")
	@DateTimeFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 状态:非好友1:好友2:已删除好友3：被好友删除4:已拉黑好友5:被好友拉黑
	 */
	private Integer status;

	/**
	 * 最后更新时间
	 */
	@JsonFormat(pattern ="YYYY-MM-dd HH:mm:ss", timezone ="GMT+8")
	@DateTimeFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date lastUpdateTime;


	/**
	 * 补充:
	 * 联系人是用户的时候 需要关联查询出 nick_name -> contactName sex
	 * 联系人是群组的时候 需要关联查询出 group_name -> contactName
	 * */
	private String contactName;
	private String sex;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setUserId(String userId) { 
		this.userId = userId;
	}
	public String getUserId() { 
		return this.userId;
	}

	public void setContactId(String contactId) { 
		this.contactId = contactId;
	}
	public String getContactId() { 
		return this.contactId;
	}

	public void setContactType(Integer contactType) { 
		this.contactType = contactType;
	}
	public Integer getContactType() { 
		return this.contactType;
	}

	public void setCreateTime(Date createTime) { 
		this.createTime = createTime;
	}
	public Date getCreateTime() { 
		return this.createTime;
	}

	public void setStatus(Integer status) { 
		this.status = status;
	}
	public Integer getStatus() { 
		return this.status;
	}

	public void setLastUpdateTime(Date lastUpdateTime) { 
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getLastUpdateTime() { 
		return this.lastUpdateTime;
	}


	@Override
	public String toString() {
		return "用户ID:"  + (userId == null ? "空" : userId ) + ", 联系人ID或者群组ID:"  + (contactId == null ? "空" : contactId ) + ", 联系人类型0:好友 1:群组:"  + (contactType == null ? "空" : contactType ) + ", 创建时间:"  + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) ) + ", 状态:非好友1:好友2:已删除好友3：被好友删除4:已拉黑好友5:被好友拉黑:"  + (status == null ? "空" : status ) + ", 最后更新时间:"  + (lastUpdateTime == null ? "空" : DateUtils.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) );
	}
}

