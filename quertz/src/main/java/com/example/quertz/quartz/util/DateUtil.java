package com.example.quertz.quartz.util;

/**
 * @program: demo
 * @description: DateUtil
 * @author: cy
 * @create: 2019-06-06 14:36
 **/

public class DateUtil {
    /**
     * 得到当前时间戳
     * @return
     */
    public static Long getCurrentTimeStamp() {
        long timeMillis = System.currentTimeMillis();
        return timeMillis;
    }
}
