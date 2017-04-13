package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.ScheduleInterface;

import java.util.List;

public interface ScheduleServiceInterface <T extends ScheduleInterface> {
    void validate(String fieldName, T schedule);

    List<ScheduleJob> getJobs(int actionId, ScheduleInterface schedule);
}
