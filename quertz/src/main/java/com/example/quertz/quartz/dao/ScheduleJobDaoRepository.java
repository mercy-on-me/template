package com.example.quertz.quartz.dao;

import com.example.quertz.quartz.Entity.po.ScheduleJobPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @program: demo
 * @description: ScheduleJobDaoRepository
 * @author: cy
 * @create: 2019-06-06 14:28
 **/
public interface ScheduleJobDaoRepository extends JpaRepository<ScheduleJobPo, Integer> {

    public ScheduleJobPo findByIdAndStatus(Integer id, Integer status);

    public List<ScheduleJobPo> findAllByStatus(Integer status);

    public List<ScheduleJobPo> findByGroupNameAndJobNameAndStatus(String groupName, String jobName, Integer status);

    public List<ScheduleJobPo> findAllByStatusInOrderByCreateTimeDesc(List<Integer> statusList);
}
