package com.easyJava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {
    public static final String YYYY_MM_DD = "YYYY-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "YYYY-MM-dd HH:mm:ss";
    public static final String _YYYYMMDD = "YYYY/MM/dd";
    public static final String YYYYMMDD = "YYYYMMdd";

    public static String format(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parser(String date, String pattern){
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
