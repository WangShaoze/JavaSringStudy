package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import com.easyJava.bean.FieldInfo;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuilderService {
    private static final Logger logger = LoggerFactory.getLogger(BuilderService.class);
    public static void execute(TableInfo tableInfo){
        File folder = new File(Constants.PATH_SERVICES);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName()+"Service.java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter =null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package "+Constants.PACKAGE_SERVICES+";");
            bw.newLine();
            bw.newLine();

            // 导入包代码生成
            bw.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanName()+"Query;");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".PaginationResultVO;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            BuilderComment.createClassComment(bw, tableInfo.getComment()+" 业务接口");
            bw.write("public interface "+tableInfo.getBeanName()+"Service {");
            bw.newLine();

            bw.write("");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\tList<"+tableInfo.getBeanName()+"> findListByParam("+tableInfo.getBeanName()+"Query query);");
            bw.newLine();
            bw.newLine();

            BuilderComment.createMethodComment(bw, "根据条件查询数量");
            bw.write("\tInteger findCountByParam("+tableInfo.getBeanName()+"Query query);");
            bw.newLine();
            bw.newLine();

            BuilderComment.createMethodComment(bw, "分页查询");
            bw.write("\tPaginationResultVO<"+tableInfo.getBeanName()+"> findListByPage("+tableInfo.getBeanName()+"Query query);");
            bw.newLine();
            bw.newLine();

            BuilderComment.createMethodComment(bw, "新增");
            bw.write("\tInteger add("+tableInfo.getBeanName()+" bean);");
            bw.newLine();
            bw.newLine();

            BuilderComment.createMethodComment(bw, "批量新增");
            bw.write("\tInteger addBatch(List<"+tableInfo.getBeanName()+"> listBean);");
            bw.newLine();
            bw.newLine();

            BuilderComment.createMethodComment(bw, "批量新增/修改");
            bw.write("\tInteger addOrUpdateBatch(List<"+tableInfo.getBeanName()+"> listBean);");
            bw.newLine();
            bw.newLine();

            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                StringBuilder moreConditionStr = new StringBuilder();
                StringBuilder moreParamStr = new StringBuilder();
                for (int i = 0; i < fieldInfoList.size(); i++) {
                    FieldInfo fieldInfo = fieldInfoList.get(i);
                    moreConditionStr.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    moreParamStr.append(fieldInfo.getJavaTypes()+" "+fieldInfo.getPropertyName());
                    if (i < fieldInfoList.size() - 1) {
                        moreConditionStr.append("And");
                        moreParamStr.append(", ");
                    }
                }
                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "查询");
                bw.write("\t" + tableInfo.getBeanName() + " getBy" + moreConditionStr + "(" +moreParamStr+");");
                bw.newLine();
                bw.newLine();

                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "更新");
                bw.write("\tInteger updateBy" + moreConditionStr + "("+tableInfo.getBeanName()+" bean, " +moreParamStr+");");
                bw.newLine();
                bw.newLine();

                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "删除");
                bw.write("\tInteger deleteBy" + moreConditionStr + "(" +moreParamStr+");");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.newLine();

            bw.newLine();
            bw.flush();

        }catch (Exception e){
            logger.error("创建Service失败！！",e);
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
