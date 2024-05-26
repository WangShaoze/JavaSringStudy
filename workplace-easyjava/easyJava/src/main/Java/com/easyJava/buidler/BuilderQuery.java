package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuilderQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuilderQuery.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()){
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName()+Constants.SUFFIX_BEAN_QUERY;
        File poFile = new File(folder, className+".java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter =null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package "+Constants.PACKAGE_QUERY+";");
            bw.newLine();
            bw.newLine();
            // 导入包代码生成
            if (tableInfo.getHaveDataTime()|| tableInfo.getHaveDate()){
                bw.write("import java.util.Date;");
                bw.newLine();
            }
            if (tableInfo.getHaveBigDecimal()){
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();

            // 生成类注释
            BuilderComment.createClassComment(bw, tableInfo.getComment()+"查询对象");
            bw.write("public class "+className+" extends BaseQuery {");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 生成属性注释
                BuilderComment.createFieldComment(bw, fieldInfo.getComment());
                // 生成属性
                bw.write("\tprivate "+fieldInfo.getJavaTypes()+" "+fieldInfo.getPropertyName()+";");
                bw.newLine();
                bw.newLine();

                // 生成String类型的参数-> 实现模糊查询
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())){
                    String propertyName = fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_FUZZY;
                    bw.write("\tprivate "+fieldInfo.getJavaTypes()+" "+propertyName+";");
                    bw.newLine();
                    bw.newLine();
                }

                // 日期类型的需要Start End参数
                if ((ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()))||(ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()))){
                    bw.write("\tprivate String "+fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_TIME_START+";");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tprivate String "+fieldInfo.getPropertyName()+Constants.SUFFIX_BEAN_QUERY_TIME_END+";");
                    bw.newLine();
                    bw.newLine();
                }
            }

            // 生成GetSet方法
            buildGetSetMethods(bw, tableInfo.getFieldList());
            buildGetSetMethods(bw, tableInfo.getFileExtentList());

            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.newLine();
            bw.flush();

        }catch (Exception e){
            logger.error("Query 创建失败！！",e);
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

    private static void buildGetSetMethods(BufferedWriter bw, List<FieldInfo> tempFileInfoList) throws IOException {
        for (FieldInfo fieldInfo : tempFileInfoList) {
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
    }
}
