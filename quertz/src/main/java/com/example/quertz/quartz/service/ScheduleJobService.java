package com.example.quertz.quartz.service;

import com.example.quertz.quartz.Entity.model.ScheduleJobModel;
import com.example.quertz.quartz.Entity.po.ScheduleJobPo;
import com.example.quertz.quartz.dao.ScheduleJobDaoRepository;
import com.example.quertz.quartz.job.ScheduleQuartzJob;
import com.example.quertz.quartz.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * @program: demo
 * @description: ScheduleJobService
 * @author: cy
 * @create: 2019-06-06 14:30
 **/
@Slf4j
@Service
public class ScheduleJobService {
    // 获取工厂类
    private StdSchedulerFactory sf = new StdSchedulerFactory();

    @Autowired
    private ScheduleJobDaoRepository scheduleJobDaoRepository;

    // 项目重启后，初始化原本已经运行的定时任务
    @PostConstruct
    public void init(){
        List<ScheduleJobPo> poList = scheduleJobDaoRepository.findAllByStatus(0);
        poList.forEach(po -> {
            startScheduleByInit(po);
        });
    }

    /**
     * 初始化时开启定时任务
     */
    private void startScheduleByInit(ScheduleJobPo po){
        try {
            Scheduler scheduler = sf.getScheduler();
            startJob(scheduler, po.getGroupName(), po.getJobName(), po.getCron());
            scheduler.start();
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }

    /**
     * 开启定时任务
     * @param model
     */
    public void startSchedule(ScheduleJobModel model) {
        if (StringUtils.isEmpty(model.getGroupName()) || StringUtils.isEmpty(model.getJobName()) || StringUtils.isEmpty(model.getCron())){
            throw new RuntimeException("参数不能为空");
        }
        List<ScheduleJobPo> poList = scheduleJobDaoRepository.findByGroupNameAndJobNameAndStatus(model.getGroupName(), model.getJobName(), 0);
        if (!ObjectUtils.isEmpty(poList)){
            throw new RuntimeException("group和job名称已存在");
        }
        try {
            Scheduler scheduler = sf.getScheduler();
            startJob(scheduler, model.getGroupName(), model.getJobName(), model.getCron());
            scheduler.start();
            ScheduleJobPo scheduleJobPo = new ScheduleJobPo();
            scheduleJobPo.setGroupName(model.getGroupName());
            scheduleJobPo.setJobName(model.getJobName());
            scheduleJobPo.setCron(model.getCron());
            scheduleJobPo.setStatus(0);
            scheduleJobPo.setCreateTime(DateUtil.getCurrentTimeStamp());
            scheduleJobPo.setModifiedTime(DateUtil.getCurrentTimeStamp());
            scheduleJobDaoRepository.save(scheduleJobPo);
        }catch (Exception e){
            log.error("exception:{}", e);
        }

    }

    /**
     * 更新定时任务
     * @param model
     */
    public void scheduleUpdateCorn(ScheduleJobModel model) {
        if (ObjectUtils.isEmpty(model.getId()) || ObjectUtils.isEmpty(model.getCron())){
            throw new RuntimeException("定时任务不存在");
        }
        try {
            ScheduleJobPo po = scheduleJobDaoRepository.findByIdAndStatus(model.getId(), 0);
            // 获取调度对象
            Scheduler scheduler = sf.getScheduler();
            // 获取触发器
            TriggerKey triggerKey = new TriggerKey(po.getJobName(), po.getGroupName());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            String oldTime = cronTrigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(model.getCron())) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(model.getCron());
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(po.getJobName(), po.getGroupName())
                        .withSchedule(cronScheduleBuilder).build();
                // 更新定时任务
                scheduler.rescheduleJob(triggerKey, trigger);
                po.setCron(model.getCron());
                // 更新数据库
                scheduleJobDaoRepository.save(po);
            }
        }catch (Exception e){
            log.info("exception:{}", e);
        }

    }
    /**
     * 任务 - 暂停
     */
    public void schedulePause(ScheduleJobModel model) {
        if (ObjectUtils.isEmpty(model.getId())){
            throw new RuntimeException("定时任务不存在");
        }
        ScheduleJobPo po = scheduleJobDaoRepository.findByIdAndStatus(model.getId(), 0);
        if (ObjectUtils.isEmpty(po)){
            throw new RuntimeException("定时任务不存在");
        }
        try {
            Scheduler scheduler = sf.getScheduler();
            JobKey jobKey = new JobKey(po.getJobName(), po.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.pauseJob(jobKey);
            po.setStatus(2);
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }
    /**
     * 任务 - 恢复
     */

    public void scheduleResume(ScheduleJobModel model) {
        if (ObjectUtils.isEmpty(model.getId())){
            throw new RuntimeException("定时任务不存在");
        }
        ScheduleJobPo po = scheduleJobDaoRepository.findByIdAndStatus(model.getId(), 2);
        if (ObjectUtils.isEmpty(po)){
            throw new RuntimeException("定时任务不存在");
        }
        try {
            Scheduler scheduler = sf.getScheduler();
            JobKey jobKey = new JobKey(po.getJobName(), po.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.resumeJob(jobKey);
            po.setStatus(0);
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }
    /**
     * 任务 - 删除一个定时任务
     */
    public void scheduleDelete(ScheduleJobModel model) {
        if (ObjectUtils.isEmpty(model.getId())){
            throw new RuntimeException("定时任务不存在");
        }
        ScheduleJobPo po = scheduleJobDaoRepository.findByIdAndStatus(model.getId(), 0);
        if (ObjectUtils.isEmpty(po)){
            throw new RuntimeException("定时任务不存在");
        }
        try {
            Scheduler scheduler = sf.getScheduler();
            JobKey jobKey = new JobKey(po.getJobName(), po.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.deleteJob(jobKey);
            po.setStatus(1);
            scheduleJobDaoRepository.save(po);
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }

    /**
     * 删除所有定时任务
     */
    public void scheduleDeleteAll() {
        try {
            Scheduler scheduler = sf.getScheduler();
            // 获取有所的组
            List<String> jobGroupNameList = scheduler.getJobGroupNames();
            for (String jobGroupName : jobGroupNameList) {
                GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(jobGroupName);
                Set<JobKey> jobKeySet = scheduler.getJobKeys(jobKeyGroupMatcher);
                for (JobKey jobKey : jobKeySet) {
                    String jobName = jobKey.getName();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    if (jobDetail == null)
                        return;
                    scheduler.deleteJob(jobKey);
                    // 更新数据库
                    List<ScheduleJobPo> poList = scheduleJobDaoRepository.findByGroupNameAndJobNameAndStatus(jobGroupName, jobName, 0);
                    poList.forEach(po -> {
                        po.setStatus(1);
                        scheduleJobDaoRepository.save(po);
                    });
                    log.info("group:{}, job:{}", jobGroupName, jobName);
                }
            }
        }catch (Exception e){
            log.error("exception:{}", e);
        }
    }
    // 开启任务
    private void startJob(Scheduler scheduler, String group, String name, String cron) throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // 在map中可传入自定义参数，在job中使用
        JobDataMap map = new JobDataMap();
        map.put("group", group);
        map.put("name", name);
        // JobDetail 是具体Job实例
        JobDetail jobDetail = JobBuilder.newJob(ScheduleQuartzJob.class).withIdentity(name, group)
                .usingJobData(map)
                .build();
        // 基于表达式构建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        // CronTrigger表达式触发器 继承于Trigger
        // TriggerBuilder 用于构建触发器实例
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}
