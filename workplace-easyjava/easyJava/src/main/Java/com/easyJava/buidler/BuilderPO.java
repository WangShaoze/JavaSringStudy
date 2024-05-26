package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.DataUtils;
import com.easyJava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class BuilderPO {
    private static final Logger logger = LoggerFactory.getLogger(BuilderPO.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName()+".java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter =null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package "+Constants.PACKAGE_PO+";");
            bw.newLine();
            bw.newLine();
            // 导入包代码生成
            bw.write("import java.io.Serializable;");
            bw.newLine();
            if (tableInfo.getHaveDataTime()|| tableInfo.getHaveDate()){
                bw.write("import "+Constants.PACKAGE_ENUMS+".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import "+Constants.PACKAGE_UTILS+".DateUtils;");
                bw.newLine();


                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS+";");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS+";");
                bw.newLine();
            }
            if (tableInfo.getHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            boolean haveIgnoreBean=false;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","), fieldInfo.getPropertyName())){
                    haveIgnoreBean = true;
                    break;
                }
            }
            if (haveIgnoreBean){
                bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS+";");
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();

            // 生成类注释
            BuilderComment.createClassComment(bw, tableInfo.getComment());
            bw.write("public class "+tableInfo.getBeanName()+" implements Serializable {");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 生成属性注释
                BuilderComment.createFieldComment(bw, fieldInfo.getComment());
                // 部分属性需要添加注解
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())){
                    bw.write("\t"+String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DataUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                    bw.write("\t"+String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DataUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())){
                    bw.write("\t"+String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DataUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t"+String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DataUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","), fieldInfo.getPropertyName())){
                    bw.write("\t"+Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.newLine();
                }
                // 生成属性
                bw.write("\tprivate "+fieldInfo.getJavaTypes()+" "+fieldInfo.getPropertyName()+";");
                bw.newLine();
                bw.newLine();
            }

            // 生成GetSet方法
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                String tempField = StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName());
                bw.write("\tpublic void set"+tempField+"("+fieldInfo.getJavaTypes()+" "+fieldInfo.getPropertyName()+") { ");
                bw.newLine();
                bw.write("\t\tthis."+fieldInfo.getPropertyName()+" = "+fieldInfo.getPropertyName()+";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                bw.write("\tpublic "+fieldInfo.getJavaTypes()+" get"+tempField+"() { ");
                bw.newLine();
                bw.write("\t\treturn this."+fieldInfo.getPropertyName()+";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }

            bw.newLine();

            // 生成toString方法
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();

            // toString 核心代码
            StringBuffer stringBuffer = new StringBuffer();
            List<FieldInfo> tmpFieldInfoList = tableInfo.getFieldList();
            for (int i = 0; i < tmpFieldInfoList.size(); i++) {
                FieldInfo fieldInfo = tmpFieldInfoList.get(i);
                String propertyName = fieldInfo.getPropertyName();
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())){
                    propertyName = "DateUtils.format("+propertyName+", DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                }else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())){
                    propertyName = "DateUtils.format("+propertyName+", DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                }
                if (i==tmpFieldInfoList.size()-1){
                    stringBuffer.append("\", "+fieldInfo.getComment()+":\"  + (").append(fieldInfo.getPropertyName()+" == null ? \"空\" : "+propertyName).append(" )");
                    break;
                }
                if (i==0){
                    stringBuffer.append("\""+fieldInfo.getComment()+":\"  + (").append(fieldInfo.getPropertyName()+" == null ? \"空\" : "+propertyName).append(" ) + ");
                    continue;
                }
                stringBuffer.append("\", "+fieldInfo.getComment()+":\"  + (").append(fieldInfo.getPropertyName()+" == null ? \"空\" : "+propertyName).append(" ) + ");
            }
            bw.write("\t\treturn "+stringBuffer.toString()+";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            bw.write("}");
            bw.newLine();
            bw.newLine();
            bw.flush();

        }catch (Exception e){
            logger.error("创建失败！！",e);
        }finally {
            if (bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStreamWriter!=null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
