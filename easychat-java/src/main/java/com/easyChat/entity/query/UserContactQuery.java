package com.easyChat.entity.query;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Date;



/**
 * @Description: 联系人查询对象
 * @author: 王绍泽
 * @date: 2024/06/04
 */
public class UserContactQuery extends BaseQuery {
	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 联系人ID或者群组ID
	 */
	private String contactId;

	private String contactIdFuzzy;

	/**
	 * 联系人类型0:好友 1:群组
	 */
	private Integer contactType;

	/**
	 * 创建时间
	 */
	private Date createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 状态:非好友1:好友2:已删除好友3：被好友删除4:已拉黑好友5:被好友拉黑
	 */
	private Integer status;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	private String lastUpdateTimeStart;

	private String lastUpdateTimeEnd;

	/**
	 * 是否关联查询用户信息
	 * */
	private Boolean queryUserInfo;

	/**
	 * 是否关联查询群组信息
	 * */
	private Boolean queryGroupInfo;

	/**
	 * 是否关联查询联系人信息
	 * */
	private Boolean queryContactUserInfo;


	/**
	 * 是否需要自己创建的群组
	 * */
	private Boolean queryExcludeMyGroup;

	/**
	 * 联系人状态
	 * statusArray
	 * 	   NOT_FRIEND(0, "非好友"),
	 *     FRIEND(1, "好友"),
	 *     DEL(2, "已删除好友"),
	 *     DEL_BE(3, "被删除好友"),
	 *     BLACKLIST(4, "拉黑好友"),
	 *     BLACKLIST_BE(5, "被好友拉黑");
	 * */
	private Integer[] statusArray;

	public Integer[] getStatusArray() {
		return statusArray;
	}

	public void setStatusArray(Integer[] statusArray) {
		this.statusArray = statusArray;
	}

	public Boolean getQueryExcludeMyGroup() {
		return queryExcludeMyGroup;
	}

	public void setQueryExcludeMyGroup(Boolean queryExcludeMyGroup) {
		this.queryExcludeMyGroup = queryExcludeMyGroup;
	}


	public Boolean getQueryContactUserInfo() {
		return queryContactUserInfo;
	}

	public void setQueryContactUserInfo(Boolean queryContactUserInfo) {
		this.queryContactUserInfo = queryContactUserInfo;
	}

	public Boolean getQueryGroupInfo() {
		return queryGroupInfo;
	}

	public void setQueryGroupInfo(Boolean queryGroupInfo) {
		this.queryGroupInfo = queryGroupInfo;
	}

	public Boolean getQueryUserInfo() {
		return queryUserInfo;
	}

	public void setQueryUserInfo(Boolean queryUserInfo) {
		this.queryUserInfo = queryUserInfo;
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

	public void setUserIdFuzzy(String userIdFuzzy) { 
		this.userIdFuzzy = userIdFuzzy;
	}
	public String getUserIdFuzzy() { 
		return this.userIdFuzzy;
	}

	public void setContactIdFuzzy(String contactIdFuzzy) { 
		this.contactIdFuzzy = contactIdFuzzy;
	}
	public String getContactIdFuzzy() { 
		return this.contactIdFuzzy;
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

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) { 
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}
	public String getLastUpdateTimeStart() { 
		return this.lastUpdateTimeStart;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) { 
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}
	public String getLastUpdateTimeEnd() { 
		return this.lastUpdateTimeEnd;
	}


}

