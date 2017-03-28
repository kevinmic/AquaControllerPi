package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.IntervalSchedule;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;

public class IntervalScheduleService implements ScheduleServiceInterface<IntervalSchedule> {
    @Override
    public void validate(String fieldName, IntervalSchedule schedule) {
        if (schedule.getRepeatInterval() <= 0) {
            throw new AquaException(ErrorType.IntervalSchedule_InvalidRepeatInterval, fieldName);
        }
        if (schedule.getTimeUnit() == null) {
            throw new AquaException(ErrorType.IntervalSchedule_TimeUnitRequired, fieldName);
        }
    }
}

