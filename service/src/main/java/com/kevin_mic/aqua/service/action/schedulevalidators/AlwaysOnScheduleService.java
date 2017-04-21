package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import com.kevin_mic.aqua.service.jobs.OnOffJob;

import java.util.ArrayList;
import java.util.List;

import static com.kevin_mic.aqua.service.action.schedulevalidators.ScheduleServiceFactory.getActionGroupName;
import static org.quartz.JobBuilder.newJob;

public class AlwaysOnScheduleService implements ScheduleServiceInterface<AlwaysOnSchedule> {
    private final OnOffScheduleService onOffScheduleService;

    public AlwaysOnScheduleService(OnOffScheduleService onOffScheduleService) {
        this.onOffScheduleService = onOffScheduleService;
    }

    @Override
    public void validate(String fieldName, AlwaysOnSchedule schedule) {
        // Nothing to validate
    }

    @Override
    public List<ScheduleJob> getJobs(int actionId, AlwaysOnSchedule schedule) {
        ScheduleJob job = new ScheduleJob();
        job.setJobDetail(onOffScheduleService.createJob(actionId, true));
        job.setTriggers(Lists.newArrayList(onOffScheduleService.loadImmediateTrigger(actionId)));
        return new ArrayList<>();
    }
}

