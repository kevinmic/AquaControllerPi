package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.ScheduleInterface;

public interface ScheduleServiceInterface <T extends ScheduleInterface> {
    void validate(String fieldName, T schedule);
}
