package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.ActionInterface;
import com.kevin_mic.aqua.model.actions.metadata.AllowedDeviceTypes;
import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.DeviceType;
import com.kevin_mic.aqua.model.types.ScheduleType;
import com.kevin_mic.aqua.service.AquaException;
import com.kevin_mic.aqua.service.ErrorType;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.inject.Inject;
import javax.swing.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleServiceFactory {
    private final OnOffScheduleService onOffScheduleService;
    private final IntervalScheduleService intervalScheduleService;
    private final AlwaysOnScheduleService alwaysOnScheduleService;
    private final RunScheduleService runScheduleService;

    @Inject
    public ScheduleServiceFactory(OnOffScheduleService onOffScheduleService, IntervalScheduleService intervalScheduleService, AlwaysOnScheduleService alwaysOnScheduleService, RunScheduleService runScheduleService) {
        this.onOffScheduleService = onOffScheduleService;
        this.intervalScheduleService = intervalScheduleService;
        this.alwaysOnScheduleService = alwaysOnScheduleService;
        this.runScheduleService = runScheduleService;
    }

    public void validateSchedules(ActionInterface action) {
        List<Field> scheduleFields = FieldUtils.getFieldsListWithAnnotation(action.getClass(), AllowedScheduleTypes.class);

        for (Field field : scheduleFields) {
            try {
                AllowedScheduleTypes[] annotationsByType = field.getAnnotationsByType(AllowedScheduleTypes.class);
                ScheduleInterface schedule = (ScheduleInterface) new PropertyDescriptor(field.getName(), action.getClass()).getReadMethod().invoke(action);
                validateSchedule(field.getName(), schedule, annotationsByType[0].value());
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

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
