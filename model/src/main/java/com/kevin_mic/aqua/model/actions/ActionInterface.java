package com.kevin_mic.aqua.model.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kevin_mic.aqua.model.actions.metadata.Schedule;
import com.kevin_mic.aqua.model.schedule.ScheduleInterface;
import com.kevin_mic.aqua.model.types.ActionType;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name=ActionInterface.DOSING_STEPPER, value = DosingStepper.class),
        @JsonSubTypes.Type(name=ActionInterface.DOSING_TIMED, value = DosingTimed.class),
        @JsonSubTypes.Type(name=ActionInterface.LIGHT_SCHEDULE, value = LightSchedule.class),
        @JsonSubTypes.Type(name=ActionInterface.PUMP_SCHEDULE, value = PumpSchedule.class),
        @JsonSubTypes.Type(name=ActionInterface.TOP_OFF, value = TopOff.class),
})
public interface ActionInterface {
    public static final String DOSING_STEPPER = "DosingStepper";
    public static final String DOSING_TIMED = "DosingTimed";
    public static final String LIGHT_SCHEDULE = "LightSchedule";
    public static final String PUMP_SCHEDULE = "PumpSchedule";
    public static final String TOP_OFF = "TopOff";

    String getName();
    void setName(String name);

    int getActionId();
    void setActionId(int actionId);

    ActionType getType();

    default ScheduleInterface findSchedule() {
        List<Field> fieldsListWithAnnotation = FieldUtils.getFieldsListWithAnnotation(this.getClass(), Schedule.class);
        if (fieldsListWithAnnotation.size() > 0) {
            try {
                return (ScheduleInterface) new PropertyDescriptor(fieldsListWithAnnotation.get(0).getName(), this.getClass()).getReadMethod().invoke(this);
            } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
