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

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICES_IMPL);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + "ServiceImpl";
        String interfaceName = tableInfo.getBeanName() + "Service";
        String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        File poFile = new File(folder, className + ".java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;


        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package " + Constants.PACKAGE_SERVICES_IMPL + ";");
            bw.newLine();
            bw.newLine();

            // 导入包代码生成
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICES + "." + interfaceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPERS + "." + tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;");
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            if (tableInfo.getHaveDataTime() || tableInfo.getHaveDate()) {
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;");
                bw.newLine();


                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS + ";");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS + ";");
                bw.newLine();
            }
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.write("import java.util.List;");
            bw.newLine();

            BuilderComment.createClassComment(bw, tableInfo.getComment() + " 业务接口实现");
            bw.write("@Service(\"" + StringUtils.lowerCaseFirstLetter(interfaceName) + "\")");
            bw.newLine();
            bw.write("public class " + className + " implements " + interfaceName + "{");
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + mapperName + "<" + tableInfo.getBeanName() + ", " + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + "> " + StringUtils.lowerCaseFirstLetter(mapperName) + ";");
            bw.newLine();
            bw.newLine();

            BuilderComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\tpublic List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " query) {");
            bw.newLine();
            bw.newLine();
            bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".selectList(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "根据条件查询数量");
            bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " query) {");
            bw.newLine();
            bw.newLine();
            bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".selectCount(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "分页查询");
            bw.write("\tpublic PaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY + " query) {");
            bw.newLine();
            bw.write("\t\tInteger count = this.findCountByParam(query);");
            bw.newLine();
            bw.write("\t\tSimplePage simplePage = new SimplePage(query.getPageNo(), count, query.getPageSize());");
            bw.newLine();
            bw.write("\t\tquery.setSimplePage(simplePage);");
            bw.newLine();
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);");
            bw.newLine();
            bw.write("\t\tPaginationResultVO<" + tableInfo.getBeanName() + "> result = new PaginationResultVO(count, simplePage.getPageSize(), simplePage.getPageNo(), simplePage.getPageTotal(), list);");
            bw.newLine();
            bw.write("\t\treturn result;");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "新增");
            bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".insert(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "批量新增");
            bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean==null||listBean.isEmpty()){");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".insertBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            BuilderComment.createMethodComment(bw, "批量新增/修改");
            bw.write("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean==null||listBean.isEmpty()){");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".insertOrUpdateBatch(listBean);");
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
                bw.write("\tpublic " + tableInfo.getBeanName() + " getBy" + moreConditionStr + "(" + moreParamStr + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".selectBy" + moreConditionStr + "(" + canShuStr + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "更新");
                bw.write("\tpublic Integer updateBy" + moreConditionStr + "(" + tableInfo.getBeanName() + " bean," + moreParamStr + ") {");
                bw.newLine();
                bw.newLine();
                bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".updateBy" + moreConditionStr + "(bean, " + canShuStr + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                BuilderComment.createMethodComment(bw, "根据 " + moreConditionStr + "删除");
                bw.write("\tpublic Integer deleteBy" + moreConditionStr + "(" + moreParamStr + ") {");
                bw.newLine();
                bw.newLine();
                bw.write("\t\treturn this." + StringUtils.lowerCaseFirstLetter(mapperName) + ".deleteBy" + moreConditionStr + "(" + canShuStr + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }
            bw.write("}");
            bw.newLine();

            bw.newLine();
            bw.flush();

        } catch (Exception e) {
            logger.error("创建Service失败！！", e);
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
