package com.easyJava.bean;

import com.easyJava.utils.PropertiesUtils;

public class Constants {
    public static Boolean IGNORE_TABLE_PREFIX;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;
    public static String SUFFIX_MAPPERS;
    public static String PATH_PROJECT;
    public static String PATH_BASE;
    public static String PATH_JAVA;
    public static String PATH_PO;
    public static String PATH_QUERY;
    public static String PATH_UTILS;
    public static String PATH_ENUMS;
    public static String PATH_MAPPERS;

    // PATH_MAPPERS_XML xml文件的路径
    public static String PATH_MAPPERS_XML;
    public static String PATH_RESOURCE;
    public static String PATH_SERVICES;
    public static String PATH_SERVICES_IMPL;
    public static String PATH_VO;
    public static String PATH_EXCEPTION;
    public static String PATH_CONTROLLER;



    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_ENUMS;
    public static String PACKAGE_MAPPERS;
    public static String PACKAGE_SERVICES;
    public static String PACKAGE_SERVICES_IMPL;
    public static String PACKAGE_VO;
    public static String PACKAGE_EXCEPTION;
    public static String PACKAGE_CONTROLLER;

    // 作者
    public static String AUTHOR_COMMENT;

    // 需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FILED;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
    // 日期格式序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;

    // 日期格式反序列化
    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;


    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getProperty("ignore.table.prefix"));
        SUFFIX_BEAN_QUERY = PropertiesUtils.getProperty("suffix.bean.query");
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getProperty("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getProperty("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getProperty("suffix.bean.query.time.end");
        SUFFIX_MAPPERS = PropertiesUtils.getProperty("suffix.mappers");

        PATH_PROJECT = PropertiesUtils.getProperty("path.project");
        PATH_BASE = PropertiesUtils.getProperty("path.base");
        PATH_JAVA = PATH_BASE+"java";
        PATH_RESOURCE = PATH_BASE+"resources";
        PATH_BASE = PATH_JAVA+"/"+PropertiesUtils.getProperty("package.base").replace(".","/");
        PATH_PO = PATH_BASE+"/"+PropertiesUtils.getProperty("package.po").replace(".","/");
        PATH_QUERY = PATH_BASE+"/"+PropertiesUtils.getProperty("package.query").replace(".","/");
        PATH_UTILS = PATH_BASE+"/"+PropertiesUtils.getProperty("package.utils").replace(".","/");
        PATH_ENUMS = PATH_BASE+"/"+PropertiesUtils.getProperty("package.enums").replace(".","/");
        PATH_MAPPERS = PATH_BASE+"/"+PropertiesUtils.getProperty("package.mappers").replace(".","/");
        PATH_MAPPERS_XML = PATH_RESOURCE+"/"+PropertiesUtils.getProperty("package.base").replace(".","/")+"/"+PropertiesUtils.getProperty("package.mappers").replace(".","/");
        PATH_SERVICES = PATH_BASE+"/"+PropertiesUtils.getProperty("package.services").replace(".","/");
        PATH_SERVICES_IMPL = PATH_BASE+"/"+PropertiesUtils.getProperty("package.services.impl").replace(".", "/");
        PATH_VO = PATH_BASE+"/"+PropertiesUtils.getProperty("package.vo").replace(".","/");
        PATH_EXCEPTION = PATH_BASE+"/"+PropertiesUtils.getProperty("package.exception").replace(".","/");
        PATH_CONTROLLER = PATH_BASE+"/"+PropertiesUtils.getProperty("package.controller").replace(".","/");


        PACKAGE_BASE = PropertiesUtils.getProperty("package.base");
        PACKAGE_PO = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.po");
        PACKAGE_QUERY = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.query");
        PACKAGE_UTILS = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.enums");
        PACKAGE_MAPPERS = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.mappers");
        PACKAGE_SERVICES = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.services");
        PACKAGE_SERVICES_IMPL = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.services.impl");
        PACKAGE_VO = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.vo");
        PACKAGE_EXCEPTION = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.exception");
        PACKAGE_CONTROLLER = PACKAGE_BASE+"."+PropertiesUtils.getProperty("package.controller");


        AUTHOR_COMMENT = PropertiesUtils.getProperty("author.comment");


        IGNORE_BEAN_TOJSON_FILED = PropertiesUtils.getProperty("ignore.bean.tojson.filed");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getProperty("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getProperty("ignore.bean.tojson.class");
        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getProperty("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getProperty("bean.date.format.class");
        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getProperty("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getProperty("bean.date.unformat.class");
    }

    public final static  String[] SQL_DATE_TIME_TYPES=new String[]{"datetime", "timestamp"};
    public final static  String[] SQL_DATE_TYPES=new String[]{"date"};
    public final static  String[] SQL_DECIMAL_TYPES=new String[]{"decimal", "double", "float"};
    public final static  String[] SQL_STRING_TYPES=new String[]{"char", "varchar", "text","mediumtext","longtext"};
    public final static  String[] SQL_INTEGER_TYPES=new String[]{"int", "tinyint"};
    public final static  String[] SQL_LONG_TYPES=new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println("PATH_BASE:"+PATH_BASE);
    }

}
