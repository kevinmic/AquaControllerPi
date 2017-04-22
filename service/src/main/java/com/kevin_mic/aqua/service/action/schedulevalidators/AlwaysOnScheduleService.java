package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.google.common.collect.Lists;
import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class AlwaysOnScheduleService implements ScheduleServiceInterface<AlwaysOnSchedule> {
    private final OnOffScheduleService onOffScheduleService;

    @Inject
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
        return Lists.newArrayList(job);
    }
}

