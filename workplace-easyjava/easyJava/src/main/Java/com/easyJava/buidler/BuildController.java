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
import java.util.Map;

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + "Controller";
        String interfaceName = tableInfo.getBeanName() + "Service";
        String queryName = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;


        File poFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;


        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();

            // 导入包代码生成
            bw.write("import "+Constants.PACKAGE_QUERY+"."+queryName+";");
            bw.newLine();
            bw.write("import "+Constants.PACKAGE_VO+".ResponseVO;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICES + "." + interfaceName + ";");
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;");
            bw.newLine();

            bw.write("import java.util.List;");
            bw.newLine();

            BuilderComment.createClassComment(bw, tableInfo.getComment() + " Controller");
            bw.write("@RestController");
            bw.newLine();
            bw.write("@RequestMapping(\"/" + StringUtils.lowerCaseFirstLetter(interfaceName) + "\")");
            bw.newLine();
            bw.write("public class " + className + " extends ABaseController {");
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate "+interfaceName+" " + StringUtils.lowerCaseFirstLetter(interfaceName) + ";");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "加载数据");
            bw.write("\t@RequestMapping(\"/loadDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO loadDataList(" + tableInfo.getBeanName()+Constants.SUFFIX_BEAN_QUERY + " query) {");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(this."+StringUtils.lowerCaseFirstLetter(interfaceName)+".findListByPage(query));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "新增");
            bw.write("\t@RequestMapping(\"/add\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO add(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\tthis." + StringUtils.lowerCaseFirstLetter(interfaceName) + ".add(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "批量新增");
            bw.write("\t@RequestMapping(\"/addBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tthis." + StringUtils.lowerCaseFirstLetter(interfaceName) + ".addBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "批量新增/修改");
            bw.write("\t@RequestMapping(\"/addOrUpdateBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tthis." + StringUtils.lowerCaseFirstLetter(interfaceName) + ".addOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();
                StringBuilder moreConditionStr = new StringBuilder();
                StringBuilder moreParamStr = new StringBuilder();
                StringBuilder canShuStr = new StringBuilder();
                for (int i = 0; i < fieldInfoList.size(); i++) {
                    FieldInfo fieldInfo = fieldInfoList.get(i);
                    moreConditionStr.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    moreParamStr.append(fieldInfo.getJavaTypes()).append(" ").append(fieldInfo.getPropertyName());
                    canShuStr.append(fieldInfo.getPropertyName());
                    if (i < fieldInfoList.size() - 1) {
                        moreConditionStr.append("And");
                        moreParamStr.append(", ");
                        canShuStr.append(", ");
                    }
                }
                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "查询");
                bw.write("\t@RequestMapping(\"/getBy"+moreConditionStr+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO<" + tableInfo.getBeanName() + "> getBy" + moreConditionStr + "(" + moreParamStr + ") {");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(this." + StringUtils.lowerCaseFirstLetter(interfaceName) + ".getBy" + moreConditionStr + "(" + canShuStr + "));");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "更新");
                bw.write("\t@RequestMapping(\"/updateBy"+moreConditionStr+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO updateBy" + moreConditionStr + "(" + tableInfo.getBeanName() + " bean," + moreParamStr + ") {");
                bw.newLine();
                bw.write("\t\tthis."+StringUtils.lowerCaseFirstLetter(interfaceName)+".updateBy"+moreConditionStr+"(bean, "+canShuStr+");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "删除");
                bw.write("\t@RequestMapping(\"/deleteBy"+moreConditionStr+"\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO deleteBy" + moreConditionStr + "(" + moreParamStr + ") {");
                bw.newLine();
                bw.write("\t\tthis."+StringUtils.lowerCaseFirstLetter(interfaceName)+".deleteBy"+moreConditionStr+"("+canShuStr+");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }
            bw.write("}");
            bw.newLine();

            bw.newLine();
            bw.flush();

        } catch (Exception e) {
            logger.error("创建 Controller 失败！！", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
