package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuilderBase {
    private static final Logger logger = LoggerFactory.getLogger(BuilderBase.class);

    public static void execute(){
        List<String> headInfoList = new ArrayList<String>();
        // 生成date枚举
        headInfoList.add("package "+Constants.PACKAGE_ENUMS+";");
        build(headInfoList, "DateTimePatternEnum", Constants.PATH_ENUMS);

        // DateUtils
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_UTILS+";");
        build(headInfoList, "DateUtils", Constants.PATH_UTILS);

        // 生成BaseMapper
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_MAPPERS+";");
        build(headInfoList, "BaseMapper", Constants.PATH_MAPPERS);

        // 生成 BaseQuery
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_QUERY+";");
        build(headInfoList, "BaseQuery", Constants.PATH_QUERY);

        // 生成 PageSize 枚举
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_ENUMS+";");
        build(headInfoList, "PageSize", Constants.PATH_ENUMS);

        // 生成 SimplePage
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_QUERY+";");
        headInfoList.add("import "+Constants.PACKAGE_ENUMS+".PageSize;");
        build(headInfoList, "SimplePage", Constants.PATH_QUERY);

        // 生成 PaginationResultVO
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_VO+";");
        build(headInfoList, "PaginationResultVO", Constants.PATH_VO);

        // 生成 BusinessException
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_EXCEPTION+";");
        headInfoList.add("import "+Constants.PACKAGE_ENUMS+".ResponseCodeEnum;");
        build(headInfoList, "BusinessException", Constants.PATH_EXCEPTION);

        // 生成 ResponseCodeEnum
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_ENUMS+";");
        build(headInfoList, "ResponseCodeEnum", Constants.PATH_ENUMS);

        // 生成 ABaseController
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_CONTROLLER+";");
        headInfoList.add("import "+Constants.PACKAGE_ENUMS+".ResponseCodeEnum;");
        headInfoList.add("import "+Constants.PACKAGE_VO+".ResponseVO;");
        build(headInfoList, "ABaseController", Constants.PATH_CONTROLLER);

        // 生成 ResponseVO
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_VO+";");
        build(headInfoList, "ResponseVO", Constants.PATH_VO);

        // 生成 AGlobalExceptionHandlerController
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_CONTROLLER+";");
        headInfoList.add("import "+Constants.PACKAGE_VO+".ResponseVO;");
        headInfoList.add("import "+Constants.PACKAGE_ENUMS+".ResponseCodeEnum;");
        headInfoList.add("import "+Constants.PACKAGE_EXCEPTION+".BusinessException;");
        build(headInfoList, "AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);

        // 生成 PropertiesUtils
        headInfoList.clear();
        headInfoList.add("package "+Constants.PACKAGE_UTILS+";");
        build(headInfoList, "PropertiesUtils", Constants.PATH_UTILS);

        // 生成 application.properties
        genFile("application.properties", Constants.PATH_RESOURCE);

        // 生成 logback.xml
        genFile("logback.xml", Constants.PATH_RESOURCE);

        // 生成 pom.xml
        genFile("pom.xml", Constants.PATH_PROJECT);
    }

    public static void build(List<String> headInfoList, String fileName, String outputPath){
        File folder = new File(outputPath);
        if (!folder.exists()){
            folder.mkdirs();
        }

        File javaFile = new File(outputPath,fileName+".java");
        OutputStream out = null;
        OutputStreamWriter otw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            out = new FileOutputStream(javaFile);
            otw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(otw);

            String templatePath = BuilderBase.class.getClassLoader().getResource("template/" +fileName+".txt").getPath();
            in = new FileInputStream(templatePath);
            isr = new InputStreamReader(in, "utf-8");
            br = new BufferedReader(isr);

            for (String headStr:headInfoList) {
                if (headStr.contains("package")){
                    bw.write(headStr);
                    bw.newLine();
                }
            }
            bw.newLine();
            for (String headStr:headInfoList) {
                if (headStr.contains("import")){
                    bw.write(headStr);
                    bw.newLine();
                }
            }

            String lineInfo = null;
            while ((lineInfo=br.readLine())!=null){
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();

        }catch (Exception e){
            logger.error("生成基础类：{} ， 失败！！", fileName, e);
        }finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr!=null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (otw!=null){
                try {
                    otw.close();
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


    public static void genFile(String fileName, String outputPath){
        File folder = new File(outputPath);
        if (!folder.exists()){
            folder.mkdirs();
        }

        File javaFile = new File(outputPath,fileName);
        OutputStream out = null;
        OutputStreamWriter otw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            out = new FileOutputStream(javaFile);
            otw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(otw);

            String templatePath = BuilderBase.class.getClassLoader().getResource("template/" +fileName+".txt").getPath();
            in = new FileInputStream(templatePath);
            isr = new InputStreamReader(in, "utf-8");
            br = new BufferedReader(isr);

            String lineInfo = null;
            while ((lineInfo=br.readLine())!=null){
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();

        }catch (Exception e){
            logger.error("生成文件：{} ， 失败！！", fileName, e);
        }finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr!=null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (otw!=null){
                try {
                    otw.close();
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
