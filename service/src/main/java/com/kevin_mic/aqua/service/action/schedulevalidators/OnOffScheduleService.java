package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.HourMinute;
import com.kevin_mic.aqua.model.schedule.OnOffSchedule;
import com.kevin_mic.aqua.model.schedule.OnOffTime;
import com.kevin_mic.aqua.model.types.DayOfWeek;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.Set;

public class OnOffScheduleService implements ScheduleServiceInterface<OnOffSchedule> {
    private static final int MAX_ON_OFF = 6;

    @Override
    public void validate(String fieldName, OnOffSchedule schedule) {
        validateDays(fieldName, schedule.getDays());
        validateOnOff(fieldName, schedule);
        validateOnOffOrder(fieldName, schedule);
    }

    void validateOnOff(String fieldName, OnOffSchedule schedule) {
        if (CollectionUtils.isEmpty(schedule.getOnOffTimes())) {
            throw new AquaException(ErrorType.ScheduleOnOffTimesRequired, fieldName);
        }
        if (schedule.getOnOffTimes().size() > MAX_ON_OFF) {
            // I assume that if you have more than MAX_ON_OFF then you are abusing this feature
            // and probably should be using an interval schedule
            throw new AquaException(ErrorType.ScheduleOnOffTimesMaximumExceeded, fieldName);
        }
        if (schedule.getOnOffTimes().stream().filter(onOff -> !onOff.isValid()).findAny().isPresent()) {
            throw new AquaException(ErrorType.ScheduleOnOffInvalid, fieldName);
        }

        schedule.getOnOffTimes().forEach(onOff -> {
            validateHourMinute(fieldName, onOff.getOn());
            validateHourMinute(fieldName, onOff.getOff());
        });
    }

    public void validateHourMinute(String fieldName, HourMinute hourMinute) {
        if (hourMinute.getHour() < 0 || hourMinute.getHour() > 23) {
            throw new AquaException(ErrorType.InvalidHour, fieldName);
        }
        if (hourMinute.getMinute() < 0 || hourMinute.getMinute() > 59) {
            throw new AquaException(ErrorType.InvalidMinute, fieldName);
        }
    }

    void validateOnOffOrder(String fieldName, OnOffSchedule schedule) {
        // Sort by On time
        Collections.sort(schedule.getOnOffTimes());

        // Assumptions
        // 1. All ON OFF times will be in order
        // 2a. ON will always be before OFF
        // 2b. EXCEPT - we allow the last OFF to cross to be before ON (so we can cross the 24 hour barrier)

        HourMinute firstOn = null;
        HourMinute lastTime = null;
        for (int i = 0; i < schedule.getOnOffTimes().size(); i++) {
            OnOffTime onOffTime = schedule.getOnOffTimes().get(i);
            HourMinute on = onOffTime.getOn();
            HourMinute off = onOffTime.getOff();

            if (firstOn == null) {
                firstOn = on;
            }

            if (lastTime != null) {
                if (on.equals(lastTime)) {
                    throw new AquaException(ErrorType.ScheduleOnOff_On_SameAsOff, fieldName);
                }
                if (on.isBefore(lastTime)) {
                    throw new AquaException(ErrorType.ScheduleOnOff_On_ConflictsWithPreviousOff, fieldName);
                }
            }
            lastTime = on;

            if (off.equals(lastTime)) {
                throw new AquaException(ErrorType.ScheduleOnOffInvalid, fieldName);
            }
            else if (off.isBefore(lastTime)) {
                if (firstOn.isBefore(off)) {
                    throw new AquaException(ErrorType.ScheduleOnOff_Off_ConflictsWithFirstOn, fieldName);
                }
                if (i != schedule.getOnOffTimes().size() - 1) {
                    throw new AquaException(ErrorType.ScheduleOnOff_Off_OnlyTheLastOffCanBeBeforeOn, fieldName);
                }
            }
            lastTime = off;
        }
    }

    void validateDays(String fieldName, Set<DayOfWeek> days) {
        if (CollectionUtils.isEmpty(days)) {
            throw new AquaException(ErrorType.ScheduleDaysRequired, fieldName);
        }
        else if (days.contains(DayOfWeek.ALL_DAYS) && days.size() > 1) {
            throw new AquaException(ErrorType.ScheduleDays_AllDaysCannotBeWithWeekDays, fieldName);
        }
    }
}

