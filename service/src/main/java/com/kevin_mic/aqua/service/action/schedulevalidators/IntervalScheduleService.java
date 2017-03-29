package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.IntervalSchedule;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;

public class IntervalScheduleService implements ScheduleServiceInterface<IntervalSchedule> {
    public static final int MAX_MINUTE_REPEAT_INTERVAL = 60*3;
    public static final int MAX_HOUR_REPEAT_INTERVAL = 48;

    @Override
    public void validate(String fieldName, IntervalSchedule schedule) {
        if (schedule.getRepeatInterval() <= 0) {
            throw new AquaException(ErrorType.IntervalSchedule_InvalidRepeatInterval, fieldName);
        }
        if (schedule.getTimeUnit() == null) {
            throw new AquaException(ErrorType.IntervalSchedule_TimeUnitRequired, fieldName);
        }

        switch (schedule.getTimeUnit()) {
            case Minute:
                if (schedule.getRepeatInterval() > MAX_MINUTE_REPEAT_INTERVAL) {
                    // If your minutes are greater than max then you are probably abusing an interval schedule
                    throw new AquaException(ErrorType.IntervalSchedule_MaxMinuteRepeatIntervalExceeded, fieldName);
                }
                break;
            case Hour:
                if (schedule.getRepeatInterval() > MAX_HOUR_REPEAT_INTERVAL) {
                    // If your hours are greater than max then you are probably abusing an interval schedule
                    throw new AquaException(ErrorType.IntervalSchedule_MaxHourRepeatIntervalExceeded, fieldName);
                }
                break;
        }
    }
}

