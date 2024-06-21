package com.easyChat.entity.po;

import java.io.Serializable;
import com.easyChat.enums.DateTimePatternEnum;
import com.easyChat.utils.DateUtils;
import java.util.Date;

import com.easyChat.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;



/**
 * @Description: app发布
 * @author: 王绍泽
 * @date: 2024/06/21
 */
public class AppUpdate implements Serializable {
	/**
	 * 自增ID
	 */
	private Integer id;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 更新描述
	 */
	private String updateDesc;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern ="YYYY-MM-dd HH:mm:ss", timezone ="GMT+8")
	@DateTimeFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 0:未发布1:灰度发布2:全网发布
	 */
	private Integer status;

	/**
	 * 灰度uid
	 */
	private String grayscaleUid;

	/**
	 * 文件类型0:本地文件 1:外链
	 */
	private Integer fileType;

	/**
	 * 外链地址
	 */
	private String outerLink;
	
	/**
	 * 前端使用的是数组需要通过此字段实现转换
	 * */
	private String[] updateDescArray;

	public String[] getUpdateDescArray() {
		if (StringUtils.isNotEmpty(updateDesc)){
			return updateDesc.split("\\|");
		}
		return updateDescArray;
	}

	public void setId(Integer id) { 
		this.id = id;
	}
	public Integer getId() { 
		return this.id;
	}

	public void setVersion(String version) { 
		this.version = version;
	}
	public String getVersion() { 
		return this.version;
	}

	public void setUpdateDesc(String updateDesc) { 
		this.updateDesc = updateDesc;
	}
	public String getUpdateDesc() { 
		return this.updateDesc;
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

	public void setGrayscaleUid(String grayscaleUid) { 
		this.grayscaleUid = grayscaleUid;
	}
	public String getGrayscaleUid() { 
		return this.grayscaleUid;
	}

	public void setFileType(Integer fileType) { 
		this.fileType = fileType;
	}
	public Integer getFileType() { 
		return this.fileType;
	}

	public void setOuterLink(String outerLink) { 
		this.outerLink = outerLink;
	}
	public String getOuterLink() { 
		return this.outerLink;
	}


	@Override
	public String toString() {
		return "自增ID:"  + (id == null ? "空" : id ) + ", 版本号:"  + (version == null ? "空" : version ) + ", 更新描述:"  + (updateDesc == null ? "空" : updateDesc ) + ", 创建时间:"  + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()) ) + ", 0:未发布1:灰度发布2:全网发布:"  + (status == null ? "空" : status ) + ", 灰度uid:"  + (grayscaleUid == null ? "空" : grayscaleUid ) + ", 文件类型0:本地文件 1:外链:"  + (fileType == null ? "空" : fileType ) + ", 外链地址:"  + (outerLink == null ? "空" : outerLink );
	}
}

