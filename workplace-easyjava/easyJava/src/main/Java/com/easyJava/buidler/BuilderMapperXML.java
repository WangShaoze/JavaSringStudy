package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderMapperXML {
    private static final Logger logger = LoggerFactory.getLogger(BuilderMapper.class);
    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITION = "query_condition";

    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_MAPPERS_XML);
        if (!folder.exists()){
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName()+Constants.SUFFIX_MAPPERS;
        File poFile = new File(folder, className+".xml");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter =null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outputStreamWriter);
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write("<mapper namespace=\""+Constants.PACKAGE_MAPPERS+"."+className+"\">");
            bw.newLine();

            // 实体映射
            bw.write("\t<!--实体映射-->");
            bw.newLine();
            String poClass = Constants.PACKAGE_PO+"."+tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\""+poClass+"\">");
            bw.newLine();

            FieldInfo idFileInfo=null;
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())){
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if (fieldInfoList.size() == 1){
                        idFileInfo = fieldInfoList.get(0);
                        break;
                    }
                }
            }
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t<!--"+fieldInfo.getComment()+"-->");
                bw.newLine();
                String key;
                if (idFileInfo!=null&&fieldInfo.getPropertyName().equals(idFileInfo.getPropertyName())){
                    key = "id";
                }else {
                    key="result";
                }
                bw.write("\t\t<"+key+" column=\""+fieldInfo.getFieldName()+"\" property=\""+fieldInfo.getPropertyName()+"\"  />");
                bw.newLine();
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();
            bw.newLine();

            // 通用查询列
            StringBuilder columStr = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                columStr.append(fieldInfo.getFieldName()).append(",");
            }
            bw.write("\t<!--通用查询列-->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_COLUMN_LIST+"\">");
            bw.newLine();
            bw.write("\t\t"+columStr.substring(0, columStr.lastIndexOf(",")));
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            // 基础查询条件
            bw.write("\t<!--基础查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_QUERY_CONDITION+"\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldList()){
                StringBuilder strTypeAdd = new StringBuilder();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())){
                    strTypeAdd.append(" and query.").append(fieldInfo.getPropertyName()).append(" != ''");
                }
                bw.write("\t\t<if test=\"query."+fieldInfo.getPropertyName()+" != null"+strTypeAdd+"\">");
                bw.newLine();
                bw.write("\t\t\tand id= #{query."+fieldInfo.getPropertyName()+"}");
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            // 构建扩展查询
            bw.write("\t<!--扩展字段查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_QUERY_CONDITION_EXTEND+"\">");
            bw.newLine();
            for (FieldInfo fieldExtendInfo: tableInfo.getFileExtentList()){
                StringBuilder andWhereStr = new StringBuilder();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldExtendInfo.getSqlType())){
                    andWhereStr.append("\t\t\tand ").append(fieldExtendInfo.getFieldName()).append(" like concat('%', #{query.").append(fieldExtendInfo.getPropertyName()).append("}, '%')");
                }else if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldExtendInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldExtendInfo.getSqlType())){
                    if (fieldExtendInfo.getPropertyName().endsWith("Start")){
                        andWhereStr.append("\t\t\t<![CDATA[ and ").append(fieldExtendInfo.getFieldName()).append(" >= str_to_date(#{query.").append(fieldExtendInfo.getPropertyName()).append("}, '%Y-%m-%d') ]]>");
                    } else if (fieldExtendInfo.getPropertyName().endsWith("End")) {
                        andWhereStr.append("\t\t\t<![CDATA[ and ").append(fieldExtendInfo.getFieldName()).append(" < date_sub(str_to_date(#{query.").append(fieldExtendInfo.getPropertyName()).append("}, '%Y-%m-%d'), interval -1 day) ]]>");
                    }
                }
                bw.write("\t\t<if test=\"query."+ fieldExtendInfo.getPropertyName()+" != null and query."+fieldExtendInfo.getPropertyName()+" != ''\">");
                bw.newLine();
                bw.write(andWhereStr.toString());
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();

            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            // 通用查询条件 普通列+扩展列
            bw.write("\t<!--通用查询条件 普通列+扩展字段-->");
            bw.newLine();
            bw.write("\t<sql id=\""+QUERY_CONDITION+"\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+BASE_QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+BASE_QUERY_CONDITION_EXTEND+"\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            // 定义查询列表
            bw.write("\t<!--查询列表-->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tselect <include refid=\""+BASE_COLUMN_LIST+"\"/> from "+tableInfo.getTableName()+" <include refid=\""+QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy != null \">order by ${query.orderBy}</if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage != null \">limit #{query.simplePage.start}, #{query.simplePage.end}</if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            // 定义查询数量
            bw.write("\t<!--查询数量-->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tselect count(1) from "+tableInfo.getTableName()+" <include refid=\""+QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            // 单条 <!--插入 （匹配有值的字段）-->
            bw.write("\t<!--插入 （匹配有值的字段）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\""+poClass+"\">");
            bw.newLine();

            FieldInfo autoIncrementField = null;
            for (FieldInfo fieldInfo: tableInfo.getFieldList()){
                if (fieldInfo.getAutoIncrement()!=null&& fieldInfo.getAutoIncrement()){
                    autoIncrementField = fieldInfo;
                    break;
                }
            }
            if (autoIncrementField!=null){
                bw.write("\t\t<selectKey keyProperty=\"bean."+autoIncrementField.getPropertyName()+"\" resultType=\""+autoIncrementField.getJavaTypes()+"\" order=\"AFTER\">");
                bw.newLine();
                bw.write("\t\t\tselect last_insert_id()");
                bw.newLine();
                bw.write("\t\t</selectKey>");
                bw.newLine();
            }

            bw.write("\t\tinsert into "+tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\" >");
                bw.newLine();
                bw.write("\t\t\t\t"+fieldInfo.getFieldName()+",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.newLine();

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean."+fieldInfo.getPropertyName()+"},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            // <!--插入或更新 （匹配有值的字段）-->
            bw.write("\t<!--插入或更新 （匹配有值的字段）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\""+poClass+"\">");
            bw.newLine();
            bw.write("\t\tinsert into " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\" >");
                bw.newLine();
                bw.write("\t\t\t\t"+fieldInfo.getFieldName()+",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.newLine();

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldList()){
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean."+fieldInfo.getPropertyName()+"},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\ton duplicate key update");
            bw.newLine();

            Map<String, FieldInfo> keyIndexTmp = new HashMap<String, FieldInfo>();

            for (Map.Entry<String, List<FieldInfo>> entry:tableInfo.getKeyIndexMap().entrySet()){
                List<FieldInfo> fieldInfoList = entry.getValue();
                for (FieldInfo fieldInfo : fieldInfoList) {
                    keyIndexTmp.put(fieldInfo.getFieldName(), fieldInfo);
                }
            }

            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo:tableInfo.getFieldList()){
                if (keyIndexTmp.get(fieldInfo.getFieldName())!=null){
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                bw.newLine();
                bw.write("\t\t\t\t"+fieldInfo.getFieldName()+" = values("+fieldInfo.getFieldName()+"),");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            // 添加 （批量插入）
            bw.write("\t<!--添加 （批量插入）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertBatch\" parameterType=\""+poClass+"\">");
            bw.newLine();
            StringBuilder fieldStringTmp = new StringBuilder();
            StringBuilder propertyStringTmp = new StringBuilder();
            for (FieldInfo fieldInfo:tableInfo.getFieldList()){
                if (fieldInfo.getAutoIncrement()){
                    continue;
                }
                fieldStringTmp.append(fieldInfo.getFieldName()).append(",");
                propertyStringTmp.append("#{item.").append(fieldInfo.getPropertyName()).append("},");
            }
            bw.write("\t\tinsert into "+tableInfo.getTableName()+"("+fieldStringTmp.substring(0, fieldStringTmp.lastIndexOf(","))+")  values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            bw.write("\t\t\t("+propertyStringTmp.substring(0, propertyStringTmp.lastIndexOf(","))+") ");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            // 添加 （批量插入或更新）
            bw.write("\t<!--添加 （批量插入或更新）-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\""+poClass+"\">");
            bw.newLine();
            bw.write("\t\tinsert into "+tableInfo.getTableName()+"("+fieldStringTmp.substring(0, fieldStringTmp.lastIndexOf(","))+") values ");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            bw.write("\t\t\t("+propertyStringTmp.substring(0, propertyStringTmp.lastIndexOf(","))+")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t\ton duplicate key update");
            bw.newLine();
            for (int i = 0; i < tableInfo.getFieldList().size(); i++) {
                FieldInfo fieldInfo = tableInfo.getFieldList().get(i);
                if (fieldInfo.getAutoIncrement()){
                    continue;
                }
                String key = "";
                if (i!=tableInfo.getFieldList().size()-1){
                    key = ",";
                }
                bw.write("\t\t"+fieldInfo.getFieldName()+" = values("+fieldInfo.getFieldName()+")"+key);
                bw.newLine();
            }
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();


            // 根据主键查询，更新，删除
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();

                StringBuilder methodName = new StringBuilder();
                StringBuilder paramsWhere = new StringBuilder();
                for (int i = 0; i < keyFieldInfoList.size(); i++) {
                    methodName.append(StringUtils.upperCaseFirstLetter(keyFieldInfoList.get(i).getPropertyName()));
                    paramsWhere.append(keyFieldInfoList.get(i).getFieldName()).append("=").append("#{").append(keyFieldInfoList.get(i).getPropertyName()).append("} ");
                    if (i < keyFieldInfoList.size()-1){
                        methodName.append("And");
                        paramsWhere.append("and ");
                    }
                }
                bw.newLine();
                bw.write("\t<!--根据 "+methodName+" 查询-->");
                bw.newLine();
                bw.write("\t<select id=\"selectBy"+methodName+"\" resultMap=\"base_result_map\">");
                bw.newLine();
                bw.write("\t\tselect <include refid=\""+BASE_COLUMN_LIST+"\"/> from "+tableInfo.getTableName()+" where "+paramsWhere);
                bw.newLine();
                bw.write("\t</select>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!--根据 "+methodName+" 更新-->");
                bw.newLine();
                bw.write("\t<update id=\"updateBy"+methodName+"\" parameterType=\""+poClass+"\">");
                bw.newLine();
                bw.write("\t\tupdate "+tableInfo.getTableName());
                bw.newLine();
                bw.write("\t\t<set>");
                bw.newLine();

                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                    bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                    bw.newLine();
                    bw.write("\t\t\t\t"+fieldInfo.getFieldName()+" = #{bean."+fieldInfo.getPropertyName()+"},");
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();
                }

                bw.write("\t\t</set>");
                bw.newLine();
                bw.write("\t\twhere "+paramsWhere);
                bw.newLine();
                bw.write("\t</update>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!--根据 "+methodName+" 删除-->");
                bw.newLine();
                bw.write("\t<delete id=\"deleteBy"+methodName+"\">");
                bw.newLine();
                bw.write("\t\tdelete from "+tableInfo.getTableName()+" where "+paramsWhere);
                bw.newLine();
                bw.write("\t</delete>");
                bw.newLine();
                bw.newLine();
            }
            bw.write("</mapper>");
            bw.newLine();
            bw.flush();
        }catch (Exception e){
            logger.error("{} mapper xml 创建失败！！",className, e);
        }finally {
            if (bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStreamWriter!=null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
