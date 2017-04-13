package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.actions.metadata.Schedule;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import javax.inject.Inject;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
        String[] fieldName = new String[1];
        ScheduleInterface schedule = getScheduleFromAction(action, fieldName);
        validateSchedule(fieldName[0], schedule, getAllowedScheduleTypes(action.getClass()));
    }

    public ScheduleInterface getScheduleFromAction(ActionInterface action, String[] fieldName) {
        List<Field> scheduleFields = FieldUtils.getFieldsListWithAnnotation(action.getClass(), Schedule.class);
        if (scheduleFields.size() > 0) {
            Field field = scheduleFields.get(0);

            if (fieldName != null) {
                fieldName[0] = field.getName();
            }

            try {
                return (ScheduleInterface) new PropertyDescriptor(field.getName(), action.getClass()).getReadMethod().invoke(action);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public <T extends ActionInterface> ScheduleType[] getAllowedScheduleTypes(Class<T> clazz) {
        AllowedScheduleTypes[] annotationsByType = clazz.getAnnotationsByType(AllowedScheduleTypes.class);
        return annotationsByType[0].value();
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
        ScheduleInterface afterSchedule = getScheduleFromAction(updated, null);

//        if (!Objects.equals(beforeSchedule, afterSchedule)) {
            deleteSchedules(updated.getActionId());
            createSchedules(updated);
//        }
    }

    private <T extends ActionInterface> void createSchedules(ActionInterface action) {
        int actionId = action.getActionId();
        try {
            ScheduleInterface schedule = getScheduleFromAction(action, null);
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
