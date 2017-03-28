package com.kevin_mic.aqua.model.schedule;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kevin_mic.aqua.model.types.ScheduleType;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name=ScheduleInterface.ALWAYS_ON, value = AlwaysOnSchedule.class),
        @JsonSubTypes.Type(name=ScheduleInterface.ON_OFF, value = OnOffSchedule.class),
        @JsonSubTypes.Type(name=ScheduleInterface.INTERVAL, value = IntervalSchedule.class),
})
public interface ScheduleInterface {
    public static final String ALWAYS_ON = "AlwaysOn";
    public static final String ON_OFF = "OnOff";
    public static final String INTERVAL = "Interval";

    public ScheduleType getType();
}
