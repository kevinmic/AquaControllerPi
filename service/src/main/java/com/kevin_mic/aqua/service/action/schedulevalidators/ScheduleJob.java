package com.kevin_mic.aqua.service.action.schedulevalidators;

import lombok.Data;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;

@Data
public class ScheduleJob {
    JobDetail jobDetail;
    List<Trigger> triggers;
}
