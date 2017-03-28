package com.kevin_mic.aqua.service.action.schedulevalidators;

import com.kevin_mic.aqua.model.actions.metadata.AllowedScheduleTypes;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
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
import java.util.List;

public class ScheduleServiceFactory {
    private final OnOffScheduleService onOffScheduleService;
    private final IntervalScheduleService intervalScheduleService;
    private final AlwaysOnScheduleService alwaysOnScheduleService;

    @Inject
    public ScheduleServiceFactory(OnOffScheduleService onOffScheduleService, IntervalScheduleService intervalScheduleService, AlwaysOnScheduleService alwaysOnScheduleService) {
        this.onOffScheduleService = onOffScheduleService;
        this.intervalScheduleService = intervalScheduleService;
        this.alwaysOnScheduleService = alwaysOnScheduleService;
    }

    public void validateSchedules(Action action) {
        List<Field> scheduleFields = FieldUtils.getFieldsListWithAnnotation(action.getClass(), AllowedScheduleTypes.class);

        for (Field field : scheduleFields) {
            try {
                ScheduleInterface schedule = (ScheduleInterface) new PropertyDescriptor(field.getName(), action.getClass()).getReadMethod().invoke(action);
                validateSchedule(field.getName(), schedule);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void validateSchedule(String fieldName, ScheduleInterface schedule) {
        if (schedule == null) {
            throw new AquaException(ErrorType.ScheduleRequired, fieldName);
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
            default:
                throw new RuntimeException("ScheduleType Not Handled - " + type);
        }
    }
}
