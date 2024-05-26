package com.easyJava.buidler;

import com.easyJava.bean.Constants;
import com.easyJava.bean.TableInfo;
import com.easyJava.utils.DataUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

public class BuilderComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws IOException {
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description: "+classComment);
        bw.newLine();
        bw.write(" * @author: "+ Constants.AUTHOR_COMMENT);
        bw.newLine();
        bw.write(" * @date: "+ DataUtils.format(new Date(), DataUtils._YYYYMMDD));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }

    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * "+(fieldComment==null?"":fieldComment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }

    public static void createMethodComment(BufferedWriter bw, String methodComment) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * "+methodComment);
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }
}
