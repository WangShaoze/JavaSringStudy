package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuilderRunApplication {
    private static final Logger logger = LoggerFactory.getLogger(BuilderRunApplication.class);
    public static void execute(){
        File folder = new File(Constants.PATH_BASE);
        if (!folder.exists()){
            folder.mkdirs();
        }
        File poFile = new File(folder, "RunApplication.java");
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter =null;
        BufferedWriter bw = null;

        try {
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out, "utf8");
            bw = new BufferedWriter(outputStreamWriter);

            // 代码所在位置的包代码生成
            bw.write("package "+Constants.PACKAGE_BASE+";");
            bw.newLine();
            bw.newLine();
            // 导入包代码生成
            bw.write("import "+Constants.PACKAGE_UTILS+".PropertiesUtils;");
            bw.newLine();
            bw.write("import org.mybatis.spring.annotation.MapperScan;");
            bw.newLine();
            bw.write("import org.slf4j.Logger;");
            bw.newLine();
            bw.write("import org.slf4j.LoggerFactory;");
            bw.newLine();
            bw.write("import org.springframework.boot.SpringApplication;");
            bw.newLine();
            bw.write("import org.springframework.boot.autoconfigure.SpringBootApplication;");
            bw.newLine();

            // 生成类注释
            BuilderComment.createClassComment(bw, "项目启动类");

            bw.write("@SpringBootApplication");
            bw.newLine();
            bw.write("@MapperScan(basePackages = \""+Constants.PACKAGE_MAPPERS+"\")");
            bw.newLine();
            bw.write("public class RunApplication {");
            bw.newLine();
            bw.write("\tpublic static final Logger logger = LoggerFactory.getLogger(RunApplication.class);");
            bw.newLine();
            bw.write("\tpublic static void main(String[] args) {");
            bw.newLine();
            bw.write("\t\tSpringApplication.run(RunApplication.class, args);");
            bw.newLine();
            bw.write("\t\tlogger.info(\"服务启动成功！ 地址: http://localhost:\"+ PropertiesUtils.getProperty(\"server.port\")+\"/\");");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.write("}");
            bw.newLine();
            bw.flush();

        }catch (Exception e){
            logger.error("创建 RunApplication.java 失败！！",e);
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
