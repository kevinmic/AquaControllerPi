package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;

import java.util.List;

public class AlwaysOnScheduleService implements ScheduleServiceInterface<AlwaysOnSchedule> {
    @Override
    public void validate(String fieldName, AlwaysOnSchedule schedule) {
        // Nothing to validate
    }

    @Override
    public List<ScheduleJob> getJobs(int actionId, ScheduleInterface schedule) {
        return null;
    }
}

