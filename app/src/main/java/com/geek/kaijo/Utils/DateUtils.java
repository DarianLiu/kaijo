package com.geek.kaijo.Utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LiuLi on 2018/9/7.
 */

public class DateUtils {

    public static String dateString1 = "yyyy-MM-dd HH:mm:ss";
    public static String dateString2 = "yyyy年MM月dd日 HH时mm分ss秒";
    public static String dateString3 = "yyyy-MM-dd";
    public static String dateString4 = "yyyy年MM月dd日";

    /**
     * 获取当前时间
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间年份
     *
     * @return Year：当前时间年份
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前时间月份
     *
     * @return Year：当前时间月份
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取当前时间日期
     *
     * @return Year：当前时间日期
     */
    public static int getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当前时间小时
     *
     * @return Year：当前时间日期
     */
    public static int getCurrentDateHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前时间分钟
     *
     * @return Year：当前时间日期
     */
    public static int getCurrentDateMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前年份的时间戳
     *
     * @return YearTimeStamp：当前年份的时间戳
     */
    public static long getCurrentYearTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970,1,1);
//        calendar.setTimeInMillis(getCurrentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        String dateStr = year + "年01月01日";
        return getStringToDate(dateStr, "yyyy年MM月dd日");
    }

    /**
     * 获取当前1970年份的时间戳
     *
     * @return YearTimeStamp：当前年份的时间戳
     */
    public static long get1970YearTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970,1,1);
        int year = calendar.get(Calendar.YEAR);
        String dateStr = year + "年01月01日";
        return getStringToDate(dateStr, "yyyy年MM月dd日");
    }
    /**
     * 字符串转为时间戳
     *
     * @param dateString 日期字符串
     * @param pattern    对应日期格式
     * @return 时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间戳转为字符串
     *
     * @param milSecond 日期时间戳
     * @param pattern   对应日期格式
     * @return 时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 时间戳转为字符串
     *
     * @param milSecond 日期时间戳
     * @param pattern   对应日期格式
     * @return 时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateToStringNonSecond(long milSecond, String pattern) {
        String str = getDateToString(milSecond, pattern);
        return str.substring(0, str.lastIndexOf(":"));
    }


    @SuppressLint("SimpleDateFormat")
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
//            format = "yyyy-MM-dd HH:mm:ss";
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }
}
