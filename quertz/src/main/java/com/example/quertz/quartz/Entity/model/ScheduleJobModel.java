package com.example.quertz.quartz.Entity.model;

import lombok.Data;

/**
 * @program: demo
 * @description: ScheduleJobModel
 * @author: cy
 * @create: 2019-06-06 14:33
 **/
@Data
public class ScheduleJobModel {
    private Integer id;

    private String groupName;

    private String jobName;

    private String cron;
}
