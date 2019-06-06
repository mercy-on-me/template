package com.example.quertz.schedule.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @program: demo
 * @description: springboot 自带的 schedule
 * @author: cy
 * @create: 2019-06-06 14:06
 **/
//@Service
public class Task1 {
    @Scheduled(cron="0/2 * * * * ?")
    public void task(){
        System.out.println("task - 2秒执行一次");
    }
}
