package com.example.quertz.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @program: demo
 * @description: 任务类
 * @author: cy
 * @create: 2019-06-06 14:24
 **/
@Slf4j
public class ScheduleQuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String group = jobExecutionContext.getJobDetail().getJobDataMap().get("group").toString();
        String name = jobExecutionContext.getJobDetail().getJobDataMap().get("name").toString();
        log.info("执行了task...group:{}, name:{}", group, name);
    }
}
