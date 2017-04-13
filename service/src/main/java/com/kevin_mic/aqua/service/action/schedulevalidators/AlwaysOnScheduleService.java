package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;

import java.util.ArrayList;
import java.util.List;

public class AlwaysOnScheduleService implements ScheduleServiceInterface<AlwaysOnSchedule> {
    @Override
    public void validate(String fieldName, AlwaysOnSchedule schedule) {
        // Nothing to validate
    }

    @Override
    public List<ScheduleJob> getJobs(int actionId, AlwaysOnSchedule schedule) {
        return new ArrayList<>();
    }
}

