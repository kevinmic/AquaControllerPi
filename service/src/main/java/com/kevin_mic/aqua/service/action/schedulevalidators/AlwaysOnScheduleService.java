package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.AlwaysOnSchedule;

public class AlwaysOnScheduleService implements ScheduleServiceInterface<AlwaysOnSchedule> {
    @Override
    public void validate(String fieldName, AlwaysOnSchedule schedule) {
        // Nothing to validate
    }
}

