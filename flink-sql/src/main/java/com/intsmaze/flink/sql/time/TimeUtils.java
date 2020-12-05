package com.intsmaze.flink.sql.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2020/10/15 18:33
 */
public class TimeUtils {

    public static Date strToDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        Date date = null;
        date = sdf.parse(dateStr);
        return date;
    }


    public static String getHHmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        String str = sdf.format(date);
        return str;
    }

    public static String getHHmmss(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        String str = sdf.format(new Date(time));
        return str;
    }

    public static String getSS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss SSS");
        String str = sdf.format(date);
        return str;
    }

    public static String getSS(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss SSS");
        String str = sdf.format(new Date(time));
        return str;
    }


    public static String getMm(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String str = sdf.format(new Date(time));
        return str;
    }

    public static String getWithoutMm(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String str = sdf.format(new Date(time));
        return str;
    }

    public static void main(String[] args) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String str = sdf.format(new Date());
        System.out.println(str);

        System.out.println(Integer.valueOf("00"));


    }
}
