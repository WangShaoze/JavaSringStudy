package com.easyChat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    private static final Object lockobj = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    private static SimpleDateFormat getsdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null) {
            synchronized (lockobj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 使用ThreadLocal来为每个线程创建一个SimpleDateFormat的实例
                    tl = new ThreadLocal<SimpleDateFormat>(){
                        @Override
                        protected SimpleDateFormat initialValue(){
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    public static String format(Date date, String pattern) {
        // 使用对应的SimpleDateFormat格式化日期对象为字符串
        return getsdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            // 使用对应的SimpleDateFormat解析字符串为日期对象
            return getsdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 如果解析失败，打印堆栈跟踪并返回当前日期
        return null;
    }
}
