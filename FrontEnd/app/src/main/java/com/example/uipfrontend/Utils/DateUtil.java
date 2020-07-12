package com.example.uipfrontend.Utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;
    
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static Calendar calendar = Calendar.getInstance();

    /**
     * 获取当前年月日
     * @return yyyy-MM-dd
     * 2019-06-05
     */
    public static String getDate() {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

    /**
     * @param format 使用指定格式获取时间
     * @return 时间字符串
     */
    public static String getDate(String format) {
        @SuppressLint("SimpleDateFormat") 
        SimpleDateFormat simple = new SimpleDateFormat(format);
        return simple.format(calendar.getTime());
    }

    /**
     * @return yyyy-MM-dd HH:mm
     * 2019-06-05 15:14
     */
    public static String getDateWithoutSecond() {
        return f.format(calendar.getTime());
    }

    /**
     * 获取当前时间
     * @return
     *  yyyy-MM-dd HH:mm:ss
     *  2019-06-05 15:14:36
     */
    public static String getFullDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(calendar.getTime());
    }

    /**
     * 距离今天多久
     * @param strDate 时间字符串
     * @return 超过一天返回原时间字符串
     */
    public static String fromToday(String strDate) {
        Date date;
        try {
            date = f.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return strDate;
        }
        
        if (date != null) {
            calendar.setTime(date);
            long time = date.getTime() / 1000;
            long now = new Date().getTime() / 1000;
            long ago = now - time;
            if (ago <= ONE_MINUTE) {
                return "刚刚";
            } else if (ago <= ONE_HOUR)
                return ago / ONE_MINUTE + "分钟前";
            else if (ago <= ONE_DAY)
                return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                        + "分钟前";
            else return strDate;
        } else {
            return strDate;
        }
    }

    public static String getYear() {
        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getMonth() {
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "";
    }

    public static String getDay() {
        return calendar.get(Calendar.DATE) + "";
    }

    public static String get24Hour() {
        return calendar.get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getMinute() {
        return calendar.get(Calendar.MINUTE) + "";
    }

    public static String getSecond() {
        return calendar.get(Calendar.SECOND) + "";
    }
}
