package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.JsonUtils;
import com.easyJava.utils.PropertiesUtils;
import com.easyJava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderTable {
    private static final Logger logger = LoggerFactory.getLogger(BuilderTable.class);
    private static Connection connection = null;
    private static final String SQL_SHOW_TABLE_STATUS = "SHOW TABLE STATUS";
    private static final String SQL_SHOW_FULL_FIELD_INFO = "SHOW FULL FIELDS FROM %s";
    private static final String SQL_SHOW_KEY_INDEX_INFO = "SHOW INDEX FROM %s";

    static {
        String drierName = PropertiesUtils.getProperty("spring.datasource.driver-class-name");
        String url = PropertiesUtils.getProperty("spring.datasource.url");
        String user = PropertiesUtils.getProperty("spring.datasource.username");
        String password = PropertiesUtils.getProperty("spring.datasource.password");

        try {
            Class.forName(drierName);
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e){
            logger.error("数据库连接失败！！", e);
        }
    }

    public static List<TableInfo> getTables(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
        try {
            preparedStatement = connection.prepareStatement(SQL_SHOW_TABLE_STATUS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String tableName = resultSet.getString("name");
                String tableComment = resultSet.getString("comment");
//                logger.info("tableName:{}, tableComment:{}", tableName, tableComment);

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX){
                    beanName = tableName.substring(beanName.indexOf("_")+1);
                }
                beanName = processField(beanName, true);
//                logger.info(beanName);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setComment(tableComment);
                tableInfo.setBeanName(beanName);
                tableInfo.setBeanParamName(beanName+Constants.SUFFIX_BEAN_QUERY);
//                logger.info("表名:{}， 表备注:{}, JavaBean:{}, 参数Bean:{}", tableInfo.getBeanName(), tableInfo.getComment(), tableInfo.getBeanName(), tableInfo.getBeanParamName());

                // 读取字段数据
                readFieldInfo(tableInfo);

                // 读取索引数据
                getKeyIndexInfo(tableInfo);

//                logger.info("表:{}", JsonUtils.convertObj2Json(tableInfo));
                tableInfoList.add(tableInfo);
            }
        }catch (Exception e){
            logger.error("数据读取失败",e);
        }finally {
            if (resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableInfoList;
    }


    private static void readFieldInfo(TableInfo tableInfo){
        PreparedStatement preparedStatement = null;
        ResultSet fieldInfoSet = null;

        List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();
        List<FieldInfo> fieldExtendInfoList = new ArrayList<FieldInfo>();
        try {
            preparedStatement = connection.prepareStatement(String.format(SQL_SHOW_FULL_FIELD_INFO, tableInfo.getTableName()));
            fieldInfoSet = preparedStatement.executeQuery();
            while (fieldInfoSet.next()){
                String field = fieldInfoSet.getString("field");
                String type = fieldInfoSet.getString("type");
                String extra = fieldInfoSet.getString("extra");
                String comment = fieldInfoSet.getString("comment");

                // 对数据进行处理
                if (type.indexOf("(")>0){
                    type = type.substring(0, type.indexOf("("));
                }

                String propertyName = processField(field, false);

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(field);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setJavaTypes(processJavaType(type));
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));

//                logger.info("field:{} ,propertyName:{}, type:{} , javaType:{}, extra:{} ,comment:{} ", field,propertyName, type,fieldInfo.getJavaTypes(), fieldInfo.getAutoIncrement(),comment);
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)){
                    tableInfo.setHaveBigDecimal(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
                    tableInfo.setHaveDate(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)){
                    tableInfo.setHaveDataTime(true);
                }
                fieldInfoList.add(fieldInfo);
                // String类型的参数 -> 实现模糊查询 company_name——> companyNameFuzzy
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())){
                    FieldInfo fuzzyFieldInfo = new FieldInfo();
                    fuzzyFieldInfo.setPropertyName(propertyName+Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fuzzyFieldInfo.setFieldName(fieldInfo.getFieldName());
                    fuzzyFieldInfo.setJavaTypes(fieldInfo.getJavaTypes());
                    fuzzyFieldInfo.setSqlType(fieldInfo.getSqlType());
                    fieldExtendInfoList.add(fuzzyFieldInfo);
                }
                // 日期类型的需要Start End参数
                if ((ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()))||(ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()))){
                    FieldInfo timeStartFieldInfo = new FieldInfo();
                    timeStartFieldInfo.setPropertyName(propertyName+Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    timeStartFieldInfo.setFieldName(fieldInfo.getFieldName());
                    timeStartFieldInfo.setJavaTypes("String");
                    timeStartFieldInfo.setSqlType(fieldInfo.getSqlType());
                    fieldExtendInfoList.add(timeStartFieldInfo);

                    FieldInfo timeEndFieldInfo = new FieldInfo();
                    timeEndFieldInfo.setPropertyName(propertyName+Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    timeEndFieldInfo.setFieldName(fieldInfo.getFieldName());
                    timeEndFieldInfo.setJavaTypes("String");
                    timeEndFieldInfo.setSqlType(fieldInfo.getSqlType());
                    fieldExtendInfoList.add(timeEndFieldInfo);
                }
            }
            tableInfo.setFieldList(fieldInfoList);
            tableInfo.setFileExtentList(fieldExtendInfoList);
        }catch (Exception e){
            logger.error("字段数据读取失败",e);
        }finally {
            if (fieldInfoSet!=null){
                try {
                    fieldInfoSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void getKeyIndexInfo(TableInfo tableInfo){
        PreparedStatement preparedStatement = null;
        ResultSet fieldInfoSet = null;
        try {
            Map<String, FieldInfo> tempMap = new HashMap<String, FieldInfo>();
            for (FieldInfo fieldInfo:tableInfo.getFieldList()){
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }

            preparedStatement = connection.prepareStatement(String.format(SQL_SHOW_KEY_INDEX_INFO, tableInfo.getTableName()));
            fieldInfoSet = preparedStatement.executeQuery();
            while (fieldInfoSet.next()) {
                String keyName = fieldInfoSet.getString("key_name");
                int nonUnique = fieldInfoSet.getInt("non_unique");
                String columnName = fieldInfoSet.getString("column_name");
                if (nonUnique==1){
                    continue;
                }
                List<FieldInfo> fieldInfoList1 = tableInfo.getKeyIndexMap().get(keyName);
                if (fieldInfoList1==null){
                    fieldInfoList1 = new ArrayList<FieldInfo>();
                    tableInfo.getKeyIndexMap().put(keyName, fieldInfoList1);
                }
                fieldInfoList1.add(tempMap.get(columnName));
            }
        }catch (Exception e){
            logger.error("索引数据读取失败",e);
        }finally {
            if (fieldInfoSet!=null){
                try {
                    fieldInfoSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static String processField(String field, Boolean upperCaseFirstLetter){
        /**
         * 驼峰转换（首词是否驼峰 upperCaseFirstLetter:true--> 驼峰）
         * */
        StringBuilder stringBuffer = new StringBuilder();
        String[] fields = field.split("_");
        stringBuffer.append(upperCaseFirstLetter? StringUtils.upperCaseFirstLetter(fields[0]) :fields[0]);
        for (int i = 1; i < fields.length; i++) {
            stringBuffer.append(StringUtils.upperCaseFirstLetter(fields[i]));
        }
        return stringBuffer.toString();

    }

    private static String processJavaType(String type){
        if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)){
            return "Date";
        }
        else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
            return "Date";
        }
        else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)){
            return "BigDecimal";
        }
        else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, type)){
            return "String";
        }
        else if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPES, type)){
            return "Integer";
        }
        else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES, type)){
            return "Long";
        }
        else  {
            throw new RuntimeException("无法识别类型:"+type);
        }
    }


}
