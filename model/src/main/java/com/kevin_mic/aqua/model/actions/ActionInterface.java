package com.kevin_mic.aqua.model.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kevin_mic.aqua.model.types.ActionType;

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

}
