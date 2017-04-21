package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleServiceFactory {
    private final OnOffScheduleService onOffScheduleService;
    private final IntervalScheduleService intervalScheduleService;
    private final AlwaysOnScheduleService alwaysOnScheduleService;
    private final RunScheduleService runScheduleService;
    private final Scheduler scheduler;

    @Inject
    public ScheduleServiceFactory(OnOffScheduleService onOffScheduleService, IntervalScheduleService intervalScheduleService, AlwaysOnScheduleService alwaysOnScheduleService, RunScheduleService runScheduleService, Scheduler scheduler) {
        this.onOffScheduleService = onOffScheduleService;
        this.intervalScheduleService = intervalScheduleService;
        this.alwaysOnScheduleService = alwaysOnScheduleService;
        this.runScheduleService = runScheduleService;
        this.scheduler = scheduler;
    }

    public void validateSchedules(ActionInterface action) {
        ScheduleInterface schedule = action.findSchedule();
        validateSchedule("schedule", schedule, getAllowedScheduleTypes(action.getClass()));
    }

    public <T extends ActionInterface> ScheduleType[] getAllowedScheduleTypes(Class<T> clazz) {
        AllowedScheduleTypes[] annotationsByType = clazz.getAnnotationsByType(AllowedScheduleTypes.class);
        if (annotationsByType != null && annotationsByType.length > 0) {
            return annotationsByType[0].value();
        }
        return null;
    }

    public <T extends ActionInterface> void deleteSchedules(int actionId) {
        try {
            List<JobKey> jobKeys = new ArrayList<>();
            jobKeys.addAll(scheduler.getJobKeys(GroupMatcher.groupEquals(getActionGroupName(actionId))));
            for (JobKey jobKey : jobKeys) {
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }


    public <T extends ActionInterface> void updateSchedules(ActionInterface before, ActionInterface updated) {

//        ScheduleInterface beforeSchedule = before != null? getScheduleFromAction(before, null) : null;
        ScheduleInterface afterSchedule = updated.findSchedule();

//        if (!Objects.equals(beforeSchedule, afterSchedule)) {
            deleteSchedules(updated.getActionId());
            createSchedules(updated);
//        }
    }

    private <T extends ActionInterface> void createSchedules(ActionInterface action) {
        int actionId = action.getActionId();
        try {
            ScheduleInterface schedule = action.findSchedule();
            if (schedule != null) {
                List<ScheduleJob> jobs = getServiceForType(schedule.getType()).getJobs(actionId, schedule);

                for (ScheduleJob job : jobs) {
                    for (Trigger trigger : job.getTriggers()) {
                        scheduler.scheduleJob(job.getJobDetail(), trigger);
                    }
                }
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getActionGroupName(int actionId) {
        return "action_" + actionId;
    }


    private void validateSchedule(String fieldName, ScheduleInterface schedule, ScheduleType[] allowedScheduleTypes) {
        if (schedule == null && (allowedScheduleTypes == null || allowedScheduleTypes.length == 0)) {
            return;
        }

        if (schedule == null) {
            throw new AquaException(ErrorType.ScheduleRequired, fieldName);
        }

        Set<ScheduleType> allowedSet = new HashSet<>();
        allowedSet.addAll(Arrays.asList(allowedScheduleTypes));
        if (!allowedSet.contains(schedule.getType())) {
            throw new AquaException(ErrorType.InvalidScheduleTypeForField, fieldName);
        }

        getServiceForType(schedule.getType()).validate(fieldName, schedule);
    }


    private ScheduleServiceInterface getServiceForType(ScheduleType type) {
        switch (type) {
            case OnOff:
                return onOffScheduleService;
            case AlwaysOn:
                return alwaysOnScheduleService;
            case Interval:
                return intervalScheduleService;
            case Run:
                return runScheduleService;
            default:
                throw new RuntimeException("ScheduleType Not Handled - " + type);
        }
    }
}
