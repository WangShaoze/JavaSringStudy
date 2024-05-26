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
import java.util.Map;

public class BuilderMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuilderMapper.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_MAPPERS);
        if (!folder.exists()){
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName()+Constants.SUFFIX_MAPPERS;
        File poFile = new File(folder, className+".java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter =null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package "+Constants.PACKAGE_MAPPERS+";");
            bw.newLine();
            bw.newLine();
            // 导入包代码生成
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();
            bw.newLine();

            // 生成类注释
            BuilderComment.createClassComment(bw, tableInfo.getComment()+"Mapper");
            bw.write("public interface "+className+"<T, P> extends BaseMapper{");
            // 便利索引生成方法
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                int index =0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParam = new StringBuilder();
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfoList.size()){
                        methodName.append("And");
                    }

                    methodParam.append("@Param(\"").append(fieldInfo.getPropertyName()).append("\") ").append(fieldInfo.getJavaTypes()).append(" ").append(fieldInfo.getPropertyName()); // @Param("code") String code
                    if (index < keyFieldInfoList.size()){
                        methodParam.append(", ");
                    }
                }
                bw.newLine();
                BuilderComment.createFieldComment(bw, "根据 "+methodName+" 查询");
                bw.write("\tT selectBy"+methodName+"("+methodParam+");");
                bw.newLine();
                bw.newLine();

                BuilderComment.createFieldComment(bw, "根据 "+methodName+" 更新");
                bw.write("\tInteger updateBy"+methodName+"(@Param(\"bean\") T t, "+methodParam+");");
                bw.newLine();
                bw.newLine();

                BuilderComment.createFieldComment(bw, "根据 "+methodName+" 删除");
                bw.write("\tInteger deleteBy"+methodName+"("+methodParam+");");
                bw.newLine();
                bw.newLine();
            }

            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.newLine();
            bw.flush();

        }catch (Exception e){
            logger.error("{} mapper 创建失败！！",className, e);
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
