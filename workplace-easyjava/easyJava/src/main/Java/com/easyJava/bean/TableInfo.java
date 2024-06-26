package com.easyJava.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {
    /**
    * 表名称
    * */
    private String tableName;

    /**
     * bean名称
     * */
    private String beanName;

    /**
     * 参数名称
     * */
    private String beanParamName;

    /**
     * 表注释
     * */
    private String comment;

    /**
     * 字段信息
     * */
    private List<FieldInfo> fieldList;

    /**
     * 扩展字段信息
     */
    private List<FieldInfo> fileExtentList;

    /**
     * 唯一索引集合
     * */
    private Map<String, List<FieldInfo>> keyIndexMap =new LinkedHashMap<String, List<FieldInfo>>();

    /**
     * 是否有日期类型
     * */
    private Boolean haveDate=false;

    /**
     * 是否有时间类型
     * */
    private Boolean haveDataTime=false;

    /**
     * 是否有bigDecimal类型
     * */
    private Boolean haveBigDecimal=false;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldInfo> fieldList) {
        this.fieldList = fieldList;
    }
    public List<FieldInfo> getFileExtentList() {
        return fileExtentList;
    }

    public void setFileExtentList(List<FieldInfo> fileExtentList) {
        this.fileExtentList = fileExtentList;
    }

    public Map<String, List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }

    public void setKeyIndexMap(Map<String, List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }

    public Boolean getHaveDate() {
        return haveDate;
    }

    public void setHaveDate(Boolean haveDate) {
        this.haveDate = haveDate;
    }

    public Boolean getHaveDataTime() {
        return haveDataTime;
    }

    public void setHaveDataTime(Boolean haveDataTime) {
        this.haveDataTime = haveDataTime;
    }

    public Boolean getHaveBigDecimal() {
        return haveBigDecimal;
    }

    public void setHaveBigDecimal(Boolean haveBigDecimal) {
        this.haveBigDecimal = haveBigDecimal;
    }
}
