package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.schedule.HourMinute;
import com.kevin_mic.aqua.model.schedule.RunSchedule;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.collections.CollectionUtils;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class RunScheduleService implements ScheduleServiceInterface<RunSchedule> {
    public static final int MAX_RUN = 6;

    private final OnOffScheduleService onOffScheduleService;

    @Inject
    public RunScheduleService(OnOffScheduleService onOffScheduleService) {
        this.onOffScheduleService = onOffScheduleService;
    }

    @Override
    public void validate(String fieldName, RunSchedule schedule) {
        onOffScheduleService.validateDays(fieldName, schedule.getDays());
        validateRun(fieldName, schedule);

        Collections.sort(schedule.getRunTimes());
        validateNoDuplicateRunTimes(fieldName, schedule);
    }

    @Override
    public List<ScheduleJob> getJobs(int actionId, RunSchedule schedule) {
        throw new RuntimeException("TODO");
    }

    void validateRun(String fieldName, RunSchedule schedule) {
        if (CollectionUtils.isEmpty(schedule.getRunTimes())) {
            throw new AquaException(ErrorType.ScheduleRunTimesRequired, fieldName);
        }
        if (schedule.getRunTimes().size() > MAX_RUN) {
            // I assume that if you have more than MAX_ON_OFF then you are abusing this feature
            // and probably should be using an interval schedule
            throw new AquaException(ErrorType.ScheduleRunTimesMaximumExceeded, fieldName);
        }
        schedule.getRunTimes().forEach(hm -> onOffScheduleService.validateHourMinute(fieldName, hm));
    }

    void validateNoDuplicateRunTimes(String fieldName, RunSchedule schedule) {
        // Order the run times

        HourMinute lastTime = null;
        for (HourMinute runTime : schedule.getRunTimes()) {
            if (lastTime != null) {
                if (runTime.equals(lastTime)) {
                    throw new AquaException(ErrorType.ScheduleRun_DuplicateRunTime, fieldName);
                }
            }
            lastTime = runTime;
        }
    }
}

