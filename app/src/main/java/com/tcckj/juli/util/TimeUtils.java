package com.tcckj.juli.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/4.
 */

public class TimeUtils {

    /**
     * 获取两个日期的时间差
     */
    public static int getTimeInterval(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int interval = 0;
        try {
            Date currentTime = new Date();// 获取现在的时间
            Date beginTime = dateFormat.parse(data);
            interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));// 时间差 单位秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return interval;
    }


    /**
     * 设定显示文字
     */
    public static String getInterval(int time) {
        String txt = null;
        if (time >= 0) {
            long day = time / (24 * 3600);// 天
            long hour = time % (24 * 3600) / 3600;// 小时
            long minute = time % 3600 / 60;// 分钟
            long second = time % 60;// 秒

            txt = " 距离现在还有：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
        } else {
            txt = "已过期";
        }
        return txt;
    }


    /**
     * 设定显示文字
     */
    public static String getIntervalTime(int time) {
        String txt = null;
        if (time >= 0) {
            long day = time / (24 * 3600);// 天
            long hour = time % (24 * 3600) / 3600;// 小时
            long minute = time % 3600 / 60;// 分钟
            long second = time % 60;// 秒

            //txt =" 距离现在还有：" + day + "天" + hour + "小时" + minute + "分" + second + "秒";
            txt = " 剩余时间：" + (day * 24 + hour) + ":" + minute + ":" + second;
        } else {
            txt = "已过期";
        }
        return txt;
    }


    //返回当前时间作为订单号
    public static String getTimeOrder() {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return time;
    }

    //返回当前时间
    public static String getTime() {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return time;
    }

    /*
    获取到指定时间的时间戳
     */
    public static String date2Date(String date) {
        try {
            return String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
